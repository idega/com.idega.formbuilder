package com.idega.formbuilder.business.form.beans;

import java.util.HashMap;
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
public class FormDocument implements IFormDocument {
	
	protected static Log logger = LogFactory.getLog(FormDocument.class);
	
	protected Document form_xforms;
	protected String form_id;
	protected FormsService formsService;
	protected FormComponentDocument pages_container;
	
	private Locale default_document_locale;
	private Map<String, IFormComponent> form_components;
	private Document components_xml;
	private int last_component_id = 0;
	private Element wizard_instance_element;
	private boolean document_changed = true;
	private Timer saveTimer;
	private boolean saving = false;
	
	public FormDocument() {
		saveTimer = new Timer("FormDocument save");
		
		pages_container = new FormComponentDocument();
		pages_container.setFormDocument(this);
	}
	
	public static FormDocument createDocument(String form_id, LocalizedStringBean form_name) throws NullPointerException, Exception {

		if(form_id == null)
			throw new NullPointerException("Form id is not provided");
		
		FormDocument form_doc = new FormDocument();
		
		form_doc.form_xforms = CacheManager.getInstance().getFormXformsTemplateCopy();
		
		form_doc.pages_container.setLoad(true);
		
		if(form_name != null)
			form_doc.setFormTitle(form_name);
		
		form_doc.loadDocument(form_doc.getXformsDocument(), form_id);
		form_doc.putIdValues();

		return form_doc;
	}
	
	public com.idega.formbuilder.business.form.Document getDocument() {
		
		return pages_container;
	}
	
	protected void putIdValues() {
		Element model = FormManagerUtil.getElementByIdFromDocument(form_xforms, FormManagerUtil.head_tag, FormManagerUtil.form_id);
		model.setAttribute(FormManagerUtil.id_att, form_id);
		
		Element form_id_element = (Element)model.getElementsByTagName(FormManagerUtil.form_id_tag).item(0);
		FormManagerUtil.setElementsTextNodeValue(form_id_element, form_id);
	
	}
	public String generateNewComponentId() {
		
		return FormManagerUtil.CTID+(++last_component_id);
	}
	
	public Document getXformsDocument() {
		return form_xforms;
	}
	protected Map<String, IFormComponent> getFormComponents() {
		
		if(form_components == null)
			form_components = new HashMap<String, IFormComponent>();
		
		return form_components;
	}
	
	public Exception[] getSavedExceptions() {

		return new Exception[0]; 
	}
	
	public void persist() {
		// if document is already scheduled for saving don't do anything
		if (!saving && false) {
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
	public Document getFormXFormsDocumentClone() {
		return (Document)form_xforms.cloneNode(true);
	}
	
	protected void loadDocument(Document xforms_doc, String form_id) throws Exception {
		
		this.form_xforms = xforms_doc;
		this.form_id = form_id;
		
		pages_container.setContainerElement(FormManagerUtil.getComponentsContainerElement(xforms_doc));
		pages_container.loadContainerComponents();
	}
	
	public static FormDocument loadDocument(String form_id) throws InitializationException, Exception {
		
		if(form_id == null)
			throw new NullPointerException("Form id was not provided");
		
		FormDocument form_doc = new FormDocument();
		form_doc.pages_container.setLoad(true);
		
		Document xforms_doc = form_doc.loadXFormsDocument(form_id);
		form_doc.loadDocument(xforms_doc, form_id);
		
		return form_doc;
	}
	
	protected Document loadXFormsDocument(String form_id) throws Exception {
		
		Document xforms_doc = getFormsService().loadForm(form_id);
		
		if(xforms_doc == null)
			throw new NullPointerException("Form document was not found by provided id");
		
		return xforms_doc;
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
	
	public void tellComponentId(String component_id) {
		
		int id_number = FormManagerUtil.parseIdNumber(component_id);
		
		if(id_number > last_component_id)
			last_component_id = id_number;
	}
}