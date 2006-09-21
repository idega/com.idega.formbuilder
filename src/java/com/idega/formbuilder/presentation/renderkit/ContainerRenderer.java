
package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.webface.WFContainer;

/**
 * <p>
 * Default renderer for the FBContainer component.<br />
 * 
 * Processes the same stuff as ContainerRenderer from webface plus id attribute
 * </p>
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class ContainerRenderer extends com.idega.webface.renderkit.ContainerRenderer {

	public void encodeBegin(FacesContext ctx, UIComponent comp) throws IOException {
		
//		try {
//			
//			IFormBuilder fb = FormBuilderFactory.createFormBuilder();
//
//			FormPropertiesBean form_props = new FormPropertiesBean();
//			form_props.setId(new Long(21));
//			form_props.setName("my form name");
//
//			fb.createFormDocument(form_props);
//			
//			System.out.println("trying to create an element ---------- ");
//			fb.createFormComponent("fbcomp_text", null);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		WFContainer container = (WFContainer) comp;
		if (!container.isRendered()) {
			return;
		}
		super.encodeBegin(ctx, comp);
		ResponseWriter out = ctx.getResponseWriter();
		
		if(container.getId() != null)
			out.writeAttribute("id", container.getId(), null);
	}

	protected String getStyleClass(WFContainer container) {
		return container.getStyleClass();
	}
	
	protected String getStyleAttributes(WFContainer container){

		return container.getStyleAttribute();
	}
}