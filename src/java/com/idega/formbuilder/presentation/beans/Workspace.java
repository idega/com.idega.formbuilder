package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.event.ActionEvent;

import com.idega.webface.event.WFTabEvent;
import com.idega.webface.event.WFTabListener;

public class Workspace implements Serializable, WFTabListener {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	public static final String CODE_VIEW = "source";
	public static final String DESIGN_VIEW = "design";
	public static final String PREVIEW_VIEW = "preview";
	
	private String selectedMenu;
	private boolean renderedMenu;
	private String view;
	private String designViewStatus;
	private boolean pagesPanelVisible;
	
	private Locale locale;
	
	public Locale getLocale() {
		return locale;
	}
	
	public void tabPressed(WFTabEvent event) {
		System.out.println("sdfsdsdsdf");
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Workspace() {
		this.renderedMenu = false;
		this.selectedMenu = "0";
		this.view = "design";
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
	
	public void changeView(ActionEvent ae) {
		String buttonId = ae.getComponent().getId();
		if(buttonId.equals("designViewTab")) {
			this.view = "design";
			this.renderedMenu = true;
		} else if(buttonId.equals("previewViewTab")) {
			this.view = "preview";
			this.renderedMenu = false;
		} else if(buttonId.equals("sourceViewTab")) {
			this.view = "source";
			this.renderedMenu = false;
		}
		
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
