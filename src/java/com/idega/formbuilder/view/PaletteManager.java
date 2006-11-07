package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.PaletteComponent;
import com.idega.formbuilder.business.form.manager.IFormManager;

public class PaletteManager implements Serializable {
	
	private static final long serialVersionUID = -753995857658793992L;
	
	private List<PaletteComponent> components = new ArrayList<PaletteComponent>();
	private IFormManager formManagerInstance;
	
	public PaletteManager() throws Exception {
		formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		Iterator it = formManagerInstance.getAvailableFormComponentsTypesList().iterator();
		while(it.hasNext()) {
			PaletteComponent temp = new PaletteComponent((String) it.next());
			components.add(temp);
		}
	}

	public List<PaletteComponent> getComponents() {
		return components;
	}

	public void setComponents(List<PaletteComponent> components) {
		this.components = components;
	}
}
