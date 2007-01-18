package com.idega.formbuilder.presentation.actions;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.webface.WFUtil;

public class UpdateTitleAction {

	public void saveFormTitle(ActionEvent ae) {
		System.out.println(ae.getComponent().getClientId(FacesContext.getCurrentInstance()));
		((FormDocument) WFUtil.getBeanInstance("formDocument")).setFormTitle(ae.getComponent().getId());

	}

}
