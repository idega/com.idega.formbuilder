package com.idega.formbuilder.business.generators;

import org.w3c.dom.Document;

public interface IComponentsGenerator {

	public abstract boolean isInitiated();

	/**
	 * 
	 * @param params - parameters, used for components document generation. Usually - context info.
	 * @throws Exception. Concrete exceptions throwed depends on implementation.
	 */
	public abstract void init(String[] params) throws Exception;

	/**
	 * 
	 * Generates xml components document from xforms components document, using parameters, 
	 * passed through init phase.
	 * 
	 * @return HTML components xml document
	 * @throws Exception. Concrete exceptions throwed depends on implementation.
	 */
	public abstract Document generateBaseComponentsDocument() throws Exception;

}