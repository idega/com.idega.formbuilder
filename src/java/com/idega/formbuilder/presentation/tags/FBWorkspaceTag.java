package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBWorkspace;

public class FBWorkspaceTag extends UIComponentTagBase {

	private String styleClass;
	private String id;
	private String view;
	
	public FBWorkspaceTag() {
		super();
		this.styleClass = "";
		this.id = "";
		this.view = "";
	}

	public String getComponentType() {
		return FBWorkspace.COMPONENT_TYPE;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getRendererType() {
		return null;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.id = null;
		this.view = null;
	}
	
	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBWorkspace workspace = (FBWorkspace) component;
			if(this.styleClass != null) {
				workspace.setStyleClass(this.styleClass);
			}
			if(this.id != null) {
				workspace.setId(this.id);
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
