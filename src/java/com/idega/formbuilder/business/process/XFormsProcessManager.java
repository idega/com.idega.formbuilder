package com.idega.formbuilder.business.process;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.def.TaskMgmtDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.jbpm.IdegaJbpmContext;
import com.idega.jbpm.def.View;
import com.idega.jbpm.def.ViewFactory;
import com.idega.jbpm.def.ViewToTask;
import com.idega.jbpm.def.ViewToTaskType;

public class XFormsProcessManager {
	
	private static final String JBPM_XFORM_VIEW_NAME = "jbpm_view_name";
	
	private ViewToTask viewToTaskBinder;
	private ViewFactory viewFactory;
	private IdegaJbpmContext idegaJbpmContext;
	
	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void assignTaskForm(String processId, String taskName, String formId) {
		
//		TODO: moe purely bpm related stuff to jbpm module.
		JbpmContext ctx = getIdegaJbpmContext().createJbpmContext();
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().getProcessDefinition(Long.parseLong(processId));
			TaskMgmtDefinition mgmt = pd.getTaskMgmtDefinition();
			Task task = mgmt.getTask(taskName);
			View view = getViewFactory().getView(formId, true);
			getViewToTaskBinder().bind(view, task);
			
		} finally {
			
			getIdegaJbpmContext().closeAndCommit(ctx);
		}
	}
	
	public List<AdvancedProperty> getTaskProperties(String processId, String taskId) {
		List<AdvancedProperty> result = new ArrayList<AdvancedProperty>();
		
		JbpmContext ctx = getIdegaJbpmContext().createJbpmContext();
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().getProcessDefinition(Long.parseLong(processId));
			TaskMgmtDefinition mgmt = pd.getTaskMgmtDefinition();
			Task task = mgmt.getTask(taskId);
			/*
			View view = getViewToTaskBinder().getView(task.getId());
			AdvancedProperty taskForm = new AdvancedProperty(JBPM_XFORM_VIEW_NAME, view == null ? "" : view.getViewId());
			result.add(taskForm);
			*/
			
			
			/*
			 * 
			 * TODO: adapt this logic to current assignments management 
			ActorTaskBind atb = getActorToTaskBinder().getActor(task.getId());
			if(atb != null) {
				AdvancedProperty taskActor = new AdvancedProperty(JBPM_XFORM_ACTOR_ID, atb == null ? "" : atb.getActorId());
				AdvancedProperty actorType = new AdvancedProperty(JBPM_XFORM_ACTOR_TYPE, atb == null ? "" : atb.getActorType());
				result.add(taskActor);
				result.add(actorType);
				String actorName = getActorToTaskBinder().getActorName(atb.getActorId(), atb.getActorType());
				AdvancedProperty name = new AdvancedProperty(JBPM_XFORM_ACTOR_NAME, actorName);
				result.add(name);
				
				IdentityMgmntBean abm = (IdentityMgmntBean) WFUtil.getBeanInstance("actorBindingManager");
//				if(abm != null) {
//					abm.setActorType(atb.getActorType());
//					String[] actors = {atb.getActorId()};
//					abm.setActorId(actors);
//				}
			}
			*/
			return result;
			
		} finally {
			
			getIdegaJbpmContext().closeAndCommit(ctx);
		}
	}

	public ViewToTask getViewToTaskBinder() {
		return viewToTaskBinder;
	}

	@Autowired
	@ViewToTaskType("xforms")
	public void setViewToTaskBinder(ViewToTask viewToTaskBinder) {
		this.viewToTaskBinder = viewToTaskBinder;
	}

	public IdegaJbpmContext getIdegaJbpmContext() {
		return idegaJbpmContext;
	}

	@Autowired
	public void setIdegaJbpmContext(IdegaJbpmContext idegaJbpmContext) {
		this.idegaJbpmContext = idegaJbpmContext;
	}
}