package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.DataSourceList;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.util.FBConstants;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBComponentPropertiesPanel extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	private String componentId;
	private String componentType;
	
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public FBComponentPropertiesPanel() {
		super();
		setRendererType(null);
	}
	
	public FBComponentPropertiesPanel(String componentId, String componentType) {
		super();
		setRendererType(null);
		this.componentId = componentId;
		this.componentType = componentType;
	}
	
	private Layer createPropertyContainer(String styleClass) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(styleClass);
		
		return body;
	}
	
	private Layer createPanelSection(String id) {
		Layer body = new Layer(Layer.DIV);
		body.setId(id);
		body.setStyleClass(PROPERTIES_PANEL_SECTION_STYLE);
		
		return body;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		if(componentId == null) {
			return;
		}
		
		if(componentType == null) {
			componentType = FBConstants.COMPONENT_TYPE;
		}
		
		Layer layer = new Layer(Layer.DIV);
		
		if(componentType.equals(FBConstants.COMPONENT_TYPE)) {
			
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			formComponent.initializeBeanInstace(componentId);
			
			if(formComponent.getPlainComponent() != null) {
				Layer body = createPanelSection("plainPropertiesPanel");
				Layer line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
				
				TextArea plainTextValue = new TextArea("propertyPlaintext", formComponent.getPlainText());
				
				plainTextValue.setOnBlur("savePlaintext(this.value);");
				plainTextValue.setOnKeyDown("savePropertyOnEnter(this.value,'compText',event);");
				
				line.add(new Text("Text"));
				line.add(plainTextValue);
				body.add(line);
				
				layer.add(body);
			} else {
//				PropertiesComponent properties = component.getProperties();
				
				Layer body = createPanelSection("labelPropertiesPanel");
				Layer line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
				
				TextInput labelValue = new TextInput("propertyLabel", formComponent.getLabel());
				
				labelValue.setOnBlur("saveComponentLabel(this.value);");
				labelValue.setOnKeyDown("savePropertyOnEnter(this.value,'compTitle',event);");
				
				line.add(new Text("Field name"));
				line.add(labelValue);
				body.add(line);
				
				line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
				
				CheckBox required = new CheckBox();
				required.setId("propertyRequired");
				required.setChecked(formComponent.getRequired());
				required.setOnClick("saveRequired(this.checked);");
				
				line.add(new Text("Required field"));
				line.add(required);
				body.add(line);
				
				line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
				
				TextArea errorMsg = new TextArea("propertyErrorMessage", formComponent.getErrorMessage());
				errorMsg.setOnBlur("saveErrorMessage(this.value)");
				errorMsg.setOnKeyDown("savePropertyOnEnter(this.value,'compErr',event);");
				
				line.add(new Text("Error message"));
				line.add(errorMsg);
				body.add(line);
				
				line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
				
				TextArea helpMsg = new TextArea("propertyHelpText", formComponent.getHelpMessage());
				helpMsg.setOnBlur("saveHelpMessage(this.value)");
				helpMsg.setOnKeyDown("savePropertyOnEnter(this.value,'compHelp',event);");
				
				line.add(new Text("Help text"));
				line.add(helpMsg);
				body.add(line);
				
				line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
				
				TextInput processVarName = new TextInput("processVarName");
				
				processVarName.setOnBlur("saveComponentProcessVariableName(this.value);");
				processVarName.setOnKeyDown("savePropertyOnEnter(this.value,'compProcVar',event);");
				
				line.add(new Text("(Temporary) Process variable name:"));
				line.add(processVarName);
				body.add(line);
				
				line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
				
				CheckBox hasAutoFill = new CheckBox();
				hasAutoFill.setId("propertyHasAutofill");
				hasAutoFill.setOnClick("toggleAutofill(this.checked);");
				hasAutoFill.setChecked(formComponent.getAutofillKey() != "" ? true : false);
				
				line.add(new Text("Autofill field"));
				line.add(hasAutoFill);
				body.add(line);
				
				layer.add(body);
				
				if(formComponent.getAutofillKey() != "") {
					Layer body2 = createPanelSection("autoPropertiesPanel");
					
					Layer line2 = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
					
					TextInput autofillValue = new TextInput();
					autofillValue.setValue(formComponent.getAutofillKey());
					autofillValue.setId("propertyAutofill");
					autofillValue.setOnBlur("saveAutofill(this.value);");
					autofillValue.setOnKeyDown("savePropertyOnEnter(this.value,'compAuto',event);");
					
					line2.add(new Text(""));
					line2.add(autofillValue);
					body2.add(line2);
					
					layer.add(body2);
				}
				
				if(formComponent.getSelectComponent() != null) {
//					ComponentSelect selectComponent = (ComponentSelect) component;
//					PropertiesSelect propertiesSelect = selectComponent.getProperties();
					
					Layer body3 = createPanelSection("advPropertiesPanel");
					
					Layer line3 = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
					
					boolean localDataSource = formComponent.getDataSrc().equals(DataSourceList.localDataSrc) ? true : false;
					
					RadioGroup dataSrcSwitch = new RadioGroup("dataSrcSwitch");
					dataSrcSwitch.setStyleClass("inlineRadioButton");
					dataSrcSwitch.setOnChange("switchDataSource(this);");
					dataSrcSwitch.addRadioButton(DataSourceList.localDataSrc, new Text("List of value"), localDataSource);
					dataSrcSwitch.addRadioButton(DataSourceList.externalDataSrc, new Text("External"), !localDataSource);
					
					line3.add(new Text("Select source"));
					line3.add(dataSrcSwitch);
					body3.add(line3);
					
					layer.add(body3);
					
					if(localDataSource) {
						Layer localBody = createPanelSection("localPropertiesPanel");
						
						FBSelectValuesList selectValues = (FBSelectValuesList) application.createComponent(FBSelectValuesList.COMPONENT_TYPE);
						selectValues.setValueBinding("itemSet", application.createValueBinding("#{formComponent.items}"));
						selectValues.setId("selectOpts");
						
						localBody.add(selectValues);
						
						layer.add(localBody);
					} else {
						Layer externalBody = createPanelSection("extPropertiesPanel");
						
						Layer externalline = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
						
						SelectDropdown select = new SelectDropdown();
						select.setId("propertyExternal");
						select.setOnChange("saveExternalSrc(this.value);");
						List<SelectOption> options = ((DataSourceList) WFUtil.getBeanInstance("dataSources")).getExternalDataSources();
						for(Iterator it = options.iterator(); it.hasNext(); ) { 
							select.addOption((SelectOption) it.next());
						}
						select.setSelectedOption(formComponent.getExternalSrc());
						
						externalline.add(new Text("External data source"));
						externalline.add(select);
						externalBody.add(externalline);
						
						layer.add(externalBody);
					}
				}
			}
		} else if(componentType.equals(FBConstants.BUTTON_TYPE)) {
//			ButtonArea area = page.getButtonArea();
//			if(area != null) {
//				Component component = area.getComponent(componentId);
//				if(component != null) {
//					PropertiesComponent properties = component.getProperties();
//					
//					Layer body = createPanelSection("labelPropertiesPanel");
//					
//					Layer line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
//					
//					TextInput title = new TextInput("propertyTitle", properties.getLabel().getString(new Locale("en")));
//					title.setOnBlur("saveComponentLabel(this.value);");
//					title.setOnKeyDown("savePropertyOnEnter(this.value,'compTitle',event);");
//					
//					line.add(new Text("Button title"));
//					line.add(title);
//					body.add(line);
//					
//					layer.add(body);
//				}
//			}
		}
		
		add(layer);
	}
	
	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
	
}
