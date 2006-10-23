package com.idega.formbuilder.actions;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.view.ActionManager;

public class NewFormAction implements ActionListener {
	
	public void processAction(ActionEvent ae) {
		try {
			String generatedId = new Long(System.currentTimeMillis()).toString();
			String id = generatedId.substring(generatedId.length() - 8);
			ActionManager.getFormManagerInstance().createFormDocument(id, null);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "EMPTY_FORM");
			//LocalizedStringBean bean = new LocalizedStringBean();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
