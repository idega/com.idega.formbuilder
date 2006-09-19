package com.idega.formbuilder.business.form;

public class WebdavSaveException extends Exception {

	private static final long serialVersionUID = 8364457644175982801L;

	public WebdavSaveException(String message) {
		super("WebdavSaveException: " + message);
	}
	
	public WebdavSaveException(String message, Throwable e) {
		super("WebdavSaveException: " + message, e);
	}
	
	public WebdavSaveException(Throwable e) {
		super("WebdavSaveException", e);
	}
}
