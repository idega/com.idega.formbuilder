package com.idega.formbuilder.util;

import java.util.Date;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.IWBundleStarter;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;

/**
 * <p>
 * This is a class with various utility methods when working with JSF.
 * </p>
 *
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBUtil {
	
	private static String bundle_identifier = "com.idega.formbuilder";
	
	private FBUtil() { }
	
	public static String getBundleIdentifier() {
		return bundle_identifier;
	}
	
	public static IWBundle getBundle(){
		return getBundle(FacesContext.getCurrentInstance());
	}

	public static IWBundle getBundle(FacesContext context){
		return IWContext.getIWContext(context).getIWMainApplication().getBundle(getBundleIdentifier());
	}
	
	public static IWResourceBundle getResourceBundle(FacesContext context){
		return getBundle(context).getResourceBundle(context.getExternalContext().getRequestLocale());
	}
	
	public static IWResourceBundle getResourceBundle(){
		return getResourceBundle(FacesContext.getCurrentInstance());
	}
	
	public static String getResourceAbsolutePath(IWMainApplication iwma, String url) {
		IWBundle iwb = iwma.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
        return iwb.getRealPathWithFileNameString(url);
	}
	
	public static String generateFormId(String name) {
		String result = "";
		result = name.replace(' ', '_');
		result += "-[" + new Date() + "]";
		return result;
	}
}