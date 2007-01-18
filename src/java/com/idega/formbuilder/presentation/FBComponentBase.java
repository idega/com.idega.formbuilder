package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.presentation.IWBaseComponent;

public class FBComponentBase extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	
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
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getRendererType() {
		return null;
	}

}
