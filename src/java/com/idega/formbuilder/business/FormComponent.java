package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.idegaweb.BundleLocalizationMap;
import com.idega.webface.WFUtil;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694114806788168L;
	
	private static Map localizedStrings = (BundleLocalizationMap) ((HashMap) WFUtil.getBeanInstance("localizedStrings")).get("com.idega.formbuilder");
	
	private String type;
	private String name;
	
	public FormComponent() {
		this.type = "";
		this.name = "";
	}
	
	public FormComponent(String type) {
		this.type = type;
		this.name = (String) localizedStrings.get(type);
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
