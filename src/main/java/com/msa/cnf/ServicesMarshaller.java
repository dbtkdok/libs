package com.msa.cnf;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

public class ServicesMarshaller {

	/**
	 * XML 파일을 Java Object로 unMarshal하여 List<UiComp>로 리턴한다.
	 *
	 * @param  String xml
	 * @param  String mode - FILE/TEXT
	 * @return List<PIDSteps>
	 */
	public static List<PIDSteps> getUnMarshalPID(String xml, String mode) throws Exception {

		List<PIDSteps>  pidList  = null;		
		List<Proc>      procList = null;		
		Services        content = null;
		XMLStreamReader reader = null;
		Unmarshaller    unmarshaller = null;
		File fileXml = null;
		try {
			unmarshaller = JAXBContext.newInstance(Services.class).createUnmarshaller();
			if (mode.equals("FILE")) {
				fileXml = new File(xml);
				content = (Services)unmarshaller.unmarshal(fileXml);
			} else {
				content = (Services)unmarshaller.unmarshal(new StringReader(xml));
				//reader  = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
				//content = (Services)unmarshaller.unmarshal(reader);
			}
			
			if (content != null) {
				pidList  = new ArrayList<PIDSteps>();
				procList = content.getProcList();				
				for (int ii=0; ii<procList.size(); ii++) {
					Proc proc = procList.get(ii);
					for (Step step: proc.getStepList()) {
						PIDSteps uiInfo = new PIDSteps();
						
						uiInfo.setPid(step.getPid());
						if (step.getService() != null) {
							uiInfo.setService(step.getService());
						}
						if (step.getQuery() != null) {
							uiInfo.setQuery(step.getQuery());
						}
						if (step.getMethod() != null) {
							uiInfo.setMethod(step.getMethod());
						}
						if (step.getGetKey() != null) {
							uiInfo.setGetKey(step.getGetKey());
						}
						if (step.getGetParams() != null) {
							uiInfo.setGetParams(step.getGetParams());
						}
						if (step.getChkKey() != null) {
							uiInfo.setChkKey(step.getChkKey());
						}
						if (step.getGetVar() != null) {
							uiInfo.setGetVar(step.getGetVar());
						}
						if (step.getCaseDoRun() != null) {
							uiInfo.setCaseDoRun(step.getCaseDoRun());
						}
						if (step.getCaseRET() != null) {
							uiInfo.setCaseRET(step.getCaseRET());
						}						
						if (step.getCaseDo() != null) {
							uiInfo.setCaseDo(step.getCaseDo());
						}
						if (step.getPostQuery() != null) {
							uiInfo.setPostQuery(step.getPostQuery());
						}
						if (step.getPrevQuery() != null) {
							uiInfo.setPrevQuery(step.getPrevQuery());
						}
						if (step.getPrevMethod() != null) {
							uiInfo.setPrevMethod(step.getPrevMethod());
						}
						if (step.getPostMethod() != null) {
							uiInfo.setPostMethod(step.getPostMethod());
						}
						
						pidList.add(uiInfo);
						System.out.println("["+ uiInfo.getPid() +" ::: "+ uiInfo.getService() +" ### "+ uiInfo.getQuery() +" ### "+ uiInfo.getMethod() +"]");
					}
				}				
			}
			return pidList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (unmarshaller != null) {
				try { unmarshaller = null; } catch (Exception e) { }
			}
			if (reader != null) {
				try { reader = null; } catch (Exception e) { }
			}
			if (content != null) {
				try { content = null; } catch (Exception e) { }
			}
			if (pidList != null) {
				try { pidList = null; } catch (Exception e) { }
			}
			if (fileXml != null) {
				try { fileXml = null; } catch (Exception e) { }
			}
		}
	}
}
