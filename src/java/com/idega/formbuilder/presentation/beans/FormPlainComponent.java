package com.idega.formbuilder.presentation.beans;

import com.idega.documentmanager.business.component.ComponentPlain;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.util.FBUtil;

public class FormPlainComponent extends FormComponent {
	
	private ComponentPlain component;
	
	public FormPlainComponent(ComponentPlain component) {
		this.component = component;
	}
	
	public ComponentPlain getComponent() {
		return component;
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
