package com.idega.formbuilder.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.Page;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentDocument extends IFormComponentContainer {

	public abstract Document getXformsDocument();
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();

	public abstract Locale getDefaultLocale();
	
	public abstract Page getConfirmationPage();
	
	public abstract Page getThxPage();
	
	public abstract void registerForLastPage(String register_page_id);
	
	public abstract String generateNewComponentId();
}