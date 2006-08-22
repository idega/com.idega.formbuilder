package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBPanel;

public class PanelRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if ((context == null)|| (component == null)){
			throw new NullPointerException();
		}
		ResponseWriter writer = context.getResponseWriter();
		//String clientId = component.getClientId(context);
		FBPanel panel = (FBPanel) component;
				
		writer.startElement("DIV", panel);
		writer.writeAttribute("style", "border: 5px; border-color: black; width: 200px; height: 120px; background-color: red;","styleClass");
		
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if ((context == null) || (component == null)){
			throw new NullPointerException();
		}
		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId(context);
		FBPanel panel = (FBPanel) component;
		
		encodeHeader(panel, writer, clientId);
		writer.endElement("DIV");
	}
	
	private void encodeHeader(UIComponent component, ResponseWriter writer, String clientId) throws IOException {
		writer.startElement("DIV", component);
		writer.startElement("P", component);
		writer.writeText("This is normal", null);
		writer.endElement("P");
		writer.endElement("DIV");
	}
}
