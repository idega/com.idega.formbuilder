package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FBGenericFormComponentTag extends UIComponentTagBase {
	
	private String type;
	private String styleClass;

	public String getComponentType() {
		return "FBGenericField";
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRendererType() {
		return "fb_genericfield";
	}

	public FBGenericFormComponentTag() {
		super();
	}

	public void release() {
		super.release();
		this.type = null;
		this.styleClass = null;
	}
	
	public void setProperties(UIComponent component) {
	    super.setProperties(component);
	    if (component != null) {
	    	FBGenericFormComponent field = (FBGenericFormComponent)component;
			if(this.type != null) {
				field.setType(this.type);
			}
			if(this.styleClass != null) {
				field.setStyleClass(this.styleClass);
			}
	    }
	}
}
