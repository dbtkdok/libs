package com.sci4s.grpc.svc;

import java.util.Map;

import javax.annotation.concurrent.ThreadSafe;

import com.google.flatbuffers.FlatBufferBuilder;
import com.sci4s.fbs.FlatJsonGrpc;
import com.sci4s.fbs.ReqMsg;
import com.sci4s.fbs.RetMsg;
import com.sci4s.grpc.MsaApiGrpc;
import com.sci4s.grpc.SciRIO;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

//@Service
//@Scope("prototype")
@ThreadSafe
public class MsaChannel extends RpcChannel {

	public ManagedChannel getChannel() {
		return this.channel;
	}
	
	public Object insNewCustVdInfo(Map<String, Object> paramMap, String type) throws Exception {		
		if ("PROTO".equals(type)) {
			return insNewCustVdInfoProto(paramMap);
		} else {		
			return insNewCustVdInfoFlat(paramMap);
		}
	}
	
	public Object insNewCustVdInfoFlat(Map<String, Object> paramMap) throws Exception {
		FlatBufferBuilder builder = new FlatBufferBuilder();
		String jsonRet = "";
		try {			
			String params = paramMap.get("uuID").toString() +"|"+ paramMap.get("contVdID").toString();
	  		params += "|"+ paramMap.get("custID").toString() +"|"+ paramMap.get("userUID").toString();
	  		params += "|"+ paramMap.get("agentID").toString() +"|"+ paramMap.get("vdUID").toString();
	  		
			int dataOffset = ReqMsg.createReqMsg(builder
					, builder.createString("" + paramMap.get("agentID").toString())
					, builder.createString("" + params));
		
			builder.finish(dataOffset);        
			ReqMsg request = ReqMsg.getRootAsReqMsg(builder.dataBuffer());
		    
//		    System.out.println("callRPCFlat.io.grpc.ConnectivityState ::::::::: "+ this.channel.getState(this.isState));
		    FlatJsonGrpc.FlatJsonBlockingStub stub = FlatJsonGrpc.newBlockingStub(this.channel);
			//System.out.println("FLATConnector.stub.callRMsg -> gprms.getData() :::: "+ gprms.getData());
			RetMsg response = stub.insNewCustVdInfo(request);
			if (response.errCode().equals("0")) {
	  			jsonRet = "SUCCESS";
	  		} else {
	  			jsonRet = "ERROR["+ response.errMsg() +"]";
	  		}
			return jsonRet;
		} catch(StatusRuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object insNewCustVdInfoProto(Map<String, Object> paramMap) throws Exception {
		String jsonRet = "";
		try {
			MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(channel);
	  		String params = paramMap.get("uuID").toString() +"|"+ paramMap.get("contVdID").toString();
	  		params += "|"+ paramMap.get("custID").toString() +"|"+ paramMap.get("userUID").toString();
	  		params += "|"+ paramMap.get("agentID").toString() +"|"+ paramMap.get("vdUID").toString();
	  		
	  		//String params = "13391518|4020199666|SUMC|1|13|19736";
	  		SciRIO.ReqMsg reqMsg = SciRIO.ReqMsg.newBuilder()
	      			.setAgentID(paramMap.get("agentID").toString() )
	  				.setMsg(params)
	  				.build();
	  		
	  		com.sci4s.grpc.SciRIO.RetMsg retMsg = stub.insNewCustVdInfo(reqMsg);
	  		if (retMsg.getErrCode().equals("0")) {
	  			jsonRet = "SUCCESS";
	  		} else {
	  			jsonRet = "ERROR["+ retMsg.getErrMsg() +"]";
	  		}
	  		
	  		return jsonRet;
		} catch(StatusRuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object delApvInfo4ApvUID(Map<String, Object> paramMap, String type) throws Exception {		
		if ("PROTO".equals(type)) {
			return delApvInfo4ApvUIDProto(paramMap);
		} else {		
			return delApvInfo4ApvUIDFlat(paramMap);
		}
	}
	
	public Object delApvInfo4ApvUIDFlat(Map<String, Object> paramMap) throws Exception {
		FlatBufferBuilder builder = new FlatBufferBuilder();
		String jsonRet = "";
		try {			
			String params = paramMap.get("agentID").toString() +"|"+ paramMap.get("userUID").toString();
			params += "|"+ paramMap.get("uuID").toString() +"|"+ paramMap.get("apvUID").toString();
	  		
			int dataOffset = ReqMsg.createReqMsg(builder
					, builder.createString("" + paramMap.get("agentID").toString())
					, builder.createString("" + params));
		
			builder.finish(dataOffset);        
			ReqMsg request = ReqMsg.getRootAsReqMsg(builder.dataBuffer());
		    
//		    System.out.println("callRPCFlat.io.grpc.ConnectivityState ::::::::: "+ this.channel.getState(this.isState));
		    FlatJsonGrpc.FlatJsonBlockingStub stub = FlatJsonGrpc.newBlockingStub(this.channel);
			//System.out.println("FLATConnector.stub.callRMsg -> gprms.getData() :::: "+ gprms.getData());
			RetMsg response = stub.delApvInfo4ApvUID(request);
			if (response.errCode().equals("0")) {
	  			jsonRet = "SUCCESS";
	  		} else {
	  			jsonRet = "ERROR["+ response.errMsg() +"]";
	  		}
			return jsonRet;
		} catch(StatusRuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object delApvInfo4ApvUIDProto(Map<String, Object> paramMap) throws Exception {
		String jsonRet = "";
		try {
			MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(channel);
			String params = paramMap.get("agentID").toString() +"|"+ paramMap.get("userUID").toString();
			params += "|"+ paramMap.get("uuID").toString() +"|"+ paramMap.get("apvUID").toString();
	  		
	  		//String params = "13391518|4020199666|SUMC|1|13|19736";
	  		SciRIO.ReqMsg reqMsg = SciRIO.ReqMsg.newBuilder()
	      			.setAgentID(paramMap.get("agentID").toString() )
	  				.setMsg(params)
	  				.build();
	  		
	  		com.sci4s.grpc.SciRIO.RetMsg retMsg = stub.delApvInfo4ApvUID(reqMsg);
	  		if (retMsg.getErrCode().equals("0")) {
	  			jsonRet = "SUCCESS";
	  		} else {
	  			jsonRet = "ERROR["+ retMsg.getErrMsg() +"]";
	  		}
	  		
	  		return jsonRet;
		} catch(StatusRuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object updTblAttach4DocNO(Map<String, Object> paramMap, String type) throws Exception {		
		if ("PROTO".equals(type)) {
			return updTblAttach4DocNOProto(paramMap);
		} else {		
			return updTblAttach4DocNOFlat(paramMap);
		}
	}
	
	public Object updTblAttach4DocNOFlat(Map<String, Object> paramMap) throws Exception {
		FlatBufferBuilder builder = new FlatBufferBuilder();
		String jsonRet = "";
		try {			
			String docKey = paramMap.get("docKey").toString();
        	String params = paramMap.get("attachID") +"|"+ paramMap.get(docKey) +"|"+ paramMap.get("docGB");
	  		
			int dataOffset = ReqMsg.createReqMsg(builder
					, builder.createString("" + paramMap.get("agentID").toString())
					, builder.createString("" + params));
		
			builder.finish(dataOffset);        
			ReqMsg request = ReqMsg.getRootAsReqMsg(builder.dataBuffer());
		    
//		    System.out.println("callRPCFlat.io.grpc.ConnectivityState ::::::::: "+ this.channel.getState(this.isState));
		    FlatJsonGrpc.FlatJsonBlockingStub stub = FlatJsonGrpc.newBlockingStub(this.channel);
			//System.out.println("FLATConnector.stub.callRMsg -> gprms.getData() :::: "+ gprms.getData());
			RetMsg response = stub.updTblAttach4DocNO(request);
			if (response.errCode().equals("0")) {
	  			jsonRet = "SUCCESS";
	  		} else {
	  			jsonRet = "ERROR["+ response.errMsg() +"]";
	  		}
			return jsonRet;
		} catch(StatusRuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public Object updTblAttach4DocNOProto(Map<String, Object> paramMap) throws Exception {
		String jsonRet = "";
		try {
			MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(channel);
			String docKey = paramMap.get("docKey").toString();
        	String params = paramMap.get("attachID") +"|"+ paramMap.get(docKey) +"|"+ paramMap.get("docGB");
//        	logger.debug("1580 "+ docKey +"......params:::::"+ params);
        	SciRIO.ReqMsg reqMsg = SciRIO.ReqMsg.newBuilder()
    				.setMsg(params) /* attachID|docNO|docGB */
    				.build();
        	
        	com.sci4s.grpc.SciRIO.RetMsg retMsg = stub.updTblAttach4DocNO(reqMsg);
        	
	  		if (retMsg.getErrCode().equals("0")) {
	  			jsonRet = "SUCCESS";
	  		} else {
	  			jsonRet = "ERROR["+ retMsg.getErrMsg() +"]";
	  		}
	  		
	  		return jsonRet;
		} catch(StatusRuntimeException ex) {
			throw ex;
		} catch(Exception ex) {
			throw ex;
		}
	}
}
