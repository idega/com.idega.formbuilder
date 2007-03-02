package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.html.HtmlAjaxCommandLink;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFDivision;
import com.idega.webface.WFUtil;

public class FBViewPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ViewPanel";
	
	private static final String SWITCHER_FACET = "SWITCHER_FACET";
	
	private static final String DESIGN_VIEW = "design";
	private static final String SOURCE_VIEW = "source";
	private static final String PREVIEW_VIEW = "preview";
	
	private String view;

	public FBViewPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision switcher = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		switcher.setId("switcher");
		switcher.setStyleClass("viewTabs");
		
		HtmlAjaxCommandLink view1 = (HtmlAjaxCommandLink) application.createComponent(HtmlAjaxCommandLink.COMPONENT_TYPE);
		view1.setOnclick("showLoadingMessage('Switching')");
		view1.setOncomplete("closeLoadingMessage()");
		view1.setId("designViewTab");
		view1.setActionListener(application.createMethodBinding("#{workspace.changeView}", new Class[]{ActionEvent.class}));
		view1.setAjaxSingle(true);
		view1.setReRender("ajaxViewPanel");
		view1.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['view_design']}"));
		
		WFDivision view1Box = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		view1Box.setId("view1Box");
		view1Box.setStyleClass("unselectedTab");
		addChild(view1, view1Box);
		addChild(view1Box, switcher);
		
		HtmlAjaxCommandLink view2 = (HtmlAjaxCommandLink) application.createComponent(HtmlAjaxCommandLink.COMPONENT_TYPE);
		view2.setOnclick("showLoadingMessage('Switching')");
		view2.setOncomplete("closeLoadingMessage(); window.location.replace(unescape(window.location.pathname+"+
				"'formbuilder'" // tmp hardcode while alex fixes url prob
				+"));");
		view2.setId("previewViewTab");
		view2.setActionListener(application.createMethodBinding("#{workspace.changeView}", new Class[]{ActionEvent.class}));
		view2.setAjaxSingle(true);
		view2.setReRender("ajaxViewPanel");
		view2.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['view_preview']}"));
		
		WFDivision view2Box = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		view2Box.setId("view2Box");
		view2Box.setStyleClass("unselectedTab");
		addChild(view2, view2Box);
		addChild(view2Box, switcher);
		
		HtmlAjaxCommandLink view3 = (HtmlAjaxCommandLink) application.createComponent(HtmlAjaxCommandLink.COMPONENT_TYPE);
		view3.setOnclick("showLoadingMessage('Switching')");
		view3.setOncomplete("closeLoadingMessage()");
		view3.setId("sourceViewTab");
		view3.setActionListener(application.createMethodBinding("#{workspace.changeView}", new Class[]{ActionEvent.class}));
		view3.setAjaxSingle(true);
		view3.setReRender("ajaxViewPanel");
		view3.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['view_source']}"));
		
		WFDivision view3Box = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		view3Box.setId("view3Box");
		view3Box.setStyleClass("unselectedTab");
		addChild(view3, view3Box);
		addChild(view3Box, switcher);
		
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
		

		ValueBinding vb = this.getValueBinding("view");
		String view = null;
		if(vb != null) {
			view = (String) vb.getValue(context);
		} else {
			view = getView();
		}
		UIComponent viewSwitch = getFacet(SWITCHER_FACET);
		if(viewSwitch != null) {
			if(view.equals(DESIGN_VIEW)) {
				UIComponent designView = getFacet(view);
				if(designView != null) {
					((WFDivision) viewSwitch.getChildren().get(0)).setStyleClass("selectedTab");
					((WFDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
					((WFDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
					renderChild(context, designView);
				}
			} else if(view.equals(PREVIEW_VIEW)) {
				UIComponent previewView = getFacet(view);
				if (previewView != null) {
					((WFDivision) viewSwitch.getChildren().get(1)).setStyleClass("selectedTab");
					((WFDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
					((WFDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
					renderChild(context, previewView);
				}
			} else if(view.equals(SOURCE_VIEW)) {
				UIComponent sourceView = getFacet(view);
				if (sourceView != null) {
					((WFDivision) viewSwitch.getChildren().get(2)).setStyleClass("selectedTab");
					((WFDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
					((WFDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
					renderChild(context, sourceView);
				}
			}
		}
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
