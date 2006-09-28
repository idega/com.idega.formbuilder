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
	 * @param params - parameters, used for components document generation. Usually - context info.
	 * @throws Exception. Concrete exceptions throwed depends on implementation.
	 */
	public void init(String[] params) throws Exception;

	/**
	 * 
	 * Generates xml components document from xforms components document, using parameters, 
	 * passed through init phase.
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