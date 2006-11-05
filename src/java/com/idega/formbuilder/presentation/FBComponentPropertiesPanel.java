package com.idega.formbuilder.presentation;

import javax.faces.application.Application;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.Component;
import com.idega.formbuilder.business.form.beans.ComponentProperties;
import com.idega.formbuilder.business.form.beans.ComponentPropertiesSelect;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
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
		System.out.println("INITIALIZING PROPERTIES");
		//Application application = context.getApplication();
		Application application = context.getApplication();
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		this.getChildren().clear();
		ValueBinding vb = this.getValueBinding("component");
		String componentId = null;
		if(vb != null) {
			componentId = (String) vb.getValue(context);
		} else {
			componentId = this.getComponent();
		}
		System.out.println("COMPONENT: " + componentId);
		if(componentId != null && !componentId.equals("")){
			//IComponentProperties properties = formManagerInstance.getComponentProperties(componentId);
			Component comp = (Component) WFUtil.getBeanInstance("component");
			comp.reloadComponent(componentId, formManagerInstance);
			IComponentProperties properties = comp.getProperties();
			if(properties != null) {
				if(properties instanceof ComponentProperties) {
					System.out.println("COMPONENT PROPERTIES");
					
					HtmlCommandLink applyButton = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
					applyButton.setValue("Apply");
					applyButton.setOnclick("applyChanges()");
					this.getChildren().add(applyButton);
					
					HtmlOutputLabel requiredLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					requiredLabel.setId(componentId + "prop_required_label");
					requiredLabel.setValue("Required field");
					requiredLabel.setFor(componentId + "prop_required");
					this.getChildren().add(requiredLabel);
					
					HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
					required.setId(componentId + "prop_required");
					required.setValueBinding("value", application.createValueBinding("#{component.required}"));
					//required.setValue(new Boolean(properties.isRequired()));
					this.getChildren().add(required);
					
					
					HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					titleLabel.setId(componentId + "PropTitleLabel");
					titleLabel.setValue("Field name");
					titleLabel.setFor(componentId + "PropTitle");
					this.getChildren().add(titleLabel);
					
					HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
					title.setId(componentId + "PropTitle");
					LocalizedStringBean labels = properties.getLabel();
					title.setValueBinding("value", application.createValueBinding("#{component.label}"));
					//title.setValue(labels.getString(new Locale("en")));
					this.getChildren().add(title);
					
					
					HtmlOutputLabel errorMsgLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
					errorMsgLabel.setId(componentId + "PropErrorMsgLabel");
					errorMsgLabel.setValue("Error message");
					errorMsgLabel.setFor(componentId + "PropErrorMsg");
					this.getChildren().add(errorMsgLabel);
					
					HtmlInputText errorMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
					errorMsg.setId(componentId + "PropErrorMsg");
					LocalizedStringBean msgs = properties.getErrorMsg();
					errorMsg.setValueBinding("value", application.createValueBinding("#{component.errorMsg}"));
					//errorMsg.setValue(msgs.getString(new Locale("en")));
					this.getChildren().add(errorMsg);
					
					if(properties instanceof ComponentPropertiesSelect) {
						HtmlOutputLabel notImplemented = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
						//notImplemented.setId(componentId + "PropErrorMsgLabel");
						notImplemented.setValue("Advanced propertied of this component are not implemented yet");
						//errorMsgLabel.setFor(componentId + "PropErrorMsg");
						this.getChildren().add(notImplemented);
					}
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
