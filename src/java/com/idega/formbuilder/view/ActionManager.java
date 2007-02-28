package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.block.form.business.FormsService;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.documentmanager.business.DocumentManagerService;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.documentmanager.business.form.manager.util.InitializationException;

// TODO: fix this class from static and global stuff
public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	private static Log logger = LogFactory.getLog(ActionManager.class);
	
	private static DocumentManager formManagerInstance;
	
	public static DocumentManager getDocumentManagerInstance() {
		if(formManagerInstance == null) {
			return createNewInstance();
		} else {
			return formManagerInstance;
		}
	}
	
	private static DocumentManager createNewInstance() {
		try {
			formManagerInstance = getDocumentManagerService().newDocumentManager(FacesContext.getCurrentInstance());
			formManagerInstance.setPersistenceManager(getFormsService());
			
		} catch(InitializationException ie) {
			ie.printStackTrace();
		}
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
		return formManagerInstance;
	}
	
	protected static DocumentManagerService getDocumentManagerService() {
		
		try {
			IWApplicationContext iwc = IWMainApplication.getDefaultIWApplicationContext();
			return (DocumentManagerService) IBOLookup.getServiceInstance(iwc, DocumentManagerService.class);
		} catch (IBOLookupException e) {
			logger.error("Could not find DocumentManagerService", e);
		}
		return null;
	}
	
	protected static FormsService getFormsService() {
		
		try {
			IWApplicationContext iwc = IWMainApplication.getDefaultIWApplicationContext();
			return (FormsService) IBOLookup.getServiceInstance(iwc, FormsService.class);
		} catch (IBOLookupException e) {
			logger.error("Could not find FormsService", e);
		}
		return null;
	}
}