package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Page;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.GenericButton;
import com.idega.webface.WFUtil;

public class FBButton extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Button";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png";
	private static final String SPEED_BUTTON_STYLE = "fbSpeedBButton";
	private static final String INLINE_STYLE = "display: inline;";
	private static final String DEFAULT_BUTTON_CLASS = "formButton";
	
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
	
	protected void initializeComponent(FacesContext context) {
		Layer container = new Layer(Layer.DIV);
		container.setId(buttonId);
		container.setStyleAttribute(INLINE_STYLE);
		container.setOnClick(onSelect);
		if(buttonId != null) {
			Page page = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage();
			if(page != null) {
				ButtonArea area = page.getButtonArea();
				if(area != null) {
					Button button = (Button) area.getComponent(buttonId);
					if(button != null) {
						this.label = button.getProperties().getLabel().getString(FBUtil.getUILocale());
					}
				}
			}
		}
		
		if(!isSelected()) {
			container.setStyleClass(DEFAULT_BUTTON_CLASS);
		} else {
			container.setStyleClass(selectedStyleClass);
		}
		
		GenericButton button = new GenericButton();
		button.setValue(label);
		button.setStyleAttribute(INLINE_STYLE);
		button.setMarkupAttribute("enabled", "false");
		
		Image icon = new Image();
		icon.setStyleClass(SPEED_BUTTON_STYLE);
		icon.setSrc(DELETE_BUTTON_IMG);
		icon.setOnClick(onDelete);
		
		container.add(button);
		container.add(icon);
		
		add(container);
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
