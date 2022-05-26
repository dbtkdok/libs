package com.sci4s.cnf;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "pids")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pids {
	
	@XmlElement(name="import")
	private List<Import> importList;

	public List<Import> getImportList() {
		return importList;
	}

	public void setImportList(List<Import> importList) {
		this.importList = importList;
	}
}