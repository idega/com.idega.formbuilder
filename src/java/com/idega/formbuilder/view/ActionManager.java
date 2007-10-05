package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import com.idega.webface.WFUtil;
import com.idega.documentmanager.business.DocumentManager;
import com.idega.documentmanager.business.DocumentManagerFactory;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	public static final String ACTION_MANAGER_MANAGED_BEAN = "actionManager";
	
	private DocumentManager documentManagerInstance;
	private DocumentManagerFactory documentManagerFactory;
	
	public ActionManager() { }
	
	public DocumentManager getDocumentManagerInstance() {
		
		if(documentManagerInstance == null) {
			
			DocumentManager documentManager = getDocumentManagerFactory().newDocumentManager(FacesContext.getCurrentInstance());
			documentManagerInstance = documentManager;
		}
		return documentManagerInstance;
	}
	
	public static ActionManager getCurrentInstance() {
		
		return (ActionManager)WFUtil.getBeanInstance(ACTION_MANAGER_MANAGED_BEAN);
	}
	
	public DocumentManagerFactory getDocumentManagerFactory() {
		return documentManagerFactory;
	}

	public void setDocumentManagerFactory(
			DocumentManagerFactory documentManagerFactory) {
		this.documentManagerFactory = documentManagerFactory;
	}
}