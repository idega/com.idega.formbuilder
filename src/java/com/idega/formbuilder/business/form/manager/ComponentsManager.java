package com.idega.formbuilder.business.form.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.PersistenceException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.FormComponentBean;
import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.manager.beans.FormBean;
import com.idega.formbuilder.business.form.manager.beans.XFormsComponentBean;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentsManager /*TODO: implement interface*/{
	
	private FormBean form_bean;
	private IPersistenceManager persistance_manager;
	
	protected ComponentsManager() { }
	
	public static ComponentsManager getInstance(FormBean form_bean, IPersistenceManager persistance_manager) throws NullPointerException {
		
		if(form_bean == null)
			throw new NullPointerException("FormBean not provided");
		
		if(persistance_manager == null)
			throw new NullPointerException("IPersistanceManager not provided");
			
		ComponentsManager comp_man = new ComponentsManager();
		comp_man.form_bean = form_bean;
		comp_man.persistance_manager = persistance_manager;
		
		return comp_man;
	}
	
	public void removeFormComponent(String component_id) throws FBPostponedException, NullPointerException {
		
		Document form_xforms = form_bean.getFormXforms();
		
		if(form_xforms == null)
			throw new NullPointerException(form_not_created);
		
		throw new NullPointerException("Not implemented yet");
	}
	
	private static final String form_not_created = "Form document not created";
	
	/*
	 * TODO: add some kind of transaction. if smth critical failes, all changes to form or schema should be rollbacked.
	 */
	public String createFormComponent(String component_type, String component_after_new_id) throws FBPostponedException, NullPointerException, Exception {
		
		Document form_xforms = form_bean.getFormXforms();
		
		if(form_xforms == null)
			throw new NullPointerException(form_not_created);
		
		CacheManager cache_manager = CacheManager.getInstance();
		cache_manager.checkForComponentType(component_type);
		
		XFormsComponentBean xforms_component = cache_manager.getXFormsComponentReferencesByType(component_type);
		
		Element new_xforms_element = xforms_component.getElement();
		new_xforms_element = (Element)form_xforms.importNode(new_xforms_element, true);
		
		FormPropertiesBean form_props = form_bean.getFormProperties();
		
		Integer new_comp_id = generateComponentId(form_props.getLast_component_id());
		form_props.setLast_component_id(new_comp_id);
		
		String new_comp_id_str = FormManagerUtil.CTID+String.valueOf(new_comp_id);
		
		new_xforms_element.setAttribute(FormManagerUtil.id_name, new_comp_id_str);
		
		localizeNewComponent(new_xforms_element, new_comp_id_str, form_xforms, cache_manager.getComponentsXforms());
		
		String bind_id = null;
		
		if(xforms_component.getBind() != null) {
			
			bind_id = new_comp_id_str+xforms_component.getBind().getAttribute(FormManagerUtil.id_name);
			new_xforms_element.setAttribute("bind", bind_id);
		}
		
		if(component_after_new_id == null) {
//			append element to component list
			Element components_container = (Element)form_xforms.getElementsByTagName("xf:group").item(0);
			
			components_container.appendChild(new_xforms_element);
			
		} else {
//			insert element after component
			Element component_after_new = FormManagerUtil.getElementByIdFromDocument(form_xforms, "body", component_after_new_id);
			
			if(component_after_new != null) {
				component_after_new.getParentNode().insertBefore(new_xforms_element, component_after_new);
			} else
				throw new NullPointerException("Component, after which new component should be placed, was not found");
		}
		
		String new_form_schema_type = null;
		
		if(xforms_component.getBind() != null) {
//			insert bind element
			new_xforms_element = (Element)form_xforms.importNode(xforms_component.getBind(), true);
			
			new_form_schema_type = FormManagerUtil.insertBindElement(form_xforms, new_xforms_element, bind_id, form_bean.getFormXsdContainedTypesDeclarations());
			
			if(xforms_component.getNodeset() != null) {
				
				new_xforms_element.setAttribute("nodeset", bind_id);
				
//				insert nodeset element
				
				new_xforms_element = form_xforms.createElement(bind_id);
				
				FormManagerUtil.insertNodesetElement(
						form_xforms, xforms_component.getNodeset(), new_xforms_element
				);
			}
		}
		
		if(component_after_new_id != null) {
			
			List<String> form_components_id_list = form_bean.getFormComponentsIdList();
			
			//find index and insert
			for (int i = 0; i < form_components_id_list.size(); i++) {
				
				if(form_components_id_list.get(i).equals(component_after_new_id)) {
					form_components_id_list.add(i, new_comp_id_str);
					break;
				}
			}
		} else
			form_bean.getFormComponentsIdList().add(new_comp_id_str);
		
		if(new_form_schema_type != null) {

			copySchemaType(cache_manager.getComponentsXsd(), form_xforms, new_form_schema_type);
			form_bean.getFormXsdContainedTypesDeclarations().add(new_form_schema_type);
		}
		
		if(!persistance_manager.isInitiated()) {
			throw new PersistenceException("Persistance manager is not initiated");
		}
		
//		DOMUtil.prettyPrintDOM(form_xforms);
		
		persistance_manager.persistDocument(form_xforms);
		
		Element new_html_component = (Element)cache_manager.getHtmlComponentReferenceByType(component_type).cloneNode(true);
		putMetaInfoOnHtmlComponent(new_html_component, new_comp_id_str, component_type);
		
		IFormComponent html_form_component = new FormComponentBean();
		html_form_component.setComponentId(new_comp_id_str);
		html_form_component.setComponent(null, new_html_component);
		form_bean.putFormComponent(html_form_component);
		
		return new_comp_id_str;
	}
	
	private static void localizeNewComponent(Element component_container, String comp_id, Document xforms_doc_to, Document xforms_doc_from) {

		NodeList children = component_container.getElementsByTagName("*");
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Element child = (Element)children.item(i);
			
			String ref = child.getAttribute("ref");
			
			if(FormManagerUtil.isRefFormCorrect(ref)) {
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				FormManagerUtil.putLocalizedText(
					FormManagerUtil.getComponentLocalizationKey(comp_id, key), null, child, xforms_doc_to,
					FormManagerUtil.getLocalizedStrings(key, xforms_doc_from)
				);
			}
		}
	}
	
	/**
	 * Replaces old_comp_id values with new_comp_id values on all attributes, which contained old_comp_id values.
	 * Puts id attribute on component element with new_comp_id value. 
	 * 
	 * @param component - form component container
	 * @param new_comp_id - new form component id to be set on attributes
	 * @param old_comp_id - all form component id
	 */
	private static void putMetaInfoOnHtmlComponent(Element component, String new_comp_id, String old_comp_id) {
		
		component.setAttribute(FormManagerUtil.id_name, new_comp_id);
		NodeList descendants = component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Element desc = (Element)descendants.item(i);
			
			String desc_text_content = FormManagerUtil.getElementsTextNodeValue(desc);
			
			if(FormManagerUtil.isLocalizationKeyCorrect(desc_text_content)) {
				
				FormManagerUtil.setElementsTextNodeValue(desc, FormManagerUtil.getComponentLocalizationKey(new_comp_id, desc_text_content));
			}
			
			NamedNodeMap attributes = desc.getAttributes();
			
			for (int j = 0; j < attributes.getLength(); j++) {
				Node attribute = attributes.item(j);
				
				String node_val = attribute.getNodeValue();
				
				if(node_val.contains(old_comp_id))
					
					attribute.setNodeValue(node_val.replace(old_comp_id, new_comp_id));
			}
		}
	}
	
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
	 * IMPORTANT: types should be unique
	 * 
	 * @return List of components types (Strings)
	 */
	public static List<String> gatherAvailableComponentsTypes(Document components_xml) {
		
		Element root = components_xml.getDocumentElement();
		
		if(!root.hasChildNodes())
			return null;
		
		NodeList children = root.getChildNodes();
		List<String> components_types = new ArrayList<String>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("div")) {
				
				String element_id = ((Element)child).getAttribute(FormManagerUtil.id_name);
				
				if(element_id != null && 
						element_id.startsWith(FormManagerUtil.CTID) &&
						!components_types.contains(element_id)
				)
					components_types.add(element_id);
			}
		}
		
		return components_types;
	}
	
	public Element getLocalizedFormHtmlComponent(String component_id, Locale locale) throws NullPointerException {

		IFormComponent form_component = form_bean.getFormComponent(component_id);
		
		if(form_component == null)
			throw new NullPointerException("Form html component not found in cache. Should not happen.");
		
		Element localized_element = form_component.getComponent(locale);
		if(localized_element != null)
			return localized_element;
		
//		trust implementation so no component without unlocalized element should contain
		Element unlocalized_element = form_component.getComponent(null);
		
		localized_element = getFormHtmlComponentLocalization(form_bean.getFormXforms(), unlocalized_element, locale.getLanguage());
		form_component.setComponent(locale, localized_element);
		
		return localized_element;
	}
	
	private Element getFormHtmlComponentLocalization(Document xforms_doc, Element unlocalized_component, String loc_str) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(xforms_doc, "head", FormManagerUtil.loc_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_component.cloneNode(true);
		
		NodeList descendants = localized_component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Node desc = descendants.item(i);
			
			String txt_content = FormManagerUtil.getElementsTextNodeValue(desc);
			
			if(FormManagerUtil.isLocalizationKeyCorrect(txt_content)) {
				
				NodeList localization_strings_elements = loc_strings.getElementsByTagName(txt_content);
				
				String localized_string = null;
				
				if(localization_strings_elements != null) {
					
					for (int j = 0; j < localization_strings_elements.getLength(); j++) {
						
						Element loc_el = (Element)localization_strings_elements.item(j);
						
						String lang = loc_el.getAttribute("lang");
						
						if(lang != null && lang.equals(loc_str)) {
							
							localized_string = FormManagerUtil.getElementsTextNodeValue(loc_el);
							break;
						}
					}
				}
				
				if(localized_string == null)
					throw new NullPointerException(
							"Could not find localization value by provided key= "+txt_content+", language= "+loc_str);
				
				FormManagerUtil.setElementsTextNodeValue(desc, localized_string);
			}
		}
		
		return localized_component;
	}
	
	private static final String simple_type = "xs:simpleType";
	private static final String complex_type = "xs:complexType";
	
	/**
	 * <p>
	 * Copies schema type from one schema document to another by provided type name.
	 * </p>
	 * <p>
	 * <b><i>WARNING: </i></b>currently doesn't support cascading types copying,
	 * i.e., when one type depends on another
	 * </p>
	 * 
	 * @param src - schema document to copy from
	 * @param dest - schema document to copy to
	 * @param type_name - name of type to copy
	 * @throws NullPointerException - some params were null or such type was not found in src document
	 */
	protected void copySchemaType(Document src, Document dest, String type_name) throws NullPointerException {
		
		if(src == null || dest == null || type_name == null) {
			
			String err_msg = 
			new StringBuffer("\nEither parameter is not provided:")
			.append("\nsrc: ")
			.append(String.valueOf(src))
			.append("\ndest: ")
			.append(String.valueOf(dest))
			.append("\ntype_name: ")
			.append(type_name)
			.toString();
			
			throw new NullPointerException(err_msg);
		}
		
		Element root = src.getDocumentElement();
		
//		check among simple types
		
		Element type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(simple_type), type_name);
		
		if(type_to_copy == null) {
//			check among complex types
			
			type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(complex_type), type_name);
		}
		
		if(type_to_copy == null)
			throw new NullPointerException("Schema type was not found by provided name: "+type_name);
		
		type_to_copy = (Element)dest.importNode(type_to_copy, true);
		((Element)dest.getElementsByTagName("xs:schema").item(0)).appendChild(type_to_copy);
	}
	
	private Element getSchemaTypeToCopy(NodeList types, String type_name_required) {
		
		for (int i = 0; i < types.getLength(); i++) {
			
			Element simple_type = (Element)types.item(i); 
			String name_att = simple_type.getAttribute("name");
			
			if(name_att != null && name_att.equals(type_name_required))
				return simple_type;
		}
		
		return null;
	}
	
	protected Integer generateComponentId(Integer last_component_id) {
		
		return new Integer(last_component_id.intValue()+1);
	}
}