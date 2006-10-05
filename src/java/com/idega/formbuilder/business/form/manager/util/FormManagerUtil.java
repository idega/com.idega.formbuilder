package com.idega.formbuilder.business.form.manager.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
 * FormManager helper class (yep, that means some methods can be tightly coupled)
 *
 * Tightly coupled with FormManager class
 * 
 */
public class FormManagerUtil {
	
	/**
	 * Properties holded for FormManager class for optimization(?) purposes
	 */
	public static final String model_name = "xf:model";
	public static final String id_name = "id";
	public static final String type_name = "type";
	public static final String slash = "/";
	public static final String fb_ = "fb_";
	public static final String loc_ref_part1 = "instance('localized_strings')/";
	public static final String loc_ref_part2 = "[@lang=instance('localized_strings')/current_language]";
	public static final String data_mod = "data_model";
	public static final String loc_tag = "localized_strings";
	public static final String output = "xf:output";
	public static final String ref_s = "ref";
	public static final String lang = "lang";
	public static final String CTID = "fbcomp_";
	public static final String loc_key_identifier = "lockey_";

	
	
	private FormManagerUtil() { }
	
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
	 * @param doc - document, to search for an element
	 * @param start_tag - where to start. Could be just null, then document root element is taken.
	 * @param id_value
	 * @return - <b>reference</b> to element in document
	 */
	public static Element getElementByIdFromDocument(Document doc, String start_tag, String id_value) {
		
		return getElementByAttributeFromDocument(doc, start_tag, "id", id_value);
	}
	
	/**
	 * 
	 * @param doc - document, to search for an element
	 * @param start_tag - where to start. Could be just null, then document root element is taken.
	 * @param attribute_name - what name attribute should be searched for
	 * @param attribute_value
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
			
			if(!model.getAttribute(id_name).equals(data_mod)) {
				
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
	 * @param loc_string - localized message
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
		
		Element loc_model = getElementByIdFromDocument(xforms, "head", data_mod);
		
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
		
		for (Iterator<Locale> iter = loc_string.getLanguagesKeySet().iterator(); iter.hasNext();) {
			Locale locale = iter.next();
			
			boolean val_set = false;
			
			if(loc_tags != null) {
				
				for (int i = 0; i < loc_tags.getLength(); i++) {
					
					Element loc_tag = (Element)loc_tags.item(i);
					
					if(loc_tag.getAttribute(lang).equals(locale.getLanguage())) {
						
						setElementsTextNodeValue(loc_tag, loc_string.getString(locale));
						
						val_set = true;
						break;
					}
				}
			}
			
			if(loc_tags == null || !val_set) {
				
//				create new localization element
				Element new_loc_el = xforms.createElement(new_key);
				new_loc_el.setAttribute(lang, locale.getLanguage());
				new_loc_el.appendChild(xforms.createTextNode(""));
				setElementsTextNodeValue(new_loc_el, loc_string.getString(locale));
				loc_strings.appendChild(new_loc_el);
			}
		}
	}
	
	public static String getComponentLocalizationKey(String component_id, String loc_key) {
		
		if(!isLocalizationKeyCorrect(loc_key))
			return null;
		
		String new_loc_key = new StringBuffer(loc_key_identifier)
		.append(component_id)
		.append(loc_key.substring(loc_key_identifier.length()))
		.toString();
		
		return new_loc_key;
	}
	
	public static String getKeyFromRef(String ref) {
		return ref.substring(ref.indexOf(slash)+1, ref.indexOf("["));
	}
	
	public static boolean isRefFormCorrect(String ref) {

		return ref != null && ref.startsWith(loc_ref_part1) && ref.endsWith(loc_ref_part2) && !ref.contains(" "); 
	}
	
	public static LocalizedStringBean getLocalizedStrings(String key, Document xforms_doc) {

		Element loc_model = getElementByIdFromDocument(xforms_doc, "head", data_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		
		NodeList key_elements = loc_strings.getElementsByTagName(key);
		LocalizedStringBean loc_str_bean = new LocalizedStringBean();
		
		for (int i = 0; i < key_elements.getLength(); i++) {
			
			Element key_element = (Element)key_elements.item(i);
			
			String lang_code = key_element.getAttribute("lang");
			
			if(lang_code != null) {
				
				String content = getElementsTextNodeValue(key_element);
				loc_str_bean.setString(new Locale(lang_code), content == null ? "" : content);
			}
		}
		
		return loc_str_bean;
	}
	
	public static boolean isLocalizationKeyCorrect(String loc_key) {
		return loc_key != null && !loc_key.contains(" ") && loc_key.startsWith(loc_key_identifier);
	}
	
	public static String getElementsTextNodeValue(Node element) {
		
		Node txt_node = element.getFirstChild();
		
		if(txt_node == null || txt_node.getNodeType() != Node.TEXT_NODE) {
			return null;
		}
		String node_value = txt_node.getNodeValue();
		
		return node_value == null ? "" : node_value.trim();
	}
	
	public static void setElementsTextNodeValue(Node element, String value) {
		
		Node txt_node = element.getFirstChild();
		
		if(txt_node == null || txt_node.getNodeType() != Node.TEXT_NODE)
			return;
			
		txt_node.setNodeValue(value);
	}
	
	/**
	 * <p>
	 * @param components_xml - components xml document, which passes the structure described:
	 * <p>
	 * optional document root name - form_components
	 * </p>
	 * <p>
	 * Component is encapsulated into div tag, which contains tag id as component type.
	 * Every component div container is child of root.
	 * </p>
	 * <p>
	 * Component type starts with "fbcomp_"
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * &lt;form_components&gt;<br />
		&lt;div class="input" id="fbcomp_text"&gt;<br />
			&lt;label class="label" for="fbcomp_text-value" id="fbcomp_text-label"&gt;			Single Line Field		&lt;/label&gt;<br />
			&lt;input class="value" id="fbcomp_text-value" name="d_fbcomp_text"	type="text" value="" /&gt;<br />
		&lt;/div&gt;<br />
	&lt;/form_components&gt;
	 * </p>
	 * </p>
	 * 
	 * IMPORTANT: types should be unique
	 * 
	 * @return List of components types (Strings)
	 */
	public static List<String> gatherAvailableComponentsTypes(Document components_xml) {
		
		Element root = components_xml.getDocumentElement();
		
		if(!root.hasChildNodes())
			return null;
		
		NodeList children = root.getChildNodes();
		List<String> components_types = new ArrayList<String>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("div")) {
				
				String element_id = ((Element)child).getAttribute(FormManagerUtil.id_name);
				
				if(element_id != null && 
						element_id.startsWith(FormManagerUtil.CTID) &&
						!components_types.contains(element_id)
				)
					components_types.add(element_id);
			}
		}
		
		return components_types;
	}
}