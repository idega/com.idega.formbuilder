package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.web2.business.Web2Business;
import com.idega.business.SpringBeanLookup;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Layer;
import com.idega.util.CoreUtil;

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
		getChildren().clear();
		
		Layer content = new Layer(Layer.DIV);
		content.setStyleClass(styleClass);
		content.setId(id + "Div");
		
		HtmlInputTextarea textarea = new HtmlInputTextarea();
		textarea.setRendered(true);
		textarea.setValueBinding("value", application.createValueBinding("#{formDocument.sourceCode}"));
		textarea.setWrap("false");
		textarea.setId("sourceTextarea");
		textarea.setStyleClass("codepress html linenumbers-on");
		
		HtmlCommandButton srcSubmit = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		srcSubmit.setId("saveSrcBtn");
		srcSubmit.setOnclick("saveSourceCode($('"+textarea.getClientId(context)+"').value); return false;");
		srcSubmit.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['change_source_code']}"));
		
		content.add(textarea);
		content.add(srcSubmit);
		
		addFacet(CONTENT_DIV_FACET, content);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		Web2Business web2 = (Web2Business) SpringBeanLookup.getInstance().getSpringBean(CoreUtil.getIWContext(), Web2Business.class);
		AddResource resourceAdder = AddResourceFactory.getInstance(context);
		resourceAdder.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, web2.getCodePressScriptFilePath());
		
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
