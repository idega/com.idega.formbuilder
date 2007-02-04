package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private String formTitle;
	private String formId;
	private int stepCount;
	private String selectedComponent;
	private String submitLabel;
	private String sourceCode;
	private List<String> pages;
	
	private LocalizedStringBean formTitleBean;
	private LocalizedStringBean submitLabelBean;
	
	private Document document;
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public FormDocument() {
		this.document = null;
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
	
	public void saveSourceCode(ActionEvent ae) {
		if(sourceCode == null)
			return;
		try {
			if(document != null) {
				document.setFormSourceCode(sourceCode);
			}
//			DocumentManager form_manager = ActionManager.getDocumentManagerInstance();
//			form_manager.setFormSourceCode(sourceCode);
		} catch (Exception e) {
			logger.error("Error when setting form source code", e);
		}
	}
	
	public void loadFormProperties(ActionEvent ae) {
		document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
		formTitleBean = document.getFormTitle();
		formTitle = formTitleBean.getString(new Locale("en"));

//		submitLabelBean = ActionManager.getDocumentManagerInstance().getSubmitButtonProperties().getLabel();
		submitLabel = "Dummy value";
		
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("2");
	}
	
	public void loadFormInfo(ActionEvent ae) {
//		pages = 
	}
	
	public void saveFormTitle(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("formTitle");
		if(value != null) {
			setFormTitle(value);
			document.setFormTitle(formTitleBean);
		}
	}
	
	public void saveSubmitLabel(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("submitLabel");
		if(value != null) {
			setSubmitLabel(value);
//			ActionManager.getDocumentManagerInstance().getSubmitButtonProperties().setLabel(submitLabelBean);
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

	public String getSourceCode() {
		try {	
			if(document != null) {	
				return document.getFormSourceCode();
			} else {
				this.document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
			}
		} catch (Exception e) {
			logger.error("Error when getting form source code", e);
		}
		return "";
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

}
