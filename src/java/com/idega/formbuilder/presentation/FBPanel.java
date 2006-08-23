package com.idega.formbuilder.presentation;

import javax.faces.component.UIPanel;
import javax.faces.el.ValueBinding;

public class FBPanel extends UIPanel {
	
	private static final String RENDERER_TYPE = "fb_panel";
	private static final String EXPAND_IMG = "";
	private static final String COLLAPSE_IMG = "";
	
	private String title;
	private String styleClass;
	private String top;
	private String left;
	//private boolean expanded;
	
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public FBPanel() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}
	
	public String getFamily() {
		return ("formbuilder");
	}

	public String getTitle() {
		if(this.title != null) {
			return this.title;
		}
		ValueBinding binding = getValueBinding("title");
		return (binding != null) ? (String) binding.getValue(getFacesContext()) : null;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
