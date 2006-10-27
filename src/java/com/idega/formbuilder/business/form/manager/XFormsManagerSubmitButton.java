package com.idega.formbuilder.business.form.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.beans.XFormsComponentDataBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerSubmitButton extends XFormsManager {
	
	public void updateAction() {
		
	}
	
	private static final String SUBMIT_TAG = "xf:submit";
	private static final String SUBMISSION_ATT = "submission";
	
	public XFormsComponentDataBean getXFormsSubmitComponent(Document xforms_doc) {
		
		NodeList submits = xforms_doc.getElementsByTagName(SUBMIT_TAG);
		
		if(submits == null || submits.getLength() == 0)
			return null;
		
		Element submit_element = (Element)submits.item(0);
		
		String submission_id = submit_element.getAttribute(SUBMISSION_ATT);
		
		if(submission_id == null)
			return null;
		
		Element submission_element = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, submission_id);
		
		if(submission_element == null)
			return null;
		
		XFormsComponentDataBean xforms_component = new XFormsComponentDataBean();
		xforms_component.setElement(submit_element);
		xforms_component.setBind(submission_element);
		
		return xforms_component;
	}
}