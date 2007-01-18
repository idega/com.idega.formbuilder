package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.components.FBFormComponent;

public class FormComponentRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBFormComponent field = (FBFormComponent) component;
		writer.startElement("DIV", field);
		writer.writeAttribute("class", field.getStyleClass(), "styleClass");
		writer.writeAttribute("id", field.getId(), "id");
		/*try {
			field.initializeComponent(context);
		} catch(FBPostponedException pe) {
			pe.printStackTrace();
		}*/
		writer.writeAttribute("onclick", field.getOnclick(), "onclick");
		DOMTransformer.renderNode(field.getElement(), field, writer);
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
	}
	
}
