package com.idega.formbuilder.presentation;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.util.FBUtil;
import com.idega.idegaweb.IWBundle;
import com.idega.webface.WFToolbarButton;

/**
 * Button component used in FBToolbar. 
 * 
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBToolbarButton extends WFToolbarButton {

	private String toolTip;
	private String styleClass;
	private IOException io_exception_throwed_at_encodeBegin_state;
	
	public FBToolbarButton() {
		// Default contstructor, for JSF
	}

	public FBToolbarButton(String defaultImageURI, IWBundle bundle) {
		String uri = bundle.getResourcesVirtualPath()+"/"+defaultImageURI;
		this.setDefaultImageURI(uri);
	}
	
	/**
	 * 
	 * @param defaultImageURI - should be context relative image path
	 */
	public FBToolbarButton(String defaultImageURI) {
		String uriWithBundle = FBUtil.getBundle().getResourcesVirtualPath()+"/"+defaultImageURI;
		this.setDefaultImageURI(uriWithBundle);
	}
	
	public void encodeBegin(FacesContext context) {
		
		try {
			ResponseWriter out = context.getResponseWriter();
			
			String buttonId = getClientId(context);
			String imageId = buttonId + "_img";
			
			out.startElement("input", null);
			out.writeAttribute("id", buttonId, null);
			out.writeAttribute("type", "hidden", null);
			out.writeAttribute("name", buttonId, null);
			out.writeAttribute("value", "false", null);
			out.writeAttribute("style", "display:none;", null);
			out.endElement("input");

			String formName = determineFormName(this);
			if (getDefaultImageURI() != null) {
				out.startElement("img", null);
				out.writeAttribute("src", getDefaultImageURI(), null);
				out.writeAttribute("id", imageId, null);
				if (formName == null) {
					throw new IOException("Toolbars should be nested in a UIForm !");
				}
				if (this.toolTip != null) {
					out.writeAttribute("alt", this.toolTip, null);
					out.writeAttribute("title", this.toolTip, null);
				}
				if (getPressedImageURI() != null) {
					String onmousedown = "this.src='" + getPressedImageURI() +"'";
					out.writeAttribute("onmousedown", onmousedown, null);
				}
				
				if(getHoverImageURI() != null) {
					out.writeAttribute("onmouseover", "this.src='" + getHoverImageURI() +"'", null);
				}
				
				String onmouseup = "document.forms['" + formName + "'].elements['" + buttonId + 
				"'].value='true';document.forms['" + formName + "'].submit();";
				out.writeAttribute("onmouseup", onmouseup, null);
				String onmouseout = "document.forms['" + formName + "'].elements['" + buttonId + "'].value='';this.src='" + 
						getDefaultImageURI() + "'";
				out.writeAttribute("onmouseout", onmouseout, null);
				
			} else {
				out.startElement("a", null);
				String onmouseup = "document.forms['" + formName + "'].elements['" + buttonId + 
				"'].value='true';document.forms['" + formName + "'].submit();";
				out.writeAttribute("onmouseup", onmouseup, null);
				
				if (this.styleClass != null) {
					out.writeAttribute("class", this.styleClass, null);
				}
				if (this.toolTip != null) {
					out.writeAttribute("alt", this.toolTip, null);
					out.writeAttribute("title", this.toolTip, null);
				}
				
				out.writeAttribute("href","#",null);
				
				String text = getDisplayText();
				if(text != null){
					out.write(text);
				}
			}
			
		} catch (IOException e) {
			
			/* cannot throw exception due to lsp principle
			 * we can do this in encodeEnd method
			 * */
			io_exception_throwed_at_encodeBegin_state = e;
		}
	}
	
	/**
	 * @see javax.faces.component.UIComponent#encodeChildren(javax.faces.context.FacesContext)
	 */
	public void encodeChildren(FacesContext context) {
	}
	
	/**
	 * @see javax.faces.component.UIComponent#encodeEnd(javax.faces.context.FacesContext)
	 */
	public void encodeEnd(FacesContext context) throws IOException {
		
		/*
		 * look at encodeBegin method
		 * */
		if(io_exception_throwed_at_encodeBegin_state != null)
			throw io_exception_throwed_at_encodeBegin_state;
		
		ResponseWriter out = context.getResponseWriter();
		
		if (getDefaultImageURI() != null)
			out.endElement("img");
		else
			out.endElement("a");
	}
}
