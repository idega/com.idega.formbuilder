package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class Workspace implements Serializable {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String view;
	private String designViewStatus;
	private List<SelectItem> views = new ArrayList<SelectItem>();
	private int selectedTab;
	private String currentLocale;
	private String currentComponent;
	private String formTitle;
	
	@SuppressWarnings("static-access")
	public String getFormTitle() {
		if(!designViewStatus.equals("noform")) {
			IFormManager am = ActionManager.getFormManagerInstance();
			LocalizedStringBean title = am.getFormTitle();
			this.formTitle = title.getString(new Locale("en"));
			System.out.println("GETTING FORM TITLE: " + title.getString(new Locale("en")));
		}	
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		System.out.println("SETTING FORM TITLE: " + formTitle);
		LocalizedStringBean bean = new LocalizedStringBean();
		bean.setString(new Locale("en"), formTitle);
		IFormManager am = ActionManager.getFormManagerInstance();
		try {
			if(am.getFormId() != null && !am.getFormId().equals("")) {
				am.setFormTitle(bean);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.formTitle = formTitle;
	}

	public String getCurrentComponent() {
		return currentComponent;
	}

	public void setCurrentComponent(String currentComponent) {
		this.currentComponent = currentComponent;
	}

	public Workspace() {
		this.formTitle = "";
		
		this.currentLocale = "en";
		this.selectedTab = 1;
		this.view = "design";
		this.designViewStatus = "noform";
		this.views.add(new SelectItem("design", "Design"));
		this.views.add(new SelectItem("preview", "Preview"));
		this.views.add(new SelectItem("source", "Source"));
	}
	
	@SuppressWarnings("static-access")
	public void formChanged(ActionEvent ae) throws Exception {
		String formId = (String) ((javax.faces.component.html.HtmlSelectOneMenu) ae.getComponent().getParent()).getValue();
		if(formId != null && !formId.equals("")) {
			try {
				IFormManager formManagerInstance = ((ActionManager)WFUtil.getBeanInstance("viewmanager")).getFormManagerInstance();
				formManagerInstance.openFormDocument(formId);
				if(formManagerInstance.getFormComponentsIdsList().size() > 0) {
					this.designViewStatus = "active";
				} else {
					this.designViewStatus = "empty";
				}
				this.view = "design";
				this.selectedTab = 1;
				this.currentComponent = null;
				this.formTitle = getFormTitle();
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception("Invalid form ID");
		}
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
