package com.idega.formbuilder.presentation;

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
import com.idega.webface.WFBlock;
import com.idega.webface.WFTitlebar;

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
		
		WFTitlebar bar = new WFTitlebar();
		bar.addTitleText("Form properties");
		
		WFBlock pageInfo = new WFBlock();
		pageInfo.setId("fbformproperties");
		pageInfo.setRendered(true);
		pageInfo.setTitlebar(bar);
		
		
		HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		titleLabel.setValue("Form title");
		titleLabel.setFor("formTitle");
		pageInfo.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("formTitle");
		//title.setOnblur("applyChanges()");
		title.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		
		UIAjaxSupport formTitleS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		formTitleS.setEvent("onblur");
		formTitleS.setReRender("mainApplication");
//		formTitleS.setReRender("formHeadingHeader");
		formTitleS.setActionListener(application.createMethodBinding("#{updateTitleAction.processAction}", new Class[]{ActionEvent.class}));
		formTitleS.setAjaxSingle(false);
//		formTitleS.setOnsubmit("alert('Support here')");
		title.getChildren().add(formTitleS);
		
		pageInfo.add(title);
		
		HtmlTag br = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br.setValue("br");
		
		pageInfo.add(br);
		
		HtmlOutputLabel submitLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		submitLabel.setValue("Submit button label");
		submitLabel.setFor("submitLabel2");
		pageInfo.add(submitLabel);
		
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
		
		pageInfo.add(submit);
		
		this.getFacets().put(WFBLOCK_CONTENT_FACET, pageInfo);
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
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[1];
		values[0] = super.saveState(context);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}
	
}
