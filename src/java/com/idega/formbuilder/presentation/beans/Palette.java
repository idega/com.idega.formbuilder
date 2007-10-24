package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.documentmanager.business.DocumentManager;
import com.idega.documentmanager.business.component.ConstComponentCategory;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.view.ActionManager;
import com.idega.util.config.Config;
import com.idega.util.config.ConfigFactory;

public class Palette implements Serializable {
	
	private static final long serialVersionUID = -753995857658793992L;
	private static final String af_components = "autofill-components";
	
	private List<PaletteComponent> basic = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> buttons = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> plain = new ArrayList<PaletteComponent>();
	private List<PaletteComponent> autofilled = new ArrayList<PaletteComponent>();
	
	public Palette() throws Exception {
		DocumentManager formManagerInstance = ActionManager.getCurrentInstance().getDocumentManagerInstance();
		if(formManagerInstance != null) {
			List<String> temp = formManagerInstance.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC));
			
			System.out.println("x: "+temp);
			Iterator<String> it = temp.iterator();
			while(it.hasNext()) {
				basic.add(new PaletteComponent(it.next()));
			}
			temp = formManagerInstance.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BUTTONS));
			Iterator<String> it2 = temp.iterator();
			while(it2.hasNext()) {
				buttons.add(new PaletteComponent(it2.next()));
			}
			temp = formManagerInstance.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.PLAIN));
			Iterator<String> it3 = temp.iterator();
			while(it3.hasNext()) {
				plain.add(new PaletteComponent(it3.next()));
			}
			
			Config cfg = ConfigFactory.getInstance().getConfig(IWBundleStarter.IW_BUNDLE_IDENTIFIER, IWBundleStarter.FB_CFG_FILE);
			Map<String, String > props = cfg.getProperies(af_components);
			
			if(props != null) {
				
				for (Iterator<String> iter = props.keySet().iterator(); iter.hasNext();) {
					String component_type = iter.next();
					
					PaletteComponent af_comp = new PaletteComponent(component_type);
					af_comp.setAutofillKey(props.get(component_type));
					autofilled.add(af_comp);
				}
			}
		}
	}

	public List<PaletteComponent> getBasic() {
		return basic;
	}

	public void setBasic(List<PaletteComponent> basic) {
		this.basic = basic;
	}
	
	public List<PaletteComponent> getAutofilled() {
		return autofilled;
	}

	public void setAutofilled(List<PaletteComponent> autofilled) {
		this.autofilled = autofilled;
	}

	public List<PaletteComponent> getButtons() {
		return buttons;
	}

	public void setButtons(List<PaletteComponent> buttons) {
		this.buttons = buttons;
	}

	public List<PaletteComponent> getPlain() {
		return plain;
	}

	public void setPlain(List<PaletteComponent> plain) {
		this.plain = plain;
	}
}
