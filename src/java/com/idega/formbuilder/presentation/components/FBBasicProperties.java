package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;
import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFBlock;
import com.idega.webface.WFTitlebar;

public class FBBasicProperties extends FBComponentBase {

//	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "BasicProperties";
	
	private static final String WFBLOCK_CONTENT_FACET = "WFBLOCK_CONTENT_FACET";
	
	public FBBasicProperties() {
		super();
		this.setRendererType(null);
	}
	
	/*public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBBasicProperties.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}*/
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		WFTitlebar bar = new WFTitlebar();
		bar.addTitleText("Simple component properties");
		
		WFBlock pageInfo = new WFBlock();
		pageInfo.setId("fbsimpleroperties");
		pageInfo.setRendered(true);
		pageInfo.setTitlebar(bar);
		
		HtmlOutputLabel requiredLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		requiredLabel.setValue("Required field");
		requiredLabel.setFor("propertyRequired");
		pageInfo.add(requiredLabel);
		
		HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		required.setId("propertyRequired");
		required.setValueBinding("value", application.createValueBinding("#{formComponent.required}"));
		required.setOnclick("$('workspaceform1:saveCompReq').click();");
		/*UIAjaxSupport requiredS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		requiredS.setEvent("onclick");
		requiredS.setReRender("mainApplication");
		requiredS.setActionListener(application.createMethodBinding("#{savePropertiesAction.processAction}", new Class[]{ActionEvent.class}));
		requiredS.setAjaxSingle(true);
		required.getChildren().add(requiredS);*/
		pageInfo.add(required);
		
		HtmlTag br = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br.setValue("br");
		
		pageInfo.add(br);
		
		HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		titleLabel.setValue("Field name");
		titleLabel.setFor("propertyTitle");
		pageInfo.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("propertyTitle");
		title.setValueBinding("value", application.createValueBinding("#{formComponent.label}"));
		title.setOnblur("$('workspaceform1:saveCompLabel').click();");
		/*UIAjaxSupport titleS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		titleS.setEvent("onclick");
		titleS.setReRender("mainApplication");
		titleS.setActionListener(application.createMethodBinding("#{savePropertiesAction.processAction}", new Class[]{ActionEvent.class}));
		titleS.setAction(application.createMethodBinding("#{savePropertiesAction.saveProperties}", null));
		titleS.setAjaxSingle(true);
		titleS.setOnsubmit("alert('SH')");
		titleS.setOncomplete("alert('SH title done')");
		title.getChildren().add(titleS);*/
		pageInfo.add(title);
		
		
		HtmlTag br2 = (HtmlTag) application.createComponent(HtmlTag.COMPONENT_TYPE);
		br2.setValue("br");
		
		pageInfo.add(br2);
		
		
		HtmlOutputLabel errorMsgLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		errorMsgLabel.setValue("Error message");
		errorMsgLabel.setFor("propertyErrorMessage");
		pageInfo.add(errorMsgLabel);
		
		HtmlInputText errorMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		errorMsg.setId("propertyErrorMessage");
		errorMsg.setValueBinding("value", application.createValueBinding("#{formComponent.errorMessage}"));
		errorMsg.setOnblur("$('workspaceform1:saveCompErr').click();");
		/*UIAjaxSupport errorMsgS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		errorMsgS.setEvent("onblur");
		errorMsgS.setReRender("mainApplication");
		errorMsgS.setActionListener(application.createMethodBinding("#{savePropertiesAction.processAction}", new Class[]{ActionEvent.class}));
		errorMsgS.setAction(application.createMethodBinding("#{savePropertiesAction.saveProperties}", null));
		errorMsgS.setAjaxSingle(false);
		errorMsgS.setOnsubmit("alert('SH')");
		errorMsgS.setOncomplete("alert('SH error done')");
		errorMsg.getChildren().add(errorMsgS);*/
		pageInfo.add(errorMsg);
		
		addFacet(WFBLOCK_CONTENT_FACET, pageInfo);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		UIComponent body = getFacet(WFBLOCK_CONTENT_FACET);
		if(body != null) {
			/*if (body.isRendered()) {
				body.encodeBegin(context);
				body.encodeChildren(context);
				body.encodeEnd(context);
			}*/
			renderChild(context, body);
		}
	}
	
}
