package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.Container;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.CoreUtil;
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

	public FBDesignView() {
		super(DEFAULT_DESIGN_VIEW_CLASS, DEFAULT_DROPBOX_CLASS);
	}
	
	public FBDesignView(String componentClass) {
		super(DEFAULT_DESIGN_VIEW_CLASS, DEFAULT_DROPBOX_CLASS);
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
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer component = new Layer(Layer.DIV);
		component.setId(DEFAULT_DROPBOX_CLASS);
		component.setStyleClass(getStyleClass());
		
		Layer formHeading = new Layer(Layer.DIV);
		formHeading.setId("formHeading");
		formHeading.setStyleClass("info");
		formHeading.setStyleClass("inlineEdit");
		formHeading.setMarkupAttribute("rel", "FormDocument.setFormTitle");
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		Text formHeadingHeader = new Text(formDocument.getFormTitle());
		formHeadingHeader.setId("formHeadingHeader");
		formHeading.add(formHeadingHeader);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		Locale locale = workspace.getLocale();
		
		DropdownMenu languageChooser = new DropdownMenu();
		languageChooser.setId("languageChooser");
		languageChooser.addMenuElementFirst("", "-Languages-");
		languageChooser.addMenuElement("en", "English");
		languageChooser.addMenuElement("is", "Icelandic");
		languageChooser.setSelectedElement(locale.getLanguage());
		
		component.add(formHeading);
		component.add(languageChooser);
		
		Layer pageNotice = new Layer(Layer.DIV);
		pageNotice.setId("designViewPageTitle");
		pageNotice.setStyleClass("inlineEdit");
		pageNotice.setStyleClass("label");
		pageNotice.setMarkupAttribute("rel", "FormPage.setTitle");
		
		FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
		
		Text currentPageTitle = new Text(formPage.getTitle());
		currentPageTitle.setId("designViewCurrentPageTitle");
		pageNotice.add(currentPageTitle);
		
		component.add(pageNotice);
		
		Layer dropBoxInner = new Layer(Layer.DIV);
		dropBoxInner.setId("dropBoxinner");
		dropBoxInner.setStyleClass(getStyleClass() + "inner");
		
		Page page = formPage.getPage();
		if(page != null) {
			if(page instanceof PageThankYou) {
				Layer noFormNotice = new Layer(Layer.DIV);
				noFormNotice.setId("noFormNotice");
				noFormNotice.setStyleClass("inlineTextarea");
				noFormNotice.setMarkupAttribute("rel", "FormDocument.setThankYouText");
				
				Text thankYouText = new Text(formDocument.getThankYouText());
				thankYouText.setId("designViewThankYou");
				noFormNotice.add(thankYouText);
				
				component.add(noFormNotice);
			} else if (formDocument.getOverviewPage() != null && page.getId().equals(formDocument.getOverviewPage().getId())) {
//				Layer thankYouTextBox = new Layer(Layer.DIV);
//				thankYouTextBox.setId("designViewThankYouBox");
//				thankYouTextBox.setStyleClass("inlineEdit");
//				thankYouTextBox.setStyleClass("biglabel");
//				thankYouTextBox.setMarkupAttribute("rel", "FormDocument.setThankYouText");
//				
//				Text thankYouText = new Text(formDocument.getThankYouText());
//				thankYouText.setId("designViewThankYou");
//				thankYouTextBox.add(thankYouText);
//				
//				component.add(thankYouTextBox);
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
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = componentStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		componentStyleClass = (String) values[1];
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
