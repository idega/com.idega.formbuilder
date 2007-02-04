package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBPalette;

public class FBPaletteTag extends UIComponentTagBase {
	
	private String styleClass;
	private String itemStyleClass;
	private String columns;
	private String items;
	
	public FBPaletteTag() {
		super();
		this.styleClass = "";
		this.itemStyleClass = "";
		this.columns = "";
		this.items = "";
	}

	public String getComponentType() {
		return FBPalette.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return null;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBPalette palette = (FBPalette) component;
			if(this.styleClass != null) {
				palette.setStyleClass(this.styleClass);
			}
			if(this.itemStyleClass != null) {
				palette.setItemStyleClass(this.itemStyleClass);
			}
			if(this.columns != null) {
				palette.setColumns(Integer.parseInt(this.columns));
			}
			if(this.items != null) {
				if (isValueReference(this.items)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.items);
	                palette.setValueBinding("items", vb);
	            } else {
	            	throw new IllegalArgumentException();
	            }
			}
		}
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public String getItemStyleClass() {
		return itemStyleClass;
	}

	public void setItemStyleClass(String itemStyleClass) {
		this.itemStyleClass = itemStyleClass;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
}
