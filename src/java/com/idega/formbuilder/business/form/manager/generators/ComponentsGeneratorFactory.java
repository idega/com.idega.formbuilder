package com.idega.formbuilder.business.form.manager.generators;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class ComponentsGeneratorFactory {
	
	protected ComponentsGeneratorFactory() { }

	/**
	 * @return IComponentsGenerator implementation depending on __not defined yet__
	 */
	public static IComponentsGenerator createComponentsGenerator() {
		
		return FormComponentsGenerator.getInstance();
	}
}