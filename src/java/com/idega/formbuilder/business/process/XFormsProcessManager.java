package com.idega.formbuilder.business.process;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.taskmgmt.def.Task;
import org.jbpm.taskmgmt.def.TaskMgmtDefinition;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.jbpm.data.ActorTaskBind;
import com.idega.jbpm.def.ActorTaskBinder;
import com.idega.jbpm.def.View;
import com.idega.jbpm.def.ViewFactory;
import com.idega.jbpm.def.ViewToTask;
import com.idega.jbpm.presentation.beans.ActorBindingViewBean;
import com.idega.webface.WFUtil;

public class XFormsProcessManager {
	
	private static final String JBPM_XFORM_VIEW_NAME = "jbpm_view_name";
	private static final String JBPM_XFORM_ACTOR_NAME = "jbpm_actor_name";
	private static final String JBPM_XFORM_ACTOR_TYPE = "jbpm_actor_type";
	private static final String JBPM_XFORM_ACTOR_ID = "jbpm_actor_id";
	
	private SessionFactory sessionFactory;
	private JbpmConfiguration jbpmConfiguration;
	private ViewToTask viewToTaskBinder;
	private ActorTaskBinder actorToTaskBinder;
	private ViewFactory viewFactory;
	
	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public JbpmConfiguration getJbpmConfiguration() {
		return jbpmConfiguration;
	}

	public void setJbpmConfiguration(JbpmConfiguration jbpmConfiguration) {
		this.jbpmConfiguration = jbpmConfiguration;
	}

	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void assignTaskForm(String processId, String taskName, String formId) {
		
		Transaction transaction = getSessionFactory().getCurrentSession().getTransaction();
		boolean transactionWasActive = transaction.isActive();
		
		if(!transactionWasActive)
			transaction.begin();
		
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		ctx.setSession(getSessionFactory().getCurrentSession());
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().getProcessDefinition(Long.parseLong(processId));
			TaskMgmtDefinition mgmt = pd.getTaskMgmtDefinition();
			Task task = mgmt.getTask(taskName);
			View view = getViewFactory().getViewNoLoad(formId);
			getViewToTaskBinder().bind(view, task);
			
		} finally {
			
			ctx.close();
			
			if(!transactionWasActive)
				transaction.commit();
		}
	}
	
	public List<AdvancedProperty> getTaskProperties(String processId, String taskId) {
		List<AdvancedProperty> result = new ArrayList<AdvancedProperty>();
		
		Transaction transaction = getSessionFactory().getCurrentSession().getTransaction();
		boolean transactionWasActive = transaction.isActive();
		
		if(!transactionWasActive)
			transaction.begin();
		
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		ctx.setSession(getSessionFactory().getCurrentSession());
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().getProcessDefinition(Long.parseLong(processId));
			TaskMgmtDefinition mgmt = pd.getTaskMgmtDefinition();
			Task task = mgmt.getTask(taskId);
			View view = getViewToTaskBinder().getView(task.getId());
			AdvancedProperty taskForm = new AdvancedProperty(JBPM_XFORM_VIEW_NAME, view == null ? "" : view.getViewId());
			result.add(taskForm);
			ActorTaskBind atb = getActorToTaskBinder().getActor(task.getId());
			if(atb != null) {
				AdvancedProperty taskActor = new AdvancedProperty(JBPM_XFORM_ACTOR_ID, atb == null ? "" : atb.getActorId());
				AdvancedProperty actorType = new AdvancedProperty(JBPM_XFORM_ACTOR_TYPE, atb == null ? "" : atb.getActorType());
				result.add(taskActor);
				result.add(actorType);
				String actorName = getActorToTaskBinder().getActorName(atb.getActorId(), atb.getActorType());
				AdvancedProperty name = new AdvancedProperty(JBPM_XFORM_ACTOR_NAME, actorName);
				result.add(name);
				
				ActorBindingViewBean abm = (ActorBindingViewBean) WFUtil.getBeanInstance("actorBindingManager");
				if(abm != null) {
					abm.setActorType(atb.getActorType());
					String[] actors = {atb.getActorId()};
					abm.setActorId(actors);
				}
			}
			return result;
			
		} finally {
			
			ctx.close();
			
			if(!transactionWasActive)
				transaction.commit();
		}
	}

	public ViewToTask getViewToTaskBinder() {
		return viewToTaskBinder;
	}

	public void setViewToTaskBinder(ViewToTask viewToTaskBinder) {
		this.viewToTaskBinder = viewToTaskBinder;
	}

	public ActorTaskBinder getActorToTaskBinder() {
		return actorToTaskBinder;
	}

	public void setActorToTaskBinder(ActorTaskBinder actorToTaskBinder) {
		this.actorToTaskBinder = actorToTaskBinder;
	}
}
