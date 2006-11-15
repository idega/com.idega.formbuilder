package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBSelectValuesList;

public class SelectValuesListRenderer extends Renderer {

	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBSelectValuesList valuesList = (FBSelectValuesList) component;
		valuesList.initializeComponent(context);
		writer.startElement("DIV", valuesList);
		writer.writeAttribute("id", valuesList.getId(), "id");
		writer.writeAttribute("class", valuesList.getStyleClass(), "styleClass");
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBSelectValuesList valuesList = (FBSelectValuesList) component;
		
		writer.endElement("DIV");
		
		Object values[] = new Object[3];
		values[0] = valuesList.getId();
		//writer.write(getEmbededJavascript(values));
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
	}
	
}
