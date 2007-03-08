package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBAdminPage;

public class FBAdminPageTag extends UIComponentTagBase {
	
	private String styleClass;
	private String id;
	
	public FBAdminPageTag() {
		super();
		this.styleClass = "";
		this.id = "";
	}

	public String getComponentType() {
		return FBAdminPage.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return null;
	}
	
	public void release() {
		super.release();
		this.styleClass = null;
		this.id = null;
	}
	
	public void setProperties(UIComponent component) {
		super.setProperties(component);
		if(component != null) {
			FBAdminPage page = (FBAdminPage) component;
			if(this.styleClass != null) {
				page.setStyleClass(styleClass);
			}
			if(this.id != null) {
				page.setId(id);
			}
		}
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

}
