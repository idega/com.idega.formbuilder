package com.idega.formbuilder.presentation.beans;

import java.util.List;

import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.component.beans.ItemBean;

public abstract class GenericComponent {
	
	public abstract Component getComponent();

	public abstract void setLabel(String value);
	
	public abstract String getLabel();
	
	public abstract void setErrorMessage(String value);
	
	public abstract String getErrorMessage();
	
	public abstract void setHelpMessage(String value);
	
	public abstract String getHelpMessage();
	
	public abstract void setRequired(boolean value);
	
	public abstract boolean getRequired();
	
	public abstract void setPlainText(String value);
	
	public abstract String getPlainText();
	
	public abstract void setAddButtonLabel(String value);
	
	public abstract String getAddButtonLabel();
	
	public abstract void setRemoveButtonLabel(String value);
	
	public abstract String getRemoveButtonLabel();
	
	public abstract void setExternalSrc(String value);
	
	public abstract String getExternalSrc();
	
	public abstract List<ItemBean> getItems();
	
	public abstract void setItems(List<ItemBean> list);
	
	public abstract String getDataSrc();
	
	public abstract void setDataSrc(String dataSrc);
	
	public abstract void setAutofillKey(String key);
	
	public abstract String getAutofillKey();

}
