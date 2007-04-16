package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.tabbedpane.HtmlPanelTab;
import org.apache.myfaces.custom.tabbedpane.HtmlPanelTabbedPane;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class FBViewPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ViewPanel";
	
	private static final String SWITCHER_FACET = "SWITCHER_FACET";
	
//	private static final String DESIGN_VIEW = "design";
//	private static final String SOURCE_VIEW = "source";
//	private static final String PREVIEW_VIEW = "preview";
	
	private String view;

	public FBViewPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		HtmlPanelTabbedPane tabbedPane = (HtmlPanelTabbedPane) application.createComponent(HtmlPanelTabbedPane.COMPONENT_TYPE);
		tabbedPane.setTabContentStyleClass("tabContent");
		tabbedPane.setStyleClass("tabbedPane");
		tabbedPane.setActiveTabStyleClass("activeTab");
		tabbedPane.setInactiveTabStyleClass("inactiveTab");
		tabbedPane.setServerSideTabSwitch(false);
		
		HtmlPanelTab tab1 = (HtmlPanelTab) application.createComponent(HtmlPanelTab.COMPONENT_TYPE);
		tab1.setLabel("Design");
		
		HtmlPanelTab tab2 = (HtmlPanelTab) application.createComponent(HtmlPanelTab.COMPONENT_TYPE);
		tab2.setLabel("Preview");
		
		HtmlPanelTab tab3 = (HtmlPanelTab) application.createComponent(HtmlPanelTab.COMPONENT_TYPE);
		tab3.setLabel("Source");
		
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
		
		tab1.getChildren().add(designView);
		tab2.getChildren().add(previewView);
		tab3.getChildren().add(sourceView);

		tabbedPane.getChildren().add(tab1);
		tabbedPane.getChildren().add(tab2);
		tabbedPane.getChildren().add(tab3);
		
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
		
		HtmlPanelTabbedPane component = (HtmlPanelTabbedPane) getFacet(SWITCHER_FACET);
		if(component != null) {
			renderChild(context, component);
		}

//		ValueBinding vb = this.getValueBinding("view");
//		String view = null;
//		if(vb != null) {
//			view = (String) vb.getValue(context);
//		} else {
//			view = getView();
//		}
//		UIComponent viewSwitch = getFacet(SWITCHER_FACET);
//		if(viewSwitch != null) {
//			if(view.equals(DESIGN_VIEW)) {
//				UIComponent designView = getFacet(view);
//				if(designView != null) {
//					((WFDivision) viewSwitch.getChildren().get(0)).setStyleClass("selectedTab");
//					((WFDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
//					((WFDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
//					renderChild(context, viewSwitch);
//					renderChild(context, designView);
//				}
//			} else if(view.equals(PREVIEW_VIEW)) {
//				UIComponent previewView = getFacet(view);
//				if (previewView != null) {
//					((WFDivision) viewSwitch.getChildren().get(1)).setStyleClass("selectedTab");
//					((WFDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
//					((WFDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
//					renderChild(context, viewSwitch);
//					renderChild(context, previewView);
//				}
//			} else if(view.equals(SOURCE_VIEW)) {
//				UIComponent sourceView = getFacet(view);
//				if (sourceView != null) {
//					((WFDivision) viewSwitch.getChildren().get(2)).setStyleClass("selectedTab");
//					((WFDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
//					((WFDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
//					renderChild(context, viewSwitch);
//					renderChild(context, sourceView);
//				}
//			}
//		}
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
