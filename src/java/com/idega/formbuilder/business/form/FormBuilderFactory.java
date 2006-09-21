package com.idega.formbuilder.business.form;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is just to convenience getting new instance of FormBuilder
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class FormBuilderFactory {
	
	private static final Lock initiation_lock = new ReentrantLock();
	
	private FormBuilderFactory() { }
	
	/**
	 * @return FormBuilder instance
	 * @throws InstantiationException - FormBuilder could not be instantiated
	 */
	public static IFormBuilder createFormBuilder() throws InstantiationException {
		
		/**
		 * TODO: look for factory design patterns
		 */
		if(!FormBuilder.isInited()) {
			
			try {
				
				initiation_lock.lock();
				
				if(!FormBuilder.isInited()) {
				
					FormBuilder.init(null);
				}
				
			} finally {
				initiation_lock.unlock();
			}
		}
		
		return FormBuilder.getInstance();
	}
}
