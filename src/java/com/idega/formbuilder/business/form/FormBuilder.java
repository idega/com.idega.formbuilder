package com.idega.formbuilder.business.form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.beans.XFormsComponentBean;
import com.idega.formbuilder.business.generators.FormComponentsGenerator;
import com.idega.formbuilder.business.generators.IComponentsGenerator;
import com.idega.formbuilder.sandbox.SandboxUtil;
import com.idega.formbuilder.util.FBUtil;
import com.idega.slide.business.IWSlideServiceBean;
/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Main FormBuilder model class. Knows about available form components, manages user's xforms document
 */
public class FormBuilder {
	
	private static Log logger = LogFactory.getLog(FormBuilder.class);
	
	private static Document components_xml = null;
	private static Document components_xforms = null;
	private static Document form_xforms_template = null;
	private static InputStream components_xforms_stream = null;
	private static boolean inited = false;
	private static List<String> components_types = null;
	private static Map<String, XFormsComponentBean> cached_xforms_components = new HashMap<String, XFormsComponentBean>();
	private static Map<String, Element> cached_html_components = new HashMap<String, Element>();
	
	private static String COMPONENTS_XFORMS_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = null;
	private static String FORM_XFORMS_TEMPLATE_CONTEXT_PATH = null;
	
	private static final String FORMS_REPO_CONTEXT = "/files/formbuilder/forms/";
	
	private IWSlideServiceBean service_bean = null;
	private Document form_xforms = null;
	private FormPropertiesBean form_props = null;
	
//	gal lengviau UI bus orientuotis pagal sitoki lista
//	private List<String> form_components_id_list = new LinkedList<String>();
	
	/**
	 * creates primary user form document and stores it to webdav
	 * 
	 * @param form_props - primary form description. Only id is mandatory.
	 * @throws FBPostponedException - see exception description at checkForPendingErrors() javadoc
	 * @throws NullPointerException - form_props is null or id not provided
	 * @throws Exception - some kind of other error occured
	 */
	public void createFormDocument(FormPropertiesBean form_properties) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		if(form_properties == null)
			throw new NullPointerException("Form properties not provided");
		
		form_props = form_properties;
		
		try {
			
			form_xforms = (Document)form_xforms_template.cloneNode(true);
			String form_id_str;
			
			if(form_props.getId() != null) {
				
				form_id_str = form_props.getId().toString();
				
				NodeList nl = form_xforms.getElementsByTagName("xf:model");
				
				Element model = (Element)nl.item(0);
				model.setAttribute("id", form_id_str);
				
			} else {
				throw new NullPointerException("Id not presented in form properties.");
			}
			
			if(form_props.getName() != null) {
				
				NodeList nl = form_xforms.getElementsByTagName("title");
				
				Element model = (Element)nl.item(0);
				model.setTextContent(form_props.getName());
			}
			
			saveDocumentToWebdav(form_xforms, getServiceBean(), form_id_str);
			
		} catch (NullPointerException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		if(form_xforms == null)
			throw new NullPointerException("Form document not created");
		
		
		
		
	}
	
	private IWSlideServiceBean getServiceBean() {
		
		if(service_bean == null)
			service_bean = new IWSlideServiceBean();
		
		return service_bean;
	}
	
	private Exception document_to_webdav_save_exception = null;
	
	/**
	 * saves xml(!) file to webdav directory
	 * 
	 * <p>
	 * <b>imporant:</b> method uses thread to upload file. So, if something bad happens during this process
	 * exception thrown is saved to document_to_webdav_save_exception variable. This variable should be time to time checked
	 * for null condition to know, if everything is alright.<br />
	 * Variable is set to null everytime, when no exception is thrown.<br />
	 * Logging is taking place for every exception thrown, so see logs for all problems.
	 * </p>
	 * 
	 * @param document - xml document to write to webdav repository
	 * @param service_bean - service bean, used to upload files
	 * @param path_to_file - where file should be placed, relative to webdav context
	 * @param file_name - how should we name the file
	 * @throws TransformerException - file is not an xml file maybe
	 * @throws NullPointerException - some parameters were not provided, or provided empty string(s)
	 */
	protected void saveDocumentToWebdav(final Document document, final IWSlideServiceBean service_bean, final String form_id) throws TransformerException, NullPointerException {
		
		if(true)
			return;
		
		if(document == null || service_bean == null || form_id == null || form_id.equals("")) {
			
			StringBuffer msg_buf = new StringBuffer("Either parameter is provided as null or empty, shouldn't be:");
			msg_buf.append("\ndocument: ");
			msg_buf.append(String.valueOf(document));
			msg_buf.append("\nservice_bean: ");
			msg_buf.append(String.valueOf(service_bean));
			msg_buf.append("\nform_id: ");
			msg_buf.append(form_id);
			
			throw new NullPointerException(msg_buf.toString());
		}
		
		final StringBuffer path_to_file_string_buffer = new StringBuffer(FORMS_REPO_CONTEXT);
		path_to_file_string_buffer.append(form_id);
		path_to_file_string_buffer.append("/");
		
		final StringBuffer file_name_string_buffer = new StringBuffer(form_id);
		file_name_string_buffer.append(".xforms");
		
		new Thread() {
			
			public void run() {
				
				try {
					
//					TODO: find better method for doing this.
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					DOMUtil.prettyPrintDOM(document, out);
					InputStream is = new ByteArrayInputStream(out.toByteArray());
//					--
					
					service_bean.uploadFileAndCreateFoldersFromStringAsRoot(
							path_to_file_string_buffer.toString(), file_name_string_buffer.toString(),
							is, "text/xml", false
					);
					
					document_to_webdav_save_exception = null;
					
				} catch (Exception e) {
					logger.error("Exception occured while saving document to webdav dir: ", e);
					document_to_webdav_save_exception = e;
				}
			}
		}.start();
	}
	
	private static void checkForComponentType(String component_type) throws NullPointerException {
		
		if(component_type == null || !components_types.contains(component_type)) {
			
			String msg;
			
			if(component_type == null)
				msg = "Component type is not provided (provided null)";
			else
				msg = "Component type given is not known. Given: "+component_type;
			
			throw new NullPointerException(msg);
		}
	}
	
	/**
	 * Gets full component reference by component type. What "reference" means,
	 * is that this is not the clone of node, but only reference to node.<br />
	 * So if you need to change node, you <b>must</b> clone it first.
	 * <p>
	 * <b><i>WARNING:</i></b> returned node should be cloned, if you want to change it in any way.
	 * </p>
	 * 
	 * @param component_type - used to find correct xforms component implementation
	 * @return reference to cached element node. See WARNING for info.
	 * @throws NullPointerException - component implementation could not be found by component type
	 */
	private XFormsComponentBean getXFormsComponentReferencesByType(String component_type) throws NullPointerException {
		
		XFormsComponentBean xforms_component = cached_xforms_components.get(component_type); 

		if(xforms_component != null)
			return xforms_component;
		
		Element xforms_element = FBUtil.getElementByIdFromDocument(components_xforms, "body", component_type);
		
		if(xforms_element == null) {
			String msg = "Component cannot be found in components xforms document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xforms document.");
			throw new NullPointerException(msg);
		}
		
		xforms_component = new XFormsComponentBean();
		xforms_component.setElement(xforms_element);
		
		String bind_to = xforms_element.getAttribute("bind");
		
		if(bind_to != null) {
			
//			get binding
			Element binding = 
				FBUtil.getElementByIdFromDocument(components_xforms, "xf:model", bind_to);
			
			if(binding == null)
				throw new NullPointerException("Binding not found");

//			get nodeset
			String nodeset_to = binding.getAttribute("nodeset");
			Element nodeset = (Element)((Element)components_xforms.getElementsByTagName("xf:instance").item(0)).getElementsByTagName(nodeset_to).item(0);
			
			xforms_component.setBind(binding);
			xforms_component.setNodeset(nodeset);
		}
		
		cached_xforms_components.put(component_type, xforms_component);
		return xforms_component;
	}
	
	private Element getHtmlComponentReferenceByType(String component_type) throws NullPointerException {
		
		Element html_component = cached_html_components.get(component_type); 

		if(html_component != null)
			return html_component;
			
		html_component = FBUtil.getElementByIdFromDocument(components_xml, null, component_type);
		
		if(html_component == null) {
			String msg = "Component cannot be found in temporal components xml document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xml document.");
			throw new NullPointerException(msg);
		}
		
		cached_html_components.put(component_type, html_component);
		return html_component;
	}

	/**
	 * <p>
	 * Creates new form component by component type provided,
	 * inserts it after specific component OR after all components in form component list.
	 * New xforms document is saved to webdav repository and component html representation returned.
	 * </p>
	 * <p>
	 * Of course form document should be created or imported before.
	 * </p>
	 * 
	 * @param component_type - type of component from components_types list,
	 * which should be inserted to form document.
	 * @param component_after_new_id - where new component should be places. This id must come from
	 * currently editing form document component. Provide <i>null</i> if component needs to be appended
	 * to other components list.
	 * @return newly created form component html representation
	 * @throws FBPostponedException - see exception description at checkForPendingErrors() javadoc
	 * @throws NullPointerException - form document was not created first, 
	 * component_after_new_id was provided, but such component was not found, other..
	 * @throws Exception - something else is wrong
	 */
	public Element createFormComponent(String component_type, String component_after_new_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		if(form_xforms == null)
			throw new NullPointerException("Form document not created");
		
		checkForComponentType(component_type);
		
		XFormsComponentBean xforms_component = getXFormsComponentReferencesByType(component_type);
		
		Element new_xforms_element = xforms_component.getElement();
		new_xforms_element = (Element)form_xforms.importNode(new_xforms_element, true);
		
		Integer new_comp_id = FBUtil.generateComponentId(form_props.getLast_component_id());
		form_props.setLast_component_id(new_comp_id);
		
		String new_comp_id_str = CTID+String.valueOf(new_comp_id);
		
		new_xforms_element.setAttribute("id", new_comp_id_str);
		
		String bind_id = null;
		
		if(xforms_component.getBind() != null) {
			
			bind_id = new_comp_id_str+xforms_component.getBind().getAttribute("id");
			new_xforms_element.setAttribute("bind", bind_id);
		}
		
		if(component_after_new_id == null) {
//			append element to component list
			Element components_container = (Element)form_xforms.getElementsByTagName("xf:group").item(0);
			components_container.appendChild(new_xforms_element);
			
		} else {
//			insert element after component
			Element component_after_new = FBUtil.getElementByIdFromDocument(form_xforms, null, component_after_new_id);
			
			if(component_after_new != null) {
				
				component_after_new.insertBefore(new_xforms_element, component_after_new);
			} else
				throw new NullPointerException("Component, after which new component should be placed, was not found");
		}
		
		if(xforms_component.getBind() != null) {
//			insert bind element
			new_xforms_element = (Element)form_xforms.importNode(xforms_component.getBind(), true);
			new_xforms_element.setAttribute("id", bind_id);
			
			if(xforms_component.getNodeset() != null) {
				
				new_xforms_element.setAttribute("nodeset", bind_id);
			}
			
			Element container = (Element)form_xforms.getElementsByTagName("xf:model").item(0);
			container.appendChild(new_xforms_element);
			
			if(xforms_component.getNodeset() != null) {
//				insert nodeset element
				
				new_xforms_element = form_xforms.createElement(bind_id);
				
				if(xforms_component.getNodeset().hasChildNodes()) {
					
					NodeList children = xforms_component.getNodeset().getChildNodes();
					
					for (int i = 0; i < children.getLength(); i++) {
						
						Node child = children.item(i);
						
						if(child.getNodeType() == Node.ELEMENT_NODE) {

							child = form_xforms.importNode(child, true);
							new_xforms_element.appendChild(child);
						}
					}
				}
				
				container = 
					(Element)((Element)form_xforms
							.getElementsByTagName("xf:instance").item(0))
							.getElementsByTagName("data").item(0);
				container.appendChild(new_xforms_element);
			}
		}
		
		saveDocumentToWebdav(form_xforms, getServiceBean(), form_props.getId().toString());

		Element new_html_component = (Element)getHtmlComponentReferenceByType(component_type).cloneNode(true);
		putAttributesOnHtmlComponent(new_html_component, new_comp_id_str, component_type);
		
		
		DOMUtil.prettyPrintDOM(form_xforms);
		
		return new_html_component;
	}
	
	/**
	 * Replaces old_comp_id values with new_comp_id values on all attributes, which contained old_comp_id values.
	 * Puts id attribute on component element with new_comp_id value. 
	 * 
	 * @param component - form component container
	 * @param new_comp_id - new form component id to be set on attributes
	 * @param old_comp_id - all form component id
	 */
	private static void putAttributesOnHtmlComponent(Element component, String new_comp_id, String old_comp_id) {
		
		component.setAttribute("id", new_comp_id);
		NodeList descendants = component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Node desc = descendants.item(i);
			NamedNodeMap attributes = desc.getAttributes();
			
			for (int j = 0; j < attributes.getLength(); j++) {
				Node attribute = attributes.item(j);
				
				String node_val = attribute.getNodeValue();
				
				if(node_val.contains(old_comp_id))
					
					attribute.setNodeValue(node_val.replace(old_comp_id, new_comp_id));
			}
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
		
//		COMPONENTS_XFORMS_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/chiba-web/web/myBasicTest.xhtml";
////		COMPONENTS_XFORMS_CONTEXT_PATH = "/files/formbuilder/components/components.xforms";
//		COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/resources/xslt/htmlxml.xsl";
//		COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/resources/xslt/components.xsl";
//		FORM_XFORMS_TEMPLATE_CONTEXT_PATH = "/Users/civilis/workspace/ePlatform35/com.idega.formbuilder/resources/templates/form.xhtml";
		
		COMPONENTS_XFORMS_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMS_CONTEXT_PATH;
		COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH;
		COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH;
		FORM_XFORMS_TEMPLATE_CONTEXT_PATH = SandboxUtil.FORM_XFORMS_TEMPLATE_CONTEXT_PATH;
		
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
	
	/**
	 * Check for exceptions thrown during previous requests
	 * @throws FBPostponedException - if some kind of exception happened during previous request. Formbuilder user knows,
	 * that error happened and can (most likely) happen again, so some adequate actions can be taken. 
	 */
	private void checkForPendingErrors() throws FBPostponedException {
		
		if(document_to_webdav_save_exception != null)
			throw new FBPostponedException(document_to_webdav_save_exception);
	}
		
	public static void main(String[] args) {
		
		try {
			
			FormBuilder.init(null);
			FormBuilder fb = FormBuilder.getInstance();
			

			FormPropertiesBean form_props = new FormPropertiesBean();
			form_props.setId(new Long(22));
			form_props.setName("my form name");

			fb.createFormDocument(form_props);
			
			fb.createFormComponent("fbcomp_text", null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
