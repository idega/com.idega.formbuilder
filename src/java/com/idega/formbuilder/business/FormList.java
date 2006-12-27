package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.idega.webface.WFUtil;

public class FormList implements Serializable {
	
	private static final long serialVersionUID = -7539955900008793992L;
	
	public List<SelectItem> getForms() {
		List<SelectItem> forms = new LinkedList<SelectItem>();
		forms.add(new SelectItem("INACTIVE", "--Please select a form--"));
		forms.addAll(((com.idega.block.form.bean.FormList) WFUtil.getBeanInstance("formList")).getForms());
		return forms;
	}

}
