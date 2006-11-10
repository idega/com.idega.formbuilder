package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentProperties implements IComponentProperties {
	
	private boolean required;
	private LocalizedStringBean label;
	private LocalizedStringBean error_msg;
	protected IComponentPropertiesParent parent_component;
	
	public LocalizedStringBean getErrorMsg() {
		return error_msg;
	}
	public void setErrorMsg(LocalizedStringBean error_msg) {
		this.error_msg = error_msg;
		parent_component.updateErrorMsg();
	}
	public LocalizedStringBean getLabel() {
		return label;
	}
	public void setLabel(LocalizedStringBean label) {
		this.label = label;
		parent_component.updateLabel();
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
		parent_component.updateConstraintRequired();
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
	public void setParentComponent(IComponentPropertiesParent parent_component) {
		this.parent_component = parent_component;
	}
	public String toString() {
		return new StringBuffer()
		.append("\nrequired: ")
		.append(required)
		.append("\nlabel: ")
		.append(label)
		.append("\nerror_msg: ")
		.append(error_msg)
		
		.toString();
	}
}