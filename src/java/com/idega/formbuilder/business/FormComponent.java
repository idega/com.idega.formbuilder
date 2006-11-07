package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.Locale;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -7539955909568793992L;
	
	private Boolean required;
	private String label;
	private String errorMsg;
	private LocalizedStringBean labelStringBean;
	private LocalizedStringBean errorStringBean;
	private IComponentProperties properties;
	
	public void loadProperties(String id, IFormManager formManagerInstance) {
		this.properties = formManagerInstance.getComponentProperties(id);
		this.labelStringBean = properties.getLabel();
		this.errorStringBean = properties.getErrorMsg();
	}
	
	public void saveProperties() {
		properties.setRequired(this.required);
		properties.setErrorMsg(errorStringBean);
		properties.setLabel(labelStringBean);
		System.out.println("SAVING PROPERTIES");
		//TODO
	}
	
	public IComponentProperties getProperties() {
		return properties;
	}

	public void setProperties(IComponentProperties properties) {
		this.properties = properties;
	}

	public Boolean getRequired() {
		return new Boolean(properties.isRequired());
	}

	public String getErrorMsg() {
		this.errorMsg = errorStringBean.getString(new Locale("en"));
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		/*LocalizedStringBean bean = properties.getErrorMsg();
		bean.setString(new Locale("en"), errorMsg);
		properties.setErrorMsg(bean);*/
		this.errorMsg = errorMsg;
		this.errorStringBean.setString(new Locale("en"), this.errorMsg);
	}
	public String getLabel() {
		this.label = labelStringBean.getString(new Locale("en"));
		return label;
		//LocalizedStringBean bean = properties.getLabel();
		//return bean.getString(new Locale("en"));
	}
	public void setLabel(String label) {
		/*LocalizedStringBean bean = properties.getLabel();
		bean.setString(new Locale("en"), label);
		properties.setLabel(bean);*/
		this.label = label;
		this.labelStringBean.setString(new Locale("en"), this.label);
		System.out.println("SETTER FOR LABEL");
	}
	public Boolean isRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		//properties.setRequired(required.booleanValue());
		this.required = required;
	}

	public LocalizedStringBean getErrorStringBean() {
		return errorStringBean;
	}

	public void setErrorStringBean(LocalizedStringBean errorStringBean) {
		this.errorStringBean = errorStringBean;
	}

	public LocalizedStringBean getLabelStringBean() {
		return labelStringBean;
	}

	public void setLabelStringBean(LocalizedStringBean labelStringBean) {
		this.labelStringBean = labelStringBean;
	}

}
