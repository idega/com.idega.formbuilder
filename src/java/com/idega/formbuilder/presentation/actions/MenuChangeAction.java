package com.idega.formbuilder.presentation.actions;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class MenuChangeAction implements ActionListener {
	
	public void processAction(ActionEvent ae) {
		String senderId = ae.getComponent().getId();
		String menuPanelId = senderId.substring(0, 4);
		if(menuPanelId.equals("tab1")) {
			setSelectedMenu("0");
		} else if(menuPanelId.equals("tab2")) {
			setSelectedMenu("1");
		} else if(menuPanelId.equals("tab3")) {
			setSelectedMenu("2");
		} else if(menuPanelId.equals("tab4")) {
			setSelectedMenu("3");
		}
	}
	
	private void setSelectedMenu(String selectedMenu) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu(selectedMenu);
	}

}
