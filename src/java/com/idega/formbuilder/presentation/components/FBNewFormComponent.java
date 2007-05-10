package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;

import com.idega.presentation.text.Link;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFDivision;

public class FBNewFormComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "NewFormComponent";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private static final String CANCEL_BUTTON_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/cancel.png";
	private static final String OK_BUTTON_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/accept.png";
	
	public FBNewFormComponent() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision body = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		body.setStyleClass(getStyleClass());
		body.setId("newFormComp");
		
		HtmlGraphicImage newIcon = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		newIcon.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/application_form_add.png");
		newIcon.setId("newIcon");
		newIcon.setStyleClass("newIcon");
		
		HtmlInputText formName = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		formName.setStyleClass("formNameInput");
		formName.setStyle("display: none");
		formName.setId("newTxt");
		formName.setOnkeypress("if(isEnterEvent(event)) { pressOk(event); return false; }");
		
//		HtmlCommandLink newButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
//		newButton.setValue("New Form");
//		newButton.setId("newBt");
//		newButton.setStyleClass("newBt");
//		newButton.setStyle("display: inline");
//		newButton.setOnclick("showInputField();return false;");
		Link newButton = new Link("New Form");
		newButton.setStyleClass("newBt");
		newButton.setId("newBt");
		newButton.setStyle("display: inline");
		newButton.setOnClick("showInputField();return false;");
//		newButton.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE_FOCAL));
//		newButton.setToFormSubmit("workspaceform1");
		
		FBToolbarButton okButton = new FBToolbarButton();
		okButton.setStyleClass("bottomButtonHidden");
		okButton.setId("okBt");
		okButton.setDisplayText("OK");
		okButton.setDefaultImageURI(OK_BUTTON_ICON);
		okButton.setAction(application.createMethodBinding("#{formDocument.createNewForm}", null));
		
		FBToolbarButton cancelButton = new FBToolbarButton();
		cancelButton.setStyleClass("bottomButtonHidden");
		cancelButton.setId("cancelBt");
		cancelButton.setDisplayText("Cancel");
		cancelButton.setDefaultImageURI(CANCEL_BUTTON_ICON);
		cancelButton.setOnclick("hideInputField();return false;");
		
		addChild(newIcon, body);
		addChild(formName, body);
		addChild(newButton, body);
		addChild(okButton, body);
		addChild(cancelButton, body);
		
		addFacet(CONTENT_DIV_FACET, body);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		UIComponent component = getFacet(CONTENT_DIV_FACET);
		if(component != null) {
			renderChild(context, component);
		}
	}
}
