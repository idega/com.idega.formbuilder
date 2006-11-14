package com.idega.formbuilder.business.form.beans;

import java.util.List;

import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.manager.IPersistenceManager;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

public interface IFormDocument {

	public abstract void createDocument(String form_id,
			LocalizedStringBean form_name) throws NullPointerException;

	public abstract String addComponent(String component_type, String component_after_this_id) throws NullPointerException;

	public abstract List<String> getFormComponentsIdList();

	public abstract IFormComponent getFormComponent(String component_id);

	public abstract Exception[] getSavedExceptions();

	public abstract void setPersistenceManager(
			IPersistenceManager persistence_manager);

	public abstract void persist() throws NullPointerException,
			InitializationException, Exception;
	
	public abstract void rearrangeDocument();
	
	public abstract FormComponentSubmitButton getSubmitButtonComponent();
	
	public abstract void unregisterComponent(String component_id);
	
	public abstract void loadDocument(String form_id) throws InitializationException, Exception;
	
	public abstract String getXFormsDocumentSourceCode() throws Exception;
	
	public abstract void setXFormsDocumentSourceCode(String src_code) throws Exception;
	
	public abstract void setFormTitle(LocalizedStringBean form_name);
	
	public abstract LocalizedStringBean getFormTitle();
	
	public abstract String getFormId();
	
	public abstract Document getFormXFormsDocument();
}