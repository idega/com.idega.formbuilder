package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.FormManagerFactory;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	private static DocumentManager formManagerInstance = null;
	
	public static DocumentManager getDocumentManagerInstance() {
//		FormManager fm;
		if(formManagerInstance == null) {
			/*try {
				formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
			} catch(InitializationException ie) {
				ie.printStackTrace();
			}
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
			return formManagerInstance;*/
			return createNewInstance();
		} else {
			return formManagerInstance;
			/*fm = (FormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
			return fm;*/
		}
	}

	/*public void setFormManagerInstance(DocumentManager formManagerInstance) {
		ActionManager.formManagerInstance = formManagerInstance;
	}*/
	
	private static DocumentManager createNewInstance() {
		try {
			formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
		} catch(InitializationException ie) {
			ie.printStackTrace();
		}
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
		return formManagerInstance;
	}
}