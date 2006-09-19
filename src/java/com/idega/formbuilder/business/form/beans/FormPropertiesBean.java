package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormPropertiesBean extends FBGenericBean {
	
	private String name;
	private String[] classes;
	private Integer last_component_id = 0;
	
	public Integer getLast_component_id() {
		return last_component_id;
	}
	public void setLast_component_id(Integer last_component_id) {
		this.last_component_id = last_component_id;
	}
	public String[] getClasses() {
		return classes;
	}
	public void setClasses(String[] classes) {
		this.classes = classes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
