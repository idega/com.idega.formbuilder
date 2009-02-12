package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.graph.def.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Multimap;
import com.idega.block.form.data.XForm;
import com.idega.block.form.data.dao.XFormsDAO;
import com.idega.jbpm.BPMContext;
import com.idega.jbpm.JbpmCallback;
import com.idega.jbpm.view.TaskView;
import com.idega.jbpm.view.ViewFactory;
import com.idega.jbpm.view.ViewFactoryType;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.xformsmanager.business.Form;
import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.business.XFormPersistenceType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.13 $
 *
 * Last modified: $Date: 2009/02/12 16:53:48 $ by $Author: donatas $
 */
@Scope("singleton")
@Service(FBHomePageBean.beanIdentifier)
public class FBHomePageBean {
	
	public static final String beanIdentifier = "FBHomePageBean";
	private BPMContext idegaJbpmContext;
	private PersistenceManager persistenceManager;
	private ViewFactory viewFactory;
	
	@Autowired
	private XFormsDAO xformsDao;
	
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public List<ProcessAllTasksForms> getAllTasksForms(IWContext iwc, final Locale locale) {
		
		return getIdegaJbpmContext().execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) throws JbpmException {
				List<ProcessDefinition> defs = context.getGraphSession().findLatestProcessDefinitions();
				HashSet<Long> defsIds = new HashSet<Long>(defs.size());
				
				for (ProcessDefinition def : defs)
					defsIds.add(def.getId());
				
				Multimap<Long, TaskView> pdsViews = getViewFactory().getAllViewsByProcessDefinitions(defsIds);
				ArrayList<ProcessAllTasksForms> allForms = new ArrayList<ProcessAllTasksForms>(pdsViews.keySet().size());
				
				for (Long pdId : pdsViews.keySet()) {
					
					ProcessDefinition pd;
					Collection<TaskView> tviews = pdsViews.get(pdId);
					
					if(tviews.isEmpty()) {
						pd = context.getGraphSession().getProcessDefinition(pdId);
					} else {
						
						 pd = tviews.iterator().next().getTask().getProcessDefinition();
					 }
					
					ProcessAllTasksForms processForms = new ProcessAllTasksForms();
					processForms.setProcessId(String.valueOf(pd.getId()));
					processForms.setProcessName(pd.getName());
					processForms.setProcessVersion(pd.getVersion());
					
					ArrayList<TaskForm> taskForms = new ArrayList<TaskForm>(tviews.size());
					
					for (TaskView taskView : tviews) {
						
						String dateCreatedStr = new IWTimestamp(taskView.getDateCreated()).getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT);
						TaskForm form = new TaskForm();
						form.setDateCreatedStr(dateCreatedStr);
						form.setFormId(taskView.getViewId());
						form.setFormName(taskView.getDefaultDisplayName());
						form.setTaskName(taskView.getTask().getName());
						taskForms.add(form);
					}
					
					processForms.setTasksCount(String.valueOf(pd.getTaskMgmtDefinition().getTasks().size()));
					processForms.setTaskForms(taskForms);
					allForms.add(processForms);
				}
				
				return allForms;
			}
		});
	}
	
	@Transactional(readOnly = true)
	public ProcessAllTasksForms getTasksFormsForProcess(final Locale locale, final String processName, final Integer version) {
		return getIdegaJbpmContext().execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) throws JbpmException {
				ProcessDefinition pd = context.getGraphSession().findProcessDefinition(processName, version);
				
				if (pd == null) {
					return null;
				}
				
				Long pdId = pd.getId();
				
				Multimap<Long, TaskView> pdsViews = getViewFactory().getAllViewsByProcessDefinitions(Collections.singletonList(pdId));
				
				Collection<TaskView> tviews = pdsViews.get(pdId);
							
				ProcessAllTasksForms processForms = new ProcessAllTasksForms();
				processForms.setProcessId(String.valueOf(pd.getId()));
				processForms.setProcessName(pd.getName());
				processForms.setProcessVersion(pd.getVersion());
					
				ArrayList<TaskForm> taskForms = new ArrayList<TaskForm>(tviews.size());
					
				for (TaskView taskView : tviews) {
						
					String dateCreatedStr = new IWTimestamp(taskView.getDateCreated()).getLocaleDateAndTime(locale, IWTimestamp.SHORT, IWTimestamp.SHORT);
					TaskForm form = new TaskForm();
					form.setDateCreatedStr(dateCreatedStr);
					form.setFormId(taskView.getViewId());
					form.setFormName(taskView.getDefaultDisplayName());
					form.setTaskName(taskView.getTask().getName());
					taskForms.add(form);
				}
				processForms.setTasksCount(String.valueOf(pd.getTaskMgmtDefinition().getTasks().size()));
				processForms.setTaskForms(taskForms);
				return processForms;
				}
		});
	}
	
	@Transactional(readOnly = true)
	public List<XForm> getRelatedByFormId(Long formId) {
		return getXformsDao().getAllVersionsByParentId(formId);
	}
	
	public List<Form> getStandaloneForms() {
		
		return getPersistenceManager().getStandaloneForms();
	}
	
	public BPMContext getIdegaJbpmContext() {
		return idegaJbpmContext;
	}

	@Autowired
	public void setIdegaJbpmContext(BPMContext idegaJbpmContext) {
		this.idegaJbpmContext = idegaJbpmContext;
	}
	
	public XFormsDAO getXformsDao() {
		return xformsDao;
	}

	public void setXformsDao(XFormsDAO xformsDao) {
		this.xformsDao = xformsDao;
	}

	public class ProcessAllTasksForms {
		
		private String processId;
		private String processName;
		private String tasksCount;
		private int processVersion;
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
		public int getProcessVersion() {
			return processVersion;
		}
		public void setProcessVersion(int processVersion) {
			this.processVersion = processVersion;
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
	public void setViewFactory(@ViewFactoryType("xforms") ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}
}