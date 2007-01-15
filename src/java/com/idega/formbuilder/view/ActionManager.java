package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.FormManagerFactory;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	private static IFormManager formManagerInstance = null;
	
	private String selectedFieldTypeValue;
	private String text;
	
	
	private String formTitle = "Untitled form";
	private String formDescription = "Please put the description of the form here";
	
	private boolean viewInitialized = false;
	
	private String currentFormName = null;
	private String currentLocale = null;

	public String getCurrentFormName() {
		return currentFormName;
	}

	public void setCurrentFormName(String currentFormName) {
		this.currentFormName = currentFormName;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSelectedFieldTypeValue() {
		return selectedFieldTypeValue;
	}

	public void setSelectedFieldTypeValue(String selectedFieldTypeValue) {
		this.selectedFieldTypeValue = selectedFieldTypeValue;
	}

	/*public ActionManager() throws Exception {
		if(formManagerInstance == null) {
			formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
		}
	}*/
	
	public static IFormManager getFormManagerInstance() {
		if(formManagerInstance == null) {
			try {
				formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
			} catch(InitializationException ie) {
				ie.printStackTrace();
			}
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
			return formManagerInstance;
		} else {
			return (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		}
	}

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public boolean isViewInitialized() {
		return viewInitialized;
	}

	public void setViewInitialized(boolean viewInitialized) {
		this.viewInitialized = viewInitialized;
	}

	public String getCurrentLocale() {
		return currentLocale;
	}

	public void setCurrentLocale(String currentLocale) {
		this.currentLocale = currentLocale;
	}

	public void setFormManagerInstance(IFormManager formManagerInstance) {
		this.formManagerInstance = formManagerInstance;
	}

}