package com.idega.formbuilder.business.form.manager;

import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormComponentParent;

public interface IXFormsManager {

	public abstract void setCacheManager(CacheManager cache_manager);

	public abstract void setFormDocument(IFormComponentParent form_document);

	/**
	 * Gets full component bycomponent type. 
	 * 
	 * @param component_type - used to find correct xforms component implementation
	 * @return element node.
	 * @throws NullPointerException - component implementation could not be found by component type
	 */
	public abstract void loadXFormsComponentByType(String component_type)
			throws NullPointerException;

	public abstract void loadXFormsComponentFromDocument(String component_id);

	public abstract void addComponentToDocument(String component_id,
			String component_after_this_id) throws NullPointerException;

	public abstract void updateConstraintRequired() throws NullPointerException;

	public abstract void updateLabel();
	
	public abstract void updatePhaseNumber();

	public abstract void updateErrorMsg();

	public abstract void setFormComponent(IFormComponent component);

	public abstract void moveComponent(String before_component_id);

	public abstract void removeComponentFromXFormsDocument();

	public abstract String insertBindElement(Element new_bind_element,
			String bind_id);

	public abstract void changeBindName(String new_bind_name);
	
	public abstract Integer extractPhaseNumber();
}