package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.idega.block.form.presentation.FormViewer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.StringUtil;
import com.idega.util.expression.ELUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.Document;
import com.idega.xformsmanager.business.Form;
import com.idega.xformsmanager.business.PersistedFormDocument;
import com.idega.xformsmanager.business.PersistenceManager;
import com.idega.xformsmanager.business.XFormPersistenceType;

public class FBPreviewPage extends FBComponentBase {

	public static final String COMPONENT_TYPE = "PreviewPage";

	private static final String FB_ADMIN_PAGE_ID = "fbPreviewPage";
	private static final String FB_HP_HEADER_BLOCK_OD = "fbHomePageHeaderBlock";
	private static final String FB_HP_MAIN_BLOCK_OD = "fbHomePageMainBlock";
	private static final String FB_HP_LEFT = "fbHPLeft";
	private static final String HEADER_NAME_ID = "headerName";
	private static final String HEADER_SLOGAN_ID = "headerSlogan";
	private static final String CHOOSE_FORM_ID = "chooseForm";

//	private static final String standaloneFormType = "standalone";

	@Autowired
	@XFormPersistenceType(CoreConstants.REPOSITORY)
	private transient PersistenceManager persistenceManager;

	private String selectedStandaloneForm = CoreConstants.EMPTY;

	public String getSelectedStandaloneForm() {
		return selectedStandaloneForm;
	}

	public void setSelectedStandaloneForm(String selectedStandaloneForm) {
		this.selectedStandaloneForm = selectedStandaloneForm;
	}

	@Override
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		IWContext iwc = CoreUtil.getIWContext();
		Layer body = new Layer(Layer.DIV);
		body.setId(FB_ADMIN_PAGE_ID);

		Layer header = new Layer(Layer.DIV);
		header.setId(FB_HP_HEADER_BLOCK_OD);

		Layer headerPartLeft = new Layer(Layer.DIV);
		headerPartLeft.setId(FB_HP_LEFT);

		Text name = new Text();
		name.setText(getLocalizedString(iwc, "fb_admin_logo", "Formbuilder"));
		name.setId(HEADER_NAME_ID);
		Text slogan = new Text();
		slogan.setText(getLocalizedString(iwc, "fb_admin_slogan", "The easy way to build your forms"));
		slogan.setId(HEADER_SLOGAN_ID);

		headerPartLeft.add(name);
		headerPartLeft.add(slogan);
		header.add(headerPartLeft);

		body.add(header);

		FormDocument formDocument = (FormDocument)WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		Document xformsDocument = formDocument.getDocument();

		Layer mainPart = new Layer(Layer.DIV);
		mainPart.setId(FB_HP_MAIN_BLOCK_OD);

		Layer formChooserLayer = new Layer(Layer.DIV);
		formChooserLayer.setStyleClass(CHOOSE_FORM_ID);
		formChooserLayer.add(new Text(getLocalizedString(iwc, "standalone_form_chooser", "Standalone forms")));

		DropdownMenu menu = getFormsMenu();

		formChooserLayer.add(menu);
		mainPart.add(formChooserLayer);

		FormViewer formViewer = new FormViewer();

		if(xformsDocument != null && StringUtil.isEmpty(selectedStandaloneForm)) {
			formViewer.setFormId(formDocument.getFormId());
			formViewer.setXFormsDocument((org.w3c.dom.Document)xformsDocument.getXformsDocument().cloneNode(true));
			menu.setSelectedElement(formDocument.getFormId());
		} else if( !StringUtil.isEmpty(selectedStandaloneForm) ){
			formViewer.setFormId(selectedStandaloneForm);
			PersistedFormDocument form = getPersistenceManager().loadForm(Long.parseLong(selectedStandaloneForm));
			formViewer.setXFormsDocument(form.getXformsDocument());
		}

		mainPart.add(formViewer);
		body.add(mainPart);
		add(body);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		IWContext iwc = CoreUtil.getIWContext();
		String selectedFormId = iwc.getParameter(CHOOSE_FORM_ID);
		if(selectedFormId != getSelectedStandaloneForm()) {
			setSelectedStandaloneForm(selectedFormId);
			super.setInitialized(false);
		}


		super.encodeBegin(context);
	}

	private DropdownMenu getFormsMenu() {
		DropdownMenu menu = new DropdownMenu(CHOOSE_FORM_ID);
		menu.setID(CHOOSE_FORM_ID);
		List<Form> forms = getPersistenceManager().getStandaloneForms();

		menu.addMenuElement(CoreConstants.EMPTY, "not selected");
		for (Form form : forms) {
			menu.addMenuElement(form.getFormId().toString(), form.getDisplayName());
		}
		menu.setOnChange("this.form.submit();");
		menu.setSelectedElement(getSelectedStandaloneForm());
		return menu;
	}

	PersistenceManager getPersistenceManager() {
		if (persistenceManager == null)
			ELUtil.getInstance().autowire(this);
		return persistenceManager;
	}

}
