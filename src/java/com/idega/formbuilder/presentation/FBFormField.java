package com.idega.formbuilder.presentation;

import javax.faces.component.UIPanel;

public class FBFormField extends UIPanel {
	
	private static final String RENDERER_TYPE = "fb_field";

	private String value;
	private String styleClass;
	private String fieldType;
	
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public FBFormField() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}
	
	public FBFormField(String type, String value) {
		super();
		this.setRendererType(RENDERER_TYPE);
		this.setFieldType(type);
		this.setValue(value);
		System.out.println("Field double constructor");
	}
	
	public String getFamily() {
		return ("formbuilder");
	}
}
