package com.idega.formbuilder.presentation;

import javax.faces.component.UIPanel;
import javax.faces.el.ValueBinding;

public class FBPanel extends UIPanel {
	
	private static final String RENDERER_TYPE = "fb_panel";
	private static final String EXPAND_IMG = "../resources/style/green_down.gif";
	private static final String COLLAPSE_IMG = "../resources/style/green_up.gif";
	
	private String title;
	private String styleClass;
	private String top;
	private String left;
	private boolean expanded = true;
	
	public String getCurrentIcon() {
		return (expanded) ? EXPAND_IMG : COLLAPSE_IMG;
	}
	
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

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

}
