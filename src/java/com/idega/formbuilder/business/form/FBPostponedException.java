package com.idega.formbuilder.business.form;

public class FBPostponedException extends Exception {

	private static final long serialVersionUID = 8364457644175982801L;

	public FBPostponedException(String message) {
		super("FBPostponedException: " + message);
	}
	
	public FBPostponedException(String message, Throwable e) {
		super("FBPostponedException: " + message, e);
	}
	
	public FBPostponedException(Throwable e) {
		super("FBPostponedException", e);
	}
}
