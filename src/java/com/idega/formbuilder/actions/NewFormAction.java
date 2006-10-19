package com.idega.formbuilder.actions;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.view.ActionManager;

public class NewFormAction implements ActionListener {
	public void processAction(ActionEvent ae) {
		try {
			ActionManager.getFormManagerInstance().createFormDocument("Form", null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
