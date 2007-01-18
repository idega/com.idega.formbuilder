package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.webface.WFUtil;

public class FormDocument implements Serializable, ActionListener {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private String formTitle;
	private String formId;
	private int stepCount;
	private String selectedComponent;
	private String submitLabel;
	
	public FormDocument() {
		this.formTitle = "";
		this.formId = "";
		this.stepCount = 0;
		this.selectedComponent = "";
		this.submitLabel = "";
	}
	
	public void processAction(ActionEvent ae) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("2");
	}
	
	public String getSelectedComponent() {
		return selectedComponent;
	}

	public void setSelectedComponent(String selectedComponent) {
		this.selectedComponent = selectedComponent;
	}
	
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormTitle() {
		return formTitle;
	}
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	public int getStepCount() {
		return stepCount;
	}
	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}

	public String getSubmitLabel() {
		return submitLabel;
	}

	public void setSubmitLabel(String submitLabel) {
		this.submitLabel = submitLabel;
	}

}
