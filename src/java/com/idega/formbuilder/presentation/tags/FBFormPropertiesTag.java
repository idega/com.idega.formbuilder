package com.idega.formbuilder.presentation.tags;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

import com.idega.formbuilder.presentation.components.FBFormProperties;

public class FBFormPropertiesTag extends UIComponentTagBase {

	public String getComponentType() {
		return FBFormProperties.COMPONENT_TYPE;
	}

	public String getRendererType() {
		return null;
	}

}
