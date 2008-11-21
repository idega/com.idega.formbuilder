package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.xformsmanager.business.component.Page;

public class FBFormPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormPage";
	
	private static final String PAGE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png";
	private static final String DELETE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png";
	private static final String DEFAULT_ICON_STYLE_CLASS = "pageIconIcon";
	private static final String DEFAULT_LABEL_STYLE_CLASS = "pageIconLabel";
	private static final String DEFAULT_SPEED_BUTTON_STYLE_CLASS = "pageSpeedButton";
	private static final String FB_PAGE_HANDLER = "fbPageHandler";
	private static final String SPECIAL = "special";
	private static final String REL_ATTRIBUTE = "rel";
	
	private Page page;
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public FBFormPage(Page page) {
		this(page, null);
	}
	
	public FBFormPage(Page page, String styleClass) {
		super(null, styleClass);
		this.page = page;
	}
	
	protected void initializeComponent(FacesContext context) {
		if(page == null) {
			return;
		}
		
		Layer pageLayer = new Layer(Layer.DIV);
		pageLayer.setId(page.getId());
		pageLayer.setStyleClass(getStyleClass());
		
		if(!page.isSpecialPage()) {
			Layer handleLayer = new Layer(Layer.DIV);
			handleLayer.setStyleClass(FB_PAGE_HANDLER);
			pageLayer.add(handleLayer);
		} else {
			pageLayer.setMarkupAttribute(REL_ATTRIBUTE, SPECIAL);
		}
		
		Image pageIconImg = new Image();
		pageIconImg.setSrc(PAGE_ICON_IMG);
		pageIconImg.setStyleClass(DEFAULT_ICON_STYLE_CLASS);
		
		Text pageIconLabel = new Text(FBUtil.getPropertyString(page.getProperties().getLabel().getString(FBUtil.getUILocale())));
		pageIconLabel.setStyleClass(DEFAULT_LABEL_STYLE_CLASS);
		
		pageLayer.add(pageIconImg);
		pageLayer.add(pageIconLabel);
		
		if(!page.isSpecialPage()) {
			Image deleteButton = new Image();
			deleteButton.setSrc(DELETE_ICON_IMG);
			deleteButton.setStyleClass(DEFAULT_SPEED_BUTTON_STYLE_CLASS);
			pageLayer.add(deleteButton);
		}
		add(pageLayer);
	}
	
}
