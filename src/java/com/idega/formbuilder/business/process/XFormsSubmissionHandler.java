package com.idega.formbuilder.business.process;

import org.jbpm.taskmgmt.exe.TaskInstance;

import com.idega.jbpm.exe.AbstractSubmissionHandler;

import org.w3c.dom.Node;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/09/14 02:59:41 $ by $Author: civilis $
 */
public class XFormsSubmissionHandler extends AbstractSubmissionHandler {
	
	public static String IDENTIFIER = "XFormsSubmissionHandler";

    public String getIdentifier() {
    	
    	return IDENTIFIER;
    }

	@Override
    protected Node unfold(Object obj) {
    	
    	return null;
    }

	@Override
    protected Object fold(Node node, Object obj) {
    	
    	return null;
    }

	@Override
    protected Node getUnfoldedFromTI(TaskInstance taskinstance) {
		
		
		//taskinstance.getTaskMgmtInstance().cre
    	return null;
    }

	@Override
    protected void storeUnfoldedToTI(TaskInstance taskinstance, Node node) {

		
		taskinstance.getContextInstance().createVariable("", taskinstance, taskinstance.getToken());
    }
}