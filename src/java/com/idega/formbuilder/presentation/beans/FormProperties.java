package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class FormProperties implements Serializable, ActionListener {
	
	private static final long serialVersionUID = -1462694112345488168L;
	
	private String formTitle;

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}
	
	public void processAction(ActionEvent ae) {
		
	}

}
