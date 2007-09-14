package com.idega.formbuilder.presentation.beans;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;

import com.idega.business.SpringBeanName;

@SpringBeanName("processLogic")
public class ProcessLogic {
	
	public void getDeployedProcesses() {
		JbpmConfiguration cfg = JbpmConfiguration.getInstance();
		JbpmContext ctx = cfg.createJbpmContext();
		
	}

}
