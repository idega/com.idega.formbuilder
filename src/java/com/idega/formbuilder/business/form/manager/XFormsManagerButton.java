package com.idega.formbuilder.business.form.manager;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.ConstButtonType;
import com.idega.formbuilder.business.form.beans.IFormComponentPage;
import com.idega.formbuilder.business.form.beans.XFormsComponentButtonDataBean;
import com.idega.formbuilder.business.form.beans.XFormsComponentDataBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerButton extends XFormsManager {
	
	@Override
	public void loadXFormsComponentFromDocument(String component_id) {
		super.loadXFormsComponentFromDocument(component_id);
		Element button_element = xforms_component.getElement();
		String button_type = button_element.getAttribute(FormManagerUtil.name_att);
		
		if(button_type != null)
			component.setType(button_type);

		loadSetRelevantValueElements();
	}
	
	
	protected void loadSetRelevantValueElements() {
		
		XFormsComponentButtonDataBean xforms_component = (XFormsComponentButtonDataBean)this.xforms_component;
		if(component.getType().equals(ConstButtonType.previous_page_button) || 
				component.getType().equals(ConstButtonType.next_page_button)
		) {
			
			Element action_element = (Element)xforms_component.getElement().getElementsByTagName(FormManagerUtil.action_tag).item(0);
			NodeList set_value_elements = action_element.getElementsByTagName(FormManagerUtil.setvalue_tag);
			
			Element set_value_yes = null;
			Element set_value_no = null;
			
			for (int i = 0; i < set_value_elements.getLength(); i++) {
				
				Element set_value_element = (Element)set_value_elements.item(i);
				String value_att = set_value_element.getAttribute(FormManagerUtil.value_att);
				
				if(value_att == null)
					throw new NullPointerException("Incorrect button format - setvalue element contains no value attribute");
				
				if(value_att.contains(FormManagerUtil.yes))
					set_value_yes = set_value_element;
				else if(value_att.contains(FormManagerUtil.no))
					set_value_no = set_value_element;
				else
					throw new IllegalArgumentException("Incorrect button format - setvalue element contains not supported value attribute: "+value_att);
			}
			
			if(set_value_yes == null || set_value_no == null)
				throw new NullPointerException("either setvalue tag was not found: " +
						"\nset_value_yes="+set_value_yes+
						"\nset_value_no="+set_value_no);
			
			if(component.getType().equals(ConstButtonType.previous_page_button)) {
				xforms_component.setPreviousPageSetvalueElement(set_value_yes);
			} else {
				xforms_component.setNextPageSetvalueElement(set_value_yes);
			}
			xforms_component.setCurrentPageSetvalueElement(set_value_no);
		}
	}
	
	@Override
	protected XFormsComponentDataBean newXFormsComponentDataBeanInstance() {
		System.out.println("id2: "+component.getId());
		return new XFormsComponentButtonDataBean();
	}
	
	public void renewButtonRelevantPages(IFormComponentPage previous, IFormComponentPage next, IFormComponentPage current) {
		
		XFormsComponentButtonDataBean xforms_component = (XFormsComponentButtonDataBean)this.xforms_component;
		
		if(component.getType().equals(ConstButtonType.previous_page_button) || component.getType().equals(ConstButtonType.next_page_button)) {
			
			if(component.getType().equals(ConstButtonType.previous_page_button)) {
				
				if(previous != null) {
					
					xforms_component.getPreviousPageSetvalueElement()
					.setAttribute(FormManagerUtil.ref_s_att,
							new StringBuffer(FormManagerUtil.inst_start)
							.append(FormManagerUtil.wizard_id_att_val)
							.append(FormManagerUtil.inst_end)
							.append(FormManagerUtil.slash)
							.append(previous.getComponentXFormsManager().getComponentNodeset().getNodeName())
							.append(FormManagerUtil.slash)
							.append(FormManagerUtil.relevant)
							.toString()
					);
				} else
					xforms_component.getPreviousPageSetvalueElement().removeAttribute(FormManagerUtil.ref_s_att);
				
			} else {
				
				if(next != null) {
					xforms_component.getNextPageSetvalueElement()
					.setAttribute(FormManagerUtil.ref_s_att,
							new StringBuffer(FormManagerUtil.inst_start)
							.append(FormManagerUtil.wizard_id_att_val)
							.append(FormManagerUtil.inst_end)
							.append(FormManagerUtil.slash)
							.append(next.getComponentXFormsManager().getComponentNodeset().getNodeName())
							.append(FormManagerUtil.slash)
							.append(FormManagerUtil.relevant)
							.toString()
					);
				} else
					xforms_component.getNextPageSetvalueElement().removeAttribute(FormManagerUtil.ref_s_att);
			}
			xforms_component.getCurrentPageSetvalueElement()
			.setAttribute(FormManagerUtil.ref_s_att,
					new StringBuffer(FormManagerUtil.inst_start)
					.append(FormManagerUtil.wizard_id_att_val)
					.append(FormManagerUtil.inst_end)
					.append(FormManagerUtil.slash)
					.append(current.getComponentXFormsManager().getComponentNodeset().getNodeName())
					.append(FormManagerUtil.slash)
					.append(FormManagerUtil.relevant)
					.toString()
			);
		}
	}
	@Override
	public void addComponentToDocument() {
		super.addComponentToDocument();
		loadSetRelevantValueElements();
	}
}