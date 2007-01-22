package com.idega.formbuilder.presentation.tags;

import org.apache.myfaces.taglib.html.ext.HtmlInputTextareaTag;

import com.idega.formbuilder.presentation.components.FBFormSourceEdit;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 */
public class FBFormSourceEditTag extends HtmlInputTextareaTag {
	
	public String getComponentType() {

		return FBFormSourceEdit.COMPONENT_TYPE;
	}
	
	public String getRendererType() {
		return null;
	}
}