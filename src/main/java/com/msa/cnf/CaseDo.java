package com.msa.cnf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CaseDo {

	@XmlAttribute(name="service")
	private String service;
	
	@XmlAttribute(name="subPrevQuery")
	private String subPrevQuery;	
	
	@XmlAttribute(name="subPostQuery")
	private String subPostQuery;	
	
	@XmlAttribute(name="subQuery1")
	private String subQuery1;
	
	@XmlAttribute(name="subQuery2")
	private String subQuery2;
	
	@XmlAttribute(name="subMethod")
	private String subMethod;
	
	@XmlAttribute(name="subPrevMethod")
	private String subPrevMethod;
	
	@XmlAttribute(name="subPostMethod")
	private String subPostMethod;
	
	@XmlAttribute(name="execMode")
	private String execMode;
	
	@XmlAttribute(name="throwMsg")
	private String throwMsg;
	
	@XmlElement(name="caseIF")
	CData caseIF;

	public String getThrowMsg() {
		return throwMsg;
	}

	public void setThrowMsg(String throwMsg) {
		this.throwMsg = throwMsg;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSubPrevQuery() {
		return subPrevQuery;
	}

	public void setSubPrevQuery(String subPrevQuery) {
		this.subPrevQuery = subPrevQuery;
	}

	public String getSubPostQuery() {
		return subPostQuery;
	}

	public void setSubPostQuery(String subPostQuery) {
		this.subPostQuery = subPostQuery;
	}

	public String getSubQuery1() {
		return subQuery1;
	}

	public void setSubQuery1(String subQuery1) {
		this.subQuery1 = subQuery1;
	}

	public String getSubQuery2() {
		return subQuery2;
	}

	public void setSubQuery2(String subQuery2) {
		this.subQuery2 = subQuery2;
	}

	public String getSubMethod() {
		return subMethod;
	}

	public void setSubMethod(String subMethod) {
		this.subMethod = subMethod;
	}

	public String getSubPrevMethod() {
		return subPrevMethod;
	}

	public void setSubPrevMethod(String subPrevMethod) {
		this.subPrevMethod = subPrevMethod;
	}

	public String getSubPostMethod() {
		return subPostMethod;
	}

	public void setSubPostMethod(String subPostMethod) {
		this.subPostMethod = subPostMethod;
	}

	public String getExecMode() {
		return execMode;
	}

	public void setExecMode(String execMode) {
		this.execMode = execMode;
	}

	public CData getCaseIF() {
		return caseIF;
	}

	public void setCaseIF(CData caseIF) {
		this.caseIF = caseIF;
	}
}
