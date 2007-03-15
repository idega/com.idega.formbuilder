package com.idega.formbuilder.presentation.converters;

import java.util.List;

import com.idega.documentmanager.business.form.beans.ItemBean;

public class FormComponentInfo {
	
	private boolean required;
	private String label;
	private String errorMessage;
	private String helpMessage;
	private String externalSrc;
	private String autofillKey;
	private String plainText;
	private List<ItemBean> items;
	
	private boolean complex;
	private boolean local;
	private boolean autofill;
	private boolean plain;
	
	public boolean isPlain() {
		return plain;
	}

	public void setPlain(boolean plain) {
		this.plain = plain;
	}

	public void setAutofill(boolean autofill) {
		this.autofill = autofill;
	}

	public boolean isAutofill() {
		if(autofillKey != null && !autofillKey.equals("")) {
			autofill = true;
			return autofill;
		}
		autofill = false;
		return autofill;
	}
	
	public boolean isLocal() {
		return local;
	}
	
	public void setLocal(boolean local) {
		this.local = local;
	}
	
	public boolean isComplex() {
		return complex;
	}
	
	public void setComplex(boolean complex) {
		this.complex = complex;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getHelpMessage() {
		return helpMessage;
	}
	
	public void setHelpMessage(String helpMessage) {
		this.helpMessage = helpMessage;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public String getExternalSrc() {
		return externalSrc;
	}
	public void setExternalSrc(String externalSrc) {
		this.externalSrc = externalSrc;
	}
	public String getAutofillKey() {
		return autofillKey;
	}
	public void setAutofillKey(String autofillKey) {
		this.autofillKey = autofillKey;
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}

}
