package com.msa.cnf;

import javax.xml.bind.annotation.XmlAttribute;

public class PIDSteps {

	private String pid;
	private String service;
	private String query;
	private String method;
	private String getKey;
	private String addSubParams;
	private String getParams;
	private String chkKey;
	private String getVar;
	private String caseDoRun;
	private String caseRET;
	private CaseDo caseDo; 
	private String postQuery;
	private String prevQuery;
	private String postMethod;
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
	public CaseDo getCaseDo() {
		return caseDo;
	}
	public void setCaseDo(CaseDo caseDo) {
		this.caseDo = caseDo;
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
	public String getAddSubParams() {
		return addSubParams;
	}
	public void setAddSubParams(String addSubParams) {
		this.addSubParams = addSubParams;
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
	
	public String getCaseRET() {
		return caseRET;
	}

	public void setCaseRET(String caseRET) {
		this.caseRET = caseRET;
	}
}
