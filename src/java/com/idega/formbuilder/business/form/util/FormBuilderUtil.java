package com.idega.formbuilder.business.form.util;

import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 * FormBuilder helper class (yep, that means some methods can be tightly coupled)
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
	public static final String loc_ref_part1 = "instance('localized_strings')/";
	public static final String loc_ref_part2 = "[@lang=instance('localized_strings')/current_language]";
	public static final String loc_mod = "localized_strings_model";
	public static final String loc_tag = "localized_strings";
	public static final String output = "xf:output";
	public static final String ref_s = "ref";
	public static final String lang = "lang";

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
			
			String err_msg = 
			new StringBuffer("\nEither parameter is not provided:")
			.append("\nsrc: ")
			.append(String.valueOf(src))
			.append("\ndest: ")
			.append(String.valueOf(dest))
			.append("\ntype_name: ")
			.append(type_name)
			.toString();
			
			throw new NullPointerException(err_msg);
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
		((Element)dest.getElementsByTagName("xs:schema").item(0)).appendChild(type_to_copy);
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
	
	public static String insertBindElement(Document form_xforms, Element new_xforms_element, String bind_id, List<String> form_xsd_contained_types_declarations) {
		
		new_xforms_element.setAttribute(id_name, bind_id);
	
		String type_att = new_xforms_element.getAttribute(type_name);
		
		NodeList models = form_xforms.getElementsByTagName(model_name);
		
		for (int i = 0; i < models.getLength(); i++) {
			
			Element model = (Element)models.item(i);
			
			if(!model.getAttribute(id_name).equals(loc_mod)) {
				
				model.appendChild(new_xforms_element);
				
				if(type_att != null && type_att.startsWith(fb_) &&
						!form_xsd_contained_types_declarations.contains(type_att))

					return type_att;
				
				return null;
			}
		}
		
		return null;
	}
	
	/**
	 * Puts localized text on element. Localization is saved on the xforms document.
	 * 
	 * @param new_key - new localization message key
	 * @param old_key - old key, if provided, is used for replacing with new_key
	 * @param element - element, to change or put localization message
	 * @param xforms - xforms document
	 * @param loc_string - Localized message. See class javadoc
	 * @throws NullPointerException - something necessary not provided
	 */
	public static void putLocalizedText(String new_key, String old_key, Element element, Document xforms, LocalizedStringBean loc_string) throws NullPointerException {

		if(xforms == null)
			throw new NullPointerException("XForms document not provided");
		
		String ref = element.getAttribute(ref_s);
		
		if(ref == null || new_key != null) {
			
			if(new_key == null)
				throw new NullPointerException("Localization to element not initialized and key for new localization string not presented");
			
//			add key to ref
			ref = new StringBuffer(loc_ref_part1)
			.append(new_key)
			.append(loc_ref_part2)
			.toString();
			
			element.setAttribute(ref_s, ref);
			
		} else if(ref != null && isRefFormCorrect(ref)) {
//			get key from ref
			
			new_key = getKeyFromRef(ref);
			
		} else
			throw new NullPointerException("Ref and key not specified or ref has incorrect format");
		
		Element loc_model = getElementByIdFromDocument(xforms, "head", loc_mod);
		
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		
		if(old_key != null) {
			
			NodeList loc_tags = loc_strings.getElementsByTagName(old_key);
			
//			find and rename those elements
			
			for (int i = 0; i < loc_tags.getLength(); i++) {
				
				Element loc_tag = (Element)loc_tags.item(i);
				xforms.renameNode(loc_tag, loc_tag.getNamespaceURI(), new_key);
			}
		}
		
		NodeList loc_tags = loc_strings.getElementsByTagName(new_key);
		
		for (Iterator<String> iter = loc_string.getLanguagesKeySet().iterator(); iter.hasNext();) {
			String loc_key = iter.next();
			
			boolean val_set = false;
			
			if(loc_tags != null) {
				
				for (int i = 0; i < loc_tags.getLength(); i++) {
					
					Element loc_tag = (Element)loc_tags.item(i);
					
					if(loc_tag.getAttribute(lang).equals(loc_key)) {
						
						loc_tag.setTextContent(loc_string.getString(loc_key));
						
						val_set = true;
						break;
					}
				}
			}
			
			if(loc_tags == null || !val_set) {
				
//				create new localization element
				Element new_loc_el = xforms.createElement(new_key);
				new_loc_el.setAttribute(lang, loc_key);
				new_loc_el.setTextContent(loc_string.getString(loc_key));
				loc_strings.appendChild(new_loc_el);
			}
		}
	}
	
	public static String getKeyFromRef(String ref) {
		return ref.substring(ref.indexOf(slash)+1, ref.indexOf("["));
	}
	
	public static boolean isRefFormCorrect(String ref) {

		return ref != null && ref.startsWith(loc_ref_part1) && ref.endsWith(loc_ref_part2) && !ref.contains(" "); 
	}
	
	public static LocalizedStringBean getLocalizedStrings(String key, Document xforms_doc) {

		Element loc_model = getElementByIdFromDocument(xforms_doc, "head", loc_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		
		NodeList key_elements = loc_strings.getElementsByTagName(key);
		LocalizedStringBean loc_str_bean = new LocalizedStringBean();
		
		for (int i = 0; i < key_elements.getLength(); i++) {
			
			Element key_element = (Element)key_elements.item(i);
			
			String lang_code = key_element.getAttribute("lang");
			
			if(lang_code != null) {
				
				String content = key_element.getTextContent();
				loc_str_bean.setString(lang_code, content == null ? "" : content);
			}
		}
		
		return loc_str_bean;
	}
}