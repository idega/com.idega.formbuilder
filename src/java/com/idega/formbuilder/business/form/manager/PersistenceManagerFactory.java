package com.idega.formbuilder.business.form.manager;


public class PersistenceManagerFactory {
	
	public static IPersistenceManager newPersistenceManager() {
		
//		return SandboxPersistenceManager.getInstance();
		return WebdavPersistenceManager.getInstance();
	}

}