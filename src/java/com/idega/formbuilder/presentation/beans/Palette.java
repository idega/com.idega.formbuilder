package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.documentmanager.business.form.ConstComponentCategory;
import com.idega.documentmanager.business.form.DocumentManager;
import com.idega.formbuilder.view.ActionManager;

public class Palette implements Serializable {
	
	private static final long serialVersionUID = -753995857658793992L;
	
	private List<PaletteComponent> basic = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> buttons = new ArrayList<PaletteComponent>();
	
	public Palette() throws Exception {
		DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
		if(formManagerInstance != null) {
			List<String> temp = formManagerInstance.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC));
			Iterator it = temp.iterator();
			while(it.hasNext()) {
				basic.add(new PaletteComponent((String) it.next()));
			}
			temp = formManagerInstance.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BUTTONS));
			Iterator it2 = temp.iterator();
			while(it2.hasNext()) {
				buttons.add(new PaletteComponent((String) it2.next()));
			}
		}
	}

	public List<PaletteComponent> getBasic() {
		return basic;
	}

	public void setBasic(List<PaletteComponent> basic) {
		this.basic = basic;
	}

	public List<PaletteComponent> getButtons() {
		return buttons;
	}

	public void setButtons(List<PaletteComponent> buttons) {
		this.buttons = buttons;
	}
}
