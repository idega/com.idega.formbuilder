package com.idega.formbuilder.business.process;

import org.jbpm.taskmgmt.def.Task;

import com.idega.jbpm.def.View;
import com.idega.jbpm.def.ViewToTask;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/14 02:59:41 $ by $Author: civilis $
 */
public class XFormsToTask implements ViewToTask {
	
	public static String IDENTIFIER = "XFormsToTask";

	public void bind(View view, Task task) {

		//task.getProcessDefinition()
		System.out.println("task name: "+task.getName());
		System.out.println("pd name: "+task.getProcessDefinition().getName());
//		view.getViewId()
		
//		TODO: persist: view id (xform id) + pd name + task name
	}
	public View getView(Task task) {
		
//		TODO: retrieve from db viewid
		
		return null;
	}
	public String getIdentifier() {
	
		return IDENTIFIER;
	}
}