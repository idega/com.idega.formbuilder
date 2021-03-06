package com.idega.formbuilder.presentation.beans;

import com.idega.xformsmanager.business.component.ComponentMultiUpload;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.util.FBUtil;

public class FormMultiUploadComponent extends FormComponent {
	
	public FormMultiUploadComponent(ComponentMultiUpload component) {
		this.component = component;
	}

	public ComponentMultiUpload getComponent() {
		return (ComponentMultiUpload) component;
	}

	public void setComponent(ComponentMultiUpload component) {
		this.component = component;
	}
	
	public void setAddButtonLabel(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getInsertButtonLabel();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setInsertButtonLabel(bean);
	}
	
	public void setRemoveButtonLabel(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getRemoveButtonLabel();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setRemoveButtonLabel(bean);
	}
	
	public String getAddButtonLabel() {
		return getComponent().getProperties().getInsertButtonLabel().getString(FBUtil.getUILocale());
	}
	
	public String getRemoveButtonLabel() {
		return getComponent().getProperties().getRemoveButtonLabel().getString(FBUtil.getUILocale());
	}
	
	public String getUploadDescription() {
		return getComponent().getProperties().getUploadingFileDescription().getString(FBUtil.getUILocale());
	}
	
	public String getDescriptionLabel() {
		return getComponent().getProperties().getDescriptionLabel().getString(FBUtil.getUILocale());
	}
	
	public String getUploaderHeaderText() {
		return getComponent().getProperties().getUploaderHeaderText().getString(FBUtil.getUILocale());
	}
	
	public void setUploadDescription(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getUploadingFileDescription();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setUploadingFileDescription(bean);
	}
	
	public void setDescriptionLabel(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getUploadingFileDescription();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setDescriptionLabel(bean);
	}
	
	public void setUploaderHeaderText(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getUploaderHeaderText();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setUploaderHeaderText(bean);
	}
}