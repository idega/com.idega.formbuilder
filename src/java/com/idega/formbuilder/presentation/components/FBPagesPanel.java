package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.ajax.html.HtmlActionParameter;
import org.ajax4jsf.ajax.html.HtmlAjaxFunction;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private static final String DELETE_PAGE_FUNCTION = "DELETE_PAGE_FUNCTION";
	private static final String CONFIRMATION_PAGE = "CONFIRMATION_PAGE";
	private static final String THANKYOU_PAGE = "THANKYOU_PAGE";
	
	private String id;
	private String styleClass;
	private String componentStyleClass;
	private String thanksPageId;
	private String confirmationPageId;

	public FBPagesPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		HtmlAjaxFunction deletePageFunction = new HtmlAjaxFunction();
		deletePageFunction.setName("deletePageFunction");
		deletePageFunction.setReRender("mainApplication");
//		deletePageFunction.setOncomplete("closeLoadingMessage()");
		deletePageFunction.setValueBinding("data", application.createValueBinding("#{formPage.deletePage}"));
		
		HtmlActionParameter deletePageFunctionP = new HtmlActionParameter();
		deletePageFunctionP.setName("pageId");
		deletePageFunctionP.setAssignToBinding(application.createValueBinding("#{formPage.id}"));
		addChild(deletePageFunctionP, deletePageFunction);
		
//		Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
//		Page confirmationPage
		
		addFacet(DELETE_PAGE_FUNCTION, deletePageFunction);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		getChildren().clear();
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", id, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		
		UIComponent facet = getFacet(DELETE_PAGE_FUNCTION);
		if(facet != null) {
			renderChild(context, facet);
		}
		
		Locale locale = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
		FormDocument formDocument = ((FormDocument) WFUtil.getBeanInstance("formDocument"));
		Document document = formDocument.getDocument();
		if(document != null) {
			
//			Page confirmation
//			
//			FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
//			formPage.setId(nextId + "_P");
//			formPage.setStyleClass(componentStyleClass);
//			formPage.setOnDelete("deletePageFunction()");
//			String label = ((PropertiesPage)currentPage.getProperties()).getLabel().getString(locale);
//			formPage.setLabel(label);
			
			List<String> ids = document.getContainedPagesIdList();
			Iterator it = ids.iterator();
			while(it.hasNext()) {
				String nextId = (String) it.next();
				Page currentPage = document.getPage(nextId);
				if(currentPage != null) {
					FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
					formPage.setId(nextId + "_P");
					formPage.setStyleClass(componentStyleClass);
					formPage.setOnDelete("deletePageFunction()");
					String label = ((PropertiesPage)currentPage.getProperties()).getLabel().getString(locale);
					formPage.setLabel(label);
					add(formPage);
				}
			}
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
		
		Object values[] = new Object[2];
		values[0] = id;
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
		return 	"<script language=\"JavaScript\">\n"
		
				+ "function setupPagesDragAndDrop() {\n"
				+ "Position.includeScrollOffsets = true;\n"
				+ "Sortable.create(\"" + values[0] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:rearrangePages,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "}\n"
				
				+ "function rearrangePages() {\n"
//				+ "var componentIDs = Sortable.serialize(\"" + values[2] + "\",{tag:\"div\",name:\"id\"});\n"
//				+ "var delimiter = '&id[]=';\n"
//				+ "var idPrefix = 'fbcomp_';\n"
//				+ "dwrmanager.updateComponentList(updateOrder,componentIDs,idPrefix,delimiter);\n"
//				+ "pressedDelete = true;\n"
				+ "}\n"
				
//				+ "function updateOrder() {}\n"
				
				+ "setupPagesDragAndDrop();\n"
				
				+ "</script>\n";
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context); 
		values[1] = id;
		values[2] = styleClass;
		values[3] = componentStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		componentStyleClass = (String) values[3];
	}
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

}
