package com.idega.formbuilder.business.form.beans;

import com.idega.repository.data.Singleton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentFactory implements Singleton {
	
	private static FormComponentFactory me;
	
	public static FormComponentFactory getInstance() {
		
		if (me == null) {
			
			synchronized (FormComponentFactory.class) {
				if (me == null) {
					me = new FormComponentFactory();
				}
			}
		}

		return me;
	}
	
	public IFormComponent getFormComponentByType(String component_type) {
		
		FormComponent component = new FormComponent();
		component.setType(component_type);
		
		return component;
	}
	
	
}