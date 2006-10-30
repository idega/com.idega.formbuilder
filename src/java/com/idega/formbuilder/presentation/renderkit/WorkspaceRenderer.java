package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import com.idega.formbuilder.presentation.FBWorkspace;
import com.idega.formbuilder.view.design.FormDesignView;

public class WorkspaceRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		FBWorkspace workspace = (FBWorkspace) component;
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", workspace);
		writer.writeAttribute("id", workspace.getId(), "id");
		writer.writeAttribute("class", workspace.getStyleClass(), "styleClass");
		
		workspace.getChildren().clear();
		ValueBinding vb = workspace.getValueBinding("view");
		String view = null;
		if(vb != null) {
			view = (String) vb.getValue(context);
		} else {
			view = workspace.getView();
		}
		if(view != null) {
			if(view.equals("design")) {
				FormDesignView designView = (FormDesignView) workspace.getFacet(view);
				if(designView != null) {
					if (designView.isRendered()) {
						designView.encodeBegin(context);
						designView.encodeChildren(context);
						designView.encodeEnd(context);
					}
				}
			} else if(view.equals("preview")) {
				
			} else if(view.equals("source")) {
				
			} else {
				throw new IllegalArgumentException();
			}
		}
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
