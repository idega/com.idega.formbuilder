package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.formbuilder.media.FormSourceDownloader;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.presentation.components.FBWorkspace;
import com.idega.presentation.IWContext;
import com.idega.util.CoreUtil;
import com.idega.util.LocaleUtil;
import com.idega.webface.WFUtil;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	public static final String BEAN_ID = "workspace";
	
	private String view;
	private Locale locale;
	private boolean processMode;
	private String activeHomepageTab;
	
	public String getActiveHomepageTab() {
		return activeHomepageTab;
	}

	public void setActiveHomepageTab(String activeHomepageTab) {
		this.activeHomepageTab = activeHomepageTab;
	}

	public boolean isProcessMode() {
		return processMode;
	}

	public void setProcessMode(boolean processMode) {
		this.processMode = processMode;
	}

	public Locale getLocale() {
		if(locale == null) {
			locale = IWContext.getInstance().getCurrentLocale();
		}
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Workspace() {
		this.view = FBViewPanel.DESIGN_VIEW;
		this.locale = IWContext.getInstance().getCurrentLocale();
		this.processMode = false;
	}
	
	public String redirectHome() {
		return "redirectHome";
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	private Document getPropertiesPanel() {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(),true);
	}
	
	private Document getViewPanel() {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBViewPanel("viewPanel", "formContainer"), false);
	}
	
	public Document[] switchView(String view) {
		setView(view);
		
		ComponentPropertyManager propertyManager = (ComponentPropertyManager) WFUtil.getBeanInstance(ComponentPropertyManager.BEAN_ID);
		propertyManager.resetComponent();
		
		Document[] result = {getViewPanel(), getPropertiesPanel()};
		
		return result;
		
	}
	
	public Document getDesignView() {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
	}
	
	public Document getWorkspace(String langCode) {
		
		this.locale = LocaleUtil.getLocale(langCode);
		this.view = FBViewPanel.DESIGN_VIEW;
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBWorkspace("mainWorkspace"), false);
	}
	
	public Class<FormSourceDownloader> getFormSourceDownloaderClass() {
		return FormSourceDownloader.class;
	}
	
	public String getFormSourceDownloaderClassName() {
		return getFormSourceDownloaderClass().getName();
	}
	
}
