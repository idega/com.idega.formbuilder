package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class FBWorkspace extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_workspace";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "Workspace";
	
	private String styleClass;
	private String view;

	public FBWorkspace() {
		super();
		this.setRendererType(FBWorkspace.RENDERER_TYPE);
	}
	
	public String getFamily() {
		return FBWorkspace.COMPONENT_FAMILY;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = view;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		view = (String) values[2];
	}

}
