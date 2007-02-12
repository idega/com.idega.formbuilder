package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.DocumentManagerFactory;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	private static DocumentManager formManagerInstance = null;
	
	public static DocumentManager getDocumentManagerInstance() {
		if(formManagerInstance == null) {
			return createNewInstance();
		} else {
			return formManagerInstance;
		}
	}
	
	private static DocumentManager createNewInstance() {
		try {
			formManagerInstance = DocumentManagerFactory.newFormManager(FacesContext.getCurrentInstance());
		} catch(InitializationException ie) {
			ie.printStackTrace();
		}
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
		return formManagerInstance;
	}
}