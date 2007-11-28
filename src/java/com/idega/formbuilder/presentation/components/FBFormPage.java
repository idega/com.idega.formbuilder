package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;

public class FBFormPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormPage";
	
	private static final String PAGE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png";
	private static final String DELETE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png";
	private static final String DEFAULT_LOAD_ACTION = "loadPageInfo(this.id);";
	private static final String DEFAULT_DELETE_ACTION = "deletePage(event);";
	private static final String DEFAULT_STYLE_CLASS = "formPageIcon";
	private static final String DEFAULT_ICON_STYLE_CLASS = "pageIconIcon";
	private static final String DEFAULT_LABEL_STYLE_CLASS = "pageIconLabel";
	private static final String DEFAULT_SPEED_BUTTON_STYLE_CLASS = "pageSpeedButton";
	private static final String PAGE_ID_POSTFIX = "_page";
	private static final String ICON_ID_POSTFIX = "_pi";
	private static final String SPEED_BUTTON_ID_POSTFIX = "_db";
	
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
	
	public FBFormPage(String id, String label, boolean special) {
		this(id, label);
		if(special) {
			this.onDelete = "";
		}
	}
	
	public FBFormPage(String id, String label) {
		setRendererType(null);
		setId(id);
		setStyleClass(DEFAULT_STYLE_CLASS);
		this.label = label;
		this.onDelete = DEFAULT_DELETE_ACTION;
		this.onLoad = DEFAULT_LOAD_ACTION;
	}
	
	protected void initializeComponent(FacesContext context) {
		Layer pageLayer = new Layer(Layer.DIV);
		pageLayer.setId(getId() + PAGE_ID_POSTFIX);
		pageLayer.setStyleClass(getStyleClass());
		pageLayer.setOnClick(onLoad);
		
		if(!"".equals(onDelete)) {
			Layer handleLayer = new Layer(Layer.DIV);
			handleLayer.setStyleClass("fbPageHandler");
			pageLayer.add(handleLayer);
		}
		
		Image pageIconImg = new Image();
		pageIconImg.setId(getId() + ICON_ID_POSTFIX);
		pageIconImg.setSrc(PAGE_ICON_IMG);
		pageIconImg.setStyleClass(DEFAULT_ICON_STYLE_CLASS);
		
		Text pageIconLabel = new Text(label);
		pageIconLabel.setStyleClass(DEFAULT_LABEL_STYLE_CLASS);
		
		pageLayer.add(pageIconImg);
		pageLayer.add(pageIconLabel);
		
		if(!"".equals(onDelete)) {
			Image deleteButton = new Image();
			deleteButton.setId(getId() + SPEED_BUTTON_ID_POSTFIX);
			deleteButton.setSrc(DELETE_ICON_IMG);
			deleteButton.setOnClick(onDelete);
			deleteButton.setStyleClass(DEFAULT_SPEED_BUTTON_STYLE_CLASS);
			pageLayer.add(deleteButton);
		}
		add(pageLayer);
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

}
