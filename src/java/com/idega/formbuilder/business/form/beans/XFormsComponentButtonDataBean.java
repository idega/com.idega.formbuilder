package com.idega.formbuilder.business.form.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsComponentButtonDataBean extends XFormsComponentDataBean {

	private Element previous_page_setvalue_element;
	private Element next_page_setvalue_element;
	private Element current_page_setvalue_element;
	
	public Element getCurrentPageSetvalueElement() {
		return current_page_setvalue_element;
	}
	public void setCurrentPageSetvalueElement(Element current_page_setvalue_element) {
		this.current_page_setvalue_element = current_page_setvalue_element;
	}
	public Element getNextPageSetvalueElement() {
		return next_page_setvalue_element;
	}
	public void setNextPageSetvalueElement(Element next_page_setvalue_element) {
		this.next_page_setvalue_element = next_page_setvalue_element;
	}
	public Element getPreviousPageSetvalueElement() {
		return previous_page_setvalue_element;
	}
	public void setPreviousPageSetvalueElement(Element previous_page_setvalue_element) {
		this.previous_page_setvalue_element = previous_page_setvalue_element;
	}
	
}