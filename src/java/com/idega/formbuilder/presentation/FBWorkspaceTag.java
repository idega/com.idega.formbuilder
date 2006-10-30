package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class FBWorkspaceTag extends UIComponentTagBase {
	
	private String styleClass;
	private String view;
	
	public FBWorkspaceTag() {
		super();
		this.styleClass = "";
		this.view = "";
	}

	public String getComponentType() {
		return FBWorkspace.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return FBWorkspace.RENDERER_TYPE;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.view = null;
	}
	
	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBWorkspace workspace = (FBWorkspace) component;
			if(this.styleClass != null) {
				workspace.setStyleClass(this.styleClass);
			}
			if(this.view != null) {
				if (isValueReference(this.view)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.view);
	                workspace.setValueBinding("view", vb);
	            } else {
	            	workspace.setView(this.view);
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

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
