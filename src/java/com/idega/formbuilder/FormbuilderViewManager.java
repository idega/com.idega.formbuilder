package com.idega.formbuilder;

import java.util.ArrayList;
import java.util.Collection;
import javax.faces.context.FacesContext;
import com.idega.core.accesscontrol.business.StandardRoles;
import com.idega.core.view.ApplicationViewNode;
import com.idega.core.view.DefaultViewNode;
import com.idega.core.view.KeyboardShortcut;
import com.idega.core.view.ViewManager;
import com.idega.core.view.ViewNode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.repository.data.Singleton;

public class FormbuilderViewManager implements Singleton  {

	private static final String IW_FORMBUILDER_VIEW_MANAGER_KEY = "iw_formbuilderviewmanager";
	private static final String FORMBUILDER_ID = "formbuilder";
	
	public static final String FORM_MANAGER_INSTANCE = "FORM_MANAGER_INSTANCE";
	public static final String FORMBUILDER_DESIGNVIEW_STATUS = "FORMBUILDER_DESIGNVIEW_STATUS";
	public static final String FORMBUILDER_CURRENT_FORM_ID = "FORMBUILDER_CURRENT_FORM_ID";
	public static final String FORMBUILDER_CURRENT_LOCALE = "FORMBUILDER_CURRENT_LOCALE";
	
	private ViewNode rootNode;
	private IWMainApplication iwma;
	
	private FormbuilderViewManager(IWMainApplication iwma){
		
		this.iwma = iwma;
	}

	public static synchronized FormbuilderViewManager getInstance(IWMainApplication iwma){
		  
		  FormbuilderViewManager formbuilder_view_manager = (FormbuilderViewManager) iwma.getAttribute(IW_FORMBUILDER_VIEW_MANAGER_KEY);
	    if(formbuilder_view_manager==null){
	      formbuilder_view_manager = new FormbuilderViewManager(iwma);
	      //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE, "en");
	      //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_FORM_ID, null);
	      iwma.setAttribute(IW_FORMBUILDER_VIEW_MANAGER_KEY,formbuilder_view_manager);
	    }
	    return formbuilder_view_manager;
	}	
	
	public static FormbuilderViewManager getInstance(FacesContext context){
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(context);
		
		return getInstance(iwma);
	}
	
	public ViewManager getViewManager(){
		return ViewManager.getInstance(this.iwma);
	}
	
	
	public ViewNode getContentNode(){
		IWBundle iwb = this.iwma.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
		//ViewNode content = root.getChild(CONTENT_ID);
		if(this.rootNode==null){
			this.rootNode = initalizeContentNode(iwb);
		}
		return this.rootNode;
	}
	
	public ViewNode initalizeContentNode(IWBundle contentBundle){
		
		ViewNode root = getViewManager().getWorkspaceRoot();
		DefaultViewNode node = new ApplicationViewNode(FORMBUILDER_ID,root);
		Collection<String> roles = new ArrayList<String>();
		roles.add(StandardRoles.ROLE_KEY_BUILDER);
		node.setAuthorizedRoles(roles);
		
		node.setJspUri(contentBundle.getJSPURI("formbuilder.jsp"));
		node.setKeyboardShortcut(new KeyboardShortcut("4"));
		
		this.rootNode = node;
		return this.rootNode;
	}
	
	
	public void initializeStandardNodes(IWBundle bundle){
		initalizeContentNode(bundle);
	}
}
