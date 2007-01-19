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
import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.formbuilder.view.ActionManager;
import com.idega.presentation.IWBaseComponent;

public class FBDesignView extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "DesignView";
	
	public static final String DESIGN_VIEW_STATUS_NOFORM = "noform";
	public static final String DESIGN_VIEW_STATUS_EMPTY = "empty";
	public static final String DESIGN_VIEW_STATUS_ACTIVE = "active";
	
	public static final String DESIGN_VIEW_NOFORM_FACET = "noFormNoticeFacet";
	public static final String DESIGN_VIEW_EMPTY_FACET = "emptyFormFacet";
	public static final String DESIGN_VIEW_HEADER_FACET = "formHeaderFacet";
	public static final String SUBMIT_BUTTON_FACET = "submit";
	
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
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBDesignView.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		
		FBDivision noFormNotice = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		noFormNotice.setId("noFormNotice");
		
		HtmlOutputText noFormNoticeHeader = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noFormNoticeHeader.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_noform_header']}"));
		noFormNotice.getChildren().add(noFormNoticeHeader);
		
		HtmlTag br = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br.setValue("br");
		noFormNotice.getChildren().add(br);
		
		HtmlOutputText noFormNoticeBody = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noFormNoticeBody.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_noform_body']}"));
		noFormNotice.getChildren().add(noFormNoticeBody);
		
		this.getFacets().put(DESIGN_VIEW_NOFORM_FACET, noFormNotice);
		
		FBDivision formHeading = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		formHeading.setId("formHeading");
		formHeading.setStyleClass("formHeading");
		
		HtmlOutputText formHeadingHeader = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		formHeadingHeader.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		formHeadingHeader.setId("formHeadingHeader");
		formHeading.getChildren().add(formHeadingHeader);
		
		UIAjaxSupport formHeadingS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		formHeadingS.setId("formHeadingHeaderS");
		formHeadingS.setEvent("onclick");
		formHeadingS.setReRender("workspaceform1:ajaxMenuPanel");
		formHeadingS.setActionListener(application.createMethodBinding("#{formDocument.loadFormProperties}", new Class[]{ActionEvent.class}));
		formHeadingS.setAjaxSingle(true);
		formHeadingHeader.getChildren().add(formHeadingS); 
		
		HtmlTag hr = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		hr.setValue("hr");
		formHeading.getChildren().add(hr);
		
		this.getFacets().put(DESIGN_VIEW_HEADER_FACET, formHeading);
		
		FBDivision emptyForm = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		emptyForm.setId("emptyForm");
		
		HtmlOutputText emptyFormHeader = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emptyFormHeader.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_empty_form_header']}"));
		emptyForm.getChildren().add(emptyFormHeader);
		
		HtmlTag br2 = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br2.setValue("br");
		emptyForm.getChildren().add(br2);
		
		HtmlOutputText emptyFormBody = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emptyFormBody.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['labels_empty_form_body']}"));
		emptyForm.getChildren().add(emptyFormBody);
		
		this.getFacets().put(DESIGN_VIEW_EMPTY_FACET, emptyForm);
		
		
		FBFormComponent submitButton = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
		submitButton.setStyleClass(this.getComponentStyleClass());
		submitButton.setSubmit(true);
		this.getFacets().put(SUBMIT_BUTTON_FACET, submitButton);
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
//		String temp;
		Application application = context.getApplication();
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		this.getChildren().clear();
		
//		IFormManager formManagerInstance = ActionManager.getFormManagerInstance();
		
		List<String> ids = ActionManager.getFormManagerInstance().getFormComponentsIdsList();
		Iterator it = ids.iterator();
//		String selectedComponent = null;
		ValueBinding vb = this.getValueBinding("selectedComponent");
		if(vb != null) {
			selectedComponent = (String) vb.getValue(context);
		}
		while(it.hasNext()) {
			String nextId = (String) it.next();
			FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
			formComponent.setId(nextId);
			formComponent.setStyleClass(this.getComponentStyleClass());
			if(nextId.equals(selectedComponent)) {
				formComponent.setSelected(true);
			} else {
				formComponent.setSelected(false);
			}
			formComponent.setSelectedStyleClass(this.getComponentStyleClass() + "Sel");
		    this.getChildren().add(formComponent);
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
			if(status.equals(FBDesignView.DESIGN_VIEW_STATUS_NOFORM)) {
				UIComponent noFormNotice = getFacet(FBDesignView.DESIGN_VIEW_NOFORM_FACET);
				if(noFormNotice != null) {
					/*if (noFormNotice.isRendered()) {
						noFormNotice.encodeBegin(context);
						noFormNotice.encodeChildren(context);
						noFormNotice.encodeEnd(context);
					}*/
					renderChild(context, noFormNotice);
				}
			} else if(status.equals(FBDesignView.DESIGN_VIEW_STATUS_EMPTY)) {
				UIComponent formHeader = getFacet(FBDesignView.DESIGN_VIEW_HEADER_FACET);
				if (formHeader != null) {
					if (formHeader.isRendered()) {
						formHeader.encodeBegin(context);
						formHeader.encodeChildren(context);
						formHeader.encodeEnd(context);
					}
//					renderChild(context, formHeader);
				}
				UIComponent emptyNotice = getFacet(FBDesignView.DESIGN_VIEW_EMPTY_FACET);
				if (emptyNotice != null) {
					/*if (emptyNotice.isRendered()) {
						emptyNotice.encodeBegin(context);
						emptyNotice.encodeChildren(context);
						emptyNotice.encodeEnd(context);
					}*/
					renderChild(context, emptyNotice);
				}
			} else if(status.equals(FBDesignView.DESIGN_VIEW_STATUS_ACTIVE)) {
				UIComponent formHeader = getFacet(FBDesignView.DESIGN_VIEW_HEADER_FACET);
				if (formHeader != null) {
					if (formHeader.isRendered()) {
						formHeader.encodeBegin(context);
						formHeader.encodeChildren(context);
						formHeader.encodeEnd(context);
					}
//					renderChild(context, formHeader);
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
		if(status != null && !status.equals(DESIGN_VIEW_STATUS_NOFORM)) {
			FBFormComponent submit = (FBFormComponent) getFacet(SUBMIT_BUTTON_FACET);
			if (submit != null) {
				submit.setSubmit(true);
				submit.setId("submitB");
				if (submit.isRendered()) {
					submit.encodeBegin(context);
					submit.encodeChildren(context);
					submit.encodeEnd(context);
				}
//				renderChild(context, submit);
			}
		}
		
		writer.endElement("DIV");
		Object values[] = new Object[3];
		values[0] = getId();
		values[1] = getComponentStyleClass();
		values[2] = getId() + "inner";
		writer.write(getEmbededJavascript(values));
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = this.getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
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
//				+ "var length = $(\"" + values[0] + "\").childNodes.length;\n"
//				+ "var submit = $(\"" + values[0] + "\").childNodes[length-1];\n"
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
				+ "dwrmanager.updateComponentList(updateOrder,componentIDs,idPrefix,delimiter);\n"
				+ "pressedDelete = true;\n"
				+ "}\n"
				
				+ "function updateOrder() {}\n"
//				+ "}\n"
				
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
