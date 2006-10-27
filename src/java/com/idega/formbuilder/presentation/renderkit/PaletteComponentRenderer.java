package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBPaletteComponent;

public class PaletteComponentRenderer extends Renderer {
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBPaletteComponent comp = (FBPaletteComponent) component;
		writer.startElement("DIV", comp);
		writer.writeAttribute("class", comp.getStyleClass(), "styleClass");
		/*ValueBinding vb = comp.getValueBinding("type");
		String type = (String) vb.getValue(context);
		if(vb != null) {
			writer.writeAttribute("id", vb.getValue(context), "type");
		}*/
		writer.writeAttribute("id", comp.getType(), "type");
		writer.startElement("P", null);
		/*vb = comp.getValueBinding("name");
		if(vb != null) {
			writer.writeText(vb.getValue(context), null);
		}*/
		writer.writeText(comp.getName(), null);
		writer.endElement("P");
		writer.endElement("DIV");
		writer.write(getEmbededJavascript(comp.getType()));
	}
	
	protected String getEmbededJavascript(String id) {
		return "<script language=\"JavaScript\">"
				+ "new Draggable(\"" + id + "\", {tag:\"div\",starteffect:handleComponentDrag,revert:true});"
				+ "</script>";
	}
}
