package com.idega.formbuilder.business.form;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.business.form.manager.FormManager;
import com.idega.formbuilder.business.form.manager.util.InitializationException;


/**
 * This class is just to convenience getting new instance of FormManager.
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class FormManagerFactory {
	
	protected FormManagerFactory() { }
	
	/**
	 * initiates FormManager if needed
	 * 
	 * @return IFormManager instance
	 * @throws InitializationException - IFormManager could not be initialized
	 */
	public static DocumentManager newFormManager(FacesContext ctx) throws InitializationException {
		
		if(!FormManager.isInited()) {
			
			synchronized(FormManagerFactory.class) {
					
				if(!FormManager.isInited()) {
						
					FormManager.init(ctx);
				}
			}
		}
		
		return FormManager.getInstance();
	}
}
