package com.idega.formbuilder.business.form.beans;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IFormComponentParent {
	
	public abstract Document getXformsDocument();
	
	public abstract List<String> getFormComponentsIdList();
	
	public abstract IFormComponent getFormComponent(String component_id);
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();
	
	public abstract Locale getDefaultLocale();
	
	public abstract Element getWizzardElement();
	
	public abstract void setWizzardElement(Element wizzard_element);
}