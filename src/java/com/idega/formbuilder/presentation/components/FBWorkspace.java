package com.idega.formbuilder.presentation.components;

import java.util.Map;

import javax.faces.context.FacesContext;

import com.idega.block.web2.presentation.Accordion;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.ProcessData;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBWorkspace extends FBComponentBase {

	public static final String COMPONENT_TYPE = "Workspace";

	private static final String PAGES_PANEL = "pagesPanel";
	private static final String FORM_PAGE_ICON_CLASS = "formPageIcon";
	private static final String PAGES_GENERAL_CONTAINER_CLASS = "pagesGeneralContainer";
	private static final String PAGES_SPECIAL_CONTAINER_CLASS = "pagesSpecialContainer";
	private static final String SELECTED_ELEMENT_CLASS = "selectedElement";
	private static final String OPTIONS_PANEL_ID = "optionsPanel";
	private static final String FB_MENU_ID = "fbMenu";
	private static final String FB_MENU_ACCORDION_ID = "fbMenuAccordion";
	private static final String PALETTE_COMPONENT_CLASS = "paletteComponent";
	private static final String COMPONENTS_LIST_CLASS = "componentsList";
	private static final String FB_MENU_BAR_CLASS = "fbMenuTabBar";
	private static final String RIGHT_PANEL_ID = "rightPanel";
	private static final String FB_MENU_ID2 = "fbMenu2";
	private static final String FB_RIGHT_ACCORDION_ID = "fbRightAccordion";
	private static final String PAGES_ACC_PANEL = "pagesAccPanel";
	private static final String VARIABLE_ACC_PANEL = "variablesAccPanel";
	private static final String ACCORDION_HEIGHT = "400";

	public FBWorkspace() {
		this(null);
	}
	
	public FBWorkspace(String id) {
		super(id, null);
	}

	@SuppressWarnings("unchecked")
	protected void initializeComponent(FacesContext context) {
		
		IWContext iwc = CoreUtil.getIWContext();
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		FormDocument fd = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		if (context.getExternalContext().getRequestParameterMap().containsKey(FormDocument.FROM_APP_REQ_PARAM)) {
			try {
				Map session_map = context.getExternalContext().getSessionMap();
				fd.setAppId((String)session_map.get(FormDocument.APP_ID_PARAM));
				fd.setPrimaryFormName((String)session_map.get(FormDocument.APP_FORM_NAME_PARAM));
				session_map.remove(FormDocument.APP_ID_PARAM);
				session_map.remove(FormDocument.APP_FORM_NAME_PARAM);
			} catch (Exception e) {
				iwc.sendRedirect("/workspace/forms");
				return;
			}
			
		}
		if(fd.getDocument() == null) {
			iwc.sendRedirect("/workspace/forms");
			return;
		}
		ProcessData pd = fd.getProcessData();
		if(pd != null) {
			if(pd.getProcessId() != null) {
				workspace.setProcessMode(true);
			}
		}

		Layer mainApplication = new Layer(Layer.DIV);
		
		Layer body = new Layer(Layer.DIV);
		body.setId(OPTIONS_PANEL_ID);
		
		Accordion acc = new Accordion(FB_MENU_ID);
		acc.setId(FB_MENU_ACCORDION_ID);
		acc.setUseSound(false);
		acc.setHeight(ACCORDION_HEIGHT);
		
		FBPalette palette = new FBPalette();
		palette.setItemStyleClass(PALETTE_COMPONENT_CLASS);
		palette.setStyleClass(COMPONENTS_LIST_CLASS);
		
		Text tab1 = new Text();
		tab1.setText(getLocalizedString(iwc, "fb_acc_comp_palette", "Component palette"));
		tab1.setStyleClass(FB_MENU_BAR_CLASS);
		
		acc.addPanel(tab1, palette);
		
		FBComponentProperties simpleProperties = new FBComponentProperties();
		
		Text tab2 = new Text();
		tab2.setText(getLocalizedString(iwc, "fb_acc_comp_properties", "Component properties"));
		tab2.setStyleClass(FB_MENU_BAR_CLASS);
		
		acc.addPanel(tab2, simpleProperties);
		
		body.add(acc);
		
		mainApplication.add(body);
		
		FBViewPanel views = new FBViewPanel();
		
		mainApplication.add(views);
		
		body = new Layer(Layer.DIV);
		body.setId(RIGHT_PANEL_ID);
		
		acc = new Accordion(FB_MENU_ID2);
		acc.setId(FB_RIGHT_ACCORDION_ID);
		acc.setUseSound(false);
		acc.setHeight(ACCORDION_HEIGHT);
		
		if(workspace.isProcessMode()) {
			FBVariableViewer variableViewer = new FBVariableViewer();
			
			tab2 = new Text();
			tab2.setText(getLocalizedString(iwc, "fb_acc_variables", "Variables and transitions"));
			tab2.setStyleClass(FB_MENU_BAR_CLASS);
			
			acc.addPanel(VARIABLE_ACC_PANEL, tab2, variableViewer);
		}
		
		FBPagesPanel pages = new FBPagesPanel();
		pages.setStyleClass(PAGES_PANEL);
		pages.setComponentStyleClass(FORM_PAGE_ICON_CLASS);
		pages.setGeneralPartStyleClass(PAGES_GENERAL_CONTAINER_CLASS);
		pages.setSpecialPartStyleClass(PAGES_SPECIAL_CONTAINER_CLASS);
		pages.setSelectedStyleClass(SELECTED_ELEMENT_CLASS);
		
		tab1 = new Text();
		tab1.setText(getLocalizedString(iwc, "fb_acc_sections", "Sections"));
		tab1.setStyleClass(FB_MENU_BAR_CLASS);
		
		acc.addPanel(PAGES_ACC_PANEL, tab1, pages);
		
		body.add(acc);
		
		mainApplication.add(body);
		
		add(mainApplication);
		
	}
	
}
