package com.idega.formbuilder.presentation;

import javax.faces.application.Application;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.beans.ComponentProperties;
import com.idega.formbuilder.business.form.beans.ComponentPropertiesSelect;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.manager.IFormManager;

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
		this.getChildren().clear();
		ValueBinding vb = this.getValueBinding("component");
		String componentId = null;
		if(vb != null) {
			componentId = (String) vb.getValue(context);
		} else {
			componentId = this.getComponent();
		}
		if(componentId != null && componentId.equals("")){
			IComponentProperties properties = formManagerInstance.getComponentProperties(componentId);
			if(properties instanceof ComponentPropertiesSelect) {
				
			} else if(properties instanceof ComponentProperties) {
				HtmlOutputLabel requiredLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
				requiredLabel.setId(componentId + "prop_required_label");
				requiredLabel.setValue("Simple label");
				requiredLabel.setFor(componentId + "prop_required");
				this.getChildren().add(requiredLabel);
				HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
				required.setId(componentId + "prop_required");
				//required.set
				required.setValue(new Integer(23));
				this.getChildren().add(required);
			}
		}
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
