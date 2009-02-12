package com.idega.formbuilder.presentation.components;

import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.idega.block.form.data.XForm;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FBHomePageBean;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Label;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.Container;
import com.idega.xformsmanager.business.component.Page;

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
	private static final String FORM_TITLE_REL = "FormDocument.saveFormTitle";
	private static final String FORM_ERROR_MESSAGE_REL = "FormDocument.saveFormErrorMessage";
	private static final String FORM_HEADING_HEADER_ID = "formHeadingHeader";
	private static final String MESSAGE_DIALOG_ID = "messageDialog";
	private static final String DISPLAY_NONE = "display: none;";
	private static final String MESSAGE_BOX_CONTENT_CLASS = "messageBoxContent";
	private static final String LANGUAGE_CHOOSER_ID = "languageChooser";
	private static final String LANGUAGE_CHOOSER_MENU_ID = "languageChooserMenu";
	private static final String VERSION_CHOOSER_ID = "versionChooser";
	private static final String VERSION_CHOOSER_MENU_ID = "versionChooserMenu";
	private static final String DESIGN_VIEW_PAGE_TITLE_ID = "designViewPageTitle";
	private static final String DESIGN_VIEW_FORM_ERROR_MESSAGE_ID = "designViewFormErrorMsg";
	private static final String LABEL_CLASS = "label";
	private static final String PAGE_TITLE_REL = "FormPage.saveTitle updatePageIconText";
	private static final String DESIGN_VIEW_CURRENT_PAGE_TITLE_ID = "designViewCurrentPageTitle";
	private static final String DROPBOX_INNER_ID = "dropBoxinner";
	private static final String INNER_POSTFIX = "inner";
	private static final String NOFORM_NOTICE_ID = "noFormNotice";
	private static final String SPEED_BUTTON_CLASS = "speedButton";
	private static final String FORM_BUTTON_CLASS = "formButton";
	private static final String MODIFIED_LABEL = "modifiedLabel";
	
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
		
		for(String nextId : ids) {
			Component component = page.getComponent(nextId);
			if(component instanceof ButtonArea) {
				continue;
			} else {
				return true;
			}
		}
		return false;
	}
	
	protected void initializeComponent(FacesContext context) {
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
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		Locale locale = workspace.getLocale();
		
		Layer languageChooserLayer = new Layer(Layer.DIV);
		languageChooserLayer.setId(LANGUAGE_CHOOSER_ID);
		
		DropdownMenu languageChooser = ICLocaleBusiness.getAvailableLocalesDropdownStringKeyed(iwc.getIWMainApplication(), LANGUAGE_CHOOSER_ID, false);
		languageChooser.setSelectedElement(locale.toString());
		languageChooser.setId(LANGUAGE_CHOOSER_MENU_ID);
		
		languageChooserLayer.add(new Label(getLocalizedString(iwc, "fb_choose_language", "Form language"), languageChooser));
		languageChooserLayer.add(languageChooser);

		component.add(formHeading);
		component.add(languageChooserLayer);
		
		if (workspace.isProcessMode()) {
			
			Layer versionChooserLayer = new Layer(Layer.DIV);
			versionChooserLayer.setId(VERSION_CHOOSER_ID);
			
			DropdownMenu versionChooser = new DropdownMenu(VERSION_CHOOSER_MENU_ID);

			versionChooser.addMenuElement(workspace.getParentFormId().toString(), "latest");
			
			FBHomePageBean bean = (FBHomePageBean) WFUtil.getBeanInstance(FBHomePageBean.beanIdentifier);
			
			List<XForm> xforms = bean.getRelatedByFormId(workspace.getParentFormId());
			
			for (XForm xform : xforms) {
				versionChooser.addMenuElement(xform.getFormId().toString(), "v." + xform.getVersion());
			}
			versionChooser.setSelectedElement(formDocument.getFormId());
			versionChooser.setId(VERSION_CHOOSER_MENU_ID);
			
			versionChooserLayer.add(new Label(getLocalizedString(iwc, "fb_choose_version", "Version"), versionChooser));
			versionChooserLayer.add(versionChooser);
			component.add(versionChooserLayer);
		}
		
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
		
		pageNotice = new Layer(Layer.DIV);
		pageNotice.setId(DESIGN_VIEW_FORM_ERROR_MESSAGE_ID);
		pageNotice.setStyleClass(INLINE_EDIT_CLASS);
		pageNotice.setStyleClass(MODIFIED_LABEL);
		pageNotice.setStyleClass(LABEL_CLASS);
		pageNotice.setMarkupAttribute(REL_ATTRIBUTE, FORM_ERROR_MESSAGE_REL);
		
		Text formErrorMessage = new Text(formDocument.getFormErrorMessage());
		formErrorMessage.setStyleClass(MODIFIED_LABEL);
		pageNotice.add(formErrorMessage);
		
		component.add(pageNotice);
		
		Layer dropBoxInner = new Layer(Layer.DIV);
		dropBoxInner.setId(DROPBOX_INNER_ID);
		dropBoxInner.setStyleClass(getStyleClass() + INNER_POSTFIX);
		
		Page page = formPage.getPage();
		if(page != null) {
			if(page.isSpecialPage()) {
				Layer thankYouTextBox = new Layer(Layer.DIV);
				thankYouTextBox.setId(NOFORM_NOTICE_ID);
				
				Text headline = new Text(getLocalizedString(iwc, "labels_special_page", "This is a special page"));
				thankYouTextBox.add(headline);
				
				Paragraph emptyFormBody = new Paragraph();
				emptyFormBody.add(getLocalizedString(iwc, "labels_special_page_body", "You cannot add components or buttons to this page"));
				thankYouTextBox.add(emptyFormBody);
				
				component.add(thankYouTextBox);
				
				fillComponentList(page.getContainedComponentsIds(), page, dropBoxInner);
			} else {
				List<String> ids = page.getContainedComponentsIds();
				if(!hasComponents(ids, page)) {
					Layer emptyForm = new Layer(Layer.DIV);
					emptyForm.setId(NOFORM_NOTICE_ID);
					
					Text emptyFormHeader = new Text(getLocalizedString(iwc, "labels_empty_form_header", "This page is empty right now"));
					emptyForm.add(emptyFormHeader);
					
					Paragraph emptyFormBody = new Paragraph();
					emptyFormBody.add(getLocalizedString(iwc, "labels_empty_form_body", "You can add components by draggin them from the palette"));
					emptyForm.add(emptyFormBody);
					
					component.add(emptyForm);
				} else {
					fillComponentList(ids, page, dropBoxInner);
				}
			}
		}
		
		component.add(dropBoxInner);
		
		FBButtonArea area = new FBButtonArea();
		area.setStyleClass(componentStyleClass);
		area.setComponentStyleClass(FORM_BUTTON_CLASS);
		component.add(area);
		
		add(component);
	}
	
	private void fillComponentList(List<String> ids, Page page, PresentationObjectContainer parent) {
		if(ids == null) {
			return;
		}
		
		for(String nextId : ids) {
			Component comp = page.getComponent(nextId);
			if(!(comp instanceof Container)) {
				FBFormComponent formComponent = new FBFormComponent(comp);
				formComponent.setStyleClass(componentStyleClass);
				formComponent.setSpeedButtonStyleClass(SPEED_BUTTON_CLASS);
				parent.add(formComponent);
			}
		}
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
