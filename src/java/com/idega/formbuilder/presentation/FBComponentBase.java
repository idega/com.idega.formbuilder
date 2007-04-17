package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.presentation.IWBaseComponent;

public class FBComponentBase extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private String id;
	private String styleClass;
	
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

	public void renderChild(FacesContext context, UIComponent component) throws IOException {
		if(component.isRendered()) {
			component.encodeBegin(context);
			component.encodeChildren(context);
			component.encodeEnd(context);
		}
	}
	
	public void addFacet(String name, UIComponent component) {
		getFacets().put(name, component);
	}
	
	public void addChild(UIComponent child, UIComponent parent) {
		parent.getChildren().add(child);
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getRendererType() {
		return null;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context); 
		values[1] = id;
		values[2] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
	}

}
