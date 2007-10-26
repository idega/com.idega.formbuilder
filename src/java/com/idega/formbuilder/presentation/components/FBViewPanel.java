package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Workspace;
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
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		String view = workspace.getView();
		if(PREVIEW_VIEW.equals(view)) {
			IFrame frame = new IFrame("lalal", PreviewPage.class);
			frame.setId("previewView");
			
			add(frame);
		} else if(SOURCE_VIEW.equals(view)) {
			FBSourceView sourceView = (FBSourceView) application.createComponent(FBSourceView.COMPONENT_TYPE);
			sourceView.setStyleClass("sourceView");
			sourceView.setId("sourceView");
			
			add(sourceView);
		} else if(DESIGN_VIEW.equals(view)) {
			FBDesignView designView = (FBDesignView) application.createComponent(FBDesignView.COMPONENT_TYPE);
			designView.setId("designView");
			designView.setStyleClass("dropBox");
			designView.setComponentStyleClass("formElement");
			designView.setSelectedStyleClass("formElement selectedElement");
			designView.setValueBinding("status", application.createValueBinding("#{workspace.designViewStatus}"));
			
			add(designView);
		}
	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
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
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
}