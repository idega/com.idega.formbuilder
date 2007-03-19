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
	
	private Element element;
	private String onclick;
	private boolean selected;
	private String selectedStyleClass;

	public String getSelectedStyleClass() {
		return selectedStyleClass;
	}

	public void setSelectedStyleClass(String selectedStyleClass) {
		this.selectedStyleClass = selectedStyleClass;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public FBFormComponent() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			String id = getId();
			Component component = page.getComponent(id);
			if(component != null) {
				try {
					Locale current = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
					Element element = component.getHtmlRepresentation(current);
//					System.out.println("__________________________________");
//					DOMUtil.prettyPrintDOM(element);
//					System.out.println("_______________xxx___________________");
					if(element != null) {
						element.setAttribute("id", id + "_i");
						setElement(element);
						setOnclick("loadComponentInfo(this.id)");
						
						HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
						deleteButton.setId("db" + id);
						deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png");
						deleteButton.setOnclick("removeComponent(this)");
						deleteButton.setStyleClass("speedButton");
						
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
		if(!isSelected()) {
			writer.writeAttribute("class", getStyleClass(), "styleClass");
		} else {
			writer.writeAttribute("class", selectedStyleClass, "styleClass");
		}
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("onclick", onclick, "onclick");
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
		values[2] = onclick;
		values[3] = selected;
		values[4] = selectedStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		element = (Element) values[1];
		onclick = (String) values[2];
		selected = (Boolean) values[3];
		selectedStyleClass = (String) values[4];
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
