package com.idega.formbuilder.presentation;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FBToolbarTag extends UIComponentTagBase {

	public FBToolbarTag() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getComponentType()
	 */
	public String getComponentType() {
		System.out.println("wazaaaaaaaaaaaaaa");
		return "FBToolbar";
	}

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getRendererType()
	 */
	public String getRendererType() {
		return "fb_toolbar";
	}
}
