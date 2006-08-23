package com.idega.formbuilder.presentation;
import com.idega.webface.WFContainer;

/**
 * <p>
 * Different renderer used<br />
 * Native method getWFRendererType changed to more generic getContainerRendererType
 * </p>
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBContainer extends WFContainer {
	
	private static String RENDERER_TYPE = "fb_container";
	private static String DEFAULT_STYLE_CLASS = RENDERER_TYPE;
	
	public static String getContainerRendererType() {
		return RENDERER_TYPE;
	}
	
	public FBContainer() {
		setRendererType(getContainerRendererType());
		setStyleClass(DEFAULT_STYLE_CLASS);
	}
}
