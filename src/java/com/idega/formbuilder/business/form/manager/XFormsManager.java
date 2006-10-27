package com.idega.formbuilder.business.form.manager;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormComponentParent;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.beans.XFormsComponentDataBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManager {
	
	private static Log logger = LogFactory.getLog(XFormsManager.class);
	
	protected CacheManager cache_manager;
	protected IFormComponentParent form_document;
	protected IFormComponent component;
	
	private static final String simple_type = "xs:simpleType";
	private static final String complex_type = "xs:complexType";
	private static final String required_att = "required";
	private static final String true_xpath = "true()";
	
	protected XFormsComponentDataBean xforms_component;
	
	public void setCacheManager(CacheManager cache_manager) {
		this.cache_manager = cache_manager;
	}
	
	public void setFormDocument(IFormComponentParent form_document) {
		
		this.form_document = form_document;
	}
	
	/**
	 * Gets full component bycomponent type. 
	 * 
	 * @param component_type - used to find correct xforms component implementation
	 * @return element node.
	 * @throws NullPointerException - component implementation could not be found by component type
	 */
	public XFormsComponentDataBean getXFormsComponentByType(String component_type) throws NullPointerException {
		
		cache_manager.checkForComponentType(component_type);
		
		XFormsComponentDataBean xforms_component = cache_manager.getCachedXformsComponent(component_type); 

		if(xforms_component != null)
			return (XFormsComponentDataBean)xforms_component.clone();
		
		Document components_xforms = cache_manager.getComponentsXforms();
		Element xforms_element = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.body_name, component_type);
		
		if(xforms_element == null) {
			String msg = "Component cannot be found in components xforms document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xforms document.");
			throw new NullPointerException(msg);
		}
		
		synchronized (XFormsManager.class) {
			
			xforms_component = cache_manager.getCachedXformsComponent(component_type); 

			if(xforms_component != null)
				return xforms_component;
			
			xforms_component = new XFormsComponentDataBean();
			xforms_component.setElement(xforms_element);
			
			String bind_to = xforms_element.getAttribute("bind");
			
			if(bind_to != null) {
				
//				get binding
				Element binding = 
					FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.model_name, bind_to);
				
				if(binding == null)
					throw new NullPointerException("Binding not found");

//				get nodeset
				String nodeset_to = binding.getAttribute("nodeset");
				Element nodeset = (Element)((Element)components_xforms.getElementsByTagName("xf:instance").item(0)).getElementsByTagName(nodeset_to).item(0);
				
				xforms_component.setBind(binding);
				xforms_component.setNodeset(nodeset);
				cache_manager.cacheXformsComponent(component_type, xforms_component);
			}
		}
		return (XFormsComponentDataBean)xforms_component.clone();
	}
	
	public void addComponentToDocument(String component_id, String component_after_this_id, XFormsComponentDataBean xforms_component)
	throws NullPointerException {
		
		if(form_document == null)
			throw new NullPointerException("Parent form document not provided");
		
		Document xforms_doc = form_document.getXformsDocument();
		
		Element new_xforms_element = xforms_component.getElement();
		
		new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
		xforms_component.setElement(new_xforms_element);
		
		new_xforms_element.setAttribute(FormManagerUtil.id_name, component_id);
		
		localizeComponent(component_id, new_xforms_element, xforms_doc, cache_manager.getComponentsXforms());
		FormManagerUtil.removeTextNodes(new_xforms_element);
		
		String bind_id = null;
		
		if(xforms_component.getBind() != null) {
			
			bind_id = component_id+FormManagerUtil.bind_name;
			new_xforms_element.setAttribute(FormManagerUtil.bind_name, bind_id);
		}
		
		if(component_after_this_id == null) {
//			append element to component list
			Element components_container = (Element)xforms_doc.getElementsByTagName("xf:group").item(0);
			
			components_container.appendChild(new_xforms_element);
			
		} else {
//			insert element after component
			Element component_after_me = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_name, component_after_this_id);
			
			if(component_after_me != null)
				component_after_me.getParentNode().insertBefore(new_xforms_element, component_after_me);
			else
				throw new NullPointerException("Component, after which new component should be placed, was not found");
		}
		
		String new_form_schema_type = null;
		
		if(xforms_component.getBind() != null) {
//			insert bind element
			new_xforms_element = (Element)xforms_doc.importNode(xforms_component.getBind(), true);
			xforms_component.setBind(new_xforms_element);
			
			new_form_schema_type = FormManagerUtil.insertBindElement(xforms_doc, new_xforms_element, bind_id, form_document.getFormXsdContainedTypesDeclarations());
			
			if(xforms_component.getNodeset() != null) {
				
				new_xforms_element.setAttribute("nodeset", bind_id);
				
//				insert nodeset element
				
				new_xforms_element = xforms_doc.createElement(bind_id);
				xforms_component.setNodeset(new_xforms_element);
				
				FormManagerUtil.insertNodesetElement(
						xforms_doc, xforms_component.getNodeset(), new_xforms_element
				);
			}
		}
		
		if(component_after_this_id != null) {
			
			List<String> form_components_id_list = form_document.getFormComponentsIdList();
			
			//find index and insert
			for (int i = 0; i < form_components_id_list.size(); i++) {
				
				if(form_components_id_list.get(i).equals(component_after_this_id)) {
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
	
	protected void localizeComponent(String comp_id, Element component_container, Document xforms_doc_to, Document xforms_doc_from) {
		
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
	
	public void updateConstraintRequired() throws NullPointerException {
		
		IComponentProperties props = component.getProperties();
		
		Element bind = xforms_component.getBind();
		
		if(bind == null) {
			logger.error("Bind element not set in xforms_component data bean. See where component is rendered for cause.");
			throw new NullPointerException("Bind element is not set");
		}
		
		if(props.isRequired())
			
			bind.setAttribute(required_att, true_xpath);
		
		else
			bind.removeAttribute(required_att);
	}
	
	public void updateLabel() {
		
		IComponentProperties props = component.getProperties();
		LocalizedStringBean loc_str = props.getLabel();
		
		NodeList labels = xforms_component.getElement().getElementsByTagName(FormManagerUtil.label_name);
		
		if(labels == null || labels.getLength() == 0)
			return;
		
		Element label = (Element)labels.item(0);
		
		FormManagerUtil.putLocalizedText(null, null, 
				label,
				form_document.getXformsDocument(),
				loc_str
		);
	}
	
	public void updateErrorMsg() {
		
		IComponentProperties props = component.getProperties();
		
		Element element = xforms_component.getElement();
		NodeList alerts = element.getElementsByTagName(FormManagerUtil.alert_name);
		
		if(alerts == null || alerts.getLength() == 0) {
			
			Element alert = FormManagerUtil.getItemElementById(cache_manager.getComponentsXforms(), "alert");
			
			Document xforms_doc = form_document.getXformsDocument();
			
			alert = (Element)xforms_doc.importNode(alert, true);
			element.appendChild(alert);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output).item(0);
			
			String new_err_id = new StringBuffer(FormManagerUtil.loc_key_identifier)
			.append(component.getId())
			.append("error")
			.toString();
			
			FormManagerUtil.putLocalizedText(
					new_err_id, FormManagerUtil.localized_entries, output, xforms_doc, props.getErrorMsg());
		} else {
			
			Element alert = (Element)alerts.item(0);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output).item(0);
			
			FormManagerUtil.putLocalizedText(
					null, null, output, form_document.getXformsDocument(), props.getErrorMsg());
		}
	}
	
	public void setFormComponent(IFormComponent component) {
		this.component = component;
	}
	
	public void setXFormsComponentDataBean(XFormsComponentDataBean xforms_component) {
		this.xforms_component = xforms_component;
	}
	
	public void moveComponent(String current_id, String before_component_id) {
		
		Document xforms_doc = form_document.getXformsDocument();
		
		Element element_to_move = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_name, current_id);
		Element element_to_insert_before = null;

		if(before_component_id != null) {
			
			element_to_insert_before = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_name, before_component_id);
		}
		
		xforms_component.setElement(
				(Element)((Element)element_to_move.getParentNode()).insertBefore(element_to_move, element_to_insert_before)
		);
	}
}