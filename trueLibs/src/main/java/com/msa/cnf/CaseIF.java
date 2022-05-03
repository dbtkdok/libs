package com.msa.cnf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CaseIF {
	
	@XmlElement(name="caseIF")
	CData caseIF;
	
	@XmlAttribute(name="subQuery1")
	private String subQuery1;
	
	@XmlAttribute(name="subQuery2")
	private Boolean subQuery2;
	
	@XmlAttribute(name="subMethod")
	private CaseIF subMethod;

	public CData getCaseIF() {
		return caseIF;
	}

	public void setCaseIF(CData caseIF) {
		this.caseIF = caseIF;
	}

	public String getSubQuery1() {
		return subQuery1;
	}

	public void setSubQuery1(String subQuery1) {
		this.subQuery1 = subQuery1;
	}

	public Boolean getSubQuery2() {
		return subQuery2;
	}

	public void setSubQuery2(Boolean subQuery2) {
		this.subQuery2 = subQuery2;
	}

	public CaseIF getSubMethod() {
		return subMethod;
	}

	public void setSubMethod(CaseIF subMethod) {
		this.subMethod = subMethod;
	}
}
