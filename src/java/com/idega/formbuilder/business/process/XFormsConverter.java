package com.idega.formbuilder.business.process;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.process.converter.DataConvertersFactory;
import com.idega.jbpm.exe.Converter;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 */
public class XFormsConverter implements Converter {

	private static final String MAPPING_ATT = "mapping";
	
	private XPathExpression exp;
	
	public XFormsConverter() {
		
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			exp = xpath.compile(new StringBuilder("//*[@").append(MAPPING_ATT).append("]").toString());
			
		} catch (XPathException e) {
			throw new RuntimeException("Could not compile XPath expression: " + e.getMessage(), e);
		}
	}
	
	public Map<String, Object> convert(Object submissionData) {

		Node sdNode = (Node)submissionData;
		
		try {
			NodeList result;
			
			synchronized (exp) {
				result = (NodeList)exp.evaluate(sdNode, XPathConstants.NODESET);
			}
			
			if(result.getLength() == 0)
				return null;
			
			Map<String, Object> variables = new HashMap<String, Object>();
			
			for (int i = 0; i < result.getLength(); i++) {
				
				Element element = (Element)result.item(i);
				String mapping = element.getAttribute(MAPPING_ATT);
				
				Object variableValue = getConvertersFactory().createConverter(getDataType(mapping)).convert(element);
				variables.put(mapping, variableValue);
			}
			
			return variables;
			
		} catch (XPathException e) {
			throw new RuntimeException("Could not evaluate XPath expression: " + e.getMessage(), e);
		}
	}
	
	protected String getDataType(String mapping) {
		
		return mapping.contains(":") ? mapping.substring(0, mapping.indexOf(":")) : "string";
	}

	public Object revert(Map<String, Object> variables, Object submissionData) {
		
		return null;
	}
	
	public static void main(String[] args) {
	
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			Node sdNode = dbFactory.newDocumentBuilder().parse(new File("/Users/civilis/dev/workspace/eplatform-4/com.idega.formbuilder/src/test/java/com/idega/formbuilder/tests/basic/submissionTest.xml"));
			
			XFormsConverter converter = new XFormsConverter();
			converter.setConvertersFactory(new DataConvertersFactory());
			
			Map<String, Object> variables = converter.convert(sdNode);
			
			System.out.println("variables got: "+variables.keySet());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DataConvertersFactory convertersFactory;

	public DataConvertersFactory getConvertersFactory() {
		return convertersFactory;
	}

	public void setConvertersFactory(DataConvertersFactory convertersFactory) {
		this.convertersFactory = convertersFactory;
	}
}