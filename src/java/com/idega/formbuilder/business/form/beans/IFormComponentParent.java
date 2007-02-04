package com.idega.formbuilder.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.Page;

public interface IFormComponentParent {
	
	public abstract Document getXformsDocument();
	
	public abstract Page getPage(String component_id);
	
	public abstract IFormComponent getContainedComponent(String component_id);
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();
	
	public abstract Locale getDefaultLocale();
	
	public abstract Element getWizardElement();
	
	public abstract void setWizardElement(Element wizard_element);
}