package com.sci4s.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.sci4s.grpc.ErrConstance;
import com.sci4s.grpc.utils.GrpcDataUtil;

public class ErrUtil {
	
	public static String getPrintStackTrace(Exception e) {        
        StringWriter errors = new StringWriter();
        try {
        	e.printStackTrace(new PrintWriter(errors));         
        	return errors.toString();
        } catch(Exception ex) {
        	return ex.toString();
        } finally {
        	if (errors != null) errors = null; 
        }
    }
	
	public static String getPrintStackTrace(Throwable e) {  
        StringWriter errors = new StringWriter();
        try {
        	e.printStackTrace(new PrintWriter(errors));         
        	return errors.toString();
        } catch(Exception ex) {
        	return ex.toString();
        } finally {
        	if (errors != null) errors = null; 
        }     
    }
	
	public static String getErrorResults(Exception e) { 
		String errMsg = ErrUtil.getPrintStackTrace(e);
		String results = null;
		try {
			if (errMsg.indexOf("@") >= 0) {
				errMsg = errMsg.split("\\@")[2];
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			} else if (errMsg.indexOf("###") >= 0) {
				errMsg = errMsg.split("\\Q###\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			} else if (errMsg.indexOf("<eval>") >= 0) {
				errMsg = errMsg.split("\\Q<eval>\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			} else if (errMsg.indexOf("Caused by:") >= 0) {
				errMsg = errMsg.split("\\QCaused by:\\E")[1];
				errMsg = errMsg.split("\\Qat \\E")[0].replaceAll(System.getProperty("line.separator"), "");
				if (errMsg.indexOf("$#") >= 0) {
					errMsg = errMsg.split("\\Q$#\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				} else if (errMsg.indexOf("BuilderException:") >= 0) {
					errMsg = errMsg.split("\\QBuilderException:\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				}
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);	
			} else if (errMsg.indexOf("BuilderException:") >= 0) {
				errMsg = errMsg.split("\\QBuilderException:\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			} else if (errMsg.indexOf("$#") >= 0) {
				errMsg = errMsg.split("\\Q$#\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			} else if (errMsg.indexOf("at ") >= 0) {
				errMsg = errMsg.split("\\Qat \\E")[1].replaceAll(System.getProperty("line.separator"), "");
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			} else {
				if (errMsg.length() > 200) {
					errMsg = errMsg.substring(0, 200);
				}
				results = GrpcDataUtil.getGrpcResults(ErrConstance.ERR_9999, errMsg, null);
			}
			return results;
        } catch(Exception ex) {
        	return ex.toString();
        } finally {
        	if (errMsg  != null) errMsg  = null; 
        	if (results != null) results = null;
        }   
	}
}
