package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.documentmanager.business.component.ConstComponentCategory;

public class Palette implements Serializable {
	
	private static final long serialVersionUID = -753995857658793992L;
	
	private static Log logger = LogFactory.getLog(Palette.class);
	
	public static final String BEAN_ID = "palette";
	
	private List<PaletteComponent> basic = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> buttons = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> plain = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> autofill = new ArrayList<PaletteComponent>();
	
	private InstanceManager instanceManager;
	
	public InstanceManager getInstanceManager() {
		return instanceManager;
	}

	public void setInstanceManager(InstanceManager instanceManager) {
		this.instanceManager = instanceManager;
	}

	public List<PaletteComponent> getBasic() {
		if(basic == null || basic.isEmpty()) {
			List<String> basics = instanceManager.getDocumentManagerInstance().getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC));
			basic = populatePaletteComponentList(basics, "fbc");
		}
		return basic;
	}

	public void setBasic(List<PaletteComponent> basic) {
		this.basic = basic;
	}
	
	public List<PaletteComponent> getButtons() {
		if(buttons == null || buttons.isEmpty()) {
			List<String> btns = instanceManager.getDocumentManagerInstance().getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BUTTONS));
			buttons = populatePaletteComponentList(btns, "fbb");
		}
		return buttons;
	}
	
	private List<PaletteComponent> populatePaletteComponentList(List<String> components, String category) {
		List<PaletteComponent> list = new ArrayList<PaletteComponent>();
		Iterator<String> it = components.iterator();
		while(it.hasNext()) {
			String nextComp = it.next();
			try {
				list.add(new PaletteComponent(nextComp, category));
			} catch(Exception e) {
				logger.error("Could not retrieve component: " + nextComp);
			}
		}
		return list;
	}

	public void setButtons(List<PaletteComponent> buttons) {
		this.buttons = buttons;
	}
	
	public List<PaletteComponent> getBasicComponents() {
		List<PaletteComponent> components = new ArrayList<PaletteComponent>();
		components.addAll(getBasic());
		components.addAll(getPlain());
		components.addAll(getButtons());
		return components;
	}
	
	public List<PaletteComponent> getAdvancedComponents() {
		List<PaletteComponent> components = new ArrayList<PaletteComponent>();
		components.addAll(getAutofill());
		return components;
	}

	public List<PaletteComponent> getPlain() {
		if(plain == null || plain.isEmpty()) {
			List<String> plains = instanceManager.getDocumentManagerInstance().getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.PLAIN));
			plain = populatePaletteComponentList(plains, "fbc");
		}
		return plain;
	}
	
	public List<PaletteComponent> getAutofill() {
		if(autofill == null || autofill.isEmpty()) {
			List<String> comps = instanceManager.getDocumentManagerInstance().getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.AUTOFILL));
			autofill = populatePaletteComponentList(comps, "fbc");
		}
		return autofill;
	}

	public void setPlain(List<PaletteComponent> plain) {
		this.plain = plain;
	}
}
