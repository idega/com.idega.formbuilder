package com.idega.formbuilder.presentation.tags;

import javax.faces.component.UIComponent;

import com.idega.formbuilder.util.FBUtil;
import com.idega.webface.WFToolbarButton;
import com.idega.webface.WFToolbarButtonTag;


/**
 * Copyright (C) idega software 2006
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBToolbarButtonTag extends WFToolbarButtonTag {

	private String image;
	private String tooltip;
	private String onmousedown;
	private String onmouseover;

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public FBToolbarButtonTag() {
		super();
	}

	public String getComponentType() {
		return "FBToolbarButton";
	}
	
	public void release() {      
		super.release();      
		image = null;
	}

	protected void setProperties(UIComponent component) {      
	 
		super.setProperties(component);
		
		if(component == null)
			return;
		
		WFToolbarButton button = (WFToolbarButton)component;
		
		if(image != null) {

			String img_path = FBUtil.getBundle().getResourcesVirtualPath()+"/"+getRelativeImagePath()+"/";
			button.setDefaultImageURI(img_path+image);
			
			if(tooltip != null)
				if(isValueReference(tooltip)) {
                    javax.faces.el.ValueBinding vb = getFacesContext().getApplication().createValueBinding(tooltip);
                    button.setValueBinding("tooltip", vb);
                } else 
                    button.setDisplayText(tooltip);

			if(onmouseover != null)
				button.setHoverImageURI(img_path+onmouseover);
			
			if(onmousedown != null)
				button.setPressedImageURI(img_path+onmousedown);
		}
	}
	
	/**
	 * 
	 * @return path to image folder relative to resource path. 
	 */
	protected String getRelativeImagePath() {
		return "images";
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getOnmousedown() {
		return onmousedown;
	}

	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	public String getOnmouseover() {
		return onmouseover;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}
}
