package com.idega.formbuilder.presentation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.business.FormComponent;

public class FBPalette extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_palette";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "Palette";
	
	private String styleClass;
	private String itemStyleClass;
	private Integer columns;
	private List items = new ArrayList();

	public FBPalette() {
		super();
		this.setRendererType(FBPalette.RENDERER_TYPE);
	}
	
	public String getFamily() {
		return FBPalette.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return FBPalette.RENDERER_TYPE;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		System.out.println("PALETTE INITILIZATION: " + this.getChildren().size());
		this.getChildren().clear();
		System.out.println("PALETTE INITILIZATION: " + this.getChildren().size());
		ValueBinding vb = this.getValueBinding("items");
		if(vb != null) {
			List items = (List) vb.getValue(context);
			Iterator it = items.iterator();
			while(it.hasNext()) {
				FormComponent current = (FormComponent) it.next();
				FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
				formComponent.setStyleClass(this.getItemStyleClass());
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				formComponent.setIcon(current.getIconPath());
				this.getChildren().add(formComponent);
			}
		}
		System.out.println("PALETTE INITILIZATION: " + this.getChildren().size());
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
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
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = itemStyleClass;
		values[3] = columns;
		values[4] = items;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		itemStyleClass = (String) values[2];
		columns = (Integer) values[3];
		items = (List) values[4];
	}

}
