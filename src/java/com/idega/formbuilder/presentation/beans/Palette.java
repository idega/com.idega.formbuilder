package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.formbuilder.view.ActionManager;

public class Palette implements Serializable {
	
	private static final long serialVersionUID = -753995857658793992L;
	
	private List<PaletteComponent> components = new ArrayList<PaletteComponent>();
	
	public Palette() throws Exception {
		Iterator it = ActionManager.getDocumentManagerInstance().getAvailableFormComponentsTypesList().iterator();
		while(it.hasNext()) {
			components.add(new PaletteComponent((String) it.next()));
		}
	}

	public List<PaletteComponent> getComponents() {
		return components;
	}

	public void setComponents(List<PaletteComponent> components) {
		this.components = components;
	}
}
