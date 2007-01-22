package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.FormManagerFactory;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	private static IFormManager formManagerInstance = null;
	
	public static IFormManager getFormManagerInstance() {
		if(formManagerInstance == null) {
			try {
				formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
			} catch(InitializationException ie) {
				ie.printStackTrace();
			}
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
			return formManagerInstance;
		} else {
			return (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		}
	}

	public void setFormManagerInstance(IFormManager formManagerInstance) {
		ActionManager.formManagerInstance = formManagerInstance;
	}

}