package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.components.FBComponentPropertiesPanel;

public class ComponentPropertiesPanelRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		FBComponentPropertiesPanel panel = (FBComponentPropertiesPanel) component;
		panel.initializeComponent(context);
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", panel);
		writer.writeAttribute("id", panel.getId(), "id");
		writer.writeAttribute("class", panel.getStyleClass(), "styleClass");
		writer.writeAttribute("style", "display: block", null);
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		//writer.write(getEmbededJavascript(null));
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
	}

	protected String getEmbededJavascript(Object values[]) {
		return 	"<script language=\"JavaScript\">\n"
				+ "function applyChanges() {\n"
				+ "document.forms['workspaceform1'].elements['workspaceform1:applyButton'].click();\n"
				+ "}\n"
				+ "</script>\n";
	}
	
}
