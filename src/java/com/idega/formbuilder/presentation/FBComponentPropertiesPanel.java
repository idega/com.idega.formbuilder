package com.idega.formbuilder.presentation;

import javax.faces.application.Application;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.DataSourceList;
import com.idega.formbuilder.business.FormComponent;
import com.idega.formbuilder.business.form.beans.ComponentProperties;
import com.idega.formbuilder.business.form.beans.ComponentPropertiesSelect;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.webface.WFUtil;

public class FBComponentPropertiesPanel extends UIComponentBase {

	public static final String RENDERER_TYPE = "fb_componentPropertiesPanel";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private String styleClass;
	private String component;
	
	public FBComponentPropertiesPanel() {
		super();
		this.setRendererType(FBComponentPropertiesPanel.RENDERER_TYPE);
	}
	
	public void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		String currentDataSrc = null;
		this.getChildren().clear();
		ValueBinding vb = this.getValueBinding("component");
		String componentId = null;
		if(vb != null) {
			componentId = (String) vb.getValue(context);
		} else {
			componentId = this.getComponent();
		}
		if(componentId != null && !componentId.equals("")){
			FormComponent comp = (FormComponent) WFUtil.getBeanInstance("component");
			comp.loadProperties(componentId, formManagerInstance);
			IComponentProperties properties = comp.getProperties();
			if(properties != null) {
				
				if(properties instanceof ComponentPropertiesSelect) {
					
					HtmlOutputLabel emptyLabelLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					emptyLabelLabel.setValue("Empty element label");
					emptyLabelLabel.setFor("propertyEmptyLabel");
					this.getChildren().add(emptyLabelLabel);
					
					HtmlInputText emptyLabel = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
					emptyLabel.setId("propertyEmptyLabel");
					emptyLabel.setOnblur("applyChanges()");
					emptyLabel.setValueBinding("value", application.createValueBinding("#{component.emptyLabel}"));
					this.getChildren().add(emptyLabel);
					
					
					HtmlOutputLabel advancedL = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					advancedL.setValue("Select options properties");
					this.getChildren().add(advancedL);
					
					HtmlSelectOneRadio dataSrcSwitch = (HtmlSelectOneRadio) application.createComponent(HtmlSelectOneRadio.COMPONENT_TYPE);
					dataSrcSwitch.setStyleClass("inlineRadioButton");
					dataSrcSwitch.setOnchange("switchDataSrc()");
					dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{dataSources.selectedDataSource}"));
					UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
					dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
					dataSrcSwitch.getChildren().add(dataSrcs);
					this.getChildren().add(dataSrcSwitch);
					
					currentDataSrc = ((DataSourceList) WFUtil.getBeanInstance("dataSources")).getSelectedDataSource();
					if(currentDataSrc != null && !currentDataSrc.equals("")) {
						if(currentDataSrc.equals("1")) {
							
							HtmlOutputLabel local = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
							local.setValue("ASDKLAJSDLKASJDs");
							this.getChildren().add(local);
							
							System.out.println("RENDERING SELECT OPTIONS");
							UIData selectOptions = (UIData) application.createComponent(UIData.COMPONENT_TYPE);
							selectOptions.setValueBinding("value", application.createValueBinding("#{options.items}"));
							selectOptions.setVar("item");
							UIColumn labels = (UIColumn) application.createComponent(UIColumn.COMPONENT_TYPE);
							HtmlOutputText label = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
							label.setValueBinding("value", application.createValueBinding("#{item.label}"));
							labels.getChildren().add(label);
							selectOptions.getChildren().add(labels);
							this.getChildren().add(selectOptions);
							
							System.out.println("RENDERING SELECT OPTIONS");
							HtmlOutputLabel local2 = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
							local2.setValue("BLA BLA BLA");
							this.getChildren().add(local2);
							
						} else if(currentDataSrc.equals("2")) {
							
							HtmlOutputLabel externalSrcLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
							externalSrcLabel.setValue("External data source");
							externalSrcLabel.setFor("propertyExternal");
							this.getChildren().add(externalSrcLabel);
							
							HtmlInputText external = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
							external.setId("propertyExternal");
							external.setOnblur("applyChanges()");
							external.setValueBinding("value", application.createValueBinding("#{component.externalSrc}"));
							this.getChildren().add(external);
							
						}
					}
				}
				if(properties instanceof ComponentProperties) {
					
					HtmlOutputLabel requiredLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					requiredLabel.setValue("Required field");
					requiredLabel.setFor("propertyRequired");
					this.getChildren().add(requiredLabel);
					
					HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
					required.setId("propertyRequired");
					required.setValueBinding("value", application.createValueBinding("#{component.required}"));
					required.setOnchange("applyChanges()");
					this.getChildren().add(required);
					
					
					HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					titleLabel.setValue("Field name");
					titleLabel.setFor("propertyTitle");
					this.getChildren().add(titleLabel);
					
					HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
					title.setId("propertyTitle");
					title.setOnblur("applyChanges()");
					title.setValueBinding("value", application.createValueBinding("#{component.label}"));
					this.getChildren().add(title);
					
					
					HtmlOutputLabel errorMsgLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					errorMsgLabel.setValue("Error message");
					errorMsgLabel.setFor("propertyErrorMessage");
					this.getChildren().add(errorMsgLabel);
					
					HtmlInputText errorMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
					errorMsg.setId("propertyErrorMessage");
					errorMsg.setOnblur("applyChanges()");
					errorMsg.setValueBinding("value", application.createValueBinding("#{component.errorMsg}"));
					this.getChildren().add(errorMsg);
					
					
				}
			}
		}
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getFamily() {
		return FBComponentPropertiesPanel.COMPONENT_FAMILY;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = component;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		component = (String) values[2];
	}

}
