package com.sci4s.grpc.svc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sci4s.cnf.PIDSLoader;
import com.sci4s.grpc.CommInfos;
import com.sci4s.grpc.ErrConstance;
import com.sci4s.grpc.SciRIO;
import com.sci4s.grpc.dao.IDataDao;
import com.sci4s.grpc.dto.GrpcParams;
import com.sci4s.grpc.dto.GrpcResp;
import com.sci4s.grpc.utils.GrpcDataUtil;
import com.sci4s.utils.AES256Util;

public class ProtoService {
	
	Logger logger = LoggerFactory.getLogger(ProtoService.class);

	public static SciRIO.RetMsg query4Data(IDataDao dataDao, String sqlID, Map<String,Object> grpcMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "";
		String   jsonRet  = "";		
		GrpcResp grpcResp = new GrpcResp();
		
		//Map<String, Object> paramMap = new HashMap<String, Object>();
		grpcResp = new CommonService().query4Data(dataDao, sqlID, grpcMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();
		//logger.debug("jsonRet ::: "+ jsonRet);

		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
        SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
        		.setResults(jsonRet)
    			.setErrCode(errCode)
    			.setErrMsg(errMsg)
    			.build();
        
        return response;
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
	
	public static SciRIO.RetMsg query4XmlData(IDataDao dataDao, Map<String,Object> paramMap, Map<String, Object> commInfoMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "";
		String   jsonRet  = "";
		GrpcResp grpcResp = new GrpcResp();
		
		grpcResp = new CommonService().query4XmlData(dataDao, paramMap, commInfoMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();

		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
        SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
        		.setResults(jsonRet)
    			.setErrCode(errCode)
    			.setErrMsg(errMsg)
    			.build();
        
        return response;
	}
	
	
	public SciRIO.RetMsg query4Update(IDataDao dataDao, String sqlID, Map<String,Object> paramMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "";
		String   jsonRet  = "";
		GrpcResp grpcResp = new GrpcResp();
		
		grpcResp = new CommonService().query4Update(dataDao, sqlID, paramMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();
		
		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
        SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
        		.setResults(jsonRet)
    			.setErrCode(errCode)
    			.setErrMsg(errMsg)
    			.build();
        return response;
	}
	
	public SciRIO.RetMsg query4Execute(IDataDao dataDao, PIDSLoader pids, String PID, String flag, String sqlID, Map<String,Object> paramMap, Map<String,Object> commInfoMap) throws Exception {	
		String jsonRet = null;
		String errCode = "0";
		String errMsg = "";
		GrpcResp grpcResp = new GrpcResp();
		
		grpcResp = new CommonService().query4Execute(dataDao, pids, PID, flag, sqlID, paramMap, commInfoMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();
		
		
		SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
        		.setResults(jsonRet)
    			.setErrCode(errCode)
    			.setErrMsg(errMsg)
    			.build();       
		
        return response;
	}
	
	public SciRIO.RetMsg query4UpdateSub(IDataDao dataDao, PIDSLoader pids, String calling, String PID, int affected, Map<String, Object> commInfoMap, Map<String, Object> paramMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "SUCCESS";
		String   jsonRet  = "";
		GrpcResp grpcResp = new GrpcResp();
		
		grpcResp = new CommonService().query4UpdateSub(dataDao, pids, calling, PID, affected, commInfoMap, paramMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();
		
		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
        SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
        		.setResults(jsonRet)
    			.setErrCode(errCode)
    			.setErrMsg(errMsg)
    			.build();        
        return response;
	}
	
	public SciRIO.RetMsg query4Updates(IDataDao dataDao, PIDSLoader pids, String sqlID, String PID, Map<String,Object> paramMap, Map<String,Object> commInfoMap) throws Exception {
		String jsonRet = "";
		String errCode = "0";
		String errMsg  = "";
		
		GrpcResp grpcResp = new GrpcResp();
		
		grpcResp = new CommonService().query4Updates(dataDao, pids, sqlID, PID, paramMap, commInfoMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();
		
		// 업무 처리 후, Json 형식으로 결과를 리턴한다.
		SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
    			.setResults(jsonRet)
    			.setErrCode(errCode)
    			.setErrMsg(errMsg)
    			.build();	

        return response;
	}
	
	public SciRIO.RetMsg query4Test(IDataDao dataDao, Map<String,Object> paramMap) throws Exception {
  		String   errCode  = "0";
  		String   errMsg   = "";
  		String   jsonRet  = "";		
  		GrpcResp grpcResp = new GrpcResp();
  		
  		grpcResp = new CommonService().query4Test(dataDao, paramMap);
		errCode = grpcResp.getErrCode();
		errMsg  = grpcResp.getErrMsg();
		jsonRet = grpcResp.getResults();
  		
  		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
          SciRIO.RetMsg response = SciRIO.RetMsg.newBuilder()
          		.setResults(jsonRet)
      			.setErrCode(errCode)
      			.setErrMsg(errMsg)
      			.build();
          
          return response;
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
///*
// * ========== getMstInfo start =============
// * */
//	/**
//	 * Master 테이블 정보가 변경되었을 경우, 싱크하기 위한 서비스
//	 * 
//	 * @param  Map<String,Object> paramMap
//	 * @return String results
//	 */
//	public String getMstInfo(IDataDao dataDao, Map<String,Object> paramMap) throws Exception {
//	
//		StringBuffer sbSel = new StringBuffer();
//		StringBuffer sbSql = new StringBuffer();
//		String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//		String ifUID = null;
//		String tblName = ""+ paramMap.get("tblName");		
//		logger.info("getMstInfo.tblName ::::::::::::::: "+ tblName);
//		
////		if ("tbl_custitemsif".equals(tblName)) { // 마스터정보 연계 외
////			results = this.getTblCustItemsIF(dataDao, paramMap);
////		} else if("tbl_itemsif".equals(tblName)) {
////			results = this.getTblItemsIF(dataDao, paramMap);
////		} else if("tbl_gpnitemmapsif".equals(tblName)) {
////			results = this.getTblGpnItemsIF(dataDao, paramMap);
////		} else if ("tbl_rolescopesif".equals(tblName)) { // 권한설정용 연계
////			results = this.getTblRoleScopesIF(dataDao, paramMap);
////		} else if ("tbl_rolemenuif".equals(tblName)) { // 권한별 XML 생성용 정보 연계
////			results = this.getTblRoleMenuIF(dataDao, paramMap);
////		} else if ("tbl_userauthif".equals(tblName)) { // 권한설정용 연계
////			results = this.getTblUserAuthIF(dataDao, paramMap);
//		if (appMap.containsKey(tblName)) {
//			Class cls = this.getClass();
//			
//			Class partypes[] = new Class[2];
//			partypes[0] = IDataDao.class;
//			partypes[1] = Map.class;
//			Method target = cls.getDeclaredMethod(appMap.get(tblName), partypes);
//
//			results = (String) target.invoke(this, new Object[]{dataDao, paramMap});
//		} else {
////			int affected = commMapper.updMstInfoList(paramMap);
//			int affected = dataDao.query4Update1("updMstInfoList", paramMap);
//			if (affected > 0) {
//				//logger.debug("1645 paramMap:::::::::::::::"+ paramMap);
////				List<Map<String,Object>> lst = commMapper.getMstInfoList(paramMap);
//				List<Map<String,Object>> lst = dataDao.query4List1("getMstInfoList", paramMap);
//		        if (lst == null || lst.size() == 0) {
//	
//		        } else { // tblName, keyCols, KeyVals, infCols
//		        	int zz = 0;
//		        	sbSel.append("SELECT \n");	
//		        	for (Map<String,Object> rsMap : lst) {
//		        		ifUID = (rsMap.get("ifUID")==null?"":"0"+rsMap.get("ifUID"));
//		        		if (zz == 0) {
//		        			//logger.debug("1656 infCols:::::::::::::::"+ rsMap.get("infCols"));     
//		        			String infCols = (rsMap.get("infCols")==null?"":""+rsMap.get("infCols"));
//		                	if (infCols.indexOf("|") >= 0) {
//		                		String[] cols = infCols.split("\\|");
//		                		for (int ii=0; ii<cols.length; ii++) {
//		                			if (ii == 0) {
//		                				sbSel.append(cols[ii]);
//		                    		} else {
//		                    			sbSel.append(", "+ cols[ii]);
//		                    		}
//		                		}
//		                		sbSel.append(" \n");
//		                	} else {
//		                		sbSel.append(infCols +" \n");
//		                	}        	
//		                	sbSel.append("FROM "+ paramMap.get("tblName") +" \n");
//		                	sbSel.append("WHERE ");
//		
//		                	String keyTyps = (rsMap.get("keyTyps")==null?"":""+rsMap.get("keyTyps")).toUpperCase();
//		                	String keyCols = (rsMap.get("keyCols")==null?"":""+rsMap.get("keyCols"));
//		                	//logger.debug("1676 keyCols:::::::::::::::"+ keyCols);
//		                	//logger.debug("1677 keyTyps:::::::::::::::"+ keyTyps);  
//		        			if (keyCols.indexOf("|") >= 0) {
//		                		String[] keys = keyCols.split("\\|");
//		                		String[] typs = keyTyps.split("\\|");
//		                		for (int ii=0; ii<keys.length; ii++) {
//		                			if (ii == 0) {
//		               					if (typs[ii].startsWith("STRING") || typs[ii].startsWith("VARCHAR")) {
//		                					sbSel.append(keys[ii] +"= '#"+ ii +"#' ");
//		                				} else {
//		                					sbSel.append(keys[ii] +"= #"+ ii +"# ");
//		                				}
//		                    		} else {
//		                    			if (typs[ii].startsWith("STRING") || typs[ii].startsWith("VARCHAR")) {
//		                    				sbSel.append(" AND "+ keys[ii] +"= '#"+ ii +"#' ");
//		                				} else {
//		                					sbSel.append(" AND "+ keys[ii] +"= #"+ ii +"# ");
//		                				}
//		                    		}
//		                		}
//		                		sbSel.append("\n");
//		                	} else {
//		                		if (keyTyps.startsWith("STRING") || keyTyps.startsWith("VARCHAR")) {
//		                			sbSel.append(keyCols +"= '#0#' \n");
//		        				} else {
//		        					sbSel.append(keyCols +"= #0# \n");
//		        				}
//		                	}
//		        		}        		
//		        		String keyVals = (rsMap.get("keyVals")==null?"":""+rsMap.get("keyVals")); 
//		        		//logger.debug("1706 keyVals:::::::::::::::"+ keyVals); 
//		        		String sql = sbSel.toString();
//		    			if (keyVals.indexOf("|") >= 0) {
//		            		String[] vals = keyVals.split("\\|");
//		            		for (int ii=0; ii<vals.length; ii++) {
//		            			sql = sql.replaceAll("\\Q#"+ ii +"#\\E", vals[ii]);
//		            		}
//		            	} else {
//		            		sql = sql.replaceAll("\\Q#0#\\E", keyVals);
//		            	}    
//		        		if (zz == 0) {
//		        			sbSql.append(sql);
//		        		} else {
//		        			sbSql.append(" UNION ALL \n"+ sql);
//		        		}
//		        		zz++;
//		        	}
//		            List<Map<String,Object>> rsList = null;
//		            if (sbSql.toString().length() > 0) {
////		            	rsList = commMapper.getSyncMstData(sbSql.toString());
//		            	Map<String, Object> param = new HashMap<String, Object>();
//		            	param.put("value", sbSql.toString());
//		            	
//		            	rsList = dataDao.query4List1("getSyncMstData", param);
//		            	if (rsList == null || rsList.size() == 0) {
//	
//		                } else {
//		                	results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//		                	try {
//		    	            	paramMap.put("ifUID", ifUID);    	            	
////		    	            	commMapper.insInfInfoHist(paramMap);
//		    	            	dataDao.query4Insert1("insInfInfoHist", paramMap);
//		                	} catch(Exception ex) {
//		                		ex.printStackTrace();
//		                	}
//		                }
//		            }
//		            //logger.debug("1738 "+ sbSql.toString());
//		        }
//			}
//		}
//        return results;
//	}
//	
///*
// * ========== CMS service start =============
// * */
//	/** 
//	 * 고객사 품목 마스터 정보 연계용 데이터 처리 서비스
//	 * 
//	 * @param  Map<String,String> paramMap
//	 * @return String results
//	 */
//    private String getTblCustItemsIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//    	
//    	int rsCnt = (Integer)dataDao.query4Object1("getTblCustItemsIFCnt", param);
//    	logger.info("getTblCustItemsIFCnt ::::::::::::::: "+ rsCnt);
//    	if (rsCnt > 0) {
//        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//        	long uuID = 0;
//        	if (rsMap.containsKey("uuID")) {
//        		uuID = Long.parseLong(rsMap.get("uuID").toString());
//        		param.put("uuID", uuID);       
//        		
//        		int affected = dataDao.query4Update1("updTblCustItemsIF", param);
//        		if (affected > 0) {
//        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblCustItemsIFList", param);
//        			if (rsList.size() > 0) {
//        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//        			}
//        		}
//        	}
//    	}        	
//        return results;         
//    }
//    
//    /** 
//	 * 품목 마스터 정보 연계용 데이터 처리 서비스
//	 * 
//	 * @param  Map<String,String> paramMap
//	 * @return String results
//	 */
//    private String getTblItemsIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//    	
//    	int rsCnt = (Integer)dataDao.query4Object1("getTblItemsIFCnt", param);
//    	logger.info("getTblItemsIFCnt ::::::::::::::: "+ rsCnt);
//    	if (rsCnt > 0) {
//        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//        	long uuID = 0;
//        	if (rsMap.containsKey("uuID")) {
//        		uuID = Long.parseLong(rsMap.get("uuID").toString());
//        		param.put("uuID", uuID);
//        		
//        		int affected = dataDao.query4Update1("updTblItemsIF", param);
//        		if (affected > 0) {
//        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblItemsIFList", param);
//        			if (rsList.size() > 0) {
//        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//        			}
//        		}
//        	}
//    	}        	
//        return results;         
//    }
//    
//    /** 
//	 * 매핑 품목 마스터 정보 연계용 데이터 처리 서비스
//	 * 
//	 * @param  Map<String,String> paramMap
//	 * @return String results
//	 */
//    private String getTblGpnItemsIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//    	
//    	int rsCnt = (Integer)dataDao.query4Object1("getTblGpnItemsIFCnt", param);
//    	logger.info("getTblItemsIFCnt ::::::::::::::: "+ rsCnt);
//    	if (rsCnt > 0) {
//        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//        	long uuID = 0;
//        	if (rsMap.containsKey("uuID")) {
//        		uuID = Long.parseLong(rsMap.get("uuID").toString());
//        		param.put("uuID", uuID);
//        		
//        		int affected = dataDao.query4Update1("updTblGpnItemsIF", param);
//        		if (affected > 0) {
//        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblGpnItemsIFList", param);
//        			if (rsList.size() > 0) {
//        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//        			}
//        		}
//        	}
//    	}        	
//        return results;         
//    }
//    
//    /**
//	 * 사용자권한 정보 연계용 데이터 처리 서비스
//	 * 
//	 * @param  Map<String,String> paramMap
//	 * @return String results
//	 */
//    private String getTblRoleScopesIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//    	
//    	int rsCnt = (Integer)dataDao.query4Object1("getTblRoleScopesIFCnt", param);
//    	logger.info("getTblRoleScopesIFCnt ::::::::::::::: "+ rsCnt);
//    	if (rsCnt > 0) {
//        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//        	long uuID = 0;
//        	if (rsMap.containsKey("uuID")) {
//        		uuID = Long.parseLong(rsMap.get("uuID").toString());
//        		param.put("uuID", uuID);       
//        		
//        		int affected = dataDao.query4Update1("updTblRoleScopesIF", param);
//        		if (affected > 0) {
//        			List<Map<String,Object>> rsList = dataDao.query4List1("getRoleScopesIFList", param);
//        			if (rsList.size() > 0) {
//        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//        			}
//        		}
//        	}
//    	}        	
//        return results;         
//    }
//    
//    
//    /**
//   	 * 권한별 XML 생성용 정보 데이터 처리 서비스
//   	 * 
//   	 * @param  Map<String,String> paramMap
//   	 * @return String results
//   	 */
//       private String getTblDictionaryIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//       	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//       	String locals = "ko|en|cn";
//       	String keyNames = "langKR|langEN|langCN";
//       	List<Map<String,Object>> rsList = dataDao.query4List1("getTblSyncDictionary", param);
////       	logger.info("getTblRoleMenuIF ::::::::::::::: "+ rsList);
//       	if (rsList != null && rsList.size() > 0) {
//           	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//           	long uuID = 0;
//           	if (rsMap.containsKey("uuID")) {
//           		uuID = Long.parseLong(rsMap.get("uuID").toString());
//           		param.put("uuID", uuID);       
//           		
//           		int affected = dataDao.query4Update1("updTblSyncDictionary", param);
//           		if (affected > 0) {
//           			Map<String, Object> retMap = new HashMap<String, Object>();
//           			param.put("dbSTS", "Y");
//           			
//           			List<Map<String,Object>> dictList = dataDao.query4List1("getTblDicList", param);
//           			String[] local = locals.split("\\|");
//           			String[] keyName = keyNames.split("\\|");
//       				for(int ii=0; ii<local.length; ii++) {
//       					String loc = local[ii];
//       					String keyNM = keyName[ii];
//       					
////       					this.saveDictionaryXml(dictList, keyNM, loc, null, "D:\\tmp\\upload");
//       					this.saveDictionaryXml(dictList, keyNM, loc, null, (String) param.get("MENU_PATH"));
//       				}
//       				
//           			if (rsList.size() > 0) {
//           				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//           			}
//           		}
//           	}
//       	}        	
//           return results;         
//       }
//    
//    /**
//	 * 권한별 XML 생성용 정보 데이터 처리 서비스
//	 * 
//	 * @param  Map<String,String> paramMap
//	 * @return String results
//	 */
//    private String getTblRoleMenuIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//    	
//    	List<Map<String,Object>> rsList = dataDao.query4List1("getTblSyncMenu", param);
////    	logger.info("getTblRoleMenuIF ::::::::::::::: "+ rsList);
//    	if (rsList != null && rsList.size() > 0) {
//        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//        	long uuID = 0;
//        	if (rsMap.containsKey("uuID")) {
//        		uuID = Long.parseLong(rsMap.get("uuID").toString());
//        		param.put("uuID", uuID);       
//        		
//        		int affected = dataDao.query4Update1("updTblSyncMenu", param);
//        		if (affected > 0) {
//        			
//        			for(int i=0; i<rsList.size(); i++) {
//        				Map<String,Object> paramMap = rsList.get(i);
//        				param.put("roleUID", paramMap.get("roleUID"));
//        				
//        				List<Map<String,Object>> menuTopList = dataDao.query4List1("getTblTopMenuList", param);
//        				List<Map<String,Object>> menuLeftList = dataDao.query4List1("getTblLeftMenuList", param);
//        				
//        				if(menuTopList.size() > 0) {
//        					this.saveTopMenuXml(menuTopList, (String) paramMap.get("roleID"), null, (String) param.get("MENU_PATH"));
//        				}
//        				
//        				if(menuLeftList.size() > 0) {
//        					this.saveLeftMenuXml(menuLeftList, (String) paramMap.get("roleID"), null, (String) param.get("MENU_PATH"));
//        				}
//        			}
//        			if (rsList.size() > 0) {
//        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//        			}
//        		}
//        	}
//    	}        	
//        return results;         
//    }
//    
//    public String saveLeftMenuXml(List<Map<String, Object>> val, String roleNM, String xmlGB, String MENU_PATH) throws Exception {
//		String retVal = null;
//		String filePath = null;
//		try {
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
// 
//            Document doc = docBuilder.newDocument();
//            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
//            ProcessingInstruction xmlstylesheet =  doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"topMenu.xsl\"");
//            
//            Element menus = doc.createElement("menus");
//            doc.appendChild(menus);
//            String menuKey = "";
//            Element menuLIST = null;
//            
//            if(val != null && val.size() > 0) {
//	            for(int ii=0; ii<val.size(); ii++) {
//	            	Map<String, Object> paramMap = val.get(ii);
//	            	if(menuKey.equals(paramMap.get("parMenuID"))) {
//	            		
//	            	} else {
//	            		Element menu = doc.createElement("menu");
//	                	menus.appendChild(menu);
//	                	
//	                	Element topMenu = doc.createElement("topMenuID");
//	                	topMenu.appendChild(doc.createTextNode("" + paramMap.get("topMenuID")));
//	                	
//	                	Element parMenuID = doc.createElement("parMenuID");
//	                	parMenuID.appendChild(doc.createTextNode("" + paramMap.get("parMenuID")));
//	                	
//	                	Element parMenuNM = doc.createElement("parMenuNM");
//	                	parMenuNM.appendChild(doc.createTextNode("" + paramMap.get("parMenuNM")));
//	                	
//	                	Element parLangEN = doc.createElement("parLangEN");
//	                	parLangEN.appendChild(doc.createTextNode("" + paramMap.get("parLangEN")));
//	                	
//	                	Element parLangCN = doc.createElement("parLangCN");
//	                	parLangCN.appendChild(doc.createTextNode("" + paramMap.get("parLangCN")));
//	                	
//	                	Element parLang = doc.createElement("parLang");
//	                	parLang.appendChild(doc.createTextNode("" + paramMap.get("parLang")));
//	                	
//	                	menuLIST = doc.createElement("menuLIST");
//	                	
//	                	menu.appendChild(topMenu);
//	                	menu.appendChild(parMenuID);
//	                	menu.appendChild(parMenuNM);
//	                	menu.appendChild(parLangEN);
//	                	menu.appendChild(parLangCN);
//	                	menu.appendChild(parLang);
//	                	menu.appendChild(menuLIST);
//	            	}
//	            	
//	            	Element xMenu = doc.createElement("xMenu");
//	            	menuLIST.appendChild(xMenu);
//	            	
//	            	Iterator<String> keys = paramMap.keySet().iterator();
//	            	while ( keys.hasNext() ) {
//	            		String key = keys.next();
//	            		if(!key.equals("topMenuID") && !key.equals("parMenuID") && !key.equals("parMenuNM") && !key.equals("parLangEN") && !key.equals("parLangCN") && !key.equals("parLang")) {
//	            			Element name = doc.createElement("" + key);
//	                		if(key.equals("linkUri")) {
//	                			name.appendChild(doc.createCDATASection("" + paramMap.get(key)));
//	                		} else {
//	                			name.appendChild(doc.createTextNode("" + paramMap.get(key)));
//	                		}
//	                		xMenu.appendChild(name);
//	            		}
//	            	}
//	            	menuKey = (String) paramMap.get("parMenuID");
//	            }
//            }
//            
//            Element root = doc.getDocumentElement();
//            doc.insertBefore(xmlstylesheet, root);
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
//            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
//            
//            if(xmlGB != null) {
//            	if(xmlGB.equals("local")) {
//            		filePath = "D:/tmp/upload/menus" + "/left";
//            	} else {
//            		filePath = MENU_PATH + "/left";
//            	}
//            } else {
//            	filePath = MENU_PATH + "/left";
//            }
//            
//            File file = new File(filePath);
//            if(!file.exists()) {
//            	file.mkdir();
//            }
//            
//            StreamResult result = new StreamResult(new FileOutputStream(new File(filePath + "/" + roleNM + ".xml")));
//            
//            DOMSource source = new DOMSource(doc); 	
//            
//            transformer.transform(source, result); 
//           
//            retVal = "SUCCEES";
//            
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//		return retVal;
//	}
//    
//    public String saveTopMenuXml(List<Map<String, Object>> val, String roleNM, String xmlGB, String MENU_PATH) throws Exception {
//		String retVal = null;
//		String filePath = null;
//		try {
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
// 
//            Document doc = docBuilder.newDocument();
//            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
//            ProcessingInstruction xmlstylesheet =  doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"topMenu.xsl\"");
//            
//            Element menus = doc.createElement("menus");
//            doc.appendChild(menus);
//            
//            if(val != null && val.size() > 0) {
//	            for(int ii=0; ii<val.size(); ii++) {
//	            	Map<String, Object> paramMap = val.get(ii);
//	            	Element menu = doc.createElement("menu");
//	            	menus.appendChild(menu);
//	            	Iterator<String> keys = paramMap.keySet().iterator();
//	            	while ( keys.hasNext() ) {
//	            		String key = keys.next();
//	            		Element name = doc.createElement("" + key);
//	            		if(key.equals("linkUri")) {
//	            			name.appendChild(doc.createCDATASection("" + paramMap.get(key)));
//	            		} else {
//	            			name.appendChild(doc.createTextNode("" + paramMap.get(key)));
//	            		}
//	                	
//	                    menu.appendChild(name);
//	            	}
//	            }
//            }
//            
//            Element root = doc.getDocumentElement();
//            doc.insertBefore(xmlstylesheet, root);
//            
//            // XML 파일로 쓰기
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
// 
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
//            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
//            
//            if(xmlGB != null) {
//            	if(xmlGB.equals("local")) {
//            		filePath = "D:/tmp/upload/menus" + "/top";
//            	} else {
//            		filePath = MENU_PATH + "/top";
//            	}
//            } else {
//            	filePath = MENU_PATH + "/top";
//            }
//            
//            File file = new File(filePath);
//            if(!file.exists()) {
//            	file.mkdirs();
//            }
//            
//            StreamResult result = new StreamResult(new FileOutputStream(new File(filePath + "/" + roleNM + ".xml")));
//            DOMSource source = new DOMSource(doc); 	
//            
//            transformer.transform(source, result); 
//            
//            retVal = "SUCCEES";
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//		return retVal;
//	}
//    
//    public String saveDictionaryXml(List<Map<String, Object>> val, String keyNM, String local, String xmlGB, String MENU_PATH) throws Exception {
//		String retVal = null;
//		String filePath = null;
////		logger.info("local ::: " + local + "  xmlGB ::: " + xmlGB + "  keyNM ::: " + keyNM);
//		try {
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
// 
//            Document doc = docBuilder.newDocument();
//            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
//            
//            Element menus = doc.createElement("properties");
//            doc.appendChild(menus);
//            
//            if(val != null && val.size() > 0) {
//	            for(int ii=0; ii<val.size(); ii++) {
//	            	Map<String, Object> paramMap = val.get(ii);
//	            	Element menu = doc.createElement("entry");
//	            	menu.setAttribute("key", (String) paramMap.get("dictID"));
//	            	menu.appendChild(doc.createCDATASection("" + paramMap.get(keyNM)));
//	            	menus.appendChild(menu);
//	            }
//            }
//            
//            // XML 파일로 쓰기
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
// 
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
//            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
//            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
//            DOMImplementation domImpl = doc.getImplementation();
//            DocumentType doctype = domImpl.createDocumentType("doctype",
//            	    "properties SYSTEM", "http://java.sun.com/dtd/properties.dtd");
//        	transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
//        	transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
//            
//            if(xmlGB != null) {
//            	if(xmlGB.equals("local")) {
//            		filePath = "D:/tmp/upload/dictionary";
//            	} else {
//            		filePath = MENU_PATH + "/dictionary";
//            	}
//            } else {
//            	filePath = MENU_PATH  + "/dictionary";
//            }
//            
//            File file = new File(filePath);
//            if(!file.exists()) {
//            	file.mkdirs();
//            }
//            
//            StreamResult result = new StreamResult(new FileOutputStream(new File(filePath + "/message_" + local + ".xml")));
//            DOMSource source = new DOMSource(doc); 	
//            
//            transformer.transform(source, result); 
////            logger.info("result :: " + result.toString());
//            retVal = "SUCCEES";
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//		return retVal;
//	}
//    
//    /** 
//	 * 고객사 품목 마스터 정보 연계용 데이터 처리 서비스
//	 * 
//	 * @param  Map<String,String> paramMap
//	 * @return String results
//	 */
//    private String getTblUserAuthIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
//    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
//    	
//    	int rsCnt = (Integer)dataDao.query4Object1("getTblUserAuthIFCnt", param);
//    	logger.info("getTblUserAuthIFCnt ::::::::::::::: "+ rsCnt);
//    	if (rsCnt > 0) {
//        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
//        	long uuID = 0;
//        	if (rsMap.containsKey("uuID")) {
//        		uuID = Long.parseLong(rsMap.get("uuID").toString());
//        		param.put("uuID", uuID);
//        		
//        		int affected = dataDao.query4Update1("updTblUserAuthIF", param);
//        		if (affected > 0) {
//        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblUserAuthIFList", param);
//        			if (rsList.size() > 0) {
//        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
//        			}
//        		}
//        	}
//    	}        	
//        return results;         
//    }
    
/*
 * ========== CMS service end =============
 * */
}
