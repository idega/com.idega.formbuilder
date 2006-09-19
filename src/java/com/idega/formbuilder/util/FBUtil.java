package com.idega.formbuilder.util;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;

/**
 * <p>
 * This is a class with various utility methods when working with JSF.
 * </p>
 *
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBUtil {
	
	private static String bundle_identifier = "com.idega.formbuilder";
	
	private FBUtil() { }
	
	public static String getBundleIdentifier() {
		return bundle_identifier;
	}
	
	public static IWBundle getBundle(){
		return getBundle(FacesContext.getCurrentInstance());
	}

	public static IWBundle getBundle(FacesContext context){
		return IWContext.getIWContext(context).getIWMainApplication().getBundle(getBundleIdentifier());
	}
	
	public static IWResourceBundle getResourceBundle(FacesContext context){
		return getBundle(context).getResourceBundle(context.getExternalContext().getRequestLocale());
	}
	
	public static IWResourceBundle getResourceBundle(){
		return getResourceBundle(FacesContext.getCurrentInstance());
	}
	
	public static String getResourceAbsolutePath(IWMainApplication iwma, String url) {
		IWBundle iwb = iwma.getBundle(FormbuilderViewManager.FORMBUILDER_BUNDLE_IDENTIFIER);
        return iwb.getRealPathWithFileNameString(url);
	}
	
	private static DocumentBuilderFactory factory = null;
	
	public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		
		if(factory == null) {
			factory = DocumentBuilderFactory.newInstance();
		    factory.setNamespaceAware(true);
		    factory.setValidating(false);
		}

	    return factory.newDocumentBuilder();
	}
	
	public static Integer generateComponentId(Integer last_component_id) {
		
		return new Integer(last_component_id.intValue()+1);
	}
	
	/**
	 * 
	 * method name should explain, what it does
	 * 
	 * @param doc - document, to search for an element
	 * @param start_tag - where to start. faster search.
	 * @param id_value - ..
	 * @return - Reference to element in document
	 */
	public static Element getElementByIdFromDocument(Document doc, String start_tag, String id_value) {
		
		return getElementByAttributeFromDocument(doc, start_tag, "id", id_value);
	}
	
	/**
	 * 
	 * method name should explain, what it does
	 * 
	 * @param doc - document, to search for an element
	 * @param start_tag - where to start. faster search.
	 * @param attribute_name - what name attribute shoulde be searched for
	 * @param attribute_value - ..
	 * @return - Reference to element in document
	 */
	public static Element getElementByAttributeFromDocument(Document doc, String start_tag, String attribute_name, String attribute_value) {
		
		Element start_element;
		
		if(start_tag != null)
			start_element = (Element)doc.getElementsByTagName(start_tag).item(0);
		else
			start_element = doc.getDocumentElement();
		
		return DOMUtil.getElementByAttributeValue(start_element, "*", attribute_name, attribute_value);
	}
}