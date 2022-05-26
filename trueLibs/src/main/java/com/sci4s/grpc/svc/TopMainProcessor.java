package com.sci4s.grpc.svc;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sci4s.cnf.PIDSLoader;
import com.sci4s.err.NotFoundBeanException;
import com.sci4s.err.NotFoundPIDException;
import com.sci4s.err.ThrowException;
import com.sci4s.grpc.ErrConstance;
import com.sci4s.grpc.dto.GrpcParams;
import com.sci4s.grpc.dto.GrpcResp;
import com.sci4s.grpc.utils.FlatDataUtil;
import com.sci4s.utils.ErrUtil;

public class TopMainProcessor {
	
	protected Logger logger = LoggerFactory.getLogger(TopMainProcessor.class);
	private String xmlUri;	
	private PIDSLoader pids;	
	private String masBufferType;
	
	private String sqlMode;
	private String mstChkTime;
	private String clang;
		
	public String getSqlMode() {
		return sqlMode;
	}
	public void setSqlMode(String sqlMode) {
		this.sqlMode = sqlMode;
	}
	public String getMstChkTime() {
		return mstChkTime;
	}
	public void setMstChkTime(String mstChkTime) {
		this.mstChkTime = mstChkTime;
	}
	public String getClang() {
		return clang;
	}
	public void setClang(String clang) {
		this.clang = clang;
	}
	public void setXmlUri(String xmlUri) {
		this.xmlUri = xmlUri;
	}
	public String getXmlUri(String xmlUri) {
		return this.xmlUri;
	}	
	public void setMasBufferType(String buffer) {      
		this.masBufferType = buffer;
    }
	public String getMasBufferType() {      
		return this.masBufferType;
    }
	public void setPIDSLoader(PIDSLoader pids) throws ThrowException {   
		this.pids = pids;
    }
	public PIDSLoader getPIDSLoader() throws ThrowException {      
		try { 
			if (this.pids == null) {
				return PIDSLoader.getInstance(this.xmlUri, this.masBufferType);
			} else {
				return this.pids;
			}
		} catch(Exception ex) {
			throw new ThrowException(ErrUtil.getPrintStackTrace(ex), "9000");
		}
    }	
	protected Object getServiceBean(String svcName) throws NotFoundBeanException { 
		try {
			return Class.forName(svcName).newInstance(); 
		} catch(Exception ex) {
			throw new NotFoundBeanException(ErrUtil.getPrintStackTrace(ex), "8887");
		}
    }
	protected GrpcResp getMstInfoSvc(Map<String, Object> paramMap) throws Exception {
		return null;
	}
	
	public GrpcResp callRMsg(GrpcParams grpcPrms) {
		String   results  = "";
		String   errMsg   = null;
		GrpcResp grpcResp = new GrpcResp();		
		String   PID      = grpcPrms.getpID();
		PIDSLoader pids   = null;
		try {
			pids = this.getPIDSLoader();
			String svcName  = pids.getPIDSteps(PID).getService(); // 서비스명
			String queryId  = pids.getPIDSteps(PID).getQuery();   // SqlID
			String methName = pids.getPIDSteps(PID).getMethod();  // 호출할 메서드명
			
			logger.info("service ::: " + svcName);
			logger.info("queryId ::: " + queryId);
			logger.info("method  ::: " + methName);
			
			Object beanObj = getServiceBean(svcName);
			if ("N".equals(queryId)) {
				Class prmTypes[] = { GrpcParams.class };
				Method method = beanObj.getClass().getDeclaredMethod(methName, prmTypes);
				grpcResp = (GrpcResp) method.invoke(beanObj, new Object[] { grpcPrms });
			} else {
				Class prmTypes[] = { String.class, GrpcParams.class };
				Method method = beanObj.getClass().getDeclaredMethod(methName, prmTypes);
				grpcResp = (GrpcResp) method.invoke(beanObj, new Object[] { queryId, grpcPrms });
			}
			return grpcResp;
		} catch (NotFoundBeanException nb) {
			grpcResp = FlatDataUtil.getErrGrpcResp("NotFoundPIDException", nb.getErrCode(), nb.getErrMsg());		
			return grpcResp;
		} catch (NotFoundPIDException ne) {
			grpcResp = FlatDataUtil.getErrGrpcResp("NotFoundPIDException", ne.getErrCode(), ne.getErrMsg());		
			return grpcResp;
		} catch (ThrowException te) {
			grpcResp = FlatDataUtil.getErrGrpcResp("ThrowException", te.getErrCode(), te.getErrMsg());		
			return grpcResp;
		} catch (Exception e1) {
			errMsg = ErrUtil.getPrintStackTrace(e1);
			grpcResp = FlatDataUtil.getErrGrpcResp("Exception", ErrConstance.ERR_9999, errMsg);
			return grpcResp;
		} finally {		
			if (grpcPrms != null) { try { grpcPrms = null; } catch (Exception e1) {} }
			if (errMsg   != null) { try { errMsg   = null; } catch (Exception e1) {} }
			if (results  != null) { try { results  = null; } catch (Exception e1) {} }
			if (grpcResp != null) { try { grpcResp = null; } catch (Exception e1) {} }
			if (pids     != null) { try { pids     = null; } catch (Exception e1) {} }
		}	
	}
	
	public GrpcResp getMstInfo(Map<String, Object> params) {
		GrpcResp grpcResp = null;
		try {
			params.put("chkTime", Integer.parseInt((this.mstChkTime==null?"5":this.mstChkTime)));// 예) 5분전까지 데이터 조회
			params.put("SQLMODE", this.sqlMode); 			
			if (!params.containsKey("clang")) {
				params.put("clang", this.clang);
			}			
			return getMstInfoSvc(params);
		} catch (Exception e1) {
			grpcResp = FlatDataUtil.getErrGrpcResp("Exception", ErrConstance.ERR_9999, ErrUtil.getPrintStackTrace(e1));	
			return grpcResp;
		} finally {
			if (grpcResp != null) grpcResp = null;
		}
	}
}
