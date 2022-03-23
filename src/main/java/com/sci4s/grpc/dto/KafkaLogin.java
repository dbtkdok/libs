package com.sci4s.grpc.dto;

public class KafkaLogin {
	
	private String msgID;
	private String agentID;
	private String userUID;
	private String userIP;
	private String serverIP;
	private String userMac;
	private String userLocIP;
	private String csKey;
		
	public String getMsgID() {
		return msgID;
	}
	public void setMsgID(String msgID) {
		this.msgID = msgID;
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
	public String getUserMac() {
		return userMac;
	}
	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}
	public String getUserLocIP() {
		return userLocIP;
	}
	public void setUserLocIP(String userLocIP) {
		this.userLocIP = userLocIP;
	}
	public String getCsKey() {
		return csKey;
	}
	public void setCsKey(String csKey) {
		this.csKey = csKey;
	}
}
