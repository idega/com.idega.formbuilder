package com.idega.formbuilder.presentation.tags;

import javax.faces.webapp.UIComponentTag;

import com.idega.formbuilder.presentation.components.FBFormPreview;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBFormPreviewTag extends UIComponentTag {
	
	public String getComponentType() {

		return FBFormPreview.COMPONENT_TYPE;
	}
	
	public String getRendererType() {
		return null;
	}
}
