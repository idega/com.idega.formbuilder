package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.idega.formbuilder.business.form.beans.IComponentPropertiesSelect;

public class DataSourceList implements Serializable {
	
private static final long serialVersionUID = -1462694112346788168L;
	
	private List<SelectItem> sources = new ArrayList<SelectItem>();
	
	private String selectedDataSource;
	
	public String getSelectedDataSource() {
		return selectedDataSource;
	}

	public void setSelectedDataSource(String selectedDataSource) {
		this.selectedDataSource = selectedDataSource;
	}

	public DataSourceList() {
		String localDataSrc = new Integer(IComponentPropertiesSelect.LOCAL_DATA_SRC).toString();
		String externalDataSrc = new Integer(IComponentPropertiesSelect.EXTERNAL_DATA_SRC).toString();
		selectedDataSource = externalDataSrc;
		sources.add(new SelectItem(localDataSrc, "List of values"));
		sources.add(new SelectItem(externalDataSrc, "External file"));
	}

	public List<SelectItem> getSources() {
		return sources;
	}

	public void setSources(List<SelectItem> sources) {
		this.sources = sources;
	}

}
