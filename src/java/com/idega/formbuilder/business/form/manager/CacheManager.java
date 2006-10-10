package com.idega.formbuilder.business.form.manager;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.core.cache.IWCacheManager2;
import com.idega.formbuilder.business.form.beans.XFormsComponentDataBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.repository.data.Singleton;
import com.idega.util.caching.CacheMap;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class CacheManager implements Singleton {
	
	private static Log logger = LogFactory.getLog(CacheManager.class);
	
	private Document form_xforms_template = null;
	private Document components_xforms = null;
	private Document components_xsd = null;
	private Document components_xml = null;
	private List<String> components_types = null;
	private Map<String, XFormsComponentDataBean> cached_xforms_components;
	private Map<String, Element> cached_html_components;
	
	private static CacheManager me;
	
	public static CacheManager getInstance() {
		
		if (me == null) {
			
			synchronized (CacheManager.class) {
				if (me == null) {
					me = new CacheManager();
				}
			}
		}

		return me;
	}
	
	protected CacheManager() { }

	public Document getFormXformsTemplateCopy() {
		
		if(form_xforms_template == null)
			throw new NullPointerException("Form xforms template not initialized");
		
		return (Document)form_xforms_template.cloneNode(true);
	}

	public void setFormXformsTemplate(Document form_xforms_template) {
		
		if(form_xforms_template != null)
			this.form_xforms_template = form_xforms_template;
	}

	public List<String> getComponentsTypes() {
		return components_types;
	}

	public void setComponentsTypes(List<String> components_types) {
		this.components_types = components_types;
	}

	public Document getComponentsXforms() {
		return components_xforms;
	}

	public void setComponentsXforms(Document components_xforms) {
		this.components_xforms = components_xforms;
	}

	public Document getComponentsXml() {
		return components_xml;
	}

	public void setComponentsXml(Document components_xml) {
		this.components_xml = components_xml;
	}

	public Document getComponentsXsd() {
		return components_xsd;
	}

	public void setComponentsXsd(Document components_xsd) {
		this.components_xsd = components_xsd;
	}
	
	public void checkForComponentType(String component_type) throws NullPointerException {
		
		if(components_types == null || component_type == null || !components_types.contains(component_type)) {
			
			String msg;
			
			if(component_type == null)
				msg = "Component type is not provided (provided null)";
			
			else if(components_types == null)
				msg = "Components types are not initialized";
				
			else
				msg = "Component type given is not known. Given: "+component_type;
			
			throw new NullPointerException(msg);
		}
	}
	
	public Element getHtmlComponentReferenceByType(String component_type) throws NullPointerException {
		
		if(cached_html_components == null)
			cached_html_components = new CacheMap();
		
		Element html_component = cached_html_components.get(component_type); 

		if(html_component != null)
			return (Element)html_component.cloneNode(true);

		html_component = FormManagerUtil.getElementByIdFromDocument(components_xml, null, component_type);
		
		if(html_component == null) {
			String msg = "Component cannot be found in temporal components xml document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xml document.");
			throw new NullPointerException(msg);
		}
		
		cached_html_components.put(component_type, html_component);
		
		return (Element)html_component.cloneNode(true);
	}
	
	public List<String> getAvailableFormComponentsTypesList() {
		
		if(components_xforms == null) {
			
			logger.error("getFormComponentsList: components_xforms is null. Should not happen ever. Something bad.");
			return null;
		}
		
		return components_types;
	}
	
	public void initAppContext(FacesContext ctx) {
		
		if(ctx == null)
			return;
		
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(ctx);
		cached_html_components = IWCacheManager2.getInstance(iwma).getCache("cached_html_components");
		cached_xforms_components = IWCacheManager2.getInstance(iwma).getCache("cached_xforms_components");
	}
	
	public void cacheXformsComponent(String key, XFormsComponentDataBean xbean) {
		
		if(cached_xforms_components == null)
			cached_xforms_components = new CacheMap();
			
		cached_xforms_components.put(key, xbean);			
	}
	
	public void cacheHtmlComponent(String key, Element ebean) {
		
		if(cached_html_components == null)
			cached_html_components = new CacheMap();
			
		cached_html_components.put(key, ebean);			
	}
	
	public XFormsComponentDataBean getCachedXformsComponent(String key) {
		
		if(cached_xforms_components == null)
			return null;
			
		return cached_xforms_components.get(key);			
	}
	
	public Element getCachedHtmlComponent(String key) {
		
		if(cached_html_components == null)
			return null;
			
		return cached_html_components.get(key);			
	}
}