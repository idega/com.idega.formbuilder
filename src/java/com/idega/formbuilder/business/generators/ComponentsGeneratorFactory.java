package com.idega.formbuilder.business.generators;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class ComponentsGeneratorFactory {
	
	private ComponentsGeneratorFactory() { }

	/**
	 * @return IComponentsGenerator implementation depending on __not defined yet__
	 */
	public static IComponentsGenerator createComponentsGenerator() {
		
		return FormComponentsGenerator.getInstance();
	}
}