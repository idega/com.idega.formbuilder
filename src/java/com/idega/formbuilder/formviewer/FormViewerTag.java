package com.idega.formbuilder.formviewer;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FormViewerTag extends UIComponentTagBase {

	private String id;
	private String styleClass;
	private String components;
	
	public FormViewerTag() {
		super();
		this.id = "";
		this.styleClass = "";
		this.components = "";
	}
	
	public String getRendererType() {
		return "fb_formviewer";
	}
	
	public String getComponentType() {
		return "FormViewer";
	}
	
	public void release() {
		super.release();
		this.id = null;
		this.styleClass = null;
		this.components = null;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FormViewer formViewer = (FormViewer) component;
			if(this.id != null) {
				formViewer.setId(this.id);
			}
			if(this.styleClass != null) {
				formViewer.setStyleClass(this.styleClass);
			}
			if(this.components != null) {
				if (isValueReference(this.components)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.components);
	                formViewer.setValueBinding("components", vb);
	            } else {
	            	formViewer.setComponents(this.components);
	            }
			}
		}
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
