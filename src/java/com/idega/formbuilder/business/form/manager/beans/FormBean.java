package com.idega.formbuilder.business.form.manager.beans;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormBean {

	private FormPropertiesBean form_props;
	private Document form_xforms;
	private List<String> form_components_id_list = new LinkedList<String>();
	private List<String> form_xsd_contained_types_declarations = new LinkedList<String>();

	public FormPropertiesBean getFormProperties() {
		return form_props;
	}

	public void setFormProperties(FormPropertiesBean form_props) {
		this.form_props = form_props;
	}

	public Document getFormXforms() {
		return form_xforms;
	}

	public void setFormXforms(Document form_xforms) {
		this.form_xforms = form_xforms;
	}

	public List<String> getFormComponentsIdList() {
		return form_components_id_list;
	}

	public List<String> getFormXsdContainedTypesDeclarations() {
		return form_xsd_contained_types_declarations;
	}
}
