package com.idega.formbuilder.presentation.components;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Task;

import com.idega.block.web2.business.Web2Business;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.formbuilder.business.process.XFormsProcessManager;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.jbpm.def.View;
import com.idega.jbpm.def.ViewToTask;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.webface.WFUtil;

public class FBHomePage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "HomePage";
	
	public static final String delete_button_postfix = "_delete";
	public static final String entries_button_postfix = "_entries";
	public static final String duplicate_button_postfix = "_duplicate";
	public static final String edit_button_postfix = "_edit";
	public static final String code_button_postfix = "_code";
	public static final String edit_process_mode_button_postfix = "_process";
	public static final String try_button_postfix = "_try";
	
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
	private static final String DELETE_BUTTON_CLASS = "deleteButton";
//	private static final String DELETE_TF_BUTTON_CLASS = "deleteTFButton";
	private static final String web2BeanIdentifier = "web2bean";
	private static final String MOOTABS_TITLE_CLASS = "mootabs_title";
	private static final String TITLE_ATTRIBUTE = "title";
	private static final String REL_ATTRIBUTE = "rel";
	private static final String HREF_ATTRIBUTE = "href";
	private static final String PROCESS_TAB_TITLE = "processes";
	private static final String STANDALONE_TAB_TITLE = "standalone";
	private static final String MOOTABS_PANEL_CLASS = "mootabs_panel";
	private static final String PROCESS_BUTTON_LIST_CLASS = "processButtonList";
	private static final String TAB_TITLE_CLASS = "tabTitle";
	private static final String PROC_BTN_CLASS = "procBtnClass";
	private static final String TRANSITION_BTN_CLASS = "transitionButton";
	private static final String EXPAND_BTN_CLASS = "expandButton";
	private static final String ATTACH_BTN_CLASS = "attachButton";
	private static final String CREATE_BTN_CLASS = "createButton";
	private static final String SMOOTHBOX_LINK_CLASS = "smoothbox";
	private static final String STATEFULL_TAB_CLASS = "stateFullTab";
	private static final String TASK_TEXT = "    Task: ";
	
	private static final String PROCESS_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/orbz-machine-32x32.png";
	private static final String STANDALONE_FORM_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/fields_32.png";
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		
		AddResource adder = AddResourceFactory.getInstance(iwc);
		Web2Business web2 = (Web2Business) getBeanInstance(web2BeanIdentifier);
		try {
			adder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, web2.getBundleURIToMootoolsLib());
			adder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, web2.getBundleUriToSmoothboxScript());
			adder.addStyleSheet(iwc, AddResource.HEADER_BEGIN, web2.getBundleUriToSmoothboxStylesheet());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		adder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, web2.getBundleUriToMootabsScript());
		adder.addStyleSheet(iwc, AddResource.HEADER_BEGIN, web2.getBundleUriToMootabsStyle());
		
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
		
		PersistenceManager persistence_manager = (PersistenceManager) WFUtil.getBeanInstance("xformsPersistenceManager");
		List<SelectItem> formsList = new ArrayList<SelectItem>(persistence_manager.getForms());
		
		Layer listContainer = new Layer(Layer.DIV);
		listContainer.setId(FORM_LIST_CONTAINER);
		
		XFormsProcessManager xformsProcessManager = (XFormsProcessManager) WFUtil.getBeanInstance("xformsProcessManager");
		ViewToTask viewToTaskBinnder = xformsProcessManager.getViewToTaskBinder();
		JbpmProcessBusinessBean jbpmProcessBusiness = (JbpmProcessBusinessBean) WFUtil.getBeanInstance(JbpmProcessBusinessBean.BEAN_ID);
		List<ProcessDefinition> processList = jbpmProcessBusiness.getProcessList();
		
		Lists tabsList = new Lists();
		tabsList.setStyleClass(MOOTABS_TITLE_CLASS);
		
		tabsList.add(addTab(iwc, STANDALONE_TAB_TITLE, STANDALONE_FORM_ICON, getLocalizedString(iwc, "fb_home_proc_alone", "Standalone")));
		tabsList.add(addTab(iwc, PROCESS_TAB_TITLE, PROCESS_ICON, getLocalizedString(iwc, "fb_home_proc_tab", "Processes")));
		
		listContainer.add(tabsList);
		
		Layer tab1Forms = new Layer(Layer.DIV);
		tab1Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab1Forms.setId(PROCESS_TAB_TITLE);
		
		for(Iterator<ProcessDefinition> processIterator = processList.iterator(); processIterator.hasNext(); ) {
			ProcessDefinition definition = (ProcessDefinition) processIterator.next();
			
			Layer processItem = new Layer(Layer.DIV);
			processItem.setStyleClass(PROCESS_ITEM_CLASS);
			
			Lists topList = new Lists();
			topList.setStyleClass(BUTTON_LIST_CLASS);
			topList.setStyleClass(PROCESS_BUTTON_LIST_CLASS);
			
			topList.add(addProcessButton(iwc, CASES_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_view_cases_link", "View cases")));
			
			topList.add(addProcessButton(iwc, DELETE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_delete_process_link", "Delete")));
			
			ListItem item2 = new ListItem();
			Link expandButton = new Link(getLocalizedString(iwc, "fb_home_expand_process_link", "Expand"));
			expandButton.setStyleClass(TRANSITION_BTN_CLASS);
			expandButton.setStyleClass(EXPAND_BTN_CLASS);
			item2.add(expandButton);
			topList.add(item2);
			
			Image processIcon = new Image();
			processIcon.setSrc(PROCESS_ICON);
			processIcon.setStyleClass(FORM_LIST_ICON_CLASS);
			
			Text processName = new Text(definition.getName());
			processName.setStyleClass(FORM_TITLE_CLASS);
			
			Text created = new Text(CoreConstants.EMPTY);
			created.setStyleClass(CREATED_DATE_CLASS);
			
			Lists list = new Lists();
			list.setStyleClass(TASK_FORM_LIST_CLASS);
				
			List<Task> tasks = jbpmProcessBusiness.getProcessDefinitionTasks(definition.getId());
				
			int formCount = 0;
			String formTitle = null;
			for(Iterator<Task> taskIterator = tasks.iterator(); taskIterator.hasNext(); ) {
				Task task = (Task) taskIterator.next();
				try {
					View view = viewToTaskBinnder.getView(task.getId());
					if(view == null) {
						list.add(getProcessEmptyTaskItem(iwc, definition.getName(), definition.getId(), task.getName()));
					} else {
						for(Iterator<SelectItem> it = formsList.iterator(); it.hasNext(); ) {
							SelectItem item = it.next();
							if(item.getValue().equals(view.getViewId())) {
								formTitle = (String) item.getLabel();
								it.remove();
								formCount++;
							}
						}
						list.add(getProcessTaskFormItem(iwc, definition.getName(), definition.getId(), formTitle,  task.getName(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + getCreatedDate(view.getViewId()), view.getViewId()));
					}
				} catch(Exception e) {
					list.add(getProcessEmptyTaskItem(iwc, definition.getName(), definition.getId(), task.getName()));
				}
			}
			
			processItem.add(processIcon);
			processItem.add(processName);
			Layer summaryText = new Layer(Layer.SPAN);
			summaryText.setStyleClass("processSummaryText");
			summaryText.add(new Text(getLocalizedString(iwc, "fb_home_total_tasks", "Total tasks: ") + tasks.size() + CoreConstants.SPACE));
			summaryText.add(new Text(getLocalizedString(iwc, "fb_home_assigned_forms", "Assigned forms: ") + formCount));
			processItem.add(summaryText);
			processItem.add(created);
			processItem.add(topList);

			processItem.add(list);
			tab1Forms.add(processItem);
		}
		listContainer.add(tab1Forms);
		
		Layer tab2Forms = new Layer(Layer.DIV);
		tab2Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab2Forms.setId(STANDALONE_TAB_TITLE);
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		formDocument.getStandaloneForms().clear();
		
		Iterator<SelectItem> it = formsList.iterator();
		while(it.hasNext()) {
			SelectItem item = it.next();
			formDocument.getStandaloneForms().add(new AdvancedProperty(item.getValue().toString(), item.getLabel()));
			tab2Forms.add(getListItem(iwc, item.getLabel(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + getCreatedDate(item.getValue().toString()), item.getValue().toString()));
		}
		
		listContainer.add(tab2Forms);
		
		fbHomePage.add(listContainer);
		
		add(fbHomePage);
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
	
	private ListItem addProcessButton(IWContext iwc, String buttonClass, String buttonTitle) {
		ListItem item2 = new ListItem();
		item2.setStyleClass(buttonClass);
		item2.setStyleClass(PROC_BTN_CLASS);
		
		Link casesButton = new Link(buttonTitle);
		casesButton.setStyleClass(buttonClass);
		casesButton.setStyleClass(PROCESS_BUTTON_CLASS);
		item2.add(casesButton);
		
		return item2;
	}
	
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
	
	private ListItem addButton(IWContext iwc, String buttonClass, String buttonText, String id, String onClick) {
		ListItem item2 = new ListItem();
		item2.setStyleClass(buttonClass);
		Link editButton = new Link(buttonText);
		editButton.setStyleClass(buttonClass);
		editButton.setId(id);
		if(onClick != null) {
			editButton.setOnClick(onClick);
		}
		item2.add(editButton);
		
		return item2;
	}
	
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
	
	private ListItem getProcessTaskFormItem(IWContext iwc, String processName, long processId, String title, String taskName, String date, String formId) {
		ListItem item = new ListItem();
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(TASK_FORM_ITEM_CLASS);
		body.setId(formId);
		
		Text name = new Text(title);
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
		
		StringBuffer clickEvent = new StringBuffer("loadTaskFormDocument('").append(processName).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(processId).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(taskName).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(formId).append("');return false;");
		list.add(addButton(iwc, EDIT_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_edit_link", "Edit"), formId + edit_process_mode_button_postfix, clickEvent.toString()));
		list.add(addButton(iwc, TRY_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_try_link", "Try"), formId + try_button_postfix, null));
		list.add(addButton(iwc, CODE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_code_link", "Code"), formId + code_button_postfix, null));
		list.add(addButton(iwc, DELETE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_delete_link", "Delete"), formId + delete_button_postfix, null));
		
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
		
		list.add(addButton(iwc, ENTRIES_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_entries_link", "Entries"), formId + entries_button_postfix, null));
		list.add(addButton(iwc, EDIT_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_edit_link", "Edit"), formId + edit_button_postfix, null));
		list.add(addButton(iwc, TRY_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_try_link", "Try"), formId + try_button_postfix, null));
		list.add(addButton(iwc, CODE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_code_link", "Code"), formId + code_button_postfix, null));
		list.add(addButton(iwc, DELETE_BUTTON_CLASS, getLocalizedString(iwc, "fb_home_delete_link", "Delete"), formId + delete_button_postfix, null));
		
		return body;
	}
	
	private String getCreatedDate(String formId) {
		String interm1 = formId.substring(formId.indexOf(CoreConstants.MINUS) + 5);
		String month = interm1.substring(0, 3);
		String interm2 = interm1.substring(interm1.indexOf(CoreConstants.UNDER) + 1);
		String day = interm2.substring(0, 2);
		String year = interm2.substring(interm2.length() - 4);
		return new StringBuilder(month).append(CoreConstants.SPACE).append(day).append(CoreConstants.COMMA).append(CoreConstants.SPACE).append(year).toString();
	}
	
}
