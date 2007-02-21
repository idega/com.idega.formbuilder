package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBComponentPropertiesPanel extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ComponentPropertiesPanel";
	
	private static final String BASIC_CONTENT_FACET = "BASIC_CONTENT_FACET";
	private static final String ADV_CONTENT_FACET = "ADV_CONTENT_FACET";
	
	public FBComponentPropertiesPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		FBBasicProperties simpleProperties = (FBBasicProperties) application.createComponent(FBBasicProperties.COMPONENT_TYPE);
		simpleProperties.setId("simplePropertiesPanel");
		
		FBAdvancedProperties advancedProperties = (FBAdvancedProperties) application.createComponent(FBAdvancedProperties.COMPONENT_TYPE);
		advancedProperties.setId("advancedPropertiesPanel");
		advancedProperties.setStyleClass("advancedPropsPanelHidden");
		
		addFacet(BASIC_CONTENT_FACET, simpleProperties);
		addFacet(ADV_CONTENT_FACET, advancedProperties);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		UIComponent component = getFacet(BASIC_CONTENT_FACET);
		if(component != null) {
			renderChild(context, component);
		}
		component = getFacet(ADV_CONTENT_FACET);
		if(component != null) {
			renderChild(context, component);
		}
	}
	
}
