package com.idega.formbuilder;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.formbuilder";
	
	public void start(IWBundle starterBundle) {
		FormbuilderViewManager cViewManager = FormbuilderViewManager.getInstance(starterBundle.getApplication());
		cViewManager.initializeStandardNodes(starterBundle);
		GlobalIncludeManager.getInstance().addBundleStyleSheet(IW_BUNDLE_IDENTIFIER, "/style/formbuilder.css");
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		// TODO Auto-generated method stub
		
	}
	
}
