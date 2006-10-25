package com.idega.formbuilder.view.design;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.myfaces.custom.div.Div;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.presentation.FBFormComponent;

public class FormDesignViewRenderer extends Renderer {
	
	private void initializeComponents(FacesContext context, UIComponent component) throws FBPostponedException {
		Application application = context.getApplication();
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		FormDesignView view = (FormDesignView) component;
		view.getChildren().clear();
		List<String> ids = formManagerInstance.getFormComponentsIdsList();
		Iterator it = ids.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
			formComponent.setId(nextId);
			formComponent.setStyleClass(view.getComponentStyleClass());
	        view.getChildren().add(formComponent);
		}
	}
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		try {
			initializeComponents(context, component);
		} catch(FBPostponedException pe) {
			pe.printStackTrace();
		}
		FormDesignView field = (FormDesignView) component;
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", field);
		writer.writeAttribute("id", field.getId(), "id");
		writer.writeAttribute("class", field.getStyleClass(), "styleClass");
		String status = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS);
		if(status != null) {
			if(status.equals("NO_FORM")) {
				Div facet = (Div) component.getFacet("noFormNoticeFacet");
				if (facet != null) {
					if (facet.isRendered()) {
						facet.encodeBegin(context);
						//if (facet.getRendersChildren()) {
							facet.encodeChildren(context);
						//}
						facet.encodeEnd(context);
					}
				}
			} else if(status.equals("EMPTY_FORM")) {
				Div facet = (Div) component.getFacet("formHeaderFacet");
				if (facet != null) {
					if (facet.isRendered()) {
						facet.encodeBegin(context);
						//if (facet.getRendersChildren()) {
							facet.encodeChildren(context);
						//}
						facet.encodeEnd(context);
					}
				}
			}
		}
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FormDesignView view = (FormDesignView) component;
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
	
	protected String getEmbededJavascript(Object values[]) {
		return 	"<script language=\"JavaScript\">\n"
				+ "function setupDragAndDrop() {\n"
				+ "alert('Setup Drag and Drop');\n"
				+ "Position.includeScrollOffsets = true;\n"
				+ "Sortable.create(\"" + values[0] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:testing,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "Droppables.add(\"" + values[0] + "\",{onDrop:handleComponentDrop});\n"
				
				
				+ "}\n"
				+ "function handleComponentDrop(element,container) {\n"
				+ "alert('handleComponentDrop');\n"
				+ "if(currentElement != null) {\n"
				+ "$(\"" + values[0] + "\").appendChild(currentElement);\n"
				+ "}\n"
				+ "currentElement = null;\n"
				
				+ "Sortable.create(\"" + values[0] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:testing,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "Droppables.add(\"" + values[0] + "\",{onDrop:handleComponentDrop});\n"
				+ "}\n"
				+ "function testing() {\n"
				+ "alert('testing');\n"
				+ "var componentIDs = Sortable.serialize(\"" + values[0] + "\",{tag:\"div\",name:\"id\"});\n"
				+ "alert(componentIDs);\n"
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
