package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBButton extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Button";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png";
	
	public String selectedStyleClass;
	public String label;
	public boolean selected;
	public String onSelect;
	
	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getSelectedStyleClass() {
		return selectedStyleClass;
	}

	public void setSelectedStyleClass(String selectedStyleClass) {
		this.selectedStyleClass = selectedStyleClass;
	}

	public FBButton() {
		super();
		setRendererType(null);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("div", this);
		if(!isSelected()) {
			writer.writeAttribute("class", getStyleClass(), "styleClass");
		} else {
			writer.writeAttribute("class", selectedStyleClass, "styleClass");
		}
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("style", "display: inline;", null);
		writer.writeAttribute("onclick", onSelect, "onclick");
		
		writer.startElement("input", null);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", label, null);
		writer.writeAttribute("style", "display: inline;", null);
		writer.endElement("input");
		
		writer.startElement("img", null);
		//writer.writeAttribute("class", "speedButton", null);
		writer.writeAttribute("style", "display: inline;", null);
		writer.writeAttribute("src", DELETE_BUTTON_IMG, null);
		writer.endElement("img");
		
		writer.endElement("div");
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
