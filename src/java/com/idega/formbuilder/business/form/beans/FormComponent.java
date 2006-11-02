package com.idega.formbuilder.business.form.beans;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.HtmlManager;
import com.idega.formbuilder.business.form.manager.IXFormsManager;
import com.idega.formbuilder.business.form.manager.XFormsManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponent implements IFormComponent, IComponentPropertiesParent {
	
	protected IFormComponent component_after_me;
	protected String component_id;
	protected String type;
	
	protected IFormComponentParent form_document;
	
	protected IComponentProperties properties;
	protected boolean created = false;
	protected boolean load = false;
	
	protected IXFormsManager xforms_manager;
	protected HtmlManager html_manager;
	
	public void render() {
		
		Document xforms_doc = form_document.getXformsDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form Xforms document was not provided");
		
		if(load || !created) {
			
			IXFormsManager xforms_manager = getXFormsManager();
			
			if(load) {
				
				xforms_manager.loadXFormsComponentFromDocument(component_id);
				
			} else {
				
				xforms_manager.loadXFormsComponentByType(type);
				xforms_manager.addComponentToDocument(component_id, 
						component_after_me == null ? null : component_after_me.getId());
			}
			
			setProperties();
			
			form_document.setFormDocumentModified(true);
			tellAboutMe();
			
			created = true;
			load = false;
		}
	}
	
	protected void setProperties() {
		
		ComponentProperties properties = (ComponentProperties)getProperties();
		Document xforms_doc = form_document.getXformsDocument();
		
		properties.setPlainLabel(FormManagerUtil.getLabelLocalizedStrings(component_id, xforms_doc));
		properties.setPlainRequired(false);
		properties.setPlainErrorMsg(FormManagerUtil.getErrorLabelLocalizedStrings(component_id, xforms_doc));
	}
	
	protected void tellAboutMe() {
		
		List<String> id_list = form_document.getFormComponentsIdList();

		for (int i = 0; i < id_list.size(); i++) {
			
			if(id_list.get(i).equals(component_id) && i != 0) {
				
				form_document.getFormComponent(id_list.get(i-1)).setComponentAfterThis(this);
				break;
			}
		}
	}
	
	public void setComponentAfterThis(IFormComponent component) {
		
		component_after_me = component;
	}
	
	public void setComponentAfterThisRerender(IFormComponent component) {
		
		IXFormsManager xforms_manager = getXFormsManager();
		
		if(component != null && component_after_me != null && !component_after_me.getId().equals(component.getId())) {

			xforms_manager.moveComponent(component.getId());
			
		} else if(component_after_me == null && component != null) {
			
			xforms_manager.moveComponent(component.getId());
			
		} else if(component == null && component_after_me != null) {
			
			xforms_manager.moveComponent(null);
		}
		component_after_me = component;
	}
	
	public String getId() {
		
		return component_id;
	}
	
	public void setId(String id) {
		
		if(id != null)
			component_id = id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setLoad(boolean load) {
		this.load = load;
	}
	
	public void setFormDocument(IFormComponentParent form_document) {
		this.form_document = form_document; 
	}
	
	public Element getHtmlRepresentationByLocale(Locale locale) throws Exception {
		
		return getHtmlManager().getHtmlRepresentationByLocale(locale);
	}
	
	public IComponentProperties getProperties() {
		
		if(properties == null) {
			ComponentProperties properties = new ComponentProperties();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}

	protected IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManager();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setFormDocument(form_document);
			xforms_manager.setFormComponent(this);
		}
		
		return xforms_manager;
	}
	
	protected HtmlManager getHtmlManager() {
		
		if(html_manager == null) {
			
			html_manager = new HtmlManager();
			html_manager.setCacheManager(CacheManager.getInstance());
			html_manager.setFormDocument(form_document);
			html_manager.setFormComponent(this);
		}
		
		return html_manager;
	}
	
	public String getType() {
		return type;
	}
	public void updateErrorMsg() {
		getXFormsManager().updateErrorMsg();
		getHtmlManager().clearLocalizedHtmlComponents();
		form_document.setFormDocumentModified(true);
	}
	public void updateLabel() {
		getXFormsManager().updateLabel();
		getHtmlManager().clearLocalizedHtmlComponents();
	}
	public void updateConstraintRequired() {
		getXFormsManager().updateConstraintRequired();
		form_document.setFormDocumentModified(true);
	}
	
	public void remove() {
		
		form_document.setFormDocumentModified(true);
		
		getXFormsManager().removeComponentFromXFormsDocument();
	}
}