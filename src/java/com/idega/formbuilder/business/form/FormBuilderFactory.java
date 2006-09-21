package com.idega.formbuilder.business.form;

/**
 * This class is just to convenience getting new instance of FormBuilder
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class FormBuilderFactory {
	
	/**
	 * @return FormBuilder instance
	 * @throws InstantiationException - FormBuilder could not be instantiated
	 */
	public static IFormBuilder createFormBuilder() throws InstantiationException {
		
		if(!FormBuilder.isInited()) {
			
			FormBuilder.init(null);
		}
		
		return FormBuilder.getInstance();
	}
}
