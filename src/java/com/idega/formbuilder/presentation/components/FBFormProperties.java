package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.documentmanager.manager.impl.FormManager;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.util.FBConstants;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.CoreUtil;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBFormProperties extends FBComponentBase {
	
	private static Log logger = LogFactory.getLog(FormManager.class);
	
	public static final String COMPONENT_TYPE = "FormProperties";
	
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	
	public FBFormProperties() {
		super();
		setRendererType(null);
	}
	
	private Layer createPropertyContainer(String styleClass) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(styleClass);
		return body;
	}
	
	private Layer createPanelSection(String id) {
		Layer body = new Layer(Layer.DIV);
		body.setId(id);
		body.setStyleClass(PROPERTIES_PANEL_SECTION_STYLE);
		return body;
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		
		Layer body = createPanelSection("formPropertiesPanel");
		Layer line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		
		try {
			if(document == null) {
				String formId = (String) CoreUtil.getIWContext().getExternalContext().getSessionMap().get(FBConstants.FORM_DOCUMENT_ID);
				document = formDocument.initializeBeanInstance(formId);
			}
		} catch(Exception e) {
			logger.error("Exception while initializing fresh instance of XForms document");
			throw new IllegalStateException();
		}
		
		TextInput title = new TextInput("formTitle", formDocument.getFormTitle());
		title.setId("formTitle");
		title.setOnBlur("saveFormTitle(this.value)");
		title.setOnKeyDown("savePropertyOnEnter(this.value,'formTitle',event);");
		
		line.add(new Text("Form title"));
		line.add(title);
		body.add(line);
		
		line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
		
		CheckBox preview = new CheckBox();
		preview.setId("previewScreen");
		preview.setChecked(formDocument.isHasPreview());
		preview.setOnChange("saveHasPreview(this);");
		
		line.add(new Text("Form contains preview"));
		line.add(preview);
		body.add(line);
		
//		CheckBox processForm = new CheckBox();
//		processForm.setId("isProcessForm");
//		processForm.setChecked(formDocument.isProcessForm());
//		processForm.setOnChange("saveIsProcessForm(this);");
//		
//		line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
//		line.add(new Text("(Temporary) Represents process task"));
//		line.add(processForm);
//		body.add(line);
		
		line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
		
		PageThankYou submitPage = formDocument.getSubmitPage();
		String submitPageTitle = null;
		String submitPageText = null;
		if(submitPage != null) {
			submitPageTitle = formDocument.getThankYouTitle();
			submitPageText = formDocument.getThankYouText();
		}
		
		TextInput thankYouTitle = new TextInput("thankYouTitle", submitPageTitle);
		thankYouTitle.setId("thankYouTitle");
		thankYouTitle.setOnBlur("saveThankYouTitle(this.value)");
		thankYouTitle.setOnKeyDown("savePropertyOnEnter(this.value,'formThxTitle',event);");
		
		line.add(new Text("Thank you title"));
		line.add(thankYouTitle);
		body.add(line);
		
		line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
		
		TextArea thankYouText = new TextArea("thankYouText", submitPageText);
		thankYouText.setId("thankYouText");
		thankYouText.setOnBlur("saveThankYouText(this.value)");
		thankYouText.setOnKeyDown("savePropertyOnEnter(this.value,'formThxText',event);");
		
		line.add(new Text("Thank You Text"));
		line.add(thankYouText);
		body.add(line);
		
//		line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
//		
//		HtmlOutputLabel showFormStepsLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
//		showFormStepsLabel.setValue("Enable section visualization");
//		
//		HtmlSelectBooleanCheckbox showFormStepsChbx = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
//		showFormStepsChbx.setId("visualization");
//		showFormStepsChbx.setValueBinding("value", application.createValueBinding("#{formDocument.enableBubbles}"));
//		showFormStepsChbx.setOnchange("saveEnableBubbles(this);");
//		
//		line.add(showFormStepsLabel);
//		line.add(showFormStepsChbx);
//		body.add(line);
		
		add(body);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
}
