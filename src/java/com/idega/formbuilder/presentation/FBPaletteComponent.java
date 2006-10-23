package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

public class FBPaletteComponent extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_paletteComponent";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "PaletteComponent";
	
	private String id;
	private String styleClass;
	private String name;
	private String type;

	public FBPaletteComponent() {
		super();
		this.setRendererType(FBPaletteComponent.RENDERER_TYPE);
	}
	
	public String getFamily() {
		return FBPaletteComponent.COMPONENT_FAMILY;
	}

	public String getRendererType() {
		return FBPaletteComponent.RENDERER_TYPE;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = id;
		values[3] = name;
		values[4] = type;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		id = (String) values[2];
		name = (String) values[3];
		type = (String) values[4];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
}
