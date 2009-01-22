package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Locale;

import org.jdom.Document;
import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.web2.business.Web2Business;
import com.idega.block.web2.business.Web2BusinessBean;
import com.idega.builder.business.BuilderLogic;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.media.FormSourceDownloader;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.presentation.components.FBWorkspace;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
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
	
	@Autowired
	private Web2Business web2;
	
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
	
	public String getJavaScriptSources() {	
		StringBuilder js = new StringBuilder(CoreConstants.DWR_UTIL_SCRIPT).append(CoreConstants.COMMA).append(CoreConstants.DWR_ENGINE_SCRIPT)
			.append(CoreConstants.COMMA).append("/dwr/interface/FormComponent.js,/dwr/interface/PropertyManager.js,/dwr/interface/FormDocument.js,")
			.append("/dwr/interface/FormPage.js,/dwr/interface/ProcessData.js,/dwr/interface/Workspace.js,");
		try {
			js.append(web2.getBundleURIToMootoolsLib()).append(CoreConstants.COMMA).append(web2.getBundleUriToSmoothboxScript()).append(CoreConstants.COMMA);
			js.append(web2.getMoodalboxScriptFilePath(true)).append(CoreConstants.COMMA).append(web2.getBundleUriToInlineEditScript()).append(CoreConstants.COMMA);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		js.append(web2.getBundleUriToMootabsScript()).append(CoreConstants.COMMA).append(web2.getBundleURIToJQueryLib()).append(CoreConstants.COMMA);
		
		IWBundle web2Bundle = IWMainApplication.getDefaultIWMainApplication().getBundle(Web2BusinessBean.WEB2_BUNDLE_IDENTIFIER);
		String basePath = "javascript/jquery-ui/1.6rc5/";
		js.append(web2Bundle.getVirtualPathWithFileNameString(new StringBuilder(basePath).append("ui.core.js").toString())).append(CoreConstants.COMMA);
		js.append(web2Bundle.getVirtualPathWithFileNameString(new StringBuilder(basePath).append("ui.draggable.js").toString())).append(CoreConstants.COMMA);
		js.append(web2Bundle.getVirtualPathWithFileNameString(new StringBuilder(basePath).append("ui.sortable.js").toString())).append(CoreConstants.COMMA);
		
		js.append(web2.getBundleUriToHumanizedMessagesScript()).append(CoreConstants.COMMA);
		
		js.append(IWMainApplication.getDefaultIWMainApplication().getBundle(org.chiba.web.IWBundleStarter.BUNDLE_IDENTIFIER)
				.getVirtualPathWithFileNameString("javascript/PresentationContext.js")).append(CoreConstants.COMMA);
		js.append(IWMainApplication.getDefaultIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER)
																						.getVirtualPathWithFileNameString("javascript/FormBuilderHelper.js"));
		
		return js.toString();
	}
	
	public String getStyleSheetSources() {
		StringBuilder css = new StringBuilder(IWMainApplication.getDefaultIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER)
				.getVirtualPathWithFileNameString("style/formbuilder.css")).append(CoreConstants.COMMA);
		
		try {
			css.append(web2.getBundleUriToSmoothboxStylesheet()).append(CoreConstants.COMMA).append(web2.getMoodalboxStyleFilePath()).append(CoreConstants.COMMA);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		css.append(web2.getBundleUriToHumanizedMessagesStyleSheet()).append(CoreConstants.COMMA).append(web2.getBundleUriToMootabsStyle());
		
		return css.toString();
	}
	
}
