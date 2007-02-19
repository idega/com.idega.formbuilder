package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.html.HtmlAjaxSupport;
import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;

import com.idega.block.web2.presentation.Accordion;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.text.Text;
import com.idega.webface.WFDivision;


public class FBMenu extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Menu";
	
	private static final String NO_MENU_FACET = "NO_MENU_FACET";
	private static final String MENU_TOOLBAR_FACET = "MENU_TOOLBAR_FACET";
	
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

		HtmlCommandButton newFormButton = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		newFormButton.setId("newFormButton");
		newFormButton.setOnclick("displayMessage('/idegaweb/bundles/com.idega.formbuilder.bundle/resources/includes/new-dialog.inc');return false");
		newFormButton.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['toolbar_new']}"));
		
		HtmlSelectOneMenu selectFormMenu = (HtmlSelectOneMenu) application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		selectFormMenu.setId("selectFormMenu");
		selectFormMenu.setValueBinding("value", application.createValueBinding("#{formDocument.formId}"));
		UISelectItems forms = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		forms.setValueBinding("value", application.createValueBinding("#{formSelector.forms}"));
		addChild(forms, selectFormMenu);
		
		HtmlAjaxSupport selectSupport = new HtmlAjaxSupport();
		selectSupport.setEvent("onchange");
		selectSupport.setOnsubmit("showLoadingMessage('Opening')");
		selectSupport.setOncomplete("closeLoadingMessage()");
		selectSupport.setAjaxSingle(false);
		selectSupport.setReRender("mainApplication");
		selectSupport.setActionListener(application.createMethodBinding("#{formDocument.changeForm}", new Class[]{ActionEvent.class}));
		addChild(selectSupport, selectFormMenu);
		
		addChild(newFormButton, menuHeaderPanel);
		addChild(selectFormMenu, menuHeaderPanel);
		
		Accordion acc = new Accordion("fbMenu");
		acc.setId("fbMenuAccordion");
		acc.setHeight("400");
		
		FBPalette palette = (FBPalette) application.createComponent(FBPalette.COMPONENT_TYPE);
		palette.setColumns(2);
		palette.setId("firstlist");
		palette.setItemStyleClass("paletteComponent");
		palette.setStyleClass("componentsList");
		
		Text tab1 = new Text();
		tab1.setText("Component palette");
		tab1.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab1, palette);
		
		FBBasicProperties simpleProperties = (FBBasicProperties) application.createComponent(FBBasicProperties.COMPONENT_TYPE);
		
//		FBAdvancedProperties advancedProperties = (FBAdvancedProperties) application.createComponent(FBAdvancedProperties.COMPONENT_TYPE);
		
		Text tab2 = new Text();
		tab2.setText("Component properties");
		tab2.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab2, simpleProperties);
		
		FBFormProperties formProperties = (FBFormProperties) application.createComponent(FBFormProperties.COMPONENT_TYPE);
		
		Text tab3 = new Text();
		tab3.setText("Form properties");
		tab3.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab3, formProperties);
		
		FBPageProperties pageProperties = (FBPageProperties) application.createComponent(FBPageProperties.COMPONENT_TYPE);
		
		Text tab4 = new Text();
		tab4.setText("Page properties");
		tab4.setStyleClass("fbMenuTabBar");
		
		acc.addPanel(tab4, pageProperties);
		add(acc);
		
		HtmlOutputText noMenuLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noMenuLabel.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['no_menu_label']}"));
		noMenuLabel.setStyleClass("noMenuLabel");
		
		addFacet(MENU_TOOLBAR_FACET, menuHeaderPanel);
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
