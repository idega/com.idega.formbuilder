package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.formreader.business.FormReader;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.util.FBUtil;
import com.idega.formbuilder.view.ActionManager;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBFormPreview extends FBComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormPreview.class);
	
	public static final String COMPONENT_TYPE = "FormPreview";
	
	private static final String container_tag = "div";
	
	public void encodeBegin(FacesContext ctx) throws IOException {
		ResponseWriter writer = ctx.getResponseWriter();
		super.encodeBegin(ctx);
		
		writer.startElement(container_tag, this);
	}
	
	public void encodeEnd(FacesContext ctx) throws IOException {
		ResponseWriter writer = ctx.getResponseWriter();
		
		try {
			
			Document document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
			
			if(document != null) {
				
				org.w3c.dom.Document x = document.getXformsDocument();
				
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