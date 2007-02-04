package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentPage extends IFormComponentContainer {

	public abstract void setButtonAreaComponentId(String button_area_id);
	
	public abstract void setPageSiblings(IFormComponentPage previous, IFormComponentPage next);
	
	public abstract void pagesSiblingsChanged();
	public abstract IFormComponentPage getPreviousPage();
	public abstract IFormComponentPage getNextPage();
}