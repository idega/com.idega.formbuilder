package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.presentation.IWBaseComponent;

public class FBFormPropertiesPanel extends IWBaseComponent {

	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "FormPropertiesPanel";
	
	private String styleClass;

	public FBFormPropertiesPanel() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBFormPropertiesPanel.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
		//System.out.println("INITIAZILING FORM PROPERTIES PANEL");
		Application application = context.getApplication();
		
		//this.setId("kwakwakwa");
		FBFormProperties formProperties = (FBFormProperties) application.createComponent(FBFormProperties.COMPONENT_TYPE);
		add(formProperties);
		
		FBSubmitProperties submitProperties = (FBSubmitProperties) application.createComponent(FBSubmitProperties.COMPONENT_TYPE);
		add(submitProperties);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		//Application application = context.getApplication();
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		writer.writeAttribute("style", "display: block", null);
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		//System.out.println("ENCODE CHILDREN: " + this.getChildCount());
		Iterator it = this.getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
}
