package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;
import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;

import com.idega.documentmanager.business.form.Component;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.webface.WFUtil;

public class FBComponentPropertiesPanel extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private static final String BUTTON_PROPERTIES_FACET = "BUTTON_PROPERTIES_FACET";
	private static final String BASIC_PROPERTIES_FACET = "BASIC_PROPERTIES_FACET";
	private static final String ADVANCED_PROPERTIES_FACET = "ADVANCED_PROPERTIES_FACET";
	private static final String EXTERNAL_PROPERTIES_FACET = "EXTERNAL_PROPERTIES_FACET";
	private static final String AUTOFILL_PROPERTIES_FACET = "AUTOFILL_PROPERTIES_FACET";
	private static final String LOCAL_PROPERTIES_FACET = "LOCAL_PROPERTIES_FACET";
	
	public FBComponentPropertiesPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
//		Label property for all components and buttons
		
		Table2 table = new Table2();
		table.setId("labelPropertiesPanel");
		table.setStyleAttribute("width: 250px;");
		table.setCellpadding(0);
		TableRowGroup group = table.createBodyRowGroup();
		TableRow row = null;
		TableCell2 cell = null;
		
		HtmlOutputText titleLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		titleLabel.setValue("Field name");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("propertyTitle");
		title.setValueBinding("value", application.createValueBinding("#{formComponent.label}"));
		title.setOnblur("saveComponentLabel(this.value);");
		title.setOnkeydown("savePropertyOnEnter(this.value,'compTitle',event);");
		
		cell = row.createCell();
		cell.add(title);
		
		addFacet(BUTTON_PROPERTIES_FACET, table);
		
//		Basic propertis for all components
		
		table = new Table2();
		table.setId("basicPropertiesPanel");
		table.setStyleAttribute("width: 250px;");
		table.setCellpadding(0);
		group = table.createBodyRowGroup();
		row = null;
		cell = null;
		
		HtmlOutputText requiredLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		requiredLabel.setValue("Required field");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(requiredLabel);
		
		HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		required.setId("propertyRequired");
		required.setValueBinding("value", application.createValueBinding("#{formComponent.required}"));
		required.setOnclick("saveRequired(this.checked);");
		
		cell = row.createCell();
		cell.add(required);
		
		HtmlOutputText errorMsgLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		errorMsgLabel.setValue("Error message");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(errorMsgLabel);
		
		HtmlInputText errorMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		errorMsg.setId("propertyErrorMessage");
		errorMsg.setValueBinding("value", application.createValueBinding("#{formComponent.errorMessage}"));
		errorMsg.setOnblur("saveErrorMessage(this.value)");
		errorMsg.setOnkeydown("savePropertyOnEnter(this.value,'compErr',event);");
		
		cell = row.createCell();
		cell.add(errorMsg);
		
//		HtmlOutputText helpMsgLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
//		helpMsgLabel.setValue("Help text");
//		
//		row = group.createRow();
//		cell = row.createCell();
//		cell.add(helpMsgLabel);
//		
//		HtmlInputText helpMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
//		helpMsg.setId("propertyHelpText");
//		helpMsg.setValueBinding("value", application.createValueBinding("#{formComponent.helpMessage}"));
//		helpMsg.setOnblur("saveHelpMessage(this.value)");
//		helpMsg.setOnkeydown("savePropertyOnEnter(this.value,'compHelp',event);");
//		
//		cell = row.createCell();
//		cell.add(helpMsg);
		
		HtmlOutputText hasAutofillLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		hasAutofillLabel.setValue("Autofill field");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(hasAutofillLabel);
		
		HtmlSelectBooleanCheckbox hasAutofill = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		hasAutofill.setId("propertyHasAutofill");
		hasAutofill.setOnclick("toggleAutofill(this.checked);");
		hasAutofill.setValueBinding("value", application.createValueBinding("#{formComponent.autofill}"));
		
		cell = row.createCell();
		cell.add(hasAutofill);
		
		addFacet(BASIC_PROPERTIES_FACET, table);
		
		table = new Table2();
		table.setId("autoPropertiesPanel");
		table.setStyleAttribute("width: 250px;");
		table.setCellpadding(0);
		group = table.createBodyRowGroup();
		row = null;
		cell = null;
		
		HtmlOutputText autofillLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		autofillLabel.setValue("");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(autofillLabel);
		
		HtmlInputText autofillValue = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		autofillValue.setId("propertyAutofill");
		autofillValue.setValueBinding("value", application.createValueBinding("#{formComponent.autofillKey}"));
		autofillValue.setOnblur("saveAutofill(this.value);");
		autofillValue.setOnkeydown("savePropertyOnEnter(this.value,'compAuto',event);");
		
		cell = row.createCell();
		cell.add(autofillValue);
		
		addFacet(AUTOFILL_PROPERTIES_FACET, table);
		
		table = new Table2();
		table.setId("advPropertiesPanel");
		table.setStyleAttribute("width: 250px;");
		table.setCellpadding(0);
		group = table.createBodyRowGroup();
		row = null;
		cell = null;
		
		HtmlOutputText emptyLabelLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emptyLabelLabel.setValue("Empty item label");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(emptyLabelLabel);
		
		HtmlInputText emptyLabel = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		emptyLabel.setId("propertyEmptyLabel");
		emptyLabel.setValueBinding("value", application.createValueBinding("#{formComponent.emptyLabel}"));
		emptyLabel.setOnblur("saveEmptyLabel(this.value);");
		emptyLabel.setOnkeydown("savePropertyOnEnter(this.value,'compEmpty',event);");
		
		cell = row.createCell();
		cell.add(emptyLabel);
		
		HtmlOutputText advancedL = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		advancedL.setValue("Select source");

		row = group.createRow();
		cell = row.createCell();
		cell.add(advancedL);
		
		HtmlSelectOneRadio dataSrcSwitch = (HtmlSelectOneRadio) application.createComponent(HtmlSelectOneRadio.COMPONENT_TYPE);
		dataSrcSwitch.setStyleClass("inlineRadioButton");
		dataSrcSwitch.setId("dataSrcSwitch");
		dataSrcSwitch.setOnchange("switchDataSource(this);");
		dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{formComponent.dataSrc}"));

		UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
		addChild(dataSrcs, dataSrcSwitch);
		
		cell = row.createCell();
		cell.add(dataSrcSwitch);
		
		addFacet(ADVANCED_PROPERTIES_FACET, table);
		
		table = new Table2();
		table.setId("extPropertiesPanel");
		table.setStyleAttribute("width: 250px;");
		table.setCellpadding(0);
		group = table.createBodyRowGroup();
		row = null;
		cell = null;
		
		HtmlOutputText externalSrcLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		externalSrcLabel.setValue("External data source");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(externalSrcLabel);
		
		HtmlInputText external = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		external.setId("propertyExternal");
		external.setValueBinding("value", application.createValueBinding("#{formComponent.externalSrc}"));
		external.setOnblur("saveExternalSrc(this.value);");
		external.setOnkeydown("savePropertyOnEnter(this.value,'compExt',event);");
		
		cell = row.createCell();
		cell.add(external);
		
		addFacet(EXTERNAL_PROPERTIES_FACET, table);
		
		table = new Table2();
		table.setId("localPropertiesPanel");
		table.setStyleAttribute("width: 250px;");
		table.setCellpadding(0);
		group = table.createBodyRowGroup();
		row = null;
		cell = null;
		
		FBSelectValuesList selectValues = (FBSelectValuesList) application.createComponent(FBSelectValuesList.COMPONENT_TYPE);
		selectValues.setValueBinding("itemSet", application.createValueBinding("#{formComponent.items}"));
		selectValues.setId("selectOpts");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(selectValues);
		
		addFacet(LOCAL_PROPERTIES_FACET, table);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		Table2 label = (Table2) getFacet(BUTTON_PROPERTIES_FACET);
		if(label != null) {
			renderChild(context, label);
		}
		Table2 basic = (Table2) getFacet(BASIC_PROPERTIES_FACET);
		Table2 auto = (Table2) getFacet(AUTOFILL_PROPERTIES_FACET);
		Table2 adv = (Table2) getFacet(ADVANCED_PROPERTIES_FACET);
		Table2 ext = (Table2) getFacet(EXTERNAL_PROPERTIES_FACET);
		Table2 local = (Table2) getFacet(LOCAL_PROPERTIES_FACET);
		FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
		Component component = formComponent.getComponent();
		if(component == null) {
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
		renderChild(context, basic);
		renderChild(context, auto);
		renderChild(context, adv);
		renderChild(context, ext);
		renderChild(context, local);
	}
	
}
