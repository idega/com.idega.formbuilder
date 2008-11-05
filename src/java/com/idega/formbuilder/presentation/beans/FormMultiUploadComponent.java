package com.idega.formbuilder.presentation.beans;

import com.idega.xformsmanager.business.component.ComponentMultiUpload;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.util.FBUtil;

public class FormMultiUploadComponent extends FormComponent {
	
	private ComponentMultiUpload component;
	
	public FormMultiUploadComponent(ComponentMultiUpload component) {
		this.component = component;
	}

	public ComponentMultiUpload getComponent() {
		return component;
	}

	public void setComponent(ComponentMultiUpload component) {
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

}
