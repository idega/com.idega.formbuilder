package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.GenericButton;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;

public class FBButton extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Button";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png";
	private static final String SPEED_BUTTON_STYLE = "fbSpeedBButton";
	private static final String INLINE_STYLE = "float: left;";
	private static final String DEFAULT_BUTTON_CLASS = "formButton";
	private static final String HANDLER_LAYER_CLASS = "fbButtonHandler";
	private static final String ASSIGN_TRANS_BOX_CLASS = "assignTransitionBox";
	private static final String ASSIGN_LABEL_CLASS = "assignLabel";
	
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
		if(buttonId == null) {
			return;
		}
		
		IWContext iwc = IWContext.getIWContext(context);
		
		String transition = null;
		
		Layer container = new Layer(Layer.DIV);
		container.setId(buttonId);
		container.setStyleAttribute(INLINE_STYLE);
		container.setOnClick(onSelect);
		Page page = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				if(button != null) {
					this.label = FBUtil.getPropertyString(button.getProperties().getLabel().getString(FBUtil.getUILocale()));
						
					PropertiesButton properties = button.getProperties();
					if(properties.getReferAction() != null) {
						transition = properties.getReferAction();
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
		
		Layer handleLayer = new Layer(Layer.DIV);
		handleLayer.setStyleClass(HANDLER_LAYER_CLASS);
		
		container.add(handleLayer);
		container.add(button);
		container.add(icon);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		
		if(workspace.isProcessMode()) {
			Layer assignVariable = new Layer(Layer.DIV);
			assignVariable.setStyleClass(ASSIGN_TRANS_BOX_CLASS);
			assignVariable.setId("trans_" + buttonId);
							
			Text assignLabel = new Text();
			assignLabel.setStyleClass(ASSIGN_LABEL_CLASS);
			if(!StringUtils.isEmpty(transition)) {
				assignLabel.setText(getLocalizedString(iwc, "fb_assigned_to_label", "Assigned to: ") + transition);
			} else {
				assignLabel.setText(getLocalizedString(iwc, "fb_no_assign_label", "Not assigned"));
			}
			
			assignVariable.add(assignLabel);
			
			container.add(assignVariable);
		}
		
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
	
}
