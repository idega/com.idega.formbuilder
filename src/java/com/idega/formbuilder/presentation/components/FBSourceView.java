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
	
	protected void initializeComponent(FacesContext context) {
		Layer content = new Layer(Layer.DIV);
		content.setStyleClass(getStyleClass());
		content.setId(getId() + "Div");
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		Layer formHeading = new Layer(Layer.DIV);
		formHeading.setId("formHeading");
		formHeading.setStyleClass("info");
		
		Text formHeadingHeader = new Text(formDocument.getFormTitle());
		formHeadingHeader.setId("formHeadingHeader");
		formHeading.add(formHeadingHeader);
		
		TextArea textarea = new TextArea();
		textarea.setRendered(true);
		textarea.setValue(formDocument.getSourceCode());
		textarea.setId("sourceTextarea");
		textarea.setStyleClass("codepress html linenumbers-on");
		
		content.add(formHeading);
		content.add(textarea);
		
		add(content);
	}
	
}
