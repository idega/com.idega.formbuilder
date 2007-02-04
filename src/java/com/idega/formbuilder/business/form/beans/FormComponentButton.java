package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.Button;
import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IXFormsManager;
import com.idega.formbuilder.business.form.manager.XFormsManagerButton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentButton extends FormComponent implements Button, IFormComponentButton {
	
	@Override
	protected IXFormsManager getXFormsManager() {
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerButton();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
		}
		
		return xforms_manager;
	}
	
	@Override
	public void render() {
		super.render();
		IFormComponentButtonArea my_button_area = (IFormComponentButtonArea)parent;
		setSiblingsAndParentPages(
				my_button_area.getPreviousPage(), my_button_area.getNextPage(), my_button_area.getCurrentPage()
		);
		((IFormComponentButtonArea)parent).setButtonMapping(getType(), getId());
	}
	
	public void setSiblingsAndParentPages(IFormComponentPage previous, IFormComponentPage next, IFormComponentPage current) {
		((XFormsManagerButton)getXFormsManager()).renewButtonRelevantPages(previous, next, current);
	}
}