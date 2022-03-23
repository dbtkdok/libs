package com.sci4s.err;

public class NotFoundBeanException extends Exception {
	
	private static final long serialVersionUID = -593288307358052436L;
	private String errMsg;
	private String errCode;
	
	public NotFoundBeanException(String errMsg, String errCode) {
		super(errMsg);
		this.errMsg  = errMsg;
		this.errCode = errCode;
	}
	public NotFoundBeanException(String errMsg) {
		super(errMsg);
		this.errMsg  = errMsg;
		this.errCode = "8887";
	}	
	public String getErrCode() {
		return this.errCode;
	}
	public String getErrMsg() {
		return this.errMsg;
	}
}
