package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;

import com.idega.presentation.IWBaseComponent;
import com.idega.webface.WFBlock;
import com.idega.webface.WFTitlebar;

public class FBSubmitProperties extends IWBaseComponent {

	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "SubmitProperties";
	
	private static final String WFBLOCK_CONTENT_FACET = "WFBLOCK_CONTENT_FACET";
	
	public FBSubmitProperties() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBSubmitProperties.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {
//		System.out.println("INITIALIZING FBSubmitProperties");
		
		Application application = context.getApplication();
		
		WFTitlebar bar = new WFTitlebar();
		bar.addTitleText("Submission properties");
		
		WFBlock pageInfo = new WFBlock();
		pageInfo.setId("fbsubmitproperties");
		pageInfo.setRendered(true);
		pageInfo.setTitlebar(bar);
		
		
		HtmlOutputLabel titleLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		titleLabel.setValue("Form title");
		titleLabel.setFor("formTitle2");
		pageInfo.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("formTitle2");
		//title.setOnblur("applyChanges()");
		//title.setValueBinding("value", application.createValueBinding("#{component.label}"));
		pageInfo.add(title);
		
		this.getFacets().put(WFBLOCK_CONTENT_FACET, pageInfo);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		this.getChildren().clear();
		UIComponent body = (UIComponent) getFacet(WFBLOCK_CONTENT_FACET);
		if(body != null) {
			System.out.println("WFBLOCK_CONTENT_FACET");
			renderChild(context, body);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[1];
		values[0] = super.saveState(context);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}
	
}
