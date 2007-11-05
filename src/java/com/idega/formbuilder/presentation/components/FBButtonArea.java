package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.Layer;
import com.idega.webface.WFUtil;

public class FBButtonArea extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ButtonArea";
	
	private static final String DEFAULT_LOAD_ACTION = "loadButtonInfo(this);";
	private static final String DEFAULT_DELETE_ACTION = "removeComponent(this);";
	
	public String componentStyleClass;
	
	public FBButtonArea() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		ButtonArea buttonArea = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage().getButtonArea();
		if(buttonArea != null) {
			List<String> ids = buttonArea.getContainedComponentsIdList();
			if(ids != null) {
				for(Iterator<String> it = ids.iterator(); it.hasNext(); ) {
					String nextId = (String) it.next();
					Button bt = (Button) buttonArea.getComponent(nextId);
					if(bt != null) {
						FBButton button = (FBButton) application.createComponent(FBButton.COMPONENT_TYPE);
						button.setLabel(bt.getProperties().getLabel().getString(FBUtil.getUILocale()));
						button.setId(nextId);
						button.setStyleClass(componentStyleClass);
						button.setOnSelect(DEFAULT_LOAD_ACTION);
						button.setOnDelete(DEFAULT_DELETE_ACTION);
						add(button);
					}
				}
			}
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement(Layer.DIV, this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement(Layer.DIV);
		super.encodeEnd(context);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = componentStyleClass;
		return values;
	}
	
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
