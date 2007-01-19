package com.idega.formbuilder.business.form.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.block.form.business.FormsService;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.StringInputStream;
import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormDocument implements IFormDocument, IFormComponentParent {
	
	static Log logger = LogFactory.getLog(FormDocument.class);
	
	Document form_xforms;
	private Document components_xml;
	private List<String> form_components_id_sequence;
	
	private int last_component_id = 0;
	String form_id;
	private String submit_button_id;
	private Element wizard_instance_element;

	private boolean document_changed = true;

	protected FormsService formsService;
	boolean saving = false;
	private Timer saveTimer;
	
	private Locale default_document_locale;
	
	private Map<String, IFormComponent> form_components;
	
	public FormDocument() {
		saveTimer = new Timer("FormDocument save");		
	}
	
	public void createDocument(String form_id, LocalizedStringBean form_name) throws NullPointerException {
		
		if(form_id == null)
			throw new NullPointerException("Form_id is not provided");
		
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
		FormManagerUtil.setElementsTextNodeValue(form_id_element, form_id);
		
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

		return new Exception[0]; 
	}
	
	public void persist() {
		// if document is already scheduled for saving don't do anything
		if (!saving) {
			saving = true;
			TimerTask saveTask = new FormSaveTask();
			// will save current state of document after 5 seconds
			saveTimer.schedule(saveTask, 5000);
		}
	}
	
	/**
	 * Saves a document
	 */
	protected class FormSaveTask extends TimerTask {

		public void run() {
			try {
				getFormsService().saveForm(form_id, form_xforms);
			}
			catch (Exception e) {
				logger.error("Error saving form document", e);
			}
			saving = false;
		}
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
		default_document_locale = null;
		
		getFormComponents().clear();
	}
	
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
		
		last_component_id = FormManagerUtil.getLastId(getFormComponentsIdList());
	}
	
	public void loadDocument(String form_id) throws InitializationException, Exception {
		
		if(form_id == null)
			throw new NullPointerException("Form document id was not provided");
		
		Document xforms_doc = getFormsService().loadForm(form_id);
		
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
		
		Element new_document_root = (Element)form_xforms.importNode(new_xforms_document.getDocumentElement(), true);
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
	
	public Locale getDefaultLocale() {
		
		if(default_document_locale == null)
			default_document_locale = FormManagerUtil.getDefaultFormLocale(form_xforms);
		
		return default_document_locale;
	}
	
	public Element getWizardElement() {
		
		if(wizard_instance_element == null)
			wizard_instance_element = FormManagerUtil.getElementByIdFromDocument(form_xforms, FormManagerUtil.head_tag, FormManagerUtil.wizard_id_att_val);
		
//		if wizard_instance_element not found - leave creation of it to xformsmanager,
		
		return wizard_instance_element;
	}
	
	public void setWizardElement(Element wizard_element) {
		
		wizard_instance_element = wizard_element;
	}
	
	public Map<Integer, List<String>> getComponentsInPhases() {
		
		Map<String, IFormComponent> form_components = getFormComponents();
		Map<Integer, List<String>> components_in_phases = new HashMap<Integer, List<String>>();
		
		for (Iterator<String> iter = form_components.keySet().iterator(); iter.hasNext();) {
			
			String comp_id = iter.next();
			IComponentProperties props = form_components.get(comp_id).getProperties();
			
			Integer phase_nr = props.getPhaseNumber();
			if(phase_nr == null)
				phase_nr = 0;
			
			List<String> components_ids = components_in_phases.get(phase_nr);
				
			if(components_ids == null) {
				
				components_ids = new ArrayList<String>();
				components_in_phases.put(phase_nr, components_ids);
			}
			
			components_ids.add(comp_id);
		}
		
		return components_in_phases;
	}

	FormsService getFormsService() {
		
		if (this.formsService == null) {
		try {
			IWApplicationContext iwc = IWMainApplication.getDefaultIWApplicationContext();
			this.formsService = (FormsService) IBOLookup.getServiceInstance(iwc, FormsService.class);
		}
		catch (IBOLookupException e) {
			logger.error("Could not find FormsService");
		}
		}
		return this.formsService;
	}
	
}