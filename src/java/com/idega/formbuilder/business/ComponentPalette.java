package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ComponentPalette implements Serializable {
		
	private List<Field> fields = new LinkedList<Field>();
	
	public ComponentPalette() {
		fields.add(new Field("Vardas", ""));
		fields.add(new Field("Profesija", ""));
		fields.add(new Field("Nieko", ""));
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
}
