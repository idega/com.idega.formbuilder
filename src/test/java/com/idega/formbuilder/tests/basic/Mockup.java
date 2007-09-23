package com.idega.formbuilder.tests.basic;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.idega.jbpm.def.ViewToTask;
import com.idega.jbpm.exe.SubmissionHandler;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/09/23 06:58:59 $ by $Author: civilis $
 *
 */
public class Mockup {
	
	private JbpmConfiguration cfg;
	private ViewToTask x2t;
	private SubmissionHandler subHandler;
	
	public void setJbpmConfiguration(JbpmConfiguration cfg) {
		this.cfg = cfg;
	}
	
	public JbpmConfiguration getJbpmConfiguration() {
		return cfg;
	}
	
	public void doStuff() {
		
		testBasicComplaintScenario();
	}
	
	public void testBasicComplaintScenario() {
		
		userChoseToInitiateBasicComplaintProcess();
		long piid = userTakesABasicComplaintTask();
		managerApprovesComplaintTask(piid);
	}
	
	public void userChoseToInitiateBasicComplaintProcess() {
		
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().findLatestProcessDefinition("basicComplaintApproval");
			ProcessInstance pi = pd.createProcessInstance();
			pi.signal();
			ctx.save(pi);
			
		} finally {
			ctx.close();
		}
	}
	
	public long userTakesABasicComplaintTask() {
		
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().findLatestProcessDefinition("basicComplaintApproval");
			
			System.out.println("taking task: "+pd);
			List<ProcessInstance> pis = ctx.getGraphSession().findProcessInstances(pd.getId());
			ProcessInstance pi = pis.iterator().next();
			
			Collection<TaskInstance> tis = pi.getTaskMgmtInstance().getUnfinishedTasks(pi.getRootToken());
			
			TaskInstance ti = tis.iterator().next();
			
			System.out.println("is open? "+ti.isOpen());
			System.out.println("start? "+ti.getStart());
			
			if(ti.getStart() == null)
				ti.start();

//			get a form here (not bound now)
//			getX2t().getView(ti.getTask().getId())
//			pass process parameters to the form (TaskInstance id)
			
//			this goes in the xforms submission handler
//			let's say we got submission values
			
			Object o = submissionValues("/Users/civilis/dev/workspace/eplatform-4/com.idega.formbuilder/src/test/java/com/idega/formbuilder/tests/basic/userTakesABasicComplaintTaskSubmission.xml");
			
			try {
				//get task instance id from xforms submission element
				getSubHandler().submit(ti, o);
				System.out.println("finished");
				ti.end();
				ctx.save(ti);
				
			} catch (Exception e) {
				//TODO: msg user, that something went just wrong ...
				e.printStackTrace();
			}
			
			return pi.getId();

			
		} finally {
			ctx.close();
		}
	}
	
	public void managerApprovesComplaintTask(long piid) {
		
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		
		try {
			ProcessInstance pi = ctx.getProcessInstance(piid);
			
			Collection<TaskInstance> tis = pi.getTaskMgmtInstance().getUnfinishedTasks(pi.getRootToken());
			
			TaskInstance ti = tis.iterator().next();
			
			if(ti.getStart() == null)
				ti.start();
			
			System.out.println("manager working on: "+ti);

//			get a form here (not bound now)
//			getX2t().getView(ti.getTask().getId())
//			pass process parameters to the form (TaskInstance id)
			
//			this goes in the xforms submission handler
//			let's say we got submission values
			
			Object o = submissionValues("/Users/civilis/dev/workspace/eplatform-4/com.idega.formbuilder/src/test/java/com/idega/formbuilder/tests/basic/managerApprovesComplaintTaskSubmission.xml");
			
			try {
				//get task instance id from xforms submission element
				getSubHandler().submit(ti, o);
				System.out.println("manager finished");
				System.out.println("vars: "+ti.getVariables());
				ti.end();
				ctx.save(ti);
				
			} catch (Exception e) {
				//TODO: msg user, that something went just wrong ...
				e.printStackTrace();
			}
			
		} finally {
			ctx.close();
		}
	}
	
	public Object submissionValues(String path) {
	
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			return dbFactory.newDocumentBuilder().parse(new File(path));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ViewToTask getX2t() {
		return x2t;
	}

	public void setX2t(ViewToTask x2t) {
		this.x2t = x2t;
	}

	public SubmissionHandler getSubHandler() {
		return subHandler;
	}

	public void setSubHandler(SubmissionHandler subHandler) {
		this.subHandler = subHandler;
	}
}