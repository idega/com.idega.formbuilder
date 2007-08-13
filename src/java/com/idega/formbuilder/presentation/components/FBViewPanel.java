package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFTabbedPane;
import com.idega.webface.WFUtil;

public class FBViewPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ViewPanel";
	
	private static final String SWITCHER_FACET = "SWITCHER_FACET";
	
	private String view;

	public static final int DESIGN_VIEW_INDEX = 0;
	public static final int PREVIEW_VIEW_INDEX = 1;
	public static final int SOURCE_VIEW_INDEX = 2;
	public static final String PREVIEW_VIEW = "preview";
	public static final String SOURCE_VIEW = "source";
	public static final String DESIGN_VIEW = "design";

	public FBViewPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFTabbedPane tabbedPane = new WFTabbedPane();
		tabbedPane.setMenuStyleClass("fbViewTabBar");
		tabbedPane.setSelectedMenuItemStyleClass("activeTab");
		tabbedPane.setDeselectedMenuItemStyleClass("inactiveTab");
		
		FBDesignView designView = (FBDesignView) application.createComponent(FBDesignView.COMPONENT_TYPE);
		designView.setId("dropBox");
		designView.setStyleClass("dropBox");
		designView.setComponentStyleClass("formElement");
		designView.setSelectedStyleClass("formElement selectedElement");
		designView.setValueBinding("status", application.createValueBinding("#{workspace.designViewStatus}"));
		
		FBSourceView sourceView = (FBSourceView) application.createComponent(FBSourceView.COMPONENT_TYPE);
		sourceView.setStyleClass("sourceView");
		sourceView.setId("sourceView");
		
		FBFormPreview previewView = (FBFormPreview) application.createComponent(FBFormPreview.COMPONENT_TYPE);
		previewView.setId("previewView");
		
		tabbedPane.addTab("tab1", _("view_design"), designView);
		tabbedPane.addTab("tab2", _("view_preview"), previewView);
		tabbedPane.addTab("tab3", _("view_source"), sourceView);
		tabbedPane.setSelectedMenuItemId("tab1");
		
		addFacet(SWITCHER_FACET, tabbedPane);
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		((Workspace)WFUtil.getBeanInstance("workspace")).isPagesPanelVisible();
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");

		ValueBinding vb = getValueBinding("view");
		if(vb != null) {
			view = (String) vb.getValue(context);
		}
		renderChild(context, getFacet(SWITCHER_FACET));
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
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

}
