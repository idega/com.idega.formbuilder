package com.idega.formbuilder.business.form.beans;

import java.util.List;

import com.idega.formbuilder.business.form.manager.IPersistenceManager;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

public interface IFormDocument {

	public abstract void createDocument(String form_id,
			LocalizedStringBean form_name) throws NullPointerException;

	public abstract void addComponent(IFormComponent component);

	public abstract List<String> getFormComponentsIdList();

	public abstract IFormComponent getFormComponent(String component_id);

	public abstract Exception[] getSavedExceptions();

	public abstract void setPersistenceManager(
			IPersistenceManager persistance_manager);

	public abstract void persist() throws NullPointerException,
			InitializationException;

}