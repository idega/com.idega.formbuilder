package com.idega.formbuilder.business.form.beans;

import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.HtmlManagerSubmitButton;
import com.idega.formbuilder.business.form.manager.XFormsManagerSubmitButton;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentSubmitButton extends FormComponent {
	
	protected XFormsManagerSubmitButton xforms_manager;
	protected HtmlManagerSubmitButton html_manager;
	
	public void render() {
		
		Document xforms_doc = form_document.getXformsDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form Xforms document was not provided");
		
		if(load || !created) {
			
			XFormsManagerSubmitButton xforms_manager = getXFormsManager();
			xforms_manager.loadXFormsSubmitComponent(xforms_doc);
			
			String submit_id = xforms_manager.getSubmitIdFromElement();
			
			if(submit_id == null)
				return;
			
			setId(submit_id);
			
			setProperties();

			form_document.setFormDocumentModified(true);
			created = true;
			load = false;
		}
	}
	
	protected void setProperties() {
		
		ComponentPropertiesSubmitButton properties = (ComponentPropertiesSubmitButton)getProperties();
		Document xforms_doc = form_document.getXformsDocument();
		
		properties.setPlainLabel(FormManagerUtil.getLabelLocalizedStrings(getId(), xforms_doc));
		properties.setAction("getaction from xforms doc");
	}
	
	public void setComponentAfterThis(IFormComponent component) { }
	
	public void setComponentAfterThisRerender(IFormComponent component) { }
	
	public IComponentProperties getProperties() {
		
		if(properties == null) {
			ComponentPropertiesSubmitButton properties = new ComponentPropertiesSubmitButton();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}

	protected XFormsManagerSubmitButton getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerSubmitButton();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setFormDocument(form_document);
			xforms_manager.setFormComponent(this);
		}
		
		return xforms_manager;
	}
	
	public void updateErrorMsg() { }
	
	public void updateConstraintRequired() { }
	
	public void updateAction() {
		getXFormsManager().updateAction();
	}
	
	protected HtmlManagerSubmitButton getHtmlManager() {
		
		if(html_manager == null) {
			
			html_manager = new HtmlManagerSubmitButton();
			html_manager.setCacheManager(CacheManager.getInstance());
			html_manager.setFormDocument(form_document);
			html_manager.setFormComponent(this);
		}
		
		return html_manager;
	}
}