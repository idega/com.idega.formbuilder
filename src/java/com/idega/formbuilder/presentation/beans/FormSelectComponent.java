package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.idega.formbuilder.util.FBUtil;
import com.idega.xformsmanager.business.component.ComponentSelect;
import com.idega.xformsmanager.component.beans.ItemBean;

public class FormSelectComponent extends FormComponent {
	
	public FormSelectComponent(ComponentSelect component) {
		this.component = component;
	}

	@Override
	public ComponentSelect getComponent() {
		return (ComponentSelect) component;
	}

	public void setComponent(ComponentSelect component) {
		this.component = component;
	}
	
	@Override
	public void setExternalSrc(String externalSrc) {
		getComponent().getProperties().setExternalDataSrc(externalSrc);
	}
	
	@Override
	public String getExternalSrc() {
		return getComponent().getProperties().getExternalDataSrc();
	}
	
	@Override
	public List<ItemBean> getItems() {
		if(getComponent() != null && getComponent().getProperties() != null && getComponent().getProperties().getItemset() != null) {
			Logger.getLogger(getClass().getName()).info("items="+getComponent().getProperties().getItemset());
			return getComponent().getProperties().getItemset().getItems(FBUtil.getUILocale());
		}
		return new ArrayList<ItemBean>();
	}
	
	@Override
	public void setItems(List<ItemBean> items) {
		getComponent().getProperties().getItemset().setItems(FBUtil.getUILocale(), items);
	}
	
	@Override
	public String getDataSrc() {
		if(getComponent().getProperties().getDataSrcUsed() != null) {
			return getComponent().getProperties().getDataSrcUsed().toString();
		} else {
			return DataSourceList.localDataSrc;
		}
	}

	@Override
	public void setDataSrc(String dataSrc) {
		getComponent().getProperties().setDataSrcUsed(Integer.parseInt(dataSrc));
	}

}
