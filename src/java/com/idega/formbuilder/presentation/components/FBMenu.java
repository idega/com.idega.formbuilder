package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.component.html.ext.HtmlOutputLabel;
import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.block.web2.presentation.Accordion;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.text.Text;
import com.idega.webface.WFDivision;


public class FBMenu extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Menu";
	
	private static final String NO_MENU_FACET = "NO_MENU_FACET";
	private static final String MENU_TOOLBAR_FACET = "MENU_TOOLBAR_FACET";
	private static final String NEW_FORM_PANEL_FACET = "NEW_FORM_PANEL_FACET";
	
	private String selectedMenu;
	private boolean show;
	
	public boolean getShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public String getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(String selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

	public FBMenu() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision menuHeaderPanel = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		menuHeaderPanel.setId("menuPanelToolbar");

		WFDivision newFormPanel = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		newFormPanel.setId("newFormPanel");
		
		HtmlOutputLabel newFormLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		newFormLabel.setFor("newFormInput");
		newFormLabel.setStyleClass("fbStandardLabel");
		
		HtmlOutputLabel newFormInput = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		newFormInput.setValueBinding("value", application.createValueBinding("#{formDocument.tempValue}"));
		newFormInput.setStyleClass("fbStandardInput");
		newFormInput.setId("newFormInput");
		
		HtmlCommandButton createNewFormButton = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		createNewFormButton.setId("createNewFormButton");
		createNewFormButton.setOnclick("createNewForm();return false");
		createNewFormButton.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['toolbar_create']}"));
		createNewFormButton.setStyleClass("fbStdImgButton");
		
		HtmlCommandButton cancelNewFormButton = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		cancelNewFormButton.setId("cancelNewFormButton");
		cancelNewFormButton.setOnclick("closeNewFormPanel();return false");
		cancelNewFormButton.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['toolbar_cancel']}"));
		cancelNewFormButton.setStyleClass("fbStdImgButton");
		
		addChild(newFormLabel, newFormPanel);
		addChild(newFormInput, newFormPanel);
		addChild(createNewFormButton, newFormPanel);
		addChild(cancelNewFormButton, newFormPanel);
		
		Accordion acc = new Accordion("fbMenu");
		acc.setId("fbMenuAccordion");
		acc.setHeight("400");
		acc.setIncludeJavascript(false);
		
		FBPalette palette = (FBPalette) application.createComponent(FBPalette.COMPONENT_TYPE);
		palette.setColumns(2);
		palette.setId("firstlist");
		palette.setItemStyleClass("paletteComponent");
		palette.setStyleClass("componentsList");
		
		Text tab1 = new Text();
		tab1.setText("Component palette");
		tab1.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab1, palette);
		
		FBComponentPropertiesPanel simpleProperties = (FBComponentPropertiesPanel) application.createComponent(FBComponentPropertiesPanel.COMPONENT_TYPE);
		
		Text tab2 = new Text();
		tab2.setText("Component properties");
		tab2.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab2, simpleProperties);
		
		FBFormProperties formProperties = (FBFormProperties) application.createComponent(FBFormProperties.COMPONENT_TYPE);
		
		Text tab3 = new Text();
		tab3.setText("Form properties");
		tab3.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab3, formProperties);
		
//		FBPageProperties pageProperties = (FBPageProperties) application.createComponent(FBPageProperties.COMPONENT_TYPE);
//		
//		Text tab4 = new Text();
//		tab4.setText("Section properties");
//		tab4.setStyleClass("fbMenuTabBar");
//		
//		acc.addPanel(tab4, pageProperties);
		add(acc);
		
		HtmlOutputText noMenuLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noMenuLabel.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['no_menu_label']}"));
		noMenuLabel.setStyleClass("noMenuLabel");
		
		addFacet(MENU_TOOLBAR_FACET, menuHeaderPanel);
		addFacet(NEW_FORM_PANEL_FACET, newFormPanel);
		addFacet(NO_MENU_FACET, noMenuLabel);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("style", "width: 300px;", null);
		
		UIComponent facet = getFacet(MENU_TOOLBAR_FACET);
		if(facet != null) {
			renderChild(context, facet);
		}
		ValueBinding showVB = getValueBinding("show");
		if(showVB != null) {
			show = (Boolean) showVB.getValue(context);
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		ValueBinding showVB = getValueBinding("show");
		if(showVB != null) {
			show = (Boolean) showVB.getValue(context);
		}
		if (!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			UIComponent current = (UIComponent) it.next();
			renderChild(context, current);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = selectedMenu;
		values[2] = show;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		selectedMenu = (String) values[1];
		show = (Boolean) values[2];
	}
	
}
