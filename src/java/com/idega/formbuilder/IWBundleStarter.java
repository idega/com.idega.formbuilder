package com.idega.formbuilder;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.include.GlobalIncludeManager;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.formbuilder";
	public static final String COMPONENTS_STYLESHEET_PATH = "COMPONENTS_STYLESHEET_PATH";
	public static final String COMPONENTS_XFORMS_STYLESHEET_PATH = "COMPONENTS_XFORMS_STYLESHEET_PATH";
	private static final String bundle_path_start = "bundle://" + IW_BUNDLE_IDENTIFIER;
	
	public void start(IWBundle starterBundle) {
		FormbuilderViewManager cViewManager = FormbuilderViewManager.getInstance(starterBundle.getApplication());
		cViewManager.initializeStandardNodes(starterBundle);
		GlobalIncludeManager.getInstance().addBundleStyleSheet(IW_BUNDLE_IDENTIFIER, "/style/formbuilder.css");
		
		IWMainApplication iw_app = starterBundle.getApplication();
		
		iw_app.setAttribute(COMPONENTS_STYLESHEET_PATH, bundle_path_start+"/resources/xslt/components.xsl");
		iw_app.setAttribute(COMPONENTS_XFORMS_STYLESHEET_PATH, bundle_path_start+"/resources/xslt/htmlxml.xsl");
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		// TODO Auto-generated method stub
		
	}
	
}
