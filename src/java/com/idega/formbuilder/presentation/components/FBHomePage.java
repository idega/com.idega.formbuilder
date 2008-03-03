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
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.formbuilder.business.process.XFormsProcessManager;
import com.idega.formbuilder.presentation.FBComponentBase;
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
	private static final String DELETE_TF_BUTTON_CLASS = "deleteTFButton";
	private static final String web2BeanIdentifier = "web2bean";
	private static final String MOOTABS_TITLE_CLASS = "mootabs_title";
	private static final String TITLE_ATTRIBUTE = "title";
	private static final String PROCESS_TAB_TITLE = "processes";
	private static final String STANDALONE_TAB_TITLE = "standalone";
	private static final String MOOTABS_PANEL_CLASS = "mootabs_panel";
	private static final String PROCESS_BUTTON_LIST_CLASS = "processButtonList";
	
	private static final String PROCESS_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/images/process.png";
	private static final String STANDALONE_FORM_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/images/window-new.png";
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = IWContext.getIWContext(context);
		
		AddResource adder = AddResourceFactory.getInstance(iwc);
		Web2Business web2 = (Web2Business) getBeanInstance(web2BeanIdentifier);
		try {
			adder.addJavaScriptAtPosition(iwc, AddResource.HEADER_BEGIN, web2.getBundleURIToMootoolsLib());
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
		
		ListItem tab1 = new ListItem();
		tab1.setMarkupAttribute(TITLE_ATTRIBUTE, PROCESS_TAB_TITLE);
		Image tabIcon = new Image();
		tabIcon.setSrc(PROCESS_ICON);
		tab1.add(tabIcon);
		tab1.add(new Text(getLocalizedString(iwc, "fb_home_proc_tab", "Processes")));
		
		ListItem tab2 = new ListItem();
		tabIcon = new Image();
		tabIcon.setSrc(STANDALONE_FORM_ICON);
		tab2.add(tabIcon);
		tab2.add(new Text(getLocalizedString(iwc, "fb_home_proc_alone", "Standalone")));
		tab2.setMarkupAttribute(TITLE_ATTRIBUTE, STANDALONE_TAB_TITLE);
		
		tabsList.add(tab1);
		tabsList.add(tab2);
		
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
			
			ListItem item2 = new ListItem();
			item2.setStyleClass(CASES_BUTTON_CLASS);
			
			Link casesButton = new Link(getLocalizedString(iwc, "fb_home_view_cases_link", "View cases"));
			casesButton.setStyleClass(CASES_BUTTON_CLASS);
			casesButton.setStyleClass(PROCESS_BUTTON_CLASS);
			item2.add(casesButton);
			
			topList.add(item2);
			
			Link deleteProcessButton = new Link(getLocalizedString(iwc, "fb_home_delete_process_link", "Delete"));
			deleteProcessButton.setStyleClass(PROCESS_BUTTON_CLASS + " " + DELETE_BUTTON_CLASS);
			
			item2 = new ListItem();
			item2.setStyleClass(DELETE_BUTTON_CLASS);
			item2.add(deleteProcessButton);
			topList.add(item2);
			
			Image processIcon = new Image();
			processIcon.setSrc(PROCESS_ICON);
			processIcon.setStyleClass(FORM_LIST_ICON_CLASS);
			
			Text processName = new Text(definition.getName());
			processName.setStyleClass(FORM_TITLE_CLASS);
			
			Text created = new Text("");
			created.setStyleClass(CREATED_DATE_CLASS);
			
			processItem.add(processIcon);
			processItem.add(processName);
			processItem.add(created);
			processItem.add(topList);
			
			Lists list = new Lists();
			list.setStyleClass(TASK_FORM_LIST_CLASS);
				
			List<Task> tasks = jbpmProcessBusiness.getProcessDefinitionTasks(definition.getId());
				
			String formTitle = null;
			for(Iterator<Task> taskIterator = tasks.iterator(); taskIterator.hasNext(); ) {
				Task task = (Task) taskIterator.next();
				View view = viewToTaskBinnder.getView(task.getId());
				if(view == null) 
					continue;
				for(Iterator<SelectItem> it = formsList.iterator(); it.hasNext(); ) {
					SelectItem item = it.next();
					if(item.getValue().equals(view.getViewId())) {
						formTitle = (String) item.getLabel();
						it.remove();
					}
				}
				list.add(getProcessTaskFormItem(context, definition.getName(), definition.getId(), formTitle,  task.getName(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + getCreatedDate(view.getViewId()), view.getViewId()));
			}

			processItem.add(list);
			tab1Forms.add(processItem);
		}
		listContainer.add(tab1Forms);
		
		Layer tab2Forms = new Layer(Layer.DIV);
		tab2Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab2Forms.setId(STANDALONE_TAB_TITLE);
		
		Iterator<SelectItem> it = formsList.iterator();
		while(it.hasNext()) {
			SelectItem item = it.next();
			tab2Forms.add(getListItem(context, item.getLabel(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + getCreatedDate(item.getValue().toString()), item.getValue().toString()));
		}
		
		listContainer.add(tab2Forms);
		
		fbHomePage.add(listContainer);
		
		add(fbHomePage);
	}
	
	private ListItem getProcessTaskFormItem(FacesContext context, String processName, long processId, String title, String taskName, String date, String formId) {
		IWContext iwc = IWContext.getIWContext(context);
		
		ListItem item = new ListItem();
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(TASK_FORM_ITEM_CLASS);
		body.setId(formId);
		
		Image formIcon = new Image();
		formIcon.setSrc(STANDALONE_FORM_ICON);
		
		Text name = new Text(title);
		name.setStyleClass(FORM_TITLE_CLASS);
		
		Text created = new Text(date + "    Task: " + taskName);
		created.setStyleClass(CREATED_DATE_CLASS);
		
		body.add(name);
		body.add(created);
		
		Lists list = new Lists();
		list.setStyleClass(BUTTON_LIST_CLASS);
		
		ListItem item2 = new ListItem();
		item2.setStyleClass(EDIT_BUTTON_CLASS);
		Link editButton = new Link(getLocalizedString(iwc, "fb_home_edit_link", "Edit"));
		editButton.setStyleClass(EDIT_BUTTON_CLASS);
		editButton.setId(formId + edit_process_mode_button_postfix);
		
		if(true)
			throw new RuntimeException("Doesn't compile with the following line. Fix this");
		//editButton.setOnClick(new StringBuffer("loadTaskFormDocument('").append(processName).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(processId).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(taskName).append(CoreConstants.JS_STR_PARAM_SEPARATOR).append(formId).append("');return false;").toString());
		item2.add(editButton);
		list.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass(TRY_BUTTON_CLASS);
		Link tryButton = new Link(getLocalizedString(iwc, "fb_home_try_link", "Try"));
		tryButton.setStyleClass(TRY_BUTTON_CLASS);
		tryButton.setId(formId + try_button_postfix);
		item2.add(tryButton);
		list.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass(CODE_BUTTON_CLASS);
		Link codeButton = new Link(getLocalizedString(iwc, "fb_home_code_link", "Code"));
		codeButton.setStyleClass(CODE_BUTTON_CLASS);
		codeButton.setId(formId + code_button_postfix);
		item2.add(codeButton);
		list.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass(DELETE_BUTTON_CLASS);
		Link deleteButton = new Link(getLocalizedString(iwc, "fb_home_delete_link", "Delete"));
		deleteButton.setStyleClass(DELETE_TF_BUTTON_CLASS);
		deleteButton.setId(formId + delete_button_postfix);
		item2.add(deleteButton);
		list.add(item2);
		
		body.add(list);
		
		item.add(body);
		
		return item;
	}
	
	private Layer getListItem(FacesContext context, String title, String date, String formId) {
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(FORM_LIST_ITEM_CLASS);
		body.setId(formId);
		
		Image processIcon = new Image();
		processIcon.setStyleClass(FORM_LIST_ICON_CLASS);
		processIcon.setSrc(PROCESS_ICON);
		Text processName = new Text("");
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
		
		ListItem item = new ListItem();
		item.setStyleClass(ENTRIES_BUTTON_CLASS);
		Link entriesButton = new Link(getLocalizedString(iwc, "fb_home_entries_link", "Entries"));
		entriesButton.setId(formId + entries_button_postfix);
		entriesButton.setStyleClass(ENTRIES_BUTTON_CLASS);
		item.add(entriesButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass(EDIT_BUTTON_CLASS);
		Link editButton = new Link(getLocalizedString(iwc, "fb_home_edit_link", "Edit"));
		editButton.setId(formId + edit_button_postfix);
		editButton.setStyleClass(EDIT_BUTTON_CLASS);
		item.add(editButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass(TRY_BUTTON_CLASS);
		Link tryButton = new Link(getLocalizedString(iwc, "fb_home_try_link", "Try"));
		tryButton.setStyleClass(TRY_BUTTON_CLASS);
		tryButton.setId(formId + try_button_postfix);
		item.add(tryButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass(CODE_BUTTON_CLASS);
		Link codeButton = new Link(getLocalizedString(iwc, "fb_home_code_link", "Code"));
		codeButton.setId(formId + code_button_postfix);
		codeButton.setStyleClass(CODE_BUTTON_CLASS);
		item.add(codeButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass(DELETE_BUTTON_CLASS);
		Link deleteButton = new Link(getLocalizedString(iwc, "fb_home_delete_link", "Delete"));
		deleteButton.setId(formId + delete_button_postfix);
		deleteButton.setStyleClass(DELETE_BUTTON_CLASS);
		item.add(deleteButton);
		list.add(item);
		
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
