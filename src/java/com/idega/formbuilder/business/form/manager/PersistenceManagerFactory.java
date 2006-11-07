package com.idega.formbuilder.business.form.manager;

import com.idega.formbuilder.sandbox.SandboxPersistenceManager;

public class PersistenceManagerFactory {
	
	public static IPersistenceManager newPersistenceManager() {
		
		return SandboxPersistenceManager.getInstance();
		//return WebdavPersistenceManager.getInstance();
	}

}