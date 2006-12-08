package com.idega.formbuilder.business.form.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.xslt.TransformerService;
import org.chiba.xml.xslt.impl.CachingTransformerService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.block.form.business.BundleResourceResolver;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.business.form.beans.ComponentPropertiesSubmitButton;
import com.idega.formbuilder.business.form.beans.FormDocument;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormDocument;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.generators.FormComponentsGenerator;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.idegaweb.DefaultIWBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Class responsible for managing user's currently editing form.
 * 
 */
public class FormManager implements IFormManager {
	
	private static Log logger = LogFactory.getLog(FormManager.class);
	
	public static final String COMPONENTS_XFORMS_CONTEXT_PATH = "resources/templates/myComponents.xhtml";
	public static final String COMPONENTS_XSD_CONTEXT_PATH = "resources/templates/default-components.xsd";
	public static final String FORM_XFORMS_TEMPLATE_RESOURCES_PATH = "resources/templates/form-template.xhtml";

	private static boolean inited = false;
	
	private static final String NOT_INITED_MSG = "Init FormManager first";
	private static final String FB_INIT_FAILED = "Could not initialize FormManager. See \"caused by\" for details.";
	
	private IFormDocument form_document;
	
	public void createFormDocument(String form_id, LocalizedStringBean form_name) throws NullPointerException, Exception {
		
		form_document.createDocument(form_id, form_name);
		form_document.persist();
	}
	
	public void openFormDocument(String form_id) throws NullPointerException, Exception {
		
		form_document.loadDocument(form_id);
	}
	
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		IFormComponent component = form_document.getFormComponent(component_id);
		
		if(component == null)
			throw new NullPointerException("Component was not found");
		
		component.remove();
		form_document.unregisterComponent(component_id);
		
		form_document.persist();
	}
	
	public Element getLocalizedFormHtmlComponent(String component_id, Locale locale) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		IFormComponent component = form_document.getFormComponent(component_id);
		
		if(component == null)
			throw new NullPointerException("Component was not found");
		
		try {
			return component.getHtmlRepresentationByLocale(locale);
			
		} catch (NullPointerException e) {
			throw e;
		} catch (Exception e) {
			NullPointerException nul_e = new NullPointerException("Html representation could not be found.");
			nul_e.setStackTrace(e.getStackTrace());
			throw nul_e;
		}
	}
	
	public Element getLocalizedSubmitComponent(Locale locale) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		IFormComponent submit_component = form_document.getSubmitButtonComponent();
		
		if(submit_component == null)
			throw new NullPointerException("Submit button was not found in document");
		
		try {
			return submit_component.getHtmlRepresentationByLocale(locale);
			
		} catch (NullPointerException e) { 
			throw e;
		} catch (Exception e) {
			NullPointerException nul_e = new NullPointerException("Html representation could not be found.");
			nul_e.setStackTrace(e.getStackTrace());
			throw nul_e;
		}
	}
	
	public String createFormComponent(String component_type, String component_after_this_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		String component_id = form_document.addComponent(component_type, component_after_this_id);
		
		form_document.persist();
		
		return component_id;
	}
	
	public void updateFormComponent(String component_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		IFormComponent component = form_document.getFormComponent(component_id);
		
		if(component == null)
			throw new NullPointerException("Component with such an id was not found on document");
		
		component.render();
		form_document.persist();
	}
	
	protected FormManager() {	}
	
	public List<String> getAvailableFormComponentsTypesList() throws FBPostponedException {
		
		return CacheManager.getInstance().getAvailableFormComponentsTypesList();
	}
	
	public List<String> getFormComponentsIdsList() {
		
		return form_document.getFormComponentsIdList();
	}
	
	public IComponentProperties getComponentProperties(String component_id) {
		
		return form_document.getFormComponent(component_id).getProperties();
	}
	
	public ComponentPropertiesSubmitButton getSubmitButtonProperties() {
		
		return (ComponentPropertiesSubmitButton)form_document.getSubmitButtonComponent().getProperties();
	}
	
	/**
	 * @return instance of this class. FormManager must be initiated first by calling init()
	 * @throws InitializationException - if FormManager was not initiated before.
	 */
	public static IFormManager getInstance() throws InitializationException {
		
		if(!inited)
			throw new InitializationException(NOT_INITED_MSG);
		
		FormManager fm = new FormManager();
		
		IFormDocument form_document = new FormDocument();
		fm.form_document = form_document;
			
		return fm;
	}
	
	public static void init(FacesContext ctx) throws InitializationException {
		if(inited) {			
			logger.error("init(): tried to call, when already inited");
			throw new InitializationException("FormManager is already initialized.");
		}

		long start = System.currentTimeMillis();
		try {
			// setup where to get files from - workspace or bundle
			String bundleInWorkspace = null;
			IWBundle bundle = null;
			
			String workspaceDir = System.getProperty(DefaultIWBundle.SYSTEM_BUNDLES_RESOURCE_DIR);
			if (workspaceDir != null) {
				bundleInWorkspace = workspaceDir + "/" + IWBundleStarter.IW_BUNDLE_IDENTIFIER + "/";
			}
			
			TransformerService transf_service = null;
			if(ctx != null) {
				IWMainApplication iw_app = IWMainApplication.getIWMainApplication(ctx);
				bundle = iw_app.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
				transf_service = (TransformerService) iw_app.getAttribute(TransformerService.class.getName());
			}
			else {
				transf_service = new CachingTransformerService(new BundleResourceResolver(null));
			}

			// load xml files
			Document components_xforms = getDocumentFromBundle(bundleInWorkspace, bundle, COMPONENTS_XFORMS_CONTEXT_PATH);
			Document components_xsd = getDocumentFromBundle(bundleInWorkspace, bundle, COMPONENTS_XSD_CONTEXT_PATH);
			Document form_xforms_template = getDocumentFromBundle(bundleInWorkspace, bundle, FORM_XFORMS_TEMPLATE_RESOURCES_PATH);
			
			// setup ComponentsGenerator
			FormComponentsGenerator components_generator = FormComponentsGenerator.getInstance();
			components_generator.setTransformerService(transf_service);
			components_generator.setDocument(components_xforms);
			
			Document components_xml = components_generator.generateBaseComponentsDocument();
			List<String> components_types = FormManagerUtil.gatherAvailableComponentsTypes(components_xml);
			
			// cache results
			CacheManager cache_manager = CacheManager.getInstance();
			cache_manager.initAppContext(ctx);
			
			cache_manager.setFormXformsTemplate(form_xforms_template);
			cache_manager.setComponentsTypes(components_types);
			cache_manager.setComponentsXforms(components_xforms);
			cache_manager.setComponentsXml(components_xml);
			cache_manager.setComponentsXsd(components_xsd);
			
			inited = true;
			
			long end = System.currentTimeMillis();
			logger.info("FormManager initialized in: "+(end-start));
			
		} catch (Exception e) {

			logger.error(FB_INIT_FAILED, e);
			throw new InitializationException(FB_INIT_FAILED, e);
		}
	}
	
	/**
	 * Parses xml file specified by pathWithinBundle and returns document.
	 * If bundleInWorkspace is not null, it is used, otherwise bundle should be provided.
	 * 
	 * @param bundleInWorkspace
	 * @param bundle
	 * @param pathWithinBundle
	 * @return
	 * @throws Exception
	 */
	private static Document getDocumentFromBundle(String bundleInWorkspace, IWBundle bundle, String pathWithinBundle) throws Exception {
		Document doc = null;
		InputStream stream = null;
		if (bundleInWorkspace != null) {
			stream = new FileInputStream(bundleInWorkspace + pathWithinBundle);
		}
		else {
			stream = bundle.getResourceInputStream(pathWithinBundle);
		}

		DocumentBuilder doc_builder = FormManagerUtil.getDocumentBuilder();
		doc = doc_builder.parse(stream);
		stream.close();

		return doc;
	}
	
	/**
	 * Check for exceptions thrown during previous requests
	 * @throws FBPostponedException - if some kind of exception happened during previous request. 
	 */
	protected void checkForPendingErrors() throws FBPostponedException {
		
		Exception[] saved_exceptions = form_document.getSavedExceptions();
		
		if(saved_exceptions != null && saved_exceptions.length != 0)
			throw new FBPostponedException(saved_exceptions[0]);
	}
	
	public static boolean isInited() {
		return inited;
	}
	
	public void rearrangeDocument() throws FBPostponedException, Exception {
		
		checkForPendingErrors();
		
		form_document.rearrangeDocument();
		form_document.persist();
	}
	
	public String getFormSourceCode() throws Exception {
		
		return form_document.getXFormsDocumentSourceCode();
	}
	
	public void setFormSourceCode(String new_source_code) throws Exception {
		
		form_document.setXFormsDocumentSourceCode(new_source_code);
	}
	
	public LocalizedStringBean getFormTitle() {
		
		return form_document.getFormTitle();
	}
	
	public void setFormTitle(LocalizedStringBean form_name) throws FBPostponedException, Exception {
		
		checkForPendingErrors();
		
		form_document.setFormTitle(form_name);
		form_document.persist();
	}
	
	public String getFormId() {
		
		return form_document.getFormId();
	}
	
	public Document getFormXFormsDocument() {
		
		return form_document.getFormXFormsDocument();
	}
	
	public Map<Integer, List<String>> getComponentsInPhases() {
		
		return form_document.getComponentsInPhases();
	}
}