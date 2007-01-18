package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBFormPropertiesPanel;

public class FBFormPropertiesPanelTag extends UIComponentTagBase {
	
	private String styleClass;
	
	public FBFormPropertiesPanelTag() {
		super();
		this.styleClass = "";
	}
	
	public String getRendererType() {
		return null;
	}
	
	public String getComponentType() {
		return FBFormPropertiesPanel.COMPONENT_TYPE;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBFormPropertiesPanel formViewer = (FBFormPropertiesPanel) component;
			if(this.styleClass != null) {
				formViewer.setStyleClass(this.styleClass);
			}
		}
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
