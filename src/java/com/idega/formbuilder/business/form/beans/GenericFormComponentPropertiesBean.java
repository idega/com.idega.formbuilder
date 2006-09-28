package com.idega.formbuilder.business.form.beans;

import java.util.Collection;

import javax.faces.model.SelectItem;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class GenericFormComponentPropertiesBean {
	
	private LocalizedStringBean title;
	private Collection<SelectItem> validations;
	
	public Collection<SelectItem> getValidations() {
		return validations;
	}
	public void setValidations(Collection<SelectItem> validations) {
		this.validations = validations;
	}
	public LocalizedStringBean getTitle() {
		return title;
	}
	public void setTitle(LocalizedStringBean title) {
		this.title = title;
	}
}
