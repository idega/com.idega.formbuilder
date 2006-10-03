package com.idega.formbuilder.business.form.manager;

import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.manager.beans.FormBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class DocumentManager {
	
	private static Log logger = LogFactory.getLog(FormManager.class);
	private FormBean form_bean;
	private IPersistenceManager persistance_manager;
	
	protected DocumentManager() { }
	
	public static DocumentManager getInstance(FormBean form_bean, IPersistenceManager persistance_manager) throws NullPointerException {
		
		if(form_bean == null)
			throw new NullPointerException("FormBean not provided");
		
		if(persistance_manager == null)
			throw new NullPointerException("IPersistanceManager not provided");
			
		DocumentManager doc_man = new DocumentManager();
		doc_man.form_bean = form_bean;
		doc_man.persistance_manager = persistance_manager;
		
		return doc_man;
	}

	public void createDocument(FormPropertiesBean form_properties) throws NullPointerException, PersistenceException {
		
		if(form_properties == null)
			throw new NullPointerException("Form properties not provided");
		
		Document form_xforms = CacheManager.getInstance().getFormXformsTemplateCopy();
		
		String form_id_str;
		
		if(form_properties.getId() != null) {
			
			form_id_str = form_properties.getId().toString();
			
			NodeList nl = form_xforms.getElementsByTagName(FormManagerUtil.model_name);
			
			Element model = (Element)nl.item(0);
			model.setAttribute(FormManagerUtil.id_name, form_id_str);
			
		} else {
			throw new NullPointerException("Id not presented in form properties.");
		}
		
		form_bean.setFormProperties(form_properties);
		form_bean.setFormXforms(form_xforms);
		
		if(form_properties.getName() != null) {
			
			Element title = (Element)form_xforms.getElementsByTagName("title").item(0);
			Element output = (Element)title.getElementsByTagName(FormManagerUtil.output).item(0);
			
			try {
				
				FormManagerUtil.putLocalizedText(null, null, output, form_xforms, form_properties.getName());
				
			} catch (Exception e) {
				logger.error("Could not set localized text for title element", e);
			}
		}
		
//		DOMUtil.prettyPrintDOM(form_xforms);
		
		try {
			persistance_manager.init(form_id_str);
			persistance_manager.persistDocument(form_xforms);
			
		} catch (Exception e) {
			throw new PersistenceException("Document could not be saved: ", e);
		}
	}
	
	/**
	 * 
	 * @return List of all form components, placed on current form
	 */
	public List<String> getFormComponentsList() {
		return form_bean.getFormComponentsIdList();
	}
}
