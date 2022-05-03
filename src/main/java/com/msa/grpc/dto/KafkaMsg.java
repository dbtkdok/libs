package com.msa.grpc.dto;

public class KafkaMsg {
	
	private String agentID;
	private String pID;
	private String userUID;
	private String borgUID;
	private String csKey;
	private String userIP;
	private String serverIP;
	private String startTime;
	private String endTime;
	private long   runTime;
	private String errCode;
	private String errMsg;
	private String sqlID;
		
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public String getBorgUID() {
		return borgUID;
	}
	public void setBorgUID(String borgUID) {
		this.borgUID = borgUID;
	}
	public String getSqlID() {
		return sqlID;
	}
	public void setSqlID(String sqlID) {
		this.sqlID = sqlID;
	}
	public String getpID() {
		return pID;
	}
	public void setpID(String pID) {
		this.pID = pID;
	}	
	public String getAgentID() {
		return agentID;
	}
	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}
	public String getUserUID() {
		return userUID;
	}
	public void setUserUID(String userUID) {
		this.userUID = userUID;
	}
	public String getCsKey() {
		return csKey;
	}
	public void setCsKey(String csKey) {
		this.csKey = csKey;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
