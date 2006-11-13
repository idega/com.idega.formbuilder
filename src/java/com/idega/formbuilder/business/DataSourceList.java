package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IComponentPropertiesSelect;
import com.idega.webface.WFUtil;

public class DataSourceList implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private String localDataSrc = new Integer(IComponentPropertiesSelect.LOCAL_DATA_SRC).toString();
	private String externalDataSrc = new Integer(IComponentPropertiesSelect.EXTERNAL_DATA_SRC).toString();
	
	private List<SelectItem> sources = new ArrayList<SelectItem>();
	
	private String selectedDataSource;
	
	public String getSelectedDataSource() {
		FormComponent fc = (FormComponent) WFUtil.getBeanInstance("component");
		IComponentProperties icp = fc.getProperties();
		if(icp instanceof IComponentPropertiesSelect) {
			IComponentPropertiesSelect icps = (IComponentPropertiesSelect) icp;
			if(icps.getDataSrcUsed() != null) {
				this.selectedDataSource = icps.getDataSrcUsed().toString();
			} else {
				this.selectedDataSource = externalDataSrc;
			}
		} else {
			System.out.println("Wronf instance type");
		}
		return selectedDataSource;
	}

	public void setSelectedDataSource(String selectedDataSource) {
		this.selectedDataSource = selectedDataSource;
		((IComponentPropertiesSelect)((FormComponent) WFUtil.getBeanInstance("component")).getProperties()).setDataSrcUsed(Integer.parseInt(selectedDataSource));
	}

	public DataSourceList() {
		sources.add(new SelectItem(localDataSrc, "List of values"));
		sources.add(new SelectItem(externalDataSrc, "External file"));
	}

	public List<SelectItem> getSources() {
		return sources;
	}

	public void setSources(List<SelectItem> sources) {
		this.sources = sources;
	}
	
	public void switchDataSource(ActionEvent ae) {
		if(selectedDataSource.equals("1")) {
			setSelectedDataSource("2");
		} else if(selectedDataSource.equals("2")) {
			setSelectedDataSource("1");
		}
	}

}
