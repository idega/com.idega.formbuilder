package com.idega.formbuilder.presentation.components;

import java.io.IOException;
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
		fbHomePage.setId("fbHomePage");
		
		//constructing the home page header part
		Layer header = new Layer(Layer.DIV);
		header.setId("fbHomePageHeaderBlock");
		
		Layer headerPartLeft = new Layer(Layer.DIV);
		headerPartLeft.setId("fbHPLeft");
		
		Text name = new Text(getLocalizedString(iwc, "fb_home_top_title", "Formbuilder"));
		name.setId("headerName");
		Text slogan = new Text(getLocalizedString(iwc, "fb_home_top_slogan", "The easy way to build your forms"));
		slogan.setId("headerSlogan");
		
		headerPartLeft.add(name);
		headerPartLeft.add(slogan);
		
		Layer headerPartRight = new Layer(Layer.DIV);
		headerPartRight.setId("fbHPRight");
		
		FBNewFormComponent newFormComponent = (FBNewFormComponent) application.createComponent(FBNewFormComponent.COMPONENT_TYPE);
		newFormComponent.setStyleClass("newFormComponentIdle");
		
		headerPartRight.add(newFormComponent);
		header.add(headerPartLeft);
		header.add(headerPartRight);
		
		fbHomePage.add(header);
		
		//constructing the home page greeting part
		PersistenceManager persistence_manager = (PersistenceManager) WFUtil.getBeanInstance("xformsPersistenceManager");
		List<SelectItem> formsList = persistence_manager.getForms();
		
		//constructing the home page form list part
		Layer listContainer = new Layer(Layer.DIV);
		listContainer.setId("formListContainer");
//		
		XFormsProcessManager xformsProcessManager = (XFormsProcessManager) WFUtil.getBeanInstance("xformsProcessManager");
		ViewToTask viewToTaskBinnder = xformsProcessManager.getViewToTaskBinder();
		JbpmProcessBusinessBean jbpmProcessBusiness = (JbpmProcessBusinessBean) WFUtil.getBeanInstance("jbpmProcessBusiness");
//		JbpmContext ctx = jbpmProcessBusiness.getJbpmContext();
		List<ProcessDefinition> processList = jbpmProcessBusiness.getProcessList();
//		
//		try {
			for(Iterator<ProcessDefinition> processIterator = processList.iterator(); processIterator.hasNext(); ) {
				ProcessDefinition definition = (ProcessDefinition) processIterator.next();
				
				
				
				Layer processItem = new Layer(Layer.DIV);
				processItem.setStyleClass("processItem");
				Layer processNameBox = new Layer(Layer.DIV);
				processNameBox.setStyleClass("processNameBox");
				Image processIcon = new Image();
				processIcon.setSrc(PROCESS_ICON);
				Text processName = new Text(definition.getName());
				processName.setStyleClass("processName");
				Link casesButton = new Link(getLocalizedString(iwc, "fb_home_view_cases_link", "View cases"));
				Link newTaskFormButton = new Link(getLocalizedString(iwc, "fb_home_new_task_form_link", "Add task form"));
				casesButton.setStyleClass("processButton casesButton");
				newTaskFormButton.setStyleClass("processButton taskFormButton");
				processNameBox.add(processIcon);
				processNameBox.add(processName);
				processNameBox.add(casesButton);
				processNameBox.add(newTaskFormButton);
				processItem.add(processNameBox);
				
				List<Task> tasks = jbpmProcessBusiness.getProcessDefinitionTasks(definition);
				
				String formTitle = null;
				for(Iterator<Task> taskIterator = tasks.iterator(); taskIterator.hasNext(); ) {
					Task task = (Task) taskIterator.next();
					View view = viewToTaskBinnder.getView(task.getId());
					if(view == null) 
						continue;
					for(Iterator<SelectItem> it = formsList.iterator(); it.hasNext(); ) {
						SelectItem item = it.next();
						if(item.getValue().equals(view.getViewId())) 
							formTitle = (String) item.getLabel();
					}
					processItem.add(getProcessTaskFormItem(context, formTitle + " (" + task.getName() + ")", "Created " + getCreatedDate(view.getViewId()), view.getViewId()));
				}
				if(formTitle == null)
					continue;
				listContainer.add(processItem);
			}
//		} finally {
//			ctx.close();
//		}
		
//		List<View> forms = viewToTaskBinnder.getAllViewsForViewType(XFormsView.VIEW_TYPE);
//		Iterator<View> it = forms.iterator();
//		DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
//		Document document = null;
		Iterator<SelectItem> it = formsList.iterator();
		while(it.hasNext()) {
			SelectItem item = it.next();
//			document = formManagerInstance..openForm(view.getViewId());
			listContainer.add(getListItem(context, item.getLabel(), "Created " + getCreatedDate(item.getValue().toString()), item.getValue().toString()));
		}
		
		fbHomePage.add(listContainer);
		
		add(fbHomePage);
	}
	
	private Layer getProcessTaskFormItem(FacesContext context, String title, String date, String formId) {
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("formListItemSub");
		body.setId("Item" + formId);
		
		Image formIcon = new Image();
		formIcon.setSrc(STANDALONE_FORM_ICON);
		
		Text name = new Text(title);
		name.setStyleClass("formTitle");
		
		Text created = new Text(date);
		created.setStyleClass("createdDate");
		
//		body.add(formIcon);
		body.add(name);
		
		Link editButton = new Link(getLocalizedString(iwc, "fb_home_edit_link", "Edit"));
		editButton.setStyleClass("bottomButton editButton processForm");
		editButton.setId(formId + edit_process_mode_button_postfix);
		body.add(editButton);
		
		Link codeButton = new Link(getLocalizedString(iwc, "fb_home_code_link", "Code"));
		codeButton.setStyleClass("bottomButton codeButton processForm");
		codeButton.setId(formId + code_button_postfix);
		body.add(codeButton);
		
		Link deleteButton = new Link(getLocalizedString(iwc, "fb_home_delete_link", "Delete"));
		deleteButton.setStyleClass("bottomButton deleteButton processForm");
		deleteButton.setId(formId + delete_button_postfix);
		body.add(deleteButton);
		
		return body;
	}
	
	private Layer getListItem(FacesContext context, String title, String date, String formId) {
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("formListItem");
		body.setId(formId);
		
		Lists list = new Lists();
		list.setStyleClass("buttonList");
		
//		Layer bodyTopLeft = new Layer(Layer.DIV);
//		bodyTopLeft.setStyleClass("formListItemTopLeft");
//		
//		Layer bodyTopRight = new Layer(Layer.DIV);
//		bodyTopRight.setStyleClass("formListItemTopRight");
//		
//		Layer bodyBottom = new Layer(Layer.DIV);
//		bodyBottom.setStyleClass("formListItemBottom");
//		body.setId("ItemBottom" + formId);
		
		Image formIcon = new Image();
		formIcon.setSrc(STANDALONE_FORM_ICON);
		formIcon.setStyleClass("formListIcon");
		
		Text name = new Text(title);
		name.setStyleClass("formTitle");
		
		Text created = new Text(date);
		created.setStyleClass("createdDate");
		
		body.add(formIcon);
		body.add(name);
		body.add(created);
		
		body.add(list);
		
		ListItem item = new ListItem();
		item.setStyleClass("entriesButton");
		Link entriesButton = new Link(getLocalizedString(iwc, "fb_home_entries_link", "Entries"));
		entriesButton.setId(formId + entries_button_postfix);
		entriesButton.setStyleClass("entriesButton");
		item.add(entriesButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass("editButton");
		Link editButton = new Link(getLocalizedString(iwc, "fb_home_edit_link", "Edit"));
		editButton.setId(formId + edit_button_postfix);
		editButton.setStyleClass("editButton");
		item.add(editButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass("codeButton");
		Link codeButton = new Link(getLocalizedString(iwc, "fb_home_code_link", "Code"));
		codeButton.setId(formId + code_button_postfix);
		codeButton.setStyleClass("codeButton");
		item.add(codeButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass("duplicateButton");
		Link duplicateButton = new Link(getLocalizedString(iwc, "fb_home_duplicate_link", "Duplicate"));
		duplicateButton.setId(formId + duplicate_button_postfix);
		duplicateButton.setStyleClass("duplicateButton");
		item.add(duplicateButton);
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass("deleteButton");
		Link deleteButton = new Link(getLocalizedString(iwc, "fb_home_delete_link", "Delete"));
		deleteButton.setId(formId + delete_button_postfix);
		deleteButton.setStyleClass("deleteButton");
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
