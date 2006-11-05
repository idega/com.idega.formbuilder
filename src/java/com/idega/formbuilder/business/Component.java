package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.Locale;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;

public class Component implements Serializable {
	
	private static final long serialVersionUID = -7539955909568793992L;
	
	private Boolean required;
	private String label;
	private String errorMsg;
	private IComponentProperties properties;
	
	public void reloadComponent(String id, IFormManager formManagerInstance) {
		properties = formManagerInstance.getComponentProperties(id);
	}
	
	public IComponentProperties getProperties() {
		return properties;
	}

	public void setProperties(IComponentProperties properties) {
		this.properties = properties;
	}

	public Boolean getRequired() {
		return new Boolean(properties.isRequired());
		//return new Boolean(true);
	}

	public Component() {
		
	}
	
	public String getErrorMsg() {
		LocalizedStringBean bean = properties.getErrorMsg();
		return bean.getString(new Locale("en"));
	}
	public void setErrorMsg(String errorMsg) {
		LocalizedStringBean bean = properties.getErrorMsg();
		bean.setString(new Locale("en"), errorMsg);
		properties.setErrorMsg(bean);
		this.errorMsg = errorMsg;
	}
	public String getLabel() {
		LocalizedStringBean bean = properties.getLabel();
		return bean.getString(new Locale("en"));
	}
	public void setLabel(String label) {
		LocalizedStringBean bean = properties.getLabel();
		bean.setString(new Locale("en"), label);
		properties.setLabel(bean);
		this.label = label;
	}
	public Boolean isRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		properties.setRequired(required.booleanValue());
		this.required = required;
	}

}
