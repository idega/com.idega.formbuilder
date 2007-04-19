package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.tabbedpane.HtmlPanelTab;

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
		
//		HtmlPanelTabbedPane tabbedPane = (HtmlPanelTabbedPane) application.createComponent(HtmlPanelTabbedPane.COMPONENT_TYPE);
//		tabbedPane.setTabContentStyleClass("tabContent");
//		tabbedPane.setStyleClass("tabbedPane");
//		tabbedPane.setActiveTabStyleClass("activeTab");
//		tabbedPane.setInactiveTabStyleClass("inactiveTab");
//		tabbedPane.setServerSideTabSwitch(true);
//		tabbedPane.setActiveSubStyleClass("activeSub");
//		tabbedPane.setInactiveSubStyleClass("inactiveSub");
//		tabbedPane.setTabChangeListener(application.createMethodBinding("#{workspace.processTabChange}", new Class[] {TabChangeEvent.class}));
		
		HtmlPanelTab tab1 = (HtmlPanelTab) application.createComponent(HtmlPanelTab.COMPONENT_TYPE);
		tab1.setLabel("Design");
		tab1.setId("designViewTab");
		
		HtmlPanelTab tab2 = (HtmlPanelTab) application.createComponent(HtmlPanelTab.COMPONENT_TYPE);
		tab2.setLabel("Preview");
		tab2.setId("previewViewTab");
		
		HtmlPanelTab tab3 = (HtmlPanelTab) application.createComponent(HtmlPanelTab.COMPONENT_TYPE);
		tab3.setLabel("Source");
		tab3.setId("sourceViewTab");
		
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
		
//		tab1.getChildren().add(designView);
//		tab2.getChildren().add(previewView);
//		tab3.getChildren().add(sourceView);

//		tabbedPane.getChildren().add(tab1);
//		tabbedPane.getChildren().add(tab2);
//		tabbedPane.getChildren().add(tab3);
		
		tabbedPane.addTab("tab1", "Design", designView, false);
		tabbedPane.addTab("tab2", "Preview", previewView, false);
		tabbedPane.addTab("tab3", "Source", sourceView, false);
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
//		HtmlPanelTabbedPane viewSwitch = (HtmlPanelTabbedPane) getFacet(SWITCHER_FACET);
//		if(viewSwitch != null) {
//			if(view.equals(DESIGN_VIEW)) {
//				viewSwitch.setSelectedIndex(DESIGN_VIEW_INDEX);
//			} else if(view.equals(PREVIEW_VIEW)) {
//				viewSwitch.setSelectedIndex(PREVIEW_VIEW_INDEX);
//			} else if(view.equals(SOURCE_VIEW)) {
//				viewSwitch.setSelectedIndex(SOURCE_VIEW_INDEX);
//			}
//			renderChild(context, viewSwitch);
//		}
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
