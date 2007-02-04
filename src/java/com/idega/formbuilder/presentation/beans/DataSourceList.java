package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.idega.formbuilder.business.form.PropertiesSelect;

public class DataSourceList implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	public static String localDataSrc = new Integer(PropertiesSelect.LOCAL_DATA_SRC).toString();
	public static String externalDataSrc = new Integer(PropertiesSelect.EXTERNAL_DATA_SRC).toString();
	
	private List<SelectItem> sources = new ArrayList<SelectItem>();

	public DataSourceList() {
		sources.clear();
		sources.add(new SelectItem(localDataSrc, "List of values"));
		sources.add(new SelectItem(externalDataSrc, "External"));
	}

	public List<SelectItem> getSources() {
		return sources;
	}

	public void setSources(List<SelectItem> sources) {
		this.sources = sources;
	}

}
