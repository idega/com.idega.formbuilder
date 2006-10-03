package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.w3c.dom.Element;

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
			fbInstance = (IFormManager) context.getExternalContext().getSessionMap().get("formbuilderInstance");
			elementId = fbInstance.createFormComponent(field.getType(), null);
		} catch(Exception e) {
			e.printStackTrace();
		}
		Element element = fbInstance.getLocalizedFormHtmlComponent(elementId, "en");
		GenericFieldParser.renderNode(element, field, writer);
	}
	
	public void decode(FacesContext context, UIComponent component) {
		
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) {
		
	}

}
