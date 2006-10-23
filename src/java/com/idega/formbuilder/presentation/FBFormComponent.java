package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

public class FBFormComponent extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_formComponent";
	public static final String COMPONENT_TYPE = "FBFormComponent";
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private String id;
	private String styleClass;
	private Element element;

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public FBFormComponent() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
