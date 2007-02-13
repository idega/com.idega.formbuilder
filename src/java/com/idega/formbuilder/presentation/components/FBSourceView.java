package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.ajax.html.HtmlAjaxCommandButton;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFDivision;

public class FBSourceView extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "SourceView";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private String id;
	private String styleClass;
	private String value;
	
	public FBSourceView() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		WFDivision content = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		content.setStyleClass(styleClass);
		content.setId(id + "Div");
		
		HtmlInputTextarea textarea = new HtmlInputTextarea();
		textarea.setRendered(true);
		textarea.setValueBinding("value", application.createValueBinding("#{formDocument.sourceCode}"));
		textarea.setWrap("false");
		textarea.setId("sourceTextarea");
		
		HtmlAjaxCommandButton srcSubmit = (HtmlAjaxCommandButton) application.createComponent(HtmlAjaxCommandButton.COMPONENT_TYPE);
		srcSubmit.setId("saveSrcBtn");
		srcSubmit.setOnclick("$('workspaceform1:saveCode').click();");
		srcSubmit.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['change_source_code']}"));
		
		addChild(textarea, content);
		addChild(srcSubmit, content);
		
		addFacet(CONTENT_DIV_FACET, content);
		
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
