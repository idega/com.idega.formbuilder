package com.idega.formbuilder.business.form.manager.generators;

import org.w3c.dom.Document;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public interface IComponentsGenerator {

	public boolean isInitiated();

	/**
	 * 
	 * Generates xml components document from xforms components document
	 * 
	 * @return HTML components xml document
	 * @throws Exception. Concrete exceptions throwed depends on implementation.
	 */
	public Document generateBaseComponentsDocument() throws Exception;
	
	/**
	 * set input document
	 * @param doc - document, to generate components from
	 */
	public void setDocument(Document doc);
}