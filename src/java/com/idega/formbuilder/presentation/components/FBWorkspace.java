package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.ajax4jsf.ajax.html.HtmlAjaxOutputPanel;

import com.idega.block.web2.business.Web2Business;
import com.idega.business.IBOLookup;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObjectUtil;
import com.idega.presentation.Script;


public class FBWorkspace extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Workspace";
	
	private static final String WORKSPACE_MENU_FACET = "menu";
	private static final String WORKSPACE_VIEW_FACET = "view";
	private static final String WORKSPACE_PAGES_FACET = "pages";
	private static final String WORKSPACE_ACTIONS_PROXY_FACET = "proxy";
	private static final String FORMBUILDER_PAGE_SCRIPT = "script";
	
	private static final String FORMBUILDER_JS = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js";
	
	private String view;

	public FBWorkspace() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		getChildren().clear();
	
		Page parentPage = PresentationObjectUtil.getParentPage(this);
		if (parentPage != null) {
			try {
				Web2Business business = (Web2Business) IBOLookup.getServiceInstance(IWContext.getInstance(), Web2Business.class);
				String prototypeURI = business.getBundleURIToPrototypeLib();
//				String scriptaculousURI = business.getBundleURIToScriptaculousLib();
				String dojoURI = business.getBundleURIToDojoLib();
				String ricoURI = business.getBundleURIToRicoLib();

				Script script = new Script();
				script.addScriptSource(prototypeURI);
//				script.addScriptSource(scriptaculousURI);
				script.addScriptSource(dojoURI);
				script.addScriptSource(ricoURI);
				script.addScriptSource(FORMBUILDER_JS);
				
//				// THIS HAS TO BE ADDED TO THE <BODY> in the html, if not it does not work in Safari
				parentPage.setOnLoad("javascript:bodyOnLoad()");
			
				addFacet(FORMBUILDER_PAGE_SCRIPT, script);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ValueBinding vb = getValueBinding("view");
		if(vb != null) {
			view = (String) vb.getValue(context);
		}
		
		FBMenu menu = (FBMenu) application.createComponent(FBMenu.COMPONENT_TYPE);
		menu.setId("optionsPanel");
		menu.setStyleClass("optionsContainer");
		menu.setValueBinding("selectedMenu", application.createValueBinding("#{workspace.selectedMenu}"));
		menu.setValueBinding("show", application.createValueBinding("#{workspace.renderedMenu}"));

		HtmlAjaxOutputPanel viewPanel = new HtmlAjaxOutputPanel();
		viewPanel.setId("ajaxViewPanel");
		
		FBViewPanel views = (FBViewPanel) application.createComponent(FBViewPanel.COMPONENT_TYPE);
		views.setValueBinding("view", vb);
		views.setId("viewPanel");
		views.setStyleClass("formContainer");
		addChild(views, viewPanel);
		
		FBPagesPanel pages = (FBPagesPanel) application.createComponent(FBPagesPanel.COMPONENT_TYPE);
		pages.setId("pagesPanel");
		pages.setStyleClass("pagesPanel");
		pages.setComponentStyleClass("formPageIcon");
		pages.setGeneralPartStyleClass("pagesGeneralContainer");
		pages.setSpecialPartStyleClass("pagesSpecialContainer");
		pages.setSelectedStyleClass("formPageIconSelected");
		
		FBActionsProxy actionsProxy = (FBActionsProxy) application.createComponent(FBActionsProxy.COMPONENT_TYPE);
		
		addFacet(WORKSPACE_MENU_FACET, menu);
		addFacet(WORKSPACE_VIEW_FACET, viewPanel);
		addFacet(WORKSPACE_PAGES_FACET, pages);
		addFacet(WORKSPACE_ACTIONS_PROXY_FACET, actionsProxy);
	}
	
	public String getEmbededJavascript() {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		result.append("var djConfig = { isDebug: true };\n");
		result.append("</script>\n");
		return result.toString();
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		writer.write(getEmbededJavascript());
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
		UIComponent script = getFacet(FORMBUILDER_PAGE_SCRIPT);
		if(script != null) {
			renderChild(context, script);
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
