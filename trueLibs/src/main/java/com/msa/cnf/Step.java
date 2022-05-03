package com.msa.cnf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Step {
	
	@XmlAttribute(name="PID")
	private String pid;

	@XmlAttribute(name="service")
	private String service;
	
	@XmlAttribute(name="query")
	private String query;
	
	@XmlAttribute(name="method")
	private String method;
	
	@XmlAttribute(name="getKey")
	private String getKey;
	
	@XmlAttribute(name="getParams")
	private String getParams;
	
	@XmlAttribute(name="chkKey")
	private String chkKey;
	
	@XmlAttribute(name="getVar")
	private String getVar;
	
	@XmlAttribute(name="caseDoRun")
	private String caseDoRun;	
	
	@XmlAttribute(name="caseRET")
	private String caseRET;	
	
	@XmlElement(name="caseDo")
	private CaseDo caseDo; 
	
	@XmlAttribute(name="postQuery")
	private String postQuery;
	
	@XmlAttribute(name="postMethod")
	private String postMethod;
		
	@XmlAttribute(name="prevQuery")
	private String prevQuery;	
		
	@XmlAttribute(name="prevMethod")
	private String prevMethod;	
	
	public String getPostMethod() {
		return postMethod;
	}

	public void setPostMethod(String postMethod) {
		this.postMethod = postMethod;
	}

	public String getPrevMethod() {
		return prevMethod;
	}

	public void setPrevMethod(String prevMethod) {
		this.prevMethod = prevMethod;
	}

	public String getPrevQuery() {
		return prevQuery;
	}

	public void setPrevQuery(String prevQuery) {
		this.prevQuery = prevQuery;
	}

	public String getPostQuery() {
		return postQuery;
	}

	public void setPostQuery(String postQuery) {
		this.postQuery = postQuery;
	}

	public String getCaseDoRun() {
		return caseDoRun;
	}

	public void setCaseDoRun(String caseDoRun) {
		this.caseDoRun = caseDoRun;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getGetKey() {
		return getKey;
	}

	public void setGetKey(String getKey) {
		this.getKey = getKey;
	}

	public String getGetParams() {
		return getParams;
	}

	public void setGetParams(String getParams) {
		this.getParams = getParams;
	}

	public String getChkKey() {
		return chkKey;
	}

	public void setChkKey(String chkKey) {
		this.chkKey = chkKey;
	}

	public String getGetVar() {
		return getVar;
	}

	public void setGetVar(String getVar) {
		this.getVar = getVar;
	}

	public CaseDo getCaseDo() {
		return caseDo;
	}

	public void setCaseDo(CaseDo caseDo) {
		this.caseDo = caseDo;
	}

	public String getCaseRET() {
		return caseRET;
	}

	public void setCaseRET(String caseRET) {
		this.caseRET = caseRET;
	}
}
