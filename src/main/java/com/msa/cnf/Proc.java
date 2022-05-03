package com.msa.cnf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Proc {
	
	@XmlElement(name="step")
	private List<Step> stepList;

	public List<Step> getStepList() {
		return stepList;
	}

	public void setStepList(List<Step> stepList) {
		this.stepList = stepList;
	}
}
