package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentPropertiesSubmitButton extends ComponentProperties {
	
	String action;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
		((FormComponentSubmitButton)parent_component).updateAction();
	}
	public void setPlainAction(String action) {
		this.action = action;
	}
}