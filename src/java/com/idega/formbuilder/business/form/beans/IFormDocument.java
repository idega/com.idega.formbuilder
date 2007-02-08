package com.idega.formbuilder.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.Page;

public interface IFormDocument {

	public abstract Exception[] getSavedExceptions();

	public abstract void persist();
	
	public abstract String getXFormsDocumentSourceCode() throws Exception;
	
	public abstract void setXFormsDocumentSourceCode(String src_code) throws Exception;
	
	public abstract void setFormTitle(LocalizedStringBean form_name);
	
	public abstract LocalizedStringBean getFormTitle();
	
	public abstract String getFormId();
	
	public abstract Document getFormXFormsDocumentClone();
	
	public abstract Document getXformsDocument();
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract Locale getDefaultLocale();
	
	public abstract void tellComponentId(String component_id);
	
	public abstract String generateNewComponentId();
}