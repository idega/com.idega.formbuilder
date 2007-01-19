package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxCommandButton;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBActionsProxy extends FBComponentBase {
	
//	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "ActionsProxy";
	
	public FBActionsProxy() {
		super();
		this.setRendererType(null);
	}
	
	/*public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}*/
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		UIAjaxCommandButton createForm = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		createForm.setId("createFormProxy");
		createForm.setOncomplete("closeLoadingMessage()");
		createForm.setAjaxSingle(true);
//		createForm.setActionListener(application.createMethodBinding("#{createFormAction.processAction}", new Class[]{ActionEvent.class}));
		createForm.setReRender("mainApplication");
		
		UIAjaxCommandButton changeMenu = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		changeMenu.setId("changeMenuProxy");
//		changeMenu.setOncomplete("closeLoadingMessage()");
		changeMenu.setAjaxSingle(true);
//		changeMenu.setActionListener(application.createMethodBinding("#{workspace.processAction}", new Class[]{ActionEvent.class}));
		changeMenu.setReRender("workspaceform1:ajaxMenuPanel");
		
		UIAjaxCommandButton deleteComponent = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		deleteComponent.setId("removeCompProxy");
		deleteComponent.setOncomplete("closeLoadingMessage()");
		deleteComponent.setAjaxSingle(true);
//		deleteComponent.setActionListener(application.createMethodBinding("#{createFormAction.processAction}", new Class[]{ActionEvent.class}));
		deleteComponent.setReRender("mainApplication");
		
		UIAjaxCommandButton getProperties = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		getProperties.setId("getCompProperties");
		getProperties.setOncomplete("closeLoadingMessage()");
		getProperties.setAjaxSingle(true);
//		deleteComponent.setActionListener(application.createMethodBinding("#{createFormAction.processAction}", new Class[]{ActionEvent.class}));
		getProperties.setReRender("mainApplication");
		
		UIAjaxCommandButton saveFormTitle = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		saveFormTitle.setId("saveFormTitle");
		saveFormTitle.setAjaxSingle(false);
		saveFormTitle.setActionListener(application.createMethodBinding("#{formDocument.saveFormTitle}", new Class[]{ActionEvent.class}));
		saveFormTitle.setReRender("mainApplication");
		
		UIAjaxCommandButton saveSubmitLabel = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		saveSubmitLabel.setId("saveSubmitLabel");
		saveSubmitLabel.setAjaxSingle(false);
		saveSubmitLabel.setActionListener(application.createMethodBinding("#{formDocument.saveSubmitLabel}", new Class[]{ActionEvent.class}));
		saveSubmitLabel.setReRender("mainApplication");
		
		UIAjaxCommandButton saveCompLabel = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		saveCompLabel.setId("saveCompLabel");
		saveCompLabel.setAjaxSingle(false);
		saveCompLabel.setActionListener(application.createMethodBinding("#{formComponent.saveComponentLabel}", new Class[]{ActionEvent.class}));
		saveCompLabel.setReRender("mainApplication");
		
		UIAjaxCommandButton saveCompReq = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		saveCompReq.setId("saveCompReq");
		saveCompReq.setAjaxSingle(false);
		saveCompReq.setActionListener(application.createMethodBinding("#{formComponent.saveComponentRequired}", new Class[]{ActionEvent.class}));
		saveCompReq.setReRender("mainApplication");
		
		UIAjaxCommandButton saveCompErr = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		saveCompErr.setId("saveCompErr");
		saveCompErr.setAjaxSingle(false);
		saveCompErr.setActionListener(application.createMethodBinding("#{formComponent.saveComponentErrorMessage}", new Class[]{ActionEvent.class}));
		saveCompErr.setReRender("mainApplication");
		
		add(createForm);
		add(deleteComponent);
		add(getProperties);
		add(changeMenu);
		add(saveFormTitle);
		add(saveSubmitLabel);
		add(saveCompLabel);
		add(saveCompReq);
		add(saveCompErr);
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
