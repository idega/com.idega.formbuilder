package com.idega.formbuilder.business;

import java.io.Serializable;

public class FormField implements Serializable {
	
	private static final long serialVersionUID = -1462694114806788168L;
	
	//private static Map localizedStrings = (BundleLocalizationMap) ((HashMap) WFUtil.getBeanInstance("localizedStrings")).get("com.idega.formbuilder");
	
	private String type = "";
	private String name = "";
	
	public FormField() {
		
	}
	
	public FormField(String type) {
		this.type = type;
		//this.name = (String) localizedStrings.get(type);
		this.name = "sample component";
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
