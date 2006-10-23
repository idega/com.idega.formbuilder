package com.idega.formbuilder.view.design;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.presentation.FBFormComponent;

public class FormDesignViewRenderer extends Renderer {
	
	private void initializeComponents(FacesContext context, UIComponent component) throws FBPostponedException {
		Application application = context.getApplication();
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		FormDesignView view = (FormDesignView) component;
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
