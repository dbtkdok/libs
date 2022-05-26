package com.sci4s.cnf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.flatbuffers.FlatBufferBuilder;
import com.sci4s.err.NotFoundPIDException;
import com.sci4s.fbs.FlatJsonGrpc;
import com.sci4s.grpc.MsaApiGrpc;
import com.sci4s.grpc.SciRIO;
import com.sci4s.grpc.svc.RpcChannel;

public class PIDSLoader {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private volatile static PIDSLoader instance;
	private Map<String, PIDSteps> svcMap = new HashMap<String, PIDSteps>();
	private String msaID = "Local";

	public String getMsaID() {
		return msaID;
	}

	public void setMsaID(String msaID) {
		this.msaID = msaID;
	}

	/**
	 * PIDSLoader의 Instance를 리턴하는 Method
	 * DCL(Double-checked Locking) 기법, jdk1.5 버젼 이상에서만 지원
	 * getInstance()가 호출될 때 instance가 없을 경우만 한 번 synchornize하니까 성능도 보장됩니다.
	 * 그리고 volatile 키워드를 사용해서 변수의 원자성을 보장합니다.
	 * 
	 * @return PIDSLoader
	 */ 
	public static synchronized PIDSLoader getInstance(String msaKeys, String bufferType) throws Exception {
		try { 
			if (instance == null) {
	        	synchronized(PIDSLoader.class) {
	                if (instance == null) {
	                	try	{ 
	                		if (msaKeys == null) {
	                			instance = new PIDSLoader(); 
	                		} else {
	                			instance = new PIDSLoader(msaKeys, bufferType); 
	                		}
	                	} catch (Exception e) {  throw e; }
	                }
	            }
	        }
	        return instance;
		 } catch(Exception e) {
			 e.printStackTrace();
			 throw e;
		 }
    }
	
	public PIDSLoader() {
		try { init(); } catch(Exception e) { e.printStackTrace(); }
	}
	public PIDSLoader(String msaID, String bufferType) {
		try { 
			init(msaID, bufferType); 
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	private void init() throws Exception {		
		System.out.println("init()");
		setPIDStepsonents4Local();	
	}
	
	private void init(String msaKeys, String bufferType) throws Exception {		
		if (msaKeys == null) {
			System.out.println("init()");
			setPIDStepsonents4Local();
		} else {
			System.out.println("init(\""+ msaKeys +"\")");
			setPIDStepsonents4Remote(msaKeys, bufferType);
		}	
	}
	
	public void setPIDStepsonents4Remote(String msaKeys, String bufferType) throws Exception {	
		String[] arrKey = msaKeys.split("\\|");
//		ManagedChannel channel = null;
		String jsonXML = null;
		// "192.168.219.195:18997"
		System.out.print(arrKey[2]+"-pids.xml URI ::: "+ arrKey[0]);
		
		RpcChannel rpcChannel = new RpcChannel();
		rpcChannel.openChannel(arrKey[0], bufferType);

		if("FLAT".equals(bufferType)) {
			System.out.println(arrKey[0]);
//			String[] url = arrKey[0].split("\\:");
//			channel = ManagedChannelBuilder.forAddress(url[0], Integer.parseInt(url[1]))
//	                .usePlaintext()
//	                .directExecutor()
//	                .maxInboundMessageSize(2147483647)
//	                .build();
			
			FlatBufferBuilder builder = new FlatBufferBuilder();
			
			int dataOffset = com.sci4s.fbs.ReqMsg.createReqMsg(builder
					, builder.createString("" + arrKey[1])
					, builder.createString("" + arrKey[2]));
		
			builder.finish(dataOffset);        
			com.sci4s.fbs.ReqMsg request = com.sci4s.fbs.ReqMsg.getRootAsReqMsg(builder.dataBuffer());			
//		    FlatJsonGrpc.FlatJsonBlockingStub stub = FlatJsonGrpc.newBlockingStub(rpcChannel.getChannel());
//		    com.sci4s.fbs.RetMsg retMsg = stub.getServiceXml(request);			
			com.sci4s.fbs.RetMsg retMsg = ((FlatJsonGrpc.FlatJsonBlockingStub)rpcChannel.getBlockingStub(bufferType)).getServiceXml(request);		    
		    if (retMsg.errCode().equals("0")) {
				jsonXML = retMsg.results();			
				//System.out.print(arrKey[2] +"_jsonXML ::: "+ jsonXML);
				if (jsonXML.indexOf("errCode") >= 0) {
					jsonXML = null;
				}
			}
		    
		} else {
//			channel = ManagedChannelBuilder.forTarget(arrKey[0])
//					.usePlaintext()
//					.build();
//			MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(rpcChannel.getChannel());			
			SciRIO.ReqMsg reqMsg = SciRIO.ReqMsg.newBuilder()
					.setAgentID(arrKey[1])
					.setMsg(arrKey[2])
					.build();			
//			RetMsg retMsg = stub.getServiceXml(reqMsg);
			SciRIO.RetMsg retMsg = ((MsaApiGrpc.MsaApiBlockingStub)rpcChannel.getBlockingStub(bufferType)).getServiceXml(reqMsg);
			if (retMsg.getErrCode().equals("0")) {
				jsonXML = retMsg.getResults();			
				//System.out.print(arrKey[2] +"_jsonXML ::: "+ jsonXML);
				if (jsonXML.indexOf("errCode") >= 0) {
					jsonXML = null;
				}
			}
		}
		//System.out.println("retMsg.getErrCode() ::: "+ retMsg.getErrCode());
		//channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);		
		rpcChannel.closeChannel();
		
		if (jsonXML != null) {
			System.out.println("#############################################");
			System.out.println(">>>>>>>>>>>>>>> Loading start <<<<<<<<<<<<<<<");	
			List<PIDSteps> pidSteps = ServicesMarshaller.getUnMarshalPID(jsonXML, "JSON");
			this.registPIDSteps(pidSteps);
			System.out.println(">>>>>>>>>>>>>>> Loading  end  <<<<<<<<<<<<<<<");
			System.out.println("#############################################");
		} else {
			System.out.println("pids-xml not found data.");
		}
	}	

	public void setPIDStepsonents4Local() throws Exception {
		ClassLoader classLoader = null;
		String filePath = null;
		File xmlFile = null;
		try {
			filePath = this.getClass().getResource("/pids.xml").getPath();
			System.out.println("filePath ::: "+ filePath);
			xmlFile  = new File(filePath);		
		} catch (Exception ex){
			ex.printStackTrace();
			System.out.println("xmlFile ERR");
		}		
		System.out.println("#############################################");
		System.out.println(">>>>>>>>>>>>>>> Loading start <<<<<<<<<<<<<<<");			
		DocumentBuilder builder      = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Unmarshaller    unmarshaller = JAXBContext.newInstance(Pids.class).createUnmarshaller();
		if (xmlFile != null) {
			Pids config = null;
			try {
				config = (Pids)unmarshaller.unmarshal(builder.parse(xmlFile));
				if (config.getImportList() != null) {
					for (Import im : config.getImportList()) {	
						String imXmlFile = this.getClass().getResource("/"+ im.getResourceName()).getPath();
						List<PIDSteps> pidSteps = ServicesMarshaller.getUnMarshalPID(imXmlFile, "FILE");
						this.registPIDSteps(pidSteps);
					}
				}	
			} catch(Exception e) {
				classLoader = this.getClass().getClassLoader();
				try (InputStream inputStream = classLoader.getResourceAsStream("/pids.xml")) {	         
		            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		            System.out.println(result);            
		    		if (result != null) {
		    			config = (Pids)unmarshaller.unmarshal(new StringReader(result));
		    			try {
		    				if (config.getImportList() != null) {
		    					for (Import im : config.getImportList()) {	
		    						try (InputStream pidInputStream = classLoader.getResourceAsStream("/"+ im.getResourceName())) {	         
		    				            String pidResult = IOUtils.toString(pidInputStream, StandardCharsets.UTF_8);
		    				            List<PIDSteps> pidSteps = ServicesMarshaller.getUnMarshalPID(pidResult, "XML");
		        						this.registPIDSteps(pidSteps);    				            
		    						} catch (IOException ee) {
		    							ee.printStackTrace();
		    				            throw new Exception(im.getResourceName() +" is not found. ::: "+ ee.getMessage());
		    				        }
		    					}
		    				}	
		    			} catch(Exception e1) {
		    				e1.printStackTrace();
		    				logger.error("No more import elements are exist.");
		    			}
		    		}	 
		        } catch (IOException e2) {
		            e.printStackTrace();
		            throw new Exception("pids.xml is not found. ::: "+ e2.getMessage());
		        }
			}
		}
		System.out.println(">>>>>>>>>>>>>>> Loading  end  <<<<<<<<<<<<<<<");
		System.out.println("#############################################");
	}
	
	public void registPIDSteps(List<PIDSteps> pidSteps) {
		try {
			for (PIDSteps pidInfo :pidSteps) {
				svcMap.put(pidInfo.getPid(), pidInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}	
	
	/**
	 * Map에 저장해 놓은 PIDSteps들을 찾아 리턴하는 Method
	 * 
	 * @param  String PID
	 * @return PIDSteps
	 */ 
	public PIDSteps getPIDSteps(String PID) throws NotFoundPIDException {
		if (svcMap.containsKey(PID)) {
			return (PIDSteps)svcMap.get(PID);
		} else {			
			System.out.println("[PID ::: "+ PID +" is not found!]");			
			throw new NotFoundPIDException("[PID ::: "+ PID +" is not found!]", "8888");
		}
	}
}
