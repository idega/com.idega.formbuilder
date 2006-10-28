package com.idega.formbuilder.business.form.beans;

import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.HtmlManager;
import com.idega.formbuilder.business.form.manager.HtmlManagerSubmitButton;
import com.idega.formbuilder.business.form.manager.XFormsManager;
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
		
		if(!created) {
			
			XFormsManagerSubmitButton xforms_manager = getXFormsManager();
			XFormsComponentDataBean xforms_component = xforms_manager.getXFormsSubmitComponent(xforms_doc);
			
			if(xforms_component == null)
				return;
			
			xforms_manager.setXFormsComponentDataBean(xforms_component);
			
			Element submit_element = xforms_component.getElement();
			String submit_id = submit_element.getAttribute(FormManagerUtil.id_att);
			setId(submit_id);
			
			ComponentPropertiesSubmitButton properties = (ComponentPropertiesSubmitButton)getProperties();
			
			properties.setPlainLabel(FormManagerUtil.getLabelLocalizedStrings(getId(), xforms_doc));
			properties.setAction("getaction from xforms doc");
			
			created = true;
		}
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