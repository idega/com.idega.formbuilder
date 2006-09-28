package com.idega.formbuilder.business.form.manager;

import javax.faces.context.FacesContext;


/**
 * This class is just to convenience getting new instance of FormManager
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class FormManagerFactory {
	
	private FormManagerFactory() { }
	
	/**
	 * @return FormManager instance
	 * @throws InstantiationException - FormManager could not be instantiated
	 */
	public static IFormManager newFormManager(FacesContext ctx) throws InstantiationException {
		
		if(!FormManager.isInited()) {
			
			synchronized(FormManager.class) {
					
				if(!FormManager.isInited()) {
						
					FormManager.init(ctx);
				}
			}
		}
		
		return FormManager.getInstance();
	}
}
