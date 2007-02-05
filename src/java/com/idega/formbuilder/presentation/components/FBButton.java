package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class FBButton extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Button";
	
	public String id;
	public String styleClass;
	public Element element;
	
	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public FBButton() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
//		Application application = context.getApplication();
		getChildren().clear();
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		Locale current = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
		ButtonArea buttonArea = page.getButtonArea();
		if(buttonArea != null) {
			try {
				Element element = buttonArea.getComponent(id).getHtmlRepresentation(current);
				element.setAttribute("id", id + "_bt");
				setElement(element);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", id, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		DOMTransformer.renderNode(element, this, writer);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = element;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		element = (Element) values[3];
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
