package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.ajax.html.HtmlAjaxCommandButton;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBActionsProxy extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ActionsProxy";
	
	public FBActionsProxy() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		HtmlAjaxCommandButton refreshView = (HtmlAjaxCommandButton) application.createComponent(HtmlAjaxCommandButton.COMPONENT_TYPE);
		refreshView.setId("refreshViewPanel");
		refreshView.setAjaxSingle(true);
		refreshView.setReRender("workspaceform1:ajaxViewPanel");
		add(refreshView);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", "actionsProxy", null);
		writer.writeAttribute("style", "display: none;", null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		Iterator it = this.getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
	}

}
