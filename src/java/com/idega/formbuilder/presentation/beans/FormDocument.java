package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.form.presentation.FormViewer;
import com.idega.block.web2.business.Web2Business;
import com.idega.bpm.xformsview.XFormsView;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.content.themes.business.TemplatesLoader;
import com.idega.content.themes.helpers.business.ThemesHelper;
import com.idega.content.tree.PageTemplate;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.core.builder.data.ICDomain;
import com.idega.core.builder.data.ICDomainHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.formbuilder.business.egov.Application;
import com.idega.formbuilder.business.egov.ApplicationBusiness;
import com.idega.formbuilder.business.process.XFormsProcessManager;
import com.idega.formbuilder.presentation.beans.FBHomePageBean.ProcessAllTasksForms;
import com.idega.formbuilder.presentation.components.FBFormPage;
import com.idega.formbuilder.presentation.components.FBHomePage;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.util.FBUtil;
import com.idega.idegaweb.IWMainApplication;
import com.idega.jbpm.view.View;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.Document;
import com.idega.xformsmanager.business.DocumentManager;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

public class FormDocument implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	private static final Log logger = LogFactory.getLog(FormDocument.class);
	
	private InstanceManager instanceManager;
	private XFormsProcessManager xformsProcessManager;
	private ProcessData processData;
	
	private String formId;
	private boolean hasPreview;
	private boolean enableBubbles;
	private Document document;
	private List<AdvancedProperty> standaloneForms = new ArrayList<AdvancedProperty>();
	
	private Workspace workspace;
	private ApplicationBusiness app_business_bean;
	private String tempValue;
	private String primary_form_name;
	private String app_id;
	
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
	public static final String FORM_ID_OPEN_TAG = "<form_id>";
	public static final String FORM_ID_CLOSE_TAG = "</form_id>";
	
	public List<String> getCommonPagesIdList() {
		List<String> result = new LinkedList<String>();
		List<String> allIds = document.getContainedPagesIdList();
		List<Page> specialPages = document.getSpecialPages();
		
		result.addAll(allIds);
		for(Page page : specialPages) {
			if(result.contains(page.getId())) {
				result.remove(page.getId());
			}
		}
		return result;
	}
	
	public Document initializeBeanInstance(Long formId) throws Exception {
		DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
		this.document = formManagerInstance.openForm(formId);
		this.formId = document.getId();
//		this.hasPreview = overviewPage != null ? true : false;
		this.hasPreview = false;
		return document;
	}
	
	public boolean createTaskFormDocument(String parameter, String processId, String taskName) throws Exception {
		Locale locale = workspace.getLocale();
		
		DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
		
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(locale, parameter);
			
		try {
			document = formManagerInstance.createForm(formName, null);
			document.save();
		} catch(Exception e) {
			logger.error("Could not create XForms document");
		}
			
//		if(getFormId() != null)
//			getFormsService().unlockForm(getFormId());
			
		workspace.setView(FBViewPanel.DESIGN_VIEW);
		
		initializeBeanInstance(getDocument());
		processData.initializeBeanInstance(getDocument(), new Long(processId), taskName);
		xformsProcessManager.assignTaskForm(new Long(processId).toString(), taskName, formId);
			
		Page page = getDocument().getPage(getDocument().getContainedPagesIdList().get(0));
		FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
		formPage.initializeBeanInstance(page);
		
		return true;
	}
	
//	TODO: fix and move away from here
	public boolean attachFormDocumentToTask(String processId, String taskName, Long formId, boolean gotoDesigner) {
		
		if(processId == null || taskName == null || formId == null)
			return false;
		
		clearAppsRelatedMetaData();
		
		try {
			if(gotoDesigner) {
				DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
				setDocument(formManagerInstance.openForm(formId));
//				if(getFormId() != null)
//					getFormsService().unlockForm(getFormId());
					
				String firstPage = getCommonPagesIdList().get(0);
				Page firstP = getDocument().getPage(firstPage);
				FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
				formPage.initializeBeanInstance(firstP);
					
				getWorkspace().setView(FBViewPanel.DESIGN_VIEW);
				getWorkspace().setProcessMode(true);
				initializeBeanInstance(getDocument());
				getProcessData().initializeBeanInstance(getDocument(), new Long(processId), taskName);
			}
			
			View view = new XFormsView();
			view.setViewId(String.valueOf(formId));
//			getViewToTaskBinder().bind(view, getJbpmProcessBusiness().getProcessTask(Long.valueOf(processId), taskName));
			
		} catch(Exception e) {
			logger.info("Exception while trying to open a form document", e);
			return false;
		}
		return true;
	}
	
	public boolean loadTaskFormDocument(String processId, String taskName, Long formId) {
		
		if(processId == null || taskName == null || formId == null)
			return false;
		
		clearAppsRelatedMetaData();
		
		try {
			DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
			setDocument(formManagerInstance.openForm(formId));
//			if(getFormId() != null)
//				getFormsService().unlockForm(getFormId());
				
			String firstPage = getCommonPagesIdList().get(0);
			Page firstP = getDocument().getPage(firstPage);
			FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
			formPage.initializeBeanInstance(firstP);
				
			getWorkspace().setView(FBViewPanel.DESIGN_VIEW);
			getWorkspace().setParentFormId(formId);
			getWorkspace().setProcessId(new Long(processId));
			getWorkspace().setTaskName(taskName);
			initializeBeanInstance(getDocument());
			getProcessData().initializeBeanInstance(getDocument(), new Long(processId), taskName);
		} catch(Exception e) {
			logger.info("Exception while trying to open a form document", e);
			return false;
		}
		return true;
	}
	
	public boolean changeTaskFormDocumentVersion(Long formId) {
		
		if(formId == null)
			return false;
		
		clearAppsRelatedMetaData();
		
		try {
			DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
			setDocument(formManagerInstance.openForm(formId));

			String firstPage = getCommonPagesIdList().get(0);
			Page firstP = getDocument().getPage(firstPage);
			FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
			formPage.initializeBeanInstance(firstP);
				
			getWorkspace().setView(FBViewPanel.DESIGN_VIEW);
			initializeBeanInstance(getDocument());
			getProcessData().initializeBeanInstance(getDocument(), getWorkspace().getProcessId(), getWorkspace().getTaskName());
		} catch(Exception e) {
			logger.info("Exception while trying to open a form document", e);
			return false;
		}
		return true;
	}
		
	public boolean createFormDocument(String parameter) throws Exception {
		Locale locale = workspace.getLocale();
		
		DocumentManager formManagerInstance = InstanceManager.getCurrentInstance().getDocumentManagerInstance();
		
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(locale, parameter);
			
		try {
			document = formManagerInstance.createForm(formName, null);
			document.save();
		} catch(Exception e) {
			logger.error("Could not create XForms document", e);
			return false;
		}
			
//		if(getFormId() != null)
//			getFormsService().unlockForm(getFormId());
			
		workspace.setView(FBViewPanel.DESIGN_VIEW);
		
		initializeBeanInstance(document);
			
		Page page = document.getPage(document.getContainedPagesIdList().get(0));
		FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
		formPage.initializeBeanInstance(page);
		
		return true;
	}
	
	public void save() {
		
		try {
			document.save();
			
//			TODO: this need to be moved under the bean implementing ApplicationType interface
			if(app_id != null && false) {
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
				
				String template_id = iwma.getSettings().getProperty(form_template_id_property_key);
				String region_id = iwma.getSettings().getProperty(form_region_id_property_key);
				
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
	
	protected void clearAppsRelatedMetaData() {
		
		app_id = null;
		primary_form_name = null;
	}
	
	public boolean loadFormDocument(Long formId) {
		
		clearAppsRelatedMetaData();
		
		try {
			if(formId != null) {
				DocumentManager formManagerInstance = InstanceManager.getCurrentInstance().getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				
				String firstPage = getCommonPagesIdList().get(0);
				Page firstP = document.getPage(firstPage);
				FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
				formPage.initializeBeanInstance(firstP);
				
				getWorkspace().setView(FBViewPanel.DESIGN_VIEW);
				initializeBeanInstance(document);
			}
		} catch(Exception e) {
			logger.info("Exception while trying to open a form document", e);
			return false;
		}
		return true;
	}
	
	public boolean loadFormDocumentCode(Long formId) {
		
		clearAppsRelatedMetaData();
		
		try {
			if(formId != null) {
				DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				
				workspace.setView(FBViewPanel.SOURCE_VIEW);
				
				String firstPage = getCommonPagesIdList().get(0);
				Page firstP = document.getPage(firstPage);
				FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
				formPage.initializeBeanInstance(firstP);
				
				initializeBeanInstance(document);
			}
		} catch(Exception e) {
			logger.info("Exception occured when trying to load form code", e);
			return false;
		}
		return true;
	}
	
	public boolean loadFormDocumentPreview(Long formId) {
		
		clearAppsRelatedMetaData();
		
		try {
			if(formId != null) {
				DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
				document = formManagerInstance.openForm(formId);
				
				workspace.setView(FBViewPanel.PREVIEW_VIEW);
				
				String firstPage = getCommonPagesIdList().get(0);
				Page firstP = document.getPage(firstPage);
				FormPage formPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
				formPage.initializeBeanInstance(firstP);
				
				initializeBeanInstance(document);
			}
		} catch(Exception e) {
			logger.info("Exception occured when trying to load form code", e);
			return false;
		}
		return true;
	}
	
	public boolean deleteFormDocument(Long formId) {
//		boolean delete_submitted_data = true;
		
		if(formId == null || true)		
			return false;
		
//		TODO: implement in persistence manager 
		/*
		try {
			getPersistenceManager().removeForm(formId, delete_submitted_data);
		} catch (FormLockException e) {
			logger.info("Form was locked when tried to delete it", e);
			return false;
		} catch (Exception e) {
			logger.error("Exception while removing form", e);
			return false;
		}
		*?
		return true;
	}
	
	public boolean deleteTaskFormDocument(Long formId) {
//		boolean delete_submitted_data = true;
		
		if(formId == null || true)		
			return false;
		
		/*
		try {
			getPersistenceManager().removeForm(documentId, delete_submitted_data);
			viewToTaskBinder.unbind(documentId);
		} catch (FormLockException e) {
			logger.info("Form was locked when tried to delete it", e);
			return false;
		} catch (Exception e) {
			logger.error("Exception while removing form", e);
			return false;
		}
		*/
		return true;
	}
	
	public String loadFormDocumentEntries(Long formId) {
		
		if(formId != null) {
			
			DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
			document = formManagerInstance.openForm(formId);
			
			return document == null ? null : document.getId();
		}
		return null;
	}
	
	public String duplicateFormDocument(String documentId, String newTitle) {
		if(documentId == null || newTitle == null)
//			TODO: (alex) tell user about error			
			throw new NullPointerException("Form id not found");
		
		/* TODO: implement
		try {
			getPersistenceManager().duplicateForm(documentId, newTitle);
		} catch (Exception e) {
			logger.error("Exception while duplicating form", e);
//			TODO: (alex) tell user about error
		}
		*/
		
		return documentId;
	}
	
	public void updatePagesList(List<String> idSequence) throws Exception {
		String confirmId = "";
		String thxId = "";
		Page confPage = null;//document.getConfirmationPage();
		if(confPage != null) {
			confirmId = confPage.getId();
		}
		Page thxPage = null;//document.getThxPage();
		if(thxPage != null) {
			thxId = thxPage.getId();
		}
		List<String> ids = document.getContainedPagesIdList();
		ids.clear();
		for(Iterator<String> it = idSequence.iterator(); it.hasNext(); ) {
			String currentId = it.next();
			if(currentId.endsWith("_P_page")) {
				currentId = currentId.substring(0, currentId.indexOf("_P_page"));
			}
			ids.add(currentId);
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

	public String getViewPreview() {
		if(hasPreview) {
			hasPreview = false;
		} else {
			hasPreview = true;
		}
		return "";
	}

//	TODO: this is temporary. remove.
	public static final String webdavSubmissionAction = "webdav:/files/forms/submissions/";
	public static final String processSubmissionAction = "process:/files/forms/submissions/";
	
	public void toggleProcessTask(boolean value) {

//		TODO: temporary. replace this method. should be possibility to choose from the actions list, 
//		or retrieve default for each use case. e.g. for simple use case, use webdav submission action,
//		for process, use jbpm ws
		if(value)
			document.getProperties().setSubmissionAction(processSubmissionAction);
		else
			document.getProperties().setSubmissionAction(webdavSubmissionAction);
	}
	
	public org.jdom.Document togglePreviewPage(boolean value) throws Exception {
		hasPreview = value;
		Page page = null;
		if(hasPreview) {
			Page thxPage = null;//document.getThxPage();
			if(thxPage != null) {
				page = document.addConfirmationPage();
			} else {
				page = document.addConfirmationPage();
			}
//			setOverviewPage(page);
		} else {
			page = null;//document.getConfirmationPage();
			if(page != null) {
//				setOverviewPage(null);
				page.remove();
			}
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormPage(page, "formPageIcon"), true);
	}
	
	public void saveSrc(String sourceCode) {
		if (StringUtil.isEmpty(sourceCode))
			return;
		
		try {
			if(document != null) {
				String formIdSearchRegex = FORM_ID_OPEN_TAG + CoreConstants.DOT + CoreConstants.PLUS + FORM_ID_CLOSE_TAG;
				String formIdReplacement = FORM_ID_OPEN_TAG + getFormId() + FORM_ID_CLOSE_TAG;
				sourceCode = sourceCode.replaceAll(formIdSearchRegex, formIdReplacement);
				
				document.setFormSourceCode(sourceCode);
				FormPage currentPage = (FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID);
				if(currentPage != null) {
					Page page = document.getPage(document.getContainedPagesIdList().get(0));
					currentPage.initializeBeanInstance(page);
				}
			}
			
		} catch (Exception e) {
			logger.error("Error when setting form source code", e);
		}
	}
	
	public Document initializeBeanInstance(Document document) {
		this.document = document;
		this.formId = document.getId();
		this.hasPreview = false;
		
		return document;
	}
	
	public String getFormId() {
		return formId;
	}
	
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	public String getFormTitle() {
		if(document == null) {
			return CoreConstants.EMPTY;
		}
		return FBUtil.getPropertyString(document.getFormTitle().getString(FBUtil.getUILocale()));
	}
	
	public void setFormTitle(String formTitle) throws Exception {
		LocalizedStringBean bean = document.getFormTitle();
		bean.setString(FBUtil.getUILocale(), formTitle);
		document.setFormTitle(bean);
	}
	
	public void setFormErrorMessage(String message) throws Exception {
		LocalizedStringBean bean = document.getFormErrorMsg();
		bean.setString(FBUtil.getUILocale(), message);
		document.setFormErrorMsg(bean);
	}
	
	public String saveFormErrorMessage(String message) {
		try {
			setFormErrorMessage(message);
			return message;
		} catch(Exception e) {
			logger.error("Could not save form title: ", e);
			return null;
		}
	}
	
	public String saveFormTitle(String formTitle) {
		try {
			setFormTitle(formTitle);
			return formTitle;
		} catch(Exception e) {
			logger.error("Could not save form title: ", e);
			return null;
		}
	}
	
	public String getOnlySourceCode() {
		try {	
			return document.getFormSourceCode();
		} catch (Exception e) {
			logger.error("Error when getting form source code", e);
			return null;
		}
	}

	public List<String> getSourceCode() {
		List<String> results = new ArrayList<String>(4);
		
		results.add(getOnlySourceCode());

		Web2Business web2 = ELUtil.getInstance().getBean(Web2Business.SPRING_BEAN_IDENTIFIER);
		results.add(web2.getBundleURIToCodeMirrorScriptFile());
		results.add(web2.getBundleURIToCodeMirrorStyleFile("xmlcolors.css"));
		results.add(web2.getBundleURIToCodeMirrorFolder());
		
		return results;
	}

	public boolean isHasPreview() {
		return hasPreview;
	}

	public void setProcessForm(boolean procForm) {
		
		document.getProperties().setSubmissionAction(processSubmissionAction);
	}
	
	public ProcessTasksBean getProcessTasks(String processName, Integer version) {
		
		FBHomePageBean bean = (FBHomePageBean) WFUtil.getBeanInstance(FBHomePageBean.beanIdentifier);
		
		IWContext iwc = IWContext.getCurrentInstance();
		
		ProcessAllTasksForms forms = bean.getTasksFormsForProcess(iwc.getCurrentLocale(), processName, version);

		org.jdom.Document renderedComponent = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), FBHomePage.getTaskList(forms), true);
		
		ProcessTasksBean result = new ProcessTasksBean();
		result.setDocument(renderedComponent);
		result.setTaskCount(forms.getTasksCount());
		result.setTaskFormCount(forms.getTaskForms().size());
		return result ; 
		
	}
	
	public boolean isProcessForm() {

		return processSubmissionAction.equals(document.getProperties().getSubmissionAction());
	}

	public void setHasPreview(boolean hasPreview) {
		this.hasPreview = hasPreview;
		if(hasPreview) {
			document.addConfirmationPage();
		} else {
			Page page = null;//document.getConfirmationPage();
			if(page != null) {
				page.remove();
			}
		}
	}

	public String getThankYouText() {
		
		return CoreConstants.EMPTY;
//		if(submitPage == null) {
//			return CoreConstants.EMPTY;
//		}
//		return FBUtil.getPropertyString(submitPage.getProperties().getText().getString(FBUtil.getUILocale()));
	}
	
	public String getFormErrorMessage() {
		if(document == null || document.getFormErrorMsg() == null) {
			return CoreConstants.EMPTY;
		}
		return FBUtil.getPropertyString(document.getFormErrorMsg().getString(FBUtil.getUILocale()));
	}

	public void setThankYouText(String thankYouText) {
//		LocalizedStringBean bean = submitPage.getProperties().getText();
//		bean.setString(FBUtil.getUILocale(), thankYouText);
//		submitPage.getProperties().setText(bean);
	}

	public String getThankYouTitle() {
		return CoreConstants.EMPTY;
//		return submitPage.getProperties().getLabel().getString(FBUtil.getUILocale());
	}

	public void setThankYouTitle(String thankYouTitle) {
//		LocalizedStringBean bean = submitPage.getProperties().getLabel();
//		bean.setString(FBUtil.getUILocale(), thankYouTitle);
//		submitPage.getProperties().setLabel(bean);
	}

	public String getTempValue() {
		return tempValue;
	}

	public void setTempValue(String tempValue) {
		this.tempValue = tempValue;
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

	public void setPrimaryFormName(String primary_form_name) {
		this.primary_form_name = primary_form_name;
	}
	
	public void setAppId(String app_id) {
		this.app_id = app_id;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public InstanceManager getInstanceManager() {
		return instanceManager;
	}

	public void setInstanceManager(InstanceManager instanceManager) {
		this.instanceManager = instanceManager;
	}

	public XFormsProcessManager getXformsProcessManager() {
		return xformsProcessManager;
	}

	public void setXformsProcessManager(XFormsProcessManager xformsProcessManager) {
		this.xformsProcessManager = xformsProcessManager;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public void setProcessData(ProcessData processData) {
		this.processData = processData;
	}

	public List<AdvancedProperty> getStandaloneForms() {
		return standaloneForms;
	}

	public void setStandaloneForms(List<AdvancedProperty> standaloneForms) {
		this.standaloneForms = standaloneForms;
	}
}