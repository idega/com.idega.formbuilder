package com.idega.formbuilder.util;

import java.util.Date;
import java.util.Locale;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWSlideServiceBean;
import com.idega.webface.WFUtil;

/**
 * <p>
 * This is a class with various utility methods when working with JSF.
 * </p>
 *
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 */
public class FBUtil {
	
	private FBUtil() { }
	
	public static IWBundle getBundle(){
		return getBundle(FacesContext.getCurrentInstance());
	}

	public static IWBundle getBundle(FacesContext context){
		return IWContext.getIWContext(context).getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
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
	
	/**
	 *
	 * @deprecated - use generateFormId from FormsService 
	 * 
	 */
	@Deprecated
	public static String generateFormId(String name) {
		
		String result = name+"-"+ new Date();
		return result.replace(' ', '_').replace(':', '_');
	}
	
	private static String webdav_server_url;
	
	public static String getWebdavServerUrl(FacesContext ctx) {
		
		if(webdav_server_url == null)
			webdav_server_url = new IWSlideServiceBean().getWebdavServerURL().toString();
		
		return webdav_server_url;
	}

	public static Locale getUILocale() {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		Locale locale = workspace.getLocale();
		if(locale == null) {
			locale = new Locale(FBConstants.FORMBUILDER_DEFAULT_LOCALE);
		}
		return locale;
	}
}