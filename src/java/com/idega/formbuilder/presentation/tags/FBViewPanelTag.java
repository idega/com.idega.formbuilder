package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBViewPanel;

public class FBViewPanelTag extends UIComponentTagBase {
	
	private String styleClass;
	private String view;
	
	public FBViewPanelTag() {
		super();
		this.styleClass = "";
		this.view = "";
	}

	public String getComponentType() {
		return FBViewPanel.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return null;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.view = null;
	}
	
	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBViewPanel workspace = (FBViewPanel) component;
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
