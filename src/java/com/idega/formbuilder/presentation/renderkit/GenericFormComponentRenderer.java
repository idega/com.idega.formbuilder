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
import com.idega.formbuilder.presentation.FBGenericFormComponent;

public class GenericFormComponentRenderer extends Renderer {
	
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		
	}
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		FBGenericFormComponent field = (FBGenericFormComponent) component;
		String elementId = null;
		IFormManager fbInstance = null;
		try {
			fbInstance = (IFormManager) context.getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
			elementId = fbInstance.createFormComponent(field.getType(), null);
			Element element = fbInstance.getLocalizedFormHtmlComponent(elementId, new Locale("en"));
			GenericFieldParser.renderNode(element, field, writer);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void decode(FacesContext context, UIComponent component) {
		
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) {
		
	}

}
