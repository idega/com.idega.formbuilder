package com.idega.formbuilder.business.form;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.beans.XFormsComponentBean;
import com.idega.formbuilder.business.form.util.FormBuilderUtil;
import com.idega.formbuilder.business.generators.ComponentsGeneratorFactory;
import com.idega.formbuilder.business.generators.IComponentsGenerator;
import com.idega.formbuilder.sandbox.SandboxUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.slide.business.IWSlideService;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Main FormBuilder model class. Knows about available form components, manages user's xforms document
 */
public class FormBuilder implements IFormBuilder {
	
	private static Log logger = LogFactory.getLog(FormBuilder.class);
	
	private static Document components_xml = null;
	private static Document components_xforms = null;
	private static Document components_xsd = null;
	private static Document form_xforms_template = null;
	private static Document form_xsd_template = null;
	private static InputStream components_xforms_stream = null;
	private static InputStream components_xsd_stream = null;
	private static boolean inited = false;
	private static List<String> components_types = null;
	private static Map<String, XFormsComponentBean> cached_xforms_components = new HashMap<String, XFormsComponentBean>();
	private static Map<String, Element> cached_html_components = new HashMap<String, Element>();
	
	private static String COMPONENTS_XFORMS_CONTEXT_PATH = null;
	private static String COMPONENTS_XSD_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = null;
	private static String COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = null;
	private static String FORM_XFORMS_TEMPLATE_CONTEXT_PATH = null;
	private static String FORM_XSD_TEMPLATE_CONTEXT_PATH = null;
	
	private static final String FORMS_REPO_CONTEXT = "/files/formbuilder/forms/";
	
	private Document form_xforms = null;
	private Document form_xsd = null;
	private FormPropertiesBean form_props = null;
	
	private List<String> form_components_id_list = new LinkedList<String>();
	
	private List<String> form_xsd_contained_types_declarations = new LinkedList<String>();
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.IFormBuilder#createFormDocument(com.idega.formbuilder.business.form.beans.FormPropertiesBean)
	 */
	public void createFormDocument(FormPropertiesBean form_properties) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		if(form_properties == null)
			throw new NullPointerException("Form properties not provided");
		
		form_props = form_properties;
		
		form_xforms = (Document)form_xforms_template.cloneNode(true);
		String form_id_str;
		String[] pathes;
		
		if(form_props.getId() != null) {
			
			form_id_str = form_props.getId().toString();
			
			NodeList nl = form_xforms.getElementsByTagName(FormBuilderUtil.model_name);
			
			Element model = (Element)nl.item(0);
			model.setAttribute(FormBuilderUtil.id_name, form_id_str);
			pathes = getFormSchemaPath(form_id_str);
			model.setAttribute("schema", "xsd/"+pathes[1]);
			
		} else {
			throw new NullPointerException("Id not presented in form properties.");
		}
		
		if(form_props.getName() != null) {
			
			NodeList nl = form_xforms.getElementsByTagName("title");
			
			Element title = (Element)nl.item(0);
			title.setTextContent(form_props.getName());
		}
		
		form_xsd = (Document)form_xsd_template.cloneNode(true);
		
		saveDocumentToWebdav(form_xsd, pathes[0], pathes[1]);
		pathes = getFormPath(form_id_str);
		saveDocumentToWebdav(form_xforms, pathes[0], pathes[1]);
	}
	
	private String[] form_pathes = null;
	
	private String[] getFormPath(String form_id) {
	
		if(form_pathes == null) {
			
			String path_to_file = 
			new StringBuffer(FORMS_REPO_CONTEXT)
			.append(form_id)
			.append(FormBuilderUtil.slash)
			.toString();
			
			form_pathes = new String[] {path_to_file, form_id+".xforms"};			
		}
		
		return form_pathes;
	}
	
	private String[] schema_pathes = null;
	
	private String[] getFormSchemaPath(String form_id) {
		
		if(schema_pathes == null) {
			
			String path_to_file = 
			new StringBuffer(FORMS_REPO_CONTEXT)
			.append("xsd/")
			.append(form_id)
			.append(FormBuilderUtil.slash)
			.toString();
			
			schema_pathes = new String[] {path_to_file.toString(), form_id+".xsd"};
		}

		return schema_pathes;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.IFormBuilder#removeFormComponent(java.lang.String)
	 */
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException {
		
		checkForPendingErrors();
		
		if(form_xforms == null)
			throw new NullPointerException("Form document not created");
		
		throw new NullPointerException("Not implemented yet");
	}
	
	protected IWSlideService getIWSlideService() {
		try {
			return (IWSlideService) IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(), IWSlideService.class);
		}
		catch (IBOLookupException e) {
			throw new RuntimeException("Error getting IWSlideService");
		}
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
	 * @param path_to_file - where file should be placed, relative to webdav context
	 * @param file_name - how should we name the file
	 * @throws TransformerException - file is not an xml file maybe
	 * @throws NullPointerException - some parameters were not provided, or provided empty string(s)
	 */
	protected void saveDocumentToWebdav(final Document document, final String path_to_file, final String file_name) throws TransformerException, NullPointerException {
		
		if(true)
			return;
		
		if(document == null || path_to_file == null || path_to_file.equals("") || file_name == null || file_name.equals("")) {
			
			String msg = 
			new StringBuffer("\nEither parameter is provided as null or empty, shouldn't be:")
			.append("\ndocument: ")
			.append(String.valueOf(document))
			.append("\npath_to_file: ")
			.append(path_to_file)
			.append("\nfile_name: ")
			.append(file_name)
			.toString();
			
			throw new NullPointerException(msg);
		}
		
		new Thread() {
			
			public void run() {
				
				try {
					
//					TODO: find better method for doing this.
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					DOMUtil.prettyPrintDOM(document, out);
					InputStream is = new ByteArrayInputStream(out.toByteArray());
//					--
					
					getIWSlideService().uploadFileAndCreateFoldersFromStringAsRoot(
							path_to_file, file_name,
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
		
		Element xforms_element = FormBuilderUtil.getElementByIdFromDocument(components_xforms, "body", component_type);
		
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
				FormBuilderUtil.getElementByIdFromDocument(components_xforms, FormBuilderUtil.model_name, bind_to);
			
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
			
		html_component = FormBuilderUtil.getElementByIdFromDocument(components_xml, null, component_type);
		
		if(html_component == null) {
			String msg = "Component cannot be found in temporal components xml document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xml document.");
			throw new NullPointerException(msg);
		}
		
		cached_html_components.put(component_type, html_component);
		return html_component;
	}

	/*
	 * TODO: add some kind of transaction. if smth critical failes, all changes to form or schema should be rollbacked.
	 */
	public Element createFormComponent(String component_type, String component_after_new_id) throws FBPostponedException, NullPointerException, Exception {
		
		checkForPendingErrors();
		
		if(form_xforms == null)
			throw new NullPointerException("Form document was not created first");
		
		checkForComponentType(component_type);
		
		XFormsComponentBean xforms_component = getXFormsComponentReferencesByType(component_type);
		
		Element new_xforms_element = xforms_component.getElement();
		new_xforms_element = (Element)form_xforms.importNode(new_xforms_element, true);
		
		Integer new_comp_id = FormBuilderUtil.generateComponentId(form_props.getLast_component_id());
		form_props.setLast_component_id(new_comp_id);
		
		String new_comp_id_str = CTID+String.valueOf(new_comp_id);
		
		new_xforms_element.setAttribute(FormBuilderUtil.id_name, new_comp_id_str);
		
		String bind_id = null;
		
		if(xforms_component.getBind() != null) {
			
			bind_id = new_comp_id_str+xforms_component.getBind().getAttribute(FormBuilderUtil.id_name);
			new_xforms_element.setAttribute("bind", bind_id);
		}
		
		if(component_after_new_id == null) {
//			append element to component list
			Element components_container = (Element)form_xforms.getElementsByTagName("xf:group").item(0);
			
			components_container.appendChild(new_xforms_element);
			
		} else {
//			insert element after component
			Element component_after_new = FormBuilderUtil.getElementByIdFromDocument(form_xforms, "body", component_after_new_id);
			
			if(component_after_new != null) {
				component_after_new.getParentNode().insertBefore(new_xforms_element, component_after_new);
			} else
				throw new NullPointerException("Component, after which new component should be placed, was not found");
		}
		
		String new_form_schema_type = null;
		
		if(xforms_component.getBind() != null) {
//			insert bind element
			new_xforms_element = (Element)form_xforms.importNode(xforms_component.getBind(), true);
			
			FormBuilderUtil.insertBindElement(form_xforms, new_xforms_element, new_form_schema_type, bind_id, form_xsd_contained_types_declarations);
			
			if(xforms_component.getNodeset() != null) {
				
				new_xforms_element.setAttribute("nodeset", bind_id);
				
//				insert nodeset element
				
				new_xforms_element = form_xforms.createElement(bind_id);
				
				FormBuilderUtil.insertNodesetElement(
						form_xforms, xforms_component.getNodeset(), new_xforms_element
				);
			}
		}
		
//		DOMUtil.prettyPrintDOM(form_xforms);
		
		String form_id_str = form_props.getId().toString();
		String[] pathes = getFormPath(form_id_str);
		saveDocumentToWebdav(form_xforms, pathes[0], pathes[1]);
		
		if(component_after_new_id != null) {
			
			//find index and insert
			for (int i = 0; i < form_components_id_list.size(); i++) {
				
				if(form_components_id_list.get(i).equals(component_after_new_id)) {
					form_components_id_list.add(i, new_comp_id_str);
					break;
				}
			}
		} else
			form_components_id_list.add(new_comp_id_str);
		
		if(new_form_schema_type != null) {
			
			FormBuilderUtil.copySchemaType(components_xsd, form_xsd, new_form_schema_type);
			form_xsd_contained_types_declarations.add(new_form_schema_type);
			pathes = getFormSchemaPath(form_id_str);
			saveDocumentToWebdav(form_xsd,  pathes[0], pathes[1]);
		}
		
		Element new_html_component = (Element)getHtmlComponentReferenceByType(component_type).cloneNode(true);
		putAttributesOnHtmlComponent(new_html_component, new_comp_id_str, component_type);
		
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
		
		component.setAttribute(FormBuilderUtil.id_name, new_comp_id);
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
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.IFormBuilder#getAvailableFormComponentsList()
	 */
	public List<String> getAvailableFormComponentsList() {
		
		if(components_xforms == null) {
			
			logger.error("getFormComponentsList: components_xforms is null. Should not happen ever. Something bad.");
			return null;
		}
		
		return components_types;
	}
	
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.business.form.IFormBuilder#getFormComponentsList()
	 */
	public List<String> getFormComponentsList() {
		return form_components_id_list;
	}
	
	private static final String NOT_INITED_MSG = "Init FormBuilder first";
	
	/**
	 * 
	 * @return instance of this class. FormBuilder should be initiated first by calling init()
	 * @throws InstantiationException - if formbuilder was not initiated.
	 */
	public static IFormBuilder getInstance() throws InstantiationException {
		
		if(!inited)
			throw new InstantiationException(NOT_INITED_MSG);
			
		return new FormBuilder();
	}
	
	public static void init(FacesContext ctx) throws InstantiationException {
		
		COMPONENTS_XFORMS_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMS_CONTEXT_PATH;
		COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH;
		COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH = SandboxUtil.COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH;
		FORM_XFORMS_TEMPLATE_CONTEXT_PATH = SandboxUtil.FORM_XFORMS_TEMPLATE_CONTEXT_PATH;
		FORM_XSD_TEMPLATE_CONTEXT_PATH = SandboxUtil.FORM_XSD_TEMPLATE_CONTEXT_PATH;
		COMPONENTS_XSD_CONTEXT_PATH = SandboxUtil.COMPONENTS_XSD_CONTEXT_PATH;
		
		try {
			
//			IWContext iwc = IWContext.getInstance();
//			IBOSession ses_bean = IBOLookup.getSessionInstance(iwc, IWSlideSession.class);			
//			components_xforms_stream = ((IWSlideSessionBean)ses_bean).getInputStream(COMPONENTS_XFORMS_CONTEXT_PATH);
//			components_xsd_stream = ((IWSlideSessionBean)ses_bean).getInputStream(COMPONENTS_XSD_CONTEXT_PATH);
			
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
			
			IComponentsGenerator components_generator = ComponentsGeneratorFactory.createComponentsGenerator();
			components_generator.init(
					new String[] {null, COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH, 
							COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH
							}
					);
			
			DocumentBuilder doc_builder = FormBuilderUtil.getDocumentBuilder();
			
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
			
			components_types = gatherAvailableComponentsTypes(components_xml);
			
			form_xforms_template = doc_builder.parse(new FileInputStream(FORM_XFORMS_TEMPLATE_CONTEXT_PATH));
			form_xsd_template = doc_builder.parse(new FileInputStream(FORM_XSD_TEMPLATE_CONTEXT_PATH));
			
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
	 * Component is encapsulated into div tag, which contains tag id as component type.
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
				
				String element_id = ((Element)child).getAttribute(FormBuilderUtil.id_name);
				
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
	
	public static boolean isInited() {
		return inited;
	}
	
	public static void main(String[] args) {

		try {
			
			long start = System.currentTimeMillis();
			IFormBuilder fb = FormBuilderFactory.newFormBuilder();
			long end = System.currentTimeMillis();
			System.out.println("inited in: "+(end-start));
//			System.out.println("<sugeneruoti komponentai > ");
//			DOMUtil.prettyPrintDOM(components_xml);
//			System.out.println("<sugeneruoti komponentai />");
			
			FormPropertiesBean form_props = new FormPropertiesBean();
			form_props.setId(new Long(22));
			form_props.setName("my form name");

			start = System.currentTimeMillis();
			fb.createFormDocument(form_props);
			end = System.currentTimeMillis();
			System.out.println("document created in: "+(end-start));
			
			start = System.currentTimeMillis();
			fb.createFormComponent("fbcomp_text", null);
			end = System.currentTimeMillis();
			System.out.println("text component created in: "+(end-start));
//			
			start = System.currentTimeMillis();
			fb.createFormComponent("fbcomp_email", null);
			end = System.currentTimeMillis();
			System.out.println("email component created in: "+(end-start));
			
//			
//			Element textarea = fb.createFormComponent("fbcomp_textarea", "fbcomp_2");
//			DOMUtil.prettyPrintDOM(textarea);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
