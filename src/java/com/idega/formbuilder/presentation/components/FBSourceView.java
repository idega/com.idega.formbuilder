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
	
	protected void initializeComponent(FacesContext context) {
		Layer content = new Layer(Layer.DIV);
		content.setStyleClass(getStyleClass());
		content.setId(getId() + DIV_POSTFIX);
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		Layer formHeading = new Layer(Layer.DIV);
		formHeading.setId(FORM_HEADING_ID);
		formHeading.setStyleClass(INFO_CLASS);
		
		Text formHeadingHeader = new Text(formDocument.getFormTitle());
		formHeadingHeader.setId(FORM_HEADING_HEADER_ID);
		formHeading.add(formHeadingHeader);
		
		TextArea textarea = new TextArea();
		textarea.setRendered(true);
		textarea.setId(SOURCE_BOX_ID);
		
		content.add(formHeading);
		content.add(textarea);
		
		add(content);
	}
	
}
