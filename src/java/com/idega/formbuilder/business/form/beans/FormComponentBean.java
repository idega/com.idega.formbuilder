package com.idega.formbuilder.business.form.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentBean implements IFormComponent {
	
	private String component_id;
	private Map<Locale, Element> localized_elements;
	private Element unlocalized_element;
	
	public FormComponentBean() {
		
		localized_elements = new HashMap<Locale, Element>();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.beans.IFormComponent#getComponentId()
	 */
	public String getComponentId() {
		return component_id;
	}
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.beans.IFormComponent#setComponentId(java.lang.String)
	 */
	public void setComponentId(String component_id) {
		this.component_id = component_id;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.beans.IFormComponent#setComponent(java.util.Locale, org.w3c.dom.Element)
	 */
	public void setComponent(Locale locale, Element element) {
		
		if(locale == null && element != null)
			unlocalized_element = element;
		else
			localized_elements.put(locale, element);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.beans.IFormComponent#getComponent(java.util.Locale)
	 */
	public Element getComponent(Locale locale) {
		
		if(locale == null)
			return unlocalized_element;
		
		return localized_elements.get(locale);
	}
}