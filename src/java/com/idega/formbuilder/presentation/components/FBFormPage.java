package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBFormPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormPage";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private static final String PAGE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-redo.png";
	
	private String id;
	private String styleClass;
	private String label;
	
	public FBFormPage() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		FBDivision switcher = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		switcher.setId("page" + id);
		switcher.setStyleClass("formPageIcon");
		
		HtmlGraphicImage pageIconImg = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		pageIconImg.setValue(PAGE_ICON_IMG);
		
		HtmlOutputText pageIconLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		pageIconLabel.setValue("Page Icon");
		
		switcher.getChildren().add(pageIconImg);
		switcher.getChildren().add(pageIconLabel);
		
		addFacet(CONTENT_DIV_FACET, switcher);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		UIComponent content = getFacet(CONTENT_DIV_FACET);
		if(content != null) {
			renderChild(context, content);
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getStyleClass() {
		return styleClass;
	}
	
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
