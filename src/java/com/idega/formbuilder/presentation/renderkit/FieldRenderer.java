package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBFormField;

public class FieldRenderer extends Renderer {

	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBFormField field = (FBFormField) component;
		writer.startElement("DIV", field);
		writer.writeAttribute("class", field.getStyleClass(), "styleClass");
		System.out.println("Render end " + field.getValue());
		writer.writeText(field.getValue(), null);
		writer.endElement("DIV");
	}
	
	public void decode(FacesContext context, UIComponent component) {
		
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) {
		
	}
	
}
