package com.idega.formbuilder.presentation.converters;

public class FormComponentInfo {
	
	private boolean required;
	private String label;
	private String errorMessage;
	private String helpMessage;
	private String emptyLabel;
	private String externalSrc;
	
	private boolean complex;
	private boolean local;
	
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
	public String getEmptyLabel() {
		return emptyLabel;
	}
	public void setEmptyLabel(String emptyLabel) {
		this.emptyLabel = emptyLabel;
	}
	public String getExternalSrc() {
		return externalSrc;
	}
	public void setExternalSrc(String externalSrc) {
		this.externalSrc = externalSrc;
	}

}
