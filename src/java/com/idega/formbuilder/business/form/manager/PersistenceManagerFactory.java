package com.idega.formbuilder.business.form.manager;

import com.idega.formbuilder.sandbox.SandboxPersistenceManager;

public class PersistenceManagerFactory {
	
	public static IPersistenceManager newPersistenceManager() {
		
//		WebdavPersistenceManager webdav_manager = WebdavPersistenceManager.getInstance();
//		webdav_manager.setFormsPath("");
		
		return SandboxPersistenceManager.getInstance();
//		return WebdavPersistenceManager.getInstance();
	}

}