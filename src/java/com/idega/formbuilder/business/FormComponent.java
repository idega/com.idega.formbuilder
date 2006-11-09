package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

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
	private String id;
	private IFormManager formManagerInstance;
	
	public FormComponent() {
		this.required = new Boolean(false);
		this.label = "";
		this.errorMsg = "";
		this.labelStringBean = null;
		this.errorStringBean = null;
	}
	
	public void loadProperties(String id, IFormManager formManagerInstance) {
		this.formManagerInstance = formManagerInstance;
		this.id = id;
		this.properties = formManagerInstance.getComponentProperties(id);
		this.required = properties.isRequired();
		this.labelStringBean = properties.getLabel();
		this.errorStringBean = properties.getErrorMsg();
	}
	
	public void saveProperties(ActionEvent ae) throws Exception {
		String trequired = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyRequired");
		if(trequired != null && !trequired.equals("")) {
			this.setRequired(new Boolean(trequired));
		}
		String tlabel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyTitle");
		if(tlabel != null) {
			this.setLabel(tlabel);
		}
		String terror = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyErrorMessage");
		if(terror != null) {
			this.setErrorMsg(terror);
		}
		if(properties != null) {
			properties.setRequired(required);
			properties.setErrorMsg(errorStringBean);
			properties.setLabel(labelStringBean);
		}
		formManagerInstance.updateFormComponent(id);
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
		this.errorMsg = errorMsg;
		this.errorStringBean.setString(new Locale("en"), errorMsg);
	}
	
	public Boolean isRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
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

	public String getLabel() {
		label = labelStringBean.getString(new Locale("en"));
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		labelStringBean.setString(new Locale("en"), this.label);
	}

}
