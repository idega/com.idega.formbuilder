package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxSupport;
import org.ajax4jsf.ajax.html.HtmlAjaxCommandLink;
import org.ajax4jsf.ajax.html.HtmlAjaxStatus;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class FBViewPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ViewPanel";
	
	private static final String SWITCHER_FACET = "SWITCHER_FACET";
	private static final String STATUS_FACET = "STATUS_FACET";
	private static final String SHOW_PAGES_BUTTON = "SHOW_PAGES_BUTTON";
	private static final String PAGES_PANEL = "PAGES_PANEL";
	
	private static final String DESIGN_VIEW = "design";
	private static final String SOURCE_VIEW = "source";
	private static final String PREVIEW_VIEW = "preview";
	
	private static final String SHOW_PAGES_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-redo.png";
//	private static final String HIDE_PAGES_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-undo.png";
	
	private String id;
	private String styleClass;
	private String view;

	public FBViewPanel() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		FBDivision switcher = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		switcher.setId("switcher");
		switcher.setStyleClass("viewTabs");
		
		HtmlAjaxCommandLink view1 = (HtmlAjaxCommandLink) application.createComponent(HtmlAjaxCommandLink.COMPONENT_TYPE);
		view1.setOnclick("showLoadingMessage('Switching')");
		view1.setOncomplete("closeLoadingMessage()");
		view1.setId("designViewTab");
		view1.setActionListener(application.createMethodBinding("#{viewChangeAction.processAction}", new Class[]{ActionEvent.class}));
		view1.setAjaxSingle(true);
		view1.setReRender("mainApplication");
		view1.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['view_design']}"));
		
		FBDivision view1Box = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		view1Box.setId("view1Box");
		view1Box.setStyleClass("unselectedTab");
		view1Box.getChildren().add(view1);
		
		switcher.getChildren().add(view1Box);
		
		HtmlAjaxCommandLink view2 = (HtmlAjaxCommandLink) application.createComponent(HtmlAjaxCommandLink.COMPONENT_TYPE);
		view2.setOnclick("showLoadingMessage('Switching')");
		view2.setOncomplete("closeLoadingMessage()");
		view2.setId("previewViewTab");
		view2.setActionListener(application.createMethodBinding("#{viewChangeAction.processAction}", new Class[]{ActionEvent.class}));
		view2.setAjaxSingle(true);
		view2.setReRender("mainApplication");
		view2.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['view_preview']}"));
		
		FBDivision view2Box = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		view2Box.setId("view2Box");
		view2Box.setStyleClass("unselectedTab");
		view2Box.getChildren().add(view2);
		
		switcher.getChildren().add(view2Box);
		
		HtmlAjaxCommandLink view3 = (HtmlAjaxCommandLink) application.createComponent(HtmlAjaxCommandLink.COMPONENT_TYPE);
		view3.setOnclick("showLoadingMessage('Switching')");
		view3.setOncomplete("closeLoadingMessage()");
		view3.setId("sourceViewTab");
		view3.setActionListener(application.createMethodBinding("#{viewChangeAction.processAction}", new Class[]{ActionEvent.class}));
		view3.setAjaxSingle(true);
		view3.setReRender("mainApplication");
		view3.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['view_source']}"));
		
		FBDivision view3Box = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		view3Box.setId("view3Box");
		view3Box.setStyleClass("unselectedTab");
		view3Box.getChildren().add(view3);
		
		switcher.getChildren().add(view3Box);
		
		FBDesignView designView = (FBDesignView) application.createComponent(FBDesignView.COMPONENT_TYPE);
		designView.setId("dropBox");
		designView.setStyleClass("dropBox");
		designView.setComponentStyleClass("formElement");
		designView.setValueBinding("status", application.createValueBinding("#{workspace.designViewStatus}"));
		addFacet(DESIGN_VIEW, designView);
		
		FBSourceView sourceView = (FBSourceView) application.createComponent(FBSourceView.COMPONENT_TYPE);
		sourceView.setStyleClass("sourceView");
		sourceView.setId("sourceView");
		addFacet(SOURCE_VIEW, sourceView);
		
		FBFormPreview previewView = (FBFormPreview) application.createComponent(FBFormPreview.COMPONENT_TYPE);
		previewView.setId("previewView");
		addFacet(PREVIEW_VIEW, previewView);
		
		addFacet(SWITCHER_FACET, switcher);
		
		HtmlAjaxStatus status = new HtmlAjaxStatus();
		status.setStartText("Working");
		status.setStartStyle("background-color: Red; font-size: 16px; font-weight: Bold; float: right;");
		status.setLayout("inline");
		
		addFacet(STATUS_FACET, status);
		
		
		HtmlGraphicImage showPagesButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		showPagesButton.setId("showPagesButton");
		showPagesButton.setStyle("display: inline;");
		showPagesButton.setValue(SHOW_PAGES_BUTTON_IMG);
		
		UIAjaxSupport showPagesButtonS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		showPagesButtonS.setEvent("onclick");
		showPagesButtonS.setAjaxSingle(true);
		showPagesButtonS.setReRender("mainApplication");
		showPagesButtonS.setActionListener(application.createMethodBinding("#{workspace.togglePagesPanel}", new Class[]{ActionEvent.class}));
		showPagesButton.getChildren().add(showPagesButtonS);
		
		addFacet(SHOW_PAGES_BUTTON, showPagesButton);
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		

		ValueBinding vb = this.getValueBinding("view");
		String view = null;
		if(vb != null) {
			view = (String) vb.getValue(context);
		} else {
			view = getView();
		}
		UIComponent status = getFacet(STATUS_FACET);
		UIComponent viewSwitch = getFacet(SWITCHER_FACET);
		UIComponent pagesButton = getFacet(SHOW_PAGES_BUTTON);
		if(viewSwitch != null) {
			if(view.equals(DESIGN_VIEW)) {
				UIComponent designView = getFacet(view);
				if(designView != null) {
					((FBDivision) viewSwitch.getChildren().get(0)).setStyleClass("selectedTab");
					((FBDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
					((FBDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
//					renderChild(context, pagesButton);
					if(((Workspace)WFUtil.getBeanInstance("workspace")).isPagesPanelVisible()) {
						UIComponent pagesPanel = getFacet(PAGES_PANEL);
						if(pagesPanel != null) {
							renderChild(context, pagesPanel);
						}
					}
//					renderChild(context, status);
					renderChild(context, designView);
				}
			} else if(view.equals(PREVIEW_VIEW)) {
				UIComponent previewView = getFacet(view);
				if (previewView != null) {
					((FBDivision) viewSwitch.getChildren().get(1)).setStyleClass("selectedTab");
					((FBDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
					((FBDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
//					renderChild(context, pagesButton);
//					renderChild(context, status);
					renderChild(context, previewView);
				}
			} else if(view.equals(SOURCE_VIEW)) {
				UIComponent sourceView = getFacet(view);
				if (sourceView != null) {
					((FBDivision) viewSwitch.getChildren().get(2)).setStyleClass("selectedTab");
					((FBDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
					((FBDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
//					renderChild(context, pagesButton);
//					renderChild(context, status);
					renderChild(context, sourceView);
				}
			}
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
		
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
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

}
