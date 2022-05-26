package com.sci4s.cnf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class CData {
	
	@XmlJavaTypeAdapter(value=Adapter.class) 
	@XmlValue
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private static class Adapter extends XmlAdapter<String, String> {  
		@Override  
		public String marshal( String v ) throws Exception {  
			return "<![CDATA[" + v + "]]>";  
		}  
		
		@Override  
		public String unmarshal( String v ) throws Exception {  
			return v;  
		}  
	}
}