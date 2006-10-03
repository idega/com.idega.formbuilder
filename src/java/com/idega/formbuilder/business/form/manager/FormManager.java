package com.idega.formbuilder.business.form.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.beans.FormBean;
import com.idega.formbuilder.business.form.manager.generators.ComponentsGeneratorFactory;
import com.idega.formbuilder.business.form.manager.generators.IComponentsGenerator;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
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
	private static final String FB_INIT_FAILED = "Could not instantiate FormManager. See \"caused by\" for details.";
	
	private DocumentManager doc_manager;
	private ComponentsManager comp_manager;
	private IPersistenceManager persistence_manager;
	
	public void createFormDocument(FormPropertiesBean form_properties) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		doc_manager.createDocument(form_properties);
	}
	
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		comp_manager.removeFormComponent(component_id);
	}
	
	public Element getLocalizedFormHtmlComponent(String component_id, String loc_str) {
		
		return comp_manager.getLocalizedFormHtmlComponent(component_id, loc_str);
	}
	
	public String createFormComponent(String component_type, String component_after_new_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		return comp_manager.createFormComponent(component_type, component_after_new_id);
	}
	
	protected FormManager() {	}
	
	public List<String> getAvailableFormComponentsList() {
		
		return CacheManager.getInstance().getAvailableFormComponentsList();
	}
	
	public List<String> getFormComponentsList() {
		return doc_manager.getFormComponentsList();
	}
	
	/**
	 * 
	 * @return instance of this class. FormManager should be initiated first by calling init()
	 * @throws InstantiationException - if FormManager was not initiated.
	 */
	public static IFormManager getInstance() throws InstantiationException {
		
		if(!inited)
			throw new InstantiationException(NOT_INITED_MSG);
		
		FormManager fm = new FormManager();
		FormBean fb = new FormBean();
		fm.persistence_manager = PersistenceManagerFactory.newPersistenceManager();
		fm.doc_manager = DocumentManager.getInstance(fb, fm.persistence_manager);
		fm.comp_manager = ComponentsManager.getInstance(fb, fm.persistence_manager);
			
		return fm;
	}
	
	public static void init(FacesContext ctx) throws InstantiationException {
		
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
			InstantiationException inst_e = new InstantiationException(FB_INIT_FAILED);
			inst_e.initCause(e);
			throw inst_e;
		}
		
		init();
	}
	
	/**
	 * Should be called, before getting an instance of this class
	 * @throws InstantiationException - smth bad happened during init phase
	 */
	protected static void init() throws InstantiationException {
		
		long start = 0;
		
		if(logger.isDebugEnabled())
			start = System.currentTimeMillis();
			
		if(inited) {
			
			logger.error("init(): tried to call, when already inited");
			throw new InstantiationException("FormManager is already instantiated.");
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
			
			components_types = ComponentsManager.gatherAvailableComponentsTypes(components_xml);
			
			form_xforms_template = doc_builder.parse(new FileInputStream(FORM_XFORMS_TEMPLATE_CONTEXT_PATH));
			
//			DOMUtil.prettyPrintDOM(components_xml);
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

			InstantiationException inst_e = new InstantiationException(FB_INIT_FAILED);
			inst_e.initCause(e);
			throw inst_e;
		}
	}
	
	/**
	 * Check for exceptions thrown during previous requests
	 * @throws FBPostponedException - if some kind of exception happened during previous request. FormManager user knows,
	 * that error happened and can (most likely) happen again, so some adequate actions can be taken. 
	 */
	private void checkForPendingErrors() throws FBPostponedException {
		
		Exception[] saved_exceptions = persistence_manager.getSavedExceptions();
		
		if(saved_exceptions != null && saved_exceptions.length != 0)
			throw new FBPostponedException(saved_exceptions[0]);
	}
	
	public static boolean isInited() {
		return inited;
	}
	
	public static void main(String[] args) {

		try {
			long start = System.currentTimeMillis();
			IFormManager fb = FormManagerFactory.newFormManager(null);
			long end = System.currentTimeMillis();
			System.out.println("inited in: "+(end-start));
//			System.out.println("<sugeneruoti komponentai > ");
//			DOMUtil.prettyPrintDOM(components_xml);
//			System.out.println("<sugeneruoti komponentai />");
			
			FormPropertiesBean form_props = new FormPropertiesBean();
			form_props.setId(123L);
			
			LocalizedStringBean title = new LocalizedStringBean();
			title.setString("en", "eng title");
			title.setString("is", "isl title");
			
			form_props.setName(title);

			start = System.currentTimeMillis();
			fb.createFormDocument(form_props);
			end = System.currentTimeMillis();
			System.out.println("document created in: "+(end-start));
			
			start = System.currentTimeMillis();
			fb.createFormComponent("fbcomp_text", null);
			end = System.currentTimeMillis();
			System.out.println("text component created in: "+(end-start));
			
			Element loc = fb.getLocalizedFormHtmlComponent("fbcomp_1", "en");
			DOMUtil.prettyPrintDOM(loc);
			loc = fb.getLocalizedFormHtmlComponent("fbcomp_1", "is");
			DOMUtil.prettyPrintDOM(loc);
			
			fb.createFormComponent("fbcomp_email", null);
			loc = fb.getLocalizedFormHtmlComponent("fbcomp_2", "en");
			DOMUtil.prettyPrintDOM(loc);
			
////			
//			start = System.currentTimeMillis();
//			fb.createFormComponent("fbcomp_email", null);
//			end = System.currentTimeMillis();
//			System.out.println("email component created in: "+(end-start));
			
//			
//			Element textarea = fb.createFormComponent("fbcomp_textarea", "fbcomp_2");
//			DOMUtil.prettyPrintDOM(textarea);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}