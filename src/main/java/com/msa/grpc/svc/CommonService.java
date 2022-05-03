package com.msa.grpc.svc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.msa.cnf.CaseDo;
import com.msa.cnf.PIDSLoader;
import com.msa.grpc.ErrConstance;
import com.msa.grpc.dao.IDataDao;
import com.msa.grpc.dto.GrpcResp;
import com.msa.grpc.utils.GrpcDataUtil;

public class CommonService {
	
	Logger logger = LoggerFactory.getLogger(CommonService.class);

	private static Map<String, String> appMap = null;
	private static List<String> commInfos = null;
	
	private MsaChannel grpcChannel;
	
	private String defMsg  = "조회된 데이터가 없습니다.";
	
	static {
		appMap = new HashMap<String, String>();
		appMap.put("tbl_custitemsif", "getTblCustItemsIF");
		appMap.put("tbl_itemsif", "getTblItemsIF");
		appMap.put("tbl_gpnitemmapsif", "getTblGpnItemsIF");
		appMap.put("tbl_rolescopesif", "getTblRoleScopesIF");
		appMap.put("tbl_rolemenuif", "getTblRoleMenuIF");
		appMap.put("tbl_userauthif", "getTblUserAuthIF");
		appMap.put("tbl_dictionaryif", "getTblDictionaryIF");
		appMap.put("tbl_cmcodes", "getTblCmCode");
		
		
		commInfos = new ArrayList<String>();
		commInfos.add("agentID");
		commInfos.add("userIP");
		commInfos.add("serverIP");
		commInfos.add("userUID");
		commInfos.add("borgUID");
		commInfos.add("PID");
		commInfos.add("clang");
	}
	
	public GrpcResp query4Data(IDataDao dataDao, String sqlID, Map<String,Object> grpcMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "";
		String   jsonRet  = "";		
		GrpcResp grpcResp = new GrpcResp();
		//Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			if (!grpcMap.containsKey("clang")) {
//				paramMap.put("clang", this.CLANG);
			}
			
			logger.info("paramMap"+ grpcMap);
			List<Object> rsList = dataDao.query4List2(sqlID, grpcMap);
	        if (rsList == null || rsList.size() == 0) {
	        	errMsg   = defMsg;
	        	errCode  = ErrConstance.NO_DATA;
	    		jsonRet = GrpcDataUtil.getGrpcResults("NO_DATA", errMsg, null);
	        } else {    
	        	jsonRet = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
	        }			
		} catch(Exception ex) {
			ex.printStackTrace();
			errCode = ErrConstance.ERR_9999;
			errMsg  = ex.toString();
			jsonRet = GrpcDataUtil.getGrpcResults(errCode, errMsg, null);
        } finally {
        	if (grpcMap != null) {
        		try { grpcMap = null; } catch(Exception ex) { }
        	}
        }
		//logger.debug("jsonRet ::: "+ jsonRet);

		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
		
		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(jsonRet);
		
        return grpcResp;
	}
	
	public GrpcResp query4XmlData(IDataDao dataDao, Map<String,Object> paramMap, Map<String, Object> commInfoMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "";
		String   jsonRet  = "";
		Map<String, Object> finalMap = null;
		GrpcResp grpcResp = new GrpcResp();
		List<Map<String, Object>> params = null;
		String firstKey = null;
		List<String> reqKeys = null;
		String xmlGB = "";
		try {
			reqKeys  = (List<String>)paramMap.get("KEYS");
			params   = ((List<Map<String, Object>>)paramMap.get("params"));	
			finalMap = params.get(0);
			logger.info("finalMap :: " + finalMap);
			firstKey = new CommonService().getMapFirstKey4List(finalMap);
			xmlGB = (String) finalMap.get("xmlADDGB");
			if (params != null && params.size() > 0) {
				if(finalMap.get("xmlType") != null) {
					finalMap.putAll(commInfoMap);
//					String[] locals = null;
//					String[] keyNMs  = null;
//					String dictJsonRet = "";
					List<Map<String,Object>> dictList = dataDao.query4List1("getTblDicList", finalMap);
//					if(finalMap.get("local").toString().indexOf("|") >= 0) {
//						locals = finalMap.get("local").toString().split("\\|");
//						keyNMs = finalMap.get("keyNM").toString().split("\\|");
//					} else {
//						locals = new String[] {finalMap.get("local").toString()};
//						keyNMs = new String[] {finalMap.get("keyNM").toString()};
//					}
					
//					for(int ii=0; ii<locals.length; ii++) {
//						String local = locals[ii];
//						String keyNM = keyNMs[ii];
////						logger.info("local :: " + local + " :: keyNM ::" + keyNM + " xmlGB - " + xmlGB);
//						saveDictionaryXml(dictList, keyNM, local, xmlGB, (String) paramMap.get("MENU_PATH"));
//					}
					jsonRet = GrpcDataUtil.getGrpcResults("0", ErrConstance.NO_ERROR, GrpcDataUtil.getJsonStringFromList(dictList));
				} else if(finalMap.get("roleMap") != null){
					Map<String,Object>  dataMap = params.get(0);
					List<Map<String,Object>> paramList = getParamList(firstKey, dataMap, reqKeys, commInfoMap);
						Map<String,Object> sqlMap = new HashMap<String, Object>();
						sqlMap.putAll(commInfoMap);
						List<Map<String,Object>> roleMap = dataDao.query4List1("getEnableJobCache", sqlMap);
						String roleJsonRet = GrpcDataUtil.getJsonStringFromList(roleMap);
						jsonRet = GrpcDataUtil.getGrpcResults("0", ErrConstance.NO_ERROR, roleJsonRet);
				} else {
					for (int kk=0; kk<params.size(); kk++){	
						Map<String,Object>  dataMap = params.get(kk);
						// query4Updates에서 호출되는 경우만.... 메인 파라미터에서 필요한 파라미터를 공통에 저장함.
						// 여기서는 상위정보 값들만 대상임...LIST는 제외시킴. query4Updates가 아닌 경우는 이미 반영되었음.
						String roleJsonRet = "[";
						List<Map<String,Object>> paramList = getParamList(firstKey, dataMap, reqKeys, commInfoMap);
						
						logger.debug(xmlGB + " query4XmlData.paramList ::: "+ paramList);
						for (int yy=0; yy<paramList.size(); yy++) {
							Map<String,Object> sqlMap = paramList.get(yy);	
							sqlMap.putAll(commInfoMap);
							
							List<Map<String,Object>> menuTopList = dataDao.query4List1("getTblTopMenuList", sqlMap);
	        				List<Map<String,Object>> menuLeftList = dataDao.query4List1("getTblLeftMenuList", sqlMap);
	        				if(yy == 0) {
	        					roleJsonRet += "{\"" + sqlMap.get("proleID") + "_TOP\":"+ GrpcDataUtil.getJsonStringFromList(menuTopList) +"}";
	        				} else {
	        					roleJsonRet += ",{\"" + sqlMap.get("proleID") + "_TOP\":"+ GrpcDataUtil.getJsonStringFromList(menuTopList) +"}";
	        				}
	        				roleJsonRet += ",{\"" + sqlMap.get("proleID") +"_LEFT\":"+ GrpcDataUtil.getJsonStringFromList(menuLeftList) +"}";
						}
						roleJsonRet += "]";
//						jsonRet = roleJsonRet;
						jsonRet = GrpcDataUtil.getGrpcResults("0", ErrConstance.NO_ERROR, roleJsonRet);
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			errCode = ErrConstance.ERR_9999;
			errMsg  = ex.toString();
			jsonRet = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
        } finally {
        	if (paramMap != null) {
        		try { paramMap = null; } catch(Exception ex) { }
        	}
        }
		//logger.debug("jsonRet ::: "+ jsonRet);

		// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(jsonRet);
		
        return grpcResp;
	}
	
	public GrpcResp query4Update(IDataDao dataDao, String sqlID, Map<String,Object> paramMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "";
		String   jsonRet  = "";		
		GrpcResp grpcResp = new GrpcResp();
		try {
			//logger.debug("191 paramMap"+ paramMap);
			int affected = dataDao.query4Update1(sqlID, paramMap);
        	jsonRet = "{\"results\":[{\"errCode\":\"0\",\"errMsg\":\"SUCCESS\"}]}";		
		} catch(Exception ex) {
			ex.printStackTrace();
			errCode = ErrConstance.ERR_9999;
			errMsg  = ex.toString();
			jsonRet = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
        }
		
		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(jsonRet);
		
        return grpcResp;
	}
	
	public GrpcResp query4Execute(IDataDao dataDao, PIDSLoader pids, String PID, String flag, String sqlID, Map<String,Object> paramMap, Map<String,Object> commInfoMap) throws Exception {	
		int affected = 0;
		String jsonRet = null;
		String nextMsg = "continue";
		GrpcResp grpcResp = new GrpcResp();
		
		// getGetParams 태그 처리
		if (pids.getPIDSteps(PID).getGetParams() != null) {
			this.setGetKeyAndParams("query4Execute.381", PID, pids.getPIDSteps(PID).getGetParams(), paramMap, commInfoMap);
		}
		
		String caseDoRun = (pids.getPIDSteps(PID).getCaseDoRun()==null?"Before":pids.getPIDSteps(PID).getCaseDoRun());
		String getVar    = (pids.getPIDSteps(PID).getGetVar()==null?null:pids.getPIDSteps(PID).getGetVar());
		String caseRET   = (pids.getPIDSteps(PID).getCaseRET()==null?"Boolean":pids.getPIDSteps(PID).getCaseRET());
		CaseDo caseDo    = (pids.getPIDSteps(PID).getCaseDo()==null?null:pids.getPIDSteps(PID).getCaseDo());
		String execMode  = null;
		
		if ("Before".equals(caseDoRun)) {
			if (pids.getPIDSteps(PID).getPrevQuery() != null) {
				this.getOptionQuery(dataDao, pids, PID, "Prev", getVar, paramMap, null);
			}
			affected = this.first4Query(dataDao, pids, flag, sqlID, paramMap, commInfoMap, getVar);
			if (pids.getPIDSteps(PID).getPostQuery() != null) {
				jsonRet = this.getOptionQuery(dataDao, pids, PID, "Post", null, paramMap, commInfoMap);
			}
		}			
		
		String subQuery1 = null;	
		String subQuery2 = null;
		String throwMsg  = null;
		if (caseDo != null) {
			subQuery1  = (caseDo.getSubQuery1()==null?null:caseDo.getSubQuery1());
			subQuery2  = (caseDo.getSubQuery2()==null?null:caseDo.getSubQuery2());
			execMode   = (caseDo.getExecMode()==null?"Auto":caseDo.getExecMode());
			throwMsg   = (caseDo.getExecMode()==null?"Auto":caseDo.getThrowMsg());	
			try {
				nextMsg    = this.getSubQueryNew(getVar, caseRET, caseDo, paramMap, commInfoMap);	
			} catch(Exception ex) {
				nextMsg  = "throw";
				throwMsg = ex.getMessage();
				if (throwMsg.indexOf("#") >= 0) {
					throwMsg = throwMsg.split("\\#")[1];
				}
			}
			logger.info("417 "+ PID +"{'throwMsg':'"+ throwMsg +"'}");
			if (!"throw".equals(nextMsg)) {
				logger.info("419 " + " nextMsg : " + nextMsg + "pid :: " + PID +"{'getVar':'"+ getVar +"','caseIF':'"+ caseRET +"','subQuery1':'"+ subQuery1 +"','subQuery2':'"+ subQuery2 +"','execMode':'"+ execMode +"'}");
			}
		}
		if ("throw".equals(nextMsg)){
        	jsonRet = GrpcDataUtil.getGrpcResults("NOT_VALID", throwMsg, null);		        	
        	// Data 처리 후 Json 형식으로 변환하여 리턴하면 됨.
        	if ("query4Update".equals(flag) && "Before".equals(caseDoRun)) {
        		throw new Exception("@FAIL@"+throwMsg+"@FAIL@");
			} else { // if ("query4Data".equals(flag)) 
				grpcResp.setErrCode("FAIL");
				grpcResp.setErrMsg(throwMsg);
				grpcResp.setResults(jsonRet);
				
		        return grpcResp;
			}
		} else {		
			if ("After".equals(caseDoRun)) {
				if (pids.getPIDSteps(PID).getPrevQuery() != null) {
					this.getOptionQuery(dataDao, pids, PID, "Prev", getVar, paramMap, null);
				}
				affected = this.first4Query(dataDao, pids, flag, sqlID, paramMap, commInfoMap, getVar);
				if (pids.getPIDSteps(PID).getPostQuery() != null) {
					jsonRet = this.getOptionQuery(dataDao, pids, PID, "Post", null, paramMap, commInfoMap);
				}
			}
			String subPID = (paramMap.containsKey("subPID")==false?"":""+paramMap.get("subPID"));
			if (subPID.length() > 0) {
				return query4UpdateSub(dataDao, pids, "query4Update", subPID, affected, commInfoMap, paramMap);
			} else {
				if (jsonRet == null || jsonRet.length() == 0) { jsonRet = GrpcDataUtil.getGrpcResults("0", "SUCCESS", null); }
				
				grpcResp.setErrCode("0");
				grpcResp.setErrMsg("SUCCESS");
				grpcResp.setResults(jsonRet);
				
		        return grpcResp;
			}
		}
	}
	
	public String[] getSplitData(String src) {
		String[] keyNames = null;
		if (src.indexOf("|") >= 0) { // 여러 건(SYS0026_01|SYS0026_02 )
			keyNames = src.split("\\|");
		} else { // 단건 //SYS0026_01
			keyNames = new String[] {src};
		}
		return keyNames;
	}
	
	private void setGetKeyAndParams(String flag, String pid, String keyName, Map<String, Object> paramMap, Map<String, Object> sqlMap) {
		String[] keys  = this.getSplitData(keyName);
		for (int pp=0; pp<keys.length; pp++) {
			//logger.debug("249 "+ flag +".setGetKeyAndParams."+ keys[pp] +"::: "+ paramMap.get(keys[pp]));
			if (paramMap.get(keys[pp]) != null) {
				//logger.debug("251 "+ flag +".setGetKeyAndParams."+ keys[pp] +"::: "+ paramMap.get(keys[pp]) +"|||"+ sqlMap.containsKey(keys[pp]));
				if (!sqlMap.containsKey(keys[pp])) {
					Object tmpVal = paramMap.get(keys[pp]);
					if (flag.indexOf("NOT_LIST") >= 0) {
						if (tmpVal instanceof List) {							
						} else if (tmpVal instanceof Map) {
							sqlMap.put(keys[pp], tmpVal);
						} else if (tmpVal instanceof String) {							
							if (!String.valueOf(tmpVal).startsWith("[") 
									&& !String.valueOf(tmpVal).endsWith("]") 
									&& !String.valueOf(tmpVal).startsWith("{") 
									&& !String.valueOf(tmpVal).endsWith("}")) {
								sqlMap.put(keys[pp], paramMap.get(keys[pp]));
							}
						} else {							
							sqlMap.put(keys[pp], tmpVal);
						}
					} else {
						sqlMap.put(keys[pp], tmpVal);
					}
				}
			}
		}	
		return;
	}
	
	private String getOptionQuery(IDataDao dataDao, PIDSLoader pids, String pid, String flag, String getVar, Map<String, Object> params, Map<String, Object> commInfo) throws Exception {
		Object rsObj = null;
		Map<String, Object> rsMap = null;
		String retText   = "";
		String queryID   = "";
		String queryMeth = "";
		if ("Prev".equals(flag)) {
			if (pids.getPIDSteps(pid).getPrevQuery() != null) {
				queryID   = pids.getPIDSteps(pid).getPrevQuery();		
				queryMeth = (pids.getPIDSteps(pid).getPrevMethod()==null?"query4Update":pids.getPIDSteps(pid).getPrevMethod());
				
				if ("query4Update".equals(queryMeth)) {
					dataDao.query4Update1(queryID, params);
				} else if ("query4Data".equals(queryMeth)) {
					rsObj = dataDao.query4Object1(queryID, params);
					if (rsObj != null) {
						if (rsObj instanceof List) {
							
						} else {		
							if (rsObj instanceof Map) {
								rsMap = (Map<String, Object>)rsObj;
								if (commInfo != null) {
									commInfo.putAll(rsMap);
								}
								if (commInfo == null && params != null) {
									params.putAll(rsMap);
								}
								rsMap = null;
							} else {
								if (getVar != null) {
									if (commInfo != null) {
										commInfo.put(getVar, rsMap);
									} 
									if (commInfo == null && params != null) {
										params.put(getVar, rsMap);
									}
								}
							}
						}
					}
					rsObj = null;
				}
			}			
		} else {
			queryID   = pids.getPIDSteps(pid).getPostQuery();		
			queryMeth = (pids.getPIDSteps(pid).getPostMethod()==null?"query4Update":pids.getPIDSteps(pid).getPostMethod());	
			if ("query4Update".equals(queryMeth)) {
				dataDao.query4Update1(queryID, commInfo);
			} else if ("query4Data".equals(queryMeth)) { // supID에서는 허용되지 않음.
				rsObj = dataDao.query4Object1(queryID, commInfo);
				if (rsObj != null) {
					if (rsObj instanceof List) {
						List<Object> rsList = (List<Object>)rsObj;
						if (rsList == null || rsList.size() == 0) {
				    		retText = GrpcDataUtil.getGrpcResults("NO_DATA", ErrConstance.NO_DATA, null);
				        } else {   
				        	try {
				        		retText = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
				        	} catch(Exception ex) {
				        		retText = GrpcDataUtil.getGrpcResults("9999", ex.toString(), null);
				        	}
				        }
					} else {		
						if (rsObj instanceof Map) {
							rsMap = (Map<String, Object>)rsObj;
							if (commInfo != null) {
								commInfo.putAll(rsMap);
							}
							if (commInfo == null && params != null) {
								params.putAll(rsMap);
							}
							rsMap = null;
						} else {
							if (getVar != null) {
								if (commInfo != null) {
									commInfo.put(getVar, rsMap);
								} 
								if (commInfo == null && params != null) {
									params.put(getVar, rsMap);
								}
							}
						}
					}
				}
				rsObj = null;
			}
		}
		return retText;
	}
	
	public GrpcResp query4UpdateSub(IDataDao dataDao, PIDSLoader pids, String calling, String PID, int affected, Map<String, Object> commInfoMap, Map<String, Object> paramMap) throws Exception {
		String   errCode  = "0";
		String   errMsg   = "SUCCESS";
		String   jsonRet  = "";
		GrpcResp grpcResp = new GrpcResp();
		
		String   subPID = (paramMap.containsKey("subPID")==false?"":""+paramMap.get("subPID"));
		if (subPID.length() == 0) {
			if (PID.indexOf("|") >= 0) { subPID = PID; }
		}		
		boolean isPostQuery = true;
		logger.info("618 "+ PID +".query4UpdateSub."+ subPID +".paramMap:::::::::::::::"+ paramMap);
		if (subPID.length() > 0) {	
			//String subPID = (String)paramMap.get("subPID");
			String[] subPIDs = null;
			if (subPID.indexOf("|") >= 0) { // 여러 건(SYS0026_01|SYS0026_02 ), (SYS0044_01|SYS0044_02|SYS0044_03|SYS0044_04)
				subPIDs = subPID.split("\\|");
			} else { // 단건 //SYS0026_01
				subPIDs = new String[] {subPID};
			}
			for (int ii=0; ii<subPIDs.length; ii++) {
				subPID =  subPIDs[ii];	
				boolean isPrevQuery = true;
				if (ii==0 && "query4Execute".equals(calling)) {// 사전쿼리를 이미 실행하고 진입함.
					isPrevQuery = false;
				}
				boolean isExec = true;
				String[] retS  = null;
				isPostQuery = true;
				if (pids.getPIDSteps(subPID).getGetParams() != null) {
					this.setGetKeyAndParams("query4UpdateSub.637", subPID, pids.getPIDSteps(subPID).getGetParams(), paramMap, commInfoMap);	
				}
							
				Map<String, Object> subParamMap = null;
				String jsonReq = "";
				String queryId = pids.getPIDSteps(subPID).getQuery();  // SqlID
				String method  = pids.getPIDSteps(subPID).getMethod(); // method
				String caseDoRun = "Before";
				
				if ("query4Update".equals(method)) { // CaseIF를 먼저 확인함.
					if (pids.getPIDSteps(subPID).getCaseDo() != null) {	
						caseDoRun = (pids.getPIDSteps(subPID).getCaseDoRun()==null?"Before":pids.getPIDSteps(subPID).getCaseDoRun());	
					}
				}	
				if (isPrevQuery && !"query4Update".equals(method)) { // query4Update는 아래에서 별도로 처리함.
					// subPID 단계의 사전쿼리를 수행함.
					if (pids.getPIDSteps(subPID).getPrevQuery() != null) {
						this.getOptionQuery(dataDao, pids, subPID, "Prev", null, paramMap, commInfoMap);	
					}
				}
				if ("update4sub".equals(method)) {
					List<String> subReqKeys = null;
					String firstKey = "";
					if (paramMap.get(subPID) != null) {
						Object paramObj = paramMap.get(subPID);
//						logger.info("669 paramObj.getClass() instanceof ::: "+ paramObj.getClass());
						if (paramObj instanceof String) {
//							logger.info("671 Map::: "+ paramObj.getClass());
							jsonReq = "{\""+ subPID +"\":"+ paramMap.get(subPID) +"}";
							subParamMap = GrpcDataUtil.getParams(subPID, jsonReq, true);
							subReqKeys  = (List<String>)subParamMap.get("KEYS");
							Iterator<String> iterator = subParamMap.keySet().iterator();
							while (iterator.hasNext()) {
								String reqKey = iterator.next();	
								if ( subParamMap.get(reqKey) instanceof List && firstKey.length() == 0) {
									firstKey = reqKey;
								}
								subReqKeys.add(reqKey);
							}							
						} else if (paramObj instanceof Map) {
							subReqKeys  = new ArrayList<String>();
							subParamMap = new HashMap<String, Object>();							
//							logger.info("686 Map::: "+ paramObj.getClass());	
							Iterator<String> iterator = ((Map<String,Object>)paramObj).keySet().iterator();
							while (iterator.hasNext()) {
								String reqKey = iterator.next();	
								if ( ((Map<String,Object>)paramObj).get(reqKey) instanceof List && firstKey.length() == 0) {
									firstKey = reqKey;
								}
								subReqKeys.add(reqKey);
							}
							subParamMap.put(subPID, (Map<String,Object>)paramObj);
						} else if (paramObj instanceof List) {
//							logger.info("697 List ::: "+ paramObj.getClass());
						}						
						try {	
							if (firstKey.length() == 0) {
								firstKey = this.getFirstKey(subReqKeys);
							}
							this.update4sub(dataDao, pids, PID, queryId, subPID, firstKey, subReqKeys, subParamMap, commInfoMap);
						} catch (Exception ee) {
							ee.printStackTrace();
							throw ee;
						}
						subParamMap.putAll(commInfoMap);
					} else { //subPID에 파라미터가 없을 경우......
						subParamMap = new HashMap<String, Object>();
						subParamMap.putAll(commInfoMap);
						dataDao.query4Update1(queryId, subParamMap);
					}					
					jsonRet = GrpcDataUtil.getGrpcResults("0", "SUCCESS", null);
				} else if ("query4Data".equals(method)) { // query4Data일 경우 -> chkKey는 조건식에 대체될 변수인 getVar의 쿼리한 실제값
					jsonReq  = (paramMap.get(subPID) == null?"":""+paramMap.get(subPID));					
					//affected = this.query4DataNLoop(subPID, queryId, jsonReq, method, commInfoMap);
					retS = this.query4DataNLoop(dataDao, pids, subPID, queryId, jsonReq, method, commInfoMap);
					isExec = Boolean.valueOf(retS[0]);
					affected = 1;
				} else if ("query4Loop".equals(method)) { // query4Loop일 경우 -> chkKey는 조건식에 대체될 변수인 getVar의 쿼리한 실제값
					jsonReq  = (paramMap.get(subPID) == null?"":""+paramMap.get(subPID));					
					//affected = this.query4DataNLoop(subPID, queryId, jsonReq, method, commInfoMap);
					retS = this.query4DataNLoop(dataDao, pids, subPID, queryId, jsonReq, method, commInfoMap);
					isExec = Boolean.valueOf(retS[0]);
					affected = 1;
				} else if ("query4Update".equals(method) || "query4Msa".equals(method)) {
					jsonReq = (paramMap.get(subPID) == null?"":""+paramMap.get(subPID));
					if (jsonReq.length() > 0) {
						subParamMap = new Gson().fromJson(jsonReq, new TypeToken<Map<String,Object>>() {}.getType());
					} else {
						subParamMap = new HashMap<String,Object>();
					}
					subParamMap.putAll(commInfoMap);
					logger.info("710 query4UpdateSub."+ subPID +".subParamMap  :::::::::::::::"+ subParamMap);
					Map<String,Object> retMap = this.query4UpdateSubPIDNew(dataDao, pids, subPID, subParamMap, commInfoMap, isExec, caseDoRun);
					isPostQuery = (Boolean)(retMap.get("isPostQuery")==null?false:retMap.get("isPostQuery"));
					jsonRet     = (String)(retMap.get("jsonRet")==null?"":retMap.get("jsonRet"));
					isExec      = (Boolean)(retMap.get("isExec")==null?true:retMap.get("isExec"));
				}				
				if (isPostQuery && !"query4Update".equals(method) && !"query4Msa".equals(method)) { // query4Update는 위에서 별도로 처리함.
					if (pids.getPIDSteps(subPID).getPostQuery() != null) {
						jsonRet = this.getOptionQuery(dataDao, pids, subPID, "Post", null, null, subParamMap);
						isPostQuery = false;
					}
				}
			}		
		}
		if (isPostQuery) {
			if (PID.indexOf("|") >= 0) { 
				if (pids.getPIDSteps(subPID).getPostQuery() != null) {
					jsonRet = this.getOptionQuery(dataDao, pids, subPID, "Post", null, null, paramMap);
				}
			} else {
				if (pids.getPIDSteps(PID).getPostQuery() != null) {
					jsonRet = this.getOptionQuery(dataDao, pids, PID, "Post", null, null, paramMap);
				}
			}
		} 
		if (jsonRet.length() == 0){		
        	jsonRet = GrpcDataUtil.getGrpcResults(errCode, errMsg, null);
		}
		
		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(jsonRet);
		
        return grpcResp;
	}
	
	private int first4Query(IDataDao dataDao, PIDSLoader pids, String flag, String sqlID, Map<String, Object> params, Map<String, Object> commInfo, String getVar) throws Exception {
		int affected = 0;
		String pid   = ""+ commInfo.get("PID");
		if ("query4Update".equals(flag)) {
			affected = dataDao.query4Update1(sqlID, params);
		} else if ("query4Data".equals(flag) || "query4Select".equals(flag)) {
			Object rsObj = dataDao.query4Object1(sqlID, params);
			if (rsObj != null) {
				if (rsObj instanceof Map) {
					Map<String, Object> rsMap = (Map<String, Object>)rsObj;					
					affected = rsMap.size();
					params.putAll(rsMap);
					rsMap = null;
				} else if (rsObj instanceof List) {
					
				} else {				
					if (getVar != null) {
						affected = 1;
						params.put(getVar, rsObj);
					}
				}
			}
		}
		if (pids.getPIDSteps(pid).getGetKey() != null) {
			// 최초 쿼리 실행 후, selectKey나 query4Data로 리턴되는 데이터 중 getKey에 설정된 컬럼을 commInfoMap에 저장하여
			// 다음 스텝에서 사용할 수 있도록 함.
			this.setGetKeyAndParams("first4Query", pid, pids.getPIDSteps(pid).getGetKey(), params, commInfo);
		}
		return affected;
	}
	
	private String getSubQueryNew(String getVar, String caseRET, CaseDo caseDo, Map<String,Object> chkMap, Map<String,Object> commMap) throws Exception {
		String subQueryID = "";
		if (caseDo.getCaseIF() != null) {
			Object results = this.getCaseIFNew(getVar, caseDo.getCaseIF().getText(), caseRET, chkMap, commMap);
			if (results instanceof Boolean) {
				if ((Boolean)results) {		
					subQueryID = caseDo.getSubQuery1();
				} else {
					subQueryID = "";
				}
			} else {
				subQueryID = (String)results;
				if ("subQuery1".equals(subQueryID)) {
					subQueryID = caseDo.getSubQuery1();
				} else if ("subQuery2".equals(subQueryID)) {
					subQueryID = caseDo.getSubQuery2();
				} else if ("continue".equals(subQueryID)) {
					subQueryID = caseDo.getSubQuery1();
				} else if ("throw".equals(subQueryID)) {
					subQueryID = caseDo.getSubQuery2();
				} else if ("skip".equals(subQueryID)) {
					subQueryID = caseDo.getSubQuery2();
				} else if ("batch".equals(subQueryID)) {// query4Updates에서만...
					subQueryID = caseDo.getSubQuery1();
				} else {
					subQueryID = "";
				}
			}			
		}
		return subQueryID;
	}
	
	private Object getCaseIFNew(String getVar, String caseIF, String caseRET, Map<String,Object> rsMap, Map<String,Object> commMap) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");		
		String[] getVars = null;
		if (getVar != null) {
			if (getVar.indexOf("|") >= 0) {
				getVars = getVar.split("\\|"); //getVar.split("\\Q|\\E");
			} else {
				getVars = new String[] {getVar};
			}
		}
		logger.info("1374 getVars.length="+ getVars.length +" ::::: "+ caseIF +"");
		for (int cc=0; cc<getVars.length; cc++) {		
			String tmpVal = "";
			if (rsMap != null) {
				tmpVal = (rsMap.get(getVars[cc])==null?"":""+ rsMap.get(getVars[cc]));
				if (commMap != null && tmpVal.length() > 0) {
					if (!commMap.containsKey(getVars[cc])) {  commMap.put(getVars[cc], tmpVal);  }
				}
			}
			if (tmpVal.length() == 0 && commMap != null) {
				tmpVal = (commMap.get(getVars[cc])==null?"":""+ commMap.get(getVars[cc]));
			}		
			caseIF = caseIF.replace(""+ getVars[cc] +"", ""+ tmpVal);
			logger.info("1387 "+ getVars[cc] +"="+ tmpVal +" ::::: "+ caseIF +"");
		}		
		Object result = null;
		try {
			if ("Boolean".equals(caseRET)) {
				result = (Boolean)engine.eval(caseIF);
				logger.info("1393 getCaseIF.Boolean ::::: "+ result +"["+ caseIF +"]");
			} else if ("Choice".equals(caseRET)) {
				result = (String)engine.eval(caseIF);
				logger.info("1396 getCaseIF.Choice ::::: "+ getVar +" , subQuery ::::: "+ result);
			} else {
				logger.info("1398 getCaseIF.else ::::: "+ caseRET);
			}
			return result;
		} catch(Exception ex) {
			ex.printStackTrace();
			String errmsg = getPrintStackTrace(ex);
			if (errmsg.indexOf(getVars[0]) >= 0) {
				throw new Exception("$#[getCaseIFNew]getVar is not value.$#");
			} else {
				if (errmsg.indexOf("ClassCastException:") >= 0) {
					errmsg = errmsg.split("\\Qat \\E")[0].replaceAll(System.getProperty("line.separator"), "");
					errmsg = errmsg.split("\\QClassCastException:\\E")[1];
					logger.info("1410 :::::::::::::::::"+ errmsg);
					throw new Exception("$#[getCaseIFNew]"+ errmsg +"$#");
				} else {
					throw ex;
				}
			}
		} finally {
			if (result != null) { result = null; }
		}
	}
	
	private String getPrintStackTrace(Exception e) {        
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));         
        return errors.toString();         
    }
	
	private String getFirstKey(List<String> reqKeys) {
		int keyInx = 0;
		String firstKey = ""; // agentID,pMenuID,dtype,PID 제외
		String upperKey = "";				
		
		List<String> excepts = new ArrayList<String>();
		excepts.add("AGENTID");
		excepts.add("PMENUID");
		excepts.add("DTYPE");
		excepts.add("PID");
		excepts.add("USERIP");
		excepts.add("SERVERIP");
		excepts.add("USERUID");
		excepts.add("BORGUID");
		excepts.add("SQLMODE");
		excepts.add("CLANG");
		
		while (true) {
			firstKey = reqKeys.get(keyInx);
			upperKey = firstKey.toUpperCase();
			keyInx++;
			if (!excepts.contains(upperKey)) {
				break;
			}
		}
		return firstKey;
	}
	
	private Map<String,Object> query4UpdateSubPIDNew(IDataDao dataDao, PIDSLoader pids, String subPID, Map<String,Object> subParamMap
			, Map<String,Object> commMap, boolean isExec, String caseDoRun) throws Exception {
		boolean isExecL = true;
		String queryId = pids.getPIDSteps(subPID).getQuery();
		String method  = pids.getPIDSteps(subPID).getMethod();
		String afterDoCase = "continue";	
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("isPostQuery", true);
		retMap.put("jsonRet", "");
		retMap.put("isExec", isExecL);
		
		if (pids.getPIDSteps(subPID).getCaseDo() != null) {
			if ("After".equals(caseDoRun)) {					
				if (pids.getPIDSteps(subPID).getCaseDo().getCaseIF() != null) {
					String subSqlID = pids.getPIDSteps(subPID).getCaseDo().getSubQuery1();						
					String getVar   = (pids.getPIDSteps(subPID).getGetVar()==null?"":pids.getPIDSteps(subPID).getGetVar());
					String caseRET  = (pids.getPIDSteps(subPID).getCaseRET()==null?"Boolean":pids.getPIDSteps(subPID).getCaseRET());
					if (isExec) {
						String[] retS = this.subQuery4Data(dataDao, subSqlID, getVar, caseRET, pids.getPIDSteps(subPID).getCaseDo(), subParamMap, null);
						isExecL = Boolean.valueOf(retS[0]);
						afterDoCase = retS[1];
						retMap.put("isExec", isExecL);
						retMap.put("afterDoCase", afterDoCase);				
					}
				}	
			}
		}
		if ("continue".equals(afterDoCase)) {
			if (pids.getPIDSteps(subPID).getPrevQuery() != null) {
				this.getOptionQuery(dataDao, pids, subPID, "Prev", null, subParamMap, null);	
			}			
			if ("query4Msa".equals(method)) { // 다른 웹서비스를 호출하는 경우...
				String results = "";
				if ("updTblAttach4DocNO".equals(queryId)) {
					results = this.updTblAttach4DocNO(subParamMap);
					if (results.startsWith("ERROR")) {
						throw new Exception("@FAIL@Attach Info Update Error!@FAIL@");
					}
				 } else if ("insNewCustVdInfo".equals(queryId)) {
					results = this.insNewCustVdInfo(subParamMap);
					if (results.startsWith("ERROR")) {
						throw new Exception("@FAIL@New Cust Info Create Error!@FAIL@");
					}
				} else if ("delApvInfo4ApvUID".equals(queryId)) {
					results = this.delApvInfo4ApvUID(subParamMap);
					if (results.startsWith("ERROR")) {
						throw new Exception("@FAIL@Apv Info Delete Error!@FAIL@");
					}
				}
			} else {						
				dataDao.query4Update1(queryId, subParamMap);
			}
			if (pids.getPIDSteps(subPID).getPostQuery() != null) {
				String jsonRet = this.getOptionQuery(dataDao, pids, subPID, "Post", null, null, subParamMap);
				retMap.put("isPostQuery", false);
				retMap.put("jsonRet", jsonRet);
			}
			if (pids.getPIDSteps(subPID).getGetKey() != null) {
				this.setGetKeyAndParams("query4UpdateSub.699", subPID, pids.getPIDSteps(subPID).getGetKey(), subParamMap, commMap);
			}
			if (pids.getPIDSteps(subPID).getGetParams() != null) {
				this.setGetKeyAndParams("query4UpdateSub.702", subPID, pids.getPIDSteps(subPID).getGetParams(), subParamMap, commMap);	
			}
		} else if("skip".equals(afterDoCase)) {
			retMap.put("isPostQuery", false);
		}	
		if ("Before".equals(caseDoRun)) {
			if (pids.getPIDSteps(subPID).getCaseDo() != null) {
				if (pids.getPIDSteps(subPID).getCaseDo().getCaseIF() != null) {
					String subSqlID = pids.getPIDSteps(subPID).getCaseDo().getSubQuery1();						
					String getVar   = (pids.getPIDSteps(subPID).getGetVar()==null?"":pids.getPIDSteps(subPID).getGetVar());
					String caseRET  = (pids.getPIDSteps(subPID).getCaseRET()==null?"Boolean":pids.getPIDSteps(subPID).getCaseRET());
					if (isExec) {
						String[] retS = this.subQuery4Data(dataDao, subSqlID, getVar, caseRET, pids.getPIDSteps(subPID).getCaseDo(), subParamMap, null);
						isExecL = Boolean.valueOf(retS[0]);
						afterDoCase = retS[1];
						retMap.put("isExec", isExecL);
						retMap.put("afterDoCase", afterDoCase);		
					}
				}
			}
		}
		return retMap;
	}
	
	private void update4sub(IDataDao dataDao, PIDSLoader pids, String PID, String sqlID, String paramNM
			, String firstKey, List<String> reqKeys, Map<String, Object> paramMap
			, Map<String,Object> commInfo) throws Exception {	
		int dataSize = 0;
		String subPID = ("params".equals(paramNM)?PID:paramNM);				
		//logger.debug("1053 update4sub paramMap.get('"+ paramNM +"') ::: "+ paramMap.get(paramNM).getClass());
		//logger.debug("1054 (paramMap.get('"+ paramNM +"') instanceof Map) ::: "+ (paramMap.get(paramNM) instanceof Map));
		//logger.debug("1055 update4sub.paramMap="+ paramNM +":::::::::::::::"+ paramMap +" Map is"+ (paramMap.get(paramNM) instanceof Map));

		if (paramMap.get(paramNM) instanceof Map) {			
			Map<String, Object> sqlMap = (Map)paramMap.get(paramNM);	
			if (sqlMap.get(firstKey) instanceof List) {	// List 구조로 내부에 Map으로 파라미터가 있는 경우....처리 가능하도록????
//				logger.debug("1060 update4sub.sqlMap.get('"+ firstKey +"') instanceof List");
				// query4Updates에서 호출되는 경우만.... 메인 파라미터에서 필요한 파라미터를 공통에 저장함.
				// 여기서는 상위정보 값들만 대상임...LIST는 제외시킴. query4Updates가 아닌 경우는 이미 반영되었음.
				this.update4sub4MapInList(dataDao, pids, sqlID, paramNM, subPID, firstKey, reqKeys, sqlMap, commInfo);
			} else {
				String mapKey = this.getMapFirstKey4List(sqlMap);
//				logger.debug("1066 update4sub.sqlMap.get('"+ mapKey +"') instanceof Map ");
				if (mapKey != null) { // Map내에 파라미터가 List 구조인 경우....처리 가능하도록????
					this.update4sub4MapInList(dataDao, pids, sqlID, paramNM, subPID, mapKey, reqKeys, sqlMap, commInfo);
				} else {
					sqlMap.putAll(commInfo);
					dataDao.query4Update1(sqlID, sqlMap);
				}
			}
		} else {
			List<Map<String,Object>> reqData  = (List<Map<String,Object>>)paramMap.get(paramNM);
			dataSize = reqData.size();
//			logger.debug("1083 update4sub.reqData.size()=="+ dataSize);
			if (reqData != null && reqData.size() > 0) {				
				for (int kk=0; kk<reqData.size(); kk++){	
					Map<String,Object>  dataMap = reqData.get(kk);
					// query4Updates에서 호출되는 경우만.... 메인 파라미터에서 필요한 파라미터를 공통에 저장함.
					// 여기서는 상위정보 값들만 대상임...LIST는 제외시킴. query4Updates가 아닌 경우는 이미 반영되었음.
					if (kk == 0 ) {//상위 부모 컬럼정보를 설정함.
						if (pids.getPIDSteps(subPID).getGetKey() != null && "params".equals(paramNM)) {
							this.setGetKeyAndParams("NOT_LIST.1091", subPID, pids.getPIDSteps(subPID).getGetKey(), dataMap, commInfo);
						}	
						if (pids.getPIDSteps(subPID).getGetParams() != null && "params".equals(paramNM)) {
							this.setGetKeyAndParams("NOT_LIST.1094", subPID, pids.getPIDSteps(subPID).getGetParams(), dataMap, commInfo);
						}
					}					
					List<Map<String,Object>> paramList = this.getParamList(firstKey, dataMap, reqKeys, commInfo);
					if (dataSize < 100) {}
//						logger.debug("1098 update4sub.instanceof LIST -> paramList:::::::::::::::"+ paramList);
					
					// 테이블 2개 다중 Row 처리		
						
					String subSqlID      = null;
					String subMethod     = null;
					String mainMethod    = pids.getPIDSteps(subPID).getMethod();
					boolean isExec       = true;
					boolean isBatch      = false;
					CaseDo caseDo        = null;
					String caseDoRun     = "Before"; 
					String getKeys       = "";
					String getVar        = "";
					String getParams     = "";
					String caseRET       = "Boolean";
					String afterDoCase   = "continue"; //continue,throw,skip
					if (pids.getPIDSteps(subPID).getCaseDo() != null) {						
						caseDo    = pids.getPIDSteps(subPID).getCaseDo();
						subSqlID  = caseDo.getSubQuery1(); // subSqlID
						subMethod = caseDo.getSubMethod(); // subMethod
						getKeys   = (pids.getPIDSteps(subPID).getGetKey()==null?"":pids.getPIDSteps(subPID).getGetKey());
						getParams = (pids.getPIDSteps(subPID).getGetParams()==null?"":pids.getPIDSteps(subPID).getGetParams());
						getVar    = (pids.getPIDSteps(subPID).getGetVar()==null?"":pids.getPIDSteps(subPID).getGetVar());
						caseDoRun = (pids.getPIDSteps(subPID).getCaseDoRun()==null?"Before":pids.getPIDSteps(subPID).getCaseDoRun());
						caseRET   = (pids.getPIDSteps(subPID).getCaseRET()==null?"Boolean":pids.getPIDSteps(subPID).getCaseRET());
//						logger.debug("1122 update4sub.caseDoRun :::::::::::::::"+ caseDoRun);
//						logger.debug("1123 update4sub.getParams :::::::::::::::"+ getParams);
//						logger.debug("1124 update4sub.getKeys   :::::::::::::::"+ getKeys);
//						logger.debug("1125 update4sub.subSqlID  :::::::::::::::"+ subSqlID);
//						logger.debug("1126 update4sub.subMethod :::::::::::::::"+ subMethod);
					}					
					if (caseDo != null) {	// subQuery1="batch"는 성능을 잃지않고 List 파라미터로 대용량 처리를 하려고 할 때......
						if (subSqlID != null) {
							//if ("batch".equals(subSqlID) && "After".equals(caseDoRun)) {
							if ("batch".equals(subSqlID)) {
								isBatch = true;
							}
						}
					} else {
						isBatch = true;//CaseDo가 없는 경우는 그대로 진행해야 함.
					}
					Map<String,Object> subSqlMap = null;
					if (!isBatch) {
						for (int yy=0; yy<paramList.size(); yy++) {
							Map<String,Object> sqlMap = paramList.get(yy);	
							sqlMap.putAll(commInfo);
							if (dataSize < 100) {}
//								logger.debug("1143 update4sub.instanceof LIST -> paramList.sqlMap:::::::::::::::"+ sqlMap);
							if ("Before".equals(caseDoRun)) {								
								if ("query4Updates".equals(mainMethod)) {
									dataDao.query4Update1(sqlID, sqlMap);
								} else if ("query4Selects".equals(mainMethod)) { // 서브쿼리에 query4Selects 추가함.
									Object rsObj = dataDao.query4Object1(sqlID, sqlMap);									
//									logger.debug("1149 update4sub."+ sqlID +".:::::::::::::::"+ rsObj);
									if (rsObj != null) {
										if (rsObj instanceof Map) { 
											sqlMap.putAll((Map<String,Object>)rsObj); 
										} else {
											sqlMap.put(getVar, rsObj);
										}	
									}	
									rsObj = null;
								}
							}
							if (subSqlID != null) {
								if (isExec) {				
									subSqlMap = new HashMap<String,Object>();	
									subSqlMap.putAll(commInfo);										
									String[] retS = this.query4BatchInfoNew(dataDao, subSqlID, subSqlMap, sqlMap, getParams, getVar, caseRET, caseDo); 
									isExec      = Boolean.valueOf(retS[0]);
									afterDoCase = retS[1];
									if (dataSize < 100) {}
//										logger.debug("1167 update4sub.instanceof LIST -> paramList.subSqlMap:::::::::::::::"+ subSqlMap);
								}
								// 처리 결과 중 특정 키나 파라미터 값을 부모 레벨로 올려줌.
								if (yy == (paramList.size() - 1)) {
									if (pids.getPIDSteps(subPID).getGetParams() != null && "params".equals(paramNM)) {
										this.setGetKeyAndParams("NOT_LIST.1163", subPID, pids.getPIDSteps(subPID).getGetParams(), subSqlMap, commInfo);
									}
								}
							}
							if ("After".equals(caseDoRun)) {
								if (dataSize < 100) {} 
//									logger.debug("1177 update4sub.After -> "+ sqlID +".subSqlMap:::::::::::::::"+ sqlMap);								
								if ("continue".equals(afterDoCase)) { dataDao.query4Update1(sqlID, sqlMap); }
							}							
							// 처리 결과 중 특정 키나 파라미터 값을 부모 레벨로 올려줌.
							if (yy == (paramList.size() - 1)) {
								if (pids.getPIDSteps(subPID).getGetParams() != null && "params".equals(paramNM)) {
									this.setGetKeyAndParams("NOT_LIST.1183", subPID, pids.getPIDSteps(subPID).getGetParams(), sqlMap, commInfo);
								}
							}
						}
					} else { // 테이블 한건만 다중 Row 처리
						if (dataSize < 100) {}
//							logger.debug("1188 update4sub.query4Update3 -> "+ sqlID +".paramList:::::::::::::::"+ paramList);
						// caseSubQuery1=='batch" && caseDoRun=='After" 일 때만, CaseIF를 선시행하고, 배치처리를 지원함.
						if (subSqlID != null) {
							Map<String,Object> sqlMap = paramList.get(0);	
							sqlMap.putAll(commInfo);							
							if ("Before".equals(caseDoRun)) {				
								dataDao.query4Update3(sqlID, paramList);
							}							
							if (isExec) {				
								subSqlMap = new HashMap<String,Object>();		
								subSqlMap.putAll(commInfo);
								String[] retS = this.query4BatchInfoNew(dataDao, subSqlID, subSqlMap, sqlMap, getParams, getVar, caseRET, caseDo); 
								isExec      = Boolean.valueOf(retS[0]);
								afterDoCase = retS[1];
							}							
							if ("After".equals(caseDoRun)) {
								dataDao.query4Update3(sqlID, paramList);
							}							
						} else {
							dataDao.query4Update3(sqlID, paramList);
						}
					}
					//int dddd = 1/0;
				}
			}
		}
	}
	
	private String[] query4DataNLoop(IDataDao dataDao, PIDSLoader pids, String subPID, String sqlID, String jsonReq, String sqlMethod, Map<String, Object> commMap) throws Exception {
		
		Map<String, Object> subParamMap = null; 
		String[] chkKeys = null;		
		String[] retS = null;
		try {
			if (pids.getPIDSteps(subPID).getChkKey() != null) {
				String chkKey = pids.getPIDSteps(subPID).getChkKey();
				chkKeys = this.getSplitData(chkKey);
			} else {
				throw new Exception("@FAIL@checked chkKey@FAIL@");
			}
			String subQuery1  = null;
			String execMode   = "Auto";
			boolean isExec    = true;	

			String getVar  = (pids.getPIDSteps(subPID).getGetVar()==null?null:pids.getPIDSteps(subPID).getGetVar());
			String caseRET = (pids.getPIDSteps(subPID).getCaseRET()==null?"Boolean":pids.getPIDSteps(subPID).getCaseRET());
			CaseDo caseDo  = (pids.getPIDSteps(subPID).getCaseDo()==null?null:pids.getPIDSteps(subPID).getCaseDo());
			if (caseDo != null) {
				subQuery1  = caseDo.getSubQuery1(); // subSqlID		
				execMode   = (caseDo.getExecMode()==null?"Auto":caseDo.getExecMode());	
				//logger.debug(537 "execMode  :::::::::::::::"+ execMode);
			}					
//			logger.debug("545 "+ subPID +"///getVar:"+ getVar +"///caseIF:"+ caseRET +"///subQuery1:"+ subQuery1 +"///execMode:"+ execMode);
			//jsonReq = (paramMap.get(subPID) == null?"":""+paramMap.get(subPID));
			if (jsonReq.length() > 0) {
//				logger.debug("548 jsonReq:"+ jsonReq);
				subParamMap = new Gson().fromJson(jsonReq, new TypeToken<Map<String,Object>>() {}.getType());
				if (subParamMap.get(chkKeys[0]) instanceof List) {
					List<Object> reqVal  = (List<Object>)(subParamMap.get(chkKeys[0]) == null ? null: subParamMap.get(chkKeys[0]));
					if (reqVal != null && reqVal.size() > 0) {
						for (int bb=0; bb<reqVal.size(); bb++) {
							Map<String,Object> subQueryMap = new HashMap<String,Object>();
							subQueryMap.put(chkKeys[0], reqVal.get(bb));								
							Iterator<String> iterator = subParamMap.keySet().iterator();
							while (iterator.hasNext()) {
								String reqKey = iterator.next();									
								if (!chkKeys[0].equals(reqKey)) {
									List<Object> subPrms = (List<Object>)(subParamMap.get(reqKey)==null ? null: subParamMap.get(reqKey));	
									subQueryMap.put(reqKey, subPrms.get(bb));
								}
							}
							subQueryMap.putAll(commMap);	
							// query4Data일 경우 -> chkKey는 조건식에 대체될 변수인 getVar의 쿼리한 실제값
							Object chkVal = dataDao.query4Object1(sqlID, subQueryMap);	
							if (isExec) {
								retS   = this.subQuery4Data(dataDao, subQuery1, getVar, caseRET, caseDo, subQueryMap, chkVal);
								isExec = Boolean.valueOf(retS[0]);
							}
						}
					}
				} else { // 단순 맵인 경우
					List<Map<String,Object>> paramLst = this.getMap4SubQuery4LoopS(dataDao, sqlMethod, sqlID, getVar, subParamMap, commMap);
					for (Map<String,Object> rsMap : paramLst) {
						if (isExec) {
							// 기존 chkVal을 rsMap에 포함시켰음
							retS = this.subQuery4Data(dataDao, subQuery1, getVar, caseRET, caseDo, rsMap, null);
							isExec = Boolean.valueOf(retS[0]);
						}
					}
				}
			} else { // subPID에 파라미터가 없을 경우......
				List<Map<String,Object>> paramLst = this.getMap4SubQuery4LoopS(dataDao, sqlMethod, sqlID, getVar, null, commMap);
				for (Map<String,Object> rsMap : paramLst) {
					if (isExec) {
						retS = this.subQuery4Data(dataDao, subQuery1, getVar, caseRET, caseDo, rsMap, null);
						isExec = Boolean.valueOf(retS[0]);
					}
				}						
			}
			if (retS == null) {
				retS = new String[] {"true","continue"};
			}
			return retS;
		} catch(Exception ex) {
			throw ex;
		} finally {
        	if (chkKeys != null) {
        		try { chkKeys = null; } catch(Exception ex) { }
        	}
        	if (subParamMap != null) {
        		try { subParamMap = null; } catch(Exception ex) { }
        	}
        	if (retS != null) {
        		try { retS = null; } catch(Exception ex) { }
        	}
        }
	}

	private String[] subQuery4Data(IDataDao dataDao,String subQuery1, String getVar
			, String caseRET, CaseDo caseDo, Map<String,Object> subQueryMap, Object chkVal) throws Exception {
	
		Map<String,Object> chkMap = null;
		if (chkVal != null) {	
			if (chkVal instanceof List) {
				
			} else {		
				chkMap = new HashMap<String,Object>();
//				logger.debug("830 chkMap.getClass().getName():::::"+ chkMap.getClass().getName());
				if (chkVal instanceof Map) {
					Map<String, Object> rsMap = (Map<String, Object>)chkVal;
					chkMap.putAll(rsMap);
					subQueryMap.putAll(rsMap);
					rsMap = null;
				} else {
					if (getVar != null) {
						chkMap.put(getVar, chkVal);
					}
				}
			}
		}
		return this.query4SubQueryNew(
					dataDao
					,  "SubSqlNM"
					, subQuery1
					, chkMap
					, subQueryMap
					, getVar
					, caseRET
					, caseDo
					);
		//long ii = 1/0;
		//return isExec;
	}
	
	/**
	 * CaseDo의 ㅣSubQuery를 수행하는 서비스 메서드
	 * 
	 * @param  String mode       : SubSqlNM / SubSqlID / BatchSqlID
	 * @param  String subSqlID
	 * @param  Map<String,Object> commMap
	 * @param  Map<String,Object> subMap
	 * @param  String getVar
	 * @param  String caseRET
	 * @return CaseDo caseDo
	 */
	private String[] query4SubQueryNew(
			IDataDao dataDao
			,  String mode
			, String subSqlID
			, Map<String,Object> commMap
			, Map<String,Object> subMap
			, String getVar
			, String caseRET
			, CaseDo caseDo
			) throws Exception {
		//---------------------------------
		String[] returns = new String[] {"true","continue"};//returns[0] = "false,true", returns[1] = "continue,throw,skip"
		boolean prevNpost = true;
		String subQueryID = (subSqlID==null?"":subSqlID);
		String execMode   = (caseDo.getExecMode()==null?"Auto":caseDo.getExecMode());
		String subMethod  = caseDo.getSubMethod();						
		String prevSqlID  = (caseDo.getSubPrevQuery()==null?"":caseDo.getSubPrevQuery());
		String postSqlID  = (caseDo.getSubPostQuery()==null?"":caseDo.getSubPostQuery());
		String prevMethod = (caseDo.getSubPrevMethod()==null?subMethod:caseDo.getSubPrevMethod());
		String postMethod = (caseDo.getSubPostMethod()==null?subMethod:caseDo.getSubPostMethod());		
		String throwMsg   = (caseDo.getThrowMsg()==null?"":caseDo.getThrowMsg());
		
		if (caseDo.getCaseIF() != null) {
			subQueryID = this.getSubQueryNew(getVar, caseRET, caseDo, subMap, commMap);
			if (subQueryID.length() == 0) {//사전쿼리도 CaseIF가 조건을 만족할 때문 실행해야 함.
				prevNpost = false;
			}
		}
		
		if (!"throw".equals(subQueryID) && prevSqlID.length() > 0) {
			if ("skip".equals(subQueryID)) {// skip일 경우, 스킵한다.
				
			} else {			
				if (prevNpost) {//사전쿼리도 CaseIF가 조건을 만족할 때문 실행해야 함.
					if ("query4Update".equals(prevMethod)) { 
						dataDao.query4Update1(prevSqlID, subMap);
					} else if ("query4Updates".equals(prevMethod)) { // 다중쿼리는 추후 추가예정
						
					} else { // query4Data도 Case가 생길때, 추후 추가예정
						
					}
				}
			}
		}		
//		logger.debug("1305 query4SubQuery.subQueryID :::::::::::::::"+ subMethod +"<"+ subQueryID +">");
		if (subQueryID.length() > 0) {
			returns[1] = subQueryID; //"continue,throw,skip,batch"
			if ("continue".equals(subQueryID)) {				
			} else if ("skip".equals(subQueryID)) {	// After 일경우, step 쿼리들을 수행하지 않고, 스킵함....
//				logger.debug("1310 $#"+ subQueryID +"$#");
			} else if ("batch".equals(subQueryID)) {// After 일경우, batch이면 리스트 쿼리를 실행함.
//				logger.debug("1312 $#batch"+ subQueryID +"$#");
			} else if ("throw".equals(subQueryID)) {	//
//				logger.debug("1314 $#"+ throwMsg +"$#");
				throw new Exception("$#"+ throwMsg +"$#");
			} else {			
				if ("query4Update".equals(subMethod)) {
					dataDao.query4Update1(subQueryID, subMap);
				} else if ("query4Updates".equals(subMethod)) { // 다중쿼리는 추후 추가예정
					
				} else if ("query4Data".equals(subMethod)) {
					//logger.debug(1322 query4SubQuery.subQueryID +".왜 getUUID() 쿼리가 한 번만 호출될까????? :::::::::::::::"+ rsObj);
					Object rsObj = dataDao.query4Object1(subQueryID, subMap);
					//logger.debug(1324 query4SubQuery.subQueryID +".왜 getUUID() 쿼리가 한 번만 호출될까????? :::::::::::::::"+ rsObj);
					if (rsObj instanceof Map) {
						if (rsObj != null) {
							subMap.putAll((Map<String,Object>)rsObj);
							if ("update4sub4MapInList".equals(mode) && "One".equals(execMode)) {//하위 쿼리에 대한 부분을 공통맵에 저장해서 리턴함.
								commMap.putAll((Map<String,Object>)rsObj);
							}
						}
					}		
					rsObj = null;
					//logger.debug(1334 query4SubQuery.subQueryID +".subMap :::::::::::::::"+ subMap);
				} else {					
				}
			}
		}
		
		if (!"throw".equals(subQueryID) && postSqlID.length() > 0) {// throw 처리 추가 예정
			if ("skip".equals(subQueryID)) {// skip일 경우, 스킵한다.
				
			} else {			
				if (prevNpost) {//사후쿼리도 CaseIF가 조건을 만족할 때문 실행해야 함.
					if ("query4Update".equals(postMethod)) { 
						dataDao.query4Update1(postSqlID, subMap);
					} else if ("query4Updates".equals(postMethod)) { // 다중쿼리는 추후 추가예정
						
					} else {
						
					}
				}
			}
		}	
		if ("One".equals(execMode)) {	//One or Auto	
			returns[0] = "false";
		} else {
			returns[0] = "true";
		}
		return returns;
	}
	
	private void update4sub4MapInList(IDataDao dataDao, PIDSLoader pids, String sqlID, String paramNM, String subPID, String firstKey, List<String> reqKeys, Map<String, Object> sqlMap, Map<String, Object> commInfo) throws Exception {
		
		List<Map<String,Object>> paramList = null;
		List<Map<String,Object>> afterList = null;
		try {
			if ("params".equals(paramNM)) {//상위 부모 컬럼정보를 설정함.
				if (pids.getPIDSteps(subPID).getGetKey() != null) {
					this.setGetKeyAndParams("NOT_LIST.975", subPID, pids.getPIDSteps(subPID).getGetKey(), sqlMap, commInfo);
				}	
				if (pids.getPIDSteps(subPID).getGetParams() != null) {
					this.setGetKeyAndParams("NOT_LIST.978", subPID, pids.getPIDSteps(subPID).getGetParams(), sqlMap, commInfo);
				}
			}		
			paramList = this.getParamList4MapInList(firstKey, sqlMap, reqKeys, commInfo);	
			if (paramList != null && paramList.size() > 0) {
				String subSqlID   = null;
				//String subMethod  = null;
				boolean isExec     = true;
				CaseDo caseDo      = null;		
				String getVar      = null;
				String caseDoRun   = "Default";
				String caseRET     = null;
				String execMode    = "Auto";
				String afterDoCase = "continue";
				
				if (pids.getPIDSteps(subPID).getCaseDo() != null) {	
					caseDo     = pids.getPIDSteps(subPID).getCaseDo();
					subSqlID   = caseDo.getSubQuery1();			
					caseRET    = (pids.getPIDSteps(subPID).getCaseRET()==null?"Boolean":pids.getPIDSteps(subPID).getCaseRET());
					getVar     = (pids.getPIDSteps(subPID).getGetVar()==null?"":pids.getPIDSteps(subPID).getGetVar());
					caseDoRun  = (pids.getPIDSteps(subPID).getCaseDoRun()==null?"Before":pids.getPIDSteps(subPID).getCaseDoRun());
					execMode   = (caseDo.getExecMode()==null?"Auto":caseDo.getExecMode());
				}
				if ("Before".equals(caseDoRun) || "Default".equals(caseDoRun)) {			
//					logger.debug("1002 update4sub4MapInList."+ caseDoRun +" -> "+ sqlID +".paramList:::::::::::::::");
					dataDao.query4Update3(sqlID, paramList);
				} else {	
					if ("Auto".equals(execMode)) {
						afterList = new ArrayList<Map<String,Object>>();
					} else {
						afterList = paramList;
					}
				}
				for (Map<String,Object> sqlParam : paramList) {
					if (subSqlID != null) {
						if (isExec) {
							String[] retS = this.query4SubQueryNew(dataDao, "update4sub4MapInList"
									, subSqlID
									, sqlMap
									, sqlParam
									, getVar
									, caseRET
									, caseDo); 
							isExec      = Boolean.valueOf(retS[0]);
							afterDoCase = retS[1];
							if ("After".equals(caseDoRun) && "Auto".equals(execMode) && "continue".equals(afterDoCase)) {
								afterList.add(sqlParam);
							}
						} else {
							break;
						}
					}
				}			
				if ("After".equals(caseDoRun)) {			
//					logger.debug("1032 "+ caseDoRun +" -> "+ sqlID +".afterList:::::::::::::::");
					if (afterList != null && afterList.size() > 0) { dataDao.query4Update3(sqlID, afterList); }
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (paramList != null) {	paramList = null; }
			if (afterList != null) {	afterList = null; }
		}
	}
	
	public String getMapFirstKey4List(Map<String, Object> paramMap) {
		String firstKey = null;
		Iterator<String> iterator = paramMap.keySet().iterator();
		while (iterator.hasNext()) {
			String mapKey = iterator.next();	
			if ( paramMap.get(mapKey) instanceof List ) {
				firstKey = mapKey;
				break;
			}
		}
		return firstKey;
	}
	
	public List<Map<String,Object>> getParamList(String firstKey, Map<String,Object> dataMap
			, List<String> reqKeys, Map<String,Object> commInfo) {
		
		Map<String,Object> sqlParam = null;
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		//logger.debug("926 (dataMap.get(firstKey) instanceof List) -> "+ firstKey +"==="+ (dataMap.get(firstKey) instanceof List));
		if (dataMap.get(firstKey) instanceof List) {
			List<String> firstLst = (List<String>)dataMap.get(firstKey);
			for (int ll=0; ll<firstLst.size(); ll++) {
				sqlParam = new HashMap<String, Object>();		
				if (firstLst.get(ll) != null && !firstLst.get(ll).isEmpty()) {
					sqlParam.put(firstKey, firstLst.get(ll));
				}
				/**
				 * 파라미터에 컬럼은 있으나 값이 없는 경우 MSA에서 값을 인식 하지 못할때 사용
				else {
					firstLst.set(ll, "");
					sqlParam.put(firstKey,firstLst.get(ll));
				}
				**/
				//logger.debug("934 firstKey -> "+ sqlParam);
				for (String colID : reqKeys) {
					if (!colID.equals(firstKey)) {
						// 리스트가 아닌 데이터는 여기서는 저장하지 않음.
						if (dataMap.get(colID) instanceof List) {
							sqlParam.put(colID, GrpcDataUtil.getVal4MapList(dataMap,colID, ll));
						}
					}
				}
				sqlParam.putAll(commInfo);
				//logger.debug("944 sqlParam -> "+ sqlParam);
				paramList.add(sqlParam);
			}						
		} else {
			sqlParam = new HashMap<String, Object>();
			sqlParam.putAll(dataMap);
			sqlParam.putAll(commInfo);
			paramList.add(sqlParam);
		}		
		return paramList;
	}
	
	/**
	 * CaseIF의 SubQuery인 Batch 모드를 수행하는 서비스 메서드
	 * 
	 * @param  String subSqlID
	 * @param  Map<String,Object> subSqlMap
	 * @param  Map<String,Object> subMap
	 * @param  String getParams
	 * @param  String getVar
	 * @param  String caseRET
	 * @return CaseDo caseDo
	 */
	private String[] query4BatchInfoNew(IDataDao dataDao,String subSqlID, Map<String,Object> subSqlMap, Map<String,Object> sqlMap
			, String getParams, String getVar, String caseRET, CaseDo caseDo) throws Exception {
		//---------------------------------
		if (getParams != null) {
			String[] mapKeys = null;
			if (getParams.length() > 0) {									
				mapKeys = this.getSplitData(getParams);
				for (int nn=0; nn<mapKeys.length; nn++) {
					subSqlMap.put(mapKeys[nn], sqlMap.get(mapKeys[nn]));										
				}
				mapKeys = null;
			}
		}
		return this.query4SubQueryNew(dataDao
				,"BatchSqlID"
				, subSqlID
				, sqlMap
				, subSqlMap
				, getVar
				, caseRET
				, caseDo); 
	}
	
	private List<Map<String,Object>> getMap4SubQuery4LoopS(IDataDao dataDao, String mode, String sqlID, String getVar, Map<String,Object> paramMap, Map<String,Object> commMap) throws Exception {
		List<Map<String,Object>> paramLst = new ArrayList<Map<String,Object>>();		
		Map<String,Object> subMap = null;
		Object chkVal = null;
		String[] getVars = null;
		try {
			if (getVar != null) {
				if (getVar.indexOf("|") >= 0) {
					getVars = getVar.split("\\|");
				} else {
					getVars = new String[] {getVar};
				}
			}
			subMap = this.copyMap2Maps(commMap, paramMap);				
			if ("query4Loop".equals(mode)) {
				chkVal = dataDao.query4List4(sqlID, subMap);
			} else {
				chkVal = dataDao.query4Object1(sqlID, subMap);
			}
			if (chkVal instanceof List) {
				List<Map<String,Object>> rsLst = (List<Map<String,Object>>)chkVal;
				if (rsLst.size() > 0) {
					//쿼리한 데이터로 CaseDo를 반복해서 실행해야 함.
					for (int cc=0; cc < rsLst.size(); cc++) {
						Map<String,Object> retMap = this.copyMap2Maps(rsLst.get(cc), subMap);					
						paramLst.add(retMap);
					}
				}								
			} else if (chkVal instanceof Map) {	
				Map<String,Object> retMap = this.copyMap2Maps((Map<String,Object>)chkVal, subMap);	
				paramLst.add(retMap);
				if (commMap != null) {
					commMap.putAll((Map<String,Object>)chkVal);
				}
			} else {
				if (getVar != null) {
					subMap.put(getVars[0], chkVal);
					if (commMap != null && !commMap.containsKey(getVars[0])) {
						commMap.put(getVars[0], chkVal);
					}
				}
				paramLst.add(subMap);
			}
			return paramLst;
		} catch(Exception ex) {
			throw ex;
		} finally {
			if (getVars != null) {
        		try { getVars = null; } catch(Exception ex) { }
        	}
        	if (subMap != null) {
        		try { subMap = null; } catch(Exception ex) { }
        	}
        	if (paramLst != null) {
        		try { paramLst = null; } catch(Exception ex) { }
        	}
        }
	}
	
	private Map<String, Object> copyMap2Maps(Map<String, Object> tarMap, Map<String, Object> srcMap) throws Exception {
		Map<String, Object> copyMap = new HashMap<String, Object>();		
		try {
			if (srcMap != null) { copyMap.putAll(srcMap); }
			if (tarMap != null) {
				Iterator<String> iterator = tarMap.keySet().iterator();
				while (iterator.hasNext()) {
					String reqKey = iterator.next();	
					if ( !copyMap.containsKey(reqKey) ) {
						copyMap.put(reqKey, tarMap.get(reqKey));
					}
				}		
			}
			return copyMap;
		} catch(Exception ex) {
			throw ex;
	    } finally {
	    	if (copyMap != null) {
	    		try { copyMap = null; } catch(Exception ex) { }
	    	}
	    }
	}
	
	private List<Map<String,Object>> getParamList4MapInList(String firstKey, Map<String,Object> dataMap
			, List<String> reqKeys, Map<String,Object> commInfo) throws Exception {
		List<Map<String,Object>> paramList = new ArrayList<Map<String,Object>>();
		try {
			if (dataMap.get(firstKey) instanceof List) {
				List<String> firstLst = (List<String>)dataMap.get(firstKey);			
//				logger.debug("889 getParamList4MapInList firstLst.size()   :::::::::::::::"+ firstLst.size());
				for (int ll=0; ll<firstLst.size(); ll++) {
					Map<String,Object> sqlParam = new HashMap<String, Object>();				
					sqlParam.put(firstKey, firstLst.get(ll));
					for (String colID : reqKeys) {
						if (!colID.equals(firstKey)) {
							// 리스트가 아닌 데이터는 여기서는 저장하지 않음.
							if (dataMap.get(colID) instanceof List) {
								//if (ll > 14946) {
									//return (((List<String>)listMap.get(pname)).get(ll)==null?"":((List<String>)listMap.get(pname)).get(ll));
									//logger.info("898 getParamList4MapInList GrpcDataUtil.getVal4MapList."+ colID +":::::::::::::::"+ ((List)dataMap.get(colID)).size());
								//}
								sqlParam.put(colID, GrpcDataUtil.getVal4MapList(dataMap,colID, ll));
							}
						}
					}	
					//logger.debug("902 상위에서 저장한 공통 파라미터로 getKey와 getParams의 값들을 sqlMap에  전달함..."+ commInfo);
					sqlParam.putAll(commInfo);
					paramList.add(sqlParam);
				}				
			} else {
				Map<String,Object> sqlParam = new HashMap<String, Object>();
				sqlParam.putAll(dataMap);
				//logger.debug("909 상위에서 저장한 공통 파라미터로 getKey와 getParams의 값들을 sqlMap에  전달함..."+ commInfo);
				sqlParam.putAll(commInfo);		
				paramList.add(sqlParam);
			}
			return paramList;
		} catch(Exception ex) {
			throw ex;
		} finally {
			if (paramList != null) { paramList = null; }
		}
	}
	
	public GrpcResp query4Updates(IDataDao dataDao, PIDSLoader pids, String sqlID, String PID, Map<String,Object> paramMap, Map<String,Object> commInfoMap) throws Exception {
		String retText = "";
		String errCode = "0";
		String errMsg  = "";
		GrpcResp grpcResp = new GrpcResp();
		
		List<String> reqKeys = null;
		String firstKey = null;
		List<Map<String, Object>> params = null;	
		Map<String, Object> finalMap = null;
		int affected = 0;
		String subPID = null;
		reqKeys  = (List<String>)paramMap.get("KEYS");	
		params   = ((List<Map<String, Object>>)paramMap.get("params"));		
		finalMap = params.get(0);
		firstKey = this.getMapFirstKey4List(finalMap);
		if (firstKey == null) {
			firstKey = this.getFirstKey(reqKeys);
		}
		//logger.debug(""1483 query4Updates.paramMap@@@@@@@@@@@@@@@@@@"+ paramMap);		
		// getGetParams 태그 처리
		if (pids.getPIDSteps(PID).getGetKey() != null) {
			this.setGetKeyAndParams("NOT_LIST.1486", PID, pids.getPIDSteps(PID).getGetKey(), finalMap, commInfoMap);
		}
		if (pids.getPIDSteps(PID).getGetParams() != null) {
			this.setGetKeyAndParams("NOT_LIST.1489", PID, pids.getPIDSteps(PID).getGetParams(), finalMap, commInfoMap);
		}		
		//logger.debug("1491 query4Updates.commInfoMap@@@@@@@@@@@@@@@@@@"+ commInfoMap);

		// Main 사전쿼리를 수행함.
		if (pids.getPIDSteps(PID).getPrevQuery() != null) {
			this.getOptionQuery(dataDao, pids, PID, "Prev", null, commInfoMap, commInfoMap);
		}
		logger.debug("1497 query4Updates.commInfoMap @@@@@@@@@@@@@@@@@@ "+ commInfoMap);
		this.update4sub(dataDao, pids, PID, sqlID, "params", firstKey, reqKeys, paramMap, commInfoMap);
		// query4Updates의 사후쿼리에서 특정 데이터 조회하여 다음 단계로 전달하려면 resultType="hashmap"로 해야 함.
		// 왜냐하면, getVar는 현재 단계의 CaseIF에 변수를 치환하기 위한 부분이므로......	
		if (pids.getPIDSteps(PID).getPostQuery() != null) {
			this.getOptionQuery(dataDao, pids, PID, "Post", null, null, commInfoMap);
		} 
		subPID = (finalMap.containsKey("subPID")==false?"":""+finalMap.get("subPID"));
		if (subPID.length() > 0) {//서브를 호출한 경우는 처리하지 않는다.
			finalMap.putAll(commInfoMap);// subPID 파라미터에 공통 파라미터를 설정함.
			logger.debug("1508 query4Updates."+ subPID +".finalMap :::::::::::::::"+ finalMap);
			grpcResp = this.query4UpdateSub(dataDao, pids,"query4Updates", subPID, affected, commInfoMap, finalMap);
		}
		if (retText.length() == 0){		
			retText = GrpcDataUtil.getGrpcResults("0", ErrConstance.NO_ERROR, null);
		}
		
		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(retText);
		
        return grpcResp;
	}
	
	public GrpcResp query4Test(IDataDao dataDao, Map<String,Object> paramMap) throws Exception {
  		String   errCode  = "0";
  		String   errMsg   = "";
  		String   jsonRet  = "";	
  		GrpcResp grpcResp = new GrpcResp();
  		int      affected = 0;
  		try {
  			logger.info("query4Data.paramMap ::: "+ paramMap);
  			
  			if(paramMap.get("queryType").equals("S")) {
  				List<Object> rsList = (List<Object>)dataDao.query4List2("getQuery4Test", paramMap); 
//  				logger.info("query4Data.rsList ::: "+ rsList);
  				
  		        if (rsList == null || rsList.size() == 0) {
  		        	errCode = ErrConstance.NO_DATA;
  		    		errMsg  = "조회된 데이터가 없습니다.";
  		    		jsonRet = GrpcDataUtil.getGrpcResults("NO_DATA", errMsg, null);
  		        } else {    
  		        	jsonRet = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
  		        	//logger.debug("jsonRet:::::::::::::::"+ jsonRet);
  		        }
  			} else if(paramMap.get("queryType").equals("I")) {
  				affected = dataDao.query4Update1("insQuery4Test", paramMap);
  				jsonRet = "{\"results\":[{\"errCode\":\"0\",\"errMsg\":\"SUCCESS\"}]}";		
  			} else if(paramMap.get("queryType").equals("U")) {
  				affected = dataDao.query4Update1("updQuery4Test", paramMap);
  				jsonRet = "{\"results\":[{\"errCode\":\"0\",\"errMsg\":\"SUCCESS\"}]}";	
  			} else if(paramMap.get("queryType").equals("D")) {
  				affected = dataDao.query4Update1("delQuery4Test", paramMap);
  				jsonRet = "{\"results\":[{\"errCode\":\"0\",\"errMsg\":\"SUCCESS\"}]}";	
  			}
		} catch(Exception ex) {
			ex.printStackTrace();
			errCode = ErrConstance.ERR_9999;
			errMsg  = ex.getMessage();
			errMsg = errMsg.replaceAll("(\r\n|\r|\n|\n\r|\\p{Z}|\\t)", "");
			errMsg = errMsg.replaceAll("\\\\", "/");
			jsonRet = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
		}
  		
  		grpcResp.setErrCode(errCode);
		grpcResp.setErrMsg(errMsg);
		grpcResp.setResults(jsonRet);
		
        return grpcResp;
  	}
/*
 * ========== getMstInfo start =============
 * */
	/**
	 * Master 테이블 정보가 변경되었을 경우, 싱크하기 위한 서비스
	 * 
	 * @param  Map<String,Object> paramMap
	 * @return String results
	 */
	public String getMstInfo(IDataDao dataDao, Map<String,Object> paramMap) throws Exception {
	
		StringBuffer sbSel = new StringBuffer();
		StringBuffer sbSql = new StringBuffer();
		String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
		String ifUID = null;
		String tblName = ""+ paramMap.get("tblName");		
		logger.info("getMstInfo.tblName ::::::::::::::: "+ tblName);
		
//		if ("tbl_custitemsif".equals(tblName)) { // 마스터정보 연계 외
//			results = this.getTblCustItemsIF(dataDao, paramMap);
//		} else if("tbl_itemsif".equals(tblName)) {
//			results = this.getTblItemsIF(dataDao, paramMap);
//		} else if("tbl_gpnitemmapsif".equals(tblName)) {
//			results = this.getTblGpnItemsIF(dataDao, paramMap);
//		} else if ("tbl_rolescopesif".equals(tblName)) { // 권한설정용 연계
//			results = this.getTblRoleScopesIF(dataDao, paramMap);
//		} else if ("tbl_rolemenuif".equals(tblName)) { // 권한별 XML 생성용 정보 연계
//			results = this.getTblRoleMenuIF(dataDao, paramMap);
//		} else if ("tbl_userauthif".equals(tblName)) { // 권한설정용 연계
//			results = this.getTblUserAuthIF(dataDao, paramMap);
		if (appMap.containsKey(tblName)) {
			Class cls = this.getClass();
			
			Class partypes[] = new Class[2];
			partypes[0] = IDataDao.class;
			partypes[1] = Map.class;
			Method target = cls.getDeclaredMethod(appMap.get(tblName), partypes);

			results = (String) target.invoke(this, new Object[]{dataDao, paramMap});
		} else {
//			int affected = commMapper.updMstInfoList(paramMap);
			
			int affected = 0;
			
			String syncType = "" + paramMap.get("syncType");
			if("sync".equals(syncType)) {
				affected = 1;
			} else {
				affected = dataDao.query4Update1("updMstInfoList", paramMap);
			}
			
			if (affected > 0) {
				//logger.debug("1645 paramMap:::::::::::::::"+ paramMap);
//				List<Map<String,Object>> lst = commMapper.getMstInfoList(paramMap);
				List<Map<String,Object>> lst = dataDao.query4List1("getMstInfoList", paramMap);
		        if (lst == null || lst.size() == 0) {
	
		        } else { // tblName, keyCols, KeyVals, infCols
		        	int zz = 0;
		        	sbSel.append("SELECT \n");	
		        	for (Map<String,Object> rsMap : lst) {
		        		ifUID = (rsMap.get("ifUID")==null?"":"0"+rsMap.get("ifUID"));
		        		if (zz == 0) {
		        			//logger.debug("1656 infCols:::::::::::::::"+ rsMap.get("infCols"));     
		        			String infCols = (rsMap.get("infCols")==null?"":""+rsMap.get("infCols"));
		                	if (infCols.indexOf("|") >= 0) {
		                		String[] cols = infCols.split("\\|");
		                		for (int ii=0; ii<cols.length; ii++) {
		                			if (ii == 0) {
		                				sbSel.append(cols[ii]);
		                    		} else {
		                    			sbSel.append(", "+ cols[ii]);
		                    		}
		                		}
		                		sbSel.append(" \n");
		                	} else {
		                		sbSel.append(infCols +" \n");
		                	}        	
		                	sbSel.append("FROM "+ paramMap.get("tblName") +" \n");
		                	sbSel.append("WHERE ");
		
		                	String keyTyps = (rsMap.get("keyTyps")==null?"":""+rsMap.get("keyTyps")).toUpperCase();
		                	String keyCols = (rsMap.get("keyCols")==null?"":""+rsMap.get("keyCols"));
		                	//logger.debug("1676 keyCols:::::::::::::::"+ keyCols);
		                	//logger.debug("1677 keyTyps:::::::::::::::"+ keyTyps);  
		        			if (keyCols.indexOf("|") >= 0) {
		                		String[] keys = keyCols.split("\\|");
		                		String[] typs = keyTyps.split("\\|");
		                		for (int ii=0; ii<keys.length; ii++) {
		                			if (ii == 0) {
		               					if (typs[ii].startsWith("STRING") || typs[ii].startsWith("VARCHAR")) {
		                					sbSel.append(keys[ii] +"= '#"+ ii +"#' ");
		                				} else {
		                					sbSel.append(keys[ii] +"= #"+ ii +"# ");
		                				}
		                    		} else {
		                    			if (typs[ii].startsWith("STRING") || typs[ii].startsWith("VARCHAR")) {
		                    				sbSel.append(" AND "+ keys[ii] +"= '#"+ ii +"#' ");
		                				} else {
		                					sbSel.append(" AND "+ keys[ii] +"= #"+ ii +"# ");
		                				}
		                    		}
		                		}
		                		sbSel.append("\n");
		                	} else {
		                		if (keyTyps.startsWith("STRING") || keyTyps.startsWith("VARCHAR")) {
		                			sbSel.append(keyCols +"= '#0#' \n");
		        				} else {
		        					sbSel.append(keyCols +"= #0# \n");
		        				}
		                	}
		        		}        		
		        		String keyVals = (rsMap.get("keyVals")==null?"":""+rsMap.get("keyVals")); 
		        		//logger.debug("1706 keyVals:::::::::::::::"+ keyVals); 
		        		String sql = sbSel.toString();
		    			if (keyVals.indexOf("|") >= 0) {
		            		String[] vals = keyVals.split("\\|");
		            		for (int ii=0; ii<vals.length; ii++) {
		            			sql = sql.replaceAll("\\Q#"+ ii +"#\\E", vals[ii]);
		            		}
		            	} else {
		            		sql = sql.replaceAll("\\Q#0#\\E", keyVals);
		            	}    
		        		if (zz == 0) {
		        			sbSql.append(sql);
		        		} else {
		        			sbSql.append(" UNION ALL \n"+ sql);
		        		}
		        		zz++;
		        	}
		            List<Map<String,Object>> rsList = null;
		            if (sbSql.toString().length() > 0) {
//		            	rsList = commMapper.getSyncMstData(sbSql.toString());
		            	Map<String, Object> param = new HashMap<String, Object>();
		            	param.put("value", sbSql.toString());
		            	
		            	rsList = dataDao.query4List1("getSyncMstData", param);
		            	if (rsList == null || rsList.size() == 0) {
	
		                } else {
		                	results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
		                	try {
		    	            	paramMap.put("ifUID", ifUID);    	            	
//		    	            	commMapper.insInfInfoHist(paramMap);
		    	            	dataDao.query4Insert1("insInfInfoHist", paramMap);
		                	} catch(Exception ex) {
		                		ex.printStackTrace();
		                	}
		                }
		            }
		            //logger.debug("1738 "+ sbSql.toString());
		        }
			}
		}
        return results;
	}
/*
 * ========== CMS service start =============
 * */
	/** 
	 * 고객사 품목 마스터 정보 연계용 데이터 처리 서비스
	 * 
	 * @param  Map<String,String> paramMap
	 * @return String results
	 */
    private String getTblCustItemsIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
    	
    	int rsCnt = 0;
    	int affected = 0;
		String syncType = "" + param.get("syncType");
		if("sync".equals(syncType)) {
			rsCnt = 1;
			affected = 1;
		} else {
			rsCnt = (Integer)dataDao.query4Object1("getTblCustItemsIFCnt", param);
		}
    	
    	logger.info("getTblCustItemsIFCnt ::::::::::::::: "+ rsCnt);
    	if (rsCnt > 0) {
        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
        	long uuID = 0;
        	if (rsMap.containsKey("uuID")) {
        		uuID = Long.parseLong(rsMap.get("uuID").toString());
        		param.put("uuID", uuID);       
        		if(!"sync".equals(syncType)) {
        			affected = dataDao.query4Update1("updTblCustItemsIF", param);
        		}
        		if (affected > 0) {
        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblCustItemsIFList", param);
        			if (rsList.size() > 0) {
        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
        			}
        		}
        	}
    	}        	
        return results;         
    }
    
    /** 
	 * 품목 마스터 정보 연계용 데이터 처리 서비스
	 * 
	 * @param  Map<String,String> paramMap
	 * @return String results
	 */
    private String getTblItemsIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
    	
    	int rsCnt = 0;
    	int affected = 0;
		String syncType = "" + param.get("syncType");
		if("sync".equals(syncType)) {
			rsCnt = 1;
			affected = 1;
		} else {
			rsCnt = (Integer)dataDao.query4Object1("getTblItemsIFCnt", param);
		}
    	logger.info("getTblItemsIFCnt ::::::::::::::: "+ rsCnt);
    	if (rsCnt > 0) {
        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
        	long uuID = 0;
        	if (rsMap.containsKey("uuID")) {
        		uuID = Long.parseLong(rsMap.get("uuID").toString());
        		param.put("uuID", uuID);
        		
        		if(!"sync".equals(syncType)) {
        			affected = dataDao.query4Update1("updTblItemsIF", param);
        		}
        		if (affected > 0) {
        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblItemsIFList", param);
        			if (rsList.size() > 0) {
        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
        			}
        		}
        	}
    	}        	
        return results;         
    }
    
    /** 
	 * 매핑 품목 마스터 정보 연계용 데이터 처리 서비스
	 * 
	 * @param  Map<String,String> paramMap
	 * @return String results
	 */
    private String getTblGpnItemsIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
    	
    	int rsCnt = 0;
    	int affected = 0;
		String syncType = "" + param.get("syncType");
		if("sync".equals(syncType)) {
			rsCnt = 1;
			affected = 1;
		} else {
			rsCnt = (Integer)dataDao.query4Object1("getTblGpnItemsIFCnt", param);
		}
		
    	logger.info("getTblItemsIFCnt ::::::::::::::: "+ rsCnt);
    	if (rsCnt > 0) {
        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
        	long uuID = 0;
        	if (rsMap.containsKey("uuID")) {
        		uuID = Long.parseLong(rsMap.get("uuID").toString());
        		param.put("uuID", uuID);
        		
        		if(!"sync".equals(syncType)) {
        			affected = dataDao.query4Update1("updTblGpnItemsIF", param);
        		}
        		if (affected > 0) {
        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblGpnItemsIFList", param);
        			if (rsList.size() > 0) {
        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
        			}
        		}
        	}
    	}        	
        return results;         
    }
    
    /**
	 * 사용자권한 정보 연계용 데이터 처리 서비스
	 * 
	 * @param  Map<String,String> paramMap
	 * @return String results
	 */
    private String getTblRoleScopesIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
    	
    	int rsCnt = 0;
    	int affected = 0;
		String syncType = "" + param.get("syncType");
		if("sync".equals(syncType)) {
			rsCnt = 1;
			affected = 1;
		} else {
			rsCnt = (Integer)dataDao.query4Object1("getTblRoleScopesIFCnt", param);
		}
    	
    	logger.info("getTblRoleScopesIFCnt ::::::::::::::: "+ rsCnt);
    	if (rsCnt > 0) {
        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
        	long uuID = 0;
        	if (rsMap.containsKey("uuID")) {
        		uuID = Long.parseLong(rsMap.get("uuID").toString());
        		param.put("uuID", uuID);       
        		
        		if(!"sync".equals(syncType)) {
        			affected = dataDao.query4Update1("updTblRoleScopesIF", param);
        		}
        		if (affected > 0) {
        			List<Map<String,Object>> rsList = dataDao.query4List1("getRoleScopesIFList", param);
        			if (rsList.size() > 0) {
        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
        			}
        		}
        	}
    	}        	
        return results;         
    }
    
    
    /**
   	 * 권한별 XML 생성용 정보 데이터 처리 서비스
   	 * 
   	 * @param  Map<String,String> paramMap
   	 * @return String results
   	 */
       private String getTblDictionaryIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
       	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
       	String locals = "ko|en|cn";
       	String keyNames = "langKR|langEN|langCN";
       	List<Map<String,Object>> rsList = dataDao.query4List1("getTblSyncDictionary", param);
//       	logger.info("getTblRoleMenuIF ::::::::::::::: "+ rsList);
       	if (rsList != null && rsList.size() > 0) {
           	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
           	long uuID = 0;
           	if (rsMap.containsKey("uuID")) {
           		uuID = Long.parseLong(rsMap.get("uuID").toString());
           		param.put("uuID", uuID);       
           		
           		int affected = dataDao.query4Update1("updTblSyncDictionary", param);
           		if (affected > 0) {
           			Map<String, Object> retMap = new HashMap<String, Object>();
           			param.put("dbSTS", "Y");
           			
           			List<Map<String,Object>> dictList = dataDao.query4List1("getTblDicList", param);
           			String[] local = locals.split("\\|");
           			String[] keyName = keyNames.split("\\|");
//       				for(int ii=0; ii<local.length; ii++) {
//       					String loc = local[ii];
//       					String keyNM = keyName[ii];
//       					
////       					this.saveDictionaryXml(dictList, keyNM, loc, null, "D:\\tmp\\upload");
//       					this.saveDictionaryXml(dictList, keyNM, loc, null, (String) param.get("MENU_PATH"));
//       				}
       				
           			if (rsList.size() > 0) {
           				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(dictList) + "}";
           			}
           		}
           	}
       	}        	
           return results;         
       }
    
       private String getTblCmCode(IDataDao dataDao, Map<String,Object> paramMap) throws Exception { 
    	    StringBuffer sbSel = new StringBuffer();
	   		StringBuffer sbSql = new StringBuffer();
	   		String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
	   		String ifUID = null;
	   		String tblName = ""+ paramMap.get("tblName");	
    	   
	   		if("tbl_cmcodes".equals(tblName)) {
	   			paramMap.put("tblName", "tbl_codes");
	   		} else {
	   			paramMap.put("tblName", tblName);
	   		}
	   		
	   		int affected = 0;
			
			String syncType = "" + paramMap.get("syncType");
			if("sync".equals(syncType)) {
				affected = 1;
			} else {
				affected = dataDao.query4Update1("updMstInfoList4Wms", paramMap);
			}
			
			if (affected > 0) {
				//logger.debug("1645 paramMap:::::::::::::::"+ paramMap);
//				List<Map<String,Object>> lst = commMapper.getMstInfoList(paramMap);
				List<Map<String,Object>> lst = dataDao.query4List1("getMstInfoList", paramMap);
		        if (lst == null || lst.size() == 0) {
	
		        } else { // tblName, keyCols, KeyVals, infCols
		        	int zz = 0;
		        	sbSel.append("SELECT \n");	
		        	for (Map<String,Object> rsMap : lst) {
		        		ifUID = (rsMap.get("ifUID")==null?"":"0"+rsMap.get("ifUID"));
		        		if (zz == 0) {
		        			//logger.debug("1656 infCols:::::::::::::::"+ rsMap.get("infCols"));     
		        			String infCols = (rsMap.get("infCols")==null?"":""+rsMap.get("infCols"));
		                	if (infCols.indexOf("|") >= 0) {
		                		String[] cols = infCols.split("\\|");
		                		for (int ii=0; ii<cols.length; ii++) {
		                			if (ii == 0) {
		                				sbSel.append(cols[ii]);
		                    		} else {
		                    			sbSel.append(", "+ cols[ii]);
		                    		}
		                		}
		                		sbSel.append(" \n");
		                	} else {
		                		sbSel.append(infCols +" \n");
		                	}        	
		                	sbSel.append("FROM "+ paramMap.get("tblName") +" \n");
		                	sbSel.append("WHERE ");
		
		                	String keyTyps = (rsMap.get("keyTyps")==null?"":""+rsMap.get("keyTyps")).toUpperCase();
		                	String keyCols = (rsMap.get("keyCols")==null?"":""+rsMap.get("keyCols"));
		                	//logger.debug("1676 keyCols:::::::::::::::"+ keyCols);
		                	//logger.debug("1677 keyTyps:::::::::::::::"+ keyTyps);  
		        			if (keyCols.indexOf("|") >= 0) {
		                		String[] keys = keyCols.split("\\|");
		                		String[] typs = keyTyps.split("\\|");
		                		for (int ii=0; ii<keys.length; ii++) {
		                			if (ii == 0) {
		               					if (typs[ii].startsWith("STRING") || typs[ii].startsWith("VARCHAR")) {
		                					sbSel.append(keys[ii] +"= '#"+ ii +"#' ");
		                				} else {
		                					sbSel.append(keys[ii] +"= #"+ ii +"# ");
		                				}
		                    		} else {
		                    			if (typs[ii].startsWith("STRING") || typs[ii].startsWith("VARCHAR")) {
		                    				sbSel.append(" AND "+ keys[ii] +"= '#"+ ii +"#' ");
		                				} else {
		                					sbSel.append(" AND "+ keys[ii] +"= #"+ ii +"# ");
		                				}
		                    		}
		                		}
		                		sbSel.append("\n");
		                	} else {
		                		if (keyTyps.startsWith("STRING") || keyTyps.startsWith("VARCHAR")) {
		                			sbSel.append(keyCols +"= '#0#' \n");
		        				} else {
		        					sbSel.append(keyCols +"= #0# \n");
		        				}
		                	}
		        		}        		
		        		String keyVals = (rsMap.get("keyVals")==null?"":""+rsMap.get("keyVals")); 
		        		//logger.debug("1706 keyVals:::::::::::::::"+ keyVals); 
		        		String sql = sbSel.toString();
		    			if (keyVals.indexOf("|") >= 0) {
		            		String[] vals = keyVals.split("\\|");
		            		for (int ii=0; ii<vals.length; ii++) {
		            			sql = sql.replaceAll("\\Q#"+ ii +"#\\E", vals[ii]);
		            		}
		            	} else {
		            		sql = sql.replaceAll("\\Q#0#\\E", keyVals);
		            	}    
		        		if (zz == 0) {
		        			sbSql.append(sql);
		        		} else {
		        			sbSql.append(" UNION ALL \n"+ sql);
		        		}
		        		zz++;
		        	}
		            List<Map<String,Object>> rsList = null;
		            if (sbSql.toString().length() > 0) {
//		            	rsList = commMapper.getSyncMstData(sbSql.toString());
		            	Map<String, Object> param = new HashMap<String, Object>();
		            	param.put("value", sbSql.toString());
		            	
		            	rsList = dataDao.query4List1("getSyncMstData", param);
		            	if (rsList == null || rsList.size() == 0) {
	
		                } else {
		                	results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
		                	try {
		    	            	paramMap.put("ifUID", ifUID);    	            	
//		    	            	commMapper.insInfInfoHist(paramMap);
		    	            	dataDao.query4Insert1("insInfInfoHist", paramMap);
		                	} catch(Exception ex) {
		                		ex.printStackTrace();
		                	}
		                }
		            }
		            //logger.debug("1738 "+ sbSql.toString());
		        }
			}
			
           return results;         
       }
       
    /**
	 * 권한별 XML 생성용 정보 데이터 처리 서비스
	 * 
	 * @param  Map<String,String> paramMap
	 * @return String results
	 */
    private String getTblRoleMenuIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
    	
    	List<Map<String,Object>> rsList = dataDao.query4List1("getTblSyncMenu", param);
//    	logger.info("getTblRoleMenuIF ::::::::::::::: "+ rsList);
    	if (rsList != null && rsList.size() > 0) {
        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
        	long uuID = 0;
        	if (rsMap.containsKey("uuID")) {
        		uuID = Long.parseLong(rsMap.get("uuID").toString());
        		param.put("uuID", uuID);       
        		
        		int affected = dataDao.query4Update1("updTblSyncMenu", param);
        		if (affected > 0) {
        			
        			for(int i=0; i<rsList.size(); i++) {
        				Map<String,Object> paramMap = rsList.get(i);
        				param.put("roleUID", paramMap.get("roleUID"));
        				
        				List<Map<String,Object>> menuTopList = dataDao.query4List1("getTblTopMenuList", param);
        				List<Map<String,Object>> menuLeftList = dataDao.query4List1("getTblLeftMenuList", param);
        				
        				if(menuTopList.size() > 0) {
        					this.saveTopMenuXml(menuTopList, (String) paramMap.get("roleID"), null, (String) param.get("MENU_PATH"));
        				}
        				
        				if(menuLeftList.size() > 0) {
        					this.saveLeftMenuXml(menuLeftList, (String) paramMap.get("roleID"), null, (String) param.get("MENU_PATH"));
        				}
        			}
        			if (rsList.size() > 0) {
        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
        			}
        		}
        	}
    	}        	
        return results;         
    }
    
    public String saveLeftMenuXml(List<Map<String, Object>> val, String roleNM, String xmlGB, String MENU_PATH) throws Exception {
		String retVal = null;
		String filePath = null;
		try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
            ProcessingInstruction xmlstylesheet =  doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"topMenu.xsl\"");
            
            Element menus = doc.createElement("menus");
            doc.appendChild(menus);
            String menuKey = "";
            Element menuLIST = null;
            
            if(val != null && val.size() > 0) {
	            for(int ii=0; ii<val.size(); ii++) {
	            	Map<String, Object> paramMap = val.get(ii);
	            	if(menuKey.equals(paramMap.get("parMenuID"))) {
	            		
	            	} else {
	            		Element menu = doc.createElement("menu");
	                	menus.appendChild(menu);
	                	
	                	Element topMenu = doc.createElement("topMenuID");
	                	topMenu.appendChild(doc.createTextNode("" + paramMap.get("topMenuID")));
	                	
	                	Element parMenuID = doc.createElement("parMenuID");
	                	parMenuID.appendChild(doc.createTextNode("" + paramMap.get("parMenuID")));
	                	
	                	Element parMenuNM = doc.createElement("parMenuNM");
	                	parMenuNM.appendChild(doc.createTextNode("" + paramMap.get("parMenuNM")));
	                	
	                	Element parLangEN = doc.createElement("parLangEN");
	                	parLangEN.appendChild(doc.createTextNode("" + paramMap.get("parLangEN")));
	                	
	                	Element parLangCN = doc.createElement("parLangCN");
	                	parLangCN.appendChild(doc.createTextNode("" + paramMap.get("parLangCN")));
	                	
	                	Element parLang = doc.createElement("parLang");
	                	parLang.appendChild(doc.createTextNode("" + paramMap.get("parLang")));
	                	
	                	menuLIST = doc.createElement("menuLIST");
	                	
	                	menu.appendChild(topMenu);
	                	menu.appendChild(parMenuID);
	                	menu.appendChild(parMenuNM);
	                	menu.appendChild(parLangEN);
	                	menu.appendChild(parLangCN);
	                	menu.appendChild(parLang);
	                	menu.appendChild(menuLIST);
	            	}
	            	
	            	Element xMenu = doc.createElement("xMenu");
	            	menuLIST.appendChild(xMenu);
	            	
	            	Iterator<String> keys = paramMap.keySet().iterator();
	            	while ( keys.hasNext() ) {
	            		String key = keys.next();
	            		if(!key.equals("topMenuID") && !key.equals("parMenuID") && !key.equals("parMenuNM") && !key.equals("parLangEN") && !key.equals("parLangCN") && !key.equals("parLang")) {
	            			Element name = doc.createElement("" + key);
	                		if(key.equals("linkUri")) {
	                			name.appendChild(doc.createCDATASection("" + paramMap.get(key)));
	                		} else {
	                			name.appendChild(doc.createTextNode("" + paramMap.get(key)));
	                		}
	                		xMenu.appendChild(name);
	            		}
	            	}
	            	menuKey = (String) paramMap.get("parMenuID");
	            }
            }
            
            Element root = doc.getDocumentElement();
            doc.insertBefore(xmlstylesheet, root);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
            
            if(xmlGB != null) {
            	if(xmlGB.equals("local")) {
            		filePath = "D:/tmp/upload/menus" + "/left";
            	} else {
            		filePath = MENU_PATH + "/left";
            	}
            } else {
            	filePath = MENU_PATH + "/left";
            }
            
            File file = new File(filePath);
            if(!file.exists()) {
            	file.mkdir();
            }
            
            StreamResult result = new StreamResult(new FileOutputStream(new File(filePath + "/" + roleNM + ".xml")));
            
            DOMSource source = new DOMSource(doc); 	
            
            transformer.transform(source, result); 
           
            retVal = "SUCCEES";
            
        }catch (Exception e){
            e.printStackTrace();
        }
		return retVal;
	}
    
    public String saveTopMenuXml(List<Map<String, Object>> val, String roleNM, String xmlGB, String MENU_PATH) throws Exception {
		String retVal = null;
		String filePath = null;
		try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
            ProcessingInstruction xmlstylesheet =  doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"topMenu.xsl\"");
            
            Element menus = doc.createElement("menus");
            doc.appendChild(menus);
            
            if(val != null && val.size() > 0) {
	            for(int ii=0; ii<val.size(); ii++) {
	            	Map<String, Object> paramMap = val.get(ii);
	            	Element menu = doc.createElement("menu");
	            	menus.appendChild(menu);
	            	Iterator<String> keys = paramMap.keySet().iterator();
	            	while ( keys.hasNext() ) {
	            		String key = keys.next();
	            		Element name = doc.createElement("" + key);
	            		if(key.equals("linkUri")) {
	            			name.appendChild(doc.createCDATASection("" + paramMap.get(key)));
	            		} else {
	            			name.appendChild(doc.createTextNode("" + paramMap.get(key)));
	            		}
	                	
	                    menu.appendChild(name);
	            	}
	            }
            }
            
            Element root = doc.getDocumentElement();
            doc.insertBefore(xmlstylesheet, root);
            
            // XML 파일로 쓰기
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
 
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
            
            if(xmlGB != null) {
            	if(xmlGB.equals("local")) {
            		filePath = "D:/tmp/upload/menus" + "/top";
            	} else {
            		filePath = MENU_PATH + "/top";
            	}
            } else {
            	filePath = MENU_PATH + "/top";
            }
            
            File file = new File(filePath);
            if(!file.exists()) {
            	file.mkdirs();
            }
            
            StreamResult result = new StreamResult(new FileOutputStream(new File(filePath + "/" + roleNM + ".xml")));
            DOMSource source = new DOMSource(doc); 	
            
            transformer.transform(source, result); 
            
            retVal = "SUCCEES";
        } catch (Exception e){
            e.printStackTrace();
        }
		return retVal;
	}
//    
    public String saveDictionaryXml(List<Map<String, Object>> val, String keyNM, String local, String xmlGB, String MENU_PATH) throws Exception {
		String retVal = null;
		String filePath = null;
//		logger.info("local ::: " + local + "  xmlGB ::: " + xmlGB + "  keyNM ::: " + keyNM);
		try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            Document doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); //standalone="no" 를 없애준다.
            
            Element menus = doc.createElement("properties");
            doc.appendChild(menus);
            
            if(val != null && val.size() > 0) {
	            for(int ii=0; ii<val.size(); ii++) {
	            	Map<String, Object> paramMap = val.get(ii);
	            	Element menu = doc.createElement("entry");
	            	menu.setAttribute("key", (String) paramMap.get("dictID"));
	            	menu.appendChild(doc.createCDATASection("" + paramMap.get(keyNM)));
	            	menus.appendChild(menu);
	            }
            }
            
            // XML 파일로 쓰기
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
 
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행
            DOMImplementation domImpl = doc.getImplementation();
            DocumentType doctype = domImpl.createDocumentType("doctype",
            	    "properties SYSTEM", "http://java.sun.com/dtd/properties.dtd");
        	transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
        	transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
            
            if(xmlGB != null) {
            	if(xmlGB.equals("local")) {
            		filePath = "D:/tmp/upload/dictionary";
            	} else {
            		filePath = MENU_PATH + "/dictionary";
            	}
            } else {
            	filePath = MENU_PATH  + "/dictionary";
            }
            
            File file = new File(filePath);
            if(!file.exists()) {
            	file.mkdirs();
            }
            
            StreamResult result = new StreamResult(new FileOutputStream(new File(filePath + "/message_" + local + ".xml")));
            DOMSource source = new DOMSource(doc); 	
            
            transformer.transform(source, result); 
//            logger.info("result :: " + result.toString());
            retVal = "SUCCEES";
        } catch (Exception e){
            e.printStackTrace();
        }
		return retVal;
	}
    
    /** 
	 * 고객사 품목 마스터 정보 연계용 데이터 처리 서비스
	 * 
	 * @param  Map<String,String> paramMap
	 * @return String results
	 */
    private String getTblUserAuthIF(IDataDao dataDao, Map<String,Object> param) throws Exception { 
    	String results = GrpcDataUtil.getGrpcResults(ErrConstance.NO_DATA, ErrConstance.NO_DATA, null);
    	
    	int rsCnt = (Integer)dataDao.query4Object1("getTblUserAuthIFCnt", param);
    	logger.info("getTblUserAuthIFCnt ::::::::::::::: "+ rsCnt);
    	if (rsCnt > 0) {
        	Map<String,Object> rsMap = (Map<String,Object>)dataDao.query4Object1("getUUID", param);
        	long uuID = 0;
        	if (rsMap.containsKey("uuID")) {
        		uuID = Long.parseLong(rsMap.get("uuID").toString());
        		param.put("uuID", uuID);
        		
        		int affected = dataDao.query4Update1("updTblUserAuthIF", param);
        		if (affected > 0) {
        			List<Map<String,Object>> rsList = dataDao.query4List1("getTblUserAuthIFList", param);
        			if (rsList.size() > 0) {
        				results = "{\"results\":"+ new ObjectMapper().writeValueAsString(rsList) + "}";
        			}
        		}
        	}
    	}        	
        return results;         
    }
    
    /**
   * thr 서비스에 신규 고객 협력사 정보를 생성한다.
   * 
   * @param  Map<String, Object> paramMap : uuID|custVdID|custID|userUID|agentID|vdUID
   * @return String results
   */
  private String insNewCustVdInfo(Map<String, Object> paramMap) {   	
  	String jsonRet = null;
  	try {
  		
  		grpcChannel.openChannel((String) paramMap.get("THR_URI"), (String) paramMap.get("BUFFERTYPE"));
  		jsonRet = (String) grpcChannel.insNewCustVdInfo(paramMap, (String) paramMap.get("BUFFERTYPE"));
  		
      	return jsonRet;
      } catch(Exception ex) {
      	return "ERROR"+ ex.getMessage() +"]";
      } finally {
    	 if (grpcChannel != null)  { 
			try { grpcChannel.closeChannel(); grpcChannel = null; } catch(Exception e) { }
		}
      }
  }
    
  /**
 * thr 서비스에 결재정보를 백업 후 삭제한다.
 * 
 * @param  Map<String, Object> paramMap : agentID|userUID|uuID|apvUID
 * @return String results
 */
  private String delApvInfo4ApvUID(Map<String, Object> paramMap) {   	
		String jsonRet = null;
		try {
			grpcChannel.openChannel((String) paramMap.get("THR_URI"), (String) paramMap.get("BUFFERTYPE"));
			
			jsonRet = (String) grpcChannel.delApvInfo4ApvUID(paramMap, (String) paramMap.get("BUFFERTYPE"));
	    	
	    	return jsonRet;
	    } catch(Exception ex) {
	    	return "ERROR"+ ex.getMessage() +"]";
	    } finally {
	    	if (grpcChannel != null)  { 
				try { grpcChannel.closeChannel(); grpcChannel = null; } catch(Exception e) { }
			}
	    }
	}
  
//  private String updTblAttach4DocNO(Map<String, Object> paramMap) {
  public String updTblAttach4DocNO(Map<String, Object> paramMap) {
  	String jsonRet = null;
  	try {
  		logger.info("updTblAttach4DocNO ========= paramMap.get(\"TSYS_URI\")" + paramMap.get("TSYS_URI"));
  		logger.info("updTblAttach4DocNO ========= paramMap.get(BUFFERTYPE)" +  paramMap.get("BUFFERTYPE"));
  		logger.info("updTblAttach4DocNO ========= grpcChannel" +  grpcChannel);
  		grpcChannel = new MsaChannel();
  		grpcChannel.openChannel((String) paramMap.get("TSYS_URI"), (String) paramMap.get("BUFFERTYPE"));
  		jsonRet = (String) grpcChannel.updTblAttach4DocNO(paramMap, (String) paramMap.get("BUFFERTYPE"));
  		logger.info("CommonService grpcChannel.updTblAttach4DocNO jsonRet======== "+jsonRet);
      	return jsonRet;
      } catch(Exception ex) {
      	return "ERROR:: "+ ex.getMessage() +"]";
      } finally {
    	if (grpcChannel != null)  { 
			try { grpcChannel.closeChannel(); grpcChannel = null; } catch(Exception e) { }
		}
      }
  }
/*
 * ========== CMS service end =============
 * */
}