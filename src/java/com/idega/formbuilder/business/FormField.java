package com.idega.formbuilder.business;

import java.io.Serializable;

public class FormField implements Serializable {
	
	private static final long serialVersionUID = -1462694114806788168L;
	private String name;
	private String type;
	
	public FormField(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
