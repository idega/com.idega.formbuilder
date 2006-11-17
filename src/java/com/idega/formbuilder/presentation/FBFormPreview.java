package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.idega.block.formreader.business.FormReader;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.view.ActionManager;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBFormPreview extends UIComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormPreview.class);
	
	private static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "FBFormPreview";
	private static final String container_tag = "div";
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	@Override
	public void encodeBegin(FacesContext ctx) throws IOException {
		super.encodeBegin(ctx);
		
		ctx.getResponseWriter().startElement(container_tag, null);
	}
	
	@Override
	public void encodeEnd(FacesContext ctx) throws IOException {
		
		ResponseWriter response_writer = ctx.getResponseWriter();
		
		try {
			
			IFormManager form_manager = ActionManager.getFormManagerInstance();
			
			if(form_manager != null) {
				
				Document x = form_manager.getFormXFormsDocument();
				
				FormReader form_reader = FormReader.getInstance();
				form_reader.init();
				form_reader.setFormDocument(x);
				
				form_reader.setOutput(response_writer);
				form_reader.generate();
			}
			
		} catch (Exception e) {
			logger.error("Error when parsing form to response writer", e);
		}
		
		response_writer.endElement(container_tag);
		super.encodeEnd(ctx);
	}
}