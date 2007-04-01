package com.idega.formbuilder.presentation.converters;

public class FormButtonInfo {
	
	private String id;
	private String type;
	private String label;
	
	public FormButtonInfo() {
		this("", "", "");
	}
	
	public FormButtonInfo(String id, String label, String type) {
		this.id = id;
		this.type = type;
		this.label = label;
	}
	
	public FormButtonInfo(String id, String label) {
		this(id, label, "");
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
