package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.div.Div;

import com.idega.formbuilder.presentation.FBDesignView;

public class DesignViewRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBDesignView designView = (FBDesignView) component;
		designView.initializeComponent(context);
		writer.startElement("DIV", designView);
		writer.writeAttribute("id", designView.getId(), "id");
		writer.writeAttribute("class", designView.getStyleClass(), "styleClass");
		
		ValueBinding vb = designView.getValueBinding("status");
		String status = null;
		if(vb != null) {
			status = (String) vb.getValue(context);
		} else {
			status = designView.getStatus();
		}
		if(status != null) {
			if(status.equals(FBDesignView.DESIGN_VIEW_STATUS_NOFORM)) {
				Div noFormNotice = (Div) designView.getFacet(FBDesignView.DESIGN_VIEW_NOFORM_FACET);
				if(noFormNotice != null) {
					if (noFormNotice.isRendered()) {
						noFormNotice.encodeBegin(context);
						noFormNotice.encodeChildren(context);
						noFormNotice.encodeEnd(context);
					}
				}
			} else if(status.equals(FBDesignView.DESIGN_VIEW_STATUS_EMPTY)) {
				Div formHeader = (Div) designView.getFacet(FBDesignView.DESIGN_VIEW_HEADER_FACET);
				if (formHeader != null) {
					if (formHeader.isRendered()) {
						formHeader.encodeBegin(context);
						formHeader.encodeChildren(context);
						formHeader.encodeEnd(context);
					}
				}
				Div emptyNotice = (Div) designView.getFacet(FBDesignView.DESIGN_VIEW_EMPTY_FACET);
				if (emptyNotice != null) {
					if (emptyNotice.isRendered()) {
						emptyNotice.encodeBegin(context);
						emptyNotice.encodeChildren(context);
						emptyNotice.encodeEnd(context);
					}
				}
			} else if(status.equals(FBDesignView.DESIGN_VIEW_STATUS_ACTIVE)) {
				Div formHeader = (Div) designView.getFacet(FBDesignView.DESIGN_VIEW_HEADER_FACET);
				if (formHeader != null) {
					if (formHeader.isRendered()) {
						formHeader.encodeBegin(context);
						formHeader.encodeChildren(context);
						formHeader.encodeEnd(context);
					}
				}
			}
		}
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBDesignView view = (FBDesignView) component;
		writer.endElement("DIV");
		Object values[] = new Object[2];
		values[0] = view.getId();
		values[1] = view.getComponentStyleClass();
		writer.write(getEmbededJavascript(values));
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		super.encodeChildren(context, component);
		
	}
	
	protected void switchVisibility(Div component, boolean makeVisible) {
		if(makeVisible) {
			component.setStyleClass(component.getStyleClass() + " " + FBDesignView.FACET_VISIBLE_STYLECLASS);
		} else {
			component.setStyleClass(component.getStyleClass() + " " + FBDesignView.FACET_INVISIBLE_STYLECLASS);
		}
	}
	
	protected String getEmbededJavascript(Object values[]) {
		return 	"<script language=\"JavaScript\">\n"
				+ "function setupDragAndDrop() {\n"
				+ "Position.includeScrollOffsets = true;\n"
				+ "Sortable.create(\"" + values[0] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:testing,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "Droppables.add(\"" + values[0] + "\",{onDrop:handleComponentDrop});\n"
				+ "}\n"
				+ "function handleComponentDrop(element,container) {\n"
				+ "switchFacets(false, true, false);"
				+ "if(currentElement != null) {\n"
				+ "var length = $(\"" + values[0] + "\").childNodes.length;\n"
				+ "var submit = $(\"" + values[0] + "\").childNodes[length-1];\n"
				+ "$(\"" + values[0] + "\").insertBefore(currentElement,submit);\n"
				+ "}\n"
				+ "currentElement = null;\n"
				+ "Sortable.create(\"" + values[0] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:testing,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "Droppables.add(\"" + values[0] + "\",{onDrop:handleComponentDrop});\n"
				+ "}\n"
				+ "function testing() {\n"
				+ "var componentIDs = Sortable.serialize(\"" + values[0] + "\",{tag:\"div\",name:\"id\"});\n"
				+ "var delimiter = '&id[]=';\n"
				+ "var idPrefix = 'fbcomp_';\n"
				+ "dwrmanager.updateComponentList(updateOrder,componentIDs,idPrefix,delimiter);\n"
				+ "}\n"
				+ "function updateOrder() {\n"
				+ "}\n"
				+ "setupDragAndDrop();\n"
				+ "</script>\n";
	}
}
