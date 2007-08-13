package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBToolbar;

/**
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 */
public class FBToolbarTag extends UIComponentTagBase {

	private String styleClass;
	private String buttonsStyleClass;
	
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
		return FBToolbar.getToolbarRendererType();
	}

	public String getButtonsStyleClass() {
		return buttonsStyleClass;
	}

	public void setButtonsStyleClass(String buttonsStyleClass) {
		this.buttonsStyleClass = buttonsStyleClass;
	}
	
	@Override
	protected void setProperties(UIComponent component) {
		
		super.setProperties(component);
		FBToolbar toolbar = (FBToolbar) component;
		
	    if(toolbar != null) {
	        if(buttonsStyleClass != null)
	            toolbar.setButtonsStyleClass(buttonsStyleClass);
	        
	        if(styleClass != null)
	            toolbar.setStyleClass(styleClass);
	    }
	}
}
