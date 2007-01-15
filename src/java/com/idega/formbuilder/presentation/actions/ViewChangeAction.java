package com.idega.formbuilder.presentation.actions;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class ViewChangeAction implements ActionListener {

	public void processAction(ActionEvent ae) {
		boolean open = openForm();
		String buttonId = ae.getComponent().getClientId(FacesContext.getCurrentInstance());
		if(buttonId.endsWith(":designViewTab")) {
			setView("design");
			if(open) {
				setRenderedMenu(true);
			} else {
				setRenderedMenu(false);
			}
		} else if(buttonId.endsWith(":previewViewTab")) {
			setView("preview");
			setRenderedMenu(false);
		} else if(buttonId.endsWith(":sourceViewTab")) {
			setView("source");
			setRenderedMenu(false);
		}
		
	}
	
	private boolean openForm() {
		String id = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getFormTitle();
		if(id != null && !id.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setView(String view) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setView(view);
	}
	
	private void setRenderedMenu(boolean rendered) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setRenderedMenu(rendered);
	}

}
