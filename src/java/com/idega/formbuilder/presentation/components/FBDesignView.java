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

import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.Container;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.webface.WFDivision;
import com.idega.webface.WFUtil;

public class FBDesignView extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "DesignView";
	
	public static final String DESIGN_VIEW_STATUS_NOFORM = "noform";
	public static final String DESIGN_VIEW_STATUS_EMPTY = "empty";
	public static final String DESIGN_VIEW_STATUS_ACTIVE = "active";
	
	public static final String DESIGN_VIEW_NOFORM_FACET = "noFormNoticeFacet";
	public static final String DESIGN_VIEW_EMPTY_FACET = "emptyFormFacet";
	public static final String DESIGN_VIEW_HEADER_FACET = "formHeaderFacet";
	public static final String DESIGN_VIEW_PAGE_FACET = "pageTitleFacet";
	
	public static final String BUTTON_AREA_FACET = "BUTTON_AREA_FACET";
	
	private String componentStyleClass;
	private String status;
	private String selectedComponent;

	public String getSelectedComponent() {
		return selectedComponent;
	}

	public void setSelectedComponent(String selectedComponent) {
		this.selectedComponent = selectedComponent;
	}

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
		
		addFacet(DESIGN_VIEW_NOFORM_FACET, noFormNotice);
		
		WFDivision formHeading = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		formHeading.setId("formHeading");
		formHeading.setStyleClass("formHeading");
		
		HtmlOutputLabel formHeadingHeader = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		formHeadingHeader.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		formHeadingHeader.setId("formHeadingHeader");
		formHeadingHeader.setOnclick("loadFormInfo()");
		addChild(formHeadingHeader, formHeading);
		
		addFacet(DESIGN_VIEW_HEADER_FACET, formHeading);
		
		WFDivision pageNotice = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		pageNotice.setId("pageNotice");
		pageNotice.setStyleClass("formHeading");
		
		HtmlOutputLabel currentPageTitle = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		currentPageTitle.setValueBinding("value", application.createValueBinding("#{formPage.title}"));
		currentPageTitle.setId("currentPageTitle");
		currentPageTitle.setOnclick("");
		addChild(currentPageTitle, pageNotice);
		
		addFacet(DESIGN_VIEW_PAGE_FACET, pageNotice);
		
		WFDivision emptyForm = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		emptyForm.setId("emptyForm");
		
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
		
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			List<String> ids = page.getContainedComponentsIdList();
			Iterator it = ids.iterator();
			vb = getValueBinding("selectedComponent");
			if(vb != null) {
				selectedComponent = (String) vb.getValue(context);
			}
			while(it.hasNext()) {
				String nextId = (String) it.next();
				Component comp = page.getComponent(nextId);
				if(comp instanceof Container) {
//					ButtonArea buttonArea = page.getButtonArea();
					FBButtonArea area = (FBButtonArea) application.createComponent(FBButtonArea.COMPONENT_TYPE);
					area.setId("pageButtonArea");
					area.setStyleClass(componentStyleClass);
					area.setComponentStyleClass("formButton");
					addFacet(BUTTON_AREA_FACET, area);
				} else {
					FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
					formComponent.setId(nextId);
					formComponent.setStyleClass(getComponentStyleClass());
					if(nextId.equals(selectedComponent)) {
						formComponent.setSelected(true);
					} else {
						formComponent.setSelected(false);
					}
					formComponent.setSelectedStyleClass(getComponentStyleClass() + "Sel");
				    add(formComponent);
				}
			}
		}

		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		vb = getValueBinding("status");
		if(vb != null) {
			status = (String) vb.getValue(context);
		} else {
			status = getStatus();
		}
		System.out.println("STATUS: " + status);
		if(status != null) {
			if(status.equals(DESIGN_VIEW_STATUS_NOFORM)) {
				UIComponent noFormNotice = getFacet(DESIGN_VIEW_NOFORM_FACET);
				if(noFormNotice != null) {
					renderChild(context, noFormNotice);
				}
			} else if(status.equals(DESIGN_VIEW_STATUS_EMPTY)) {
				UIComponent formHeader = getFacet(DESIGN_VIEW_HEADER_FACET);
				if (formHeader != null) {
					renderChild(context, formHeader);
				}
				UIComponent emptyNotice = getFacet(DESIGN_VIEW_EMPTY_FACET);
				if (emptyNotice != null) {
					renderChild(context, emptyNotice);
				}
			} else if(status.equals(DESIGN_VIEW_STATUS_ACTIVE)) {
				UIComponent pageHeader = getFacet(DESIGN_VIEW_PAGE_FACET);
				if (pageHeader != null) {
					renderChild(context, pageHeader);
				}
				UIComponent formHeader = getFacet(DESIGN_VIEW_HEADER_FACET);
				if (formHeader != null) {
					renderChild(context, formHeader);
				}
			}
		}
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId() + "inner", null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeEnd(context);
		writer.endElement("DIV");
		
		if(!status.equals(DESIGN_VIEW_STATUS_NOFORM)) {
			UIComponent buttonArea = getFacet(BUTTON_AREA_FACET);
			if (buttonArea != null) {
				renderChild(context, buttonArea);
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
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = componentStyleClass;
		values[2] = status;
		values[3] = selectedComponent;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		componentStyleClass = (String) values[1];
		status = (String) values[2];
		selectedComponent = (String) values[3];
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
	
}
