package com.idega.formbuilder.presentation.actions;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class ShowFormPropertiesAction implements ActionListener {
	
	public void processAction(ActionEvent ae) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("2");
	}

}
