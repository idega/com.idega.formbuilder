package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxSupport;
import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.presentation.IWBaseComponent;

public class FBFormProperties extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "FormProperties";
	
	private static final String WFBLOCK_CONTENT_FACET = "WFBLOCK_CONTENT_FACET";
	
	public FBFormProperties() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBFormProperties.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		/*WFTitlebar bar = new WFTitlebar();
		bar.addTitleText("Form properties");
		
		WFBlock pageInfo = new WFBlock();
		pageInfo.setId("fbformproperties");
		pageInfo.setRendered(true);
		pageInfo.setTitlebar(bar);*/
		
		FBDivision formHeading = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		formHeading.setId("none");
//		formHeading.setStyleClass("formHeading");
		
		
		HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		titleLabel.setValue("Form title");
		titleLabel.setFor("formTitle");
		formHeading.getChildren().add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("formTitle");
		title.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		
		UIAjaxSupport formTitleS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		formTitleS.setEvent("onblur");
		formTitleS.setOnsubmit("showLoadingMessage('Saving')");
		formTitleS.setReRender("mainApplication");
//		formTitleS.setActionListener(application.createMethodBinding("#{formDocument.processAction}", new Class[]{ActionEvent.class}));
		formTitleS.setAction(application.createMethodBinding("#{formDocument.processAction}", new Class[]{ActionEvent.class}));
		formTitleS.setAjaxSingle(false);
		formTitleS.setOncomplete("closeLoadingMessage()");
		title.getChildren().add(formTitleS);
		
		formHeading.getChildren().add(title);
		
		HtmlTag br = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br.setValue("br");
		
		formHeading.getChildren().add(br);
		
		HtmlOutputLabel submitLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		submitLabel.setValue("Submit button label");
		submitLabel.setFor("submitLabel2");
		formHeading.getChildren().add(submitLabel);
		
		HtmlInputText submit = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		submit.setId("submitLabel2");
		//title.setOnblur("applyChanges()");
		submit.setValueBinding("value", application.createValueBinding("#{formDocument.submitLabel}"));
		

		UIAjaxSupport submitLabelS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		submitLabelS.setEvent("onblur");
		submitLabelS.setReRender("mainApplication");
//		formTitleS.setReRender("formHeadingHeader");
		submitLabelS.setActionListener(application.createMethodBinding("#{updateTitleAction.processAction}", new Class[]{ActionEvent.class}));
		submitLabelS.setAjaxSingle(false);
//		submitLabelS.setOnsubmit("alert('Support here')");
		submit.getChildren().add(submitLabelS); 
		
		formHeading.getChildren().add(submit);
		
		this.getFacets().put(WFBLOCK_CONTENT_FACET, formHeading);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		UIComponent body = (UIComponent) getFacet(WFBLOCK_CONTENT_FACET);
		if(body != null) {
//			renderChild(context, body);
			if(body.isRendered()) {
				body.encodeBegin(context);
				body.encodeChildren(context);
				body.encodeEnd(context);
			}
		}
	}
	
}
