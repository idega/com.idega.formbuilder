package com.idega.formbuilder.business.form.manager;

public class PersistenceManagerFactory {
	
	public static IPersistenceManager newPersistenceManager() {
		
		return WebdavPersistenceManager.getInstance();
	}

}