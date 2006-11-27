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
	private Integer phase_number;
	
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
		.append("\nphase number: ")
		.append(phase_number)
		
		.toString();
	}
	public Integer getPhaseNumber() {
		return phase_number;
	}
	public void setPhaseNumber(Integer phase_number) {
		
		if(phase_number == 0)
			phase_number = null;
		
		this.phase_number = phase_number;
		parent_component.updatePhaseNumber();
	}
	
	public void setPlainPhaseNumber(Integer phase_number) {
		this.phase_number = phase_number;
	}
}