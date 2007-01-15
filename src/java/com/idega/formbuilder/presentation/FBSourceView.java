package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxCommandButton;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;

import com.idega.presentation.IWBaseComponent;

public class FBSourceView extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "SourceView";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private String styleClass;
	private String value;
	
	public FBSourceView() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBSourceView.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
//		String style = null;
		Application application = context.getApplication();
		this.getChildren().clear();
//		style = styleClass;
		FBDivision content = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
//		content.setStyleClass(styleClass);
//		content.setId(getId() + "qwerty");
		
		HtmlInputTextarea textarea = new HtmlInputTextarea();
		textarea.setRendered(true);
		textarea.setValueBinding("value", application.createValueBinding("#{formSourceCodeManager.sourceCode}"));
		textarea.setWrap("false");
		
		UIAjaxCommandButton srcSubmit = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		srcSubmit.setAjaxSingle(false);
		srcSubmit.setReRender("mainApplication");
		srcSubmit.setActionListener(application.createMethodBinding("#{formSourceCodeManager.processAction}", new Class[]{ActionEvent.class}));
		srcSubmit.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['change_source_code']}"));
		
		content.getChildren().add(textarea);
		content.getChildren().add(srcSubmit);
		
		this.getFacets().put(CONTENT_DIV_FACET, content);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		String style = null;
		if (!this.isRendered()) {
			return;
		}
		style = styleClass;
		FBDivision content = (FBDivision) getFacet(CONTENT_DIV_FACET);
		if(content != null) {
			content.setStyleClass(style);
			renderChild(context, content);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context); 
		values[1] = styleClass;
		values[2] = value;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		value = (String) values[2];
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

}
