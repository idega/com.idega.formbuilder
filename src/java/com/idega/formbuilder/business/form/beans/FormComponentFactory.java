package com.idega.formbuilder.business.form.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.repository.data.Singleton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentFactory implements Singleton {
	
	private static FormComponentFactory me;
	
	private Map<String, List<String>> components_tags_classified;
	private static final String type_simple = "type_simple";
	private static final String type_select = "type_select";
	
	private FormComponentFactory() { 
		
		components_tags_classified = new HashMap<String, List<String>>();
		
		List<String> types = new ArrayList<String>();
		types.add("fbcomp_text");
		types.add("fbcomp_textarea");
		types.add("fbcomp_secret");
		types.add("fbcomp_email");
		types.add("fbcomp_upload_file");
		components_tags_classified.put(type_simple, types);
		
		types = new ArrayList<String>();
		
		types.add("fbcomp_multiple_select_minimal");
		types.add("xf:select");
		types.add("fbcomp_single_select_minimal");
		types.add("xf:select1");
		types.add("fbcomp_multiple_select");
		types.add("fbcomp_single_select");
		
		components_tags_classified.put(type_select, types);
		
	}
	
	public static FormComponentFactory getInstance() {
		
		if (me == null) {
			
			synchronized (FormComponentFactory.class) {
				if (me == null) {
					me = new FormComponentFactory();
				}
			}
		}

		return me;
	}
	
	public IFormComponent getFormComponentByType(String component_type) {
		
		IFormComponent component = recognizeFormComponent(component_type);
		component.setType(component_type);
		
		return component;
	}
	
	public IFormComponent recognizeFormComponent(String tag_name) {
		
		if(tag_name.equals(FormManagerUtil.submit_tag))
			return new FormComponentSubmitButton();
		
//		List<String> types = components_tags_classified.get(type_simple);
//		
//		if(types.contains(tag_name)) {
//			
//			return new FormComponent();
//		} 
		
		List<String> types = components_tags_classified.get(type_select);
		
		if(types.contains(tag_name)) {
			return new FormComponentSelect();
		}
		
		return new FormComponent();
	}
}