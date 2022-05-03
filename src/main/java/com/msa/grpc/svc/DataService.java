package com.msa.grpc.svc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msa.cnf.PIDSLoader;
import com.msa.fbs.RetMsg;
import com.msa.grpc.CommInfos;
import com.msa.grpc.ErrConstance;
import com.msa.grpc.SciRIO;
import com.msa.grpc.dao.IDataDao;
import com.msa.grpc.dto.GrpcParams;
import com.msa.grpc.dto.GrpcResp;
import com.msa.grpc.utils.GrpcDataUtil;
import com.msa.utils.AES256Util;

public class DataService {
	
	Logger logger = LoggerFactory.getLogger(DataService.class);
	
	public Map<String,Object> getCommInfoMap(GrpcParams grpcPrms) throws Exception {
		AES256Util aes256 = null;
		String[] arrCsKey = null;
		String roleID     = null;
		
		Map<String,Object> commInfoMap = new HashMap<String, Object>();
		try {
			aes256 = new AES256Util();			
			// csKey=julianDate|agentID|userUID|borgUID|userIP|serverIP|roleID|consignGB
			// csKey=julianDate|PID|agentID|clientID|loginID|userPwd|LOGIN|userIP|serverIP|roleID|consignGB
			arrCsKey = new CommonService().getSplitData(aes256.decryptII(grpcPrms.getCsKey()));
			if (arrCsKey.length == 10) {//loginKey
				roleID = arrCsKey[9];
			} else {
				roleID = arrCsKey[6];
			}
			if ("AU9999".equals(roleID)) {
				throw new Exception("@FAIL@Not have access rights.@FAIL@");
			}
			for (String key : CommInfos.commInfos) {
				String methName = "get"+ key.substring(0,1).toUpperCase() + key.substring(1);		
				if ("PID".equals(key)) {
					methName = "getpID";
				}	
				try {
					Method getter = grpcPrms.getClass().getDeclaredMethod(methName, null);	
					commInfoMap.put(key, getter.invoke(grpcPrms, null));
				} catch(Exception ex) {
					throw ex;
				}
			}
			if (arrCsKey.length == 10) {//loginKey
				commInfoMap.put("roleID",   arrCsKey[9]);
				commInfoMap.put("consignGB",arrCsKey[10]);
			} else {
				commInfoMap.put("roleID",   arrCsKey[6]);
				commInfoMap.put("consignGB",arrCsKey[7]);
			}			
			return commInfoMap;
			
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new Exception("@FAIL@Not have access rights.@FAIL@");
		}  finally {
        	if (commInfoMap != null) {
        		try { commInfoMap = null; } catch(Exception ex) { }
        	}
        	if (aes256 != null) {
        		try { aes256 = null; } catch(Exception ex) { }
        	}
        }
	}
	
	public String query4Json(IDataDao dataDao, String sqlID, Map<String,Object> grpcMap) throws Exception {
		
		String jsonRet = null;
		List<Map<String,Object>> rsList = dataDao.query4List1(sqlID, grpcMap);
	    if (rsList == null || rsList.size() == 0) {
	    	String errMsg   = CommInfos.defMsg;
	    	String errCode  = ErrConstance.NO_DATA;
	    	
	    	jsonRet = GrpcDataUtil.getGrpcResults("NO_DATA", errMsg, null);
	    } else {    
	    	jsonRet = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
	    }		
	    return jsonRet;
	}
	
	public GrpcResp query4Data(String bufferType, IDataDao dataDao, String sqlID, Map<String,Object> grpcMap) throws Exception {	
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			
			System.out.println("bufferType ::: " + bufferType);
			
			RetMsg retMsg = new FlatService().query4Data(dataDao, sqlID, grpcMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4Data(dataDao, sqlID, grpcMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	public GrpcResp query4XmlData(String bufferType, IDataDao dataDao, Map<String,Object> paramMap, Map<String, Object> commInfoMap) throws Exception {
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4XmlData(dataDao, paramMap, commInfoMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4XmlData(dataDao, paramMap, commInfoMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	
	public GrpcResp query4Update(String bufferType, IDataDao dataDao, String sqlID, Map<String,Object> paramMap) throws Exception {
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4Update(dataDao, sqlID, paramMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4Update(dataDao, sqlID, paramMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	public GrpcResp query4Execute(String bufferType, IDataDao dataDao, PIDSLoader pids, String PID, String flag, String sqlID, Map<String,Object> paramMap, Map<String,Object> commInfoMap) throws Exception {	
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4Execute(dataDao, pids, PID, flag, sqlID, paramMap, commInfoMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4Execute(dataDao, pids, PID, flag, sqlID, paramMap, commInfoMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	private GrpcResp query4UpdateSub(String bufferType, IDataDao dataDao, PIDSLoader pids, String calling, String PID, int affected, Map<String, Object> commInfoMap, Map<String, Object> paramMap) throws Exception {
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4UpdateSub(dataDao, pids, calling, PID, affected, commInfoMap, paramMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4UpdateSub(dataDao, pids, calling, PID, affected, commInfoMap, paramMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	public GrpcResp query4Updates(String bufferType, IDataDao dataDao, PIDSLoader pids, String sqlID, String PID, Map<String,Object> paramMap, Map<String,Object> commInfoMap) throws Exception {
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4Updates(dataDao, pids, sqlID, PID, paramMap, commInfoMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4Updates(dataDao, pids, sqlID, PID, paramMap, commInfoMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	public GrpcResp query4Test(String bufferType, IDataDao dataDao, Map<String,Object> paramMap) throws Exception {
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4Test(dataDao, paramMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4Test(dataDao, paramMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
  	}
	
//	/**
//     * thr 서비스에 신규 고객 협력사 정보를 생성한다.
//     * 
//     * @param  Map<String, Object> paramMap : uuID|custVdID|custID|userUID|agentID|vdUID
//     * @return String results
//     */
//    private String insNewCustVdInfo(Map<String, Object> paramMap) {   	
//    	final ManagedChannel channel = ManagedChannelBuilder.forTarget((String) paramMap.get("THR_URI"))
//    			.usePlaintext()
//    			.build();
//    	String jsonRet = null;
//    	try {
//        	MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(channel);
//    		String params = paramMap.get("uuID").toString() +"|"+ paramMap.get("contVdID").toString();
//    		params += "|"+ paramMap.get("custID").toString() +"|"+ paramMap.get("userUID").toString();
//    		params += "|"+ paramMap.get("agentID").toString() +"|"+ paramMap.get("vdUID").toString();
//    		
//    		//String params = "13391518|4020199666|SUMC|1|13|19736";
//    		SciRIO.ReqMsg reqMsg = SciRIO.ReqMsg.newBuilder()
//        			.setAgentID(paramMap.get("agentID").toString() )
//    				.setMsg(params)
//    				.build();
//    		
//    		RetMsg retMsg = stub.insNewCustVdInfo(reqMsg);
//    		if (retMsg.getErrCode().equals("0")) {
//    			jsonRet = "SUCCESS";
//    		} else {
//    			jsonRet = "ERROR["+ retMsg.getErrMsg() +"]";
//    		}
//        	return jsonRet;
//        } catch(Exception ex) {
//        	return "ERROR"+ ex.getMessage() +"]";
//        } finally {
//        	try { channel.shutdown().awaitTermination(1, TimeUnit.SECONDS); } catch(Exception ex) { }
//        }
//    }
//	
//    /**
//     * thr 서비스에 결재정보를 백업 후 삭제한다.
//     * 
//     * @param  Map<String, Object> paramMap : agentID|userUID|uuID|apvUID
//     * @return String results
//     */
//    private String delApvInfo4ApvUID(Map<String, Object> paramMap) {   	
//    	final ManagedChannel channel = ManagedChannelBuilder.forTarget((String) paramMap.get("THR_URI"))
//    			.usePlaintext()
//    			.build();
//    	String jsonRet = null;
//    	try {
//        	MsaApiGrpc.MsaApiBlockingStub stub = MsaApiGrpc.newBlockingStub(channel);
//        	
//    		String params = paramMap.get("agentID").toString() +"|"+ paramMap.get("userUID").toString();
//    		params += "|"+ paramMap.get("uuID").toString() +"|"+ paramMap.get("apvUID").toString();
//    		
//    		//String params = "13|1|13391518|4020199666";
//    		SciRIO.ReqMsg reqMsg = SciRIO.ReqMsg.newBuilder()
//        			.setAgentID(paramMap.get("agentID").toString() )
//    				.setMsg(params)
//    				.build();
//    		
//    		RetMsg retMsg = stub.delApvInfo4ApvUID(reqMsg);
//    		if (retMsg.getErrCode().equals("0")) {
//    			jsonRet = "SUCCESS";
//    		} else {
//    			jsonRet = "ERROR["+ retMsg.getErrMsg() +"]";
//    		}
//        	return jsonRet;
//        } catch(Exception ex) {
//        	return "ERROR"+ ex.getMessage() +"]";
//        } finally {
//        	try { channel.shutdown().awaitTermination(1, TimeUnit.SECONDS); } catch(Exception ex) { }
//        }
//    }
//    
//
/*
 * ========== getMstInfo start =============
 * */
	/**
	 * Master 테이블 정보가 변경되었을 경우, 싱크하기 위한 서비스
	 * 
	 * @param  Map<String,Object> paramMap
	 * @return String results
	 */
	public GrpcResp getMstInfo(IDataDao dataDao, Map<String,Object> paramMap) throws Exception {
		String   errCode  = "0";
  		String   errMsg   = "";
  		GrpcResp grpcResp = new GrpcResp();
		String results = new CommonService().getMstInfo(dataDao, paramMap);
		
		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(results);
		
        return grpcResp;
	}
	
	public GrpcResp getSyncConfig(String bufferType, IDataDao dataDao, String sqlID, Map<String,Object> grpcMap) throws Exception {	
		GrpcResp grpcResp = new GrpcResp();
		
		if("FLAT".equals(bufferType)) {
			RetMsg retMsg = new FlatService().query4Data(dataDao, sqlID, grpcMap);
			grpcResp.setErrCode(retMsg.errCode());
			grpcResp.setErrMsg(retMsg.errMsg());
			grpcResp.setResults(retMsg.results());
		} else {
			SciRIO.RetMsg retMsg = new ProtoService().query4Data(dataDao, sqlID, grpcMap);
			grpcResp.setErrCode(retMsg.getErrCode());
			grpcResp.setErrMsg(retMsg.getErrMsg());
			grpcResp.setResults(retMsg.getResults());
		}
		
        return grpcResp;
	}
	
	
	
}
