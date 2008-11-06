package com.idega.formbuilder.presentation.components;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.DataSourceList;
import com.idega.formbuilder.presentation.beans.FormButton;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.presentation.beans.FormMultiUploadComponent;
import com.idega.formbuilder.presentation.beans.FormPlainComponent;
import com.idega.formbuilder.presentation.beans.FormSelectComponent;
import com.idega.formbuilder.presentation.beans.GenericComponent;
import com.idega.formbuilder.util.FBUtil;
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
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.ComponentPlain;
import com.idega.xformsmanager.business.component.ComponentSelect;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.business.component.properties.PropertiesPlain;

public class FBComponentProperties extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	private String componentId;
	private String componentType;
	private GenericComponent component;

	private static final String SINGLE_LINE_PROPERTY = "fbSingleLineProperty";
	private static final String TWO_LINE_PROPERTY = "fbTwoLineProperty";
	private static final String COMP_LAYER_ID = "fbComponentPropertiesPanel";
	private static final String PLAIN_PROPERTIES_PANEL = "plainPropertiesPanel";
	private static final String FB_PROPERTY_CLASS = "fbProperty";
	private static final String PROPERTY_PLAIN_TEXT_NAME = "propertyPlaintext";
	private static final String PROPERTY_LABEL_NAME = "propertyLabel";
	private static final String LABEL_PROPERTIES_PANEL = "labelPropertiesPanel";
	private static final String PROPERTY_REQUIRED_ID = "propertyRequired";
	
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
	
	public FBComponentProperties(GenericComponent comp) {
		this.component = comp;
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
		if(component == null) {
			return;
		}
		
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer layer = new Layer(Layer.DIV);
		layer.setId(COMP_LAYER_ID);
		
		Locale locale = FBUtil.getUILocale();
		
		if(component instanceof FormPlainComponent) {
			ComponentPlain comp = (ComponentPlain) component.getComponent();
			PropertiesPlain properties = comp.getProperties();
			
			if(properties.getText() != null) {
				String componentId = comp.getId();
				
				Layer body = createPanelSection(PLAIN_PROPERTIES_PANEL);
				Layer line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextArea plainTextValue = new TextArea(PROPERTY_PLAIN_TEXT_NAME, properties.getText().getString(locale));
				plainTextValue.setOnBlur("saveComponentProperty('" + componentId + "','plainText',this.value, event)");
				plainTextValue.setOnKeyDown("saveComponentProperty('" + componentId + "','plainText',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_plaintext", "Text"), plainTextValue));
				line.add(plainTextValue);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput labelValue = new TextInput(PROPERTY_LABEL_NAME, properties.getLabel() == null ? "" : properties.getLabel().getString(locale));
				labelValue.setOnBlur("saveComponentProperty('" + componentId + "','plainLabel',this.value, event)");
				labelValue.setOnKeyDown("saveComponentProperty('" + componentId + "','plainLabel',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_fieldname", "Field name"), labelValue));
				line.add(labelValue);
				body.add(line);
				
				layer.add(body);
			}
			
		} else if(component instanceof FormComponent) {
			Component comp = component.getComponent();
			PropertiesComponent properties = comp.getProperties();
			
			Layer body = createPanelSection(LABEL_PROPERTIES_PANEL);
			Layer line = createPropertyContainer(TWO_LINE_PROPERTY);
			
			TextInput labelValue = new TextInput(PROPERTY_LABEL_NAME, properties.getLabel().getString(locale));
			
			String componentId = comp.getId();
			
			labelValue.setOnBlur("saveComponentProperty('" + componentId + "','compLabel',this.value, event)");
			labelValue.setOnKeyDown("saveComponentProperty('" + componentId + "','compLabel',this.value, event)");
			
			line.add(new Label(getLocalizedString(iwc, "comp_prop_fieldname", "Field name"), labelValue));
			line.add(labelValue);
			body.add(line);
			
// 			TODO better... 
			com.idega.xformsmanager.component.FormComponent docComponentent = (com.idega.xformsmanager.component.FormComponent) comp;
			
			if (docComponentent.getType().equals("fbc_text_output") || docComponentent.getType().equals("xf:output")) {
			    
			    layer.add(body);
			    
			    line = createPropertyContainer(SINGLE_LINE_PROPERTY);
			
			    CheckBox hasAutoFill = new CheckBox();
			    hasAutoFill.setId(PROPERTY_AUTOFILL_CHECKBOX);
			    hasAutoFill.setOnClick("toggleAutofill(this.checked);");
			    String autofillKey = properties.getAutofillKey();
			    hasAutoFill.setChecked(autofillKey != null ? true : false);
				
			    line.add(hasAutoFill);
			    line.add(new Label(getLocalizedString(iwc, "comp_prop_autofill", "Autofill field"), hasAutoFill));
			    body.add(line);
				
				
			    body = createPanelSection("autoPropertiesPanel");
				
			    line = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
				
			    TextInput autofillValue = new TextInput();
			    autofillValue.setValue(autofillKey);
			    autofillValue.setId("propertyAutofill");
			    autofillValue.setOnBlur("saveComponentProperty('" + componentId + "','compAuto',this.value, event)");
			    autofillValue.setOnKeyDown("saveComponentProperty('" + componentId + "','compAuto',this.value, event)");
				
			    if(autofillKey != null)
				autofillValue.setStyleClass("activeAutofill");
				
			    line.add(new Text(CoreConstants.EMPTY));
			    line.add(autofillValue);
			    body.add(line);
			    
			   layer.add(body);
			   
			   add(layer);
			   return;
			    
			}
			
			/* TODO: cleanup after this
			if(component instanceof FormMultiUploadComponent) {
				layer.add(body);
				
				ComponentMultiUpload compMultiUpload = (ComponentMultiUpload) comp;
				
				body = createPanelSection("multiUpPropertiesPanel");
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput addButtonLabel = new TextInput("propertyAddButtonLabel", compMultiUpload.getProperties().getAddButtonLabel().getString(locale));
				addButtonLabel.setOnBlur("saveComponentProperty('" + componentId + "','compAddButton',this.value, event)");
				addButtonLabel.setOnKeyDown("saveComponentProperty('" + componentId + "','compAddButton',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_add_button_label", "Add button label"), addButtonLabel));
				line.add(addButtonLabel);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput removeButtonLabel = new TextInput("propertyRemoveButtonLabel", compMultiUpload.getProperties().getRemoveButtonLabel().getString(locale));
				removeButtonLabel.setOnBlur("saveComponentProperty('" + componentId + "','compRemoveButton',this.value, event)");
				removeButtonLabel.setOnKeyDown("saveComponentProperty('" + componentId + "','compRemoveButton',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_remove_button_label", "Remove button label"), removeButtonLabel));
				line.add(removeButtonLabel);
				body.add(line);
				
				layer.add(body);
				
				add(layer);
				
				return;
			}
			*/
			
			if(component instanceof FormMultiUploadComponent) {
				
				layer.add(body);
				
				body = createPanelSection("multiUpPropertiesPanel");
				
				line = createPropertyContainer(SINGLE_LINE_PROPERTY);
				
				CheckBox required = new CheckBox();
				required.setId(PROPERTY_REQUIRED_ID);
				required.setChecked(properties.isRequired());
				required.setOnChange("saveComponentProperty('" + componentId + "','compRequired',this.checked, event)");
				
				line.add(required);
				line.add(new Label(getLocalizedString(iwc, "comp_prop_requiredfield", "Required field"), required));
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput addButtonLabel = new TextInput("propertyAddButtonLabel", component.getAddButtonLabel());
				addButtonLabel.setOnBlur("saveComponentProperty('" + componentId + "','compAddButton',this.value, event)");
				addButtonLabel.setOnKeyDown("saveComponentProperty('" + componentId + "','compAddButton',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_add_button_label", "Add button label"), addButtonLabel));
				line.add(addButtonLabel);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput removeButtonLabel = new TextInput("propertyRemoveButtonLabel", component.getRemoveButtonLabel());
				removeButtonLabel.setOnBlur("saveComponentProperty('" + componentId + "','compRemoveButton',this.value, event)");
				removeButtonLabel.setOnKeyDown("saveComponentProperty('" + componentId + "','compRemoveButton',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_remove_button_label", "Remove button label"), removeButtonLabel));
				line.add(removeButtonLabel);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput descriptionLabel = new TextInput("propertyDescriptionLabel", component.getDescriptionLabel());
				
				String updateAction = "saveComponentProperty('" + componentId + "','uploadDescLbl',this.value, event)";
				descriptionLabel.setOnBlur(updateAction);
				descriptionLabel.setOnKeyDown(updateAction);
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_upload_descriptive_name", "Descriptive name"), descriptionLabel));
				line.add(descriptionLabel);
				body.add(line);
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
				
				TextInput uploadDescriptionLabel = new TextInput("propertyUploadDescription", component.getUploadDescription());
				uploadDescriptionLabel.setOnBlur("saveComponentProperty('" + componentId + "','uploadDesc',this.value, event)");
				uploadDescriptionLabel.setOnKeyDown("saveComponentProperty('" + componentId + "','uploadDesc',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_upload_description", "Upload description label"), uploadDescriptionLabel));
				line.add(uploadDescriptionLabel);
				body.add(line);
				
				layer.add(body);
				
				add(layer);
				
				return;
				
			}
			
			line = createPropertyContainer(SINGLE_LINE_PROPERTY);
			
			CheckBox required = new CheckBox();
			required.setId(PROPERTY_REQUIRED_ID);
			required.setChecked(properties.isRequired());
			required.setOnChange("saveComponentProperty('" + componentId + "','compRequired',this.checked, event);");
//			required.setOnClick("toggleValidationText(this.checked);");
			
			line.add(required);
			line.add(new Label(getLocalizedString(iwc, "comp_prop_requiredfield", "Required field"), required));
			body.add(line);
//			validation text
			
/*
			    line = createPropertyContainer(TWO_LINE_PROPERTY);
				
			    TextArea validationText = new TextArea(PROPERTY_VALIDATION_TEXT, properties.getValidationText().getString(locale));
			    
			    validationText.setId("propertyValidationText");
			    
			    validationText.setOnBlur("saveComponentProperty('" + componentId + "','compValidation',this.value, event)");
			    validationText.setOnKeyDown("saveComponentProperty('" + componentId + "','compValidation',this.value, event)");
			    
			    Label validationLabel = new Label(getLocalizedString(iwc, "comp_prop_validation", "Validation text"), validationText);
			    validationLabel.setID("propertyValidationLabel");
			    
			    if (properties.isRequired()){
				validationText.setStyleClass("activeValidationText");
				validationLabel.setStyleClass("activeValidationLabel");
			    }
			    
			    line.add(validationLabel);
			    line.add(validationText);
			    body.add(line);
*/
			
			Collection<ErrorType> errors = properties.getExistingErrors();
			
			for (ErrorType errorType : errors) {
				
				line = createPropertyContainer(TWO_LINE_PROPERTY);
		
				TextArea errorMsg = new TextArea();
				errorMsg.setContent(properties.getErrorMsg(errorType).getString(locale));
				errorMsg.setOnBlur("saveComponentErrorMessage('"+errorType+"', this.value, event)");
				errorMsg.setOnKeyDown("saveComponentErrorMessage('"+errorType+"',this.value, event)");
				
				line.add(new Label(getErrorTypeLabel(iwc, errorType, locale), errorMsg));
				line.add(errorMsg);
				body.add(line);
			}
			
			line = createPropertyContainer(TWO_LINE_PROPERTY);
			
			TextArea helpMsg = new TextArea(PROPERTY_HELP_TEXT_NAME, properties.getHelpText().getString(locale));
			helpMsg.setOnBlur("saveComponentProperty('" + componentId + "','compHelp',this.value, event)");
			helpMsg.setOnKeyDown("saveComponentProperty('" + componentId + "','compHelp',this.value, event)");
			
			line.add(new Label(getLocalizedString(iwc, "comp_prop_helpmsg", "Help text"), helpMsg));
			line.add(helpMsg);
			body.add(line);
			
			line = createPropertyContainer(SINGLE_LINE_PROPERTY);
			
			CheckBox hasAutoFill = new CheckBox();
			hasAutoFill.setId(PROPERTY_AUTOFILL_CHECKBOX);
			hasAutoFill.setOnClick("toggleAutofill(this.checked);");
			String autofillKey = properties.getAutofillKey();
			hasAutoFill.setChecked(autofillKey != null ? true : false);
			
			line.add(hasAutoFill);
			line.add(new Label(getLocalizedString(iwc, "comp_prop_autofill", "Autofill field"), hasAutoFill));
			body.add(line);
			
			layer.add(body);
			
			body = createPanelSection("autoPropertiesPanel");
			
			line = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
			
			TextInput autofillValue = new TextInput();
			autofillValue.setValue(autofillKey);
			autofillValue.setId("propertyAutofill");
			autofillValue.setOnBlur("saveComponentProperty('" + componentId + "','compAuto',this.value, event)");
			autofillValue.setOnKeyDown("saveComponentProperty('" + componentId + "','compAuto',this.value, event)");
			
			if(autofillKey != null)
				autofillValue.setStyleClass("activeAutofill");
			
			line.add(new Text(CoreConstants.EMPTY));
			line.add(autofillValue);
			body.add(line);
			
			layer.add(body);
			
			if(component instanceof FormSelectComponent) {
				ComponentSelect compSelect = (ComponentSelect) comp;
				
				body = createPanelSection("advPropertiesPanel");
				
				line = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
				
				RadioGroup dataSrcSwitch = new RadioGroup("dataSrcSwitch");
				
				line.add(new Label(new Text(getLocalizedString(iwc, "comp_prop_select_source", "Select source")), dataSrcSwitch));
				body.add(line);
				
				line = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
				
				String dataSrc;
				if(compSelect.getProperties().getDataSrcUsed() != null) {
					dataSrc = compSelect.getProperties().getDataSrcUsed().toString();
				} else {
					dataSrc = DataSourceList.localDataSrc;
				}
				
				boolean localDataSource = dataSrc.equals(DataSourceList.localDataSrc) ? true : false;
				
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
				
				line.add(dataSrcSwitch);
				body.add(line);
				
				layer.add(body);
				
				SelectDropdown select = new SelectDropdown();
				
				if(localDataSource) {
					Layer localBody = createPanelSection("localPropertiesPanel");
					
					FBSelectValuesList selectValues = new FBSelectValuesList((FormComponent) component);
					
					localBody.add(new Label(new Text(getLocalizedString(iwc, "comp_prop_itemslist", "Options")), select));
					localBody.add(selectValues);
					
					layer.add(localBody);
				} else {
					Layer externalBody = createPanelSection("extPropertiesPanel");
					
					Layer externalline = createPropertyContainer(FBComponentProperties.SINGLE_LINE_PROPERTY);
					
					
					select.setId("propertyExternal");
					select.setOnChange("saveComponentProperty('" + componentId + "','externalSrc',this.value, event)");
					List<SelectOption> options = ((DataSourceList) WFUtil.getBeanInstance("dataSources")).getExternalDataSources();
					for(Iterator<SelectOption> it = options.iterator(); it.hasNext(); ) { 
						select.addOption(it.next());
					}
					select.setSelectedOption(compSelect.getProperties().getExternalDataSrc());
					
					externalline.add(new Label(new Text(getLocalizedString(iwc, "comp_prop_externaldata", "External data")), select));
					externalline.add(select);
					externalBody.add(externalline);
					
					layer.add(externalBody);
				}
			}
			
		} else if(component instanceof FormButton) {
			Button button = (Button) component.getComponent();
			if(button != null) {
				PropertiesButton properties = button.getProperties();
				
				Layer body = createPanelSection("labelPropertiesPanel");
				
				Layer line = createPropertyContainer(FBComponentProperties.TWO_LINE_PROPERTY);
				
				TextInput title = new TextInput("propertyTitle", properties.getLabel().getString(locale));
				title.setOnBlur("saveComponentProperty('" + button.getId() + "','buttonLabel',this.value, event)");
				title.setOnKeyDown("saveComponentProperty('" + button.getId() + "','buttonLabel',this.value, event)");
				
				line.add(new Label(getLocalizedString(iwc, "comp_prop_buttonlabel", "Button label"), title));
				line.add(title);
				body.add(line);
				
//				line = createPropertyContainer(FBComponentProperties.TWO_LINE_PROPERTY);
//				
//				title = new TextInput("propertyAction", properties.getReferAction());
//				title.setOnBlur("saveButtonAction(this.value);");
//				title.setOnKeyDown("savePropertyOnEnter(this.value,'btnAction',event);");
//				
//				line.add(new Label("Button action", title));
//				line.add(title);
//				body.add(line);
				
				layer.add(body);
			}
		}
		
		add(layer);
	}
	
	private String getErrorTypeLabel(IWContext iwc, ErrorType errorType, Locale locale) {
		
		return "Error "+errorType;
	}
	
	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public GenericComponent getComponent() {
		return component;
	}

	public void setComponent(GenericComponent component) {
		this.component = component;
	}
	
}
