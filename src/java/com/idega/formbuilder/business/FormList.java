package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class FormList implements Serializable {
	
	private static final long serialVersionUID = -1462694114566788168L;
	
	private List<SelectItem> forms = new ArrayList<SelectItem>();

	public List<SelectItem> getForms() {
		/*FormListViewer viewer = new FormListViewer();
		List<FormBean> formBeans = viewer.getForms();
		Iterator it = formBeans.iterator();
		while(it.hasNext()) {
			FormBean current = (FormBean) it.next();
			forms.add(new SelectItem(new Integer(12), current.getName()));
		}*/
		forms.clear();
		forms.add(new SelectItem("LABEL", "--Switch to--"));
		forms.add(new SelectItem(new Integer(1).toString(), "Form1"));
		forms.add(new SelectItem(new Integer(2).toString(), "Form2"));
		forms.add(new SelectItem(new Integer(3).toString(), "Form3"));
		forms.add(new SelectItem(new Integer(4).toString(), "Form4"));
		return forms;
	}

	public void setForms(List<SelectItem> forms) {
		this.forms = forms;
	}

}
