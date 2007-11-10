package com.idega.formbuilder.presentation.beans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.block.form.process.XFormsToTask;
import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.DocumentManager;
import com.idega.documentmanager.business.FormLockException;
import com.idega.documentmanager.business.component.Page;
import com.idega.formbuilder.presentation.components.FBAddTaskForm;
import com.idega.formbuilder.presentation.components.FBDatatypeVariables;
import com.idega.formbuilder.util.FBConstants;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class TaskFormDocument extends FormDocument {
	
	private static final long serialVersionUID = -1462694112789788168L;
	
	private static Log logger = LogFactory.getLog(TaskFormDocument.class);
	
	public static final String BEAN_ID = "taskFormDocument";
	
	private String taskName;
	private long taskId;
	private String processName;
	private long processId;
	
	private JbpmProcessBusinessBean jbpmProcessBusiness;
	private XFormsToTask viewToTaskBinder;
	private InstanceManager instanceManager;
	
	@SuppressWarnings("unchecked")
	public boolean loadTaskFormDocument(String processName, long processId, String taskName, String formId) {
		
		if(processName == null || taskName == null || formId == null)
			return false;
		
		clearAppsRelatedMetaData();
		
		try {
			getWorkspace().setProcessMode(true);
				
			DocumentManager formManagerInstance = instanceManager.getDocumentManagerInstance();
			setDocument(formManagerInstance.openForm(formId));
			CoreUtil.getIWContext().getExternalContext().getSessionMap().put(FBConstants.FORM_DOCUMENT_ID, formId);
//				if(getFormId() != null)
//					getFormsService().unlockForm(getFormId());
				
			String firstPage = getCommonPagesIdList().get(0);
			Page firstP = getDocument().getPage(firstPage);
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			formPage.initializeBeanInstance(firstP);
				
			getWorkspace().setView("design");
			initializeBeanInstance(getDocument(), processName, processId, taskName);
		} catch (FormLockException e) {
			// TODO: inform about lock
			logger.info("Form was locked when tried to open it", e);
			return false;
		} catch(Exception e) {
			logger.info("Exception while trying to open a form document", e);
			return false;
		}
		return true;
	}
	
	public org.jdom.Document addNewVariable(String name, String datatype) {
		jbpmProcessBusiness.addTaskVariable(new Long(processId).toString(), taskName, datatype, name);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDatatypeVariables(datatype), true);
	}
	
	public org.jdom.Document getRenderedAddTaskFormComponent(String processId, String taskName, String formName, boolean idle) {
		if(idle) {
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBAddTaskForm("idle"), true);
		} else {
			if(processId == null) 
				return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBAddTaskForm("idle"), true);
			
			this.processId = new Long(processId).longValue();
			if(formName != null && !formName.equals("")) {
//				this.formName = formName;
				return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBAddTaskForm("idle"), true);
			} else if(taskName != null && !taskName.equals("")) {
				this.taskName = taskName;
				return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBAddTaskForm("name"), true);
			} else {
				return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBAddTaskForm("task"), true);
			}
		}
	}
	
	public Document initializeBeanInstance(Document document, String processName, long processId, String taskName) {
		super.initializeBeanInstance(document);
		Long taskId = viewToTaskBinder.getTask(getFormId());
		this.taskId = taskId.longValue();
		this.processName = processName;
		this.taskName = taskName;
		this.processId = processId;
		return document;
	}
	
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public JbpmProcessBusinessBean getJbpmProcessBusiness() {
		return jbpmProcessBusiness;
	}

	public void setJbpmProcessBusiness(JbpmProcessBusinessBean jbpmProcessBusiness) {
		this.jbpmProcessBusiness = jbpmProcessBusiness;
	}

	public XFormsToTask getViewToTaskBinder() {
		return viewToTaskBinder;
	}

	public void setViewToTaskBinder(XFormsToTask viewToTaskBinder) {
		this.viewToTaskBinder = viewToTaskBinder;
	}

	public InstanceManager getInstanceManager() {
		return instanceManager;
	}

	public void setInstanceManager(InstanceManager instanceManager) {
		this.instanceManager = instanceManager;
	}

	public long getProcessId() {
		return processId;
	}

	public void setProcessId(long processId) {
		this.processId = processId;
	}

}
