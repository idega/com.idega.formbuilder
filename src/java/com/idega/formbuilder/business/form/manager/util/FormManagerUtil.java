package com.idega.formbuilder.business.form.manager.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
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
	
	public static final String model_tag = "xf:model";
	public static final String label_tag = "xf:label";
	public static final String alert_tag = "xf:alert";
	public static final String head_tag = "head";
	public static final String id_att = "id";
	public static final String type_att = "type";
	public static final String slash = "/";
	public static final String fb_ = "fb_";
	public static final String loc_ref_part1 = "instance('localized_strings')/";
	public static final String loc_ref_part2 = "[@lang=instance('localized_strings')/current_language]";
	public static final String data_mod = "data_model";
	public static final String loc_tag = "localized_strings";
	public static final String output_tag = "xf:output";
	public static final String ref_s_att = "ref";
	public static final String lang_att = "lang";
	public static final String CTID = "fbcomp_";
	public static final String loc_key_identifier = "lockey_";
	public static final String localized_entries = "localizedEntries";
	public static final String body_tag = "body";
	public static final String bind_att = "bind";
	public static final String name_att = "name";
	public static final String schema_tag = "xs:schema";
	public static final String form_id = "form_id";
	public static final String title_tag = "title";
	public static final String nodeset_att = "nodeset";
	public static final String group_tag = "xf:group";
	public static final String submit_tag = "xf:submit";
	public static final String itemset_tag = "xf:itemset";
	public static final String item_tag = "xf:item";
	public static final String model_att = "model";
	public static final String src_att = "xf:src";
	public static final String item_label_tag = "item_label";
	public static final String item_value_tag = "item_value";
	public static final String localized_entries_tag = "localizedEntries";
	public static final String default_language_tag = "default_language";
	
	
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
	
	public static void insertNodesetElement(Document form_xforms, Element nodeset, Element new_nodeset_element) {
		
		if(nodeset.hasChildNodes()) {
			
			NodeList children = nodeset.getChildNodes();
			
			for (int i = 0; i < children.getLength(); i++) {
				
				Node child = children.item(i);
				
				if(child.getNodeType() == Node.ELEMENT_NODE) {

					child = form_xforms.importNode(child, true);
					new_nodeset_element.appendChild(child);
				}
			}
		}
		
		Element container = 
			(Element)((Element)form_xforms
					.getElementsByTagName("xf:instance").item(0))
					.getElementsByTagName("data").item(0);
		container.appendChild(new_nodeset_element);
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
		
		String ref = element.getAttribute(ref_s_att);
		
		if(ref == null || new_key != null) {
			
			if(new_key == null)
				throw new NullPointerException("Localization to element not initialized and key for new localization string not presented");
			
//			add key to ref
			ref = new StringBuffer(loc_ref_part1)
			.append(new_key)
			.append(loc_ref_part2)
			.toString();
			
			element.setAttribute(ref_s_att, ref);
			
		} else if(ref != null && isRefFormCorrect(ref)) {
//			get key from ref
			
			new_key = getKeyFromRef(ref);
			
		} else
			throw new NullPointerException("Ref and key not specified or ref has incorrect format");
		
		Element loc_model = getElementByIdFromDocument(xforms, head_tag, data_mod);
		
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
		
		Collection<Locale> lang_key_set = loc_string.getLanguagesKeySet();
		
		Collection<String> lang_strings = new ArrayList<String>();
		
		for (Iterator<Locale> iter = lang_key_set.iterator(); iter.hasNext();) {
			
			lang_strings.add(iter.next().getLanguage());
		}
		
		for (int i = 0; i < loc_tags.getLength(); i++) {
			
			Element loc_tag = (Element)loc_tags.item(i);
			
			if(!lang_strings.contains(loc_tag.getAttribute(lang_att))) {
				
				loc_strings.removeChild(loc_tag);
			}
		}
		lang_strings = null;
		
		for (Iterator<Locale> iter = lang_key_set.iterator(); iter.hasNext();) {
			Locale locale = iter.next();
			
			boolean val_set = false;
			
			if(loc_tags != null) {
				
				for (int i = 0; i < loc_tags.getLength(); i++) {
					
					Element loc_tag = (Element)loc_tags.item(i);
					
					if(loc_tag.getAttribute(lang_att).equals(locale.getLanguage())) {
						
						if(loc_string.getString(locale) != null)
							setElementsTextNodeValue(loc_tag, loc_string.getString(locale));
						
						val_set = true;
						break;
					}
				}
			}
			
			if(loc_tags == null || !val_set) {
				
//				create new localization element
				Element new_loc_el = xforms.createElement(new_key);
				new_loc_el.setAttribute(lang_att, locale.getLanguage());
				new_loc_el.appendChild(xforms.createTextNode(""));
				setElementsTextNodeValue(new_loc_el, loc_string.getString(locale) == null ? "" : loc_string.getString(locale));
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

		Element loc_model = getElementByIdFromDocument(xforms_doc, head_tag, data_mod);
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
	
	public static LocalizedStringBean getTitleLocalizedStrings(Document xforms_doc) {
		
		Element title = (Element)xforms_doc.getElementsByTagName(title_tag).item(0);
		Element output = (Element)title.getElementsByTagName(output_tag).item(0);
		
		return getElementLocalizedStrings(output, xforms_doc);
	}
	
	public static LocalizedStringBean getLabelLocalizedStrings(String component_id, Document xforms_doc) {
		
		Element component = getElementByIdFromDocument(xforms_doc, "body", component_id);
		
		NodeList labels = component.getElementsByTagName(FormManagerUtil.label_tag);
		
		if(labels == null || labels.getLength() == 0)
			return new LocalizedStringBean();
		
		Element label = (Element)labels.item(0);
		
		return getElementLocalizedStrings(label, xforms_doc);
	}
	
	public static LocalizedStringBean getElementLocalizedStrings(Element element, Document xforms_doc) {
		
		String ref = element.getAttribute("ref");
		
		if(!isRefFormCorrect(ref))
			return new LocalizedStringBean();
		
		String key = getKeyFromRef(ref);
		
		return getLocalizedStrings(key, xforms_doc);
	}
	
	public static Locale getDefaultFormLocale(Document form_xforms) {
		
		Element loc_model = getElementByIdFromDocument(form_xforms, head_tag, data_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		NodeList default_language_node_list = loc_strings.getElementsByTagName(default_language_tag);
		
		if(default_language_node_list == null || default_language_node_list.getLength() == 0)
			return null;
		
		String lang = getElementsTextNodeValue((Element)default_language_node_list.item(0));
		
		if(lang == null)
			return null;
		
		return new Locale(lang);
	}
	
	public static LocalizedStringBean getErrorLabelLocalizedStrings(String component_id, Document xforms_doc) {
		
		Element component = getElementByIdFromDocument(xforms_doc, "body", component_id);
		
		NodeList alerts = component.getElementsByTagName(FormManagerUtil.alert_tag);
		
		if(alerts == null || alerts.getLength() == 0)
			return new LocalizedStringBean();
		
		Element output = (Element)((Element)alerts.item(0)).getElementsByTagName(FormManagerUtil.output_tag).item(0);
		
		String ref = output.getAttribute("ref");
		
		if(!isRefFormCorrect(ref))
			return new LocalizedStringBean();
		
		String key = getKeyFromRef(ref);
		
		return getLocalizedStrings(key, xforms_doc);
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
				
				String element_id = ((Element)child).getAttribute(FormManagerUtil.id_att);
				
				if(element_id != null && 
						element_id.startsWith(FormManagerUtil.CTID) &&
						!components_types.contains(element_id)
				)
					components_types.add(element_id);
			}
		}
		
		return components_types;
	}
	
	public static Element getItemElementById(Document item_doc, String item_id) {
		
		Element item = FormManagerUtil.getElementByIdFromDocument(item_doc, head_tag, item_id);
		if(item == null)
			return null;
		
		return DOMUtil.getFirstChildElement(item);
	}
	
	public static void removeTextNodes(Node node) {
		
		NodeList children = node.getChildNodes();
		List<Node> childs_to_remove = new ArrayList<Node>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child.getNodeType() == Node.TEXT_NODE) {
				
				childs_to_remove.add(child);
				
			} else {
				
				if(child.hasChildNodes())
					removeTextNodes(child);
			}
		}
		
		for (Iterator<Node> iter = childs_to_remove.iterator(); iter.hasNext();) {
			node.removeChild(iter.next());
		}
	}
	
	public static List<String[]> getComponentsTagNamesAndIds(Document xforms_doc) {
		
		Element body_element = (Element)xforms_doc.getElementsByTagName(body_tag).item(0);
		Element group_element = (Element)body_element.getElementsByTagName(group_tag).item(0);
		
		List<Element> components_elements = DOMUtil.getChildElements(group_element);
		List<String[]> components_tag_names_and_ids = new ArrayList<String[]>();
		
		for (Iterator<Element> iter = components_elements.iterator(); iter.hasNext();) {
			
			Element component_element = iter.next();
			String[] tag_name_and_id = new String[2];
			tag_name_and_id[0] = component_element.getTagName();
			tag_name_and_id[1] = component_element.getAttribute(id_att);
			
			components_tag_names_and_ids.add(tag_name_and_id);
		}
		
		return components_tag_names_and_ids;
	}
	
	public static String serializeDocument(Document document) throws IOException {
		
		OutputFormat output_format = new OutputFormat();
		output_format.setOmitXMLDeclaration(true);
		
		StringWriter writer = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(writer, output_format);
		serializer.asDOMSerializer();
		serializer.serialize(document.getDocumentElement());
		
		return writer.toString();
	}
}