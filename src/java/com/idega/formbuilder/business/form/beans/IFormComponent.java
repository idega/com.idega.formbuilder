package com.idega.formbuilder.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Element;

public interface IFormComponent {

	public abstract String getComponentId();

	public abstract void setComponentId(String component_id);

	public abstract void setComponent(Locale locale, Element element);

	public abstract Element getComponent(Locale locale);

}