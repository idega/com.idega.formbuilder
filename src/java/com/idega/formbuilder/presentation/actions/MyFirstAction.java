package com.idega.formbuilder.presentation.actions;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class MyFirstAction implements ActionListener {

	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		String temp = arg0.getComponent().getId();
		System.out.println(temp);
	}

}
