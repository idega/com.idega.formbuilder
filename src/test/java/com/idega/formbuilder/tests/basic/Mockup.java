package com.idega.formbuilder.tests.basic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.w3c.dom.Node;

import com.idega.formbuilder.business.process.XFormsToTask;
import com.idega.jbpm.exe.SubmissionHandler;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/09/21 11:30:29 $ by $Author: civilis $
 *
 */
public class Mockup {
	
	private JbpmConfiguration cfg;
	private XFormsToTask x2t;
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
		
		if(true)
			return;
		userChoseToInitiateBasicComplaintProcess();
		userTakesABasicComplaintTask();
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
	
	public void userTakesABasicComplaintTask() {
		
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		
		try {
			ProcessDefinition pd = ctx.getGraphSession().findLatestProcessDefinition("basicComplaintApproval");
			
			System.out.println("taking task: "+pd);
			List<ProcessInstance> pis = ctx.getGraphSession().findProcessInstances(pd.getId());
			ProcessInstance pi = pis.iterator().next();
			
			Collection<TaskInstance> tis = pi.getTaskMgmtInstance().getUnfinishedTasks(pi.getRootToken());
			
			TaskInstance ti = tis.iterator().next();
			
			System.out.println("is open? "+ti.isOpen());
			if(!ti.isOpen())
				ti.start();

//			get a form here (not bound now)
//			getX2t().getView(ti.getTask().getId())
//			pass process parameters to the form (TaskInstance id)
			
//			this goes in the xforms submission handler
//			let's say we got submission values
			
			Object o = submissionValues();
			
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

			
		} finally {
			ctx.close();
		}
	}
	
	public Object submissionValues() {
	
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			return dbFactory.newDocumentBuilder().parse(new File("/Users/civilis/dev/workspace/eplatform-4/com.idega.formbuilder/src/test/java/com/idega/formbuilder/tests/basic/userTakesABasicComplaintTaskSubmission.xml"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public XFormsToTask getX2t() {
		return x2t;
	}

	public void setX2t(XFormsToTask x2t) {
		this.x2t = x2t;
	}

	public SubmissionHandler getSubHandler() {
		return subHandler;
	}

	public void setSubHandler(SubmissionHandler subHandler) {
		this.subHandler = subHandler;
	}
}