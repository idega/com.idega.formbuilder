package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBDesignView;


public class FBDesignViewTag extends UIComponentTagBase {

	private String styleClass;
	private String componentStyleClass;
	private String status;
	
	public FBDesignViewTag() {
		super();
		this.styleClass = "";
		this.componentStyleClass = "";
		this.status = "";
	}
	
	public String getRendererType() {
		return null;
	}
	
	public String getComponentType() {
		return FBDesignView.COMPONENT_TYPE;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.componentStyleClass = null;
		this.status = null;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBDesignView formViewer = (FBDesignView) component;
			if(this.styleClass != null) {
				formViewer.setStyleClass(this.styleClass);
			}
			if(this.componentStyleClass != null) {
				formViewer.setComponentStyleClass(this.componentStyleClass);
			}
			if(this.status != null) {
				if (isValueReference(this.status)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.status);
	                formViewer.setValueBinding("status", vb);
	            } else {
	            	formViewer.setStatus(this.status);
	            }
			}
		}
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
