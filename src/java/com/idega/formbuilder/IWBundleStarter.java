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
	public static final String TEMPORAL_COMPONENTS_XML_STYLESHEET_PATH = "TEMPORAL_COMPONENTS_XML_STYLESHEET_PATH";
	public static final String FINAL_COMPONENTS_XML_STYLESHEET_PATH = "FINAL_COMPONENTS_XML_STYLESHEET_PATH";
	public static final String COMPONENTS_XFORMS_CONTEXT_PATH = "COMPONENTS_XFORMS_CONTEXT_PATH";
	public static final String COMPONENTS_XSD_CONTEXT_PATH = "COMPONENTS_XSD_CONTEXT_PATH";
	public static final String FORM_XFORMS_TEMPLATE_RESOURCES_PATH = "FORM_XFORMS_TEMPLATE_CONTEXT_PATH";
	public static final String bundle_path_start = "bundle://" + IW_BUNDLE_IDENTIFIER+"/";
	
	
	
	public void start(IWBundle starterBundle) {
		FormbuilderViewManager cViewManager = FormbuilderViewManager.getInstance(starterBundle.getApplication());
		cViewManager.initializeStandardNodes(starterBundle);
		GlobalIncludeManager.getInstance().addBundleStyleSheet(IW_BUNDLE_IDENTIFIER, "/style/formbuilder.css");
		
		IWMainApplication iw_app = starterBundle.getApplication();
		
		iw_app.setAttribute(TEMPORAL_COMPONENTS_XML_STYLESHEET_PATH, "resources/xslt/htmlxml.xsl");
		iw_app.setAttribute(FINAL_COMPONENTS_XML_STYLESHEET_PATH, "resources/xslt/components.xsl");
		iw_app.setAttribute(FORM_XFORMS_TEMPLATE_RESOURCES_PATH, "resources/templates/form-template.xhtml");
		
//		temporary local path, should be webdav context path
		iw_app.setAttribute(COMPONENTS_XFORMS_CONTEXT_PATH, "resources/templates/myComponents.xhtml");
		iw_app.setAttribute(COMPONENTS_XSD_CONTEXT_PATH, "resources/templates/default-components.xsd");
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		// TODO Auto-generated method stub
		
	}
	
}
