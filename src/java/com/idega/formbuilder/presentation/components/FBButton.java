package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Page;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.Layer;
import com.idega.webface.WFUtil;

public class FBButton extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Button";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png";
	private static final String SPEED_BUTTON_STYLE = "fbSpeedBButton";
	private static final String INLINE_STYLE = "display: inline;";
	
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
		this(null, null, null, null);
	}
	
	public FBButton(String buttonId) {
		this(buttonId, null, null, null);
	}
	
	public FBButton(String buttonId, String styleClass, String onSelect, String onDelete) {
		super();
		this.buttonId = buttonId;
		this.onSelect = onSelect;
		this.onDelete = onDelete;
		this.setStyleClass(styleClass);
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
						this.label = button.getProperties().getLabel().getString(FBUtil.getUILocale());
					}
				}
			}
		}
		
		writer.startElement(Layer.DIV, this);
		if(!isSelected()) {
			writer.writeAttribute("class", getStyleClass(), "styleClass");
		} else {
			writer.writeAttribute("class", selectedStyleClass, "styleClass");
		}
		
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("style", INLINE_STYLE, null);
		writer.writeAttribute("onclick", onSelect, "onclick");
		
		writer.startElement("input", null);
		writer.writeAttribute("type", "button", null);
		writer.writeAttribute("value", label, null);
		writer.writeAttribute("style", INLINE_STYLE, null);
		writer.writeAttribute("enabled", "false", null);
		writer.endElement("input");
		
		writer.startElement("img", null);
		writer.writeAttribute("class", SPEED_BUTTON_STYLE, null);
		writer.writeAttribute("src", DELETE_BUTTON_IMG, null);
		writer.writeAttribute("onclick", onDelete, "onclick");
		writer.endElement("img");
		
		writer.endElement(Layer.DIV);
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
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = label;
		values[2] = onSelect;
		values[3] = onDelete;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		label = (String) values[1];
		onSelect = (String) values[2];
		onDelete = (String) values[3];
	}
	
}
