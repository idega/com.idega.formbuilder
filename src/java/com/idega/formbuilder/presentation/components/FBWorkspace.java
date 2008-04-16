package com.idega.formbuilder.presentation.components;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.dao.EmptyResultDataAccessException;

import com.idega.formbuilder.business.process.XFormsProcessManager;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.jbpm.def.ViewToTask;
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
	private static final String FORM_LIST_URL = "/workspace/forms";
	private static final String TAB_TITLE_CLASS = "title";
	private static final String PALETTE_COMPONENT_CLASS = "paletteComponent";
	private static final String COMPONENTS_LIST_CLASS = "componentsList";
	private static final String LEFT_ACC_ID = "accordionLeft";
	private static final String RIGHT_ACC_ID = "accordionRight";
	private static final String RIGHT_PANEL_ID = "rightPanel";
	private static final String TOGGLER_CLASS = "toggler";
	private static final String ELEMENT_CLASS = "element";
	private static final String AT_START_CLASS = "atStart";
	private static final String AT_START_RIGHT_CLASS = "atStartRight";
	private static final String FIRST_TOGGLER_CLASS = "firstToggler";
	private static final String LEFT_PANEL_0 = "panel0Content";
	private static final String LEFT_PANEL_1 = "panel1Content";
	private static final String RIGHT_PANEL_0 = "panel0Content2";
	private static final String RIGHT_PANEL_1 = "panel1Content2";

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
				iwc.sendRedirect(FORM_LIST_URL);
				return;
			}
			
		}
		if(fd.getDocument() == null) {
			iwc.sendRedirect(FORM_LIST_URL);
			return;
		}
		
		XFormsProcessManager xformsProcessManager = (XFormsProcessManager) WFUtil.getBeanInstance("xformsProcessManager");
		ViewToTask viewToTaskBinnder = xformsProcessManager.getViewToTaskBinder();
		
		Long task = null;
		try {
			task = viewToTaskBinnder.getTask(fd.getFormId());
		} catch(EmptyResultDataAccessException e) {
			workspace.setProcessMode(false);
		}
		
		if(task != null && task.intValue() > 0) {
			workspace.setProcessMode(true);
		} else {
			workspace.setProcessMode(false);
		}
		
		Layer mainApplication = new Layer(Layer.DIV);
		
		Layer body = new Layer(Layer.DIV);
		body.setId(OPTIONS_PANEL_ID);
		
		Layer leftAccordion = new Layer(Layer.DIV);
		leftAccordion.setId(LEFT_ACC_ID);
		
		Layer tab1 = new Layer(Layer.SPAN);
		tab1.setStyleClass(TOGGLER_CLASS);
		tab1.setStyleClass(AT_START_CLASS);
		tab1.setStyleClass(FIRST_TOGGLER_CLASS);
		
		Text tab1Title = new Text(getLocalizedString(iwc, "fb_acc_comp_palette", "Component palette"));
		tab1Title.setStyleClass(TAB_TITLE_CLASS);
		tab1.add(tab1Title);
		
		leftAccordion.add(tab1);
		
		Layer panel1 = new Layer(Layer.DIV);
		panel1.setId(LEFT_PANEL_0);
		panel1.setStyleClass(ELEMENT_CLASS);
		panel1.setStyleClass(AT_START_CLASS);
		
		FBPalette palette = new FBPalette();
		palette.setItemStyleClass(PALETTE_COMPONENT_CLASS);
		palette.setStyleClass(COMPONENTS_LIST_CLASS);
		
		panel1.add(palette);
		
		leftAccordion.add(panel1);
		
		Layer tab2 = new Layer(Layer.SPAN);
		tab2.setStyleClass(TOGGLER_CLASS);
		tab2.setStyleClass(AT_START_CLASS);
		
		
		Text tab2Title = new Text(getLocalizedString(iwc, "fb_acc_comp_properties", "Component properties"));
		tab2Title.setStyleClass(TAB_TITLE_CLASS);
		tab2.add(tab2Title);
		
		leftAccordion.add(tab2);
		
		Layer panel2 = new Layer(Layer.DIV);
		panel2.setStyleClass(ELEMENT_CLASS);
		panel2.setStyleClass(AT_START_CLASS);
		panel2.setId(LEFT_PANEL_1);
		
		FBComponentProperties simpleProperties = new FBComponentProperties();
		
		panel2.add(simpleProperties);
		
		leftAccordion.add(panel2);
		
		body.add(leftAccordion);
		
		mainApplication.add(body);
		
		FBViewPanel views = new FBViewPanel();
		
		mainApplication.add(views);
		
		body = new Layer(Layer.DIV);
		body.setId(RIGHT_PANEL_ID);
		
		Layer rightAccordion = new Layer(Layer.DIV);
		rightAccordion.setId(RIGHT_ACC_ID);
		
		if(workspace.isProcessMode()) {
			tab1 = new Layer(Layer.SPAN);
			tab1.setStyleClass(TOGGLER_CLASS);
			tab1.setStyleClass(AT_START_RIGHT_CLASS);
			tab1.setStyleClass(FIRST_TOGGLER_CLASS);
			
			tab1Title = new Text(getLocalizedString(iwc, "fb_acc_variables", "Variables and transitions"));
			tab1Title.setStyleClass(TAB_TITLE_CLASS);
			tab1.add(tab1Title);
			
			rightAccordion.add(tab1);
			
			panel1 = new Layer(Layer.DIV);
			panel1.setId(RIGHT_PANEL_0);
			panel1.setStyleClass(ELEMENT_CLASS);
			panel1.setStyleClass(AT_START_RIGHT_CLASS);
			
			FBVariableViewer variableViewer = new FBVariableViewer();
			
			panel1.add(variableViewer);
			
			rightAccordion.add(panel1);
			
		}
		
		tab2 = new Layer(Layer.SPAN);
		tab2.setStyleClass(TOGGLER_CLASS);
		tab2.setStyleClass(AT_START_RIGHT_CLASS);
		
		
		tab2Title = new Text(getLocalizedString(iwc, "fb_acc_sections", "Sections"));
		tab2Title.setStyleClass(TAB_TITLE_CLASS);
		tab2.add(tab2Title);
		
		rightAccordion.add(tab2);
		
		panel2 = new Layer(Layer.DIV);
		panel2.setStyleClass(ELEMENT_CLASS);
		panel2.setStyleClass(AT_START_RIGHT_CLASS);
		panel2.setId(RIGHT_PANEL_1);
		
		FBPagesPanel pages = new FBPagesPanel();
		pages.setStyleClass(PAGES_PANEL);
		pages.setComponentStyleClass(FORM_PAGE_ICON_CLASS);
		pages.setGeneralPartStyleClass(PAGES_GENERAL_CONTAINER_CLASS);
		pages.setSpecialPartStyleClass(PAGES_SPECIAL_CONTAINER_CLASS);
		pages.setSelectedStyleClass("selectedPage");
		
		panel2.add(pages);
		
		rightAccordion.add(panel2);
		
		body.add(rightAccordion);
		
		mainApplication.add(body);
		
		add(mainApplication);
		
	}
	
}
