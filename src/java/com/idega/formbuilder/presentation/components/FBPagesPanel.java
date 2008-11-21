package com.idega.formbuilder.presentation.components;

import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.Document;
import com.idega.xformsmanager.business.component.Page;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private static final String PAGES_PANEL_TOOLBAR_CLASS = "pagesPanelToolbar";
	private static final String PAGES_PANEL_HEADER_CLASS = "pagesPanelHeaderText";
	
	private String componentStyleClass;
	private String selectedStyleClass;

	public String getSelectedStyleClass() {
		return selectedStyleClass;
	}

	public void setSelectedStyleClass(String selectedStyleClass) {
		this.selectedStyleClass = selectedStyleClass;
	}

	@Override
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		FormDocument formDocument = ((FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID));
		
		Layer body = new Layer(Layer.DIV);
		body.setId("pagesPanelMain");
		body.setStyleClass("pagesPanel");
		
		Layer messageBox = new Layer(Layer.DIV);
		messageBox.setId("pagesPanelMessageBox");
		
		Text headline = new Text(getLocalizedString(iwc, "labels_pages_disabled", "Section actions disabled in this view"));
		messageBox.add(headline);
		
		body.add(messageBox);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		String view = workspace.getView();
		if(!FBViewPanel.DESIGN_VIEW.equals(view)) {
			messageBox.setStyleAttribute("display", "block");
		} else {
			messageBox.setStyleAttribute("display", "none");
		}
		
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
		
//		Link previewSectionBtn = new Link(getLocalizedString(iwc, "fb_preview_page_link", "Preview"));
//		previewSectionBtn.setId("previewPageButton");
//		if(formDocument.isHasPreview()) {
//			previewSectionBtn.setStyleClass("toolbarBtn removePreviewPageBtn");
//		} else {
//			previewSectionBtn.setStyleClass("toolbarBtn addPreviewPageBtn");
//		}
		
		actionBox.add(newSectionBtn);
//		actionBox.add(previewSectionBtn);
		
		body.add(actionBox);
		
		Layer general = new Layer(Layer.DIV);
		general.setId("pagesPanel");
		general.setStyleClass("pagesGeneralContainer");
		
		String selectedPageId = null;
		Page selectedPage = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage();
		if(selectedPage != null) {
			selectedPageId = selectedPage.getId();
		}
		Document document = formDocument.getDocument();
		if(document != null) {
			List<String> ids = formDocument.getCommonPagesIdList();
			if(ids != null) {
				for(String nextId : ids) {
					Page currentPage = document.getPage(nextId);
					
					FBFormPage formPage = new FBFormPage(currentPage);
					formPage.setStyleClass(generateClassAttribute(nextId.equals(selectedPageId)));
					general.add(formPage);
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
		
		List<Page> specialPages = document.getSpecialPages();
		
		if(specialPages != null) {
			for(Page specialPage : specialPages) {
				
				FBFormPage formPage = new FBFormPage(specialPage);
				formPage.setStyleClass(generateClassAttribute(specialPage.getId().equals(selectedPageId)));
				special.add(formPage);
				
			}
		}
		
		body.add(special);
		
		add(body);
	}
	
	private String generateClassAttribute(boolean selected) {
		StringBuilder style = new StringBuilder(componentStyleClass)
			.append(CoreConstants.SPACE);
		if(selected) {
			style.append(CoreConstants.SPACE)
			.append(selectedStyleClass);
		}
		return style.toString();
	}
	
	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

}
