package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.Container;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
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
	
	public static final String DEFAULT_DESIGN_VIEW_CLASS = "designView";
	public static final String DEFAULT_DROPBOX_CLASS = "dropBox";
	
	public static final String BUTTON_AREA_FACET = "BUTTON_AREA_FACET";
	
	private String componentStyleClass;
	private String selectedStyleClass;
	private String status;

	public FBDesignView() {
		super(DEFAULT_DESIGN_VIEW_CLASS, DEFAULT_DROPBOX_CLASS);
		setRendererType(null);
	}
	
	public FBDesignView(String componentClass) {
		super(DEFAULT_DESIGN_VIEW_CLASS, DEFAULT_DROPBOX_CLASS);
		setRendererType(null);
		this.componentStyleClass = componentClass;
	}
	
	private boolean hasComponents(List<String> ids, Page page) {
		if(ids == null || page == null)
			return false;
		
		for(Iterator<String> it = ids.iterator(); it.hasNext(); ) {
			Component component = page.getComponent(it.next());
			if(component instanceof ButtonArea) {
				continue;
			} else {
				return true;
			}
		}
		return false;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer component = new Layer(Layer.DIV);
		component.setId(DEFAULT_DROPBOX_CLASS);
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
				
				Text noFormNoticeHeader = new Text(getLocalizedString(iwc, "labels_noform_header", "This is a special page"));
				noFormNotice.add(noFormNoticeHeader);
				
				Paragraph noFormNoticeBody = new Paragraph();
				noFormNoticeBody.add(getLocalizedString(iwc, "labels_noform_body", ""));
				noFormNotice.add(noFormNoticeBody);
				
				component.add(noFormNotice);
			} else {
				List<String> ids = page.getContainedComponentsIdList();
				if(!hasComponents(ids, page)) {
					Layer emptyForm = new Layer(Layer.DIV);
					emptyForm.setId("emptyForm");
					
					Text emptyFormHeader = new Text(getLocalizedString(iwc, "labels_empty_form_header", "This page is empty right now"));
					emptyForm.add(emptyFormHeader);
					
					Paragraph emptyFormBody = new Paragraph();
					emptyFormBody.add(getLocalizedString(iwc, "labels_empty_form_body", "You can add components by draggin them from the palette"));
					emptyForm.add(emptyFormBody);
					
					component.add(emptyForm);
				} else {
					for(Iterator<String> it = ids.iterator(); it.hasNext(); ) {
						String nextId = it.next();
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
		
		add(component);
	}
	
	@SuppressWarnings("unchecked")
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
