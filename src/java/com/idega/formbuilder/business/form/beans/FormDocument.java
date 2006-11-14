package com.idega.formbuilder.business.form.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.data.StringInputStream;
import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IPersistenceManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormDocument implements IFormDocument, IFormComponentParent {
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private Document form_xforms;
	private Document components_xml;
	private List<String> form_components_id_sequence;
	
	private int last_component_id = 0;
	private String form_id;
	protected IPersistenceManager persistence_manager;
	private String submit_button_id;
	
	private Map<String, IFormComponent> form_components;
	
	public void createDocument(String form_id, LocalizedStringBean form_name) throws NullPointerException {
		
		if(form_id == null)
			throw new NullPointerException("Form_id is not provided");
		
		IPersistenceManager persistence_manager = getPersistenceManager();
		persistence_manager.init(form_id);
		
		clear();
		
		this.form_id = form_id;
		
		form_xforms = CacheManager.getInstance().getFormXformsTemplateCopy();
		
		putIdValues();
		
		if(form_name != null)
			setFormTitle(form_name);
		
		loadSubmitButton();
	}
	
	protected void putIdValues() {
		Element model = FormManagerUtil.getElementByIdFromDocument(form_xforms, FormManagerUtil.head_tag, FormManagerUtil.form_id);
		model.setAttribute(FormManagerUtil.id_att, form_id);
		
		Element form_id_element = (Element)model.getElementsByTagName(FormManagerUtil.form_id_tag).item(0);
		form_id_element.setTextContent(form_id);
		
		Element submission_element = (Element)model.getElementsByTagName(FormManagerUtil.submission_tag).item(0);
		String action_att_val = submission_element.getAttribute(FormManagerUtil.action_att);
		submission_element.setAttribute(FormManagerUtil.action_att, action_att_val+form_id);
	}
	
	public String addComponent(String component_type, String component_after_this_id) throws NullPointerException {
		
		IFormComponent component = FormComponentFactory.getInstance().getFormComponentByType(component_type);

		if(component_after_this_id != null) {
			
			IFormComponent comp_after_new = getFormComponent(component_after_this_id);
			
			if(comp_after_new == null)
				throw new NullPointerException("Component after not found");
			
			component.setComponentAfterThis(comp_after_new);
		}
		
		String component_id = FormManagerUtil.CTID+generateNewComponentId();
		
		component.setId(component_id);
		component.setFormDocument(this);
		
		component.render();
		
		getFormComponents().put(component_id, component);
		
		return component_id;
	}
	
	protected void loadSubmitButton() {
		
		FormComponentSubmitButton component = new FormComponentSubmitButton();
		
		component.setFormDocument(this);
		component.render();
		
		submit_button_id = component.getId();
		
		if(submit_button_id != null)
			getFormComponents().put(submit_button_id, component);
	}
	
	public FormComponentSubmitButton getSubmitButtonComponent() {
		
		return (FormComponentSubmitButton)getFormComponents().get(submit_button_id);
	}
	
	protected int generateNewComponentId() {
		
		return ++last_component_id;
	}
	
	public Document getXformsDocument() {
		return form_xforms;
	}

	public List<String> getFormComponentsIdList() {
		
		if(form_components_id_sequence == null)
			form_components_id_sequence = new LinkedList<String>();
		
		return form_components_id_sequence;
	}

	public IFormComponent getFormComponent(String component_id) {
		
//		TODO: if not found, scan xforms document and load if found
		return getFormComponents().get(component_id);
	}
	
	protected Map<String, IFormComponent> getFormComponents() {
		
		if(form_components == null)
			form_components = new HashMap<String, IFormComponent>();
		
		return form_components;
	}
	
	public void unregisterComponent(String component_id) {
		
		getFormComponentsIdList().remove(component_id);
		getFormComponents().remove(component_id);
	}
	
	public Exception[] getSavedExceptions() {

		return persistence_manager.getSavedExceptions(); 
	}
	
	public void setPersistenceManager(IPersistenceManager persistence_manager) {
		
		if(persistence_manager != null)
			this.persistence_manager = persistence_manager;
	}
	
	public void persist() throws NullPointerException, InitializationException, Exception {
		
		IPersistenceManager persistence_manager = getPersistenceManager();
		
		if(!persistence_manager.isInitiated())
				persistence_manager.init(form_id);
		
		persistence_manager.persistDocument(form_xforms);
	}
	
	protected IPersistenceManager getPersistenceManager() throws NullPointerException {
		
		if(persistence_manager == null)
			throw new NullPointerException("Persistence manager is not provided");
		
		return persistence_manager;
	}
	
	public void rearrangeDocument() throws NullPointerException {
		
		int size = form_components_id_sequence.size();
		
		for (int i = size-1; i >= 0; i--) {
			
			String component_id = form_components_id_sequence.get(i);
			
			if(form_components.containsKey(component_id)) {
				
				IFormComponent comp = form_components.get(component_id);
				
				if(i != size-1) {
					
					comp.setComponentAfterThisRerender(
						form_components.get(
								form_components_id_sequence.get(i+1)
						)
					);
				} else
					comp.setComponentAfterThisRerender(null);
				
			} else
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
		}
	}
	
	protected void clear() {
		
		form_xforms = null;
		components_xml = null;
		getFormComponentsIdList().clear();
		
		last_component_id = 0;
		form_id = null;
		submit_button_id = null;
		
		getFormComponents().clear();
	}
	
	private boolean document_changed = true;
	
	public void setFormDocumentModified(boolean changed) {
		document_changed = changed;
	}
	
	public boolean isFormDocumentModified() {
		return document_changed;
	}
	public Document getComponentsXml() {
		
		return components_xml;
	}
	public void setComponentsXml(Document xml) {
		components_xml = xml;
	}
	public String getFormId() {
		return form_id;
	}
	public Document getFormXFormsDocument() {
		return (Document)form_xforms.cloneNode(true);
	}
	
	protected void loadDocument(Document xforms_doc, String form_id) throws InitializationException, Exception {
		
		clear();
		
		this.form_xforms = xforms_doc;
		this.form_id = form_id;
		
		List<String[]> components_tag_names_and_ids = FormManagerUtil.getComponentsTagNamesAndIds(xforms_doc);
		
		FormComponentFactory components_factory = FormComponentFactory.getInstance();
		
		List<String> components_id_list = getFormComponentsIdList();
		
		for (Iterator<String[]> iter = components_tag_names_and_ids.iterator(); iter.hasNext();) {
			
			String[] ctnaid = iter.next();
			IFormComponent component = components_factory.recognizeFormComponent(ctnaid[0]);
			String component_id = ctnaid[1];
			
			component.setId(component_id);
			component.setFormDocument(this);
			component.setLoad(true);
			
			if(component instanceof FormComponentSubmitButton) {
				submit_button_id = component_id;
			} else {
				
				components_id_list.add(component_id);
			}
			
			getFormComponents().put(component_id, component);
			
			component.render();
		}
	}
	
	public void loadDocument(String form_id) throws InitializationException, Exception {
		
		if(form_id == null)
			throw new NullPointerException("Form document id was not provided");
		
		IPersistenceManager persistence_manager = getPersistenceManager();
		persistence_manager.init(form_id);
		Document xforms_doc = persistence_manager.loadDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form document was not found by provided id");
		
		loadDocument(xforms_doc, form_id);
	}
	
	public String getXFormsDocumentSourceCode() throws Exception {
		
		return FormManagerUtil.serializeDocument(form_xforms);
	}
	public void setXFormsDocumentSourceCode(String src_code) throws Exception {
		
		DocumentBuilder builder = FormManagerUtil.getDocumentBuilder();
		Document new_xforms_document = builder.parse(new StringInputStream(src_code));
		
		Element new_document_root = (Element)form_xforms.adoptNode(new_xforms_document.getDocumentElement());
		
		form_xforms.replaceChild(new_document_root, form_xforms.getDocumentElement());
		
		loadDocument(form_xforms, form_id);
	}
	
	public void setFormTitle(LocalizedStringBean form_name) {
		
		if(form_name == null)
			throw new NullPointerException("Form name is not provided.");
		
		Element title = (Element)form_xforms.getElementsByTagName(FormManagerUtil.title_tag).item(0);
		Element output = (Element)title.getElementsByTagName(FormManagerUtil.output_tag).item(0);
		
		try {
			
			FormManagerUtil.putLocalizedText(null, null, output, form_xforms, form_name);
			
		} catch (Exception e) {
			logger.error("Could not set localized text for title element", e);
		}
	}
	
	public LocalizedStringBean getFormTitle() {
		
		return FormManagerUtil.getTitleLocalizedStrings(form_xforms);
	}
}