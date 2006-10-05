package com.idega.formbuilder.business.form.manager;

import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.manager.util.InitializationException;

public interface IPersistenceManager {

	/**
	 * saves xml(!) document
	 * 
	 * @param document - xml document to save
	 * 
	 * @throws InitializationException - IPersistenceManager was not initiated before
	 * @throws NullPointerException - document was not provided, __implementation specific__
	 */
	public abstract void persistDocument(final Document document)
			throws InitializationException, NullPointerException;

	/**
	 * 
	 * @param document_id - identifier, used for document recognition
	 * 
	 * @throws NullPointerException - document_id not provided
	 * @throws InstantiationException - __implementation specific__
	 * 
	 */
	public abstract void init(String document_id) throws NullPointerException;
	
	public abstract boolean isInitiated();
	
	/**
	 * 
	 * __implementation specific__
	 * 
	 * @return exceptions array, that happened during previous request
	 */
	public abstract Exception[] getSavedExceptions();
}