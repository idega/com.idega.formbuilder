package com.idega.formbuilder.presentation;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.ajax.UIAjaxCommandButton;

import com.idega.presentation.IWBaseComponent;

public class FBActionsProxy extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "ActionsProxy";
	
	public FBActionsProxy() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		UIAjaxCommandButton createForm = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		createForm.setId("createFormProxy");
		createForm.setOncomplete("closeLoadingMessage()");
		createForm.setAjaxSingle(true);
//		createForm.setActionListener(application.createMethodBinding("#{createFormAction.processAction}", new Class[]{ActionEvent.class}));
		createForm.setReRender("mainApplication");
		
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
		
		this.getChildren().add(createForm);
		this.getChildren().add(deleteComponent);
		this.getChildren().add(getProperties);
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
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}

}
