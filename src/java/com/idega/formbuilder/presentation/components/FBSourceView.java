package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.TextArea;
import com.idega.webface.WFUtil;

public class FBSourceView extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "SourceView";
	
	private static final String DIV_POSTFIX = "Div";
	private static final String FORM_HEADING_ID = "formHeading";
	private static final String INFO_CLASS = "info";
	private static final String FORM_HEADING_HEADER_ID = "formHeadingHeader";
	private static final String SOURCE_BOX_ID = "sourceTextarea";
//	private static final String CODEPRESS_BOX_CLASS = "codepress html linenumbers-on";
	
	protected void initializeComponent(FacesContext context) {
//		IWContext iwc = CoreUtil.getIWContext();
		
		Layer content = new Layer(Layer.DIV);
		content.setStyleClass(getStyleClass());
		content.setId(getId() + DIV_POSTFIX);
		
//		Web2Business web2 = (Web2Business) getBeanInstance("web2bean");
//		
//		List<String> scriptFiles = new ArrayList<String>();
//		scriptFiles.add(web2.getCodePressScriptFilePath());
//		
//		content.add(PresentationUtil.getJavaScriptSourceLines(scriptFiles));
		
//		Script script = new Script();
//		script.addScriptLine("CodePress.run();");
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		Layer formHeading = new Layer(Layer.DIV);
		formHeading.setId(FORM_HEADING_ID);
		formHeading.setStyleClass(INFO_CLASS);
		
		Text formHeadingHeader = new Text(formDocument.getFormTitle());
		formHeadingHeader.setId(FORM_HEADING_HEADER_ID);
		formHeading.add(formHeadingHeader);
		
//		Link saveLink = new Link(getLocalizedString(iwc, "fb_source_save_button", "Save changes"));
//		saveLink.setNoURL();
//		saveLink.setOnClick("fbsavesource();");
//		saveLink.setId("saveCodeButton");
//		saveLink.setStyleClass("rightButton");
//		
////		formHeading.add(saveLink);
		
		TextArea textarea = new TextArea();
		textarea.setRendered(true);
		textarea.setValue(formDocument.getSourceCode());
		textarea.setId(SOURCE_BOX_ID);
//		textarea.setStyleClass(CODEPRESS_BOX_CLASS);
		
		content.add(formHeading);
		content.add(textarea);
//		content.add(script);
		
		add(content);
	}
	
}
