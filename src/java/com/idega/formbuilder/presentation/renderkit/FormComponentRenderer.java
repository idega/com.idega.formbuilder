package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.presentation.FBFormComponent;

public class FormComponentRenderer extends Renderer {
	
	protected void initializeComponents(FacesContext context, UIComponent component) throws FBPostponedException {
		FBFormComponent field = (FBFormComponent) component;
		IFormManager formManagerInstance = (IFormManager) context.getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		Element element = formManagerInstance.getLocalizedFormHtmlComponent(field.getId(), new Locale("en"));
		element.setAttribute("class", field.getStyleClass());
		field.setElement(element);
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		try {
			initializeComponents(context, component);
		} catch(FBPostponedException pe) {
			pe.printStackTrace();
		}
		ResponseWriter writer = context.getResponseWriter();
		FBFormComponent field = (FBFormComponent) component;
		GenericFieldParser.renderNode(field.getElement(), field, writer);
	}
}
