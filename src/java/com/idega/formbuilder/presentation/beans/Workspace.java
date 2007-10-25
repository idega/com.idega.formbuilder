package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.event.ActionEvent;

import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.util.CoreUtil;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String selectedMenu;
	private boolean renderedMenu;
	private String view;
	private String designViewStatus;
	private boolean pagesPanelVisible;
	
	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Workspace() {
		this.renderedMenu = false;
		this.selectedMenu = "0";
		this.view = FBViewPanel.DESIGN_VIEW;
		this.designViewStatus = "noform";
		this.pagesPanelVisible = false;
		this.locale = new Locale("en");
	}
	
	public void togglePagesPanel(ActionEvent ae) {
		if(pagesPanelVisible) {
			pagesPanelVisible = false;
		} else {
			pagesPanelVisible = true;
		}
	}
	
	public String redirectHome() {
		return "redirectHome";
	}

	public boolean isRenderedMenu() {
		return renderedMenu;
	}

	public void setRenderedMenu(boolean renderedMenu) {
		this.renderedMenu = renderedMenu;
	}

	public String getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
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

	public String getDesignViewStatus() {
		return designViewStatus;
	}

	public void setDesignViewStatus(String designViewStatus) {
		this.designViewStatus = designViewStatus;
	}

	public boolean isPagesPanelVisible() {
		return pagesPanelVisible;
	}

	public void setPagesPanelVisible(boolean pagesPanelVisible) {
		this.pagesPanelVisible = pagesPanelVisible;
	}

}
