package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.Layer;
import com.idega.webface.WFUtil;

public class FBButtonArea extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ButtonArea";
	
	private static final String DEFAULT_LOAD_ACTION = "loadButtonInfo(this);";
	private static final String DEFAULT_DELETE_ACTION = "removeButton(this);";
	
	public String componentStyleClass;
	
	@Override
	protected void initializeComponent(FacesContext context) {
		Layer container = new Layer(Layer.DIV);
		container.setId("pageButtonArea");
		container.setStyleClass(getStyleClass());
		
		ButtonArea buttonArea = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage().getButtonArea();
		if(buttonArea != null) {
			List<String> ids = buttonArea.getContainedComponentsIds();
			if(ids != null) {
				for(Iterator<String> it = ids.iterator(); it.hasNext(); ) {
					String nextId = it.next();
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
	
	@Override
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = componentStyleClass;
		return values;
	}
	
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		componentStyleClass = (String) values[1];
	}

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}
	
}
