package com.idega.formbuilder.presentation;

import com.idega.webface.WFContainerTag;

/**
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBContainerTag extends WFContainerTag {

	public String getComponentType() {

		return "FBContainer";
	}
	
	public String getRendererType() {
		return FBContainer.getContainerRendererType();
	}
}
