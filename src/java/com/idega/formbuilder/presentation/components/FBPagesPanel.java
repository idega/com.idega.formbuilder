package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesPage;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private static final String DEFAULT_PAGE_LOAD_ACTION = "loadPageInfo(this.id);";
	private static final String DEFAULT_PAGE_REMOVE_ACTION = "deletePage(event);";
	private static final String DEFAULT_CONFIRM_LOAD_ACTION = "loadConfirmationPage(this.id);";
	private static final String DEFAULT_THX_LOAD_ACTION = "loadThxPage(this.id);";
	private static final String SPECIAL = "Special";
	private static final String P = "_P";
	private static final String PAGES_PANEL_TOOLBAR_CLASS = "pagesPanelToolbar";
	private static final String PAGES_PANEL_HEADER_CLASS = "pagesPanelHeaderText";
	
	private String componentStyleClass;
	private String generalPartStyleClass;
	private String specialPartStyleClass;
	private String selectedStyleClass;

	public String getSelectedStyleClass() {
		return selectedStyleClass;
	}

	public void setSelectedStyleClass(String selectedStyleClass) {
		this.selectedStyleClass = selectedStyleClass;
	}

	public String getGeneralPartStyleClass() {
		return generalPartStyleClass;
	}

	public void setGeneralPartStyleClass(String generalPartStyleClass) {
		this.generalPartStyleClass = generalPartStyleClass;
	}

	public String getSpecialPartStyleClass() {
		return specialPartStyleClass;
	}

	public void setSpecialPartStyleClass(String specialPartStyleClass) {
		this.specialPartStyleClass = specialPartStyleClass;
	}

	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		FormDocument formDocument = ((FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID));
		
		Layer body = new Layer(Layer.DIV);
		body.setId("pagesPanelMain");
		body.setStyleClass("pagesPanel");
		
		Layer generalPagesHeader = new Layer(Layer.DIV);
		generalPagesHeader.setStyleClass(PAGES_PANEL_TOOLBAR_CLASS);
		Text generalPagesHeaderText = new Text(getLocalizedString(iwc, "fb_pages_general_section", "General sections"));
		generalPagesHeaderText.setStyleClass(PAGES_PANEL_HEADER_CLASS);
		
		generalPagesHeader.add(generalPagesHeaderText);
		
		body.add(generalPagesHeader);
		
		Layer actionBox = new Layer(Layer.DIV);
		actionBox.setStyleClass("actionBox");
		
		Link newSectionBtn = new Link(getLocalizedString(iwc, "fb_add_page_link", "New section"));
		newSectionBtn.setId("newPageButton");
		newSectionBtn.setStyleClass("toolbarBtn");
		newSectionBtn.setOnClick("createNewPage();return false;");
		
		Link previewSectionBtn = new Link(getLocalizedString(iwc, "fb_preview_page_link", "Preview"));
		previewSectionBtn.setId("previewPageButton");
		previewSectionBtn.setOnClick("saveHasPreview(event);return false;");
		if(formDocument.isHasPreview()) {
			previewSectionBtn.setStyleClass("toolbarBtn removePreviewPageBtn");
		} else {
			previewSectionBtn.setStyleClass("toolbarBtn addPreviewPageBtn");
		}
		
		
		actionBox.add(newSectionBtn);
		actionBox.add(previewSectionBtn);
		
		body.add(actionBox);
		
		Layer general = new Layer(Layer.DIV);
		general.setId("pagesPanel");
		general.setStyleClass("pagesGeneralContainer");
		
		Locale locale = FBUtil.getUILocale();
		
		String selectedPageId = null;
		Page selectedPage = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage();
		if(selectedPage != null) {
			selectedPageId = selectedPage.getId();
		}
		Document document = formDocument.getDocument();
		if(document != null) {
			List<String> ids = formDocument.getCommonPagesIdList();
			if(ids != null) {
				for(Iterator<String> it = ids.iterator(); it.hasNext(); ) {
					String nextId = it.next();
					Page currentPage = document.getPage(nextId);
					if(currentPage != null) {
						FBFormPage formPage = new FBFormPage();
						formPage.setId(nextId + P);
						if(nextId.equals(selectedPageId)) {
							formPage.setStyleClass(componentStyleClass + CoreConstants.SPACE + selectedStyleClass);
						} else {
							formPage.setStyleClass(componentStyleClass);
						}
						formPage.setOnDelete(DEFAULT_PAGE_REMOVE_ACTION);
						formPage.setOnLoad(DEFAULT_PAGE_LOAD_ACTION);
						String label = ((PropertiesPage)currentPage.getProperties()).getLabel().getString(locale);
						formPage.setLabel(label);
						formPage.setActive(false);
						general.add(formPage);
					}
				}
			}
		}
		
		body.add(general);
		
		Layer specialPagesHeader = new Layer(Layer.DIV);
		specialPagesHeader.setStyleClass(PAGES_PANEL_TOOLBAR_CLASS);
		Text specialPagesHeaderText = new Text(getLocalizedString(iwc, "fb_pages_special_section", "Special sections"));
		specialPagesHeaderText.setStyleClass(PAGES_PANEL_HEADER_CLASS);
		specialPagesHeader.add(specialPagesHeaderText);
		
		body.add(specialPagesHeader);
		
		Layer special = new Layer(Layer.DIV);
		special.setId("pagesPanelSpecial");
		special.setStyleClass("pagesSpecialContainer");
		
		if(formDocument.isHasPreview()) {
			Page confirmation = document.getConfirmationPage();
			if(confirmation != null) {
				FBFormPage formPage = new FBFormPage();
				formPage.setId(confirmation.getId() + P);
				if(confirmation.getId().equals(selectedPageId)) {
					formPage.setStyleClass(componentStyleClass + SPECIAL + CoreConstants.SPACE + selectedStyleClass);
				} else {
					formPage.setStyleClass(componentStyleClass + SPECIAL);
				}
				String label = ((PropertiesPage)confirmation.getProperties()).getLabel().getString(locale);
				formPage.setLabel(label);
				formPage.setActive(false);
				formPage.setOnLoad(DEFAULT_CONFIRM_LOAD_ACTION);
				special.add(formPage);
			}
		}
		Page thanks = document.getThxPage();
		if(thanks != null) {
			FBFormPage formPage = new FBFormPage();
			formPage.setId(thanks.getId() + P);
			if(thanks.getId().equals(selectedPageId)) {
				formPage.setStyleClass(componentStyleClass + SPECIAL + CoreConstants.SPACE + selectedStyleClass);
			} else {
				formPage.setStyleClass(componentStyleClass + SPECIAL);
			}
			String label = ((PropertiesPage)thanks.getProperties()).getLabel().getString(locale);
			formPage.setLabel(label);
			formPage.setActive(false);
			formPage.setOnLoad(DEFAULT_THX_LOAD_ACTION);
			special.add(formPage);
		}
		
		body.add(special);
		
		add(body);
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context); 
		values[1] = componentStyleClass;
		values[2] = generalPartStyleClass;
		values[3] = specialPartStyleClass;
		values[4] = selectedStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		componentStyleClass = (String) values[1];
		generalPartStyleClass = (String) values[2];
		specialPartStyleClass = (String) values[3];
		selectedStyleClass = (String) values[4];
	}

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

}
