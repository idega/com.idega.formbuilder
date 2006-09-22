package com.idega.formbuilder.business.form.util;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 * FormBuilder (yep, that means some methods can be tightly coupled) helper class for working with xml files
 *
 * Tightly coupled with FormBuilder class
 * 
 */
public class FormBuilderUtil {
	
	/**
	 * Properties holded for FormBuilder class for optimization(?) purposes
	 */
	public static final String model_name = "xf:model";
	public static final String id_name = "id";
	public static final String type_name = "type";
	public static final String slash = "/";
	public static final String fb_ = "fb_";

	private static final String simple_type = "xs:simpleType";
	private static final String complex_type = "xs:complexType";
	
	private FormBuilderUtil() { }
	
	/**
	 * <p>
	 * Copies schema type from one schema document to another by provided type name.
	 * </p>
	 * <p>
	 * <b><i>WARNING: </i></b>currently doesn't support cascading types copying,
	 * i.e., when one type depends on another
	 * </p>
	 * 
	 * @param src - schema document to copy from
	 * @param dest - schema document to copy to
	 * @param type_name - name of type to copy
	 * @throws NullPointerException - some params were null or such type was not found in src document
	 */
	public static void copySchemaType(Document src, Document dest, String type_name) throws NullPointerException {
		
		if(src == null || dest == null || type_name == null) {
			
			StringBuffer err_msg = new StringBuffer("\nEither parameter is not provided:");
			err_msg.append("\nsrc: ");
			err_msg.append(String.valueOf(src));
			err_msg.append("\ndest: ");
			err_msg.append(String.valueOf(dest));
			err_msg.append("\ntype_name: ");
			err_msg.append(type_name);
			
			throw new NullPointerException(err_msg.toString());
		}
		
		Element root = src.getDocumentElement();
		
//		check among simple types
		
		Element type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(simple_type), type_name);
		
		if(type_to_copy == null) {
//			check among complex types
			
			type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(complex_type), type_name);
		}
		
		if(type_to_copy == null)
			throw new NullPointerException("Schema type was not found by provided name: "+type_name);
		
		type_to_copy = (Element)dest.importNode(type_to_copy, true);
		
		dest.getDocumentElement().appendChild(type_to_copy);
	}
	
	private static Element getSchemaTypeToCopy(NodeList types, String type_name_required) {
		
		for (int i = 0; i < types.getLength(); i++) {
			
			Element simple_type = (Element)types.item(i); 
			String name_att = simple_type.getAttribute("name");
			
			if(name_att != null && name_att.equals(type_name_required))
				return simple_type;
		}
		
		return null;
	}
	
	public static Integer generateComponentId(Integer last_component_id) {
		
		return new Integer(last_component_id.intValue()+1);
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
	
	public static void insertNodesetElement(Document form_xforms, Element nodeset, Element new_xforms_element) {
		
		if(nodeset.hasChildNodes()) {
			
			NodeList children = nodeset.getChildNodes();
			
			for (int i = 0; i < children.getLength(); i++) {
				
				Node child = children.item(i);
				
				if(child.getNodeType() == Node.ELEMENT_NODE) {

					child = form_xforms.importNode(child, true);
					new_xforms_element.appendChild(child);
				}
			}
		}
		
		Element container = 
			(Element)((Element)form_xforms
					.getElementsByTagName("xf:instance").item(0))
					.getElementsByTagName("data").item(0);
		container.appendChild(new_xforms_element);
	}
	
	public static void insertBindElement(Document form_xforms, Element new_xforms_element, String new_form_schema_type, String bind_id, List<String> form_xsd_contained_types_declarations) {
		
		new_xforms_element.setAttribute(id_name, bind_id);
	
		String type_att = new_xforms_element.getAttribute(type_name);
		
		if(type_att != null && type_att.startsWith(fb_) &&
				!form_xsd_contained_types_declarations.contains(type_att)) {

			new_form_schema_type = type_att;
		}
		
		Element container = (Element)form_xforms.getElementsByTagName(model_name).item(0);
		container.appendChild(new_xforms_element);
	}
}
