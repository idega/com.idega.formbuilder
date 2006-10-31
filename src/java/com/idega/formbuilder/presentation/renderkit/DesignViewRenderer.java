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
	
	/*private void initializeComponent(FacesContext context, UIComponent component) throws FBPostponedException {
		Application application = context.getApplication();
		String formId = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_CURRENT_FORM_ID);
	    System.out.println("FORM_ID: " + formId);
		
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		FBDesignView view = (FBDesignView) component;
		view.getChildren().clear();
		List<String> ids = formManagerInstance.getFormComponentsIdsList();
		if(formId == null || formId.equals("")) {
	    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "NO_FORM");
	    } else if(ids.isEmpty()) {
	    	FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "EMPTY_FORM");
	    }
		Iterator it = ids.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
			formComponent.setId(nextId);
			formComponent.setStyleClass(view.getComponentStyleClass());
	        view.getChildren().add(formComponent);
		}
	}*/
	
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
		
		/*Div facet1 = (Div) component.getFacet("noFormNoticeFacet");
		Div facet2 = (Div) component.getFacet("formHeaderFacet");
		Div facet3 = (Div) component.getFacet("emptyFormFacet");
		if (facet1 != null) {
			if (facet1.isRendered()) {
				facet1.encodeBegin(context);
				facet1.encodeChildren(context);
				facet1.encodeEnd(context);
			}
		}
		if (facet2 != null) {
			if (facet2.isRendered()) {
				facet2.encodeBegin(context);
				facet2.encodeChildren(context);
				facet2.encodeEnd(context);
			}
		}
		if (facet3 != null) {
			if (facet3.isRendered()) {
				facet3.encodeBegin(context);
				facet3.encodeChildren(context);
				facet3.encodeEnd(context);
			}
		}
		String status = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS);
		if(status != null) {
			if(status.equals("NO_FORM")) {
				switchVisibility(facet1, true);
				switchVisibility(facet2, false);
				switchVisibility(facet3, false);
			} else if(status.equals("EMPTY_FORM")) {
				switchVisibility(facet2, true);
				switchVisibility(facet3, true);
				switchVisibility(facet1, false);
			} else {
				switchVisibility(facet2, true);
				switchVisibility(facet1, false);
				switchVisibility(facet3, false);
			}
		}*/
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
