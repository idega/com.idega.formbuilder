package com.idega.formbuilder.presentation.components;

import java.io.IOException;

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

import com.idega.formbuilder.presentation.FBComponentBase;


public class FBMenu extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Menu";
	
	private static final String NO_MENU_FACET = "NO_MENU_FACET";
	private static final String MENU_TOOLBAR_FACET = "MENU_TOOLBAR_FACET";
	
	private static final String MENU_TAB_1 = "tab1";
	private static final String MENU_TAB_2 = "tab2";
	private static final String MENU_TAB_3 = "tab3";
	private static final String MENU_TAB_4 = "tab4";
	
	private String id;
	private String styleClass;
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
		
		FBDivision menuHeaderPanel = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		menuHeaderPanel.setId("menuPanelToolbar");
		
//		HtmlAjaxFunction newFormFunction = new HtmlAjaxFunction();
//		newFormFunction.setName("createNewForm2");
//		newFormFunction.setReRender("mainApplication");
////		newFormFunction.setData(application.createValueBinding("#{formDocument.selectedComponent}"));
//		newFormFunction.setAction(application.createMethodBinding("#{formDocument.createNewForm}", new Class[]{String.class}));
////		newFormFunction.setActionListener(application.createMethodBinding("#{formDocument.createNewForm}", new Class[]{ActionEvent.class}));
		
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
		selectSupport.setActionListener(application.createMethodBinding("#{workspace.changeForm}", new Class[]{ActionEvent.class}));
		addChild(selectSupport, selectFormMenu);
		
//		addChild(newFormFunction, menuHeaderPanel);
		addChild(newFormButton, menuHeaderPanel);
		addChild(selectFormMenu, menuHeaderPanel);
		
		
		FBMenuPanel tab1 = (FBMenuPanel) application.createComponent(FBMenuPanel.COMPONENT_TYPE);
		tab1.setExpanded(false);
		tab1.setValueBinding("title", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['component_palette_panel']}"));
		tab1.setId(MENU_TAB_1);
		
		FBPalette palette = (FBPalette) application.createComponent(FBPalette.COMPONENT_TYPE);
		palette.setColumns(2);
		palette.setId("firstlist");
		palette.setItemStyleClass("paletteComponent");
		palette.setStyleClass("componentsList");
		palette.setValueBinding("items", application.createValueBinding("#{palette.components}"));
		
		tab1.add(palette);
		
		
		
		FBMenuPanel tab2 = (FBMenuPanel) application.createComponent(FBMenuPanel.COMPONENT_TYPE);
		tab2.setExpanded(false);
		tab2.setValueBinding("title", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['field_properties_panel']}"));
		tab2.setId(MENU_TAB_2);
		
		FBBasicProperties simpleProperties = (FBBasicProperties) application.createComponent(FBBasicProperties.COMPONENT_TYPE);
		
		FBAdvancedProperties advancedProperties = (FBAdvancedProperties) application.createComponent(FBAdvancedProperties.COMPONENT_TYPE);
		
		tab2.add(simpleProperties);
		tab2.add(advancedProperties);
		
		
		
		FBMenuPanel tab3 = (FBMenuPanel) application.createComponent(FBMenuPanel.COMPONENT_TYPE);
		tab3.setExpanded(false);
		tab3.setValueBinding("title", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['form_properties_panel']}"));
		tab3.setId(MENU_TAB_3);
		
		FBFormProperties formProperties = (FBFormProperties) application.createComponent(FBFormProperties.COMPONENT_TYPE);
		
		tab3.add(formProperties);
		
		
		
		FBMenuPanel tab4 = (FBMenuPanel) application.createComponent(FBMenuPanel.COMPONENT_TYPE);
		tab4.setExpanded(false);
		tab4.setValueBinding("title", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['form_steps_panel']}"));
		tab4.setId(MENU_TAB_4);
		
		
		HtmlOutputText noMenuLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		noMenuLabel.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['no_menu_label']}"));
		noMenuLabel.setStyleClass("noMenuLabel");
		
		addFacet(MENU_TOOLBAR_FACET, menuHeaderPanel);
		addFacet(NO_MENU_FACET, noMenuLabel);
		
		addFacet(MENU_TAB_1, tab1);
		addFacet(MENU_TAB_2, tab2);
		addFacet(MENU_TAB_3, tab3);
		addFacet(MENU_TAB_4, tab4);
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
		
		if(show) {
			ValueBinding selectedMenuVB = this.getValueBinding("selectedMenu");
			if(selectedMenuVB != null) {
				selectedMenu = (String) selectedMenuVB.getValue(context);
			} else {
				selectedMenu = getSelectedMenu();
			}
			if(selectedMenu != null) {
				FBMenuPanel tab1 = (FBMenuPanel) getFacet(MENU_TAB_1);
				FBMenuPanel tab2 = (FBMenuPanel) getFacet(MENU_TAB_2);
				FBMenuPanel tab3 = (FBMenuPanel) getFacet(MENU_TAB_3);
				FBMenuPanel tab4 = (FBMenuPanel) getFacet(MENU_TAB_4);
				if(selectedMenu.equals("0")) {
					tab1.setExpanded(true);
					tab2.setExpanded(false);
					tab3.setExpanded(false);
					tab4.setExpanded(false);
				} else if(selectedMenu.equals("1")) {
					tab1.setExpanded(false);
					tab2.setExpanded(true);
					tab3.setExpanded(false);
					tab4.setExpanded(false);
				} else if(selectedMenu.equals("2")) {
					tab1.setExpanded(false);
					tab2.setExpanded(false);
					tab3.setExpanded(true);
					tab4.setExpanded(false);
				} else if(selectedMenu.equals("3")) {
					tab1.setExpanded(false);
					tab2.setExpanded(false);
					tab3.setExpanded(false);
					tab4.setExpanded(true);
				}
			}
		} else {
			UIComponent comp = getFacet(NO_MENU_FACET);
			if(comp != null) {
				renderChild(context, comp);
			}
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		boolean show;
		ValueBinding showVB = getValueBinding("show");
		if(showVB != null) {
			show = (Boolean) showVB.getValue(context);
		} else {
			show = getShow();
		}
		if (!isRendered()) {
			return;
		}
		
		if(show) {
			FBMenuPanel tab1 = (FBMenuPanel) getFacet(MENU_TAB_1);
			if(tab1 != null) {
				renderChild(context, tab1);
			}
			FBMenuPanel tab2 = (FBMenuPanel) getFacet(MENU_TAB_2);
			if(tab2 != null) {
				renderChild(context, tab2);
			}
			FBMenuPanel tab3 = (FBMenuPanel) getFacet(MENU_TAB_3);
			if(tab3 != null) {
				renderChild(context, tab3);
			}
			FBMenuPanel tab4 = (FBMenuPanel) getFacet(MENU_TAB_4);
			if(tab4 != null) {
				renderChild(context, tab4);
			}
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = selectedMenu;
		values[4] = show;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		selectedMenu = (String) values[3];
		show = (Boolean) values[4];
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
