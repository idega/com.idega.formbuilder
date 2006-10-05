package com.idega.formbuilder.business.form.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponent implements IFormComponent {
	
	private static final String simple_type = "xs:simpleType";
	private static final String complex_type = "xs:complexType";
	
	private String component_after_me_id;
	private String component_id;
	private String type;
	
	private IFormComponentParent form_document;
	
	private Element unlocalized_html_component;
	private Map<Locale, Element> localized_html_components;
	
	public void render() {
		
		Document xforms_doc = form_document.getXformsDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form Xforms document was not provided");
		
		CacheManager cache_manager = CacheManager.getInstance();
		cache_manager.checkForComponentType(type);
		
		XFormsComponentInfoBean xforms_component = cache_manager.getXFormsComponentReferencesByType(type);
		
		Element new_xforms_element = xforms_component.getElement();
		new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
		
		new_xforms_element.setAttribute(FormManagerUtil.id_name, component_id);
		
		localizeComponent(new_xforms_element, component_id, xforms_doc, cache_manager.getComponentsXforms());
		
		String bind_id = null;
		
		if(xforms_component.getBind() != null) {
			
			bind_id = component_id+xforms_component.getBind().getAttribute(FormManagerUtil.id_name);
			new_xforms_element.setAttribute("bind", bind_id);
		}
		
		if(component_after_me_id == null) {
//			append element to component list
			Element components_container = (Element)xforms_doc.getElementsByTagName("xf:group").item(0);
			
			components_container.appendChild(new_xforms_element);
			
		} else {
//			insert element after component
			Element component_after_me = FormManagerUtil.getElementByIdFromDocument(xforms_doc, "body", component_after_me_id);
			
			if(component_after_me != null) {
				component_after_me.getParentNode().insertBefore(new_xforms_element, component_after_me);
			} else
				throw new NullPointerException("Component, after which new component should be placed, was not found");
		}
		
		String new_form_schema_type = null;
		
		if(xforms_component.getBind() != null) {
//			insert bind element
			new_xforms_element = (Element)xforms_doc.importNode(xforms_component.getBind(), true);
			
			new_form_schema_type = FormManagerUtil.insertBindElement(xforms_doc, new_xforms_element, bind_id, form_document.getFormXsdContainedTypesDeclarations());
			
			if(xforms_component.getNodeset() != null) {
				
				new_xforms_element.setAttribute("nodeset", bind_id);
				
//				insert nodeset element
				
				new_xforms_element = xforms_doc.createElement(bind_id);
				
				FormManagerUtil.insertNodesetElement(
						xforms_doc, xforms_component.getNodeset(), new_xforms_element
				);
			}
		}
		
		if(component_after_me_id != null) {
			
			List<String> form_components_id_list = form_document.getFormComponentsIdList();
			
			//find index and insert
			for (int i = 0; i < form_components_id_list.size(); i++) {
				
				if(form_components_id_list.get(i).equals(component_after_me_id)) {
					form_components_id_list.add(i, component_id);
					break;
				}
			}
		} else
			form_document.getFormComponentsIdList().add(component_id);
		
		if(new_form_schema_type != null) {

			copySchemaType(cache_manager.getComponentsXsd(), xforms_doc, new_form_schema_type);
			form_document.getFormXsdContainedTypesDeclarations().add(new_form_schema_type);
		}
	}
	
	public void setComponentAfterThis(IFormComponent component) {
		
		if(component == null)
			throw new NullPointerException("Component not provided");
		
		component_after_me_id = component.getId();
	}
	
	public String getId() {
		
		return component_id;
	}
	
	public void setId(String id) {
		
		if(id != null)
			component_id = id;
	}
	
	protected void localizeComponent(Element component_container, String comp_id, Document xforms_doc_to, Document xforms_doc_from) {

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
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setFormDocument(IFormComponentParent form_document) {
		this.form_document = form_document; 
	}
	
	public Element getHtmlRepresentationByLocale(Locale locale) {

		Map<Locale, Element> localized_html_components = getLocalizedHtmlComponents();
		Element localized_element = localized_html_components.get(locale);
		
		if(localized_element != null)
			return localized_element;
		
		if(unlocalized_html_component == null) {
			
			unlocalized_html_component = (Element)CacheManager.getInstance().getHtmlComponentReferenceByType(type).cloneNode(true);
			putMetaInfoOnHtmlComponent(unlocalized_html_component, component_id, type);
		}
		
		localized_element = getFormHtmlComponentLocalization(locale.getLanguage());
		localized_html_components.put(locale, localized_element);
		
		return localized_element;
	}
	
	protected Map<Locale, Element> getLocalizedHtmlComponents() {
		
		if(localized_html_components == null)
			localized_html_components = new HashMap<Locale, Element>();
		
		return localized_html_components;
	}
	
	/**
	 * Replaces old_comp_id values with new_comp_id values on all attributes, which contained old_comp_id values.
	 * Puts id attribute on component element with new_comp_id value. 
	 * 
	 * @param component - form component container
	 * @param new_comp_id - new form component id to be set on attributes
	 * @param old_comp_id - all form component id
	 */
	protected static void putMetaInfoOnHtmlComponent(Element component, String new_comp_id, String old_comp_id) {
		
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
	
	protected Element getFormHtmlComponentLocalization(String loc_str) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				form_document.getXformsDocument(), "head", FormManagerUtil.data_mod
		);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_html_component.cloneNode(true);
		
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
}