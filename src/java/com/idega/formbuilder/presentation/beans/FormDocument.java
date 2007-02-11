package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.util.FBUtil;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private Document document;
	
	private String formTitle;
	private String formId;
	private String sourceCode;
	private boolean hasPreview;
	
	private LocalizedStringBean formTitleBean;
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public FormDocument() {
		document = null;
		
		formId = "";
		formTitle = "";
		formTitleBean = null;
		hasPreview = false;
	}
	
	public void clearFormDocumentInfo() {
		this.formId = "";
		this.formTitle = "";
		this.formTitleBean = null;
		this.hasPreview = false;
	}
	
	public String getNewFormDocument() {
		String name = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("newFormT");
		if(name == null || name.equals("")) {
			name = "UNTITLED FORM";
		}
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Locale locale = workspace.getLocale();
			DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
			Document document = null;
			String id = FBUtil.generateFormId(name);
			LocalizedStringBean formName = new LocalizedStringBean();
			formName.setString(locale, name);
			
			try {
				document = formManagerInstance.createForm(id, formName);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			workspace.setView("design");
			workspace.setDesignViewStatus("empty");
			workspace.setSelectedMenu("0");
			workspace.setRenderedMenu(true);
			
			clearFormDocumentInfo();
			setFormTitle(name);
			setFormId(id);
			setDocument(document);
			
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			if(formPage != null) {
				formPage.setPage(page);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
			document.save();
			return null;
		}
		return null;
	}
	
	public String getViewPreview() {
		if(hasPreview) {
			hasPreview = false;
		} else {
			hasPreview = true;
		}
		return "";
	}
	
	public void togglePreviewPage(ActionEvent ae) {
		if(hasPreview) {
			hasPreview = false;
		} else {
			hasPreview = true;
		}
	}
	
	public void changeForm(ActionEvent ae) {
		DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		Locale locale = workspace.getLocale();
		if(formId != null && !formId.equals("") && !formId.equals("INACTIVE")) {
			try {
				Document currentDocument = formManagerInstance.openForm(formId);
				document = currentDocument;
				String firstPage = currentDocument.getContainedPagesIdList().get(0);
				Page firstP = currentDocument.getPage(firstPage);
				if(firstP.getContainedComponentsIdList().size() > 0) {
					workspace.setDesignViewStatus("active");
				} else {
					workspace.setDesignViewStatus("empty");
				}
				FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
				if(formPage != null) {
					formPage.clearPageInfo();
					formPage.setPage(firstP);
				}
				workspace.setView("design");
				workspace.setRenderedMenu(true);
				workspace.setSelectedMenu("0");
				formTitle = currentDocument.getFormTitle().getString(locale);
				FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
				if(formComponent != null) {
					formComponent.clearFormComponentInfo();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void saveSourceCode(ActionEvent ae) {
		if(sourceCode == null)
			return;
		try {
			if(document != null) {
				document.setFormSourceCode(sourceCode);
			}
		} catch (Exception e) {
			logger.error("Error when setting form source code", e);
		}
	}
	
	public void loadFormInfo() {
		document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
		formTitleBean = document.getFormTitle();
		formTitle = formTitleBean.getString(new Locale("en"));
	}
	
	public void loadFormProperties(ActionEvent ae) {
		document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
		formTitleBean = document.getFormTitle();
		formTitle = formTitleBean.getString(new Locale("en"));
		
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("2");
	}
	
	public void saveFormTitle(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("formTitle");
		if(value != null) {
			setFormTitle(value);
			document.setFormTitle(formTitleBean);
		}
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

	public LocalizedStringBean getFormTitleBean() {
		return formTitleBean;
	}

	public void setFormTitleBean(LocalizedStringBean formTitleBean) {
		this.formTitleBean = formTitleBean;
	}

	public String getSourceCode() {
		try {	
			if(document != null) {	
				return document.getFormSourceCode();
			} else {
				document = ActionManager.getDocumentManagerInstance().getCurrentDocument();
				return document.getFormSourceCode();
			}
		} catch (Exception e) {
			logger.error("Error when getting form source code", e);
		}
		return "";
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public boolean isHasPreview() {
		return hasPreview;
	}

	public void setHasPreview(boolean hasPreview) {
		this.hasPreview = hasPreview;
	}

}
