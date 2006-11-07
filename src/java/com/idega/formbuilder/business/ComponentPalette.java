package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ComponentPalette implements Serializable {
		
	private static final long serialVersionUID = -7539955900908793992L;
	
	private List<FormComponent> fields = new ArrayList<FormComponent>();
	//private Map localizedFBStrings = new HashMap();
	
	public ComponentPalette() {
		/*Map localizedStrings = (HashMap) WFUtil.getBeanInstance("localizedStrings");
		localizedFBStrings = (BundleLocalizationMap) localizedStrings.get("com.idega.formbuilder");*/
	}
	
	public ComponentPalette(List list) {
		Iterator it = list.iterator();
		FormComponent temp = null;
		System.out.println("---------------------");
		while(it.hasNext()) {
			System.out.println("XXXXXXXXXXXXXXXXXX");
			try {
			temp = new FormComponent((String) it.next());
			} catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("XXXXXXXXXXXXXXXXXX");
			fields.add(temp);
			System.out.println("XXXXXXXXXXXXXXXXXX");
		}
	}

	public List<FormComponent> getFields() {
		return fields;
	}
	
	public void setTypeList(List<String> list) {
		Iterator it = list.iterator();
		FormComponent temp = null;
		while(it.hasNext()) {
			try {
				temp = new FormComponent((String) it.next());
				} catch(Exception e) {
					e.printStackTrace();
				}
			fields.add(temp);
		}
	}

	public void setFields(List<FormComponent> fields) {
		this.fields = fields;
	}
}
