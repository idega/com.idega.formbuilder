package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.webface.WFUtil;

public class FBWorkspace extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Workspace";

	private static final String WORKSPACE_MENU_FACET = "menu";

	private static final String WORKSPACE_VIEW_FACET = "view";

	private static final String WORKSPACE_PAGES_FACET = "pages";

	private String view;

	public FBWorkspace() {
		super();
		setRendererType(null);
	}

	protected void initializeComponent(FacesContext context) {

		if (context.getExternalContext().getRequestParameterMap().containsKey(FormDocument.FROM_APP_REQ_PARAM)) {
			FormDocument fd = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
			try {
				Map session_map = context.getExternalContext().getSessionMap();
				fd.setAppId((String)session_map.get(FormDocument.APP_ID_PARAM));
				fd.setPrimaryFormName((String)session_map.get(FormDocument.APP_FORM_NAME_PARAM));
				session_map.remove(FormDocument.APP_ID_PARAM);
				session_map.remove(FormDocument.APP_FORM_NAME_PARAM);
			} catch (Exception e) {
				// TODO: use logger and redirect back to applications list if
				// possible
				e.printStackTrace();
			}
		}

		Application application = context.getApplication();
		getChildren().clear();

		ValueBinding vb = getValueBinding("view");
		if (vb != null) {
			view = (String) vb.getValue(context);
		}

		FBMenu menu = (FBMenu) application.createComponent(FBMenu.COMPONENT_TYPE);
		menu.setId("optionsPanel");
		menu.setStyleClass("optionsContainer");
		menu.setValueBinding("selectedMenu", application.createValueBinding("#{workspace.selectedMenu}"));
		menu.setValueBinding("show", application.createValueBinding("#{workspace.renderedMenu}"));

		FBViewPanel views = (FBViewPanel) application.createComponent(FBViewPanel.COMPONENT_TYPE);
		views.setValueBinding("view", vb);
		views.setId("viewPanel");
		views.setStyleClass("formContainer");

		FBPagesPanel pages = (FBPagesPanel) application.createComponent(FBPagesPanel.COMPONENT_TYPE);
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
		
//		AddResource resourceAdder = AddResourceFactory.getInstance(context);
//		resourceAdder.addStyleSheet(context, AddResource.HEADER_BEGIN, FormbuilderViewManager.FORMBUILDER_CSS);

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
