package com.sci4s.grpc.svc;

import static java.lang.String.format;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.flatbuffers.FlatBufferBuilder;
import com.sci4s.fbs.Data;
import com.sci4s.fbs.FlatJsonGrpc;
import com.sci4s.fbs.RetMsg;
import com.sci4s.fbs.hr.Login;
import com.sci4s.grpc.ErrConstance;
import com.sci4s.grpc.HRGIO;
import com.sci4s.grpc.MsaApiGrpc;
import com.sci4s.grpc.MsaHRGrpc;
import com.sci4s.grpc.SciRIO;
import com.sci4s.grpc.dto.GrpcParams;
import com.sci4s.grpc.dto.GrpcResp;
import com.sci4s.grpc.utils.GrpcParamUtil;
import com.sci4s.grpc.utils.JsonUtil;
import com.sci4s.utils.AES256Util;
import com.sci4s.utils.DateUtil;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class RpcChannel {
	
	protected static final boolean IS_TRACE_LEVEL = true;
	
	protected ManagedChannel channel  = null;
	protected long TIME_OUT = 5000;
	
	protected String channelName = null;// 호출되는 채널 서비스명
	protected Map<String,String> devChannelNames;//개발시점 서비스 URL 맵

	protected boolean isState = true;
	
	protected FlatJsonGrpc.FlatJsonBlockingStub flatStub;
	protected MsaApiGrpc.MsaApiBlockingStub protoStub;
	
	protected String grpcAddr = null;
	protected int grpcPort = 0;
	
	protected String caPemFile;
	protected String crtPemFile;
	protected String privateKeyFile;
	
	private String afterMsg;// 함수 호출 후, 리터되는 메시지
	
	public String getAfterMsg() {
		return afterMsg;
	}
	public void setAfterMsg(String afterMsg) {
		this.afterMsg = afterMsg;
	}
	private static SslContext getBuildSslContext(String caPemFile, String crtPemFile,
			String privateKeyFile) throws SSLException {
		SslContextBuilder builder = GrpcSslContexts.forClient();
		if (caPemFile != null) {
			builder.trustManager(new File(caPemFile));
		}
		if (crtPemFile != null && privateKeyFile != null) {
			builder.keyManager(new File(crtPemFile), new File(privateKeyFile));
		}
		return builder.build();
	}
	
	/**
	 * 
	 * @param type REQUIRE or ADDRESS or TARGET
	 * @param inx 시도횟수
	 * @return
	 * @throws SSLException
	 */
	private ManagedChannel getManagedChannel(String type, int inx) throws SSLException {
		if ("REQUIRE".equals(type)) {
			return NettyChannelBuilder.forAddress(this.grpcAddr, this.grpcPort)
	    		    .sslContext(getBuildSslContext(caPemFile,crtPemFile,privateKeyFile))
	    		    .negotiationType(NegotiationType.TLS)
	    		    .build();
		} else {			
			if ("ADDRESS".equals(type)) {
				return ManagedChannelBuilder.forAddress(this.grpcAddr, this.grpcPort)
		                .usePlaintext()
		                .directExecutor()
		                .maxInboundMessageSize(ErrConstance.GRPC_MAX_SIZE)
		                .build();

			} else {
				return ManagedChannelBuilder.forTarget(this.grpcAddr +":"+ this.grpcPort)
						.usePlaintext()
						.directExecutor()
						.maxInboundMessageSize(ErrConstance.GRPC_MAX_SIZE)
						.build();
			}
		}
	}
	
	public ManagedChannel getChannel() {
		return this.channel;
	}	
	
	public Object getBlockingStub(String type) {
		if ("PROTO".equals(type)) {
			return MsaApiGrpc.newBlockingStub(this.channel);
		} else {
			return FlatJsonGrpc.newBlockingStub(this.channel);
		}		
	}
	
	protected void tryOpenChannel(String url, String type, int inx) throws Exception {
		this.channel = getManagedChannel(type, inx);
	}
	
	/**
	 * 
	 * @param GRPC_URI
	 * @param type      - "ADDRESS/TARGER"
	 * @return
	 * @throws Exception
	 */
	public String openTargetChannel(String GRPC_URI, String type) throws Exception {
		// 로컬 개발용 URL Map을 확인한다.
		String[] url = GRPC_URI.split("\\:");
		this.grpcAddr = url[0];
		this.grpcPort = Integer.parseInt(url[1]);
		if (this.devChannelNames != null && this.devChannelNames.containsKey(this.channelName)) {
			this.grpcAddr = devChannelNames.get(this.channelName);
		}
		this.openChannel(this.grpcAddr, this.grpcPort, type);
		return this.afterMsg;
	}

	public ManagedChannel openChannel(String GRPC_URI, String type) throws Exception {
		String[] url = GRPC_URI.split("\\:");
		this.grpcAddr = url[0];
		this.grpcPort = Integer.parseInt(url[1]);
		
		if (this.devChannelNames != null && this.devChannelNames.containsKey(this.channelName)) {
			this.grpcAddr = devChannelNames.get(this.channelName);
		}
		return this.openChannel(this.grpcAddr, this.grpcPort, type);
	}
	/**
	 * 
	 * @param GRPC_URI
	 * @param type      - "ADDRESS/TARGER"
	 * @return
	 * @throws Exception
	 */
	private ManagedChannel openChannel(String grpcAddr_, int grpcPort_, String type_) throws Exception {
		this.afterMsg = null;
		try {		
			this.tryOpenChannel(grpcAddr_, type_, 1);

	        if (IS_TRACE_LEVEL && this.channel != null) {
	        	this.afterMsg = format("[INFO] %s managed channel isTerminated: %b, isShutdown: %b, state: %s", toString(), this.channel.isTerminated(), this.channel.isShutdown(), this.channel.getState(false).name());
	        }
	        if (this.channel == null || this.channel.isTerminated() || this.channel.isShutdown()) {
	            if (this.channel != null && this.channel.isTerminated()) {
	            	this.afterMsg = format("[WARN] %s managed channel was marked terminated", toString());
	            }
	            if (this.channel != null && this.channel.isShutdown()) {
	            	this.afterMsg = format("[WARN] %s managed channel was marked shutdown.", toString());
	            }
	            this.tryOpenChannel(grpcAddr_, type_, 2);
	            if (IS_TRACE_LEVEL && this.channel != null) {
	            	this.afterMsg = format("[INFO] %s managed channel isTerminated: %b, isShutdown: %b, state: %s", toString(), this.channel.isTerminated(), this.channel.isShutdown(), this.channel.getState(false).name());
		        }
	        }
	        if (this.afterMsg == null) {
	        	this.afterMsg = type_ +"://"+ grpcAddr_ +":"+ grpcPort_;
	        }
			return this.channel;
		} catch (Exception e){	
		    throw e;
		}
	}
	
	public Object callRPC(GrpcParams gprms, String type ,String resp) throws Exception {		
		if ("PROTO".equals(type)) {
			return callRPCProto(gprms, resp);
		} else {		
			return callRPCFlat(gprms, resp);
		}
	}
	
	public GrpcResp callRPCResponse(GrpcParams gprms, String type, String resp) throws Exception {
		GrpcResp grpcResp = new GrpcResp();
		try {
		if("FLAT".equals(type)) {
			RetMsg flatResp  = (RetMsg) callRPCFlat(gprms, "getResponse");			
			grpcResp.setErrCode((flatResp.errCode()==null?"0":flatResp.errCode()));
			grpcResp.setErrMsg((flatResp.errMsg()==null?"":flatResp.errMsg()));
			if (flatResp.results() != null && flatResp.results().length() > 0) {
				grpcResp.setResults(flatResp.results());
			}
		} else {
			SciRIO.RetMsg protoResp = (SciRIO.RetMsg) callRPCProto(gprms,"getResponse");
			grpcResp.setErrCode((protoResp.getErrCode()==null?"0":protoResp.getErrCode()));
			grpcResp.setErrMsg((protoResp.getErrMsg()==null?"":protoResp.getErrMsg()));
			if (protoResp.getResults() != null && protoResp.getResults().length() > 0) {
				grpcResp.setResults(protoResp.getResults());
			}
		}
		return grpcResp;
		} catch(Exception ex) {
			throw ex;
		} finally {
			if (grpcResp != null) grpcResp = null;
		}
	}
	
	public Object callRPCFlat(GrpcParams gprms, String resp) throws Exception {
		FlatBufferBuilder builder = new FlatBufferBuilder();
		try {			
			int dataOffset = Data.createData(builder
					, builder.createString("" + gprms.getpID())
					, builder.createString("" + gprms.getAgentID())
					, builder.createString("" + gprms.getCsKey())
					, builder.createString("" + gprms.getUserIP())
					, builder.createString("" + gprms.getServerIP())
					, builder.createString("" + gprms.getUserUID())
					, builder.createString("" + gprms.getBorgUID())
					, builder.createString("" + (gprms.getClang()==null?ErrConstance.CLANG:gprms.getClang()))
					, builder.createString("" + gprms.getData())
					, builder.createString("0")
					, builder.createString(""));
		
			builder.finish(dataOffset);        
		    Data request = Data.getRootAsData(builder.dataBuffer());

		    FlatJsonGrpc.FlatJsonBlockingStub stub = FlatJsonGrpc.newBlockingStub(this.channel);
			RetMsg response = stub.callRMsg(request);
			if (response.results() != null && response.results().length() > 0) {
				if ("getResponse".equals(resp)) {
					return response;
				} else if ("getResults".equals(resp)) {
					return response.results();
				} else {
					return this.getData(response.results(), gprms.getType(), response.errCode(), response.errMsg());
				}	
			} else {
				return null;
			}
		} catch(StatusRuntimeException ex) {
			throw new Exception(ex);
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object callRPCProto(GrpcParams gprms, String resp) throws Exception {
		try {
			SciRIO.Data request = SciRIO.Data.newBuilder()
				.setPID(gprms.getpID())
				.setData(gprms.getData())
				.setCsKey(gprms.getCsKey())
				.setUserIP(gprms.getUserIP())
				.setServerIP(gprms.getServerIP())
				.setUserUID(gprms.getUserUID())
				.setBorgUID(gprms.getBorgUID())
				.setAgentID(gprms.getAgentID())
				.setClang("" + (gprms.getClang()==null?ErrConstance.CLANG:gprms.getClang()))
				.build();

			MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(this.channel);
			
			//logger.debug("PROTOConnector.stub.callRMsg -> gprms.getData() :::: "+ gprms.getData());
			SciRIO.RetMsg response = stub.callRMsg(request);
			if (response.getResults() != null && response.getResults().length() > 0) {
				if ("getResponse".equals(resp)) {
					return response;
				} else if ("getResults".equals(resp)) {
					return response.getResults();
				} else {
					return this.getData(response.getResults(), gprms.getType(), response.getErrCode(), response.getErrMsg());
				}
			} else {
				return null;
			}		
		} catch(StatusRuntimeException ex) {
			throw new Exception(ex);
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object callRPCMapProto(GrpcParams gprms, Map<String, String> grpcMap, String resp) throws Exception {
		try {
			SciRIO.Data request = SciRIO.Data.newBuilder()
				.setPID(gprms.getpID())
				.setCsKey(gprms.getCsKey())
				.setUserIP(gprms.getUserIP())
				.setServerIP(gprms.getServerIP())
				.setUserUID(gprms.getUserUID())
				.setBorgUID(gprms.getBorgUID())
				.setAgentID(gprms.getAgentID())
				.setClang("" + (gprms.getClang()==null?ErrConstance.CLANG:gprms.getClang()))
				.putAllParams(grpcMap)
				.build();
			MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(this.channel);
			//logger.debug("PROTOConnector.stub.callRMsg -> gprms.getData() :::: "+ gprms.getData());
			SciRIO.RetMsg response = stub.callRMsg(request);
			if (response.getResults() != null && response.getErrCode().equals("0")) {
				if ("getResults".equals(resp)) {
					return response.getResults();
				} else {
					return new ObjectMapper().readValue(response.getResults(), Map.class);
				}	
			} else {
				return null;
			}
		} catch(StatusRuntimeException ex) {
			throw new Exception(ex);
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object getEnableJob(GrpcParams gprms, String resp, String type) throws Exception {
		if ("PROTO".equals(type)) {
			return getEnableJobProto(gprms, resp);
		} else {		
			return getEnableJobFlat(gprms, resp);
		}
	}
	
	public Object getEnableJobFlat(GrpcParams gprms, String resp) throws Exception {
		FlatBufferBuilder builder = null;
		FlatJsonGrpc.FlatJsonBlockingStub fbsStub = null;
		Data fbsReq = null;
		RetMsg fbsResp = null;
		Map<String, Object> retMap = null;
		try {				
			builder = new FlatBufferBuilder();
			
			int dataOffset = Data.createData(builder
					, builder.createString("" + gprms.getpID())
					, builder.createString("" + gprms.getAgentID())
					, builder.createString("" + gprms.getCsKey())
					, builder.createString("" + gprms.getUserIP())
					, builder.createString("" + gprms.getServerIP())
					, builder.createString("" + gprms.getUserUID())
					, builder.createString("" + gprms.getBorgUID())
					, builder.createString("" + (gprms.getClang()==null?ErrConstance.CLANG:gprms.getClang()))
					, builder.createString("" + gprms.getData())
					, builder.createString("0")
					, builder.createString(""));

			builder.finish(dataOffset);        
			fbsReq  = Data.getRootAsData(builder.dataBuffer());
			fbsStub = FlatJsonGrpc.newBlockingStub(this.channel);
			fbsResp = fbsStub.getEnableJob(fbsReq);
			if (fbsResp != null && fbsResp.errCode().equals("0")) {
				if ("getResults".equals(resp)) {
					return fbsResp.results();
				} else {
					return com.sci4s.grpc.utils.JsonUtil.getJsonToObjMap(fbsResp.results(), false);
				}
			} else {
				return null;
			}
		} catch(StatusRuntimeException ex) {
			throw new Exception(ex);
		} catch(Exception ex) {
			throw ex;
		} finally {   	
	    	if (retMap   != null) { retMap = null; }
	    	if (gprms    != null) { gprms = null; }
	    	if (builder  != null) { builder = null; }
	    	if (fbsStub  != null) { fbsStub = null; }
	    	if (fbsReq   != null) { fbsReq = null; }
	    	if (fbsResp  != null) { fbsResp = null; }
	    }
	}
	
	public Object getEnableJobProto(GrpcParams gprms, String resp) throws Exception {		
		Map<String, Object> retMap = null;
		MsaApiGrpc.MsaApiBlockingStub protoStub = null;
		SciRIO.Data protoReq = null;
		SciRIO.RetMsg protoResp = null;
		try {	
			protoReq  = SciRIO.Data.newBuilder()
					.setPID(gprms.getpID())
					.setCsKey(gprms.getCsKey())
					.setData(gprms.getData())
					.setUserIP(gprms.getUserIP())
					.setServerIP(gprms.getServerIP())
					.setUserUID(gprms.getUserUID())
					.setBorgUID(gprms.getBorgUID())
					.setAgentID(gprms.getAgentID())
					.setClang((gprms.getClang()==null?ErrConstance.CLANG:gprms.getClang()))
	//				.putAllParams(grpcMap)
					.build();
			
			protoStub = MsaApiGrpc.newBlockingStub(this.channel);
			protoResp = protoStub.getEnableJob(protoReq);
			if (protoResp != null && protoResp.getErrCode().equals("0")) {
				if ("getResults".equals(resp)) {
					return protoResp.getResults();
				} else {
					return com.sci4s.grpc.utils.JsonUtil.getJsonToObjMap(protoResp.getResults(), false);
				}
			} else {
				return null;
			}			
		} catch(StatusRuntimeException ex) {
			throw new Exception(ex);
		} catch(Exception ex) {
			throw ex;
		} finally {   	
	    	if (retMap   != null) { retMap = null; }
	    	if (gprms    != null) { gprms = null; }
	    	if (protoStub!= null) { protoStub = null; }
	    	if (protoReq != null) { protoReq = null; }
	    	if (protoResp!= null) { protoResp = null; }
	    }
	}
	
	public Object loginProcess(String loginKey, String resp, String type) throws Exception {
		if (this.afterMsg == null) {
        	this.afterMsg = type +"://"+ this.grpcAddr +":"+ this.grpcPort;
        }
		if ("PROTO".equals(type)) {
			return loginProcessProto(loginKey, resp);
		} else {		
			return loginProcessFlat(loginKey, resp);
		}
	}
	
	public Object loginProcessFlat(String loginKey, String resp) throws Exception {
		FlatBufferBuilder builder = null;
		com.sci4s.fbs.hr.FlatJsonGrpc.FlatJsonBlockingStub fbsStub = null;
		Data fbsResp = null;
		Login fbsLogin = null; 
		Map<String, Object> retMap = null;
		try {				
			builder = new FlatBufferBuilder();				
			int dataOffset = Login.createLogin(builder, builder.createString("" + loginKey));
			builder.finish(dataOffset); 
			
			fbsLogin = Login.getRootAsLogin(builder.dataBuffer());		
			fbsStub  = com.sci4s.fbs.hr.FlatJsonGrpc.newBlockingStub(this.channel);				
			fbsResp  = fbsStub.loginProcess(fbsLogin);			
			if (fbsResp != null) {
				if (fbsResp.errCode().equals("0")) {				
					if ("getData".equals(resp)) {
						return fbsResp.data();
					} else {
						return new ObjectMapper().readValue(fbsResp.data(), Map.class);
					}
				} else {
					if ("getMap".equals(resp)) {
						String tmpErr = fbsResp.errCode()+">>>"+(fbsResp.errMsg()==null || "null".equals(fbsResp.errMsg())?"System error":fbsResp.errMsg());
						retMap = new HashMap<String, Object>();
						retMap.put("FAIL", tmpErr);					
						return retMap;
					} else {
						return getErrMsg(fbsResp.errCode(), (fbsResp.errMsg()==null || "null".equals(fbsResp.errMsg())?"System error":fbsResp.errMsg()));
					}
				}
			} else {
				return fbsResp;
			}	
		} catch(Exception ex) {
			throw ex;
		} finally {   	
	    	if (fbsLogin != null) { fbsLogin = null; }
	    	if (builder  != null) { builder = null; }
	    	if (fbsStub  != null) { fbsStub = null; }
	    	if (fbsResp  != null) { fbsResp = null; }
	    	if (retMap   != null) { retMap = null; }	    	
	    }
	}
	
	public Object loginProcessProto(String loginKey, String resp) throws Exception {		
		MsaHRGrpc.MsaHRBlockingStub protoStub = null;
		SciRIO.Data protoResp = null;
		HRGIO.Login protoLogin  = null;
		Map<String, Object> retMap = null;
		try {				
			protoLogin= HRGIO.Login.newBuilder().setLoginKey(loginKey).build();		
			protoStub = MsaHRGrpc.newBlockingStub(this.channel);
			protoResp = protoStub.loginProcess(protoLogin);
			if (protoResp != null) {
				if (protoResp.getErrCode().equals("0")) {				
					if ("getData".equals(resp)) {
						return protoResp.getData();
					} else {
						return new ObjectMapper().readValue(protoResp.getData(), Map.class);
					}
				} else {
					if ("getMap".equals(resp)) {
						String tmpErr = protoResp.getErrCode()+">>>"+(protoResp.getErrMsg()==null || "null".equals(protoResp.getErrMsg())?"System error":protoResp.getErrMsg());
						retMap = new HashMap<String, Object>();
						retMap.put("FAIL", tmpErr);
						return retMap;
					} else {
						return getErrMsg(protoResp.getErrCode(), (protoResp.getErrMsg()==null || "null".equals(protoResp.getErrMsg())?"System error":protoResp.getErrMsg()));
					}
				}
			} else {
				return null;
			}
		} catch(StatusRuntimeException ex) {
			throw new Exception(ex);
		} catch(Exception ex) {
			throw ex;
		} finally {   	
	    	if (protoLogin!= null) { protoLogin = null; }
	    	if (protoStub != null) { protoStub = null; }
	    	if (protoResp != null) { protoResp = null; }
	    	if (retMap    != null) { retMap = null; }
	    }
	}
	
	protected Object getData(String retData, String type, String errCd, String errMsg) throws Exception {
		Object retObj  = null;
		try {
			if ("LIST".equals(type.toUpperCase())) {	
		        JSONArray jsonArr = (JSONArray)((JSONObject)new JSONParser().parse(retData)).get("results");
		        List<Map<String, Object>> retList = null;		        
	        	if (jsonArr != null && jsonArr.size() > 0) {	
	        		ObjectMapper objMapper = new ObjectMapper();
	        		retList = objMapper.readValue(jsonArr.toJSONString(), objMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
	        	} else {
	        		retList = new ArrayList<Map<String, Object>>();
	        	}
		        return retList;
			} else if ("GRID".equals(type.toUpperCase())) {	
				if (retData == null) {// 시스템 오류
					return this.getErrMsg(errCd, errMsg);
				} else {
					return retData;
				}
			} else if ("JSON".equals(type.toUpperCase())) {
				if (retData == null) {// 시스템 오류
					return this.getErrMsg(errCd, errMsg);
				} else {
					return retData;
				}
			} else if ("BEAN".equals(type.toUpperCase())) {
				return retObj;
			} else {
				return null;
			}
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	protected String getErrMsg(String errCd, String errMsg) throws Exception {
		if (errCd != null) {
			if ("0".equals(errCd) || "NO_DATA".equals(errCd)) {
				return "{errCode:\"NO_DATA\",errMsg:\"Not found\"}";
			} else {
				return "{errCode:\"ERR_9999\",errMsg:\""+ errMsg +"\"}";
			}
		} else {					
			return "{errCode:\"NO_DATA\",errMsg:\"Not found\"}";
		}
	}	
	
	public void closeChannel() throws Exception {
		this.afterMsg = null;
		//this.channel.shutdown().awaitTermination(this.TIME_OUT, TimeUnit.SECONDS);
		
		if (!this.channel.isShutdown()) {
			try {
				this.channel.shutdown();
				if (!this.channel.awaitTermination(this.TIME_OUT, TimeUnit.MICROSECONDS)) {
					this.afterMsg = format("[WARN] Timed out gracefully shutting down connection: %s", this.channel);
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
				throw e;
			}
		}
		// Forceful shut down if still not terminated.
		if (!this.channel.isTerminated()) {
			try {
				this.afterMsg = format("[INFO] Timed out forcefully shutting down connection: %s", this.channel);
				this.channel.shutdownNow();
				if (!this.channel.awaitTermination(this.TIME_OUT, TimeUnit.MICROSECONDS)) {
					System.out.println(format("[WARN] Timed out forcefully shutting down connection: %s", this.channel));
				}
			} catch (Exception e) {
				Thread.currentThread().interrupt();
				this.afterMsg = format("[ERROR] Timed out forcefully shutting down connection: %s", e);
				throw e;
			}
		}
		/*
		try {
			this.channel.shutdown();
		    if (!this.channel.awaitTermination(this.TIME_OUT, TimeUnit.MICROSECONDS)) {
		    	this.channel.shutdownNow();
		    }
		} catch (Exception e){
			logger.error("Unexpected exception while waiting for channel shutdownNow", e);
			//throw e;
		} 
		*/
	}
	
	public Map<String, Object> callLoginI(String bufferType, String loginKey) throws Exception {
		
		Map<String, Object> retMap  = null;
		Map<String, Object> dataMap = null;
		String errMsg = null;
		AES256Util aes256 = new AES256Util();
		try {
			retMap = (Map<String, Object>) this.loginProcess(aes256.encryptII(loginKey), "getMap", bufferType);
			if (retMap != null) {
				if (retMap.containsKey("FAIL")) {
					// SECU001>>>{"errCode":"E9999","errMsg":"[8000]your id is wrong or there is no your organization!"}
					errMsg = ""+ retMap.get("FAIL");
					if (errMsg.indexOf("[8000]") >= 0) {
						errMsg = "[8000]your id is wrong or there is no your organization!";
					} else if (errMsg.indexOf("[8001]") >= 0) {
						errMsg = "[8001]your password is wrong!";
					}
				} else {
					errMsg  = GrpcParamUtil.getRetMsg(retMap);
					dataMap = GrpcParamUtil.getRetMap(retMap, "results");		
				}
				if (errMsg != null) {
		    		throw new Exception(errMsg);
		    	}
			}
			return dataMap;
		} catch(Exception ex) {
			throw ex;
		} finally {   
	    	if (retMap  != null) { retMap  = null; }
	    	if (dataMap != null) { dataMap = null; }
	    	if (aes256  != null) { aes256  = null; }
	    }
	}
	
	public Map<String, Object> callLoginII(String chnl, String bufferType, String GRPC_URI, String loginKey) throws Exception {
		String[] loginArr = loginKey.split("\\Q|\\E");
		Map<String, Object> retMap = null;
		GrpcParams gprms = null;

		SciRIO.RetMsg protoResp = null;
		RetMsg flatResp = null;
		
		String retData = null;
		String retMsg  = null;
		String retErr  = null;
		AES256Util aes256 = null;
		try {			
			//String paramsKey  = "";
	    	//String encPwd = aes256.encryptII(userPwd);
	    	//paramsKey += julianDate +"|"+ PID +"|"+ agentID +"|";
	    	//paramsKey += custID +"|"+ userId +"|"+ encPwd +"|";
	    	//paramsKey += LOGIN +"|"+ userIP +"|"+ serverIP;
	    	//paramsKey += "|AU9999|N";//AU9999:권한없음
			
			String inputPwd = loginArr[5]; //Auto Login 시 Token임.
				
			int ISLOGIN = Integer.parseInt(loginArr[6]); //0:로그인 시도, 5:자동로그인

			Map<String, Object> rpcPrms = new HashMap<String, Object>();
			rpcPrms.put("loginKey", loginKey);
			rpcPrms.put("jDate",    loginArr[0]);
			rpcPrms.put("PID",      loginArr[1]);
			rpcPrms.put("agentID",  loginArr[2]);
			rpcPrms.put("custID",   loginArr[3]);
			rpcPrms.put("loginID",  loginArr[4]);
			rpcPrms.put("userPwd",  inputPwd);
			rpcPrms.put("pwd",      inputPwd);		            		
			rpcPrms.put("ISLOGIN",  loginArr[6]);
			rpcPrms.put("borgUID",  "0");
			rpcPrms.put("userIP",   loginArr[7]);
			rpcPrms.put("serverIP", loginArr[8]);
			if (ISLOGIN == 5) {
				rpcPrms.put("series",   loginArr[9]);//Auto 로그인시 확인용임.
			}

			//  파라미터 데이터를 JSON 형식으로 변환함.
			String jsonReq = JsonUtil.getJsonStringFromMapHead("params", rpcPrms);
			//logger.info("jsonReqjsonReqjsonReqjsonReqjsonReq "+ jsonReq);			
			gprms = GrpcParamUtil.getGRpcParams(rpcPrms);			
			gprms.setChannel(chnl);
			gprms.setType("JSON");		
			gprms.setData(jsonReq);				
			gprms.setpID(loginArr[1]);
			gprms.setUserIP(loginArr[7]);
			gprms.setServerIP(loginArr[8]);
			gprms.setUserUID("0");
			gprms.setBorgUID("0");
			gprms.setAgentID(loginArr[2]);

    		GrpcResp resp = this.callRPCResponse(gprms, bufferType, "getResponse");
    		if (resp.getResults() != null && resp.getResults().length() > 0) {
				retData = resp.getResults();	
			} else {
				retMsg  = resp.getErrMsg();	
				retErr  = resp.getErrCode();	
			}
			//logger.info(retData);
			//{"errCode":"NO_DATA","errMsg":"議고쉶???곗씠?곌? ?놁뒿?덈떎."}
			if (resp == null || retData.indexOf("NO_DATA") >= 0) {
				throw new Exception("[8000]your id is wrong or there is no your organization!");
			} else {
				//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@		
				List<Map<String, Object>> retList = null;
				if (retErr == null) {
					JSONArray jsonArr = (JSONArray)((JSONObject)new JSONParser().parse(retData)).get("results");	        
		        	if (jsonArr != null && jsonArr.size() > 0) {	
		        		ObjectMapper objMapper = new ObjectMapper();	        		
		        		retList = objMapper.readValue(jsonArr.toJSONString(), objMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
		        	} else {
		        		retList = new ArrayList<Map<String, Object>>();
		        	}
					retMap = retList.get(0);
					
					int julianDate    = DateUtil.toJulian(new Date());        	
			    	
			    	// csKey=julianDate|agentID|userUID|borgUID|userIP|serverIP
			    	String tokenStr = julianDate +"|"+ retMap.get("agentID") +"|"+ retMap.get("userUID");
			    	tokenStr += "|"+ retMap.get("borgUID");
			    	tokenStr += "|"+ loginArr[7] +"|"+ loginArr[8];	
			    	aes256 = new AES256Util();
			    	String encKey = aes256.encryptII(tokenStr);
			    	//logger.info("encKey ::: " + encKey);
			    	retMap.put("csKey", encKey);	
			    	String userPwd = ""+ retMap.get("pwd"); 
			    	String token   = ""+ retMap.get("token"); 
	    			//System.out.println("token    ::::::::::::::::::::: " + token);
	    			//System.out.println("inputPwd ::::::::::::::::::::: " + inputPwd);
			    	if (!inputPwd.equals(userPwd)) {	
			    		boolean isAlert = true;
			    		if (ISLOGIN == 5 && retMap.get("token") != null && retMap.get("series") != null && !"N".equals(token)) {
			    			isAlert = false;
			    		}			    		
			    		if (isAlert) {
			    			throw new Exception("[8001]your password is wrong!");
			    		}
					}	
			    	retMap.put("pwd", userPwd);	    
				}
			}
			return retMap;
		} catch(Exception ex) {
			throw ex;
		} finally {   
			if (loginArr  != null) { loginArr  = null; }
			if (gprms     != null) { gprms     = null; }
	    	if (retMap    != null) { retMap    = null; }
	    	if (flatResp  != null) { flatResp  = null; }
	    	if (aes256    != null) { aes256    = null; }
	    	if (protoResp != null) { protoResp = null; }
	    }
	}
}
