package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.webface.WFUtil;

public class FBFormComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormComponent";
	
	private static final String DELETE_BUTTON_FACET = "DELETE_BUTTON_FACET";
	
	private String id;
	private String styleClass;
	private Element element;
	private boolean submit;
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

	public boolean isSubmit() {
		return submit;
	}

	public void setSubmit(boolean submit) {
		this.submit = submit;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public FBFormComponent() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Element element;
		Application application = context.getApplication();
		getChildren().clear();
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		String currentLocale = "en";
		if(currentLocale != null) {
			Locale current = new Locale(currentLocale);
			if(current != null) {
				try {
					if(submit) {
						/*Element element = formManagerInstance.getLocalizedSubmitComponent(current);
						Element button = (Element) element.getFirstChild();
						button.setAttribute("disabled", "true");
						this.setElement(element);*/
					} else {
						element = page.getComponent(id).getHtmlRepresentation(current);
						element.setAttribute("id", id + "_i");
						setElement(element);
						setOnclick("editProperties(this.id)");
						HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
						deleteButton.setId("db" + id);
						deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
						deleteButton.setOnclick("deleteComponentJSF(this)");
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
			writer.writeAttribute("class", styleClass, "styleClass");
		} else {
			writer.writeAttribute("class", selectedStyleClass, "styleClass");
		}
		writer.writeAttribute("id", id, "id");
		if(submit) {
			writer.writeAttribute("onclick", "A4J.AJAX.Submit('_viewRoot','workspaceform1',event,{'parameters':{'workspaceform1:formHeadingHeaderS':'workspaceform1:formHeadingHeaderS'},'actionUrl':'/workspace/formbuilder/','single':true})", "onclick");
		} else {
			writer.writeAttribute("onclick", onclick, "onclick");
		}
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
		Object values[] = new Object[8];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = element;
		values[4] = submit;
		values[5] = onclick;
		values[6] = selected;
		values[7] = selectedStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		element = (Element) values[3];
		submit = (Boolean) values[4];
		onclick = (String) values[5];
		selected = (Boolean) values[6];
		selectedStyleClass = (String) values[7];
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
