package com.idega.formbuilder.formviewer;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBFormField;

public class FormViewerRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		
		FormViewer field = (FormViewer) component;
		
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("DIV", field);
		System.out.println("KWAAAAAAAAAAAAAAAA");
		writer.writeAttribute("class", field.getStyleClass(), "styleClass");
		System.out.println("KWAAAAAAAAAAAAAAAA");
		writer.startElement("H1", null);
		writer.writeText(field.getStyleClass(), null);
		writer.endElement("H1");
		writer.endElement("DIV");
		
	}
	
	public void decode(FacesContext context, UIComponent component) {
		
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) {
		
	}
}
