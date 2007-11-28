package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.presentation.components.FBWorkspace;
import com.idega.util.CoreUtil;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	public static final String BEAN_ID = "workspace";
	
	private static final String DEFAULT_LOCALE = "en";
	
	private String view;
	private Locale locale;
	private boolean processMode;
	
	public boolean isProcessMode() {
		return processMode;
	}

	public void setProcessMode(boolean processMode) {
		this.processMode = processMode;
	}

	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Workspace() {
		this.view = FBViewPanel.DESIGN_VIEW;
		this.locale = new Locale(DEFAULT_LOCALE);
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
	
	public Document switchView(String view) {
		setView(view);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBViewPanel("viewPanel", "formContainer"), false);
	}
	
	public Document getDesignView() {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
	}
	
	public Document getWorkspace(String locale) {
		Locale loc = new Locale(locale);
		this.locale = loc;
		this.view = FBViewPanel.DESIGN_VIEW;
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBWorkspace("mainWorkspace"), false);
	}
	
}
