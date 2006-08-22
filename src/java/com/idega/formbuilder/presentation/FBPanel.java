package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class FBPanel extends UIPanel {
	
	private static String RENDERER_TYPE="fb_panel";
	
	private static final String EXPAND_IMG = "expand.jpg";
	private static final String COLLAPSE_IMG = "collapse.jpg";
	
	private String styleClass = null;
	private String title = null;
	private boolean expanded = true;
	
	public FBPanel() {
		super();
		//this.setRendererType(RENDERER_TYPE);
	}
	
	public String getFamily() {
		return ("formbuilder");
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		if ((context == null)|| (this == null)){
			throw new NullPointerException();
		}
		System.out.println("CHECKPOINT");
		ResponseWriter writer = context.getResponseWriter();
		//String clientId = component.getClientId(context);
		FBPanel panel = (FBPanel) this;
				
		writer.startElement("DIV", panel);
		writer.writeAttribute("style", "border: 5px; border-color: black; width: 200px; height: 120px; background-color: red;","styleClass");
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		if ((context == null) || (this == null)){
			throw new NullPointerException();
		}
		ResponseWriter writer = context.getResponseWriter();
		String clientId = this.getClientId(context);
		FBPanel panel = (FBPanel) this;
		
		encodeHeader(panel, writer, clientId);
		writer.endElement("DIV");
	}
	
	private void encodeHeader(UIComponent component, ResponseWriter writer, String clientId) throws IOException {
		writer.startElement("DIV", component);
		writer.startElement("P", component);
		writer.writeText("This is normal", null);
		writer.endElement("P");
		writer.endElement("DIV");
	}
}
