package com.idega.formbuilder.presentation.actions;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class SavePropertiesAction implements ActionListener, Serializable {
	
	private static final long serialVersionUID = -1462694119586709168L;
	
	private String test;
	
	public SavePropertiesAction() {
		this.test = "sample text";
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public void processAction(ActionEvent ae) {
		System.out.println("SavePropertiesActionlistener");
		System.out.println(ae.getComponent().getId());
		String temp = ae.getComponent().getId();
		temp.split("asd");
	}
	
	public void saveProperties() {
		System.out.println("SavePropertiesAction");
	}

}
