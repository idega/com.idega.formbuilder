package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.formadmin.presentation.actions.GetAvailableFormsAction;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PageThankYou;
import com.idega.formbuilder.business.form.PropertiesThankYouPage;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.converters.FormDocumentInfo;
import com.idega.formbuilder.util.FBUtil;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private Document document;
	private PropertiesThankYouPage properties;
	
	private String formTitle;
	private String formId;
	private String sourceCode;
	private boolean hasPreview;
	private String thankYouTitle;
	private String thankYouText;
	private String tempValue;
	
	private LocalizedStringBean formTitleBean;
	private LocalizedStringBean thankYouTitleBean;
	private LocalizedStringBean thankYouTextBean;
	
	private String getCurrentFormId(FacesContext context) {
		String result = "";
		Map map = context.getExternalContext().getRequestParameterMap();
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			String value = (String) map.get(key);
			if(value.equals("true")) {
				return key;
			}
		}
		return result;
	}
	
	public String createNewForm() {
		String name = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:newTxt");
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
			setFormId(id);
			setDocument(document);
			
			loadFormInfo();
			
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			if(formPage != null) {
				formPage.setPage(page);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
			try {
				document.save();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "newFormSuccess";
		}
		return "";
		
	}
	
	public void save() {
		try {
			document.save();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String loadFormDocument() {
		try {
			String buttonId = getCurrentFormId(FacesContext.getCurrentInstance());
			String formId = buttonId.substring(15, buttonId.indexOf("_edit"));
			if(formId != "") {
				DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				String firstPage = document.getContainedPagesIdList().get(0);
				Page firstP = document.getPage(firstPage);
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
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
				loadFormInfo();
				FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
				if(formComponent != null) {
					formComponent.clearFormComponentInfo();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "loadFormSuccess";
	}
	
	public String loadFormDocumentCode() {
		try {
			String buttonId = getCurrentFormId(FacesContext.getCurrentInstance());
			String formId = buttonId.substring(15, buttonId.indexOf("_code"));
			if(formId != "") {
				DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
				workspace.setView(Workspace.CODE_VIEW);
				workspace.setRenderedMenu(false);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "loadCodeSuccess";
	}
	
	public void deleteFormDocument() {
		
	}
	
	public String loadFormDocumentEntries() {
		String buttonId = getCurrentFormId(FacesContext.getCurrentInstance());
		String formId = buttonId.substring(15, buttonId.indexOf("_entries"));
		if(formId != "") {
			
			GetAvailableFormsAction admin = (GetAvailableFormsAction) WFUtil.getBeanInstance("availableFormsAction");
			admin.setSelectedRow(formId);
		}
		return "loadEntriesSuccess";
	}
	
	public void duplicateFormDocument() {
		
	}
	
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
		
		thankYouTitle = "";
		thankYouTextBean = null;
		thankYouText = "";
		thankYouTextBean = null;
	}
	
	public void clearFormDocumentInfo() {
		formId = "";
		formTitle = "";
		formTitleBean = null;
		hasPreview = false;
		
		thankYouTitle = "";
		thankYouTextBean = null;
		thankYouText = "";
		thankYouTextBean = null;
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
			setFormId(id);
			setDocument(document);
			
			loadFormInfo();
			
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			if(formPage != null) {
				formPage.setPage(page);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
			try {
				document.save();
			} catch (Exception e) {
				// TODO: handle exception
			}
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
			Page page = document.getConfirmationPage();
			if(page != null) {
				page.remove();
				try {
					document.save();
				} catch (Exception e) {
					// TODO: handle exception
				}
				hasPreview = false;
			}
		} else {
			document.addConfirmationPage(null);
			try {
				document.save();
			} catch (Exception e) {
				// TODO: handle exception
			}
			hasPreview = true;
		}
	}
	
	public void changeForm(ActionEvent ae) {
		DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(formId != null && !formId.equals("") && !formId.equals("INACTIVE")) {
			try {
				document = formManagerInstance.openForm(formId);
				String firstPage = document.getContainedPagesIdList().get(0);
				Page firstP = document.getPage(firstPage);
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
				loadFormInfo();
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
	
	public void loadFormProperties(ActionEvent ae) {
		loadFormInfo();
	}
	
	public FormDocumentInfo getFormDocumentInfo() {
		FormDocumentInfo info = new FormDocumentInfo();
		info.setTitle(formTitle);
		info.setHasPreview(hasPreview);
		info.setThankYouTitle(thankYouTitle);
		info.setThankYouText(thankYouText);
		return info;
	}
	
	public void loadFormInfo() {
		formTitleBean = document.getFormTitle();
		formTitle = formTitleBean.getString(new Locale("en"));
		if(document.getConfirmationPage() != null) {
			hasPreview = true;
		} else {
			hasPreview = false;
		}
		PageThankYou thxPage = document.getThxPage();
		if(thxPage != null) {
			properties = thxPage.getProperties();
			if(properties != null) {
				Locale locale = new Locale("en");
				thankYouTitleBean = properties.getLabel();
				thankYouTitle = thankYouTitleBean.getString(locale);
				
				thankYouTextBean = properties.getText();
				thankYouText = thankYouTextBean.getString(locale);
			}
		}
	}
	
	public void saveFormTitle(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("formTitle");
		if(value != null) {
			setFormTitle(value);
			document.setFormTitle(formTitleBean);
		}
	}
	
	public void saveThankYouLabel(ActionEvent ae) {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("thankYouTitle");
		if(value != null) {
			setThankYouTitle(value);
		}
	}
	
	public void saveThankYouText(ActionEvent ae) {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("thankYouText");
		if(value != null) {
			setThankYouText(value);
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

	public String getThankYouText() {
		return thankYouText;
	}

	public void setThankYouText(String thankYouText) {
		this.thankYouText = thankYouText;
		thankYouTextBean.setString(new Locale("en"), thankYouText);
		if(properties != null) {
			properties.setText(thankYouTextBean);
		}
	}

	public String getThankYouTitle() {
		return thankYouTitle;
	}

	public void setThankYouTitle(String thankYouTitle) {
		this.thankYouTitle = thankYouTitle;
		thankYouTitleBean.setString(new Locale("en"), thankYouTitle);
		if(properties != null) {
			properties.setLabel(thankYouTitleBean);
		}
	}

	public PropertiesThankYouPage getProperties() {
		return properties;
	}

	public void setProperties(PropertiesThankYouPage properties) {
		this.properties = properties;
	}

	public LocalizedStringBean getThankYouTextBean() {
		return thankYouTextBean;
	}

	public void setThankYouTextBean(LocalizedStringBean thankYouTextBean) {
		this.thankYouTextBean = thankYouTextBean;
	}

	public LocalizedStringBean getThankYouTitleBean() {
		return thankYouTitleBean;
	}

	public void setThankYouTitleBean(LocalizedStringBean thankYouTitleBean) {
		this.thankYouTitleBean = thankYouTitleBean;
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
	}

}
