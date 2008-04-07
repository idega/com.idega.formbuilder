package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import com.idega.core.localisation.business.ICLocaleBusiness;
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
import com.idega.presentation.ui.Label;
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
	
	private static final String FORM_HEADER_ID = "formHeading";
	private static final String INFO_CLASS = "info";
	private static final String INLINE_EDIT_CLASS = "inlineEdit";
	private static final String REL_ATTRIBUTE = "rel";
	private static final String FORM_TITLE_REL = "FormDocument.setFormTitle";
	private static final String FORM_HEADING_HEADER_ID = "formHeadingHeader";
	private static final String MESSAGE_DIALOG_ID = "messageDialog";
	private static final String DISPLAY_NONE = "display: none;";
	private static final String MESSAGE_BOX_CONTENT_CLASS = "messageBoxContent";
	private static final String LANGUAGE_CHOOSER_ID = "languageChooser";
	private static final String DESIGN_VIEW_PAGE_TITLE_ID = "designViewPageTitle";
	private static final String LABEL_CLASS = "label";
	private static final String PAGE_TITLE_REL = "FormPage.saveTitle updatePageIconText";
	private static final String DESIGN_VIEW_CURRENT_PAGE_TITLE_ID = "designViewCurrentPageTitle";
	private static final String DROPBOX_INNER_ID = "dropBoxinner";
	private static final String INNER_POSTFIX = "inner";
	private static final String NOFORM_NOTICE_ID = "noFormNotice";
	private static final String THANK_YOU_TEXT_REL = "FormDocument.setThankYouText";
	private static final String DESIGN_VIEW_THANK_YOU_ID = "designViewThankYou";
	private static final String EMPTY_FORM_ID = "emptyForm";
	private static final String LOAD_COMPONENT_ACTION = "loadComponentInfo(this);";
	private static final String REMOVE_COMPONENT_ACTION = "removeComponent(this);";
	private static final String SPEED_BUTTON_CLASS = "speedButton";
	private static final String FORM_BUTTON_CLASS = "formButton";
	
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
		formHeading.setId(FORM_HEADER_ID);
		formHeading.setStyleClass(INFO_CLASS);
		formHeading.setStyleClass(INLINE_EDIT_CLASS);
		formHeading.setMarkupAttribute(REL_ATTRIBUTE, FORM_TITLE_REL);
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		Text formHeadingHeader = new Text(formDocument.getFormTitle());
		formHeadingHeader.setId(FORM_HEADING_HEADER_ID);
		formHeading.add(formHeadingHeader);
		
		Layer messageBox = new Layer(Layer.DIV);
		messageBox.setId(MESSAGE_DIALOG_ID);
		messageBox.setStyleAttribute(DISPLAY_NONE);
		
		Text messageBoxContent = new Text();
		messageBoxContent.setStyleClass(MESSAGE_BOX_CONTENT_CLASS);
		messageBoxContent.setText(getLocalizedString(iwc, "fb_no_content", "No content"));
		
		messageBox.add(messageBoxContent);
//		formHeading.add(messageBox);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		Locale locale = workspace.getLocale();
		
		Layer languageChooserLayer = new Layer(Layer.DIV);
		languageChooserLayer.setId(LANGUAGE_CHOOSER_ID);
		
		DropdownMenu languageChooser = ICLocaleBusiness.getAvailableLocalesDropdownStringKeyed(iwc.getIWMainApplication(), LANGUAGE_CHOOSER_ID, false);
		languageChooser.setSelectedElement(locale.getLanguage());
		
		languageChooserLayer.add(new Label(getLocalizedString(iwc, "fb_choose_language", "Form language"), languageChooser));
		languageChooserLayer.add(languageChooser);
		
		component.add(formHeading);
		component.add(languageChooserLayer);
		
		Layer pageNotice = new Layer(Layer.DIV);
		pageNotice.setId(DESIGN_VIEW_PAGE_TITLE_ID);
		pageNotice.setStyleClass(INLINE_EDIT_CLASS);
		pageNotice.setStyleClass(LABEL_CLASS);
		pageNotice.setMarkupAttribute(REL_ATTRIBUTE, PAGE_TITLE_REL);
		
		FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
		
		Text currentPageTitle = new Text(formPage.getTitle());
		currentPageTitle.setId(DESIGN_VIEW_CURRENT_PAGE_TITLE_ID);
		pageNotice.add(currentPageTitle);
		
		component.add(pageNotice);
		
		Layer dropBoxInner = new Layer(Layer.DIV);
		dropBoxInner.setId(DROPBOX_INNER_ID);
		dropBoxInner.setStyleClass(getStyleClass() + INNER_POSTFIX);
		
		Page page = formPage.getPage();
		if(page != null) {
			if(page instanceof PageThankYou) {
//				Layer thankYouTextBox = new Layer(Layer.DIV);
//				thankYouTextBox.setId("noFormNotice2");
//				
//				Text headline = new Text(getLocalizedString(iwc, "labels_confirmation_page", "This is the confirmation page of the form"));
//				headline.setId("designViewThankYou2");
//				thankYouTextBox.add(headline);
//				
//				Paragraph emptyFormBody = new Paragraph();
//				emptyFormBody.add(getLocalizedString(iwc, "labels_confirmation_page_body", "You can only add buttons to this page"));
//				thankYouTextBox.add(emptyFormBody);
				
				Layer noFormNotice = new Layer(Layer.DIV);
				noFormNotice.setId(NOFORM_NOTICE_ID);
				noFormNotice.setStyleClass(INLINE_EDIT_CLASS);
				noFormNotice.setMarkupAttribute(REL_ATTRIBUTE, THANK_YOU_TEXT_REL);
				
				Text thankYouText = new Text(formDocument.getThankYouText());
				thankYouText.setId(DESIGN_VIEW_THANK_YOU_ID);
				noFormNotice.add(thankYouText);
				
//				component.add(thankYouTextBox);
				component.add(noFormNotice);
			} else if (formDocument.getOverviewPage() != null && page.getId().equals(formDocument.getOverviewPage().getId())) {
				Layer thankYouTextBox = new Layer(Layer.DIV);
				thankYouTextBox.setId(NOFORM_NOTICE_ID);
				
				Text thankYouText = new Text(getLocalizedString(iwc, "labels_confirmation_page", "This is the confirmation page of the form"));
				thankYouText.setId(DESIGN_VIEW_THANK_YOU_ID);
				thankYouTextBox.add(thankYouText);
				
				Paragraph emptyFormBody = new Paragraph();
				emptyFormBody.add(getLocalizedString(iwc, "labels_confirmation_page_body", "You can only add buttons to this page"));
				thankYouTextBox.add(emptyFormBody);
				
				component.add(thankYouTextBox);
			} else {
				List<String> ids = page.getContainedComponentsIdList();
				if(!hasComponents(ids, page)) {
					Layer emptyForm = new Layer(Layer.DIV);
					emptyForm.setId(EMPTY_FORM_ID);
					
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
							formComponent.setOnLoad(LOAD_COMPONENT_ACTION);
							formComponent.setOnDelete(REMOVE_COMPONENT_ACTION);
							formComponent.setSpeedButtonStyleClass(SPEED_BUTTON_CLASS);
							dropBoxInner.add(formComponent);
						}
					}
				}
			}
		}
		
		component.add(dropBoxInner);
		
		if(page != null) {
			FBButtonArea area = (FBButtonArea) application.createComponent(FBButtonArea.COMPONENT_TYPE);
			area.setStyleClass(componentStyleClass);
			area.setComponentStyleClass(FORM_BUTTON_CLASS);
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
