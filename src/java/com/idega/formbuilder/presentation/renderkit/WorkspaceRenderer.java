package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.div.Div;

import com.idega.formbuilder.presentation.FBDesignView;
import com.idega.formbuilder.presentation.FBWorkspace;

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
				FBDesignView designView = (FBDesignView) workspace.getFacet(view);
				if(designView != null) {
					if (designView.isRendered()) {
						designView.encodeBegin(context);
						designView.encodeChildren(context);
						designView.encodeEnd(context);
					}
				}
			} else if(view.equals("preview")) {
				Div facet2 = (Div) component.getFacet(view);
				if (facet2 != null) {
					if (facet2.isRendered()) {
						facet2.encodeBegin(context);
						facet2.encodeChildren(context);
						facet2.encodeEnd(context);
					}
				}
			} else if(view.equals("source")) {
				Div facet3 = (Div) component.getFacet(view);
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
		//System.out.println("RENDERING WORKSPACE CHILDREN: " + component.getChildCount());
		super.encodeChildren(context, component);
		
	}
}
