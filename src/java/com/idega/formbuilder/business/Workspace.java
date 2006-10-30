package com.idega.formbuilder.business;

import java.io.Serializable;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String view;

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
