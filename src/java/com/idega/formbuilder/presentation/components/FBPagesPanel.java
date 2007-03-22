package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;

import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PropertiesPage;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.text.Text;
import com.idega.webface.WFDivision;
import com.idega.webface.WFUtil;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private static final String CONFIRMATION_PAGE = "CONFIRMATION_PAGE";
	private static final String THANKYOU_PAGE = "THANKYOU_PAGE";
	private static final String GENERAL_PAGES_HEADER = "GENERAL_PAGES_HEADER";
	private static final String SPECIAL_PAGES_HEADER = "SPECIAL_PAGES_HEADER";
	private static final String TOOLBAR_FACET = "TOOLBAR_FACET";
	
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
		Application application = context.getApplication();
		
		WFDivision topToolbar = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		topToolbar.setStyleClass("pagesPanelToolbar");
		HtmlCommandButton newSectionBtn = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		newSectionBtn.setId("newPageButton");
		newSectionBtn.setOnclick("createNewPage();return false;");
		newSectionBtn.setValue("New Section");
		addChild(newSectionBtn, topToolbar);
		addFacet(TOOLBAR_FACET, topToolbar);
		
		WFDivision generalPagesHeader = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		generalPagesHeader.setStyleClass("pagesPanelHeader");
		Text generalPagesHeaderText = new Text();
		generalPagesHeaderText.setText("General sections");
		generalPagesHeaderText.setStyleClass("pagesPanelHeaderText");
		addChild(generalPagesHeaderText, generalPagesHeader);
		addFacet(GENERAL_PAGES_HEADER, generalPagesHeader);
		
		WFDivision specialPagesHeader = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		specialPagesHeader.setStyleClass("pagesPanelHeader");
		Text specialPagesHeaderText = new Text();
		specialPagesHeaderText.setText("Special sections");
		specialPagesHeaderText.setStyleClass("pagesPanelHeaderText");
		addChild(specialPagesHeaderText, specialPagesHeader);
		addFacet(SPECIAL_PAGES_HEADER, specialPagesHeader);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		getChildren().clear();
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		UIComponent component = getFacet(TOOLBAR_FACET);
		if(component != null) {
			renderChild(context, component);
		}
		
		component = getFacet(GENERAL_PAGES_HEADER);
		if(component != null) {
			renderChild(context, component);
		}
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", generalPartStyleClass, null);
		//writer.writeAttribute("style", "display: block; height: 300px; overflow: auto;", null);
		
		Locale locale = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
		FormDocument formDocument = ((FormDocument) WFUtil.getBeanInstance("formDocument"));
		Document document = formDocument.getDocument();
		if(document != null) {
			if(formDocument.isHasPreview()) {
				Page confirmation = document.getConfirmationPage();
				if(confirmation != null) {
					FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
					formPage.setId(confirmation.getId() + "_P");
					formPage.setStyleClass(componentStyleClass + "Special");
					String label = ((PropertiesPage)confirmation.getProperties()).getLabel().getString(locale);
					formPage.setLabel(label);
					formPage.setActive(false);
					formPage.setOnLoad("loadConfirmationPage();");
					addFacet(CONFIRMATION_PAGE, formPage);
				}
			}
			Page thanks = document.getThxPage();
			if(thanks != null) {
				FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
				formPage.setId(thanks.getId() + "_P");
				formPage.setStyleClass(componentStyleClass + "Special");
				String label = ((PropertiesPage)thanks.getProperties()).getLabel().getString(locale);
				formPage.setLabel(label);
				formPage.setActive(false);
				formPage.setOnLoad("loadThxPage();");
				addFacet(THANKYOU_PAGE, formPage);
			}
			List<String> ids = formDocument.getCommonPagesIdList();
			Iterator it = ids.iterator();
			String selectedPageId = null;
			Page selectedPage = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
			if(selectedPage != null) {
				selectedPageId = selectedPage.getId();
			}
			//System.out.println(selectedPageId);
			while(it.hasNext()) {
				
				String nextId = (String) it.next();
				Page currentPage = document.getPage(nextId);
				if(currentPage != null) {
					FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
					formPage.setId(nextId + "_P");
					if(nextId.equals(selectedPageId)) {
						formPage.setStyleClass(selectedStyleClass);
						//System.out.println(nextId);
					} else {
						formPage.setStyleClass(componentStyleClass);
					}
					formPage.setOnDelete("deletePage(this.id)");
					formPage.setOnLoad("loadPageInfo(this.id);");
					String label = ((PropertiesPage)currentPage.getProperties()).getLabel().getString(locale);
					formPage.setLabel(label);
					formPage.setActive(false);
					add(formPage);
				}
			}
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("DIV");
		
		UIComponent component = getFacet(SPECIAL_PAGES_HEADER);
		if(component != null) {
			renderChild(context, component);
		}
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId() + "Special", null);
		writer.writeAttribute("class", specialPartStyleClass, null);
		//writer.writeAttribute("style", "display: block; height: 150px; overflow: auto; margin-top: 15px;", null);
		
		component = getFacet(CONFIRMATION_PAGE);
		if(component != null) {
			renderChild(context, component);
		}
		component = getFacet(THANKYOU_PAGE);
		if(component != null) {
			renderChild(context, component);
		}
		writer.endElement("DIV");
		writer.endElement("DIV");
		super.encodeEnd(context);
		
		Object values[] = new Object[2];
		values[0] = getId();
		values[1] = componentStyleClass;
		writer.write(getEmbededJavascript(values));
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	private String getEmbededJavascript(Object values[]) {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		result.append("setupPagesDragAndDrop('" + values[0] + "','" + values[1] + "');\n");
		result.append("</script>\n");
		return 	result.toString();
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
