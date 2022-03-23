package com.sci4s.err;

public class NotFoundPIDException extends Exception {
	
	private static final long serialVersionUID = -7810665619823147715L;
	private String errMsg;
	private String errCode;
	
	public NotFoundPIDException(String errMsg, String errCode) {
		super(errMsg);
		this.errMsg  = errMsg;
		this.errCode = errCode;
	}
	public NotFoundPIDException(String errMsg) {
		super(errMsg);
		this.errMsg  = errMsg;
		this.errCode = "8888";
	}	
	public String getErrCode() {
		return this.errCode;
	}
	public String getErrMsg() {
		return this.errMsg;
	}
}
