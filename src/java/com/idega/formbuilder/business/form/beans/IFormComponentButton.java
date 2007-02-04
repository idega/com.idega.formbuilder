package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentButton extends IFormComponent {

	public abstract void setSiblingsAndParentPages(IFormComponentPage previous, IFormComponentPage next, IFormComponentPage current);
}