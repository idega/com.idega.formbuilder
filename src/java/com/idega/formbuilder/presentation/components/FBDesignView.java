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
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxSupport;
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
	
	public static final String BUTTON_AREA_FACET = "BUTTON_AREA_FACET";
	
	private String styleClass;
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
		
//		UIAjaxSupport formHeadingS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
//		formHeadingS.setId("formHeadingHeaderS");
//		formHeadingS.setEvent("onclick");
//		formHeadingS.setReRender("ajaxMenuPanel");
//		formHeadingS.setActionListener(application.createMethodBinding("#{formDocument.loadFormProperties}", new Class[]{ActionEvent.class}));
//		formHeadingS.setAjaxSingle(true);
//		addChild(formHeadingS, formHeadingHeader);
		
		addFacet(DESIGN_VIEW_HEADER_FACET, formHeading);
		
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
					ButtonArea buttonArea = page.getButtonArea();
					FBButtonArea area = (FBButtonArea) application.createComponent(FBButtonArea.COMPONENT_TYPE);
					area.setId(buttonArea.getId());
					area.setStyleClass(componentStyleClass);
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
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = componentStyleClass;
		values[3] = status;
		values[4] = selectedComponent;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		componentStyleClass = (String) values[2];
		status = (String) values[3];
		selectedComponent = (String) values[4];
	}
	
	private String getEmbededJavascript(Object values[]) {
		return 	"<script language=\"JavaScript\">\n"
		
				+ "function setupDragAndDrop() {\n"
				+ "Position.includeScrollOffsets = true;\n"
				+ "Sortable.create(\"" + values[2] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:rearrange,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "Droppables.add(\"" + values[0] + "\",{onDrop:handleComponentDrop});\n"
				+ "}\n"
				
				+ "function handleComponentDrop(element,container) {\n"
				+ "var empty = $('workspaceform1:emptyForm');\n"
				+ "if(empty) {\n"
				+ "if(empty.style) {\n"
				+ "empty.style.display = 'none';\n"
				+ "} else {\n"
				+ "empty.display = 'none';\n"
				+ "}\n"
				+ "}\n"
				+ "if(currentElement != null) {\n"
				+ "$(\"" + values[2] + "\").appendChild(currentElement);\n"
				+ "}\n"
				+ "currentElement = null;\n"
				+ "Sortable.create(\"" + values[2] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:rearrange,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "Droppables.add(\"" + values[0] + "\",{onDrop:handleComponentDrop});\n"
				+ "}\n"
				
				+ "function rearrange() {\n"
				+ "var componentIDs = Sortable.serialize(\"" + values[2] + "\",{tag:\"div\",name:\"id\"});\n"
				+ "var delimiter = '&id[]=';\n"
				+ "var idPrefix = 'fbcomp_';\n"
				+ "dwrmanager.updateComponentList(componentIDs,idPrefix,delimiter,updateOrder);\n"
				+ "pressedDelete = true;\n"
				+ "}\n"
				
				+ "function updateOrder() {}\n"
				
				+ "setupDragAndDrop();\n"
				
				+ "</script>\n";
	}
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
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
