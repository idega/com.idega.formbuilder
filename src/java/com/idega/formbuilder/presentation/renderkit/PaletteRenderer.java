package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBPalette;
import com.idega.formbuilder.presentation.FBPaletteComponent;

public class PaletteRenderer extends Renderer {
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBPalette palette = (FBPalette) component;
		palette.initializeComponent(context);
		writer.startElement("DIV", palette);
		writer.writeAttribute("id", palette.getId(), "id");
		writer.writeAttribute("class", palette.getStyleClass(), "styleClass");
		writer.startElement("TABLE", null);
		
		int columns = palette.getColumns();
		int count = 1;
		
		Iterator it = palette.getChildren().iterator();
		while(it.hasNext()) {
			if((count % columns) == 1) {
				writer.startElement("TR", null);
			}
			FBPaletteComponent current = (FBPaletteComponent) it.next();
			if(current != null) {
				writer.startElement("TD", null);
				current.encodeEnd(context);
				writer.endElement("TD");
			}
			if((count % columns) == 0) {
				writer.endElement("TR");
			}
			count++;
		}
		
		writer.endElement("TABLE");
		writer.endElement("DIV");
	}

}
