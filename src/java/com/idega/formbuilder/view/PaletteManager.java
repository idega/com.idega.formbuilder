package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.FormComponent;
import com.idega.formbuilder.business.form.manager.IFormManager;

public class PaletteManager implements Serializable {
	
	private static final long serialVersionUID = -753995857658793992L;
	
	private List<FormComponent> components = new ArrayList<FormComponent>();
	private IFormManager formManagerInstance;
	
	public PaletteManager() throws Exception {
		//try {
		formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		components.clear();
		List list = formManagerInstance.getAvailableFormComponentsList();
		Iterator it = list.iterator();
			//FormField temp = null;
		while(it.hasNext()) {
			FormComponent temp = new FormComponent((String) it.next());
			components.add(temp);
		}
		/*} catch(Exception e) {
			e.printStackTrace();
		}*/
	}

	public List<FormComponent> getComponents() {
		return components;
	}

	public void setComponents(List<FormComponent> components) {
		this.components = components;
	}
}
