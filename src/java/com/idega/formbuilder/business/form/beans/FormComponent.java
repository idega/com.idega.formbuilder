package com.idega.formbuilder.business.form.beans;

import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.HtmlManager;
import com.idega.formbuilder.business.form.manager.XFormsManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponent implements IFormComponent {
	
	protected IFormComponent component_after_me;
	protected String component_id;
	protected String type;
	
	protected IFormComponentParent form_document;
	
	protected Element unlocalized_html_component;
	protected Map<Locale, Element> localized_html_components;
	
	protected IComponentProperties properties;
	protected boolean created = false;
	
	protected XFormsManager xforms_manager;
	protected HtmlManager html_manager;
	
	public void render() {
		
		Document xforms_doc = form_document.getXformsDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form Xforms document was not provided");
		
		if(!created) {
			
			XFormsManager xforms_manager = getXFormsManager();
			XFormsComponentDataBean xforms_component = xforms_manager.getXFormsComponentByType(type);
			
			xforms_manager.addComponentToDocument(component_id, 
					component_after_me == null ? null : component_after_me.getId(),
					xforms_component);
			
			xforms_manager.setXFormsComponentDataBean(xforms_component);
			
			ComponentProperties properties = (ComponentProperties)getProperties();
			
			properties.setPlainLabel(FormManagerUtil.getLabelLocalizedStrings(getId(), xforms_doc));
			properties.setPlainRequired(false);
			properties.setPlainErrorMsg(FormManagerUtil.getErrorLabelLocalizedStrings(getId(), xforms_doc));
			
			created = true;
		}
	}
	
	public void setComponentAfterThis(IFormComponent component) {
		
		if(component == null)
			throw new NullPointerException("Component not provided");
		
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
	
	public void setFormDocument(IFormComponentParent form_document) {
		this.form_document = form_document; 
	}
	
	public Element getHtmlRepresentationByLocale(Locale locale) {

		return getHtmlManager().getHtmlRepresentationByLocale(locale);
	}
	
	public IComponentProperties getProperties() {
		
		if(properties == null) {
			properties = new ComponentProperties();
			((ComponentProperties)properties).setXFormsManager(getXFormsManager());
		}
		
		return properties;
	}

	protected XFormsManager getXFormsManager() {
		
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
}