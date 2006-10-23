package com.idega.formbuilder.view.design;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FormDesignViewTag extends UIComponentTagBase {

	private String id;
	private String styleClass;
	private String componentStyleClass;
	
	public FormDesignViewTag() {
		super();
		this.id = "";
		this.styleClass = "";
		this.componentStyleClass = "";
	}
	
	public String getRendererType() {
		return FormDesignView.RENDERER_TYPE;
	}
	
	public String getComponentType() {
		return FormDesignView.COMPONENT_TYPE;
	}
	
	public void release() {
		super.release();
		this.id = null;
		this.styleClass = null;
		this.componentStyleClass = null;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FormDesignView formViewer = (FormDesignView) component;
			if(this.id != null) {
				formViewer.setId(this.id);
			}
			if(this.styleClass != null) {
				formViewer.setStyleClass(this.styleClass);
			}
			if(this.componentStyleClass != null) {
				formViewer.setComponentStyleClass(this.componentStyleClass);
			}
		}
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

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}
}
