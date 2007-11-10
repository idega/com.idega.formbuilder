package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.presentation.pages.PreviewPage;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.IFrame;
import com.idega.util.RenderUtils;
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
		super();
		setRendererType(null);
	}
	
	public FBViewPanel(String id, String styleClass) {
		super(id, styleClass);
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		String view = workspace.getView();
		if(PREVIEW_VIEW.equals(view)) {
			IFrame frame = new IFrame(PREVIEW_IFRAME_NAME, PreviewPage.class);
			frame.setId(PREVIEW_VIEW_ID);
			
			add(frame);
		} else if(SOURCE_VIEW.equals(view)) {
			FBSourceView sourceView = (FBSourceView) application.createComponent(FBSourceView.COMPONENT_TYPE);
			sourceView.setStyleClass(SOURCE_VIEW_ID);
			sourceView.setId(SOURCE_VIEW_ID);
			
			add(sourceView);
		} else if(DESIGN_VIEW.equals(view)) {
			FBDesignView designView = (FBDesignView) application.createComponent(FBDesignView.COMPONENT_TYPE);
			designView.setId(DESIGN_VIEW_ID);
			designView.setStyleClass(DROPBOX_CLASS);
			designView.setComponentStyleClass(FORM_ELEMENT_CLASS);
			designView.setSelectedStyleClass(FORM_ELEMENT_CLASS + " " + SELECTED_FORM_ELEMENT_CLASS);
			
			add(designView);
		}
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement(Layer.DIV, this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
 	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement(Layer.DIV);
		super.encodeEnd(context);
	}
	
}