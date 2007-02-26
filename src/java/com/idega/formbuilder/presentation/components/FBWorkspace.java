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
	private static final String WORKSPACE_PAGES_FACET = "pages";
	private static final String WORKSPACE_ACTIONS_PROXY_FACET = "proxy";
	
	private String view;

	public FBWorkspace() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		getChildren().clear();
//		
//		Page parentPage = PresentationObjectUtil.getParentPage(this);
//		if (parentPage != null) {
//			try {
//				Web2Business business = (Web2Business) IBOLookup.getServiceInstance(IWContext.getInstance(), Web2Business.class);
//				String prototypeURI = business.getBundleURIToPrototypeLib();
////				String scriptaculousURI = business.getBundleURIToScriptaculousLib();
//				String ricoURI = business.getBundleURIToRico();
//	
//				Script s = parentPage.getAssociatedScript();
//				s.addScriptSource(prototypeURI);
////				s.addScriptSource(scriptaculousURI);
//				s.addScriptSource(ricoURI);
//
//				parentPage.addScriptSource(prototypeURI);
////				parentPage.addScriptSource(scriptaculousURI);
//				parentPage.addScriptSource(ricoURI);
//				
//				// THIS HAS TO BE ADDED TO THE <BODY> in the html, if not it does not work in Safari
//				parentPage.setOnLoad("javascript:bodyOnLoad()");
//				
//				add(s);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		ValueBinding vb = getValueBinding("view");
		if(vb != null) {
			view = (String) vb.getValue(context);
		}
		
		HtmlAjaxOutputPanel menuPanel = new HtmlAjaxOutputPanel();
		menuPanel.setId("ajaxMenuPanel");
		
		FBMenu menu = (FBMenu) application.createComponent(FBMenu.COMPONENT_TYPE);
		menu.setId("optionsPanel");
		menu.setStyleClass("optionsContainer");
		menu.setValueBinding("selectedMenu", application.createValueBinding("#{workspace.selectedMenu}"));
		menu.setValueBinding("show", application.createValueBinding("#{workspace.renderedMenu}"));
		addChild(menu, menuPanel);
		
		HtmlAjaxOutputPanel viewPanel = new HtmlAjaxOutputPanel();
		viewPanel.setId("ajaxViewPanel");
		
		FBViewPanel views = (FBViewPanel) application.createComponent(FBViewPanel.COMPONENT_TYPE);
		views.setValueBinding("view", vb);
		views.setId("viewPanel");
		views.setStyleClass("formContainer");
		addChild(views, viewPanel);
		
		HtmlAjaxOutputPanel pagesPanel = new HtmlAjaxOutputPanel();
		pagesPanel.setId("ajaxPagesPanel");
		
		FBPagesPanel pages = (FBPagesPanel) application.createComponent(FBPagesPanel.COMPONENT_TYPE);
		pages.setId("pagesPanel");
		pages.setStyleClass("pagesPanel");
		pages.setComponentStyleClass("formPageIcon");
		addChild(pages, pagesPanel);
		
		FBActionsProxy actionsProxy = (FBActionsProxy) application.createComponent(FBActionsProxy.COMPONENT_TYPE);
		
		addFacet(WORKSPACE_MENU_FACET, menuPanel);
		addFacet(WORKSPACE_VIEW_FACET, viewPanel);
		addFacet(WORKSPACE_PAGES_FACET, pagesPanel);
		addFacet(WORKSPACE_ACTIONS_PROXY_FACET, actionsProxy);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
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
		UIComponent menu = getFacet(WORKSPACE_MENU_FACET);
		if(menu != null) {
			renderChild(context, menu);
		}
		UIComponent view = getFacet(WORKSPACE_VIEW_FACET);
		if(view != null) {
			renderChild(context, view);
		}
		UIComponent pages = getFacet(WORKSPACE_PAGES_FACET);
		if(pages != null) {
			renderChild(context, pages);
		}
		UIComponent proxy = getFacet(WORKSPACE_ACTIONS_PROXY_FACET);
		if(proxy != null) {
			renderChild(context, proxy);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = view;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		view = (String) values[1];
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
