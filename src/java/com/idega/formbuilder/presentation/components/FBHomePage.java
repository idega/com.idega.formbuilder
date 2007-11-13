package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Task;

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
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBHomePage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "HomePage";
	
	public static final String delete_button_postfix = "_delete";
	public static final String entries_button_postfix = "_entries";
	public static final String duplicate_button_postfix = "_duplicate";
	public static final String edit_button_postfix = "_edit";
	public static final String code_button_postfix = "_code";
	public static final String edit_process_mode_button_postfix = "_process";
	
	private static final String CONTAINER_DIV_ID = "fbHomePage";
	private static final String CONTAINER_HEADER_ID = "fbHomePageHeaderBlock";
	private static final String HEADER_LEFT = "fbHPLeft";
	private static final String HEADER_RIGHT = "fbHPRight";
	private static final String HEADER_NAME = "headerName";
	private static final String HEADER_SLOGAN = "headerSlogan";
	private static final String NEW_FORM_COMP_IDLE_CLASS = "newFormComponentIdle";
	private static final String NEW_FORM_COMP_ID = "newFormComp";
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
	private static final String CODE_BUTTON_CLASS = "codeButton";
	private static final String DUPLICATE_BUTTON_CLASS = "duplicateButton";
	private static final String DELETE_BUTTON_CLASS = "deleteButton";
	
	private static final String PROCESS_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/images/process.png";
	private static final String STANDALONE_FORM_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/images/window-new.png";
	
	public FBHomePage() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		IWContext iwc = IWContext.getIWContext(context);
		
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
		
		Layer headerPartRight = new Layer(Layer.DIV);
		headerPartRight.setId(HEADER_RIGHT);
		
		FBNewFormComponent newFormComponent = (FBNewFormComponent) application.createComponent(FBNewFormComponent.COMPONENT_TYPE);
		newFormComponent.setStyleClass(NEW_FORM_COMP_IDLE_CLASS);
		newFormComponent.setId(NEW_FORM_COMP_ID);
		newFormComponent.setCompact(true);
		
		headerPartRight.add(newFormComponent);
		header.add(headerPartLeft);
		header.add(headerPartRight);
		
		fbHomePage.add(header);
		
		PersistenceManager persistence_manager = (PersistenceManager) WFUtil.getBeanInstance("xformsPersistenceManager");
		List<SelectItem> formsList = new ArrayList<SelectItem>(persistence_manager.getForms());
		
		Layer listContainer = new Layer(Layer.DIV);
		listContainer.setId(FORM_LIST_CONTAINER);
		
		XFormsProcessManager xformsProcessManager = (XFormsProcessManager) WFUtil.getBeanInstance("xformsProcessManager");
		ViewToTask viewToTaskBinnder = xformsProcessManager.getViewToTaskBinder();
		JbpmProcessBusinessBean jbpmProcessBusiness = (JbpmProcessBusinessBean) WFUtil.getBeanInstance(JbpmProcessBusinessBean.BEAN_ID);
		List<ProcessDefinition> processList = jbpmProcessBusiness.getProcessList();
		
		for(Iterator<ProcessDefinition> processIterator = processList.iterator(); processIterator.hasNext(); ) {
			ProcessDefinition definition = (ProcessDefinition) processIterator.next();
			Layer processItem = new Layer(Layer.DIV);
			processItem.setStyleClass(PROCESS_ITEM_CLASS);
			Lists topList = new Lists();
			topList.setStyleClass("buttonList processButtonList");
			ListItem item2 = new ListItem();
			Link casesButton = new Link(getLocalizedString(iwc, "fb_home_view_cases_link", "View cases"));
			
			item2.add(casesButton);
			topList.add(item2);
			item2 = new ListItem();
			FBAddTaskForm newTaskFormButton = new FBAddTaskForm();
			
			item2.add(newTaskFormButton);
			topList.add(item2);
			casesButton.setStyleClass(PROCESS_BUTTON_CLASS + " " + CASES_BUTTON_CLASS);
			newTaskFormButton.setStyleClass(PROCESS_BUTTON_CLASS + " " + TASK_FORM_BUTTON_CLASS);
			newTaskFormButton.setId(new Long(definition.getId()).toString());
			
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
				
			List<Task> tasks = jbpmProcessBusiness.getProcessDefinitionTasks(new Long(definition.getId()).toString());
				
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
			listContainer.add(processItem);
			}
		
		Iterator<SelectItem> it = formsList.iterator();
		while(it.hasNext()) {
			SelectItem item = it.next();
			listContainer.add(getListItem(context, item.getLabel(), getLocalizedString(iwc, "fb_home_created_label", "Created") + ": " + getCreatedDate(item.getValue().toString()), item.getValue().toString()));
		}
		
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
		editButton.setOnClick("loadTaskFormDocument('" + processName + "', '" + processId + "', '" + taskName + "', '" + formId + "')");
		editButton.setNoURL();
		item2.add(editButton);
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
		deleteButton.setStyleClass(DELETE_BUTTON_CLASS);
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
		processName.setStyleClass(PROCESS_NAME_CLASS + " " + FORM_TITLE_CLASS);
		Link casesButton = new Link(getLocalizedString(iwc, "fb_home_view_cases_link", "View cases"));
		Link newTaskFormButton = new Link(getLocalizedString(iwc, "fb_home_new_task_form_link", "Add task form"));
		casesButton.setStyleClass(PROCESS_BUTTON_CLASS + " " + CASES_BUTTON_CLASS);
		newTaskFormButton.setStyleClass(PROCESS_BUTTON_CLASS + " " + TASK_FORM_BUTTON_CLASS);
		
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
		item.setStyleClass(CODE_BUTTON_CLASS);
		Link codeButton = new Link(getLocalizedString(iwc, "fb_home_code_link", "Code"));
		codeButton.setId(formId + code_button_postfix);
		codeButton.setStyleClass(CODE_BUTTON_CLASS);
		item.add(codeButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass(DUPLICATE_BUTTON_CLASS);
		Link duplicateButton = new Link(getLocalizedString(iwc, "fb_home_duplicate_link", "Duplicate"));
		duplicateButton.setId(formId + duplicate_button_postfix);
		duplicateButton.setStyleClass(DUPLICATE_BUTTON_CLASS);
		item.add(duplicateButton);
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
		String interm1 = formId.substring(formId.indexOf("-") + 5);
		String month = interm1.substring(0, 3);
		String interm2 = interm1.substring(interm1.indexOf("_") + 1);
		String day = interm2.substring(0, 2);
		String year = interm2.substring(interm2.length() - 4);
		return month + " " + day + ", " + year;
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
	
}
