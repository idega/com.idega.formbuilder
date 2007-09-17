package com.idega.formbuilder.business.process;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.def.Task;

import com.idega.jbpm.data.ViewTaskBind;
import com.idega.jbpm.def.View;
import com.idega.jbpm.def.ViewToTask;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/09/17 13:31:13 $ by $Author: civilis $
 */
public class XFormsToTask implements ViewToTask {
	
	private String IDENTIFIER = "XFormsToTask";
	private JbpmConfiguration cfg;
	private SessionFactory sessionFactory;

	public void bind(View view, Task task) {

		JbpmContext ctx = cfg.createJbpmContext();
		
		Session session = null;
		try {
			ViewTaskBind bind = new ViewTaskBind();
			bind.setTaskId(task.getId());
			bind.setViewIdentifier(view.getViewId());
			
			session = getSessionFactory().openSession();
			
			session.save(bind);
			
		} finally {
			
			ctx.close();
			
			if(session != null)
				session.close();
		}
	}
	public View getView(Task task) {
		
//		TODO: retrieve from db viewid
		
		return null;
	}
	public String getIdentifier() {
	
		return IDENTIFIER;
	}
	
	public void setIdentifier(String identifier) {
    	
    	IDENTIFIER = identifier;
    }
	
	public void setJbpmConfiguration(JbpmConfiguration cfg) {
		this.cfg = cfg;
	}
	
	public JbpmConfiguration getJbpmConfiguration() {
		return cfg;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}