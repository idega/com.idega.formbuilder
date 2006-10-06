package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentProperties implements IComponentProperties {
	
	public static final short BASIC_COMPONENT_CLASS = 1;
	public static final short TEXT_COMPONENT_CLASS = 2;
	public static final short SELECT_COMPONENT_CLASS = 3;
	
	private boolean required;
	private LocalizedStringBean label;
	private LocalizedStringBean error_msg;
	
	public LocalizedStringBean getErrorMsg() {
		return error_msg;
	}
	public void setErrorMsg(LocalizedStringBean error_msg) {
		this.error_msg = error_msg;
	}
	public LocalizedStringBean getLabel() {
		return label;
	}
	public void setLabel(LocalizedStringBean label) {
		this.label = label;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public short getComponentClass() {
		return BASIC_COMPONENT_CLASS;
	}
}
