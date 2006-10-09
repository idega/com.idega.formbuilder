package com.idega.formbuilder.business.form.manager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormComponentParent;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class HtmlManager {
	
	protected Element unlocalized_html_component;
	protected Map<Locale, Element> localized_html_components;
	protected IFormComponent component;
	
	protected CacheManager cache_manager;
	protected IFormComponentParent form_document;
	
	public Element getHtmlRepresentationByLocale(Locale locale) {

		Map<Locale, Element> localized_html_components = getLocalizedHtmlComponents();
		Element localized_element = localized_html_components.get(locale);
		
		if(localized_element != null)
			return localized_element;
		
		if(unlocalized_html_component == null) {
			
			unlocalized_html_component = (Element)cache_manager.getHtmlComponentReferenceByType(component.getType()).cloneNode(true);
			putMetaInfoOnHtmlComponent(unlocalized_html_component, component.getId(), component.getType());
		}
		
		localized_element = getFormHtmlComponentLocalization(locale.getLanguage());
		localized_html_components.put(locale, localized_element);
		
		return localized_element;
	}
	
	protected Map<Locale, Element> getLocalizedHtmlComponents() {
		
		if(localized_html_components == null)
			localized_html_components = new HashMap<Locale, Element>();
		
		return localized_html_components;
	}
	
	protected Element getFormHtmlComponentLocalization(String loc_str) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				form_document.getXformsDocument(), "head", FormManagerUtil.data_mod
		);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_html_component.cloneNode(true);
		
		NodeList descendants = localized_component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Node desc = descendants.item(i);
			
			String txt_content = FormManagerUtil.getElementsTextNodeValue(desc);
			
			if(FormManagerUtil.isLocalizationKeyCorrect(txt_content)) {
				
				NodeList localization_strings_elements = loc_strings.getElementsByTagName(txt_content);
				
				String localized_string = null;
				
				if(localization_strings_elements != null) {
					
					for (int j = 0; j < localization_strings_elements.getLength(); j++) {
						
						Element loc_el = (Element)localization_strings_elements.item(j);
						
						String lang = loc_el.getAttribute("lang");
						
						if(lang != null && lang.equals(loc_str)) {
							
							localized_string = FormManagerUtil.getElementsTextNodeValue(loc_el);
							break;
						}
					}
				}
				
				if(localized_string == null)
					throw new NullPointerException(
							"Could not find localization value by provided key= "+txt_content+", language= "+loc_str);
				
				FormManagerUtil.setElementsTextNodeValue(desc, localized_string);
			}
		}
		
		return localized_component;
	}
	
	public void setFormComponent(IFormComponent component) {
		this.component = component;
	}
	
	/**
	 * Replaces old_comp_id values with new_comp_id values on all attributes, which contained old_comp_id values.
	 * Puts id attribute on component element with new_comp_id value. 
	 * 
	 * @param component - form component container
	 * @param new_comp_id - new form component id to be set on attributes
	 * @param old_comp_id - all form component id
	 */
	protected void putMetaInfoOnHtmlComponent(Element component, String new_comp_id, String old_comp_id) {
		
		component.setAttribute(FormManagerUtil.id_name, new_comp_id);
		NodeList descendants = component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Element desc = (Element)descendants.item(i);
			
			String desc_text_content = FormManagerUtil.getElementsTextNodeValue(desc);
			
			if(FormManagerUtil.isLocalizationKeyCorrect(desc_text_content)) {
				
				FormManagerUtil.setElementsTextNodeValue(desc, FormManagerUtil.getComponentLocalizationKey(new_comp_id, desc_text_content));
			}
			
			NamedNodeMap attributes = desc.getAttributes();
			
			for (int j = 0; j < attributes.getLength(); j++) {
				Node attribute = attributes.item(j);
				
				String node_val = attribute.getNodeValue();
				
				if(node_val.contains(old_comp_id))
					
					attribute.setNodeValue(node_val.replace(old_comp_id, new_comp_id));
			}
		}
	}
	
	public void setCacheManager(CacheManager cache_manager) {
		this.cache_manager = cache_manager;
	}
	
	public void setFormDocument(IFormComponentParent form_document) {
		
		this.form_document = form_document;
	}
}