package com.idega.formbuilder.business.process;

import org.jbpm.JbpmConfiguration;
import org.jbpm.taskmgmt.exe.TaskInstance;

import com.idega.jbpm.exe.AbstractSubmissionHandler;

import org.w3c.dom.Node;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/09/17 13:31:13 $ by $Author: civilis $
 */
public class XFormsSubmissionHandler extends AbstractSubmissionHandler {
	
	private String IDENTIFIER = "XFormsSubmissionHandler";
	private JbpmConfiguration cfg;

    public String getIdentifier() {
    	
    	return IDENTIFIER;
    }
    
    public void setIdentifier(String identifier) {
    	
    	IDENTIFIER = identifier;
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
	
	public void setJbpmConfiguration(JbpmConfiguration cfg) {
		this.cfg = cfg;
	}
	
	public JbpmConfiguration getJbpmConfiguration() {
		return cfg;
	}
}