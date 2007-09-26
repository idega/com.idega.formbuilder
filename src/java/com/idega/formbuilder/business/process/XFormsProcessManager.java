package com.idega.formbuilder.business.process;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.def.TaskMgmtDefinition;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.chiba.process.XFormsView;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.jbpm.data.ActorTaskBinding;
import com.idega.jbpm.def.ActorToTaskBinder;
import com.idega.jbpm.def.View;
import com.idega.jbpm.def.ViewToTask;
import com.idega.jbpm.identity.ActorBindingManager;
import com.idega.webface.WFUtil;

public class XFormsProcessManager {
	
	private static final String JBPM_XFORM_VIEW_NAME = "jbpm_view_name";
	private static final String JBPM_XFORM_ACTOR_NAME = "jbpm_actor_name";
	private static final String JBPM_XFORM_ACTOR_TYPE = "jbpm_actor_type";
	private static final String JBPM_XFORM_ACTOR_ID = "jbpm_actor_id";
	
	public void assignTaskForm(String processId, String taskId, String formId) {
		JbpmContext ctx = getJbpmProcessBusiness().getJbpmContext();
		try {
			ProcessDefinition pd = getJbpmProcessBusiness().getProcessDefinition(processId, ctx);
			TaskMgmtDefinition mgmt = pd.getTaskMgmtDefinition();
			Task task = mgmt.getTask(taskId);
			XFormsView view = new XFormsView();
			view.setViewId(formId);
			getJbpmProcessBusiness().getViewToTaskBinder().bind(view, task);
		} finally {
			if(ctx != null) {
				ctx.close();
			}
		}
	}
	
	public List getTaskProperties(String processId, String taskId) {
		List result = new ArrayList();
		JbpmContext ctx = getJbpmProcessBusiness().getJbpmContext();
		try {
			ProcessDefinition pd = getJbpmProcessBusiness().getProcessDefinition(processId, ctx);
			TaskMgmtDefinition mgmt = pd.getTaskMgmtDefinition();
			Task task = mgmt.getTask(taskId);
			View view = getViewToTaskBinder().getView(task.getId());
			AdvancedProperty taskForm = new AdvancedProperty(JBPM_XFORM_VIEW_NAME, view == null ? "" : view.getViewId());
			result.add(taskForm);
			ActorTaskBinding atb = getActorToTaskBinder().getActor(task.getId());
			if(atb != null) {
				AdvancedProperty taskActor = new AdvancedProperty(JBPM_XFORM_ACTOR_ID, atb == null ? "" : atb.getActorId());
				AdvancedProperty actorType = new AdvancedProperty(JBPM_XFORM_ACTOR_TYPE, atb == null ? "" : atb.getActorType());
				result.add(taskActor);
				result.add(actorType);
				String actorName = getActorToTaskBinder().getActorName(atb.getActorId(), atb.getActorType());
				AdvancedProperty name = new AdvancedProperty(JBPM_XFORM_ACTOR_NAME, actorName);
				result.add(name);
				
				ActorBindingManager abm = (ActorBindingManager) WFUtil.getBeanInstance("actorBindingManager");
				if(abm != null) {
					abm.setActorType(atb.getActorType());
					String[] actors = {atb.getActorId()};
					abm.setActorId(actors);
				}
			}
			return result;
		} finally {
			if(ctx != null) {
				ctx.close();
			}
		}
	}
	
	private JbpmProcessBusinessBean jbpmProcessBusiness;
	private ViewToTask viewToTaskBinder;
	private ActorToTaskBinder actorToTaskBinder;

	public ViewToTask getViewToTaskBinder() {
		return viewToTaskBinder;
	}

	public void setViewToTaskBinder(ViewToTask viewToTaskBinder) {
		this.viewToTaskBinder = viewToTaskBinder;
	}

	public JbpmProcessBusinessBean getJbpmProcessBusiness() {
		return jbpmProcessBusiness;
	}

	public void setJbpmProcessBusiness(JbpmProcessBusinessBean jbpmProcessBusiness) {
		this.jbpmProcessBusiness = jbpmProcessBusiness;
	}

	public ActorToTaskBinder getActorToTaskBinder() {
		return actorToTaskBinder;
	}

	public void setActorToTaskBinder(ActorToTaskBinder actorToTaskBinder) {
		this.actorToTaskBinder = actorToTaskBinder;
	}

}
