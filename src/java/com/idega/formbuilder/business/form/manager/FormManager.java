package com.idega.formbuilder.business.form.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.FormComponentFactory;
import com.idega.formbuilder.business.form.beans.FormDocument;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IComponentSelectProperties;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormDocument;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.generators.ComponentsGeneratorFactory;
import com.idega.formbuilder.business.form.manager.generators.IComponentsGenerator;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.formbuilder.sandbox.SandboxUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Class responsible for managing user's currently editing form.
 * 
 */
public class FormManager implements IFormManager {
	
	private static Log logger = LogFactory.getLog(FormManager.class);
	
	private static InputStream components_xforms_stream = null;
	private static InputStream components_xsd_stream = null;
	private static boolean inited = false;
	
	private static String COMPONENTS_XFORMS_CONTEXT_PATH = null;
	private static String COMPONENTS_XSD_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = null;
	private static String FORM_XFORMS_TEMPLATE_CONTEXT_PATH = null;
	
	private static final String NOT_INITED_MSG = "Init FormManager first";
	private static final String FB_INIT_FAILED = "Could not initialize FormManager. See \"caused by\" for details.";
	
	private IFormDocument form_document;
	
	public void createFormDocument(String form_id, LocalizedStringBean form_name) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		form_document.createDocument(form_id, form_name);
		form_document.persist();
	}
	
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		throw new NullPointerException("__Not implemented__");
	}
	
	public Element getLocalizedFormHtmlComponent(String component_id, Locale locale) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		IFormComponent component = form_document.getFormComponent(component_id);
		
		if(component == null)
			throw new NullPointerException("Component was not found");
		
		return component.getHtmlRepresentationByLocale(locale);
	}
	
	public String createFormComponent(String component_type, String component_after_this_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		IFormComponent component = FormComponentFactory.getInstance().getFormComponentByType(component_type);

		if(component_after_this_id != null) {
			
			IFormComponent comp_after_new = form_document.getFormComponent(component_after_this_id);
			
			if(comp_after_new == null)
				throw new NullPointerException("Component after not found");
			
			component.setComponentAfterThis(comp_after_new);
		}
		
		form_document.addComponent(component);
		
		form_document.persist();
		
		return component.getId();
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
	
	/**
	 * @return instance of this class. FormManager must be initiated first by calling init()
	 * @throws InitializationException - if FormManager was not initiated before.
	 */
	public static IFormManager getInstance() throws InitializationException {
		
		if(!inited)
			throw new InitializationException(NOT_INITED_MSG);
		
		FormManager fm = new FormManager();
		
		IFormDocument form_document = new FormDocument();
		form_document.setPersistenceManager(PersistenceManagerFactory.newPersistenceManager());
		fm.form_document = form_document;
			
		return fm;
	}
	
	public static void init(FacesContext ctx) throws InitializationException {
		
		COMPONENTS_XFORMS_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMS_CONTEXT_PATH;
		COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH;
		COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH;
		FORM_XFORMS_TEMPLATE_CONTEXT_PATH = SandboxUtil.FORM_XFORMS_TEMPLATE_CONTEXT_PATH;
		COMPONENTS_XSD_CONTEXT_PATH = SandboxUtil.COMPONENTS_XSD_CONTEXT_PATH;
		
		try {
			
//			IWContext iwc = IWContext.getInstance();
//			IBOSession ses_bean = IBOLookup.getSessionInstance(iwc, IWSlideSession.class);			
//			components_xforms_stream = ((IWSlideSessionBean)ses_bean).getInputStream(COMPONENTS_XFORMS_CONTEXT_PATH);
//			components_xsd_stream = ((IWSlideSessionBean)ses_bean).getInputStream(COMPONENTS_XSD_CONTEXT_PATH);
			
			CacheManager.getInstance().initAppContext(ctx);
			
		} catch (Exception e) {
			
			throw new InitializationException(FB_INIT_FAILED, e);
		}
		
		init();
	}
	
	/**
	 * Should be called, before getting an instance of this class
	 * @throws InstantiationException - smth bad happened during init phase
	 */
	protected static void init() throws InitializationException {
		
		long start = 0;
		
		if(logger.isDebugEnabled())
			start = System.currentTimeMillis();
			
		if(inited) {
			
			logger.error("init(): tried to call, when already inited");
			throw new InitializationException("FormManager is already initialized.");
		}
		
		try {
			
			IComponentsGenerator components_generator = ComponentsGeneratorFactory.createComponentsGenerator();
			components_generator.init(
					new String[] {null, COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH, 
							COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH
							}
					);
			
			Document components_xforms = null;
			Document components_xsd = null;
			Document components_xml = null;
			List<String> components_types = null;
			Document form_xforms_template = null;
			
			DocumentBuilder doc_builder = FormManagerUtil.getDocumentBuilder();
			
			if(components_xforms_stream != null) {
				
				components_xforms = doc_builder.parse(components_xforms_stream);
				components_xforms_stream = null;
			} else
				components_xforms = doc_builder.parse(new FileInputStream(COMPONENTS_XFORMS_CONTEXT_PATH));
			
			if(components_xsd_stream != null) {
				
				components_xsd = doc_builder.parse(components_xsd_stream);
				components_xsd_stream = null;
			} else
				components_xsd = doc_builder.parse(new FileInputStream(COMPONENTS_XSD_CONTEXT_PATH));
			
			components_generator.setDocument(components_xforms);
			components_xml = components_generator.generateBaseComponentsDocument();
			
			components_types = FormManagerUtil.gatherAvailableComponentsTypes(components_xml);
			
			form_xforms_template = doc_builder.parse(new FileInputStream(FORM_XFORMS_TEMPLATE_CONTEXT_PATH));
			
			CacheManager cache_manager = CacheManager.getInstance();
			cache_manager.setFormXformsTemplate(form_xforms_template);
			cache_manager.setComponentsTypes(components_types);
			cache_manager.setComponentsXforms(components_xforms);
			cache_manager.setComponentsXml(components_xml);
			cache_manager.setComponentsXsd(components_xsd);
			
			inited = true;
			
			if(logger.isDebugEnabled()) {
				long end = System.currentTimeMillis();
				
				logger.debug("init() time: "+(end-start));
			}
			
		} catch (Exception e) {

			throw new InitializationException(FB_INIT_FAILED, e);
		}
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
	
	public void rearrangeDocument() {
		
	}
	
	public static void main(String[] args) {

		try {
			long start = System.currentTimeMillis();
			IFormManager fm = FormManagerFactory.newFormManager(null);
			long end = System.currentTimeMillis();
			//System.out.println("inited in: "+(end-start));
			
			LocalizedStringBean title = new LocalizedStringBean();
			title.setString(new Locale("en"), "eng title");
			title.setString(new Locale("is"), "isl title");
			
			start = System.currentTimeMillis();
			fm.createFormDocument("11", title);
			end = System.currentTimeMillis();
			//System.out.println("document created in: "+(end-start));
			
			start = System.currentTimeMillis();
			String created = fm.createFormComponent("fbcomp_text", null);
			
//			Element html = fm.getLocalizedFormHtmlComponent(created, new Locale("en"));
//			
//			DOMUtil.prettyPrintDOM(html);
//			
//			html = fm.getLocalizedFormHtmlComponent(created, new Locale("en"));
			
			
//			IComponentProperties props = fm.getComponentProperties(created);
//			
//			System.out.println("what str bean is now: "+props.getLabel());
			
//			LocalizedStringBean loc_str = new LocalizedStringBean();
//			loc_str.setString(new Locale("en"), null);
//			props.setLabel(loc_str);
//			
//			fm.updateFormComponent(created);
			
			end = System.currentTimeMillis();
			//System.out.println("text component created in: "+(end-start));
			
//			IComponentProperties properties = fm.getComponentProperties(created);
//			
//			properties.setRequired(false);
//			properties.setErrorMsg(null);
//			
//			fm.updateFormComponent(created);
//			
//			created = fm.createFormComponent("fbcomp_email", created);
			
//			Element loc = fm.getLocalizedFormHtmlComponent(created, new Locale("en"));
//			Element loc2 = fm.getLocalizedFormHtmlComponent(created, new Locale("is"));
//			
//			System.out.println("english one");
//			DOMUtil.prettyPrintDOM(loc);
//			System.out.println("icelandish");
//			DOMUtil.prettyPrintDOM(loc2);
//			
//			created = fm.createFormComponent("fbcomp_email", created);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}