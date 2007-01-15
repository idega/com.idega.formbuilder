package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.div.Div;

import com.idega.formbuilder.presentation.FBViewPanel;

public class WorkspaceRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		FBViewPanel workspace = (FBViewPanel) component;
		ResponseWriter writer = context.getResponseWriter();
		//workspace.initializeComponent(context);
		
		
		writer.startElement("DIV", workspace);
		writer.writeAttribute("id", workspace.getId(), "id");
		writer.writeAttribute("class", workspace.getStyleClass(), "styleClass");
		
		//workspace.getChildren().clear();
		
		ValueBinding vb = workspace.getValueBinding("view");
		String view = null;
		if(vb != null) {
			view = (String) vb.getValue(context);
		} else {
			view = workspace.getView();
		}
		if(view != null) {
			if(view.equals("design")) {
				UIComponentBase designView = (UIComponentBase) workspace.getFacet(view);
				if(designView != null) {
					if (designView.isRendered()) {
						designView.encodeBegin(context);
						designView.encodeChildren(context);
						designView.encodeEnd(context);
					}
				}
			} else if(view.equals("preview")) {
				UIComponentBase facet2 = (UIComponentBase) component.getFacet(view);
				if (facet2 != null) {
					if (facet2.isRendered()) {
						facet2.encodeBegin(context);
						facet2.encodeChildren(context);
						facet2.encodeEnd(context);
					}
				}
			} else if(view.equals("source")) {
				UIComponentBase facet3 = (UIComponentBase) component.getFacet(view);
				if (facet3 != null) {
					if (facet3.isRendered()) {
						facet3.encodeBegin(context);
						facet3.encodeChildren(context);
						facet3.encodeEnd(context);
					}
				}
			}
		}
		Div viewSwitch = (Div) workspace.getFacet("viewSwitch");
		if(viewSwitch != null) {
			if (viewSwitch.isRendered()) {
				viewSwitch.encodeBegin(context);
				viewSwitch.encodeChildren(context);
				viewSwitch.encodeEnd(context);
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
