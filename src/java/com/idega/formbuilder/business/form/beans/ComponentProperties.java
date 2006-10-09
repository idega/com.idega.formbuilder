package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.manager.XFormsManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentProperties implements IComponentProperties {
	
	private boolean required;
	private LocalizedStringBean label;
	private LocalizedStringBean error_msg;
	private XFormsManager xforms_manager;
	
	public LocalizedStringBean getErrorMsg() {
		return error_msg;
	}
	public void setErrorMsg(LocalizedStringBean error_msg) {
		this.error_msg = error_msg;
		xforms_manager.updateErrorMsg();
	}
	public LocalizedStringBean getLabel() {
		return label;
	}
	public void setLabel(LocalizedStringBean label) {
		this.label = label;
		xforms_manager.updateLabel();
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
		xforms_manager.updateConstraintRequired();
	}
	public void setXFormsManager(XFormsManager xforms_manager) {
		this.xforms_manager = xforms_manager;
	}
	public void setPlainLabel(LocalizedStringBean label) {
		this.label = label;
	}
	public void setPlainRequired(boolean required) {
		this.required = required;
	}
	public void setPlainErrorMsg(LocalizedStringBean error_msg) {
		this.error_msg = error_msg;
	}
}