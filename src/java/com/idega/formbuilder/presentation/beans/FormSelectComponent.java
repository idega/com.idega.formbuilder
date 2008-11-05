package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.List;

import com.idega.xformsmanager.business.component.ComponentSelect;
import com.idega.xformsmanager.component.beans.ItemBean;
import com.idega.formbuilder.util.FBUtil;

public class FormSelectComponent extends FormComponent {
	
	public FormSelectComponent(ComponentSelect component) {
		this.component = component;
	}

	public ComponentSelect getComponent() {
		return (ComponentSelect) component;
	}

	public void setComponent(ComponentSelect component) {
		this.component = component;
	}
	
	public void setExternalSrc(String externalSrc) {
		getComponent().getProperties().setExternalDataSrc(externalSrc);
	}
	
	public String getExternalSrc() {
		return getComponent().getProperties().getExternalDataSrc();
	}
	
	public List<ItemBean> getItems() {
		if(getComponent() != null) {
			return getComponent().getProperties().getItemset().getItems(FBUtil.getUILocale());
		}
		return new ArrayList<ItemBean>();
	}
	
	public void setItems(List<ItemBean> items) {
		getComponent().getProperties().getItemset().setItems(FBUtil.getUILocale(), items);
	}
	
	public String getDataSrc() {
		if(getComponent().getProperties().getDataSrcUsed() != null) {
			return getComponent().getProperties().getDataSrcUsed().toString();
		} else {
			return DataSourceList.localDataSrc;
		}
	}

	public void setDataSrc(String dataSrc) {
		getComponent().getProperties().setDataSrcUsed(Integer.parseInt(dataSrc));
	}

}
