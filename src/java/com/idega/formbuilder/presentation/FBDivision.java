package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.div.Div;

public class FBDivision extends Div {
	
	public static final String COMPONENT_TYPE = "Division";
	
	public String onclick;
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = onclick;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		this.onclick = (String) values[1];
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		if(onclick != null && !onclick.equals("")) {
			writer.writeAttribute("onClick", this.getOnclick(), null);
		}
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

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

}
