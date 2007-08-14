package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.form.presentation.FormViewer;
import com.idega.block.formadmin.presentation.actions.GetAvailableFormsAction;
import com.idega.content.themes.business.TemplatesLoader;
import com.idega.content.themes.helpers.ThemesHelper;
import com.idega.content.tree.PageTemplate;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.builder.data.ICDomain;
import com.idega.core.builder.data.ICDomainHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.documentmanager.business.FormLockException;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PageThankYou;
import com.idega.documentmanager.business.form.PropertiesDocument;
import com.idega.documentmanager.business.form.PropertiesThankYouPage;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.egov.Application;
import com.idega.formbuilder.business.egov.ApplicationBusiness;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.presentation.converters.FormDocumentInfo;
import com.idega.formbuilder.presentation.converters.FormPageInfo;
import com.idega.formbuilder.view.ActionManager;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.webface.WFUtil;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private PersistenceManager persistence_manager;
	
	private Document document;
	private PropertiesThankYouPage properties;
	private PropertiesDocument documentProperties;
	
	private ApplicationBusiness app_business_bean;
	private String formTitle;
	private String formId;
	private boolean hasPreview;
	private boolean enableBubbles;
	private String thankYouTitle;
	private String thankYouText;
	private String tempValue;
	private String primary_form_name;
	private String app_id;
	
	private LocalizedStringBean formTitleBean;
	private LocalizedStringBean thankYouTitleBean;
	private LocalizedStringBean thankYouTextBean;
	private static final String root_uri = "/pages/";
	private static final String applications_forms_page_uri = "/pages/applications_forms/";
	private static final String applications_forms_page_uri_db = "/applications_forms/";
	private static final String applications_forms_page_name = "applications_forms";
	private static final String egov_form_type = "egovform";
	private static final String form_id_property_name = "formId";
	private static final String region_id_property_name = "regionId";
	private static final String form_template_id_property_key = "fb.form.template.id";
	private static final String form_region_id_property_key = "fb.form.region.id";
	
	public static final String BEAN_ID = "formDocument";
	public static final String APP_FORM_NAME_PARAM = "appform_name";
	public static final String APP_ID_PARAM = "appid";
	public static final String FROM_APP_REQ_PARAM = "fapp";
	
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
	
	public FormDocumentInfo createFormDocument(String parameter) throws Exception {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Locale locale = workspace.getLocale();
			DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
			Document document = null;
			String id = getPersistenceManager().generateFormId(parameter);
			LocalizedStringBean formName = new LocalizedStringBean();
			formName.setString(locale, parameter);
			
			try {
				document = formManagerInstance.createForm(id, formName);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
//			if(getFormId() != null)
//				getFormsService().unlockForm(getFormId());
			
			workspace.setView("design");
			workspace.setDesignViewStatus("empty");
			workspace.setSelectedMenu("0");
			workspace.setRenderedMenu(true);
			
			clearFormDocumentInfo();
			
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
	
//	public String createNewForm() throws Exception {
//		
//		FacesContext ctx = FacesContext.getCurrentInstance();
//		Map req_param_map = ctx.getExternalContext().getRequestParameterMap();
//		String name = (String)req_param_map.get("workspaceform1:newTxt");
//		
//		if(name == null || name.equals(""))
//			name = primary_form_name;
//		
//		if(name == null || name.equals(""))
//			throw new Exception("Form name not provided by the user");
//
//		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
//		
//		if(workspace != null) {
//			Locale locale = workspace.getLocale();
//			DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
//			Document document = null;
//			String id = getPersistenceManager().generateFormId(name);
//			LocalizedStringBean formName = new LocalizedStringBean();
//			formName.setString(locale, name);
//			
//			try {
//				document = formManagerInstance.createForm(id, formName);
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//			
////			if(getFormId() != null)
////				getFormsService().unlockForm(getFormId());
//			
//			workspace.setView("design");
//			workspace.setDesignViewStatus("empty");
//			workspace.setSelectedMenu("0");
//			workspace.setRenderedMenu(true);
//			
//			clearFormDocumentInfo();
//			document.getProperties().setStepsVisualizationUsed(true);
//			loadFormInfo(document);
//			
//			Page page = document.getPage(document.getContainedPagesIdList().get(0));
//			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
//			if(formPage != null) {
//				formPage.loadPageInfo(page);
//			}
//			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
//			if(formComponent != null) {
//				formComponent.clearFormComponentInfo();
//			}
//			return "newFormSuccess";
//		}
//		return null;
//	}
	
	public void save() {
		
		try {
			document.save();
			
			if(app_id != null) {
				FacesContext ctx = FacesContext.getCurrentInstance();
				
				String name = primary_form_name;
				String app_id = this.app_id;
				this.app_id = null;
				primary_form_name = null;
				
				if(app_id == null || name == null) {
					logger.warn("Application id or name was not set when trying to save application form for the first time.");
					return;
				}
				
				IWMainApplication iwma = IWMainApplication.getIWMainApplication(ctx);
				
				Map<String, PageTemplate> p_templates = TemplatesLoader.getInstance(iwma).getPageTemplates();
				PageTemplate egov_form_template = p_templates.get(egov_form_type);
				
				
				if(egov_form_template == null) {
					logger.error("eGov form page was not created for application due to missing eGov form page template");
					return;
				}
				
				BuilderService bservice = getBuilderService();
				
				ICDomain domain = getDomain();
				int domain_id = -1;
				
				if(domain != null)
					domain_id = domain.getID();
				
				String key = bservice.getPageKeyByURI(applications_forms_page_uri);
				
				if(key == null) {
					
					key = bservice.getPageKeyByURI(root_uri);
					
					int created_page_key =
						bservice.createNewPage(
								key, 
								applications_forms_page_name, 
								bservice.getPageKey(),
								null, 		//template id
								applications_forms_page_uri_db,				//uri
								bservice.getTree(IWContext.getIWContext(ctx)), 
								IWContext.getIWContext(ctx), 
								null, //subtype 
								domain_id, 
								bservice.getIBXMLFormat(),
								null 		//source markup
						);
					
					key = String.valueOf(created_page_key); 
				}
				
				String template_id = (String)iwma.getSettings().getProperty(form_template_id_property_key);
				String region_id = (String)iwma.getSettings().getProperty(form_region_id_property_key);
				
				template_id = "".equals(template_id) ? null : template_id;
				region_id = "".equals(region_id) ? null : region_id;
				
				int created_page_key =
					bservice.createNewPage(
							key, 
							name, 
							bservice.getPageKey(),
							template_id,									//template id
							null,											//uri
							bservice.getTree(IWContext.getIWContext(ctx)), 
							IWContext.getIWContext(ctx), 
							egov_form_type,									//subtype 
							domain_id, 
							bservice.getIBXMLFormat(),
							null											//source markup
					);
				
				ThemesHelper helper = ThemesHelper.getInstance();
				
				String webdav_uri_to_page = helper.loadPageToSlide(egov_form_type, egov_form_template.getTemplateFile(), null, created_page_key);
				if (webdav_uri_to_page != null) {
					helper.getThemesService().updatePageWebDav(created_page_key, webdav_uri_to_page);
				}
				
				String page_uri = bservice.getPageURI(created_page_key);
				
				Application app = getAppBusiness().getApplication(Integer.parseInt(app_id));
				
				if(app == null) {
//					TODO: log - something wrong, as it should be created and saved before going here actually
					
				} else {
					app.setUrl(page_uri);
					app.store();
				}
				
				String page_key_str = String.valueOf(created_page_key);
				
				List<String> formviewer_ids = bservice.getModuleId(page_key_str, FormViewer.class.getName());
				
				if(formviewer_ids == null || formviewer_ids.isEmpty()) {
					logger.error("Formviewer not found in the page: "+page_uri);
					return;
				}
				
				String formviewer_id = formviewer_ids.get(0);
				bservice.setProperty(page_key_str, formviewer_id, form_id_property_name, new String[] {document.getId()}, iwma);
				
				if(region_id != null) {
					
					String current_region_id = bservice.getProperty(page_key_str, formviewer_id, region_id_property_name);
					
					if(current_region_id == null || !current_region_id.equals(region_id)) {
						bservice.renameRegion(page_key_str, current_region_id, null, region_id, null);
						bservice.setProperty(page_key_str, formviewer_id, region_id_property_name, new String[] {region_id}, iwma);
					}
				}
				
//				as it is not set when creating page (bug or what?) - add here
				bservice.setTemplateId(page_key_str, template_id);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setAppBusiness(ApplicationBusiness app_business_bean) {
		this.app_business_bean = app_business_bean;
	}
	
	public ApplicationBusiness getAppBusiness() {
		return app_business_bean;
	}
	
	private void clearAppsRelatedMetaData() {
		
		app_id = null;
		primary_form_name = null;
	}
	
	public boolean loadFormDocument(String formId) {
		
		clearAppsRelatedMetaData();
		
		try {
			formId = retrieveFormIdFormButtonId(formId, "_edit");
			if(formId != null && !formId.equals("")) {
				DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				
//				if(getFormId() != null)
//					getFormsService().unlockForm(getFormId());
				
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
		} catch (FormLockException e) {
			// TODO: inform about lock
			logger.info("Form was locked when tried to open it", e);
			return false;
		} catch(Exception e) {
			logger.info("Exception while trying to open a form document", e);
			return false;
		}
		return true;
	}
	
	public boolean loadFormDocumentCode(String formId) {
		
		clearAppsRelatedMetaData();
		
		try {
			formId = retrieveFormIdFormButtonId(formId, "_code");
			if(formId != null && !formId.equals("")) {
				DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				
//				if(getFormId() != null)
//					getFormsService().unlockForm(getFormId());
				
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
				workspace.setView(FBViewPanel.SOURCE_VIEW);
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
		} catch (FormLockException e) {
			// TODO: inform about lock
			logger.info("Form was locked when tried to open it", e);
			return false;
		} catch(Exception e) {
			logger.info("Exception occured when trying to load form code", e);
			return false;
		}
		return true;
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
	
	public boolean deleteFormDocument(String documentId) {
		boolean delete_submitted_data = true;
		
		if(documentId == null)		
			return false;
		try {
			getPersistenceManager().removeForm(documentId, delete_submitted_data);
		} catch (FormLockException e) {
			logger.info("Form was locked when tried to delete it", e);
			return false;
		} catch (Exception e) {
			logger.error("Exception while removing form", e);
			return false;
		}
		return true;
	}
	
	public boolean loadFormDocumentEntries(String formId) {
		formId = retrieveFormIdFormButtonId(formId, "_entries");
		if(formId != null && !formId.equals("")) {
			
			GetAvailableFormsAction admin = (GetAvailableFormsAction) WFUtil.getBeanInstance("availableFormsAction");
			admin.setSelectedRow(formId);
			
			return true;
		}
		return false;
	}
	
	public String duplicateFormDocument(String documentId, String newTitle) {
		if(documentId == null || newTitle == null)
//			TODO: (alex) tell user about error			
			throw new NullPointerException("Form id not found");
		try {
			getPersistenceManager().duplicateForm(documentId, newTitle);
		} catch (Exception e) {
			logger.error("Exception while duplicating form", e);
//			TODO: (alex) tell user about error
		}
		
		return documentId;
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
		enableBubbles = false;
		
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
		enableBubbles = false;
		
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
		info.setEnableSwitcher(enableBubbles);
		return info;
	}
	
	public void loadFormInfo(Document document) {
		this.document = document;
		setFormId(document.getId());
		formTitleBean = document.getFormTitle();
		formTitle = formTitleBean.getString(new Locale("en"));
		if(document.getConfirmationPage() != null) {
			hasPreview = true;
		} else {
			hasPreview = false;
		}
		documentProperties = document.getProperties();
		if(documentProperties != null) {
			enableBubbles = documentProperties.isStepsVisualizationUsed();
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
	
	public String saveFormTitle(String formTitle) {
		setFormTitle(formTitle);
		return formTitle;
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
	
	public PersistenceManager getPersistenceManager() {
		
		return persistence_manager;
	}
	
	public void setPersistenceManager(PersistenceManager persistence_manager) {
		this.persistence_manager = persistence_manager;
	}
	
	protected BuilderService getBuilderService() {
		
		try {
			return BuilderServiceFactory.getBuilderService(IWMainApplication.getDefaultIWApplicationContext());
		} catch (RemoteException e) {
			
			logger.error("Error while retrieving builder service", e);
		}
		return null;
	}
	
	public ICDomain getDomain() {
		ICDomainHome domainHome = null;
		try {
			domainHome = (ICDomainHome) IDOLookup.getHome(ICDomain.class);
		} catch (IDOLookupException e) {
			logger.error(e);
			return null;
		}
		try {
			return domainHome.findFirstDomain();
		} catch (FinderException e) {
			logger.error(e);
			return null;
		}
	}

	public boolean isEnableBubbles() {
		return enableBubbles;
	}

	public void setEnableBubbles(boolean enableBubbles) {
		if(documentProperties != null) {
			this.enableBubbles = enableBubbles;
			documentProperties.setStepsVisualizationUsed(enableBubbles);
		}
	}
	
	public void setPrimaryFormName(String primary_form_name) {
		this.primary_form_name = primary_form_name;
	}
	
	public void setAppId(String app_id) {
		this.app_id = app_id;
	}
}