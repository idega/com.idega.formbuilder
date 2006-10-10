package com.idega.formbuilder.business.form.beans;

import java.util.List;

import org.w3c.dom.Document;

public interface IFormComponentParent {
	
	public abstract Document getXformsDocument();
	
	public abstract List<String> getFormComponentsIdList();
	
	public abstract List<String> getFormXsdContainedTypesDeclarations();
	
	public abstract IFormComponent getFormComponent(String component_id);
}