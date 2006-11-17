package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.div.Div;

public class FBDivision extends Div {
	
	//public static final String RENDERER_TYPE = "fb_selectvalueslist";
	//public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "Division";
	
	/*public FBDiv() {
		super();
		this.setRendererType(FBSelectValuesList.RENDERER_TYPE);
	}*/
	
	public boolean getRendersChildren() {
		return true;
	}
	
	/*public String getFamily() {
		return FBSelectValuesList.COMPONENT_FAMILY;
	}*/
	
	/*public String getRendererType() {
		return FBSelectValuesList.RENDERER_TYPE;
	}*/
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[1];
		values[0] = super.saveState(context);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!this.isRendered()) {
			return;
		}
		super.encodeChildren(context);
	}

}
