package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String view;
	private String designViewStatus;
	private List<SelectItem> views = new ArrayList<SelectItem>();
	private int selectedTab;
	private String currentLocale;
	
	public Workspace() {
		this.currentLocale = "en";
		this.selectedTab = 1;
		this.view = "design";
		this.designViewStatus = "noform";
		this.views.add(new SelectItem("design", "Design"));
		this.views.add(new SelectItem("preview", "Preview"));
		this.views.add(new SelectItem("source", "Source"));
	}
	
	public void viewChanged(ActionEvent ae) {
		String buttonId = ae.getComponent().getClientId(FacesContext.getCurrentInstance());
		if(buttonId.endsWith(":designViewButton")) {
			view = "design";
		} else if(buttonId.endsWith(":previewViewButton")) {
			view = "preview";
		} else if(buttonId.endsWith(":sourceViewButton")) {
			view = "source";
		}
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public List<SelectItem> getViews() {
		return views;
	}

	public void setViews(List<SelectItem> views) {
		this.views = views;
	}

	public String getDesignViewStatus() {
		return designViewStatus;
	}

	public void setDesignViewStatus(String designViewStatus) {
		this.designViewStatus = designViewStatus;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

	public String getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(String currentLocale) {
		this.currentLocale = currentLocale;
	}

}
