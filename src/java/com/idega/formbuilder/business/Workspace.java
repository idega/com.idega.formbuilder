package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.model.SelectItem;

import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.view.ActionManager;
import com.idega.webface.WFUtil;

public class Workspace implements Serializable, ActionListener {
	
	private static final long serialVersionUID = -7539955904708793992L;
	
	private String view;
	private String designViewStatus;
	private List<SelectItem> views = new ArrayList<SelectItem>();
	private int selectedTab;
	private String currentLocale;
	private String currentComponent;
	private String formTitle;
	
	public String getFormTitle() {
		return formTitle;
	}
	
	public void processAction(ActionEvent ae) {
		String buttonId = ae.getComponent().getClientId(FacesContext.getCurrentInstance());
		System.out.println(buttonId);
	}

	public void setFormTitle(String formTitle) {
		LocalizedStringBean bean = new LocalizedStringBean();
		bean.setString(new Locale("en"), formTitle);
		IFormManager am = ((ActionManager)WFUtil.getBeanInstance("viewmanager")).getFormManagerInstance();
		try {
			if(am.getFormId() != null && !am.getFormId().equals("")) {
				am.setFormTitle(bean);
			}
			//ActionManager.getFormManagerInstance().setFormTitle(bean);
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
		this.currentLocale = "en";
		this.selectedTab = 1;
		this.view = "design";
		this.designViewStatus = "noform";
		this.views.add(new SelectItem("design", "Design"));
		this.views.add(new SelectItem("preview", "Preview"));
		this.views.add(new SelectItem("source", "Source"));
	}
	
	public void formChanged(ActionEvent ae) throws Exception {
		/*String buttonId = ae.getComponent().getClientId(FacesContext.getCurrentInstance());
		/*System.out.println(buttonId);
		 * 
		 */
		
		String formId = (String) ((javax.faces.component.html.HtmlSelectOneMenu) ae.getComponent()).getValue();
		if(formId != null && !formId.equals("")) {
			try {
				((ActionManager)WFUtil.getBeanInstance("viewmanager")).getFormManagerInstance().openFormDocument(formId);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new Exception("Invalid form ID");
		}
	}
	
	public void viewChanged(ActionEvent ae) {
		String buttonId = ae.getComponent().getClientId(FacesContext.getCurrentInstance());
		String selectedForm = "";
		if(buttonId.endsWith(":designViewButton")) {
			view = "design";
		} else if(buttonId.endsWith(":previewViewButton")) {
			view = "preview";
		} else if(buttonId.endsWith(":sourceViewButton")) {
			view = "source";
		} else if(buttonId.equals("workspace1:formList")) {
			selectedForm = (String) ((HtmlSelectOneMenu) ae.getComponent()).getValue();
			System.out.println("NEW FORM SELECTED: " + selectedForm);
		}
		System.out.println("FORM CHANGE EVENT " + buttonId);
	}
	
	public void formChange(ActionEvent ae) {
		String buttonId = ae.getComponent().getClientId(FacesContext.getCurrentInstance());
		System.out.println("FORM CHANGE EVENT " + buttonId);
	}

	public String getView() {
		System.out.println("GETTING VIEW");
		return view;
	}

	public void setView(String view) {
		System.out.println("SETTING VIEW");
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
