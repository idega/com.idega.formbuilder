package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.webface.WFUtil;

public class FBButtonArea extends FBComponentBase {

	public static final String COMPONENT_TYPE = "ButtonArea";
	
	public String id;
	public String styleClass;
	
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
			Iterator it = ids.iterator();
			while(it.hasNext()) {
				String nextId = (String) it.next();
				FBButton button = (FBButton) application.createComponent(FBButton.COMPONENT_TYPE);
				button.setId(nextId);
				add(button);
			}
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", id, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
}
