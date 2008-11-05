package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.xformsmanager.business.DocumentManager;
import com.idega.xformsmanager.business.DocumentManagerFactory;
import com.idega.idegaweb.IWMainApplication;
import com.idega.webface.WFUtil;

public class InstanceManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	public static final String BEAN_ID = "actionManager";
	
	private DocumentManager documentManagerInstance;
	private DocumentManagerFactory documentManagerFactory;
	
	public DocumentManager getDocumentManagerInstance() {
		
		if(documentManagerInstance == null) {
			
			DocumentManager documentManager = getDocumentManagerFactory().newDocumentManager(IWMainApplication.getIWMainApplication(FacesContext.getCurrentInstance()));
			documentManagerInstance = documentManager;
		}
		return documentManagerInstance;
	}
	
	public static InstanceManager getCurrentInstance() {
		
		return (InstanceManager)WFUtil.getBeanInstance(BEAN_ID);
	}
	
	public DocumentManagerFactory getDocumentManagerFactory() {
		return documentManagerFactory;
	}

	@Autowired
	public void setDocumentManagerFactory(
			DocumentManagerFactory documentManagerFactory) {
		this.documentManagerFactory = documentManagerFactory;
	}
}