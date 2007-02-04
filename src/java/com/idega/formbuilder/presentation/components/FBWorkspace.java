package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.ajax4jsf.ajax.html.HtmlAjaxOutputPanel;

import com.idega.formbuilder.presentation.FBComponentBase;


public class FBWorkspace extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Workspace";
	
	private static final String WORKSPACE_MENU_FACET = "menu";
	private static final String WORKSPACE_VIEW_FACET = "view";
	private static final String WORKSPACE_ACTIONS_PROXY_FACET = "proxy";
	
	private String id;
	private String styleClass;
	private String view;

	public FBWorkspace() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		this.getChildren().clear();
		
		ValueBinding vb = this.getValueBinding("view");
		if(vb != null) {
			view = (String) vb.getValue(context);
		}
		
//		HtmlAjaxRegion menuPanelRegion = new HtmlAjaxRegion();
//		menuPanelRegion.setSelfRendered(true);
////		menuPanelRegion
		
		HtmlAjaxOutputPanel menuPanel = new HtmlAjaxOutputPanel();
		menuPanel.setId("ajaxMenuPanel");
		
		FBMenu menu = (FBMenu) application.createComponent(FBMenu.COMPONENT_TYPE);
		menu.setId("optionsPanel");
		menu.setStyleClass("optionsContainer");
		menu.setValueBinding("selectedMenu", application.createValueBinding("#{workspace.selectedMenu}"));
		menu.setValueBinding("show", application.createValueBinding("#{workspace.renderedMenu}"));
		addChild(menu, menuPanel);
//		menuPanel.getChildren().add(menu);
//		menuPanelRegion.getChildren().add(menuPanel);
		
		HtmlAjaxOutputPanel viewPanel = new HtmlAjaxOutputPanel();
		viewPanel.setId("ajaxViewPanel");
		
		FBViewPanel views = (FBViewPanel) application.createComponent(FBViewPanel.COMPONENT_TYPE);
		views.setValueBinding("view", vb);
		views.setId("viewPanel");
		views.setStyleClass("formContainer");
		addChild(views, viewPanel);
//		viewPanel.getChildren().add(views);
		
		
		FBActionsProxy actionsProxy = (FBActionsProxy) application.createComponent(FBActionsProxy.COMPONENT_TYPE);
		
		addFacet(WORKSPACE_MENU_FACET, menuPanel);
		addFacet(WORKSPACE_VIEW_FACET, viewPanel);
		addFacet(WORKSPACE_ACTIONS_PROXY_FACET, actionsProxy);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", id, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!this.isRendered()) {
			return;
		}
		UIComponent menu = getFacet(WORKSPACE_MENU_FACET);
		if(menu != null) {
			renderChild(context, menu);
		}
		UIComponent view = getFacet(WORKSPACE_VIEW_FACET);
		if(view != null) {
			renderChild(context, view);
		}
		UIComponent proxy = getFacet(WORKSPACE_ACTIONS_PROXY_FACET);
		if(proxy != null) {
			renderChild(context, proxy);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = view;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		view = (String) values[3];
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
