package com.idega.formbuilder.view.design;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class FormDesignView extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_formviewer";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "FormDesignView";
	
	private String id;
	private String styleClass;
	private String componentStyleClass;
	
	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

	public FormDesignView() {
		super();
		this.setRendererType(FormDesignView.RENDERER_TYPE);
	}
	
	public String getFamily() {
		return FormDesignView.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return FormDesignView.RENDERER_TYPE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = id;
		values[3] = componentStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		id = (String) values[2];
		componentStyleClass = (String) values[3];
	}
}
