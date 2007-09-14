package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Page;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.webface.WFUtil;

public class FBButton extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Button";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png";
	
	public String selectedStyleClass;
	public String label;
	public boolean selected;
	public String onSelect;
	public String onDelete;
	private String buttonId;
	
	public String getButtonId() {
		return buttonId;
	}

	public void setButtonId(String buttonId) {
		this.buttonId = buttonId;
	}

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
		new FBButton(null, null, null);
	}
	
	public FBButton(String buttonId) {
		new FBButton(buttonId, null, null);
	}
	
	public FBButton(String buttonId, String onSelect, String onDelete) {
		super();
		setRendererType(null);
		this.buttonId = buttonId;
		this.onSelect = onSelect;
		this.onDelete = onDelete;
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		if(buttonId != null) {
			Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
			if(page != null) {
				ButtonArea area = page.getButtonArea();
				if(area != null) {
					Button button = (Button) area.getComponent(buttonId);
					if(button != null) {
						setId(button.getId());
						this.label = button.getProperties().getLabel().getString(new Locale("en"));
					}
				}
			}
		}
		
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
		writer.writeAttribute("enabled", "false", null);
		writer.endElement("input");
		
		writer.startElement("img", null);
		writer.writeAttribute("class", "fbSpeedBButton", null);
		writer.writeAttribute("src", DELETE_BUTTON_IMG, null);
		writer.writeAttribute("onclick", onDelete, "onclick");
		writer.endElement("img");
		
		writer.endElement("div");
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}
	
}
