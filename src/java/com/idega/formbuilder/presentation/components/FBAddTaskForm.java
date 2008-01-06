package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import org.jbpm.taskmgmt.def.Task;

import com.idega.block.form.process.XFormsToTask;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.ProcessData;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextInput;
import com.idega.webface.WFUtil;

public class FBAddTaskForm extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "AddTaskForm";
	
	private static final String DEFAULT_LINK_CLASS = "processButton taskFormButton";
	
	private String status;
	
	public FBAddTaskForm() {
		this("idle");
	}
	
	public FBAddTaskForm(String status) {
		super();
		setRendererType(null);
		this.status = status;
	}
	
	protected void initializeComponent(FacesContext context) {
//		getChildren().clear();
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer body = new Layer(Layer.DIV);
		
//		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		ProcessData processData = (ProcessData) WFUtil.getBeanInstance(ProcessData.BEAN_ID);
		
		XFormsToTask viewToTaskbinder = (XFormsToTask) WFUtil.getBeanInstance("process_xforms_viewToTask");
		JbpmProcessBusinessBean jbpmProcessBean = (JbpmProcessBusinessBean)WFUtil.getBeanInstance("jbpmProcessBusiness");
		List<Task> tasks = jbpmProcessBean.getProcessDefinitionTasks(processData.getProcessId());

		Long processId = processData.getProcessId();
		
		if(status.equals("idle")) {
			body.setId("newTF_" + processId + "_box_1");
			Link newTaskFormButton = new Link(getLocalizedString(iwc, "fb_home_new_task_form_link", "Add task form"));
			newTaskFormButton.setStyleClass(DEFAULT_LINK_CLASS);
			newTaskFormButton.setOnClick("reloadAddTaskForm1(event);return false;");
			body.add(newTaskFormButton);
		} else if(status.equals("task")) {
			
			body.setId("newTF_" + processId + "_box_2");
			DropdownMenu taskChooser = new DropdownMenu();
			taskChooser.setId("newTF_" + processId + "_chooser");
			taskChooser.addMenuElementFirst("", "---------");
			
			for(Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
				Task task = it.next();
				if(viewToTaskbinder.getView(task.getId()) == null) {
					taskChooser.addMenuElement(task.getName(), task.getName());
				}
			}
			
			taskChooser.setOnChange("reloadAddTaskForm2(event);return false;");
			taskChooser.setOnBlur("resetAddTaskForm(event);return false;");
			body.add(new Label("Choose a task", taskChooser));
			body.add(taskChooser);
		} else if(status.equals("name")) {
			body.setId("newTF_" + processId + "_box_3");
			TextInput name = new TextInput();
			name.setOnBlur("resetAddTaskForm(event);return false;");
			name.setId("newTF_" + processId + "_input");
			body.add(new Label("Form name", name));
			body.add(name);
		}
		
		add(body);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
