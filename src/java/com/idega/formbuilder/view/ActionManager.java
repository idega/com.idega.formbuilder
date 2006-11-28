package com.idega.formbuilder.view;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.FormManagerFactory;
import com.idega.formbuilder.business.form.manager.IFormManager;

public class ActionManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	private IFormManager formManagerInstance;
	
	private String selectedFieldTypeValue;
	private String text;
	
	
	private String formTitle = "Untitled form";
	private String formDescription = "Please put the description of the form here";
	
	private boolean viewInitialized = false;
	
	private String currentFormName = null;
	private String currentLocale = null;
	
	public void changeSelectedForm(ActionEvent ae) {
		System.out.println("Please put the description of the form here");
		return;
	}

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

	public ActionManager() throws Exception {
		formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
		/*try {
			if(!isViewInitialized()) {
				initialize();
				setViewInitialized(true);
			}
		} catch(Exception e) {
			setViewInitialized(false);
			e.printStackTrace();
		}*/
	}
	
	/*private void initialize() throws Exception {
		try {
			System.out.println("CREATING FORM MANAGER");
			formManagerInstance = FormManagerFactory.newFormManager(FacesContext.getCurrentInstance());
			System.out.println("CREATED FORM MANAGER");
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORM_MANAGER_INSTANCE, formManagerInstance);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "NO_FORM");
			
		} catch(InitializationException e) {
			throw new Exception("FormManager instantiation failed: " + e);
		} catch(Exception e) {
			throw new Exception("UIManager instantiation failed: " + e);
		}
	}*/
	
	public static IFormManager getFormManagerInstance() {
		return(IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
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