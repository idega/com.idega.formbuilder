package com.idega.formbuilder.presentation;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FBToolbarTag extends UIComponentTagBase {

	private String styleClass;
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public FBToolbarTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	public String getComponentType() {
		return "FBToolbar";
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	public String getRendererType() {
		return "fb_toolbar";
	}
	
	
}
