package com.sci4s.grpc.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sci4s.grpc.dto.GrpcParams;
import com.sci4s.utils.AES256Util;
import com.sci4s.utils.DateUtil;

public class GrpcParamUtil {
	public static GrpcParams getGRpcParams(Map<String, Object> map) throws Exception {
		Logger    logger = LoggerFactory.getLogger(GrpcReflectUtil.class);
		GrpcParams gprms = null;		
		AES256Util aes256 = null;
		String csKey = "";
		try { 
			//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			// 1. PID기준 RPC 메서드명 셋팅
//			String rpcClass = GrpcReflectUtil.getProperty4RPC(""+map.get("PID"), "class", "MsaApi");
//			String methName = GrpcReflectUtil.getProperty4RPC(""+map.get("PID"), "method","callRPC");
//			String channel  = GrpcReflectUtil.getProperty4RPC(""+map.get("PID"), "channel","api");
//			String retType  = GrpcReflectUtil.getProperty4RPC(""+map.get("PID"), "retType","JSON");
//			logger.info("getGRpcParams PID     #######"+ map.get("PID"));
//			logger.info("getGRpcParams METHOD  #######"+ methName);
//			logger.info("getGRpcParams CLASS   #######"+ rpcClass);
//			logger.info("getGRpcParams CHANNEL #######"+ channel);
//			logger.info("getGRpcParams RETTYPE #######"+ retType);
			//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
			// 2. RPC 호출을 위한 Parameter 객체(GrpcParams) 설정
			String encKey = "";
			if (map.containsKey("csKey")) {
				encKey = ""+ map.get("csKey");
			} else {
				int julianDate  = DateUtil.toJulian(new Date());				
				aes256 = new AES256Util();				
				// csKey=julianDate|agentID|userUID|borgUID|userIP|serverIP|roleID|consignGB
		    	csKey += julianDate+"|"+map.get("agentID")+"|"+map.get("userUID");
		    	csKey += "|"+map.get("borgUID")+"|"+map.get("userIP")+"|"+map.get("serverIP");
		    	csKey += "|"+map.get("roleID")+"|"+map.get("consignGB");
		    	encKey = aes256.encryptII(csKey);
			}
	    	
			gprms = new GrpcParams();
			gprms.setpID(""+ map.get("PID"));
			gprms.setAgentID(""+ map.get("agentID"));		
			gprms.setCsKey(encKey);
			gprms.setUserIP(""+ map.get("userIP"));
			gprms.setServerIP(""+ map.get("serverIP"));
			gprms.setUserUID(""+ map.get("userUID"));
			gprms.setBorgUID(""+ map.get("borgUID"));			
			gprms.setRoleID(""+ map.get("roleID"));
			gprms.setConsignGB(""+ map.get("consignGB"));
//			if (!map.containsKey("clang")) {
				gprms.setClang(""+ map.get("clang"));
//			}			
			if (map.containsKey("bMenuID")) {
				gprms.setBmenuID("");
			}
			if (map.containsKey("menuID")) {
				gprms.setMenuID("");
			}
			gprms.setChannel("api");      //pids.propertiese
			gprms.setRpcClass("MsaCore"); //pids.propertiese
			gprms.setMethodNM("CallRMsg");//pids.propertiese
			gprms.setType("JSON");        //pids.propertiese
			
			return gprms;
		} catch(Exception ex) {
			throw ex;
		} finally {		
			if (gprms   != null) { gprms  = null; }
			if (aes256  != null) { aes256 = null; }
			if (logger  != null) { logger = null; }
		}		
	}
	
	/**
	 * GRPC 호출 후, 리턴되는 MSG가 있으면 오류, 없으면 성공임.
	 * @param  map
	 * @return String MSG
	 * @throws Exception
	 */
	public static String getRetMsg(Map<String, Object> map) throws Exception {
		String msg = null;
		if (map.get("results") instanceof List) {
	    	List<Map<String, Object>> lst = (List<Map<String, Object>>)map.get("results");
	    	if (lst.size() == 1) {
	    		if (lst.get(0).containsKey("MSG")) {
	    			msg = ""+ lst.get(0).get("MSG");
	    		}
	    	}
	    }
		return msg;
	}
	/**
	 * GRPC 호출 후, 리턴되는 results가 단일 Row 데이터일 경우
	 * @param  map
	 * @param  String keyName
	 * @return Map<String, Object>
	 * @throws Exception
	 */
	public static Map<String, Object> getRetMap(Map<String, Object> map, String keyName) throws Exception {
	    return (Map<String, Object>)map.get(keyName);//"results"
	}
	/**
	 * GRPC 호출 후, 리턴되는 results가 여러 Row 데이터일 경우
	 * @param  map
	 * @param  String keyName
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getRetList(Map<String, Object> map, String keyName) throws Exception {
	    return (List<Map<String, Object>>)map.get(keyName);//"results"
	}
}
