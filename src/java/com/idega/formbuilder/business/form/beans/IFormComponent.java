package com.idega.formbuilder.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Element;

public interface IFormComponent {

	public abstract void render();

	public abstract void setComponentAfterThis(IFormComponent component);
	
	public abstract void setComponentAfterThisRerender(IFormComponent component);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract void setType(String type);
	
	public abstract String getType();

	public abstract void setFormDocument(IFormComponentParent form_document);
	
	public abstract Element getHtmlRepresentationByLocale(Locale locale);
	
	public abstract IComponentProperties getProperties();
}