package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFDivision;

public class FBFormPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormPage";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private static final String PAGE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png";
	private static final String DELETE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png";
	
	private String label;
	private boolean active;
	private String activeStyleClass;
	private String onDelete;
	private String onLoad;
	
	public String getOnLoad() {
		return onLoad;
	}

	public void setOnLoad(String onLoad) {
		this.onLoad = onLoad;
	}

	public String getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}

	public String getActiveStyleClass() {
		return activeStyleClass;
	}

	public void setActiveStyleClass(String activeStyleClass) {
		this.activeStyleClass = activeStyleClass;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public FBFormPage() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision switcher = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		switcher.setId(getId() + "_page");
		switcher.setStyleClass(getStyleClass());
		
		HtmlGraphicImage pageIconImg = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		pageIconImg.setId(getId() + "_pi");
		pageIconImg.setValue(PAGE_ICON_IMG);
		pageIconImg.setOnclick(onLoad);
		pageIconImg.setStyle("display: block");
		
		HtmlOutputText pageIconLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		pageIconLabel.setValue(label);
		pageIconLabel.setStyle("display: block");
		
		addChild(pageIconImg, switcher);
		addChild(pageIconLabel, switcher);
		
		if(onDelete != null && !onDelete.equals("")) {
			HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
			deleteButton.setId(getId() + "_db");
			deleteButton.setValue(DELETE_ICON_IMG);
			deleteButton.setOnclick(onDelete);
			deleteButton.setStyleClass("speedButton");
			addChild(deleteButton, switcher);
		}
		addFacet(CONTENT_DIV_FACET, switcher);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		WFDivision content = (WFDivision) getFacet(CONTENT_DIV_FACET);
		if(content != null) {
			if(isActive()) {
				content.setStyleClass(activeStyleClass);
				content.getChildren().remove(2);
			}
			renderChild(context, content);
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

}
