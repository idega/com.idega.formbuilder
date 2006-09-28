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
		Element element = null;
		try {
			IFormManager fbInstance = (IFormManager) context.getExternalContext().getSessionMap().get("formbuilderInstance");
			/*
			if(field == null) {
				System.out.println("COMPONENT IS NULL");
			}
			else if(field.getType() == null) {
				System.out.println("TYPE IS NULL");
			}
			*/
			element = fbInstance.createFormComponent(field.getType(), null);
		} catch(Exception e) {
			e.printStackTrace();
		}
		GenericFieldParser.renderNode(element, field, writer);
	}
	
	public void decode(FacesContext context, UIComponent component) {
		
	}
	
	public void encodeChildren(FacesContext context, UIComponent component) {
		
	}

}
