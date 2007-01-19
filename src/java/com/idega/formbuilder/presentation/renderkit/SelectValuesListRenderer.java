package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.components.FBSelectValuesList;

public class SelectValuesListRenderer extends Renderer {

	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBSelectValuesList valuesList = (FBSelectValuesList) component;
		valuesList.initializeComponent(context);
		
		writer.startElement("DIV", valuesList);
		writer.writeAttribute("id", valuesList.getId(), "id");
		writer.writeAttribute("class", valuesList.getStyleClass(), "styleClass");
		writer.writeAttribute("style", "width: 320px; height: 180px;", null);
		
		UIComponent addOptionButton = valuesList.getFacet("addOptionButton");
		if(addOptionButton != null) {
			if (addOptionButton.isRendered()) {
				addOptionButton.encodeBegin(context);
				addOptionButton.encodeChildren(context);
				addOptionButton.encodeEnd(context);
			}
		}
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", valuesList.getId() + "Inner", null);
		writer.writeAttribute("style", "width: 320px; height: 150px; overflow: auto;", null);
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBSelectValuesList valuesList = (FBSelectValuesList) component;
		
		writer.endElement("DIV");
		writer.endElement("DIV");
		
		writer.startElement("DIV", null);
		writer.writeAttribute("style", "display: none", null);
		Object values[] = new Object[1];
		values[0] = valuesList.getId();
		writer.write(FBSelectValuesList.getEmbededJavascript(FBSelectValuesList.getJavascriptParameters(valuesList.getId())));
		writer.endElement("DIV");
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
		
	}

}
