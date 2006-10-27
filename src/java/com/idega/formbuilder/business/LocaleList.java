package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class LocaleList implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private List<SelectItem> locales = new ArrayList<SelectItem>();

	public List<SelectItem> getLocales() {
		locales.clear();
		/*Iterator it = FacesContext.getCurrentInstance().getExternalContext().getRequestLocales();
		while(it.hasNext()) {
			Locale current = (Locale) it.next();
			locales.add(new SelectItem(current.getLanguage(), current.getCountry()));
		}*/
		locales.add(new SelectItem("en", "English"));
		locales.add(new SelectItem("se", "Swedish"));
		locales.add(new SelectItem("is", "Islandic"));
		locales.add(new SelectItem("lt", "Lithuanian"));
		return locales;
	}

	public void setLocales(List<SelectItem> locales) {
		this.locales = locales;
	}

}
