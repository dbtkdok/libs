package com.sci4s.cnf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="services")
@XmlAccessorType(XmlAccessType.FIELD)
public class Services {
	
	@XmlElement(name="proc")
	private List<Proc> procList;

	public List<Proc> getProcList() {
		return procList;
	}

	public void setProcList(List<Proc> procList) {
		this.procList = procList;
	}
}