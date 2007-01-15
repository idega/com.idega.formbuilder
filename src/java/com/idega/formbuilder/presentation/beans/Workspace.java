package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String selectedMenu;
	private boolean renderedMenu;
	private String view;
	private String designViewStatus;
	
	public Workspace() {
		this.renderedMenu = false;
		this.selectedMenu = "0";
		this.view = "design";
		this.designViewStatus = "noform";
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

}
