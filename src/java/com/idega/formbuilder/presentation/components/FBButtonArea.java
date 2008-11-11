package com.idega.formbuilder.presentation.components;

import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;

public class FBButtonArea extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ButtonArea";
	
	private static final String DEFAULT_LOAD_ACTION = "loadButtonInfo(this);";
	private static final String DEFAULT_DELETE_ACTION = "removeButton(this);";
	private static final String NOBUTTONS_NOTICE_ID = "noButtonsNotice";
	
	public String componentStyleClass;
	
	public FBButtonArea() {}
	
	public FBButtonArea(String styleClass, String componentStyleClass) {
		setStyleClass(styleClass);
		this.componentStyleClass = componentStyleClass;
	}
	
	@Override
	protected void initializeComponent(FacesContext context) {
		Layer container = new Layer(Layer.DIV);
		container.setId("pageButtonArea");
		container.setStyleClass(getStyleClass());
		
		ButtonArea buttonArea = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage().getButtonArea();
		if(buttonArea != null) {
			List<String> ids = buttonArea.getContainedComponentsIds();
			if(ids == null || ids.isEmpty()) {
				IWContext iwc = CoreUtil.getIWContext();
				
				Layer emptyForm = new Layer(Layer.DIV);
				emptyForm.setId(NOBUTTONS_NOTICE_ID);
				
				Text emptyFormHeader = new Text(getLocalizedString(iwc, "labels_empty_buttons_text", "No buttons in this section at the moment"));
				emptyForm.add(emptyFormHeader);
				
				container.add(emptyForm);
			} else {
				for(String nextId : ids) {
					Button bt = (Button) buttonArea.getComponent(nextId);
					if(bt != null) {
						FBButton button = new FBButton();
						button.setLabel(FBUtil.getPropertyString(bt.getProperties().getLabel().getString(FBUtil.getUILocale())));
						button.setButtonId(nextId);
						button.setOnSelect(DEFAULT_LOAD_ACTION);
						button.setOnDelete(DEFAULT_DELETE_ACTION);
						container.add(button);
					}
				}
			}
		}
		add(container);
	}
	
	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}
	
}
