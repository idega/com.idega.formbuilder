package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class ComponentPalette implements Serializable {
		
	private static final long serialVersionUID = -7539955900908793992L;
	private List<FormField> fields = new LinkedList<FormField>();
	
	public ComponentPalette() {
		//TODO Substitute with proper logic for retrieving a list of FormFields
		fields.add(new FormField("field_text"));
		fields.add(new FormField("field_radio"));
		fields.add(new FormField("field_textarea"));
		fields.add(new FormField("field_checkbox"));
		fields.add(new FormField("field_dropdown"));
		fields.add(new FormField("field_list"));
	}

	public List<FormField> getFields() {
		return fields;
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}
}
