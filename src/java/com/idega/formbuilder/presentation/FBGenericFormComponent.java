package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class FBGenericFormComponent extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_genericfield";
	public static final String COMPONENT_TYPE = "FBGenericField";
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private String type;
	private String styleClass;

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public FBGenericFormComponent() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = type;
		values[2] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		type = (String) values[1];
		styleClass = (String) values[2];
	}

}
