package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.idega.formbuilder.IWBundleStarter;
import com.idega.idegaweb.BundleLocalizationMap;
import com.idega.webface.WFUtil;

public class PaletteComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694114806788168L;
	
	public static final String BEAN_ID = "paletteComponent";
	
	private static Map localizedStrings = (BundleLocalizationMap) ((HashMap) WFUtil.getBeanInstance("localizedStrings")).get(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
	
	private String type;
	private String name;
	private String iconPath;
	private String category;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public PaletteComponent() {
		this.type = "";
		this.iconPath = "";
		this.category = "";
	}
	
	public PaletteComponent(String type, String category) throws Exception {
		this.type = type;
		this.iconPath = (String) localizedStrings.get(type + "_icon");
		this.category = category;
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
				name = (String) localizedStrings.get(type);
			} else
				name = "";
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
