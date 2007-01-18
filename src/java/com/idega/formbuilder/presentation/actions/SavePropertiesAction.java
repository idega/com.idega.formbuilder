package com.idega.formbuilder.presentation.actions;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.webface.WFUtil;

public class SavePropertiesAction implements ActionListener, Serializable {
	
	private static final long serialVersionUID = -1462694687346788168L;
	
	String temp;
	
	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public SavePropertiesAction() {
		temp = "sampletext";
	}

	public void processAction(ActionEvent ae) {
		System.out.println("SavePropertiesActionlistener");
		System.out.println(ae.getComponent().getId());
		String temp = ae.getComponent().getId();
		temp.split("asd");
	}
	
	public void saveFormTitle(ActionEvent ae) {
		System.out.println(ae.getComponent().getClientId(FacesContext.getCurrentInstance()));
		((FormDocument) WFUtil.getBeanInstance("formDocument")).setFormTitle(ae.getComponent().getId());

	}

}
