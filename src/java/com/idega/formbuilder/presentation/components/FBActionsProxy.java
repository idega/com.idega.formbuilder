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
		
//		UIAjaxCommandButton saveCompLabel = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
//		saveCompLabel.setId("saveCompLabel");
//		saveCompLabel.setAjaxSingle(false);
//		saveCompLabel.setActionListener(application.createMethodBinding("#{formComponent.saveComponentLabel}", new Class[]{ActionEvent.class}));
//		saveCompLabel.setReRender("mainApplication");
//		
//		UIAjaxCommandButton saveCompReq = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
//		saveCompReq.setId("saveCompReq");
//		saveCompReq.setAjaxSingle(false);
//		saveCompReq.setActionListener(application.createMethodBinding("#{formComponent.saveComponentRequired}", new Class[]{ActionEvent.class}));
//		saveCompReq.setReRender("mainApplication");
		
//		UIAjaxCommandButton saveCompErr = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
//		saveCompErr.setId("saveCompErr");
//		saveCompErr.setAjaxSingle(false);
//		saveCompErr.setActionListener(application.createMethodBinding("#{formComponent.saveComponentErrorMessage}", new Class[]{ActionEvent.class}));
//		saveCompErr.setReRender("mainApplication");
//		
//		UIAjaxCommandButton saveEmptyLabel = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
//		saveEmptyLabel.setId("saveEmptyLabel");
//		saveEmptyLabel.setAjaxSingle(false);
//		saveEmptyLabel.setActionListener(application.createMethodBinding("#{formComponent.saveComponentEmptyLabel}", new Class[]{ActionEvent.class}));
//		saveEmptyLabel.setReRender("mainApplication");
//		
//		UIAjaxCommandButton saveExtSrc = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
//		saveExtSrc.setId("saveExtSrc");
//		saveExtSrc.setAjaxSingle(false);
//		saveExtSrc.setActionListener(application.createMethodBinding("#{formComponent.saveComponentExternalSource}", new Class[]{ActionEvent.class}));
//		saveExtSrc.setReRender("mainApplication");
//		
//		UIAjaxCommandButton switcher = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
//		switcher.setId("srcSwitcher");
//		switcher.setAjaxSingle(false);
//		switcher.setActionListener(application.createMethodBinding("#{formComponent.saveComponentDataSource}", new Class[]{ActionEvent.class}));
//		switcher.setReRender("workspaceform1:ajaxMenuPanel");
		
		HtmlAjaxCommandButton refreshView = (HtmlAjaxCommandButton) application.createComponent(HtmlAjaxCommandButton.COMPONENT_TYPE);
		refreshView.setId("refreshViewPanel");
		refreshView.setAjaxSingle(true);
		refreshView.setReRender("workspaceform1:ajaxViewPanel");
		
//		add(saveCompLabel);
//		add(saveCompReq);
//		add(saveCompErr);
//		add(saveEmptyLabel);
//		add(saveExtSrc);
//		add(switcher);
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
