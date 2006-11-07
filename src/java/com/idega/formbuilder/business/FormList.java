package com.idega.formbuilder.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import com.idega.block.form.bean.FormBean;
import com.idega.block.form.presentation.FormListViewer;
import com.idega.repository.data.Singleton;

/**
 * It's singleton because this bean is supposed to be used as managed bean in application scope
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormList implements Singleton {
	
	private List<SelectItem> forms_data;

	public List<SelectItem> getForms() {
		
		if(forms_data == null)
			loadFormList();
		
		return forms_data;
	}

	public void setForms(List<SelectItem> forms_data) {
		this.forms_data = forms_data;
	}

	public void loadFormList() {
		
		if(forms_data == null)
			forms_data = new ArrayList<SelectItem>();
		else
			forms_data.clear();
		
		FormListViewer viewer = new FormListViewer();
		List<FormBean> form_beans = viewer.getForms();
		
		if(form_beans == null)
			return;
		
		for (Iterator<FormBean> iter = form_beans.iterator(); iter.hasNext();) {
			FormBean form_bean = iter.next();
			
			String form_id = form_bean.getFormId();
			String form_name = form_bean.getName();
			
			if(form_id != null && form_name != null)
				forms_data.add(new SelectItem(form_id, form_name));
		}
	}
}