package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import com.idega.formbuilder.business.FormComponent;
import com.idega.formbuilder.presentation.FBPalette;
import com.idega.formbuilder.presentation.FBPaletteComponent;

public class PaletteRenderer extends Renderer {
	
	protected void initializeComponent(FacesContext context, UIComponent component) {
		Application application = context.getApplication();
		FBPalette palette = (FBPalette) component;
		palette.getChildren().clear();
		ValueBinding vb = palette.getValueBinding("items");
		if(vb != null) {
			List items = (List) vb.getValue(context);
			Iterator it = items.iterator();
			while(it.hasNext()) {
				FormComponent current = (FormComponent) it.next();
				FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
				formComponent.setStyleClass(palette.getItemStyleClass());
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				palette.getChildren().add(formComponent);
			}
		}
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		initializeComponent(context, component);
		ResponseWriter writer = context.getResponseWriter();
		FBPalette palette = (FBPalette) component;
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
