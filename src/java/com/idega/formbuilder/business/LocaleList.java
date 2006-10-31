package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

public class LocaleList implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private List<SelectItem> locales = new ArrayList<SelectItem>();

	public LocaleList() {
		locales.add(new SelectItem("en", "English"));
		locales.add(new SelectItem("se", "Swedish"));
		locales.add(new SelectItem("is", "Islandic"));
		locales.add(new SelectItem("lt", "Lithuanian"));
	}
	
	public List<SelectItem> getLocales() {
		return locales;
	}

	public void setLocales(List<SelectItem> locales) {
		this.locales = locales;
	}

}
