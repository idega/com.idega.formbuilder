package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IComponentPropertiesSelect;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.view.ActionManager;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private IComponentProperties properties;
	
	private String id;
	
	private Boolean required;
	private String label;
	private String errorMessage;
	
	private String emptyLabel;
	
	private LocalizedStringBean labelStringBean;
	private LocalizedStringBean errorStringBean;
	
	private LocalizedStringBean emptyLabelBean;
	
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

	public FormComponent() {
		this.id = "";
		
		this.label = "";
		this.errorMessage = "";
		this.required = false;
		this.emptyLabel = "";
		
		this.labelStringBean = null;
		this.errorStringBean = null;
		this.emptyLabelBean = null;
	}
	
	public void loadProperties(String id) {
		this.id = id;
		properties = ActionManager.getFormManagerInstance().getComponentProperties(id);
		
		required = properties.isRequired();
		
		labelStringBean = properties.getLabel();
		label = labelStringBean.getString(new Locale("en"));
		
		errorStringBean = properties.getErrorMsg();
		errorMessage = errorStringBean.getString(new Locale("en"));
		
		if(properties instanceof IComponentPropertiesSelect) {
			IComponentPropertiesSelect selectProperties = (IComponentPropertiesSelect) properties;
			
			emptyLabelBean = selectProperties.getEmptyElementLabel();
			emptyLabel = emptyLabelBean.getString(new Locale("en"));
			
//			this.externalSrc = ((IComponentPropertiesSelect) properties).getExternalDataSrc();
//			this.itemset = ((IComponentPropertiesSelect) properties).getItemset();
		}
	}
	
	public void saveComponentLabel(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyTitle");
		if(value != null) {
			setLabel(value);
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		}
	}
	
	public void saveComponentRequired(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyRequired");
		if(value != null) {
			setRequired(new Boolean(value));
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		}
	}
	
	public void saveComponentErrorMessage(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyErrorMessage");
		if(value != null) {
			setErrorMessage(value);
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		this.errorStringBean.setString(new Locale("en"), errorMessage);
		this.properties.setErrorMsg(errorStringBean);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		this.labelStringBean.setString(new Locale("en"), label);
		this.properties.setLabel(labelStringBean);
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
		this.properties.setRequired(required);
	}

	public IComponentProperties getProperties() {
		return properties;
	}

	public void setProperties(IComponentProperties properties) {
		this.properties = properties;
	}

	public boolean isSimple() {
		if(properties instanceof IComponentPropertiesSelect) {
			return false;
		}
		return true;
	}

	public String getEmptyLabel() {
		return emptyLabel;
	}

	public void setEmptyLabel(String emptyLabel) {
		this.emptyLabel = emptyLabel;
	}

	public LocalizedStringBean getEmptyLabelBean() {
		return emptyLabelBean;
	}

	public void setEmptyLabelBean(LocalizedStringBean emptyLabelBean) {
		this.emptyLabelBean = emptyLabelBean;
	}

}
