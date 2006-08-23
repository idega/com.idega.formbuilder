package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBPanel;

public class PanelRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBPanel panel = (FBPanel) component;
		writer.startElement("DIV", panel);
		writer.writeAttribute("style", "position: absolute; top: " + panel.getTop() + "px; left: " + panel.getLeft() + "px;","position");
		writer.writeAttribute("class", panel.getStyleClass(), "styleClass");
			writer.startElement("DIV", panel);
			writer.writeAttribute("id", "fb_panel_header", "headerStyle");
				writer.startElement("DIV", panel);
				writer.writeAttribute("id", "fb_panel_header_text", "headerTextStyle");
				writer.writeText(panel.getTitle(), null);
				writer.endElement("DIV");
				
				writer.startElement("DIV", panel);
				writer.writeAttribute("id", "fb_panel_header_icon", "headerIconStyle");
				writer.endElement("DIV");
			writer.endElement("DIV");
			writer.startElement("DIV", panel);
			writer.endElement("DIV");
		writer.endElement("DIV");
	}
	
	public void decode(FacesContext context, UIComponent component) {
		
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) {
		
	}
}
