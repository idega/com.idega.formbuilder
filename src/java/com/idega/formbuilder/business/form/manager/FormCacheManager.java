package com.idega.formbuilder.business.form.manager;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormCacheManager {
	
	private Map<String, Element> unlocalized_form_components = new HashMap<String, Element>();
	private Map<String[], Element> localized_form_components = new HashMap<String[], Element>();
	
	public void putUnlocalizedFormHtmlComponent(String component_id, Element component) {
		unlocalized_form_components.put(component_id, component);
	}
	
	public void putLocalizedFormHtmlComponent(String component_id, String locale_str, Element component) {
		localized_form_components.put(new String[] {component_id, locale_str}, component);
	}
	
	public Element getLocalizedFormHtmlComponent(String component_id, String locale_str) {
		return localized_form_components.get(new String[] {component_id, locale_str});
	}
	
	public Element getUnlocalizedFormHtmlComponent(String component_id) {
		return unlocalized_form_components.get(component_id);
	}
}