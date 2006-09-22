package com.idega.formbuilder.business.form;

/**
 * This class is just to convenience getting new instance of FormBuilder
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class FormBuilderFactory {
	
	private FormBuilderFactory() { }
	
	/**
	 * @return FormBuilder instance
	 * @throws InstantiationException - FormBuilder could not be instantiated
	 */
	public static IFormBuilder newFormBuilder() throws InstantiationException {
		
		if(!FormBuilder.isInited()) {
			
			synchronized(FormBuilder.class) {
					
				if(!FormBuilder.isInited()) {
						
					FormBuilder.init(null);
				}
			}
		}
		
		return FormBuilder.getInstance();
	}
}
