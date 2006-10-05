
package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.FormManagerFactory;
import com.idega.formbuilder.business.form.manager.IFormManager;
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
		
		try {
			
			System.out.println("__________________encode begin___________");
			
			long start = System.currentTimeMillis();
			IFormManager fb = FormManagerFactory.newFormManager(ctx);
			long end = System.currentTimeMillis();
			System.out.println("inited in: "+(end-start));
	//		System.out.println("<sugeneruoti komponentai > ");
	//		DOMUtil.prettyPrintDOM(components_xml);
	//		System.out.println("<sugeneruoti komponentai />");
			
			LocalizedStringBean title = new LocalizedStringBean();
			title.setString(new Locale("en"), "eng title");
			title.setString(new Locale("is"), "isl title");
			
			start = System.currentTimeMillis();
			fb.createFormDocument("1", title);
			end = System.currentTimeMillis();
			System.out.println("document created in: "+(end-start));
			
//			start = System.currentTimeMillis();
//			fb.createFormComponent("fbcomp_text", null);
//			end = System.currentTimeMillis();
//			System.out.println("text component created in: "+(end-start));
	////		
			start = System.currentTimeMillis();
			fb.createFormComponent("fbcomp_email", null);
			end = System.currentTimeMillis();
			System.out.println("email component created in: "+(end-start));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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