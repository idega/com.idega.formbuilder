package com.idega.formbuilder.presentation.beans;

import com.idega.documentmanager.business.component.ComponentMultiUploadDescription;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.util.FBUtil;

public class FormMultiUploadDescriptionComponent extends FormComponent {
	
	private ComponentMultiUploadDescription component;
	
	public FormMultiUploadDescriptionComponent(ComponentMultiUploadDescription component) {
		this.component = component;
	}

	public ComponentMultiUploadDescription getComponent() {
		return component;
	}

	public void setComponent(ComponentMultiUploadDescription component) {
		this.component = component;
	}
	
	public void setAddButtonLabel(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getAddButtonLabel();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setAddButtonLabel(bean);
	}
	
	public void setRemoveButtonLabel(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getRemoveButtonLabel();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setRemoveButtonLabel(bean);
	}
	
	public String getAddButtonLabel() {
		return getComponent().getProperties().getAddButtonLabel().getString(FBUtil.getUILocale());
	}
	
	public String getRemoveButtonLabel() {
		return getComponent().getProperties().getRemoveButtonLabel().getString(FBUtil.getUILocale());
	}
	
	public String getUploadDescription() {
		return getComponent().getProperties().getDescriptionLabel().getString(FBUtil.getUILocale());
	}
	
	public void setUploadDescription(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getDescriptionLabel();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setDescriptionLabel(bean);
	}

}
