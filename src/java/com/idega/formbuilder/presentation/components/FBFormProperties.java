package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.util.FBConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextArea;
import com.idega.util.CoreUtil;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBFormProperties extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormProperties";
	
	private static final String PROPERTIES_PANEL_SECTION_STYLE = "fbPropertiesPanelSection";
	private static final String PROPERTY_CLASS = "fbProperty";
	private static final String PANEL_ID = "formPropertiesPanel";
	
	public FBFormProperties() {
		super();
		setRendererType(null);
	}
	
	private Layer createPropertyContainer(String styleClass) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(styleClass);
		body.setStyleClass(PROPERTY_CLASS);
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
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = createPanelSection(PANEL_ID);
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		Layer line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
		
		CheckBox preview = new CheckBox();
		preview.setId("previewScreen");
		preview.setChecked(formDocument.isHasPreview());
		preview.setOnChange("saveHasPreview(this);");
		
		line.add(preview);
		line.add(new Label(getLocalizedString(iwc, "fb_form_property_has_preview", "Form contains preview"), preview));
		body.add(line);
		
		PageThankYou submitPage = formDocument.getSubmitPage();
		String submitPageText = null;
		if(submitPage != null) {
			submitPageText = formDocument.getThankYouText();
		}
		
		line = createPropertyContainer(FBConstants.TWO_LINE_PROPERTY);
		
		TextArea thankYouText = new TextArea("thankYouText", submitPageText);
		thankYouText.setId("thankYouText");
		thankYouText.setOnBlur("saveThankYouText(this.value)");
		thankYouText.setOnKeyDown("savePropertyOnEnter(this.value,'formThxText',event);");
		
		line.add(new Label(getLocalizedString(iwc, "fb_form_property_thx_text", "Thank You Text"), thankYouText));
		line.add(thankYouText);
		body.add(line);
		
		add(body);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
}
