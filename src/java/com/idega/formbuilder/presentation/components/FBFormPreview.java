package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.form.presentation.FormViewer;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.view.ActionManager;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 */
public class FBFormPreview extends FBComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormPreview.class);
	
	public static final String COMPONENT_TYPE = "FormPreview";
	public static final String FORM_VIEWER = "FORM_VIEWER";
	
	private static final String container_tag = "div";
	
	public void encodeBegin(FacesContext ctx) throws IOException {
		super.encodeBegin(ctx);
//		ResponseWriter writer = ctx.getResponseWriter();
//		super.encodeBegin(ctx);
//		
//		writer.startElement(container_tag, this);
	}
	
	@Override
	public void encodeEnd(FacesContext ctx) throws IOException {
//		ResponseWriter writer = ctx.getResponseWriter();
//		
//		try {
//			
//			Document document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
//			
//			if(document != null) {
//				
//				org.w3c.dom.Document x = document.getXformsDocument();
//				
//				FormReader form_reader = new FormReader();
//				
//				form_reader.setBaseFormURI(FBUtil.getWebdavServerUrl(ctx)+"/files/public/");
//				form_reader.setFormDocument(x);
//				
//				form_reader.setOutput(writer);
//				form_reader.generate();
//			}
//			
//		} catch (Exception e) {
//			logger.error("Error when parsing form to response writer", e);
//		}
//		writer.endElement(container_tag);
		super.encodeEnd(ctx);
	}
	
	@Override
	protected void initializeComponent(FacesContext context) {
		super.initializeComponent(context);
		
		Document xforms_document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
		if(xforms_document == null) {
			logger.error("Form document got form ActionManager was null");
			return;
		}
			
		FormViewer form_viewer = new FormViewer();
		form_viewer.setXFormsDocument((org.w3c.dom.Document)xforms_document.getXformsDocument().cloneNode(true));
		getFacets().put(FORM_VIEWER, form_viewer);
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		UIComponent form_viewer = getFacet(FORM_VIEWER);
		
		if(form_viewer != null) {
			
			form_viewer.setRendered(true);
			renderChild(context, form_viewer);
		}
	}
}