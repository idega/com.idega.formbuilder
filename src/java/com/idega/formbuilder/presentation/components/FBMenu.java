package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.block.web2.presentation.Accordion;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;


public class FBMenu extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Menu";
	
	public FBMenu() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {	
		IWContext iwc = CoreUtil.getIWContext();
		Application application = context.getApplication();
		getChildren().clear();
		
		Accordion acc = new Accordion("fbMenu");
		acc.setId("fbMenuAccordion");
		acc.setUseSound(false);
		acc.setHeight("500");
		
		FBPalette palette = (FBPalette) application.createComponent(FBPalette.COMPONENT_TYPE);
		palette.setItemStyleClass("paletteComponent");
		palette.setStyleClass("componentsList");
		
		Text tab1 = new Text();
		tab1.setText(getLocalizedString(iwc, "fb_acc_comp_palette", "Component palette"));
		tab1.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab1, palette);
		
		FBComponentProperties simpleProperties = (FBComponentProperties) application.createComponent(FBComponentProperties.COMPONENT_TYPE);
		
		Text tab2 = new Text();
		tab2.setText(getLocalizedString(iwc, "fb_acc_comp_properties", "Component properties"));
		tab2.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab2, simpleProperties);
		
		FBFormProperties formProperties = (FBFormProperties) application.createComponent(FBFormProperties.COMPONENT_TYPE);
		formProperties.setId("fbFormPropertiesPanel");
		
		Text tab3 = new Text();
		tab3.setText(getLocalizedString(iwc, "fb_acc_form_properties", "Form properties"));
		tab3.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab3, formProperties);
		
		add(acc);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("style", "width: 300px;", null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
}
