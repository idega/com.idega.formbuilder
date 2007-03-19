package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.idegaweb.BundleLocalizationMap;
import com.idega.webface.WFUtil;

public class PaletteComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694114806788168L;
	
	private static Map localizedStrings = (BundleLocalizationMap) ((HashMap) WFUtil.getBeanInstance("localizedStrings")).get("com.idega.formbuilder");
	
	private String type;
	private String name;
	private String iconPath;
	private String autofill_key;
	
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public PaletteComponent() {
		this.type = "";
		this.iconPath = "";
	}
	
	public PaletteComponent(String type) throws Exception {
		this.type = type;
		this.iconPath = (String) localizedStrings.get(type + "_icon");
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		
		if(name == null) {
			
			if(type != null) {
				
				if(autofill_key != null)
					name = (String) localizedStrings.get(type+'-'+autofill_key);
				else
					name = (String) localizedStrings.get(type);
				
			} else
				name = "";
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAutofillKey() {
		return autofill_key;
	}

	public void setAutofillKey(String autofill_key) {
		this.autofill_key = autofill_key;
	}
}
