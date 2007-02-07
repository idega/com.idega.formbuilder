package com.idega.formbuilder.business.form.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsComponentButtonDataBean extends XFormsComponentDataBean {

	private Element toggle_element;

	public Element getToggleElement() {
		return toggle_element;
	}
	public void setToggleElement(Element toggle_element) {
		this.toggle_element = toggle_element;
	}
}