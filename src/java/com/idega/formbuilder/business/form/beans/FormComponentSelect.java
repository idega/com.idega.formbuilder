package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IXFormsManager;
import com.idega.formbuilder.business.form.manager.XFormsManagerSelect;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentSelect extends FormComponent implements IComponentPropertiesSelectParent {
	
	protected IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerSelect();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setFormDocument(form_document);
			xforms_manager.setFormComponent(this);
		}
		
		return xforms_manager;
	}
	
	public IComponentProperties getProperties() {
		
		if(properties == null) {
			ComponentPropertiesSelect properties = new ComponentPropertiesSelect();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}
	
	protected void setProperties() {
		
		super.setProperties();
		
		ComponentPropertiesSelect properties = (ComponentPropertiesSelect)getProperties();
		XFormsManagerSelect xforms_manager = (XFormsManagerSelect)getXFormsManager();
		
		properties.setDataSrcUsedPlain(xforms_manager.getDataSrcUsed());
		properties.setEmptyElementLabelPlain(xforms_manager.getEmptyElementLabel());
		properties.setExternalDataSrcPlain(xforms_manager.getExternalDataSrc());
		properties.setItemsetPlain(xforms_manager.getItemset());
	}
	
	public void updateDataSrcUsed() {
		
		((XFormsManagerSelect)getXFormsManager()).updateDataSrcUsed();
		getHtmlManager().clearLocalizedHtmlComponents();
		form_document.setFormDocumentModified(true);
	}
	
	public void updateItemset() {
		getHtmlManager().clearLocalizedHtmlComponents();
		form_document.setFormDocumentModified(true);
	}
	
	public void updateEmptyElementLabel() {
		((XFormsManagerSelect)getXFormsManager()).updateEmptyElementLabel();
		getHtmlManager().clearLocalizedHtmlComponents();
		form_document.setFormDocumentModified(true);
	}
	
	public void updateExternalDataSrc() {
		((XFormsManagerSelect)getXFormsManager()).updateExternalDataSrc();
		getHtmlManager().clearLocalizedHtmlComponents();
		form_document.setFormDocumentModified(true);
	}
}