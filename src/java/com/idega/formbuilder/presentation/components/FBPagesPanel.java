package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PropertiesPage;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private static final String CONFIRMATION_PAGE = "CONFIRMATION_PAGE";
	private static final String THANKYOU_PAGE = "THANKYOU_PAGE";
	
	private String componentStyleClass;

	public FBPagesPanel() {
		super();
		setRendererType(null);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		getChildren().clear();
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("style", "display: block; min-height: 400px; overflow: auto;", null);
		
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
					formPage.setOnLoad("loadConfirmationPage()");
					//TODO previewPage handling
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
				formPage.setOnLoad("loadThxPage()");
				//TODO thankYouPage handling
				addFacet(THANKYOU_PAGE, formPage);
			}
			List<String> ids = formDocument.getCommonPagesIdList();
			Iterator it = ids.iterator();
			while(it.hasNext()) {
				String nextId = (String) it.next();
				Page currentPage = document.getPage(nextId);
				if(currentPage != null) {
					FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
					formPage.setId(nextId + "_P");
					formPage.setStyleClass(componentStyleClass);
					formPage.setOnDelete("deletePage(this.id)");
					formPage.setOnLoad("loadPageInfo(this.id)");
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
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId() + "Special", null);
		writer.writeAttribute("style", "display: block;", null);
		
		UIComponent component = getFacet(CONFIRMATION_PAGE);
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
		Object values[] = new Object[2];
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

}
