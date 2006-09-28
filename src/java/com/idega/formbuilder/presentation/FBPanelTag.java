package com.idega.formbuilder.presentation;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.shared_tomahawk.taglib.UIComponentTagBase;

public class FBPanelTag extends UIComponentTagBase {
	
	private String title;
	private String styleClass;
	private String top;
	private String left;
	private String expanded;
	
	public String getExpanded() {
		return expanded;
	}

	public void setExpanded(String expanded) {
		this.expanded = expanded;
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

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public FBPanelTag() {
		super();
	}
	
	public String getRendererType() {
		return "fb_panel";
	}
	
	public String getComponentType() {
		return "FBPanel";
	}

	public void release() {
		super.release();
		this.title = null;
	}
	
	public void setProperties(UIComponent component) {
	    super.setProperties(component);
	    if (component != null) {
	    	FBPanel panel = (FBPanel)component;
			if(this.title != null) {
				if (isValueReference(this.title)) {
	                ValueBinding vb = getFacesContext().getApplication().createValueBinding(this.title);
	                panel.setValueBinding("title", vb);
	            } else {
	            	panel.setTitle(this.title);
	            }
			}
			if(this.styleClass != null) {
				panel.setStyleClass(this.styleClass);
			}
			if(this.top != null) {
				panel.setTop(this.top);
			}
			if(this.left != null) {
				panel.setLeft(this.left);
			}
			if(this.expanded != null) {
				if(this.expanded.toLowerCase().equals("true")) {
					panel.setExpanded(true);
				} else if(this.expanded.toLowerCase().equals("false")) {
					panel.setExpanded(false);
				}
			}
	    }
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
