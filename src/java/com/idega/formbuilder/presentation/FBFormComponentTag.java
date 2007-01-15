package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FBFormComponentTag extends UIComponentTagBase {
	
	private String id;
	private String styleClass;
	private String onclick;

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getComponentType() {
		return FBFormComponent.COMPONENT_TYPE;
	}
	
	public String getRendererType() {
		return null;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FBFormComponentTag() {
		super();
		this.id = "";
		this.styleClass = "";
		this.onclick = "";
	}

	public void release() {
		super.release();
		this.id = null;
		this.styleClass = null;
		this.onclick = null;
	}
	
	public void setProperties(UIComponent component) {
	    super.setProperties(component);
	    if (component != null) {
	    	FBFormComponent field = (FBFormComponent)component;
			if(this.id != null) {
				field.setId(this.id);
			}
			if(this.styleClass != null) {
				field.setStyleClass(this.styleClass);
			}
			if(this.onclick != null) {
				field.setOnclick(this.onclick);
			}
	    }
	}
}
