package com.idega.formbuilder.formviewer;

import javax.faces.component.UIComponentBase;

public class FormViewer extends UIComponentBase {
	
	private static final String RENDERER_TYPE = "fb_formviewer";
	private static final String COMPONENT_FAMILY = "formbuilder";
	
	private String id;
	private String styleClass;
	private String components;
	
	public FormViewer() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}
	
	public String getFamily() {
		return this.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return this.RENDERER_TYPE;
	}

	public String getComponents() {
		return components;
	}

	public void setComponents(String components) {
		this.components = components;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
}
