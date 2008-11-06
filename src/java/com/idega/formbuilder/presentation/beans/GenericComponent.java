package com.idega.formbuilder.presentation.beans;

import java.util.List;

import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.component.beans.ItemBean;

public abstract class GenericComponent {

	public abstract String getId();

	public abstract Component getComponent();

	public abstract void setLabel(String value);

	public abstract String getLabel();

	public abstract void setErrorMessage(ErrorType errorType, String value);

	public abstract String getErrorMessage(ErrorType errorType);

	public abstract void setHelpMessage(String value);

	public abstract String getHelpMessage();

//	public abstract void setValidationText(String value);
//
//	public abstract String getValidationText();

	public abstract void setRequired(boolean value);

	public abstract boolean getRequired();

	public abstract void setPlainText(String value);

	public abstract String getPlainText();

	public abstract void setAddButtonLabel(String value);

	public abstract String getAddButtonLabel();

	public abstract void setRemoveButtonLabel(String value);

	public abstract String getRemoveButtonLabel();
	
	public String getDescriptionLabel() {
		return null;
	}

	public void setDescriptionLabel(String value) {
	}

	public abstract void setExternalSrc(String value);

	public abstract String getExternalSrc();

	public abstract List<ItemBean> getItems();

	public abstract void setItems(List<ItemBean> list);

	public abstract String getDataSrc();

	public abstract void setDataSrc(String dataSrc);

	public abstract void setAutofillKey(String key);

	public abstract String getAutofillKey();

	public abstract String getUploadDescription();

	public abstract void setUploadDescription(String value);
}