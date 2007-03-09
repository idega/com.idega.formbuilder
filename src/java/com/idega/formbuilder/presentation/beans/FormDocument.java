package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.form.business.FormsService;
import com.idega.block.formadmin.presentation.actions.GetAvailableFormsAction;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PageThankYou;
import com.idega.documentmanager.business.form.PropertiesThankYouPage;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBFormListItem;
import com.idega.formbuilder.presentation.converters.FormDocumentInfo;
import com.idega.formbuilder.presentation.converters.FormPageInfo;
import com.idega.formbuilder.view.ActionManager;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.webface.WFUtil;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private Document document;
	private PropertiesThankYouPage properties;
	
	private String formTitle;
	private String formId;
	private boolean hasPreview;
	private String thankYouTitle;
	private String thankYouText;
	private String tempValue;
	
	private LocalizedStringBean formTitleBean;
	private LocalizedStringBean thankYouTitleBean;
	private LocalizedStringBean thankYouTextBean;
	
	protected FormsService forms_service;
	
	public List<String> getCommonPagesIdList() {
		List<String> result = new LinkedList<String>();
		List<String> ids = document.getContainedPagesIdList();
		String confId = "";
		String tksId = "";
		Page temp = document.getConfirmationPage();
		if(temp != null) {
			confId = temp.getId();
		}
		temp = document.getThxPage();
		if(temp != null) {
			tksId = temp.getId();
		}
		Iterator it = ids.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			if(nextId.equals(confId) || nextId.equals(tksId)) {
				continue;
			}
			result.add(nextId);
		}
		return result;
	}
	
//	public void logFormDocument() {
//		Locale locale = new Locale("en");
//		System.out.println("Document ID: " + formId);
//		System.out.println("Document title: " + formTitle);
//		
//		List<String> pages = document.getContainedPagesIdList();
//		Iterator it = pages.iterator();
//		while(it.hasNext()) {
//			String pageId = (String) it.next();
//			Page page = document.getPage(pageId);
//			if(page != null) {
//				System.out.println("Page ID: " + page.getId());
//				System.out.println("Page Type: " + page.getType());
//				System.out.println("Page title: " + page.getProperties().getLabel().getString(locale));
//				
//				List<String> components = page.getContainedComponentsIdList();
//				Iterator itr = components.iterator();
//				while(itr.hasNext()) {
//					String componentId = (String) itr.next();
//					Component component = page.getComponent(componentId);
//					if(component != null) {
//						System.out.println("Component ID: " + component.getId());
//						System.out.println("Component Type: " + component.getType());
//					}
//				}
//				
//				ButtonArea area = page.getButtonArea();
//				if(area != null) {
//					System.out.println("ButtonArea ID: " + area.getId());
//					System.out.println("ButtonArea Type: " + area.getType());
//					
//					List<String> ids = area.getContainedComponentsIdList();
//					Iterator its = ids.iterator();
//					while(its.hasNext()) {
//						String buttonId = (String) its.next();
//						Button button = (Button) area.getComponent(buttonId);
//						
//						if(button != null) {
//							System.out.println("Button ID: " + button.getId());
//							System.out.println("Button Type: " + button.getType());
//						}
//					}
//				}
//			}
//		}
//	}
	
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
	
	public FormDocumentInfo createFormDocument(String parameter) {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Locale locale = workspace.getLocale();
			DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
			Document document = null;
			String id = getFormsService().generateFormId(parameter);
			LocalizedStringBean formName = new LocalizedStringBean();
			formName.setString(locale, parameter);
			
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
			//setFormId(id);
			//setDocument(document);
			
			loadFormInfo(document);
			
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			if(formPage != null) {
				formPage.loadPageInfo(page);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
		}
		return getFormDocumentInfo();
	}
	
	public String createNewForm() throws Exception {
		String name = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:newTxt");
		if(name == null || name.equals("")) {
			throw new Exception("Form name not provided by the user");
		}
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Locale locale = workspace.getLocale();
			DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
			Document document = null;
			String id = getFormsService().generateFormId(name);
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
			//setFormId(id);
			//setDocument(document);
			
			loadFormInfo(document);
			
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			if(formPage != null) {
				formPage.loadPageInfo(page);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
			return "newFormSuccess";
		}
		return null;
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
				DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				String firstPage = getCommonPagesIdList().get(0);
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
					formPage.loadPageInfo(firstP);
				}
				workspace.setView("design");
				workspace.setRenderedMenu(true);
				workspace.setSelectedMenu("0");
				loadFormInfo(document);
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
				DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
				workspace.setView(Workspace.CODE_VIEW);
				workspace.setRenderedMenu(false);
				
				String firstPage = getCommonPagesIdList().get(0);
				Page firstP = document.getPage(firstPage);
				FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
				if(formPage != null) {
					formPage.clearPageInfo();
					formPage.setPage(firstP);
				}
				loadFormInfo(document);
				FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
				if(formComponent != null) {
					formComponent.clearFormComponentInfo();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "loadCodeSuccess";
	}
	
	protected String retrieveFormIdFormButtonId(String button_id, String button_postfix) {
		
		try {
			String form_id = button_id.substring(button_id.lastIndexOf(":")+1, button_id.indexOf(button_postfix));
			return form_id == null || form_id.equals("") ? null : form_id;
		} catch (Exception e) {
			logger.error("Form id couldn't be parsed from button id: "+button_id, e);
			return null;
		}
	}
	
	public String deleteFormDocument() {
		
		String form_id = retrieveFormIdFormButtonId(getCurrentFormId(FacesContext.getCurrentInstance()), FBFormListItem.delete_button_postfix);
		
		boolean delete_submitted_data = true;
		
		if(form_id == null)
//			TODO: (alex) tell user about error			
			throw new NullPointerException("Form id not found");
		
		try {
			getFormsService().removeForm(form_id, delete_submitted_data);
			
		} catch (Exception e) {
			logger.error("Exception while removing form", e);
//			TODO: (alex) tell user about error
		}
		
		return "redirectHome";
	}
	
	public String loadFormDocumentEntries() {
		String formId = retrieveFormIdFormButtonId(getCurrentFormId(FacesContext.getCurrentInstance()), FBFormListItem.entries_button_postfix);
		
		if(formId != "") {
			
			GetAvailableFormsAction admin = (GetAvailableFormsAction) WFUtil.getBeanInstance("availableFormsAction");
			admin.setSelectedRow(formId);
		}
		return "loadEntriesSuccess";
	}
	
	public String duplicateFormDocument() {
		
		String form_id = retrieveFormIdFormButtonId(getCurrentFormId(FacesContext.getCurrentInstance()), FBFormListItem.duplicate_button_postfix);
		
//		TODO: (alex) display tab with new form name
		
		if(form_id == null)
//			TODO: (alex) tell user about error			
			throw new NullPointerException("Form id not found");
		
		try {
			getFormsService().duplicateForm(form_id, null);
			
		} catch (Exception e) {
			logger.error("Exception while duplicating form", e);
//			TODO: (alex) tell user about error
		}
		
		return "redirectHome";
	}
	
	public void updatePagesList(String idSequence, String idPrefix, String delimiter) throws Exception {
		String confirmId = "";
		String thxId = "";
		Page confPage = document.getConfirmationPage();
		if(confPage != null) {
			confirmId = confPage.getId();
		}
		Page thxPage = document.getThxPage();
		if(thxPage != null) {
			thxId = thxPage.getId();
		}
		List<String> ids = document.getContainedPagesIdList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			String currentId = tokenizer.nextToken();
			if(currentId.endsWith("_P_page")) {
				currentId = currentId.substring(0, currentId.indexOf("_P_page"));
			}
			ids.add(idPrefix + currentId);
		}
		if(!confirmId.equals("")) {
			ids.add(confirmId);
		}
		ids.add(thxId);
		document.rearrangeDocument();
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
	
	public String getViewPreview() {
		if(hasPreview) {
			hasPreview = false;
		} else {
			hasPreview = true;
		}
		return "";
	}
	
	public FormPageInfo togglePreviewPage(boolean value) throws Exception {
		FormPageInfo result = new FormPageInfo();
		hasPreview = value;
		if(hasPreview) {
			Page thxPage = document.getThxPage();
			Page page = null;
			if(thxPage != null) {
				page = document.addConfirmationPage(thxPage.getId());
			} else {
				page = document.addConfirmationPage(null);
			}
			result.setPageTitle(page.getProperties().getLabel().getString(new Locale("en")));
			result.setPageId(page.getId());
		} else {
			Page page = document.getConfirmationPage();
			if(page != null) {
				result.setPageId(page.getId());
				result.setPageTitle(null);
				page.remove();
			} else {
				throw new Exception("Confirmation page does not exist in the document");
			}
		}
		return result;
	}
	
	/*public void changeForm(ActionEvent ae) {
		DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
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
				loadFormInfo(document);
				FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
				if(formComponent != null) {
					formComponent.clearFormComponentInfo();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}*/
	
	public void saveSrc(String source_code) {
		
		if(source_code == null)
			return;
		try {
			if(document != null) {
				
				document.setFormSourceCode(source_code);
				
				FormPage current_page = (FormPage) WFUtil.getBeanInstance("formPage");
				
				if(current_page != null) {
					
					current_page.clearPageInfo();
					
					if(!document.getContainedPagesIdList().isEmpty()) {
					
						current_page.loadPageInfo(document.getPage(document.getContainedPagesIdList().get(0)));
						
					} else
						current_page.setPage(null);
				}
			}
		} catch (Exception e) {
			logger.error("Error when setting form source code", e);
		}
	}
	
	public void loadFormProperties(ActionEvent ae) {
		loadFormInfo(document);
	}
	
	public FormDocumentInfo getFormDocumentInfo() {
		FormDocumentInfo info = new FormDocumentInfo();
		info.setTitle(formTitle);
		info.setHasPreview(hasPreview);
		info.setThankYouTitle(thankYouTitle);
		info.setThankYouText(thankYouText);
		return info;
	}
	
	public void loadFormInfo(Document document) {
		this.document = document;
		formId = document.getId();
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
			try {
				document.setFormTitle(formTitleBean);
			} catch(Exception e) {
				//TODO
				e.printStackTrace();
			}
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
				document = ActionManager.getCurrentInstance().getDocumentManagerInstance().getCurrentDocument();
				return document.getFormSourceCode();
			}
		} catch (Exception e) {
			logger.error("Error when getting form source code", e);
		}
		return "";
	}

	public boolean isHasPreview() {
		return hasPreview;
	}

	public void setHasPreview(boolean hasPreview) {
		this.hasPreview = hasPreview;
		if(hasPreview) {
			document.addConfirmationPage(null);
		} else {
			Page page = document.getConfirmationPage();
			if(page != null) {
				page.remove();
			}
		}
	}

	public String getThankYouText() {
		return thankYouText;
	}

	public void setThankYouText(String thankYouText) {
		this.thankYouText = thankYouText;
		if(thankYouTextBean != null) {
			thankYouTextBean.setString(new Locale("en"), thankYouText);
		}
		if(properties != null) {
			properties.setText(thankYouTextBean);
		}
	}

	public String getThankYouTitle() {
		return thankYouTitle;
	}

	public FormPageInfo setThankYouTitle(String thankYouTitle) {
		this.thankYouTitle = thankYouTitle;
		if(thankYouTitleBean != null) {
			thankYouTitleBean.setString(new Locale("en"), thankYouTitle);
		}
		if(properties != null) {
			properties.setLabel(thankYouTitleBean);
		}
		Page page = document.getThxPage();
		FormPageInfo result = new FormPageInfo();
		if(page != null) {
			result.setPageTitle(thankYouTitle);
			result.setPageId(page.getId());
		}
		return result;
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
	
	protected FormsService getFormsService() {
		
		if (forms_service == null) {
			try {
				IWApplicationContext iwc = IWMainApplication.getDefaultIWApplicationContext();
				forms_service = (FormsService) IBOLookup.getServiceInstance(iwc, FormsService.class);
			}
			catch (IBOLookupException e) {
				logger.error("Could not find FormsService");
			}
		}
		return forms_service;
	}
}