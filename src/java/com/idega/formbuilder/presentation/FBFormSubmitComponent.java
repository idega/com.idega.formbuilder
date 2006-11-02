package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponentBase;

import org.w3c.dom.Element;

public class FBFormSubmitComponent extends UIComponentBase {

	public static final String RENDERER_TYPE = "fb_formComponent";
	public static final String COMPONENT_TYPE = "FBFormComponent";
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private String styleClass;
	private Element element;
	@Override
	public String getFamily() {
		// TODO Auto-generated method stub
		return null;
	}

}
