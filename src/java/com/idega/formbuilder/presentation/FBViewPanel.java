package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxCommandLink;

import com.idega.presentation.IWBaseComponent;

public class FBViewPanel extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "ViewPanel";
	
	private static final String SWITCHER_FACET = "SWITCHER_FACET";
	
	private static final String DESIGN_VIEW = "design";
	private static final String SOURCE_VIEW = "source";
	private static final String PREVIEW_VIEW = "preview";
	
	public static final String CONTENT_DIV_FACET = "body";
	
	private String styleClass;
	private String view;

	public FBViewPanel() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBViewPanel.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		FBDivision switcher = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		switcher.setId("switcher");
		switcher.setStyleClass("viewTabs");
		
		UIAjaxCommandLink view1 = (UIAjaxCommandLink) application.createComponent(UIAjaxCommandLink.COMPONENT_TYPE);
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
		
		UIAjaxCommandLink view2 = (UIAjaxCommandLink) application.createComponent(UIAjaxCommandLink.COMPONENT_TYPE);
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
		
		UIAjaxCommandLink view3 = (UIAjaxCommandLink) application.createComponent(UIAjaxCommandLink.COMPONENT_TYPE);
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
		this.getFacets().put(DESIGN_VIEW, designView);
		
		FBSourceView sourceView = (FBSourceView) application.createComponent(FBSourceView.COMPONENT_TYPE);
		sourceView.setStyleClass("dropBox");
		sourceView.setId("sourceView");
		this.getFacets().put(SOURCE_VIEW, sourceView);
		
		FBFormPreview previewView = (FBFormPreview) application.createComponent(FBFormPreview.COMPONENT_TYPE);
		this.getFacets().put(PREVIEW_VIEW, previewView);
		
		this.getFacets().put(SWITCHER_FACET, switcher);
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
		
		UIComponent viewSwitch = (UIComponent) getFacet(SWITCHER_FACET);
		if(viewSwitch != null) {
//			FBDivision = viewSwitch.get
//			renderChild(context, viewSwitch);
			if(view.equals(DESIGN_VIEW)) {
				UIComponent designView = getFacet(view);
				if(designView != null) {
					((FBDivision) viewSwitch.getChildren().get(0)).setStyleClass("selectedTab");
					((FBDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
					((FBDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
					renderChild(context, designView);
				}
			} else if(view.equals(PREVIEW_VIEW)) {
				UIComponent previewView = getFacet(view);
				if (previewView != null) {
					((FBDivision) viewSwitch.getChildren().get(1)).setStyleClass("selectedTab");
					((FBDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
					((FBDivision) viewSwitch.getChildren().get(2)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
					renderChild(context, previewView);
				}
			} else if(view.equals(SOURCE_VIEW)) {
				UIComponent sourceView = getFacet(view);
				if (sourceView != null) {
					((FBDivision) viewSwitch.getChildren().get(2)).setStyleClass("selectedTab");
					((FBDivision) viewSwitch.getChildren().get(1)).setStyleClass("unselectedTab");
					((FBDivision) viewSwitch.getChildren().get(0)).setStyleClass("unselectedTab");
					renderChild(context, viewSwitch);
					renderChild(context, sourceView);
				}
			}
		}
		
//		if(view != null) {
//			if(view.equals(DESIGN_VIEW)) {
//				UIComponent designView = getFacet(view);
//				if(designView != null) {
////					renderChild(context, designView);
//				}
//			} else if(view.equals(PREVIEW_VIEW)) {
//				UIComponent previewView = getFacet(view);
//				if (previewView != null) {
////					renderChild(context, previewView);
//				}
//			} else if(view.equals(SOURCE_VIEW)) {
//				UIComponent sourceView = getFacet(view);
//				if (sourceView != null) {
////					renderChild(context, sourceView);
//				}
//			}
//		}
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
		
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!this.isRendered()) {
			return;
		}
		super.encodeChildren(context);
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context); 
		values[1] = styleClass;
		values[2] = view;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		view = (String) values[2];
	}

}
