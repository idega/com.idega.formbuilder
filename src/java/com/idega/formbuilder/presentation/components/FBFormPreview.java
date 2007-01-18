package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.idega.block.formreader.business.FormReader;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.util.FBUtil;
import com.idega.formbuilder.view.ActionManager;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBFormPreview extends UIComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormPreview.class);
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "FBFormPreview";
	
	private static final String container_tag = "div";
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public void encodeBegin(FacesContext ctx) throws IOException {
		ResponseWriter writer = ctx.getResponseWriter();
		super.encodeBegin(ctx);
		
		writer.startElement(container_tag, this);
	}
	
	public void encodeEnd(FacesContext ctx) throws IOException {
		ResponseWriter writer = ctx.getResponseWriter();
		
		try {
			
			IFormManager form_manager = ActionManager.getFormManagerInstance();
			
			if(form_manager != null) {
				
				Document x = form_manager.getFormXFormsDocument();
				
				FormReader form_reader = new FormReader();
				
				form_reader.setBaseFormURI(FBUtil.getWebdavServerUrl(ctx)+"/files/public/");
				form_reader.setFormDocument(x);
				
				form_reader.setOutput(writer);
				form_reader.generate();
			}
			
		} catch (Exception e) {
			logger.error("Error when parsing form to response writer", e);
		}
		writer.endElement(container_tag);
		super.encodeEnd(ctx);
	}
}