package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class FBFormFieldTag extends UIComponentTagBase {
	
	private String fieldType;
	private String value;
	private String styleClass;

	public FBFormFieldTag() {
		super();
		this.fieldType = "";
		this.value = "";
		this.styleClass = "";
	}
	
	public String getRendererType() {
		return "fb_field";
	}
	
	public String getComponentType() {
		return "FBFormField";
	}

	public void release() {
		super.release();
		this.fieldType = null;
		this.value = null;
		this.styleClass = null;
	}
	
	public void setProperties(UIComponent component) {
	    super.setProperties(component);
	    if (component != null) {
	    	FBFormField panel = (FBFormField)component;
			if(this.value != null) {
				if (isValueReference(this.value)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.value);
	                panel.setValueBinding("value", vb);
	            } else {
	            	panel.setValue(this.value);
	            }
			}
			if(this.styleClass != null) {
				panel.setStyleClass(this.styleClass);
			}
			if(this.fieldType != null) {
				panel.setFieldType(this.fieldType);
			}
	    }
	}


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

}
