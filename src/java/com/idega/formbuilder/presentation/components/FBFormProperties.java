package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlInputTextarea;
import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFDivision;

public class FBFormProperties extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormProperties";
	
	private static final String CONTENT_FACET = "CONTENT_FACET";
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	private static final String SINGLE_LINE_PROPERTY = "fbSingleLineProperty";
	private static final String TWO_LINE_PROPERTY = "fbTwoLineProperty";
	
	public FBFormProperties() {
		super();
		setRendererType(null);
	}
	
	private WFDivision createPropertyContainer(String styleClass, Application application) {
		WFDivision body = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		body.setStyleClass(styleClass);
		
		return body;
	}
	
	private UIComponent createFormProperties(Application application) {
		WFDivision body = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		body.setId(getId());
		body.setStyleClass(PROPERTIES_PANEL_SECTION_STYLE);
		
		WFDivision line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		titleLabel.setValue("Form title");
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("formTitle");
		title.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		title.setOnblur("saveFormTitle(this.value)");
		title.setOnkeydown("savePropertyOnEnter(this.value,'formTitle',event);");
		
		line.add(titleLabel);
		line.add(title);
		body.add(line);
		
		line = createPropertyContainer(SINGLE_LINE_PROPERTY, application);
		
		HtmlOutputLabel previewLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		previewLabel.setValue("Form contains preview");
		
		HtmlSelectBooleanCheckbox preview = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		preview.setId("previewScreen");
		preview.setValueBinding("value", application.createValueBinding("#{formDocument.hasPreview}"));
		preview.setOnchange("saveHasPreview(this);");
		
		line.add(previewLabel);
		line.add(preview);
		body.add(line);
		
		line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel thankYouTitleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		thankYouTitleLabel.setValue("Thank you title");
		
		HtmlInputText thankYouTitle = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		thankYouTitle.setId("thankYouTitle");
		thankYouTitle.setValueBinding("value", application.createValueBinding("#{formDocument.thankYouTitle}"));
		thankYouTitle.setOnblur("saveThankYouTitle(this.value)");
		thankYouTitle.setOnkeydown("savePropertyOnEnter(this.value,'formThxTitle',event);");
		
		line.add(thankYouTitleLabel);
		line.add(thankYouTitle);
		body.add(line);
		
		line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel thankYouTextLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		thankYouTextLabel.setValue("Thank You Text");
		
		HtmlInputTextarea thankYouText = (HtmlInputTextarea) application.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
		thankYouText.setId("thankYouText");
		thankYouText.setValueBinding("value", application.createValueBinding("#{formDocument.thankYouText}"));
		thankYouText.setOnblur("saveThankYouText(this.value)");
		thankYouText.setOnkeydown("savePropertyOnEnter(this.value,'formThxText',event);");
		
		line.add(thankYouTextLabel);
		line.add(thankYouText);
		body.add(line);
		
//		line = createPropertyContainer(SINGLE_LINE_PROPERTY, application);
//		
//		HtmlOutputLabel showFormStepsLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
//		showFormStepsLabel.setValue("Enable section visualization");
//		
//		HtmlSelectBooleanCheckbox showFormStepsChbx = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
//		showFormStepsChbx.setId("visualization");
//		showFormStepsChbx.setValueBinding("value", application.createValueBinding("#{formDocument.enableBubbles}"));
//		showFormStepsChbx.setOnchange("saveEnableBubbles(this);");
//		
//		line.add(showFormStepsLabel);
//		line.add(showFormStepsChbx);
//		body.add(line);
		
		return body;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		addFacet(CONTENT_FACET, createFormProperties(application));
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		Application application = context.getApplication();
		if (!isRendered()) {
			return;
		}
		WFDivision body = (WFDivision) getFacet(CONTENT_FACET);
		if(body == null) {
			body = (WFDivision) createFormProperties(application);
		}
		renderChild(context, body);
	}
	
}
