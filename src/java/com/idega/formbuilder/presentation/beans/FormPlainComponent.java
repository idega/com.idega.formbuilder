package com.idega.formbuilder.presentation.beans;

import com.idega.xformsmanager.business.component.ComponentStatic;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.util.FBUtil;

public class FormPlainComponent extends FormComponent {
	
	public FormPlainComponent(ComponentStatic component) {
		this.component = component;
	}
	
	public ComponentStatic getComponent() {
		return (ComponentStatic) component;
	}
	
	public void setPlainText(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getText();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setText(bean);
	}
	
	public String getPlainText() {
		return getComponent().getProperties().getText() == null ? null : getComponent().getProperties().getText().getString(FBUtil.getUILocale());
	}

}
