package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.Container;
import com.idega.documentmanager.business.form.Page;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.webface.WFDivision;
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
	
	public static final String BUTTON_AREA_FACET = "BUTTON_AREA_FACET";
	
	private String componentStyleClass;
	private String selectedStyleClass;
	private String status;

	public FBDesignView() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision noFormNotice = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		noFormNotice.setId("noFormNotice");
		
		HtmlOutputText noFormNoticeHeader = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noFormNoticeHeader.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_noform_header']}"));
		addChild(noFormNoticeHeader, noFormNotice);
		
		HtmlTag br = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br.setValue("br");
		addChild(br, noFormNotice);
		
		HtmlOutputText noFormNoticeBody = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noFormNoticeBody.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_noform_body']}"));
		addChild(noFormNoticeBody, noFormNotice);
		
		addFacet(DESIGN_VIEW_SPECIAL_FACET, noFormNotice);
		
		WFDivision formHeading = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		formHeading.setId("formHeading");
		formHeading.setStyleClass("formHeading");
		
		FBInlineEdit formHeadingHeader = (FBInlineEdit) application.createComponent(FBInlineEdit.COMPONENT_TYPE);
		formHeadingHeader.setId("formHeadingHeader");
		formHeadingHeader.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		formHeadingHeader.setOnSelect("loadFormInfo();");
		formHeadingHeader.setOnBlur("saveFormTitleOnBlur");
		formHeadingHeader.setOnReturn("saveFormTitleOnReturn");
		addChild(formHeadingHeader, formHeading);
		
		addFacet(DESIGN_VIEW_HEADER_FACET, formHeading);
		
		WFDivision pageNotice = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		pageNotice.setId("pageNotice");
		pageNotice.setStyleClass("formHeading");
		
		FBInlineEdit currentPageTitle = (FBInlineEdit) application.createComponent(FBInlineEdit.COMPONENT_TYPE);
		currentPageTitle.setId("currentPageTitle");
		currentPageTitle.setValueBinding("value", application.createValueBinding("#{formPage.title}"));
		currentPageTitle.setOnBlur("savePageTitleOnBlur");
		currentPageTitle.setOnReturn("savePageTitleOnReturn");
		addChild(currentPageTitle, pageNotice);
		
		addFacet(DESIGN_VIEW_PAGE_FACET, pageNotice);
		
		WFDivision emptyForm = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		emptyForm.setId("emptyForm");
		emptyForm.setStyle("display: none;");
		
		HtmlOutputText emptyFormHeader = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emptyFormHeader.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_empty_form_header']}"));
		addChild(emptyFormHeader, emptyForm);
		
		HtmlTag br2 = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br2.setValue("br");
		addChild(br2, emptyForm);
		
		HtmlOutputText emptyFormBody = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emptyFormBody.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_empty_form_body']}"));
		addChild(emptyFormBody, emptyForm);
		
		addFacet(DESIGN_VIEW_EMPTY_FACET, emptyForm);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		getChildren().clear();
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		ValueBinding vb;
		
		FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
		Page page = formPage.getPage();
		if(page != null) {
			String selectedComponentId = null;
			Component selectedComponent = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getComponent();
			if(selectedComponent != null) {
				selectedComponentId = selectedComponent.getId();
			}
			ButtonArea barea = page.getButtonArea();
			if(barea != null) {
				FBButtonArea area = (FBButtonArea) application.createComponent(FBButtonArea.COMPONENT_TYPE);
				area.setId("pageButtonArea");
				area.setStyleClass(componentStyleClass);
				area.setComponentStyleClass("formButton");
				addFacet(BUTTON_AREA_FACET, area);
			}
			if(page != null) {
				List<String> ids = page.getContainedComponentsIdList();
				Iterator it = ids.iterator();
				while(it.hasNext()) {
					String nextId = (String) it.next();
					Component comp = page.getComponent(nextId);
					if(comp instanceof Container) {
						continue;
					} else {
						FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
						formComponent.setId(nextId);
						if(nextId.equals(selectedComponentId)) {
							formComponent.setStyleClass(getSelectedStyleClass());
						} else {
							formComponent.setStyleClass(getComponentStyleClass());
						}
						formComponent.setOnLoad("loadComponentInfo(this);");
						formComponent.setOnDelete("removeComponent(this);");
						formComponent.setSpeedButtonStyleClass("speedButton");
					    add(formComponent);
					}
				}
			}
		}

		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		vb = getValueBinding("status");
		if(vb != null) {
			status = (String) vb.getValue(context);
		}
		if(status != null) {
			UIComponent pageHeader = getFacet(DESIGN_VIEW_PAGE_FACET);
			if (pageHeader != null) {
				renderChild(context, pageHeader);
			}
			UIComponent formHeader = getFacet(DESIGN_VIEW_HEADER_FACET);
			if (formHeader != null) {
				renderChild(context, formHeader);
			}
			WFDivision noFormNotice = (WFDivision) getFacet(DESIGN_VIEW_SPECIAL_FACET);
			WFDivision emptyNotice = (WFDivision) getFacet(DESIGN_VIEW_EMPTY_FACET);
			if(noFormNotice != null && emptyNotice != null) {
				if(formPage.isSpecial()) {
					noFormNotice.setStyle("display: block;");
					emptyNotice.setStyle("display: none;");
				} else if(page.getContainedComponentsIdList().size() == 0) {
					emptyNotice.setStyle("display: block;");
					noFormNotice.setStyle("display: none;");
				} else {
					noFormNotice.setStyle("display: none;");
					emptyNotice.setStyle("display: none;");
				}
				renderChild(context, noFormNotice);
				renderChild(context, emptyNotice);
			}
		}
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId() + "inner", null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeEnd(context);
		writer.endElement("DIV");
		
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				UIComponent buttonArea = getFacet(BUTTON_AREA_FACET);
				if (buttonArea != null) {
					renderChild(context, buttonArea);
				}
			}
		}
		
		writer.endElement("DIV");
		Object values[] = new Object[3];
		values[0] = getId();
		values[1] = componentStyleClass;
		values[2] = getId() + "inner";
		writer.write(getEmbededJavascript(values));
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			UIComponent current = (UIComponent) it.next();
			renderChild(context, current);
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
	
	private String getEmbededJavascript(Object values[]) {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		result.append("setupComponentDragAndDrop('" + values[0] + "','" + values[1] + "');\n");
		result.append("</script>\n");
		return 	result.toString();
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
