package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxCommandButton;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBSourceView extends FBComponentBase {
	
//	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "SourceView";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private String id;
	private String styleClass;
	private String value;
	
	public FBSourceView() {
		super();
		this.setRendererType(null);
	}
	
	/*public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBSourceView.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}*/
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		FBDivision content = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		content.setStyleClass(styleClass);
		content.setId(id + "Div");
		
		HtmlInputTextarea textarea = new HtmlInputTextarea();
		textarea.setRendered(true);
		textarea.setValueBinding("value", application.createValueBinding("#{formSourceCodeManager.sourceCode}"));
		textarea.setWrap("false");
		textarea.setId("sourceTextarea");
		
		UIAjaxCommandButton srcSubmit = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		srcSubmit.setId("saveSrcBtn");
		srcSubmit.setAjaxSingle(false);
		srcSubmit.setReRender("mainApplication");
		srcSubmit.setActionListener(application.createMethodBinding("#{formSourceCodeManager.processAction}", new Class[]{ActionEvent.class}));
		srcSubmit.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['change_source_code']}"));
		
		content.getChildren().add(textarea);
		content.getChildren().add(srcSubmit);
		
		this.getFacets().put(CONTENT_DIV_FACET, content);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!this.isRendered()) {
			return;
		}
		UIComponent content = getFacet(CONTENT_DIV_FACET);
		if(content != null) {
			renderChild(context, content);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = value;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		value = (String) values[3];
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
