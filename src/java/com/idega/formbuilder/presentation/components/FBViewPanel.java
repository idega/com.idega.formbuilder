package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.presentation.pages.PreviewPage;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.IFrame;
import com.idega.webface.WFUtil;

public class FBViewPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ViewPanel";
	
	public static final int DESIGN_VIEW_INDEX = 0;
	public static final int PREVIEW_VIEW_INDEX = 1;
	public static final int SOURCE_VIEW_INDEX = 2;
	public static final String PREVIEW_VIEW = "preview";
	public static final String SOURCE_VIEW = "source";
	public static final String DESIGN_VIEW = "design";
	
	private static final String PREVIEW_IFRAME_NAME = "previewIFrame";
	private static final String PREVIEW_VIEW_ID = "previewView";
	private static final String SOURCE_VIEW_ID = "sourceView";
	private static final String DESIGN_VIEW_ID = "designView";
	private static final String DROPBOX_CLASS = "dropBox";
	private static final String FORM_ELEMENT_CLASS = "formElement";
	private static final String SELECTED_FORM_ELEMENT_CLASS = "selectedElement";

	public FBViewPanel() {
		super(null, null);
	}
	
	public FBViewPanel(String id, String styleClass) {
		super(id, styleClass);
	}
	
	protected void initializeComponent(FacesContext context) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("formContainer");
		body.setId("viewPanel");
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		String view = workspace.getView();
		if(PREVIEW_VIEW.equals(view)) {
			IFrame frame = new IFrame(PREVIEW_IFRAME_NAME, PreviewPage.class);
			frame.setId(PREVIEW_VIEW_ID);
			
			body.add(frame);
		} else if(SOURCE_VIEW.equals(view)) {
			FBSourceView sourceView = new FBSourceView();
			sourceView.setStyleClass(SOURCE_VIEW_ID);
			sourceView.setId(SOURCE_VIEW_ID);
			
			body.add(sourceView);
		} else if(DESIGN_VIEW.equals(view)) {
			FBDesignView designView = new FBDesignView();
			designView.setId(DESIGN_VIEW_ID);
			designView.setStyleClass(DROPBOX_CLASS);
			designView.setComponentStyleClass(FORM_ELEMENT_CLASS);
			designView.setSelectedStyleClass(FORM_ELEMENT_CLASS + " " + SELECTED_FORM_ELEMENT_CLASS);
			
			body.add(designView);
		}
		
		add(body);
	}

}