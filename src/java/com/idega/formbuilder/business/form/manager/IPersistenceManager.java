package com.idega.formbuilder.business.form.manager;

import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

public interface IPersistenceManager {

	/**
	 * saves xml(!) file to webdav directory
	 * 
	 * <p>
	 * <b>imporant:</b> method uses thread to upload file. So, if something bad happens during this process
	 * exception thrown is saved to document_to_webdav_save_exception variable. This variable should be time to time checked
	 * for null condition to know, if everything is alright.<br />
	 * Variable is set to null everytime, when no exception is thrown.<br />
	 * Logging is taking place for every exception thrown, so see logs for all problems.
	 * </p>
	 * 
	 * @param document - xml document to write to webdav repository
	 * @param service_bean - service bean, used to upload files
	 * @param path_to_file - where file should be placed, relative to webdav context
	 * @param file_name - how should we name the file
	 * @throws TransformerException - file is not an xml file maybe
	 * @throws NullPointerException - some parameters were not provided, or provided empty string(s)
	 */
	public abstract void persistDocument(final Document document)
			throws TransformerException, InstantiationException, NullPointerException;
	
	public abstract void init(String document_id) throws InstantiationException;
	
	public abstract boolean isInitiated();
	
	public abstract Exception[] getSavedExceptions();
}