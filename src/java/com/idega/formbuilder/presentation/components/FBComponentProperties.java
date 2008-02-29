package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesButton;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.DataSourceList;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBComponentProperties extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	private String componentId;
	private String componentType;

	private static final String SINGLE_LINE_PROPERTY = "fbSingleLineProperty";
	private static final String TWO_LINE_PROPERTY = "fbTwoLineProperty";
	private static final String COMP_LAYER_ID = "fbComponentPropertiesPanel";
	private static final String PLAIN_PROPERTIES_PANEL = "plainPropertiesPanel";
	private static final String FB_PROPERTY_CLASS = "fbProperty";
	private static final String PROPERTY_PLAIN_TEXT_NAME = "propertyPlaintext";
	private static final String PROPERTY_LABEL_NAME = "propertyLabel";
	private static final String LABEL_PROPERTIES_PANEL = "labelPropertiesPanel";
	private static final String PROPERTY_REQUIRED_ID = "propertyRequired";
	private static final String PROPERTY_ERROR_MESSAGE_NAME = "propertyErrorMessage";
	private static final String PROPERTY_HELP_TEXT_NAME = "propertyHelpText";
	private static final String PROPERTY_AUTOFILL_CHECKBOX = "propertyHasAutofill";
	
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public FBComponentProperties() {
		this(null, null);
	}
	
	public FBComponentProperties(String componentId, String componentType) {
		super();
		this.componentId = componentId;
		this.componentType = componentType;
	}
	
	private Layer createPropertyContainer(String styleClass) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(styleClass);
		body.setStyleClass(FB_PROPERTY_CLASS);
		
		return body;
	}
	
	private Layer createPanelSection(String id) {
		Layer body = new Layer(Layer.DIV);
		body.setId(id);
		body.setStyleClass(PROPERTIES_PANEL_SECTION_STYLE);
		
		return body;
	}
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		if(componentId == null) {
			return;
		}
		
		if(componentType == null) {
			componentType = FBConstants.COMPONENT_TYPE;
		}
		
		Layer layer = new Layer(Layer.DIV);
		layer.setId(COMP_LAYER_ID);
		
		if(componentType.equals(FBConstants.COMPONENT_TYPE)) {
			
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance(FormComponent.BEAN_ID);
			formComponent.initializeBeanInstace(componentId, "component");
			
			if(formComponent.getPlainComponent() != null) {
				Layer body = createPanelSection(PLAIN_PROPERTIES_PANEL);
				Layer line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextArea plainTextValue = new TextArea(PROPERTY_PLAIN_TEXT_NAME, formComponent.getPlainText());
				
				plainTextValue.setOnBlur("savePlaintext(this.value);");
				plainTextValue.setOnKeyDown("savePropertyOnEnter(this.value,'compText',event);");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_plaintext", "Text"), plainTextValue));
				line.add(plainTextValue);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput labelValue = new TextInput(PROPERTY_LABEL_NAME, formComponent.getLabel());
				
				labelValue.setOnBlur("saveComponentLabel(this.value);");
				labelValue.setOnKeyDown("savePropertyOnEnter(this.value,'compTitle',event);");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_fieldname", "Field name"), labelValue));
				line.add(labelValue);
				body.add(line);
				
				line = createPropertyContainer(SINGLE_LINE_PROPERTY);
				
				String variableName = formComponent.getVariableName();
				TextInput processVarName = new TextInput("processVarName", variableName == null ? "" : variableName);
				
				processVarName.setOnBlur("saveComponentProcessVariableName(this.value);");
				processVarName.setOnKeyDown("savePropertyOnEnter(this.value,'compProcVar',event);");
				
				line.add(new Text("(Temporary) Process variable name:"));
				line.add(processVarName);
				body.add(line);
				
				layer.add(body);
			} else {
				Layer body = createPanelSection(LABEL_PROPERTIES_PANEL);
				Layer line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput labelValue = new TextInput(PROPERTY_LABEL_NAME, formComponent.getLabel());
				
				labelValue.setOnBlur("saveComponentLabel(this.value);");
				labelValue.setOnKeyDown("savePropertyOnEnter(this.value,'compTitle',event);");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_fieldname", "Field name"), labelValue));
				line.add(labelValue);
				body.add(line);
				
				line = createPropertyContainer(SINGLE_LINE_PROPERTY);
				
				CheckBox required = new CheckBox();
				required.setId(PROPERTY_REQUIRED_ID);
				required.setChecked(formComponent.getRequired());
				required.setOnClick("saveRequired(this.checked);");
				
				line.add(required);
				line.add(new Label(getLocalizedString(iwc, "comp_prop_requiredfield", "Required field"), required));
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextArea errorMsg = new TextArea(PROPERTY_ERROR_MESSAGE_NAME, formComponent.getErrorMessage());
				errorMsg.setOnBlur("saveErrorMessage(this.value)");
				errorMsg.setOnKeyDown("savePropertyOnEnter(this.value,'compErr',event);");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_errormsg", "Error message"), errorMsg));
				line.add(errorMsg);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextArea helpMsg = new TextArea(PROPERTY_HELP_TEXT_NAME, formComponent.getHelpMessage());
				helpMsg.setOnBlur("saveHelpMessage(this.value)");
				helpMsg.setOnKeyDown("savePropertyOnEnter(this.value,'compHelp',event);");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_helpmsg", "Help text"), helpMsg));
				line.add(helpMsg);
				body.add(line);
				
				line = createPropertyContainer(SINGLE_LINE_PROPERTY);
				
				String variableName = formComponent.getVariableName();
				TextInput processVarName = new TextInput("processVarName", variableName == null ? "" : variableName);
				
				processVarName.setOnBlur("saveComponentProcessVariableName(this.value);");
				processVarName.setOnKeyDown("savePropertyOnEnter(this.value,'compProcVar',event);");
				
				line.add(new Text("(Temporary) Process variable name:"));
				line.add(processVarName);
				body.add(line);
				
				line = createPropertyContainer(SINGLE_LINE_PROPERTY);
				
				CheckBox hasAutoFill = new CheckBox();
				hasAutoFill.setId(PROPERTY_AUTOFILL_CHECKBOX);
				hasAutoFill.setOnClick("toggleAutofill(this.checked);");
				String autofillKey = formComponent.getAutofillKey();
				hasAutoFill.setChecked(autofillKey != null ? true : false);
				
				line.add(hasAutoFill);
				line.add(new Label(getLocalizedString(iwc, "comp_prop_autofill", "Autofill field"), hasAutoFill));
				body.add(line);
				
				layer.add(body);
				
				Layer body2 = createPanelSection("autoPropertiesPanel");
				
				Layer line2 = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
				
				TextInput autofillValue = new TextInput();
				autofillValue.setValue(autofillKey);
				autofillValue.setId("propertyAutofill");
				autofillValue.setOnBlur("saveAutofill(this.value);");
				autofillValue.setOnKeyDown("savePropertyOnEnter(this.value,'compAuto',event);");
				if(autofillKey != null)
					autofillValue.setStyleClass("activeAutofill");
				
				line2.add(new Text(""));
				line2.add(autofillValue);
				body2.add(line2);
				
				layer.add(body2);
				
				if(formComponent.getSelectComponent() != null) {
					Layer body3 = createPanelSection("advPropertiesPanel");
					
					Layer line3 = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
					
					line3.add(new Text(getLocalizedString(iwc, "comp_prop_select_source", "Select source")));
					body3.add(line3);
					
					line3 = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
					
					boolean localDataSource = formComponent.getDataSrc().equals(DataSourceList.localDataSrc) ? true : false;
					
					RadioGroup dataSrcSwitch = new RadioGroup("dataSrcSwitch");
					dataSrcSwitch.setStyleClass("inlineRadioButton");
					RadioButton lcl = new RadioButton(DataSourceList.localDataSrc, DataSourceList.localDataSrc);
					RadioButton ext = new RadioButton(DataSourceList.externalDataSrc, DataSourceList.externalDataSrc);
					dataSrcSwitch.addRadioButton(lcl, new Text(getLocalizedString(iwc, "comp_prop_valuelist", "List of values")));
					dataSrcSwitch.addRadioButton(ext, new Text(getLocalizedString(iwc, "comp_prop_external", "External")));
					if(localDataSource) {
						lcl.setSelected(true);
						ext.setSelected(false);
					} else {
						ext.setSelected(true);
						lcl.setSelected(false);
					}
					lcl.setOnChange("switchDataSource();");
					ext.setOnChange("switchDataSource();");
					
					line3.add(dataSrcSwitch);
					body3.add(line3);
					
					layer.add(body3);
					
					if(localDataSource) {
						Layer localBody = createPanelSection("localPropertiesPanel");
						
						FBSelectValuesList selectValues = new FBSelectValuesList();
						selectValues.setId("selectOpts");
						
						localBody.add(selectValues);
						
						layer.add(localBody);
					} else {
						Layer externalBody = createPanelSection("extPropertiesPanel");
						
						Layer externalline = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
						
						SelectDropdown select = new SelectDropdown();
						select.setId("propertyExternal");
						select.setOnChange("saveExternalSrc(this.value);");
						List<SelectOption> options = ((DataSourceList) WFUtil.getBeanInstance("dataSources")).getExternalDataSources();
						for(Iterator<SelectOption> it = options.iterator(); it.hasNext(); ) { 
							select.addOption(it.next());
						}
						select.setSelectedOption(formComponent.getExternalSrc());
						
						externalline.add(new Text(getLocalizedString(iwc, "comp_prop_externaldata", "External data")));
						externalline.add(select);
						externalBody.add(externalline);
						
						layer.add(externalBody);
					}
				}
			}
		} else if(componentType.equals(FBConstants.BUTTON_TYPE)) {
			FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
			Page page = formPage.getPage();
			if(page == null)
				return;
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button)area.getComponent(componentId);
				if(button != null) {
					PropertiesButton properties = button.getProperties();
					
					Layer body = createPanelSection("labelPropertiesPanel");
					
					Layer line = createPropertyContainer(FBComponentProperties.TWO_LINE_PROPERTY);
					
					TextInput title = new TextInput("propertyTitle", properties.getLabel().getString(new Locale("en")));
					title.setOnBlur("saveButtonLabel(this.value);");
					title.setOnKeyDown("savePropertyOnEnter(this.value,'btnTitle',event);");
					
					line.add(new Label(getLocalizedString(iwc, "comp_prop_buttonlabel", "Button label"), title));
					line.add(title);
					body.add(line);
					
					line = createPropertyContainer(FBComponentProperties.TWO_LINE_PROPERTY);
					
					title = new TextInput("propertyAction", properties.getReferAction());
					title.setOnBlur("saveButtonAction(this.value);");
					title.setOnKeyDown("savePropertyOnEnter(this.value,'btnAction',event);");
					
					line.add(new Label("Button action", title));
					line.add(title);
					body.add(line);
					
					layer.add(body);
				}
			}
		}
		
		add(layer);
	}
	
	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	
}
