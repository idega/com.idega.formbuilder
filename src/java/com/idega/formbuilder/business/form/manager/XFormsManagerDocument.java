package com.idega.formbuilder.business.form.manager;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerDocument extends XFormsManagerContainer {

	public void setComponentsContainer(Element element) {
		
		xforms_component = newXFormsComponentDataBeanInstance();
		xforms_component.setElement(element);
	}
}