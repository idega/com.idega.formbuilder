package com.idega.formbuilder.presentation.beans;

import java.util.List;

import javax.faces.model.SelectItem;

import com.idega.webface.WFUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class FormList {
	
	public List<SelectItem> getForms() {
//		List<SelectItem> forms = new LinkedList<SelectItem>();
//		forms.add(new SelectItem("INACTIVE", "--Please select a form--"));
//		forms.addAll(((com.idega.block.form.bean.FormList) WFUtil.getBeanInstance("formList")).getForms());
		return ((com.idega.block.form.bean.FormList) WFUtil.getBeanInstance("formList")).getForms();
	}
}