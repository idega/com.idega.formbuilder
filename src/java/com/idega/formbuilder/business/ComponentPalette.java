package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


public class ComponentPalette implements Serializable {
		
	private static final long serialVersionUID = -7539955900908793992L;
	private List<FormField> fields = new LinkedList<FormField>();
	
	public ComponentPalette() {
		fields.add(new FormField("Vardas", ""));
		fields.add(new FormField("Profesija", ""));
		fields.add(new FormField("Email", ""));
		fields.add(new FormField("Gymimo data", ""));
		fields.add(new FormField("Checkbox", ""));
		fields.add(new FormField("Input field", ""));
	}

	public List<FormField> getFields() {
		return fields;
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}
	
	public String sum() {
		return "WOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW";
	}
	
}
