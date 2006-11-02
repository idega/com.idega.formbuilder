package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class FBComponentPropertiesPanelTag extends UIComponentTagBase {
	
	private String styleClass;
	private String component;
	
	public FBComponentPropertiesPanelTag() {
		super();
		this.styleClass = "";
		this.component = "";
	}

	public String getComponentType() {
		return FBComponentPropertiesPanel.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return FBComponentPropertiesPanel.RENDERER_TYPE;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.component = null;
	}
	
	public void setProperties(UIComponent comp) {
		super.setProperties(comp);
		if(comp != null) {
			FBComponentPropertiesPanel panel = (FBComponentPropertiesPanel) comp;
			if(this.styleClass != null) {
				panel.setStyleClass(this.styleClass);
			}
			if(this.component != null) {
				if (isValueReference(this.component)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.component);
	                panel.setValueBinding("component", vb);
	            } else {
	            	panel.setComponent(this.component);
	            }
			}
		}
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
