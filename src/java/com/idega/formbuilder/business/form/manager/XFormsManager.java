package com.idega.formbuilder.business.form.manager;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.PropertiesComponent;
import com.idega.formbuilder.business.form.beans.IFormComponent;
import com.idega.formbuilder.business.form.beans.IFormComponentContainer;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.beans.XFormsComponentDataBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManager implements IXFormsManager {
	
	private static Log logger = LogFactory.getLog(XFormsManager.class);
	
	protected CacheManager cache_manager;
	protected IFormComponentContainer component_parent;
	protected IFormComponent component;
	protected XFormsComponentDataBean xforms_component;
	
	private static final String simple_type = "xs:simpleType";
	private static final String complex_type = "xs:complexType";
	private static final String required_att = "required";
	private static final String true_xpath = "true()";
	
	public void setCacheManager(CacheManager cache_manager) {
		this.cache_manager = cache_manager;
	}
	
	public void setComponentParent(IFormComponentContainer component_parent) {
		
		this.component_parent = component_parent;
	}
	
	public void loadXFormsComponentByType(String component_type) throws NullPointerException {
		
		cache_manager.checkForComponentType(component_type);
		
		XFormsComponentDataBean xforms_component = cache_manager.getCachedXformsComponent(component_type);
		
		if(xforms_component != null) {
			this.xforms_component = (XFormsComponentDataBean)xforms_component.clone();
			return;
		}
		
		Document components_xforms = cache_manager.getComponentsXforms();
		Element xforms_element = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.body_tag, component_type);
		
		if(xforms_element == null) {
			String msg = "Component cannot be found in components xforms document by provided type: "+component_type;
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xforms document.");
			throw new NullPointerException(msg);
		}
		
		synchronized (XFormsManager.class) {
			
			xforms_component = cache_manager.getCachedXformsComponent(component_type); 

			if(xforms_component != null) {
				this.xforms_component = (XFormsComponentDataBean)xforms_component.clone();
				return;
			}
			
			loadXFormsComponent(components_xforms, xforms_element);
			cache_manager.cacheXformsComponent(component_type, (XFormsComponentDataBean)this.xforms_component.clone());
		}
	}
	
	public void loadXFormsComponentFromDocument(String component_id) {
		
		Document xforms_doc = component_parent.getXformsDocument();
		Element my_element = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, component_id);
		
		loadXFormsComponent(xforms_doc, my_element);
	}
	
	protected XFormsComponentDataBean newXFormsComponentDataBeanInstance() {
		return new XFormsComponentDataBean();
	}
	
	protected void loadXFormsComponent(Document components_xforms, Element xforms_element) {
		
		xforms_component = newXFormsComponentDataBeanInstance();
		xforms_component.setElement(xforms_element);
		getBindingsAndNodesets(components_xforms);
	}
	
	protected void setBindingsAndNodesets() {
		
		if(xforms_component.getBind() != null) {
			
//			insert bind element
			String component_id = component.getId();
			Document xforms_doc = component_parent.getXformsDocument();
			
			String bind_id = FormManagerUtil.bind_att+'.'+component_id;
			xforms_component.getElement().setAttribute(FormManagerUtil.bind_att, bind_id);
			
			Element element = (Element)xforms_doc.importNode(xforms_component.getBind(), true);
			xforms_component.setBind(element);
			
			String new_form_schema_type = insertBindElement(element, bind_id);
			
			if(new_form_schema_type != null) {

				copySchemaType(cache_manager.getComponentsXsd(), xforms_doc, new_form_schema_type, component_id+new_form_schema_type);
			}
			
			insertNodesetElement(bind_id);
		}
	}
	
	protected void getBindingsAndNodesets(Document components_xforms) {
		
		String bind_to = xforms_component.getElement().getAttribute(FormManagerUtil.bind_att);
		
		if(!FormManagerUtil.isEmpty(bind_to)) {
			
//			get binding
			Element binding = 
				FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, bind_to);
			
			if(binding == null)
				throw new NullPointerException("Binding not found by provided bind value: "+bind_to);
			
			xforms_component.setBind(binding);
			setNodeset(components_xforms);
		}
	}
	
	protected void setNodeset(Document components_xforms) {
		
//		get nodeset
		String nodeset_to = xforms_component.getBind().getAttribute(FormManagerUtil.nodeset_att);
		
		if(nodeset_to == null)
			return;
		
		String instance_id = null;
		
		if(nodeset_to.contains(FormManagerUtil.inst_start)) {
			instance_id = nodeset_to.substring(
					nodeset_to.indexOf(FormManagerUtil.inst_start)
					+FormManagerUtil.inst_start.length(),
					nodeset_to.indexOf(FormManagerUtil.inst_end)
			);
		}
		
		if(nodeset_to.contains(FormManagerUtil.slash)) {
			nodeset_to = nodeset_to.substring(nodeset_to.indexOf(FormManagerUtil.slash)+1);
		}
		Element nodeset = null;
		
		if(instance_id != null) {
			nodeset = (Element)(FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, instance_id)).getElementsByTagName(nodeset_to).item(0);
		} else {
			nodeset = (Element)((Element)components_xforms.getElementsByTagName(FormManagerUtil.instance_tag).item(0)).getElementsByTagName(nodeset_to).item(0);
		}
		
		xforms_component.setNodeset(nodeset);
	}
	
	public void addComponentToDocument() {
		
		Document xforms_doc = component_parent.getXformsDocument();
		Element component_element = xforms_component.getElement();
		
		component_element = (Element)xforms_doc.importNode(component_element, true);
		xforms_component.setElement(component_element);
		
		String component_id = component.getId();
		component_element.setAttribute(FormManagerUtil.id_att, component_id);
		
		localizeComponent(component_id, component_element, xforms_doc, cache_manager.getComponentsXforms());
		FormManagerUtil.removeTextNodes(component_element);
		
		setBindingsAndNodesets();
		
		if(component.getComponentAfterThis() == null) {
			Element components_container = component_parent.getComponentXFormsManager().getComponentElement();
			components_container.appendChild(component_element);
			component_parent.getContainedComponentsIdList().add(component_id);
			
		} else {
			Element component_after_me = component.getComponentAfterThis().getComponentXFormsManager().getComponentElement();
			component_after_me.getParentNode().insertBefore(component_element, component_after_me);
			
			List<String> parent_components_id_list = component_parent.getContainedComponentsIdList();
			
			String component_after_this_id = component.getComponentAfterThis().getId();
			
			for (int i = 0; i < parent_components_id_list.size(); i++) {
				
				if(parent_components_id_list.get(i).equals(component_after_this_id)) {
					parent_components_id_list.add(i, component_id);
					break;
				}
			}
		}
	}
	
	protected void localizeComponent(String comp_id, Element component_container, Document xforms_doc_to, Document xforms_doc_from) {
		
		NodeList children = component_container.getElementsByTagName("*");
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Element child = (Element)children.item(i);
			
			String ref = child.getAttribute(FormManagerUtil.ref_s_att);
			
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
	 * @param src_type_name - name of type to copy
	 * @throws NullPointerException - some params were null or such type was not found in src document
	 */
	protected void copySchemaType(Document src, Document dest, String src_type_name, String dest_type_name) throws NullPointerException {
		
		if(src == null || dest == null || src_type_name == null) {
			
			String err_msg = 
			new StringBuffer("\nEither parameter is not provided:")
			.append("\nsrc: ")
			.append(String.valueOf(src))
			.append("\ndest: ")
			.append(String.valueOf(dest))
			.append("\ntype_name: ")
			.append(src_type_name)
			.toString();
			
			throw new NullPointerException(err_msg);
		}
		
		Element root = src.getDocumentElement();
		
//		check among simple types
		
		Element type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(simple_type), src_type_name);
		
		if(type_to_copy == null) {
//			check among complex types
			
			type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(complex_type), src_type_name);
		}
		
		if(type_to_copy == null)
			throw new NullPointerException("Schema type was not found by provided name: "+src_type_name);
		
		type_to_copy = (Element)dest.importNode(type_to_copy, true);
		type_to_copy.setAttribute(FormManagerUtil.name_att, dest_type_name);
		
		((Element)dest.getElementsByTagName(FormManagerUtil.schema_tag).item(0)).appendChild(type_to_copy);
	}
	
	private Element getSchemaTypeToCopy(NodeList types, String type_name_required) {
		
		for (int i = 0; i < types.getLength(); i++) {
			
			Element simple_type = (Element)types.item(i); 
			String name_att = simple_type.getAttribute(FormManagerUtil.name_att);
			
			if(name_att != null && name_att.equals(type_name_required))
				return simple_type;
		}
		
		return null;
	}
	
	public void updateConstraintRequired() throws NullPointerException {
		
		PropertiesComponent props = component.getProperties();
		
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
		
		PropertiesComponent props = component.getProperties();
		LocalizedStringBean loc_str = props.getLabel();
		
		NodeList labels = xforms_component.getElement().getElementsByTagName(FormManagerUtil.label_tag);
		
		if(labels == null || labels.getLength() == 0)
			return;
		
		Element label = (Element)labels.item(0);
		
		FormManagerUtil.putLocalizedText(null, null, 
				label,
				component_parent.getXformsDocument(),
				loc_str
		);
	}
	
	public void updateErrorMsg() {
		
		PropertiesComponent props = component.getProperties();
		
		Element element = xforms_component.getElement();
		NodeList alerts = element.getElementsByTagName(FormManagerUtil.alert_tag);
		
		if(alerts == null || alerts.getLength() == 0) {
			
			Element alert = FormManagerUtil.getItemElementById(cache_manager.getComponentsXforms(), "alert");
			
			Document xforms_doc = component_parent.getXformsDocument();
			
			alert = (Element)xforms_doc.importNode(alert, true);
			element.appendChild(alert);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output_tag).item(0);
			
			String new_err_id = new StringBuffer(FormManagerUtil.loc_key_identifier)
			.append(component.getId())
			.append("error")
			.toString();
			
			FormManagerUtil.putLocalizedText(
					new_err_id, FormManagerUtil.localized_entries, output, xforms_doc, props.getErrorMsg());
		} else {
			
			Element alert = (Element)alerts.item(0);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output_tag).item(0);
			
			FormManagerUtil.putLocalizedText(
					null, null, output, component_parent.getXformsDocument(), props.getErrorMsg());
		}
	}
	
	public void setFormComponent(IFormComponent component) {
		this.component = component;
	}
	
	public void moveComponent(String before_component_id) {
		
		if(component_parent == null)
			throw new NullPointerException("Parent form document not provided");
		
		Document xforms_doc = component_parent.getXformsDocument();
		
		Element element_to_move = xforms_component.getElement();
		Element element_to_insert_before = null;

		if(before_component_id != null) {
			
			element_to_insert_before = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, before_component_id);
		} else {

			Element components_container = (Element)element_to_move.getParentNode();
			element_to_insert_before = DOMUtil.getLastChildElement(components_container);
		}
		
		xforms_component.setElement(
				(Element)((Element)element_to_move.getParentNode()).insertBefore(element_to_move, element_to_insert_before)
		);
	}
	
	public void removeComponentFromXFormsDocument() {
		
		removeComponentLocalization();
		removeComponentBindings();
		
		Element element_to_remove = xforms_component.getElement();
		element_to_remove.getParentNode().removeChild(element_to_remove);
	}
	
	protected void removeComponentBindings() {
		
		Document xforms_doc = component_parent.getXformsDocument();
		
		Element bind_element = xforms_component.getBind();
		
		if(bind_element != null) {
			
			String schema_type_att_value = bind_element.getAttribute(FormManagerUtil.type_att);
			
			if(schema_type_att_value != null && schema_type_att_value.startsWith(component.getId())) {
				
				Element schema_element = (Element)xforms_doc.getElementsByTagName(FormManagerUtil.schema_tag).item(0);
				
				Element type_element_to_remove = DOMUtil.getElementByAttributeValue(schema_element, "*", FormManagerUtil.name_att, schema_type_att_value);
				
				if(type_element_to_remove != null)
					schema_element.removeChild(type_element_to_remove);
			}
			bind_element.getParentNode().removeChild(bind_element);
		}
		Element nodeset = xforms_component.getNodeset();
		
		if(nodeset != null)
			nodeset.getParentNode().removeChild(nodeset);
	}
	
	protected void removeComponentLocalization() {
		
		NodeList children = xforms_component.getElement().getElementsByTagName("*");
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				component_parent.getXformsDocument(), FormManagerUtil.head_tag, FormManagerUtil.data_mod);
		
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Element child = (Element)children.item(i);
			
			String ref = child.getAttribute(FormManagerUtil.ref_s_att);
			
			if(FormManagerUtil.isRefFormCorrect(ref)) {
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				
//				those elements should be the child nodes
				NodeList localization_elements = loc_strings.getElementsByTagName(key);
				
				if(localization_elements != null) {
					
					int elements_count = localization_elements.getLength();
					
					for (int j = 0; j < elements_count; j++) {
						
						loc_strings.removeChild(localization_elements.item(0));
					}
				}
			}
		}
	}

	public String insertBindElement(Element new_bind_element, String bind_id) {
		
		Document form_xforms = component_parent.getXformsDocument();
		
		new_bind_element.setAttribute(FormManagerUtil.id_att, bind_id);
	
		Element model = FormManagerUtil.getElementByIdFromDocument(form_xforms, FormManagerUtil.head_tag, component_parent.getFormId());
		model.appendChild(new_bind_element);
		
		String type_att = new_bind_element.getAttribute(FormManagerUtil.type_att);
		
		if(type_att != null && type_att.startsWith(FormManagerUtil.fb_)) {
			
			new_bind_element.setAttribute(FormManagerUtil.type_att, component.getId()+type_att);
			return type_att;
		}
		return null;
	}
	
	protected void insertNodesetElement(String bind_id) {
		
		if(xforms_component.getNodeset() != null) {
			
			Document xforms_doc = component_parent.getXformsDocument();
//			insert nodeset element
			Element nodeset_element = xforms_doc.createElement(bind_id);
			
			FormManagerUtil.insertNodesetElement(
					xforms_doc, xforms_component.getNodeset(), nodeset_element
			);
			
			xforms_component.setNodeset(nodeset_element);
		}
	}
	
	public void changeBindName(String new_bind_name) {

		Element bind_element = xforms_component.getBind();
		
		if(bind_element == null)
			return;
		new_bind_name = FormManagerUtil.escapeNonXmlTagSymbols(new_bind_name.replace(' ', '_'));
		Element nodeset_element = xforms_component.getNodeset();
		bind_element.setAttribute(FormManagerUtil.nodeset_att, new_bind_name);
		nodeset_element = (Element)nodeset_element.getOwnerDocument().renameNode(nodeset_element, nodeset_element.getNamespaceURI(), new_bind_name);
		xforms_component.setNodeset(nodeset_element);
	}
	
	public void updateP3pType() {
		
		PropertiesComponent props = component.getProperties();
		String p3ptype = props.getP3ptype();
		
		if(p3ptype == null)
			xforms_component.getBind().removeAttribute(FormManagerUtil.p3ptype_att);
		else
			xforms_component.getBind().setAttribute(FormManagerUtil.p3ptype_att, p3ptype);
	}
	
	public LocalizedStringBean getLocalizedStrings() {
		
		return FormManagerUtil.getLabelLocalizedStrings(xforms_component.getElement(), component_parent.getXformsDocument());
	}
	
	public LocalizedStringBean getErrorLabelLocalizedStrings() {
		return FormManagerUtil.getErrorLabelLocalizedStrings(xforms_component.getElement(), component_parent.getXformsDocument());
	}
	public Element getComponentElement() {
		return xforms_component.getElement();
	}
	public Element getComponentNodeset() {
		return xforms_component.getNodeset();
	}
	public Element getComponentBind() {
		return xforms_component.getBind();
	}
}