package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.webface.WFToolbarButton;

/**
 * Button component used in FBToolbar. 
 * 
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 */
public class FBToolbarButton extends WFToolbarButton {

	private String toolTip;
	private String styleClass;
	
	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
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
			String click = "document.forms['" + formName + "'].elements['" + buttonId + 
			"'].value='true';document.forms['" + formName + "'].submit();";

			out.startElement("div", null);
			out.writeAttribute("class", styleClass, null);
//			out.writeAttribute("id", buttonId + "_C", null);
			out.writeAttribute("onclick", click, null);
			
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
//				String onmouseup = "document.forms['" + formName + "'].elements['" + buttonId + 
//				"'].value='true';document.forms['" + formName + "'].submit();";
//				out.writeAttribute("onmouseup", onmouseup, null);
				String onmouseout = "document.forms['" + formName + "'].elements['" + buttonId + "'].value='';this.src='" + 
						getDefaultImageURI() + "'";
				out.writeAttribute("onmouseout", onmouseout, null);
				out.endElement("img");
				
				out.startElement("a", null);
//				String onmouseup2 = "document.forms['" + formName + "'].elements['" + buttonId + 
//				"'].value='true';document.forms['" + formName + "'].submit();";
//				out.writeAttribute("onmouseup", onmouseup2, null);
				
				if (this.toolTip != null) {
					out.writeAttribute("alt", this.toolTip, null);
					out.writeAttribute("title", this.toolTip, null);
				}
				
				out.writeAttribute("href","#",null);
				
				String text = getDisplayText();
				if(text != null){
					out.write(text);
				}
				out.endElement("a");
				
			}
			out.endElement("div");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void encodeChildren(FacesContext context) {
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = toolTip;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		toolTip = (String) values[2];
	}
}
