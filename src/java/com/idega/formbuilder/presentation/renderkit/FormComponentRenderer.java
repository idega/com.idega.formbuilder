package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.FBFormComponent;

public class FormComponentRenderer extends Renderer {
	
	/*protected void initializeComponents(FacesContext context, UIComponent component) throws FBPostponedException {
		FBFormComponent field = (FBFormComponent) component;
		IFormManager formManagerInstance = (IFormManager) context.getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		Element element = formManagerInstance.getLocalizedFormHtmlComponent(field.getId(), new Locale("en"));
		element.setAttribute("class", field.getStyleClass());
		field.setElement(element);
	}*/
	
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		FBFormComponent field = (FBFormComponent) component;
		try {
			field.initializeComponent(context);
		} catch(FBPostponedException pe) {
			pe.printStackTrace();
		}
		ResponseWriter writer = context.getResponseWriter();
		//FBFormComponent field = (FBFormComponent) component;
		//GenericFieldParser.renderNode(field.getElement(), field, writer);
		DOMTransformer.renderNode(field.getElement(), field, writer);
		//temp.renderNode(node, component, writer)
	}
}
