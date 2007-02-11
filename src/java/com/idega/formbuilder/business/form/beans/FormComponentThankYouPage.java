package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.PageThankYou;
import com.idega.formbuilder.business.form.PropertiesThankYouPage;
import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IXFormsManager;
import com.idega.formbuilder.business.form.manager.XFormsManagerThankYouPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentThankYouPage extends FormComponentPage implements PageThankYou {
	
	@Override
	public PropertiesThankYouPage getProperties() {
		
		if(properties == null) {
			ComponentPropertiesThankYouPage properties = new ComponentPropertiesThankYouPage();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesThankYouPage)properties;
	}
	
	@Override
	protected IXFormsManager getXFormsManager() {
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerThankYouPage();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
			xforms_manager.setFormDocument(form_document);
		}
		
		return xforms_manager;
	}
	
	@Override
	protected void setProperties() {
		super.setProperties();
		
		ComponentPropertiesThankYouPage properties = (ComponentPropertiesThankYouPage)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainText(((XFormsManagerThankYouPage)getXFormsManager()).getThankYouText());
	}
}