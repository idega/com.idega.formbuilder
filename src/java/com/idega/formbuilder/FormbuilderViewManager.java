package com.idega.formbuilder;

import java.util.ArrayList;
import java.util.Collection;

import com.idega.core.accesscontrol.business.StandardRoles;
import com.idega.core.view.DefaultViewNode;
import com.idega.core.view.KeyboardShortcut;
import com.idega.core.view.ViewManager;
import com.idega.core.view.ViewNode;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.workspace.view.WorkspaceApplicationNode;

public class FormbuilderViewManager {

	private ViewNode formbuilderNode;
	private IWMainApplication iwma;
	
	public static FormbuilderViewManager getInstance(IWMainApplication iwma) {
		FormbuilderViewManager instance = (FormbuilderViewManager) iwma.getAttribute("formbuilderviewmanager");
		if(instance==null){
			instance = new FormbuilderViewManager();
			instance.iwma=iwma;
			iwma.setAttribute("formbuilderviewmanager",instance);
		}
		return instance;
	}
	
	public ViewManager getViewManager(){
		return ViewManager.getInstance(this.iwma);
	}
	
	
	public ViewNode getFormbuilderViewNode(){
		IWBundle iwb = this.iwma.getBundle("com.idega.formbuilder");
		if(this.formbuilderNode == null){
			this.formbuilderNode = initalizeFormbuilderNode(iwb);
		}
		return this.formbuilderNode;
	}

	private ViewNode initalizeFormbuilderNode(IWBundle iwb) {
		ViewManager viewManager = ViewManager.getInstance(this.iwma);
		ViewNode workspace = viewManager.getWorkspaceRoot();
		
		Collection<String> roles = new ArrayList<String>();
		roles.add(StandardRoles.ROLE_KEY_BUILDER);
		
		DefaultViewNode devNode = new WorkspaceApplicationNode("formbuilder",workspace,roles);
		devNode.setKeyboardShortcut(new KeyboardShortcut("4"));
		
		return devNode;
	}
	
}
