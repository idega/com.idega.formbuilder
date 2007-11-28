package com.idega.formbuilder.presentation.components;

import java.util.Map;

import javax.faces.context.FacesContext;

import com.idega.block.web2.presentation.Accordion;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
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

	public FBWorkspace() {
		this(null);
	}
	
	public FBWorkspace(String id) {
		super(id, null);
	}

	@SuppressWarnings("unchecked")
	protected void initializeComponent(FacesContext context) {
		
		IWContext iwc = CoreUtil.getIWContext();

		if (context.getExternalContext().getRequestParameterMap().containsKey(FormDocument.FROM_APP_REQ_PARAM)) {
			FormDocument fd = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
			try {
				Map session_map = context.getExternalContext().getSessionMap();
				fd.setAppId((String)session_map.get(FormDocument.APP_ID_PARAM));
				fd.setPrimaryFormName((String)session_map.get(FormDocument.APP_FORM_NAME_PARAM));
				session_map.remove(FormDocument.APP_ID_PARAM);
				session_map.remove(FormDocument.APP_FORM_NAME_PARAM);
			} catch (Exception e) {
				// TODO: use logger and redirect back to applications list if
				// possible
				e.printStackTrace();
			}
		}

		Layer mainApplication = new Layer(Layer.DIV);
		
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
		
		mainApplication.add(body);
		
//		FBMenu menu = new FBMenu();
		
		FBViewPanel views = new FBViewPanel();
		
		mainApplication.add(views);
		
		body = new Layer(Layer.DIV);
		body.setId("rightPanel");
		
		acc = new Accordion("fbMenu2");
		acc.setId("fbRightAccordion");
		acc.setUseSound(false);
		acc.setHeight("400");
		
		FBPagesPanel pages = new FBPagesPanel();
		pages.setStyleClass(PAGES_PANEL);
		pages.setComponentStyleClass(FORM_PAGE_ICON_CLASS);
		pages.setGeneralPartStyleClass(PAGES_GENERAL_CONTAINER_CLASS);
		pages.setSpecialPartStyleClass(PAGES_SPECIAL_CONTAINER_CLASS);
		pages.setSelectedStyleClass(SELECTED_ELEMENT_CLASS);
		
		tab1 = new Text();
		tab1.setText("Sections");
		tab1.setStyleClass("fbMenuTabBar");
		
		acc.addPanel("pagesAccPanel", tab1, pages);
		
		FBVariableViewer variableViewer = new FBVariableViewer();
		
		tab2 = new Text();
		tab2.setText("Variables and transitions");
		tab2.setStyleClass("fbMenuTabBar");
		
		acc.addPanel("variablesAccPanel", tab2, variableViewer);
		
		body.add(acc);
		
		mainApplication.add(body);
		
		add(mainApplication);
	}
}
