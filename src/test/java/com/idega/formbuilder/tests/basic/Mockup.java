package com.idega.formbuilder.tests.basic;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.node.TaskNode;
import org.jbpm.taskmgmt.def.Task;

import com.idega.formbuilder.business.process.XFormsSubmissionHandler;
import com.idega.formbuilder.business.process.XFormsToTask;
import com.idega.jbpm.def.DefaultViewImpl;
import com.idega.jbpm.def.ViewToTask;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/17 13:31:13 $ by $Author: civilis $
 *
 */
public class Mockup {
	
	private JbpmConfiguration cfg;
	private XFormsToTask x2t;
	private XFormsSubmissionHandler subHandler;
	
	public void setJbpmConfiguration(JbpmConfiguration cfg) {
		this.cfg = cfg;
	}
	
	public JbpmConfiguration getJbpmConfiguration() {
		return cfg;
	}
	
	public void doStuff() {
		
		System.out.println("do stuff");
		JbpmContext ctx = getJbpmConfiguration().createJbpmContext();
		
		try {
			
			ProcessDefinition pd = ctx.getGraphSession().findLatestProcessDefinition("oneTask");
			
			TaskNode taskNode = (TaskNode)pd.getNode("task");
			
			DefaultViewImpl view = new DefaultViewImpl();
			view.setViewId("XForms view id");
			
			System.out.println("wtf: "+getX2t());
			System.out.println("wtf2: "+taskNode);
			System.out.println("wtf3: "+taskNode.getTasks());
			
			getX2t().bind(view, (Task)taskNode.getTasks().iterator().next());
			
			org.hibernate.Session ses = ctx.getSessionFactory().openSession();
			
			System.out.println("doing stuff.."+ses.getClass().getName());
			
		} finally {
			ctx.close();
		}
	}

	public XFormsToTask getX2t() {
		return x2t;
	}

	public void setX2t(XFormsToTask x2t) {
		this.x2t = x2t;
	}

	public XFormsSubmissionHandler getSubHandler() {
		return subHandler;
	}

	public void setSubHandler(XFormsSubmissionHandler subHandler) {
		this.subHandler = subHandler;
	}
}