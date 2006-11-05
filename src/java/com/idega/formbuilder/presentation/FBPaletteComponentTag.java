package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_impl.taglib.UIComponentTagBase;

public class FBPaletteComponentTag extends UIComponentTagBase {
	
	private String id;
	private String styleClass;
	private String name;
	private String type;
	private String icon;
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public FBPaletteComponentTag() {
		super();
		this.id = "";
		this.styleClass = "";
		this.name = "";
		this.type = "";
		this.icon = "";
	}

	public String getComponentType() {
		return FBPaletteComponent.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return FBPaletteComponent.RENDERER_TYPE;
	}

	public void release() {
		super.release();
		this.id = null;
		this.styleClass = null;
		this.name = null;
		this.type = null;
		this.icon = null;
	}

	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBPaletteComponent comp = (FBPaletteComponent) component;
			if(this.id != null) {
				comp.setId(this.id);
			}
			if(this.styleClass != null) {
				comp.setStyleClass(this.styleClass);
			}
			if(this.name != null) {
				if (isValueReference(this.name)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.name);
	                comp.setValueBinding("name", vb);
	            } else {
	            	comp.setName(this.name);
	            }
			}
			if(this.type != null) {
				if (isValueReference(this.type)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.type);
	                comp.setValueBinding("type", vb);
	            } else {
	            	comp.setType(this.type);
	            }
			}
			if(this.icon != null) {
				if (isValueReference(this.icon)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.icon);
	                comp.setValueBinding("icon", vb);
	            } else {
	            	comp.setIcon(this.icon);
	            }
			}
		}
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
