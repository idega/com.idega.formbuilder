
package com.idega.formbuilder.business.form;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.WebdavFile;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.business.IBOLookup;
import com.idega.business.IBOSession;
import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.generators.FormComponentsGenerator;
import com.idega.formbuilder.business.generators.IComponentsGenerator;
import com.idega.formbuilder.sandbox.SandboxChibaServlet;
import com.idega.formbuilder.util.FBUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWSlideServiceBean;
import com.idega.slide.business.IWSlideSession;
import com.idega.slide.business.IWSlideSessionBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Main FormBuilder model class. Knows about available form components, manages user's xforms document,
 * returns requested component type html.
 */
public class FormBuilder {
	
	private static Log logger = LogFactory.getLog(FormBuilder.class);
	
	private static Document components_xml = null;
	private static Document components_xforms = null;
	private static Document form_xforms_template = null;
	private static InputStream components_xforms_stream = null;
	private static boolean inited = false;
	private static List<String> components_types = null;
	
	private static String COMPONENTS_XFORMS_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = null;
	private static String FORM_XFORMS_TEMPLATE_CONTEXT_PATH = null;
	
	private Document form_xforms = null;
	
	public void addComponent() {
		
	}
	
	public void editComponent() {
		
	}
	
	public void removeComponent() {
		
	}
	
	public void createFormDocument(FormPropertiesBean form_props) {
		
		try {
			
			form_xforms = (Document)form_xforms_template.cloneNode(true);
			
			DOMUtil.prettyPrintDOM(form_xforms);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private FormBuilder() {	}
	
	/**
	 * 
	 * @return List of available form components types
	 */
	public List<String> getFormComponentsList() {
		
		if(components_xforms == null) {
			
			logger.error("getFormComponentsList: components_xforms is null. Should not happen ever. Something bad.");
			return null;
		}
		
		return components_types;
	}
	
	private static final String NOT_INITED_MSG = "Init FormBuilder first";
	
	/**
	 * 
	 * @return instance of this class. FormBuilder should be initiated first by calling init()
	 * @throws InstantiationException - if formbuilder was not initiated.
	 */
	public static FormBuilder getInstance() throws InstantiationException {
		
		if(!inited)
			throw new InstantiationException(NOT_INITED_MSG);
			
		return new FormBuilder();
	}
	
	public static void init(FacesContext ctx) throws InstantiationException {
		
		COMPONENTS_XFORMS_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/chiba-web/web/myBasicTest.xhtml";
//		COMPONENTS_XFORMS_CONTEXT_PATH = "/files/formbuilder/components/components.xforms";
		COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/resources/xslt/htmlxml.xsl";
		COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/resources/xslt/components.xsl";
		FORM_XFORMS_TEMPLATE_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/resources/templates/form.xhtml";
		
		try {
			
//			IWContext iwc = IWContext.getInstance();
//			IBOSession ses_bean = IBOLookup.getSessionInstance(iwc, IWSlideSession.class);			
//			components_xforms_stream = ((IWSlideSessionBean)ses_bean).getInputStream(COMPONENTS_XFORMS_CONTEXT_PATH);
			
		} catch (Exception e) {
			InstantiationException inst_e = new InstantiationException(FB_INIT_FAILED);
			inst_e.initCause(e);
			throw inst_e;
		}
		
		init();
	}
	
	private static final String FB_INIT_FAILED = "Could not instantiate FormBuilder. See \"caused by\" for details.";
	
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
			throw new InstantiationException("Formbuilder is already instantiated.");
		}
		
		try {
			
			IComponentsGenerator components_generator = FormComponentsGenerator.getInstance();
			components_generator.init(
					new String[] {null, COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH, 
							COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH
							}
					);
			
			DocumentBuilder doc_builder = FBUtil.getDocumentBuilder();
			
			if(components_xforms_stream != null)
				
				components_xforms = doc_builder.parse(components_xforms_stream);
			else
				components_xforms = doc_builder.parse(new FileInputStream(COMPONENTS_XFORMS_CONTEXT_PATH));
			
			((FormComponentsGenerator)components_generator).setXFormsDocument(components_xforms);
			components_xml = components_generator.generateBaseComponentsDocument();
			
			components_types = gatherAvailableComponentsTypes(components_xml);
			
			form_xforms_template = doc_builder.parse(new FileInputStream(FORM_XFORMS_TEMPLATE_CONTEXT_PATH));
			
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
	
	protected static String CTID = "fbcomp_";
	/**
	 * <p>
	 * @param components_xml - components xml document, which passes the structure described:
	 * <p>
	 * optional document root name - form_components
	 * </p>
	 * <p>
	 * Component is encapsulated into div tag, which contains component type as tag id.
	 * Every component div container is child of root.
	 * </p>
	 * <p>
	 * Component type starts with "fbcomp_"
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * &lt;form_components&gt;<br />
		&lt;div class="input" id="fbcomp_text"&gt;<br />
			&lt;label class="label" for="fbcomp_text-value" id="fbcomp_text-label"&gt;			Single Line Field		&lt;/label&gt;<br />
			&lt;input class="value" id="fbcomp_text-value" name="d_fbcomp_text"	type="text" value="" /&gt;<br />
		&lt;/div&gt;<br />
	&lt;/form_components&gt;
	 * </p>
	 * </p>
	 * 
	 * To change component type identification (which is now set to "fbcomp_"), override CTID variable.
	 * 
	 * IMPORTANT: types should be unique
	 * 
	 * @return List of components types (Strings)
	 */
	protected static List<String> gatherAvailableComponentsTypes(Document components_xml) {
		
		Element root = components_xml.getDocumentElement();
		
		if(!root.hasChildNodes())
			return null;
		
		NodeList children = root.getChildNodes();
		List<String> components_types = new ArrayList<String>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("div")) {
				
				String element_id = ((Element)child).getAttribute("id");
				
				if(element_id != null && 
						element_id.startsWith(CTID) &&
						!components_types.contains(element_id)
				)
					components_types.add(element_id);
			}
		}
		
		return components_types;
	}
		
	public static void main(String[] args) {
		
		try {
			
			FormBuilder.init(null);
			FormBuilder fb = FormBuilder.getInstance();
			fb.createFormDocument(null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
