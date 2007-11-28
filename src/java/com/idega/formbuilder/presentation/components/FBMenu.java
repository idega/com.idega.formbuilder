package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.block.web2.presentation.Accordion;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;


public class FBMenu extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Menu";
	
	protected void initializeComponent(FacesContext context) {	
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = new Layer(Layer.DIV);
		body.setId("optionsPanel");
		
		Accordion acc = new Accordion("fbMenu");
		acc.setId("fbMenuAccordion");
		acc.setUseSound(false);
		acc.setHeight("400");
		
		FBPalette palette = new FBPalette();
		palette.setItemStyleClass("paletteComponent");
		palette.setStyleClass("componentsList");
		
		Text tab1 = new Text();
		tab1.setText(getLocalizedString(iwc, "fb_acc_comp_palette", "Component palette"));
		tab1.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab1, palette);
		
		FBComponentProperties simpleProperties = new FBComponentProperties();
		
		Text tab2 = new Text();
		tab2.setText(getLocalizedString(iwc, "fb_acc_comp_properties", "Component properties"));
		tab2.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab2, simpleProperties);
		
		body.add(acc);
		
		add(body);
	}
	
}
