package com.idega.formbuilder.business;

import java.io.Serializable;

public class FormField implements Serializable {
	
	private static final long serialVersionUID = -1462694114806788168L;
	
	private String type;
	
	public FormField() {
		this.type = "";
	}
	public FormField(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
