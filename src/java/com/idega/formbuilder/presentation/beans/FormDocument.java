package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private String formTitle;
	private String formId;
	private int stepCount;
	private String selectedComponent;
	private String submitLabel;
	private List<String> pages;
	
	private LocalizedStringBean formTitleBean;
	private LocalizedStringBean submitLabelBean;
	
	public FormDocument() {
		this.formId = "";
		
		this.stepCount = 0;
		this.selectedComponent = "";
		
		this.formTitle = "";
		this.submitLabel = "";
		
		this.formTitleBean = null;
		this.submitLabelBean = null;
		
		this.pages = new ArrayList<String>();
		
		this.pages.add("Page1");
		this.pages.add("Page2");
		this.pages.add("Page3");
		this.pages.add("Page4");
		this.pages.add("Page5");
	}
	
	public void clearFormDocumentInfo() {
		this.formId = "";
		this.formTitle = "";
		this.formTitleBean = null;
		this.submitLabel = "";
		this.submitLabelBean = null;
	}
	
	public void loadFormProperties(ActionEvent ae) {
		formTitleBean = ActionManager.getFormManagerInstance().getFormTitle();
		formTitle = formTitleBean.getString(new Locale("en"));

		submitLabelBean = ActionManager.getFormManagerInstance().getSubmitButtonProperties().getLabel();
		submitLabel = submitLabelBean.getString(new Locale("en"));
		
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("2");
	}
	
	public void loadFormInfo(ActionEvent ae) {
//		pages = 
	}
	
	public void saveFormTitle(ActionEvent ae) throws Exception {
		String yes = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:formTitle");
		if(yes != null) {
			setFormTitle(yes);
			ActionManager.getFormManagerInstance().setFormTitle(formTitleBean);
		}
	}
	
	public void saveSubmitLabel(ActionEvent ae) throws Exception {
		String yes = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:submitLabel");
		if(yes != null) {
			setSubmitLabel(yes);
			ActionManager.getFormManagerInstance().getSubmitButtonProperties().setLabel(submitLabelBean);
		}
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
		if(formTitleBean != null) {
			formTitleBean.setString(new Locale("en"), formTitle);
		}
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
		if(submitLabelBean != null) {
			submitLabelBean.setString(new Locale("en"), submitLabel);
		}
	}

	public LocalizedStringBean getFormTitleBean() {
		return formTitleBean;
	}

	public void setFormTitleBean(LocalizedStringBean formTitleBean) {
		this.formTitleBean = formTitleBean;
	}

	public LocalizedStringBean getSubmitLabelBean() {
		return submitLabelBean;
	}

	public void setSubmitLabelBean(LocalizedStringBean submitLabelBean) {
		this.submitLabelBean = submitLabelBean;
	}

	public List<String> getPages() {
		return pages;
	}

	public void setPages(List<String> pages) {
		this.pages = pages;
	}

}
