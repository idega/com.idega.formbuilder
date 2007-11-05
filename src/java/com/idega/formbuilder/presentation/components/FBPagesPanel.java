package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesPage;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private static final String CONFIRMATION_PAGE = "CONFIRMATION_PAGE";
	private static final String THANKYOU_PAGE = "THANKYOU_PAGE";
	private static final String GENERAL_PAGES_HEADER = "GENERAL_PAGES_HEADER";
	private static final String SPECIAL_PAGES_HEADER = "SPECIAL_PAGES_HEADER";
	private static final String TOOLBAR_FACET = "TOOLBAR_FACET";
	private static final String DEFAULT_PAGE_LOAD_ACTION = "loadPageInfo(this.id);";
	private static final String DEFAULT_PAGE_REMOVE_ACTION = "deletePage(this.id);";
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

	public FBPagesPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer topToolbar = new Layer(Layer.DIV);
		topToolbar.setStyleClass(PAGES_PANEL_TOOLBAR_CLASS);
		
		Link newSectionBtn = new Link(getLocalizedString(iwc, "fb_add_page_link", "New section"));
		newSectionBtn.setId("newPageButton");
		newSectionBtn.setOnClick("createNewPage();return false;");
		topToolbar.add(newSectionBtn);
		addFacet(TOOLBAR_FACET, topToolbar);
		
		Layer generalPagesHeader = new Layer(Layer.DIV);
		generalPagesHeader.setStyleClass(PAGES_PANEL_TOOLBAR_CLASS);
		Text generalPagesHeaderText = new Text(getLocalizedString(iwc, "fb_pages_general_section", "General sections"));
		generalPagesHeaderText.setStyleClass(PAGES_PANEL_HEADER_CLASS);
		generalPagesHeader.add(generalPagesHeaderText);
		addFacet(GENERAL_PAGES_HEADER, generalPagesHeader);
		
		Layer specialPagesHeader = new Layer(Layer.DIV);
		specialPagesHeader.setStyleClass(PAGES_PANEL_TOOLBAR_CLASS);
		Text specialPagesHeaderText = new Text(getLocalizedString(iwc, "fb_pages_special_section", "Special sections"));
		specialPagesHeaderText.setStyleClass(PAGES_PANEL_HEADER_CLASS);
		specialPagesHeader.add(specialPagesHeaderText);
		addFacet(SPECIAL_PAGES_HEADER, specialPagesHeader);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		getChildren().clear();
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement(Layer.DIV, this);
		writer.writeAttribute("id", getId() + "Main", "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		UIComponent component = getFacet(TOOLBAR_FACET);
		if(component != null) {
			renderChild(context, component);
		}
		
		component = getFacet(GENERAL_PAGES_HEADER);
		if(component != null) {
			renderChild(context, component);
		}
		
		writer.startElement(Layer.DIV, null);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", generalPartStyleClass, null);
		
		Locale locale = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
		FormDocument formDocument = ((FormDocument) WFUtil.getBeanInstance("formDocument"));
		Document document = formDocument.getDocument();
		if(document != null) {
			String selectedPageId = null;
			Page selectedPage = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
			if(selectedPage != null) {
				selectedPageId = selectedPage.getId();
			}
			
			if(formDocument.isHasPreview()) {
				Page confirmation = document.getConfirmationPage();
				if(confirmation != null) {
					FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
					formPage.setId(confirmation.getId() + P);
					if(confirmation.getId().equals(selectedPageId)) {
						formPage.setStyleClass(componentStyleClass + SPECIAL + " " + selectedStyleClass);
					} else {
						formPage.setStyleClass(componentStyleClass + SPECIAL);
					}
					String label = ((PropertiesPage)confirmation.getProperties()).getLabel().getString(locale);
					formPage.setLabel(label);
					formPage.setActive(false);
					formPage.setOnLoad(DEFAULT_CONFIRM_LOAD_ACTION);
					addFacet(CONFIRMATION_PAGE, formPage);
				}
			}
			Page thanks = document.getThxPage();
			if(thanks != null) {
				FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
				formPage.setId(thanks.getId() + P);
				if(thanks.getId().equals(selectedPageId)) {
					formPage.setStyleClass(componentStyleClass + SPECIAL + " " + selectedStyleClass);
				} else {
					formPage.setStyleClass(componentStyleClass + SPECIAL);
				}
				String label = ((PropertiesPage)thanks.getProperties()).getLabel().getString(locale);
				formPage.setLabel(label);
				formPage.setActive(false);
				formPage.setOnLoad(DEFAULT_THX_LOAD_ACTION);
				addFacet(THANKYOU_PAGE, formPage);
			}
			List<String> ids = formDocument.getCommonPagesIdList();
			if(ids != null) {
				for(Iterator<String> it = ids.iterator(); it.hasNext(); ) {
					String nextId = it.next();
					Page currentPage = document.getPage(nextId);
					if(currentPage != null) {
						FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
						formPage.setId(nextId + P);
						if(nextId.equals(selectedPageId)) {
							formPage.setStyleClass(componentStyleClass + " " + selectedStyleClass);
						} else {
							formPage.setStyleClass(componentStyleClass);
						}
						formPage.setOnDelete(DEFAULT_PAGE_REMOVE_ACTION);
						formPage.setOnLoad(DEFAULT_PAGE_LOAD_ACTION);
						String label = ((PropertiesPage)currentPage.getProperties()).getLabel().getString(locale);
						formPage.setLabel(label);
						formPage.setActive(false);
						add(formPage);
					}
				}
			}
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement(Layer.DIV);
		
		UIComponent component = getFacet(SPECIAL_PAGES_HEADER);
		if(component != null) {
			renderChild(context, component);
		}
		
		writer.startElement(Layer.DIV, null);
		writer.writeAttribute("id", getId() + SPECIAL, null);
		writer.writeAttribute("class", specialPartStyleClass, null);
		
		component = getFacet(CONFIRMATION_PAGE);
		if(component != null) {
			renderChild(context, component);
		}
		component = getFacet(THANKYOU_PAGE);
		if(component != null) {
			renderChild(context, component);
		}
		writer.endElement(Layer.DIV);
		writer.endElement(Layer.DIV);
		super.encodeEnd(context);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
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
