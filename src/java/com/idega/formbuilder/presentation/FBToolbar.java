package com.idega.formbuilder.presentation;

import com.idega.webface.WFToolbar;

/**
 * <p>
 * Different renderer used<br />
 * Native method getWFRendererType changed to more generic getToolbarRendererType<br />
 * </p>
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBToolbar extends WFToolbar {
	
	private static String RENDERER_TYPE = "fb_toolbar";
	
	public static String getToolbarRendererType() {
		return RENDERER_TYPE;
	}
	
	public FBToolbar(){
		setRendererType(getToolbarRendererType());
		setToolbarStyle();
	}
}
