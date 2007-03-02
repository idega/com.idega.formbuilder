package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFDivision;

public class FBNewFormComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "NewFormComponent";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
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
		formName.setStyle("display: hidden");
		formName.setId("newTxt");
		
		HtmlCommandLink newButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		newButton.setValue("New Form");
		newButton.setId("newBt");
		newButton.setStyleClass("newBt");
		newButton.setStyle("display: inline");
		newButton.setOnclick("showInputField();return false;");
		
		HtmlCommandLink okButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		okButton.setValue("OK");
		okButton.setId("okBt");
		okButton.setStyleClass("okBt");
		okButton.setStyle("display: hidden");
		okButton.setAction(application.createMethodBinding("#{formDocument.createNewForm}", null));
		
		HtmlCommandLink cancelButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		cancelButton.setValue("Cancel");
		cancelButton.setId("cancelBt");
		cancelButton.setStyleClass("cancelBt");
		cancelButton.setStyle("display: hidden");
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
	
//	public void encodeEnd(FacesContext context) throws IOException {
//		ResponseWriter writer = context.getResponseWriter();
//		writer.write(getEmbededJavascript());
//	}
	
//	public static String getEmbededJavascript() {
//		StringBuilder result = new StringBuilder();
//		result.append("<script language=\"JavaScript\">\n");
//		
//		result.append("function showInputField() {\n");
////		result.append("var container = $('newFormComp');\n");
//		result.append("var input = document.getElementById('workspaceform1:newTxt');\n");
//		result.append("var newBt = document.getElementById('workspaceform1:newBt');\n");
//		result.append("var okBt = document.getElementById('workspaceform1:okBt');\n");
//		result.append("var cancelBt = document.getElementById('workspaceform1:cancelBt');\n");
//		result.append("input.style.display = 'inline';\n");
//		result.append("newBt.style.display = 'none';\n");
//		result.append("okBt.style.display = 'inline';\n");
//		result.append("cancelBt.style.display = 'inline';\n");
//		result.append("}\n");
//		
//		result.append("function hideInputField() {\n");
////		result.append("var container = $('newFormComp');\n");
//		result.append("var input = document.getElementById('workspaceform1:newTxt');\n");
//		result.append("var newBt = document.getElementById('workspaceform1:newBt');\n");
//		result.append("var okBt = document.getElementById('workspaceform1:okBt');\n");
//		result.append("var cancelBt = document.getElementById('workspaceform1:cancelBt');\n");
//		result.append("input.style.display = 'none';\n");
//		result.append("newBt.style.display = 'inline';\n");
//		result.append("okBt.style.display = 'none';\n");
//		result.append("cancelBt.style.display = 'none';\n");
//		result.append("}\n");
//		
//		result.append("var container = document.getElementById('newFormComp');\n");
//		result.append("Rico.Corner.round(container);\n");
//		
//		result.append("</script>\n");
//		return result.toString();
//		
//	}

}
