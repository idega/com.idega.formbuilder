package com.idega.formbuilder.business.form.manager.beans;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.manager.FormCacheManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormBean {

	private FormPropertiesBean form_props;
	private Document form_xforms;
	private List<String> form_components_id_list;
	private List<String> form_xsd_contained_types_declarations;
	private FormCacheManager form_cache_manager;

	public FormPropertiesBean getFormProperties() {
		return form_props;
	}

	public void setFormProperties(FormPropertiesBean form_props) {
		this.form_props = form_props;
	}

	public Document getFormXforms() {
		return form_xforms;
	}

	public void setFormXforms(Document form_xforms) {
		this.form_xforms = form_xforms;
	}

	public List<String> getFormComponentsIdList() {
		
		if(form_components_id_list == null)
			form_components_id_list = new LinkedList<String>();
		
		return form_components_id_list;
	}

	public List<String> getFormXsdContainedTypesDeclarations() {
		
		if(form_xsd_contained_types_declarations == null)
			form_xsd_contained_types_declarations = new LinkedList<String>();
		
		return form_xsd_contained_types_declarations;
	}
	
	public void setFormCacheManager(FormCacheManager cache_manager) {
		
		form_cache_manager = cache_manager;
	}
	
	public void putFormComponent(IFormComponent component) {
		
		getFormCacheManager().putFormComponent(component);
	}
	
	public IFormComponent getFormComponent(String component_id) {
		return getFormCacheManager().getFormComponent(component_id);
	}
	
	private FormCacheManager getFormCacheManager() {
		
//		no cache manager set - create default
		
		if(form_cache_manager == null)
			form_cache_manager = new FormCacheManager();
		
		return form_cache_manager;
	}
}
