package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.Container;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PageThankYou;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.presentation.Layer;
import com.idega.presentation.Script;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBDesignView extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "DesignView";
	
	public static final String DESIGN_VIEW_STATUS_NOFORM = "noform";
	public static final String DESIGN_VIEW_STATUS_EMPTY = "empty";
	public static final String DESIGN_VIEW_STATUS_ACTIVE = "active";
	
	public static final String DESIGN_VIEW_SPECIAL_FACET = "specialFormFacet";
	public static final String DESIGN_VIEW_EMPTY_FACET = "emptyFormFacet";
	public static final String DESIGN_VIEW_HEADER_FACET = "formHeaderFacet";
	public static final String DESIGN_VIEW_PAGE_FACET = "pageTitleFacet";
	
	public static final String BUTTON_AREA_FACET = "BUTTON_AREA_FACET";
	
	private String componentStyleClass;
	private String selectedStyleClass;
	private String status;

	public FBDesignView() {
		super("designView", "dropBox");
		setRendererType(null);
	}
	
	public FBDesignView(String componentClass) {
		super("designView", "dropBox");
		setRendererType(null);
		this.componentStyleClass = componentClass;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		Layer component = new Layer(Layer.DIV);
		component.setId("dropBox");
		component.setStyleClass(getStyleClass());
		
		Layer formHeading = new Layer(Layer.DIV);
		formHeading.setId("formHeading");
		formHeading.setStyleClass("info");
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		
		Text formHeadingHeader = new Text(formDocument.getFormTitle());
		formHeadingHeader.setId("formHeadingHeader");
		formHeading.add(formHeadingHeader);
		
		component.add(formHeading);
		
		Layer pageNotice = new Layer(Layer.DIV);
		pageNotice.setId("pageNotice");
		pageNotice.setStyleClass("label");
		
		Text currentPageTitle = new Text(((FormPage) WFUtil.getBeanInstance("formPage")).getTitle());
		currentPageTitle.setId("currentPageTitle");
		pageNotice.add(currentPageTitle);
		
		component.add(pageNotice);
		
		Layer dropBoxInner = new Layer(Layer.DIV);
		dropBoxInner.setId("dropBoxinner");
		dropBoxInner.setStyleClass(getStyleClass() + "inner");
		
		FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
		Page page = formPage.getPage();
		if(page != null) {
			if(page instanceof PageThankYou || (formDocument.getOverviewPage() != null && page.getId().equals(formDocument.getOverviewPage().getId()))) {
				Layer noFormNotice = new Layer(Layer.DIV);
				noFormNotice.setId("noFormNotice");
				
				Text noFormNoticeHeader = new Text(_("labels_noform_header"));
				noFormNotice.add(noFormNoticeHeader);
				
				Paragraph noFormNoticeBody = new Paragraph();
				noFormNoticeBody.add(_("labels_noform_body"));
				noFormNotice.add(noFormNoticeBody);
				
				component.add(noFormNotice);
			} else {
				List<String> ids = page.getContainedComponentsIdList();
				if(ids.isEmpty()) {
					Layer emptyForm = new Layer(Layer.DIV);
					emptyForm.setId("emptyForm");
					emptyForm.setStyleAttribute("display: none;");
					
					Text emptyFormHeader = new Text(_("labels_empty_form_header"));
					emptyForm.add(emptyFormHeader);
					
					Paragraph emptyFormBody = new Paragraph();
					emptyFormBody.add(_("labels_empty_form_body"));
					emptyForm.add(emptyFormBody);
					
					component.add(emptyForm);
				} else {
					for(Iterator it = ids.iterator(); it.hasNext(); ) {
						String nextId = (String) it.next();
						Component comp = page.getComponent(nextId);
						if(comp instanceof Container) {
							continue;
						} else {
							FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
							formComponent.setId(nextId);
							formComponent.setStyleClass(componentStyleClass);
							formComponent.setOnLoad("loadComponentInfo(this);");
							formComponent.setOnDelete("removeComponent(this);");
							formComponent.setSpeedButtonStyleClass("speedButton");
							dropBoxInner.add(formComponent);
						}
					}
				}
			}
		}
		
		component.add(dropBoxInner);
		
		if(page != null) {
			FBButtonArea area = (FBButtonArea) application.createComponent(FBButtonArea.COMPONENT_TYPE);
			area.setId("pageButtonArea");
			area.setStyleClass(componentStyleClass);
			area.setComponentStyleClass("formButton");
			component.add(area);
		}
		
		Script script = new Script("JavaScript");
		script.addScriptLine("setupComponentDragAndDrop('dropBox','lalala')");
		component.add(script);
		
		add(component);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = componentStyleClass;
		values[2] = status;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		componentStyleClass = (String) values[1];
		status = (String) values[2];
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

	public String getSelectedStyleClass() {
		return selectedStyleClass;
	}

	public void setSelectedStyleClass(String selectedStyleClass) {
		this.selectedStyleClass = selectedStyleClass;
	}
	
}
