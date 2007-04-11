package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.Page;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.webface.WFUtil;

public class FBFormComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormComponent";
	
	private static final String DELETE_BUTTON_FACET = "DELETE_BUTTON_FACET";
	
	private static final String DELETE_BUTTON_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png";
	
	private Element element;
	private String onLoad;
	private String onDelete;
	private String speedButtonStyleClass;

	public String getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(String onDelete) {
		this.onDelete = onDelete;
	}

	public String getOnLoad() {
		return onLoad;
	}

	public void setOnLoad(String onLoad) {
		this.onLoad = onLoad;
	}

	public FBFormComponent() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			Component component = page.getComponent(getId());
			if(component != null) {
				try {
					Locale current = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
					Element element = (Element) component.getHtmlRepresentation(current).cloneNode(true);
					if(element != null) {
						
						element.removeAttribute("id");
//						element.setAttribute("id", getId() + "_i");
						setElement(element);
						
						HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
						deleteButton.setId("db" + getId());
						deleteButton.setValue(DELETE_BUTTON_ICON);
						deleteButton.setOnclick(onDelete);
						deleteButton.setStyleClass(speedButtonStyleClass);
						
						addFacet(DELETE_BUTTON_FACET, deleteButton);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("onclick", onLoad, "onclick");
		DOMTransformer.renderNode(element, this, writer);
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
		UIComponent deleteButton = getFacet(DELETE_BUTTON_FACET);
		if(deleteButton != null) {
			renderChild(context, deleteButton);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = element;
		values[2] = onLoad;
		values[3] = onDelete;
		values[4] = speedButtonStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		element = (Element) values[1];
		onLoad = (String) values[2];
		onDelete = (String) values[3];
		speedButtonStyleClass = (String) values[4];
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getSpeedButtonStyleClass() {
		return speedButtonStyleClass;
	}

	public void setSpeedButtonStyleClass(String speedButtonStyleClass) {
		this.speedButtonStyleClass = speedButtonStyleClass;
	}

}
