package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ComponentPalette implements Serializable {
		
	private static final long serialVersionUID = -7539955900908793992L;
	
	private List<FormField> fields = new ArrayList<FormField>();
	//private Map localizedFBStrings = new HashMap();
	
	public ComponentPalette() {
		/*Map localizedStrings = (HashMap) WFUtil.getBeanInstance("localizedStrings");
		localizedFBStrings = (BundleLocalizationMap) localizedStrings.get("com.idega.formbuilder");*/
	}
	
	public ComponentPalette(List list) {
		Iterator it = list.iterator();
		FormField temp = null;
		System.out.println("---------------------");
		while(it.hasNext()) {
			System.out.println("XXXXXXXXXXXXXXXXXX");
			temp = new FormField((String) it.next());
			System.out.println("XXXXXXXXXXXXXXXXXX");
			fields.add(temp);
			System.out.println("XXXXXXXXXXXXXXXXXX");
		}
	}

	public List<FormField> getFields() {
		return fields;
	}
	
	public void setTypeList(List<String> list) {
		Iterator it = list.iterator();
		FormField temp = null;
		while(it.hasNext()) {
			temp = new FormField((String) it.next());
			fields.add(temp);
		}
	}

	public void setFields(List<FormField> fields) {
		this.fields = fields;
	}
}
