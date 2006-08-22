package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;


public class FBPanelTag extends UIComponentTag {
	
	public String getRendererType() {
		return "fb_panel";
	}
	
	public String getComponentType() {
		return "FBPanel";
	}

	public void release() {
		super.release();
	}
	
	public void setProperties(UIComponent component) {
	    super.setProperties(component);  
	}
}
