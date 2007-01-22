package com.idega.formbuilder.presentation.beans;

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
		FormComponent fc = (FormComponent) WFUtil.getBeanInstance("formComponent");
		IComponentProperties icp = fc.getProperties();
		if(icp instanceof IComponentPropertiesSelect) {
			IComponentPropertiesSelect icps = (IComponentPropertiesSelect) icp;
			if(icps.getDataSrcUsed() != null) {
				this.selectedDataSource = icps.getDataSrcUsed().toString();
			} else {
				this.setSelectedDataSource(localDataSrc);
			}
		}
		return selectedDataSource;
	}

	public void setSelectedDataSource(String selectedDataSource) {
		this.selectedDataSource = selectedDataSource;
		((IComponentPropertiesSelect)((FormComponent) WFUtil.getBeanInstance("formComponent")).getProperties()).setDataSrcUsed(Integer.parseInt(selectedDataSource));
	}

	public DataSourceList() {
		selectedDataSource = localDataSrc;
		sources.clear();
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
			selectedDataSource = "2";
			((IComponentPropertiesSelect)((FormComponent) WFUtil.getBeanInstance("formComponent")).getProperties()).setDataSrcUsed(IComponentPropertiesSelect.EXTERNAL_DATA_SRC);
		} else if(selectedDataSource.equals("2")) {
			selectedDataSource = "1";
			((IComponentPropertiesSelect)((FormComponent) WFUtil.getBeanInstance("formComponent")).getProperties()).setDataSrcUsed(IComponentPropertiesSelect.LOCAL_DATA_SRC);
		}
	}

}
