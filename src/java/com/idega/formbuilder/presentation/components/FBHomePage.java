package com.idega.formbuilder.presentation.components;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.xformsmanager.business.Form;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FBHomePageBean;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FBHomePageBean.ProcessAllTasksForms;
import com.idega.formbuilder.presentation.beans.FBHomePageBean.TaskForm;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.PresentationUtil;
import com.idega.webface.WFUtil;

public class FBHomePage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "HomePage";
	
	private static final String CONTAINER_DIV_ID = "fbHomePage";
	private static final String CONTAINER_HEADER_ID = "fbHomePageHeaderBlock";
	private static final String HEADER_LEFT = "fbHPLeft";
	private static final String HEADER_NAME = "headerName";
	private static final String HEADER_SLOGAN = "headerSlogan";
	private static final String FORM_LIST_CONTAINER = "formListContainer";
	private static final String PROCESS_ITEM_CLASS = "processItem";
	private static final String PROCESS_BUTTON_CLASS = "processButton";
	private static final String CASES_BUTTON_CLASS = "casesButton";
	private static final String TASK_FORM_BUTTON_CLASS = "taskFormButton";
	private static final String FORM_LIST_ICON_CLASS = "formListIcon";
	private static final String FORM_LIST_ITEM_CLASS = "formListItem";
	private static final String FORM_TITLE_CLASS = "formTitle";
	private static final String CREATED_DATE_CLASS = "createdDate";
	private static final String TASK_FORM_LIST_CLASS = "taskFormList";
	private static final String TASK_FORM_ITEM_CLASS = "formListItemSub";
	private static final String PROCESS_NAME_CLASS = "processName";
	private static final String BUTTON_LIST_CLASS = "buttonList";
	private static final String ENTRIES_BUTTON_CLASS = "entriesButton";
	private static final String EDIT_BUTTON_CLASS = "editButton";
	private static final String TRY_BUTTON_CLASS = "tryButton";
	private static final String CODE_BUTTON_CLASS = "codeButton";
//	private static final String DELETE_BUTTON_CLASS = "deleteButton";
//	private static final String DELETE_TF_BUTTON_CLASS = "deleteTFButton";
	private static final String web2BeanIdentifier = "web2bean";
	private static final String MOOTABS_TITLE_CLASS = "mootabs_title";
	private static final String TITLE_ATTRIBUTE = "title";
//	private static final String REL_ATTRIBUTE = "rel";
//	private static final String HREF_ATTRIBUTE = "href";
	private static final String PROCESS_TAB_TITLE = "processes";
	private static final String STANDALONE_TAB_TITLE = "standalone";
	private static final String MOOTABS_PANEL_CLASS = "mootabs_panel";
	private static final String PROCESS_BUTTON_LIST_CLASS = "processButtonList";
	private static final String TAB_TITLE_CLASS = "tabTitle";
//	private static final String PROC_BTN_CLASS = "procBtnClass";
	private static final String TRANSITION_BTN_CLASS = "transitionButton";
	private static final String EXPAND_BTN_CLASS = "expandButton";
//	private static final String ATTACH_BTN_CLASS = "attachButton";
//	private static final String CREATE_BTN_CLASS = "createButton";
	private static final String STATEFULL_TAB_CLASS = "stateFullTab";
	private static final String TASK_TEXT = "    Task: ";
	
	private static final String PROCESS_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/orbz-machine-32x32.png";
	private static final String STANDALONE_FORM_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/fields_32.png";
	private static final String FORMSHOME_JS = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formshome.js";
	private static final String FORMSHOME_CSS = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formshome.css";
	
	private static final String formIdAtt = "formId";
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		
		Web2Business web2 = (Web2Business) getBeanInstance(web2BeanIdentifier);
		try {
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, web2.getBundleURIToMootoolsLib());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, web2.getBundleUriToSmoothboxScript());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, web2.getBundleUriToMootabsScript());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, web2.getBundleURIToJQueryLib());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, web2.getBundleUriToHumanizedMessagesScript());
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, CoreConstants.DWR_ENGINE_SCRIPT);
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/Workspace.js");
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, "/dwr/interface/FormDocument.js");
			PresentationUtil.addJavaScriptSourceLineToHeader(iwc, FORMSHOME_JS);
			
			PresentationUtil.addStyleSheetToHeader(iwc, web2.getBundleUriToSmoothboxStylesheet());
			PresentationUtil.addStyleSheetToHeader(iwc, web2.getBundleUriToMootabsStyle());
			PresentationUtil.addStyleSheetToHeader(iwc, web2.getBundleUriToHumanizedMessagesStyleSheet());
			PresentationUtil.addStyleSheetToHeader(iwc, FORMSHOME_CSS);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		Layer fbHomePage = new Layer(Layer.DIV);
		fbHomePage.setId(CONTAINER_DIV_ID);
		
		Layer header = new Layer(Layer.DIV);
		header.setId(CONTAINER_HEADER_ID);
		
		Layer headerPartLeft = new Layer(Layer.DIV);
		headerPartLeft.setId(HEADER_LEFT);
		
		Text name = new Text(getLocalizedString(iwc, "fb_home_top_title", "Formbuilder"));
		name.setId(HEADER_NAME);
		Text slogan = new Text(getLocalizedString(iwc, "fb_home_top_slogan", "The easy way to build your forms"));
		slogan.setId(HEADER_SLOGAN);
		
		headerPartLeft.add(name);
		headerPartLeft.add(slogan);
		header.add(headerPartLeft);
		fbHomePage.add(header);
		
		Layer listContainer = new Layer(Layer.DIV);
		listContainer.setId(FORM_LIST_CONTAINER);
		
		Lists tabsList = new Lists();
		tabsList.setStyleClass(MOOTABS_TITLE_CLASS);
		
		tabsList.add(addTab(iwc, STANDALONE_TAB_TITLE, STANDALONE_FORM_ICON, getLocalizedString(iwc, "fb_home_proc_alone", "Standalone")));
		tabsList.add(addTab(iwc, PROCESS_TAB_TITLE, PROCESS_ICON, getLocalizedString(iwc, "fb_home_proc_tab", "Processes")));
		
		listContainer.add(tabsList);
		
		Layer tab1Forms = new Layer(Layer.DIV);
		tab1Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab1Forms.setId(PROCESS_TAB_TITLE);
		
		displayProcessTasks(iwc, tab1Forms, iwc.getCurrentLocale());
		
		listContainer.add(tab1Forms);
		
		Layer tab2Forms = new Layer(Layer.DIV);
		tab2Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab2Forms.setId(STANDALONE_TAB_TITLE);
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		formDocument.getStandaloneForms().clear();
		
		FBHomePageBean homePageBean = (FBHomePageBean) WFUtil.getBeanInstance(FBHomePageBean.beanIdentifier);
		List<Form> forms = homePageBean.getStandaloneForms();
		
		Locale locale = iwc.getCurrentLocale();
		
		for (Form persistedForm : forms) {

			String dateCreatedStr = new IWTimestamp(persistedForm.getDateCreated()).getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT);
			formDocument.getStandaloneForms().add(new AdvancedProperty(persistedForm.getFormId().toString(), persistedForm.getDisplayName()));
			tab2Forms.add(getListItem(iwc, persistedForm.getDisplayName(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + dateCreatedStr, persistedForm.getFormId().toString()));
		}
		
		listContainer.add(tab2Forms);
		
		fbHomePage.add(listContainer);
		
		add(fbHomePage);
	}
	
	protected void displayProcessTasks(IWContext iwc, Layer container, Locale locale) {
		
		FBHomePageBean homePageBean = (FBHomePageBean) WFUtil.getBeanInstance(FBHomePageBean.beanIdentifier);
		List<ProcessAllTasksForms> allForms = homePageBean.getAllTasksForms(iwc, locale);
		
		for (ProcessAllTasksForms processForms : allForms) {
			
			Layer processItem = new Layer(Layer.DIV);
			processItem.setStyleClass(PROCESS_ITEM_CLASS);
			
			Lists topList = new Lists();
			topList.setStyleClass(BUTTON_LIST_CLASS);
			topList.setStyleClass(PROCESS_BUTTON_LIST_CLASS);
			
//			topList.add(addProcessButton(iwc, CASES_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_view_cases_link", "View cases")));
//			topList.add(addProcessButton(iwc, DELETE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_delete_process_link", "Delete")));
			
			ListItem item2 = new ListItem();
			Link expandButton = new Link(getLocalizedString(iwc, "fb_home_expand_process_link", "Expand"));
			expandButton.setStyleClass(TRANSITION_BTN_CLASS);
			expandButton.setStyleClass(EXPAND_BTN_CLASS);
			item2.add(expandButton);
			topList.add(item2);
			
			Image processIcon = new Image();
			processIcon.setSrc(PROCESS_ICON);
			processIcon.setStyleClass(FORM_LIST_ICON_CLASS);
			
			Text processName = new Text(processForms.getProcessName());
			processName.setStyleClass(FORM_TITLE_CLASS);
			
			Text created = new Text(CoreConstants.EMPTY);
			created.setStyleClass(CREATED_DATE_CLASS);
			
			Lists list = new Lists();
			list.setStyleClass(TASK_FORM_LIST_CLASS);
			list.setStyleAttribute("display", "none");
			
			for (TaskForm taskForm : processForms.getTaskForms()) {

				list.add(getProcessTaskFormItem(iwc, processForms.getProcessName(), processForms.getProcessId(), taskForm.getFormName(), taskForm.getTaskName(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + taskForm.getDateCreatedStr(), taskForm.getFormId()));
			}
			
			processItem.add(processIcon);
			processItem.add(processName);
			Layer summaryText = new Layer(Layer.SPAN);
			summaryText.setStyleClass("processSummaryText");
			summaryText.add(new Text(getLocalizedString(iwc, "fb_home_total_tasks", "Total tasks: ") + processForms.getTasksCount() + CoreConstants.SPACE));
			summaryText.add(new Text(getLocalizedString(iwc, "fb_home_assigned_forms", "Assigned forms: ") + processForms.getTaskForms().size()));
			processItem.add(summaryText);
			processItem.add(created);
			processItem.add(topList);

			processItem.add(list);
			container.add(processItem);
		}
	}
	
	private ListItem addTab(IWContext iwc, String tabTitleParameter, String tabIconSrc, String tabTitle) {
		ListItem tab1 = new ListItem();
		tab1.setMarkupAttribute(TITLE_ATTRIBUTE, tabTitleParameter);
		tab1.setStyleClass(STATEFULL_TAB_CLASS);
		Image tabIcon = new Image();
		tabIcon.setSrc(tabIconSrc);
		tab1.add(tabIcon);
		Text tab1Title = new Text(tabTitle);
		tab1Title.setStyleClass(TAB_TITLE_CLASS);
		tab1.add(tab1Title);
		
		return tab1;
	}
	
//	private ListItem addProcessButton(IWContext iwc, String buttonClass, String buttonTitle) {
//		ListItem item2 = new ListItem();
//		item2.setStyleClass(buttonClass);
//		item2.setStyleClass(PROC_BTN_CLASS);
//		
//		Link casesButton = new Link(buttonTitle);
//		casesButton.setStyleClass(buttonClass);
//		casesButton.setStyleClass(PROCESS_BUTTON_CLASS);
//		item2.add(casesButton);
//		
//		return item2;
//	}
	
	/*
	private ListItem addSmoothboxButton(IWContext iwc, String buttonClass, String buttonText, String href, String titleAttribute, String relAttribute) {
		ListItem item2 = new ListItem();
		item2.setStyleClass(buttonClass);
		Link editButton = new Link(buttonText);
		editButton.setMarkupAttribute(HREF_ATTRIBUTE, href);
		editButton.setMarkupAttribute(TITLE_ATTRIBUTE, titleAttribute);
		editButton.setStyleClass(buttonClass);
		editButton.setStyleClass(SMOOTHBOX_LINK_CLASS);
		editButton.setMarkupAttribute(REL_ATTRIBUTE, relAttribute);
		item2.add(editButton);
		
		return item2;
	}
	*/
	
	private ListItem addButton(IWContext iwc, String buttonClass, String buttonText, String id, String onClick) {
		ListItem item2 = new ListItem();
		item2.setStyleClass(buttonClass);
		Link editButton = new Link(buttonText);
		editButton.setStyleClass(buttonClass);
		editButton.setMarkupAttribute(formIdAtt, id);
		//editButton.setId(id);
		if(onClick != null) {
			editButton.setOnClick(onClick);
		}
		item2.add(editButton);
		
		return item2;
	}
	
	/*
	private ListItem getProcessEmptyTaskItem(IWContext iwc, String processName, long processId, String taskName) {
		ListItem item = new ListItem();
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(TASK_FORM_ITEM_CLASS);
		
		Text name = new Text(taskName);
		name.setStyleClass(FORM_TITLE_CLASS);
		
		body.add(name);
		
		Lists list = new Lists();
		list.setStyleClass(BUTTON_LIST_CLASS);
		
		list.add(addSmoothboxButton(iwc, ATTACH_BTN_CLASS, getLocalizedString(iwc, "fb_home_attach_link", "Attach"), "#TB_inline?height=100&width=300&inlineId=attachTaskFormDialog", getLocalizedString(iwc, "fb_modal_attach_title", "Attach a form"), processId + CoreConstants.UNDER + taskName));
		list.add(addSmoothboxButton(iwc, CREATE_BTN_CLASS, getLocalizedString(iwc, "fb_home_create_link", "Create"), "#TB_inline?height=100&width=300&inlineId=newTaskFormDialog", getLocalizedString(iwc, "fb_modal_createTF_title", "Create new task form"), processId + CoreConstants.UNDER + taskName));
		
		body.add(list);
		
		item.add(body);
		
		return item;
	}
	*/
	
	private ListItem getProcessTaskFormItem(IWContext iwc, String processName, String processId, String title, String taskName, String date, String formId) {
		ListItem item = new ListItem();
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(TASK_FORM_ITEM_CLASS);
		body.setId(formId);
		
		Text name = new Text(title == null ? "Removed" : title);
		name.setStyleClass(FORM_TITLE_CLASS);
		
		StringBuilder builder = new StringBuilder(date)
		.append(TASK_TEXT)
		.append(taskName);
		
		Text created = new Text(builder.toString());
		created.setStyleClass(CREATED_DATE_CLASS);
		
		body.add(name);
		body.add(created);
		
		Lists list = new Lists();
		list.setStyleClass(BUTTON_LIST_CLASS);
		
		StringBuilder clickEvent = new StringBuilder("loadTaskFormDocument('").append(processName).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(processId).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(taskName).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(formId).append("');return false;");
		list.add(addButton(iwc, EDIT_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_edit_link", "Edit"), formId, clickEvent.toString()));
		list.add(addButton(iwc, TRY_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_try_link", "Try"), formId, null));
		list.add(addButton(iwc, CODE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_code_link", "Code"), formId, null));
//		list.add(addButton(iwc, DELETE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_delete_link", "Delete"), formId, null));
		
		body.add(list);
		
		item.add(body);
		
		return item;
	}
	
	private Layer getListItem(IWContext iwc, String title, String date, String formId) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(FORM_LIST_ITEM_CLASS);
		body.setId(formId);
		
		Image processIcon = new Image();
		processIcon.setStyleClass(FORM_LIST_ICON_CLASS);
		processIcon.setSrc(PROCESS_ICON);
		Text processName = new Text(CoreConstants.EMPTY);
		processName.setStyleClass(PROCESS_NAME_CLASS);
		processName.setStyleClass(FORM_TITLE_CLASS);
		Link casesButton = new Link(getLocalizedString(iwc, "fb_home_view_cases_link", "View cases"));
		Link newTaskFormButton = new Link(getLocalizedString(iwc, "fb_home_new_task_form_link", "Add task form"));
		casesButton.setStyleClass(PROCESS_BUTTON_CLASS);
		casesButton.setStyleClass(CASES_BUTTON_CLASS);
		newTaskFormButton.setStyleClass(PROCESS_BUTTON_CLASS);
		newTaskFormButton.setStyleClass(TASK_FORM_BUTTON_CLASS);
		
		Lists list = new Lists();
		list.setStyleClass(BUTTON_LIST_CLASS);
		
		Image formIcon = new Image();
		formIcon.setSrc(STANDALONE_FORM_ICON);
		formIcon.setStyleClass(FORM_LIST_ICON_CLASS);
		
		Text name = new Text(title);
		name.setStyleClass(FORM_TITLE_CLASS);
		
		Text created = new Text(date);
		created.setStyleClass(CREATED_DATE_CLASS);
		
		body.add(formIcon);
		body.add(name);
		body.add(created);
		
		body.add(list);
		
		list.add(addButton(iwc, EDIT_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_edit_link", "Edit"), formId, null));
		list.add(addButton(iwc, TRY_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_try_link", "Try"), formId, null));
		list.add(addButton(iwc, CODE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_code_link", "Code"), formId, null));
		list.add(addButton(iwc, ENTRIES_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_entries_link", "Entries"), formId, null));
//		list.add(addButton(iwc, DELETE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_delete_link", "Delete"), formId, null));
		
		return body;
	}
	
}
