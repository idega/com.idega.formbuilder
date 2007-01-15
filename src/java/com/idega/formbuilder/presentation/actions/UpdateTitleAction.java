package com.idega.formbuilder.presentation.actions;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class UpdateTitleAction implements ActionListener {

	public void processAction(ActionEvent ae) {
		System.out.println(ae.getComponent().getClientId(FacesContext.getCurrentInstance()));
//		WFUtil.getBeanInstance("formDocument")

	}

}
