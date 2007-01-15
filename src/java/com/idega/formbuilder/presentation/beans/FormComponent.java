package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

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
		this.properties = ActionManager.getFormManagerInstance().getComponentProperties(id);
		
		this.required = properties.isRequired();
		this.labelStringBean = properties.getLabel();
		this.errorStringBean = properties.getErrorMsg();
		
		this.errorMessage = errorStringBean.getString(new Locale("en"));
		this.label = labelStringBean.getString(new Locale("en"));
		
		if(properties instanceof IComponentPropertiesSelect) {
			IComponentPropertiesSelect selectProperties = (IComponentPropertiesSelect) properties;
//			System.out.println("LOADING PROPERTIES OF SELECT COMPONENT");
//			this.externalSrc = ((IComponentPropertiesSelect) properties).getExternalDataSrc();
			this.emptyLabelBean = selectProperties.getEmptyElementLabel();
//			this.itemset = ((IComponentPropertiesSelect) properties).getItemset();
			
			this.emptyLabel = emptyLabelBean.getString(new Locale("en"));
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
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
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
