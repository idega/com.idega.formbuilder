package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class FBSelectValuesListTag extends UIComponentTagBase {
	
	private String styleClass;
	private String itemSet;
	
	public FBSelectValuesListTag() {
		super();
		this.styleClass = "";
		this.itemSet = "";
	}

	public String getComponentType() {
		return FBSelectValuesList.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return FBSelectValuesList.RENDERER_TYPE;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.itemSet = null;
	}
	
	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBSelectValuesList valuesList = (FBSelectValuesList) component;
			if(this.styleClass != null) {
				valuesList.setStyleClass(this.styleClass);
			}
			if(this.itemSet != null) {
				if (isValueReference(this.itemSet)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.itemSet);
	                valuesList.setValueBinding("itemSet", vb);
	            }
			}
		}
	}

	public String getItemSet() {
		return itemSet;
	}

	public void setItemSet(String itemSet) {
		this.itemSet = itemSet;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
