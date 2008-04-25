package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Multimap;
import com.idega.documentmanager.business.PersistedForm;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.XFormPersistenceType;
import com.idega.jbpm.IdegaJbpmContext;
import com.idega.jbpm.def.TaskView;
import com.idega.jbpm.def.ViewFactory;
import com.idega.jbpm.def.ViewFactoryType;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/04/24 23:53:11 $ by $Author: laddi $
 */
@Scope("singleton")
@Service(FBHomePageBean.beanIdentifier)
public class FBHomePageBean {
	
	public static final String beanIdentifier = "FBHomePageBean";
	private IdegaJbpmContext idegaJbpmContext;
	private PersistenceManager persistenceManager;
	private ViewFactory viewFactory;
	
	@Transactional(readOnly=true)
	public List<ProcessAllTasksForms> getAllTasksForms(IWContext iwc, Locale locale) {
		
		JbpmContext jbpmContext = getIdegaJbpmContext().createJbpmContext();
		
		try {
			List<ProcessDefinition> defs = jbpmContext.getGraphSession().findLatestProcessDefinitions();
			HashSet<Long> defsIds = new HashSet<Long>(defs.size());
			
			for (ProcessDefinition def : defs)
				defsIds.add(def.getId());
			
			Multimap<Long, TaskView> pdsViews = getViewFactory().getAllViewsByProcessDefinitions(defsIds);
			ArrayList<ProcessAllTasksForms> allForms = new ArrayList<ProcessAllTasksForms>(pdsViews.keySet().size());
			
			for (Long pdId : pdsViews.keySet()) {
				
				ProcessDefinition pd;
				Collection<TaskView> tviews = pdsViews.get(pdId);
				
				if(tviews.isEmpty()) {
					pd = jbpmContext.getGraphSession().getProcessDefinition(pdId);
				} else {
					
					 pd = tviews.iterator().next().getTask().getProcessDefinition();
				 }
				
				ProcessAllTasksForms processForms = new ProcessAllTasksForms();
				processForms.setProcessId(String.valueOf(pd.getId()));
				processForms.setProcessName(pd.getName());
				
				ArrayList<TaskForm> taskForms = new ArrayList<TaskForm>(tviews.size());
				
				for (TaskView taskView : tviews) {
					
					String dateCreatedStr = new IWTimestamp(taskView.getDateCreated()).getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT);
					TaskForm form = new TaskForm();
					form.setDateCreatedStr(dateCreatedStr);
					form.setFormId(taskView.getViewId());
					form.setFormName(taskView.getDisplayName());
					form.setTaskName(taskView.getTask().getName());
					taskForms.add(form);
				}
				
				processForms.setTasksCount(String.valueOf(pd.getTaskMgmtDefinition().getTasks().size()));
				processForms.setTaskForms(taskForms);
				allForms.add(processForms);
			}
			
			return allForms;
			
		} finally {
			getIdegaJbpmContext().closeAndCommit(jbpmContext);
		}
	}
	
	public List<PersistedForm> getStandaloneForms() {
		
		return getPersistenceManager().getStandaloneForms();
	}
	
	public IdegaJbpmContext getIdegaJbpmContext() {
		return idegaJbpmContext;
	}

	@Autowired
	public void setIdegaJbpmContext(IdegaJbpmContext idegaJbpmContext) {
		this.idegaJbpmContext = idegaJbpmContext;
	}
	
	public class ProcessAllTasksForms {
		
		private String processId;
		private String processName;
		private String tasksCount;
		private List<TaskForm> taskForms;
		
		public List<TaskForm> getTaskForms() {
			return taskForms;
		}
		public void setTaskForms(List<TaskForm> taskForms) {
			this.taskForms = taskForms;
		}
		public String getProcessName() {
			return processName;
		}
		public void setProcessName(String processName) {
			this.processName = processName;
		}
		public String getTasksCount() {
			return tasksCount;
		}
		public void setTasksCount(String tasksCount) {
			this.tasksCount = tasksCount;
		}
		public String getProcessId() {
			return processId;
		}
		public void setProcessId(String processId) {
			this.processId = processId;
		}
	}
	
	public class TaskForm {
		
		private String dateCreatedStr;
		private String formName;
		private String taskName;
		private String formId;
		
		public String getDateCreatedStr() {
			return dateCreatedStr;
		}
		public void setDateCreatedStr(String dateCreatedStr) {
			this.dateCreatedStr = dateCreatedStr;
		}
		public String getFormName() {
			return formName;
		}
		public void setFormName(String formName) {
			this.formName = formName;
		}
		public String getTaskName() {
			return taskName;
		}
		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}
		public String getFormId() {
			return formId;
		}
		public void setFormId(String formId) {
			this.formId = formId;
		}
	}

	public PersistenceManager getPersistenceManager() {
		return persistenceManager;
	}

	@Autowired
	@XFormPersistenceType("slide")
	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	@Autowired
	@ViewFactoryType("xforms")
	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
}