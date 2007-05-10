package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;
import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;
import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;

import com.idega.documentmanager.business.form.Component;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.webface.WFDivision;
import com.idega.webface.WFUtil;

public class FBComponentPropertiesPanel extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private static final String BUTTON_PROPERTIES_FACET = "BUTTON_PROPERTIES_FACET";
	private static final String BASIC_PROPERTIES_FACET = "BASIC_PROPERTIES_FACET";
	private static final String ADVANCED_PROPERTIES_FACET = "ADVANCED_PROPERTIES_FACET";
	private static final String EXTERNAL_PROPERTIES_FACET = "EXTERNAL_PROPERTIES_FACET";
	private static final String AUTOFILL_PROPERTIES_FACET = "AUTOFILL_PROPERTIES_FACET";
	private static final String LOCAL_PROPERTIES_FACET = "LOCAL_PROPERTIES_FACET";
	private static final String PLAIN_PROPERTIES_FACET = "PLAIN_PROPERTIES_FACET";
	
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	private static final String SINGLE_LINE_PROPERTY = "fbSingleLineProperty";
	private static final String TWO_LINE_PROPERTY = "fbTwoLineProperty";
	
	public FBComponentPropertiesPanel() {
		super();
		setRendererType(null);
	}
	
	private WFDivision createPropertyContainer(String styleClass, Application application) {
		WFDivision body = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		body.setStyleClass(styleClass);
		
		return body;
	}
	
	private WFDivision createPanelSection(String id, Application application) {
		WFDivision body = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		body.setId(id);
		body.setStyleClass(PROPERTIES_PANEL_SECTION_STYLE);
		
		return body;
	}
	
	private UIComponent createBasicProperties(Application application) {
		WFDivision body = createPanelSection("basicPropertiesPanel", application);
		
		WFDivision line = createPropertyContainer(SINGLE_LINE_PROPERTY, application);
		
		HtmlOutputLabel requiredLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		requiredLabel.setValue("Required field");
		
		HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		required.setId("propertyRequired");
		required.setValueBinding("value", application.createValueBinding("#{formComponent.required}"));
		required.setOnclick("saveRequired(this.checked);");
		
		line.add(requiredLabel);
		line.add(required);
		body.add(line);
		
		line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel errorMsgLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		errorMsgLabel.setValue("Error message");
		
		HtmlInputTextarea errorMsg = (HtmlInputTextarea) application.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
		errorMsg.setId("propertyErrorMessage");
		errorMsg.setValueBinding("value", application.createValueBinding("#{formComponent.errorMessage}"));
		errorMsg.setOnblur("saveErrorMessage(this.value)");
		errorMsg.setOnkeydown("savePropertyOnEnter(this.value,'compErr',event);");
		
		line.add(errorMsgLabel);
		line.add(errorMsg);
		body.add(line);
		
		line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel helpMsgLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		helpMsgLabel.setValue("Help text");
		
		HtmlInputTextarea helpMsg = (HtmlInputTextarea) application.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
		helpMsg.setId("propertyHelpText");
		helpMsg.setValueBinding("value", application.createValueBinding("#{formComponent.helpMessage}"));
		helpMsg.setOnblur("saveHelpMessage(this.value)");
		helpMsg.setOnkeydown("savePropertyOnEnter(this.value,'compHelp',event);");
		
		line.add(helpMsgLabel);
		line.add(helpMsg);
		body.add(line);
		
		line = createPropertyContainer(SINGLE_LINE_PROPERTY, application);
		
		HtmlOutputLabel hasAutofillLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		hasAutofillLabel.setValue("Autofill field");
		
		HtmlSelectBooleanCheckbox hasAutofill = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		hasAutofill.setId("propertyHasAutofill");
		hasAutofill.setOnclick("toggleAutofill(this.checked);");
		hasAutofill.setValueBinding("value", application.createValueBinding("#{formComponent.autofill}"));
		
		line.add(hasAutofillLabel);
		line.add(hasAutofill);
		body.add(line);
		
		return body;
	}
	
	private UIComponent createAutofillProperties(Application application) {
		WFDivision body = createPanelSection("autoPropertiesPanel", application);
		
		WFDivision line = createPropertyContainer(SINGLE_LINE_PROPERTY, application);
		
		HtmlOutputLabel autofillLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		autofillLabel.setValue("");
		
		HtmlInputText autofillValue = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		autofillValue.setId("propertyAutofill");
		autofillValue.setValueBinding("value", application.createValueBinding("#{formComponent.autofillKey}"));
		autofillValue.setOnblur("saveAutofill(this.value);");
		autofillValue.setOnkeydown("savePropertyOnEnter(this.value,'compAuto',event);");
		
		line.add(autofillLabel);
		line.add(autofillValue);
		body.add(line);
		
		return body;
	}
	
	private UIComponent createButtonProperties(Application application) {
		WFDivision body = createPanelSection("labelPropertiesPanel", application);
		
		WFDivision line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		titleLabel.setValue("Field name");
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("propertyTitle");
		title.setValueBinding("value", application.createValueBinding("#{formComponent.label}"));
		title.setOnblur("saveComponentLabel(this.value);");
		title.setOnkeydown("savePropertyOnEnter(this.value,'compTitle',event);");
		
		line.add(titleLabel);
		line.add(title);
		body.add(line);
		
		return body;
	}
	
	private UIComponent createPlainProperties(Application application) {
		WFDivision body = createPanelSection("plainPropertiesPanel", application);
		
		WFDivision line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel plainTextLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		plainTextLabel.setValue("Text");
		
		HtmlInputTextarea plainTextValue = (HtmlInputTextarea) application.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
		plainTextValue.setId("propertyPlaintext");
		plainTextValue.setValueBinding("value", application.createValueBinding("#{formComponent.plainText}"));
		plainTextValue.setOnblur("savePlaintext(this.value);");
		plainTextValue.setOnkeydown("savePropertyOnEnter(this.value,'compText',event);");
		
		line.add(plainTextLabel);
		line.add(plainTextValue);
		body.add(line);
		
		return body;
	}
	
	private UIComponent createAdvancedProperties(Application application) {
		WFDivision body = createPanelSection("advPropertiesPanel", application);
		
		WFDivision line = createPropertyContainer(SINGLE_LINE_PROPERTY, application);
		
		HtmlOutputLabel advancedL = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		advancedL.setValue("Select source");
		
		HtmlSelectOneRadio dataSrcSwitch = (HtmlSelectOneRadio) application.createComponent(HtmlSelectOneRadio.COMPONENT_TYPE);
		dataSrcSwitch.setStyleClass("inlineRadioButton");
		dataSrcSwitch.setId("dataSrcSwitch");
		dataSrcSwitch.setOnchange("switchDataSource(this);");
		dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{formComponent.dataSrc}"));

		UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
		addChild(dataSrcs, dataSrcSwitch);
		
		line.add(advancedL);
		line.add(dataSrcSwitch);
		body.add(line);
		
		return body;
	}
	
	private UIComponent createExternalProperties(Application application) {
		WFDivision body = createPanelSection("extPropertiesPanel", application);
		
		WFDivision line = createPropertyContainer(TWO_LINE_PROPERTY, application);
		
		HtmlOutputLabel externalSrcLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		externalSrcLabel.setValue("External data source");
		
		HtmlInputText external = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		external.setId("propertyExternal");
		external.setValueBinding("value", application.createValueBinding("#{formComponent.externalSrc}"));
		external.setOnblur("saveExternalSrc(this.value);");
		external.setOnkeydown("savePropertyOnEnter(this.value,'compExt',event);");
		external.setDisabled(true);
		
		line.add(externalSrcLabel);
		line.add(external);
		body.add(line);
		
		return body;
	}
	
	private UIComponent createLocalProperties(Application application) {
		WFDivision body = createPanelSection("localPropertiesPanel", application);
		
		FBSelectValuesList selectValues = (FBSelectValuesList) application.createComponent(FBSelectValuesList.COMPONENT_TYPE);
		selectValues.setValueBinding("itemSet", application.createValueBinding("#{formComponent.items}"));
		selectValues.setId("selectOpts");
		
		body.add(selectValues);
		
		return body;
	}

	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		addFacet(BUTTON_PROPERTIES_FACET, createButtonProperties(application));
		addFacet(BASIC_PROPERTIES_FACET, createBasicProperties(application));
		addFacet(AUTOFILL_PROPERTIES_FACET, createAutofillProperties(application));
		addFacet(PLAIN_PROPERTIES_FACET, createPlainProperties(application));
		addFacet(ADVANCED_PROPERTIES_FACET, createAdvancedProperties(application));
		addFacet(EXTERNAL_PROPERTIES_FACET, createExternalProperties(application));
		addFacet(LOCAL_PROPERTIES_FACET, createLocalProperties(application));
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		super.encodeBegin(context);
		
		WFDivision plain = (WFDivision) getFacet(PLAIN_PROPERTIES_FACET);
		if(plain == null) {
			plain = (WFDivision) createPlainProperties(application);
		}
		WFDivision label = (WFDivision) getFacet(BUTTON_PROPERTIES_FACET);
		if(label == null) {
			label = (WFDivision) createButtonProperties(application);
		}
		WFDivision basic = (WFDivision) getFacet(BASIC_PROPERTIES_FACET);
		if(basic == null) {
			basic = (WFDivision) createBasicProperties(application);
		}
		WFDivision auto = (WFDivision) getFacet(AUTOFILL_PROPERTIES_FACET);
		if(auto == null) {
			auto = (WFDivision) createAutofillProperties(application);
		}
		WFDivision adv = (WFDivision) getFacet(ADVANCED_PROPERTIES_FACET);
		if(adv == null) {
			adv = (WFDivision) createAdvancedProperties(application);
		}
		WFDivision ext = (WFDivision) getFacet(EXTERNAL_PROPERTIES_FACET);
		if(ext == null) {
			ext = (WFDivision) createExternalProperties(application);
		}
		WFDivision local = (WFDivision) getFacet(LOCAL_PROPERTIES_FACET);
		if(local == null) {
			local = (WFDivision) createLocalProperties(application);
		}
		FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
		if(formComponent.getPropertiesPlain() != null) {
			plain.setStyleAttribute("display: block");
			label.setStyleAttribute("display: none");
			basic.setStyleAttribute("display: none");
			auto.setStyleAttribute("display: none");
			adv.setStyleAttribute("display: none");
			ext.setStyleAttribute("display: none");
			local.setStyleAttribute("display: none");
		} else {
			Component component = formComponent.getComponent();
			if(component == null) {
				plain.setStyleAttribute("display: none");
				basic.setStyleAttribute("display: block");
				if(formComponent.getAutofillKey() != "") {
					auto.setStyleAttribute("display: block");
				} else {
					auto.setStyleAttribute("display: none");
				}
				if(formComponent.getPropertiesSelect() != null) {
					adv.setStyleAttribute("display: block");
					if(formComponent.getDataSrc().equals("2")) {
						ext.setStyleAttribute("display: block");
						local.setStyleAttribute("display: none");
					} else {
						ext.setStyleAttribute("display: none");
						local.setStyleAttribute("display: block");
					}
				} else {
					adv.setStyleAttribute("display: none");
					ext.setStyleAttribute("display: none");
					local.setStyleAttribute("display: none");
				}
			} else {
				basic.setStyleAttribute("display: none");
				auto.setStyleAttribute("display: none");
				adv.setStyleAttribute("display: none");
				ext.setStyleAttribute("display: none");
				local.setStyleAttribute("display: none");
			}
		}
		renderChild(context, plain);
		renderChild(context, label);
		renderChild(context, basic);
		renderChild(context, auto);
		renderChild(context, adv);
		renderChild(context, ext);
		renderChild(context, local);
	}
	
}
