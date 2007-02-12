package com.idega.formbuilder.business.form;

import java.util.List;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public interface DocumentManager {

	/**
	 * creates primary user form document and stores it depending
	 * 
	 * @param form_id - cannot be null
	 * @param name - form name. Can be null, then default is used.
	 *  
	 * @throws FBPostponedException - if some kind of exception happened during previous request.
	 * @throws NullPointerException - form_id was not provided
	 * @throws Exception - some kind of other error occured
	 * 
	 * @return Created form document
	 */
	public abstract com.idega.formbuilder.business.form.Document createForm(String form_id, LocalizedStringBean name)
			throws NullPointerException, Exception;

	/**
	 * 
	 * @return List of available form components types by category
	 */
	public List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category);

	/**
	 * Open and load document by form id
	 * 
	 * @param form_id
	 * @throws NullPointerException - form_id is not provided
	 * @throws Exception
	 * 
	 * @return loaded document
	 */
	public abstract com.idega.formbuilder.business.form.Document openForm(String form_id) throws NullPointerException, Exception;
	
	public abstract com.idega.formbuilder.business.form.Document getCurrentDocument();
}