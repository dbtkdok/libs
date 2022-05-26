package com.sci4s.err;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sci4s.grpc.ErrConstance;

public class ThrowException extends Exception {
	
	private static final long serialVersionUID = -7814465619823147715L;
	protected static final Logger logger = LoggerFactory.getLogger(ThrowException.class);
	private String errMsg;
	private String errCode;
	
	public ThrowException(String errMsg, String errCode) {
		super(errMsg);
		this.errMsg  = errMsg;
		this.errCode = errCode;
	}
	public ThrowException(String errMsg) {
		super(errMsg);
		this.errMsg  = errMsg;
		this.errCode = ErrConstance.ERR_9999;
	}	
	public String getErrCode() {
		return this.errCode;
	}
	public String getErrMsg() {
		String tmpMsg = this.errMsg;
		if (tmpMsg.length() < 200) {
			
		} else {		
			if (tmpMsg.indexOf("@") >= 0) {
				logger.error("tmpMsg.indexOf('@'):::::"+ tmpMsg);
				this.errMsg = tmpMsg.split("\\@")[2];
			} else if (tmpMsg.indexOf("###") >= 0) {
				logger.error("tmpMsg.indexOf('###'):::::"+ tmpMsg);
				this.errMsg = tmpMsg.split("\\Q###\\E")[1].replaceAll(System.getProperty("line.separator"), "");
			} else if (tmpMsg.indexOf("<eval>") >= 0) {
				logger.error("tmpMsg.indexOf('<eval>'):::::"+ tmpMsg);
				this.errMsg = tmpMsg.split("\\Q<eval>\\E")[1].replaceAll(System.getProperty("line.separator"), "");
			} else if (tmpMsg.indexOf("Caused by:") >= 0) {
				logger.error("tmpMsg.indexOf('Caused by:'):::::"+ tmpMsg);
				tmpMsg = tmpMsg.split("\\QCaused by:\\E")[1];
				this.errMsg = tmpMsg.split("\\Qat \\E")[0].replaceAll(System.getProperty("line.separator"), "");
				if (tmpMsg.indexOf("$#") >= 0) {
					logger.error("tmpMsg.indexOf('Caused by:->$#'):::::"+ tmpMsg);
					this.errMsg = tmpMsg.split("\\Q$#\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				} else if (tmpMsg.indexOf("BuilderException:") >= 0) {
					logger.error("tmpMsg.indexOf('Caused by:->BuilderException'):::::"+ tmpMsg);
					this.errMsg = tmpMsg.split("\\QBuilderException:\\E")[1].replaceAll(System.getProperty("line.separator"), "");
				}
			} else if (tmpMsg.indexOf("BuilderException:") >= 0) {
				logger.error("tmpMsg.indexOf('BuilderException'):::::"+ tmpMsg);
				this.errMsg = tmpMsg.split("\\QBuilderException:\\E")[1].replaceAll(System.getProperty("line.separator"), "");
			} else if (tmpMsg.indexOf("$#") >= 0) {
				logger.error("tmpMsg.indexOf('$#'):::::"+ tmpMsg);
				this.errMsg = tmpMsg.split("\\Q$#\\E")[1].replaceAll(System.getProperty("line.separator"), "");
			} else if (tmpMsg.indexOf("at ") >= 0) {
				logger.error("tmpMsg.indexOf('at'):::::"+ tmpMsg);
				if (tmpMsg.indexOf("FileNotFoundException") >= 0) {
					logger.error("tmpMsg.indexOf('FileNotFoundException'):::::"+ tmpMsg);
					this.errMsg = tmpMsg.split("\\)")[0].replaceAll(System.getProperty("line.separator"), "") + ")";
				} else {
					this.errMsg = tmpMsg.split("\\Qat \\E")[1].replaceAll(System.getProperty("line.separator"), "");
				}
			}   else {
				logger.error("else tmpMsg:::::"+ tmpMsg);
				if (tmpMsg.length() > 200) {
					this.errMsg = tmpMsg.substring(0, 200);
				}
			}		
		}
		return this.errMsg;
	}
}
