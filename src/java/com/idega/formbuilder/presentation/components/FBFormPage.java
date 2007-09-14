package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.RenderUtils;

public class FBFormPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormPage";
	
	private static final String PAGE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/document-new.png";
	private static final String DELETE_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png";
	
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
		getChildren().clear();
		
		Layer pageLayer = new Layer(Layer.DIV);
		pageLayer.setId(getId() + "_page");
		pageLayer.setStyleClass(getStyleClass());
		pageLayer.setOnClick(onLoad);
		
		Image pageIconImg = new Image();
		pageIconImg.setId(getId() + "_pi");
		pageIconImg.setSrc(PAGE_ICON_IMG);
		pageIconImg.setStyleClass("pageIconIcon");
		
		Text pageIconLabel = new Text(label);
		pageIconLabel.setStyleClass("pageIconLabel");
		
		pageLayer.add(pageIconImg);
		pageLayer.add(pageIconLabel);
		
		if(!"".equals(onDelete)) {
			Image deleteButton = new Image();
			deleteButton.setId(getId() + "_db");
			deleteButton.setSrc(DELETE_ICON_IMG);
			deleteButton.setOnClick(onDelete);
			deleteButton.setStyleClass("speedButton");
			pageLayer.add(deleteButton);
		}
		add(pageLayer);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
//			if(isActive()) {
//				content.setStyleClass(activeStyleClass);
//				content.getChildren().remove(2);
//			}
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

}
