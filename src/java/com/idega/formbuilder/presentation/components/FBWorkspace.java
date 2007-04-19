package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.web2.business.Web2Business;
import com.idega.business.IBOLookup;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.presentation.IWContext;
import com.idega.webface.WFUtil;

public class FBWorkspace extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Workspace";

	private static final String WORKSPACE_MENU_FACET = "menu";

	private static final String WORKSPACE_VIEW_FACET = "view";

	private static final String WORKSPACE_PAGES_FACET = "pages";

	private static final String FORMBUILDER_JS = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js";

	private static final String DWR_ENGINE_JS = "/dwr/engine.js";

	private static final String DWR_UTIL_JS = "/dwr/util.js";

	private static final String FORM_COMPONENT_JS = "/dwr/interface/FormComponent.js";

	private static final String FORM_DOCUMENT_JS = "/dwr/interface/FormDocument.js";

	private static final String FORM_PAGE_JS = "/dwr/interface/FormPage.js";

	private String view;

	public FBWorkspace() {
		super();
		setRendererType(null);
	}

	protected void initializeComponent(FacesContext context) {

		if (context.getExternalContext().getRequestParameterMap().containsKey(
				FormDocument.FROM_APP_REQ_PARAM)) {
			FormDocument fd = (FormDocument) WFUtil
					.getBeanInstance(FormDocument.BEAN_ID);

			try {
				fd.createNewForm();

			} catch (Exception e) {
				// TODO: use logger and redirect back to applications list if
				// possible
				e.printStackTrace();
			}
		}

		Application application = context.getApplication();
		getChildren().clear();

//		try {
//			Web2Business business = (Web2Business) IBOLookup
//					.getServiceInstance(IWContext.getInstance(),
//							Web2Business.class);
////			String prototypeURI = business.getBundleURIToPrototypeLib();
//			String dojoURI = business.getBundleURIToDojoLib();
////			String ricoURI = business.getBundleURIToRicoLib();
//
//			AddResource resourceAdder = AddResourceFactory.getInstance(context);
//
//			// resourceAdder.addJavaScriptAtPosition(context,
//			// AddResource.HEADER_BEGIN, prototypeURI);
//			// resourceAdder.addJavaScriptAtPosition(context,
//			// AddResource.HEADER_BEGIN, ricoURI);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, dojoURI);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, FORMBUILDER_JS);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, DWR_ENGINE_JS);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, DWR_UTIL_JS);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, FORM_COMPONENT_JS);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, FORM_DOCUMENT_JS);
//			resourceAdder.addJavaScriptAtPosition(context,
//					AddResource.HEADER_BEGIN, FORM_PAGE_JS);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		ValueBinding vb = getValueBinding("view");
		if (vb != null) {
			view = (String) vb.getValue(context);
		}

		FBMenu menu = (FBMenu) application
				.createComponent(FBMenu.COMPONENT_TYPE);
		menu.setId("optionsPanel");
		menu.setStyleClass("optionsContainer");
		menu.setValueBinding("selectedMenu", application
				.createValueBinding("#{workspace.selectedMenu}"));
		menu.setValueBinding("show", application
				.createValueBinding("#{workspace.renderedMenu}"));

		FBViewPanel views = (FBViewPanel) application
				.createComponent(FBViewPanel.COMPONENT_TYPE);
		views.setValueBinding("view", vb);
		views.setId("viewPanel");
		views.setStyleClass("formContainer");

		FBPagesPanel pages = (FBPagesPanel) application
				.createComponent(FBPagesPanel.COMPONENT_TYPE);
		pages.setId("pagesPanel");
		pages.setStyleClass("pagesPanel");
		pages.setComponentStyleClass("formPageIcon");
		pages.setGeneralPartStyleClass("pagesGeneralContainer");
		pages.setSpecialPartStyleClass("pagesSpecialContainer");
		pages.setSelectedStyleClass("selectedElement");

		addFacet(WORKSPACE_MENU_FACET, menu);
		addFacet(WORKSPACE_VIEW_FACET, views);
		addFacet(WORKSPACE_PAGES_FACET, pages);
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
		UIComponent menu = getFacet(WORKSPACE_MENU_FACET);
		if (menu != null) {
			renderChild(context, menu);
		}
		UIComponent view = getFacet(WORKSPACE_VIEW_FACET);
		if (view != null) {
			renderChild(context, view);
		}
		UIComponent pages = getFacet(WORKSPACE_PAGES_FACET);
		if (pages != null) {
			 renderChild(context, pages);
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
