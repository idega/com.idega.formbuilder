package com.idega.formbuilder.business.form.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerPage extends XFormsManagerContainer {

	@Override
	public String insertBindElement(Element new_bind_element, String bind_id) {
		
		Document form_xforms = component_parent.getXformsDocument();
		new_bind_element.setAttribute(FormManagerUtil.id_att, bind_id);
		new_bind_element.setAttribute(FormManagerUtil.nodeset_att, 
				new StringBuffer(FormManagerUtil.inst_start)
				.append(FormManagerUtil.wizard_id_att_val)
				.append(FormManagerUtil.inst_end)
				.append(FormManagerUtil.slash)
				.append(bind_id)
				.toString()
		);
		new_bind_element.setAttribute(FormManagerUtil.relevant_att, 
				new StringBuffer(FormManagerUtil.inst_start)
				.append(FormManagerUtil.wizard_id_att_val)
				.append(FormManagerUtil.inst_end)
				.append(FormManagerUtil.slash)
				.append(bind_id)
				.append(FormManagerUtil.slash)
				.append(FormManagerUtil.relevant_yes)
				.toString()
		);

		Element model = FormManagerUtil.getElementByIdFromDocument(form_xforms, FormManagerUtil.head_tag, component_parent.getFormId());
		model.appendChild(new_bind_element);
		
		String type_att = new_bind_element.getAttribute(FormManagerUtil.type_att);
		
		if(type_att != null && type_att.startsWith(FormManagerUtil.fb_)) {
			
			new_bind_element.setAttribute(FormManagerUtil.type_att, component.getId()+type_att);
			return type_att;
		}
		return null;
	}
	
	@Override
	protected void insertNodesetElement(String bind_id) {
		
		if(xforms_component.getNodeset() != null) {
			
			Document xforms_doc = component_parent.getXformsDocument();

			Element nodeset_element = xforms_doc.createElement(bind_id);
			nodeset_element.setAttribute(FormManagerUtil.relevant_att, FormManagerUtil.no);
			
			FormManagerUtil.insertPageNodeset(
					xforms_doc, xforms_component.getNodeset(), nodeset_element
			);
			
			xforms_component.setNodeset(nodeset_element);
		}
	}
	
	public void changePageRelevance(boolean relevant) {
		
		xforms_component.getNodeset().setAttribute(
				FormManagerUtil.relevant_att, relevant ? FormManagerUtil.yes : FormManagerUtil.no
		);
	}
}