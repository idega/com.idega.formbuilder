package com.idega.formbuilder.business.form.manager;

import java.util.HashMap;
import java.util.Map;

import com.idega.formbuilder.business.form.beans.IFormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormCacheManager {
	
	private Map<String, IFormComponent> form_components;
	
	public FormCacheManager() {
		
		form_components = new HashMap<String, IFormComponent>();
	}
	
	public void putFormComponent(IFormComponent component) {
		
		if(component.getComponentId() != null)
			
			form_components.put(component.getComponentId(), component);
	}
	
	public IFormComponent getFormComponent(String component_id) {
		return form_components.get(component_id);
	}
}