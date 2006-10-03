package com.idega.formbuilder.business.form.manager;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.manager.beans.FormBean;
import com.idega.formbuilder.business.form.manager.beans.XFormsComponentBean;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class ComponentsManager {
	
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
		
		Integer new_comp_id = FormManagerUtil.generateComponentId(form_props.getLast_component_id());
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

			FormManagerUtil.copySchemaType(cache_manager.getComponentsXsd(), form_xforms, new_form_schema_type);
			form_bean.getFormXsdContainedTypesDeclarations().add(new_form_schema_type);
		}
		
		if(!persistance_manager.isInitiated()) {
			throw new PersistenceException("Persistance manager is not initiated");
		}
		
//		DOMUtil.prettyPrintDOM(form_xforms);
		
		persistance_manager.persistDocument(form_xforms);
		
		Element new_html_component = (Element)cache_manager.getHtmlComponentReferenceByType(component_type).cloneNode(true);
		putMetaInfoOnHtmlComponent(new_html_component, new_comp_id_str, component_type);
		cache_manager.putUnlocalizedFormHtmlComponent(String.valueOf(form_props.getId()), new_comp_id_str, new_html_component);
		
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
			
			String desc_text_content = desc.getTextContent();
			
			if(FormManagerUtil.isLocalizationKeyCorrect(desc_text_content)) {
				
				desc.setTextContent(FormManagerUtil.getComponentLocalizationKey(new_comp_id, desc_text_content));
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
	
	public Element getLocalizedFormHtmlComponent(String component_id, String loc_str) throws NullPointerException {
		
		CacheManager cache_manager = CacheManager.getInstance();
		String form_id_str = String.valueOf(form_bean.getFormProperties().getId());
		
		Element form_component = cache_manager.getLocalizedFormHtmlComponent(
				form_id_str, component_id, loc_str);
		
		if(form_component != null)
			return form_component;
		
		form_component = cache_manager.getUnlocalizedFormHtmlComponent(form_id_str, component_id);
		
		if(form_component == null)
			throw new NullPointerException("Unlocalized form html component not found in cache. Should not happen.");
		
		return getFormHtmlComponentLocalization(form_bean.getFormXforms(), form_component, loc_str);
	}
	
	private Element getFormHtmlComponentLocalization(Document xforms_doc, Element unlocalized_component, String loc_str) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(xforms_doc, "head", FormManagerUtil.loc_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_component.cloneNode(true);
		
		NodeList descendants = localized_component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Element desc = (Element)descendants.item(i);
			
			String txt_content = desc.getTextContent();
			
			if(FormManagerUtil.isLocalizationKeyCorrect(txt_content)) {
				
				NodeList localization_strings_elements = loc_strings.getElementsByTagName(txt_content);
				
				String localized_string = null;
				
				if(localization_strings_elements != null) {
					
					for (int j = 0; j < localization_strings_elements.getLength(); j++) {
						
						Element loc_el = (Element)localization_strings_elements.item(j);
						
						String lang = loc_el.getAttribute("lang");
						
						if(lang != null && lang.equals(loc_str)) {
							
							localized_string = loc_el.getTextContent();
							break;
						}
					}
				}
				
				if(localized_string == null)
					throw new NullPointerException(
							"Could not find localization value by provided key= "+txt_content+", language= "+loc_str);
				
				desc.setTextContent(localized_string);
			}
		}
		
		return localized_component;
	}
}