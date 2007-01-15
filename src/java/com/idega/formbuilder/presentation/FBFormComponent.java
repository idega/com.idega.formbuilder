package com.idega.formbuilder.presentation;

import java.io.IOException;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.view.ActionManager;
import com.idega.presentation.IWBaseComponent;

public class FBFormComponent extends IWBaseComponent {
	
	public static final String COMPONENT_TYPE = "FormComponent";
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private static final String DELETE_BUTTON_FACET = "DELETE_BUTTON_FACET";
	
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
	
	public String getRendererType() {
		return null;
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		IFormManager formManagerInstance = ActionManager.getFormManagerInstance();
		String currentLocale = "en";
		if(currentLocale != null) {
			Locale current = new Locale(currentLocale);
			if(current != null) {
				try {
					if(submit) {
						Element element = formManagerInstance.getLocalizedSubmitComponent(current);
						Element button = (Element) element.getFirstChild();
						button.setAttribute("disabled", "true");
						this.setElement(element);
					} else {
						Element element = formManagerInstance.getLocalizedFormHtmlComponent(this.getId(), current);
						element.setAttribute("id", this.getId() + "_i");
						this.setElement(element);
						this.setOnclick("editProperties(this.id)");
						HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
						deleteButton.setId("db" + this.getId());
						deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
						deleteButton.setOnclick("deleteComponentJSF(this)");
						deleteButton.setStyleClass("speedButton");
						
						this.getFacets().put(DELETE_BUTTON_FACET, deleteButton);
					}
				} catch(FBPostponedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		boolean selected;
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		selected = isSelected();
		if(!selected) {
			writer.writeAttribute("class", getStyleClass(), "styleClass");
		} else {
			writer.writeAttribute("class", getSelectedStyleClass(), "styleClass");
		}
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("onclick", getOnclick(), "onclick");
		DOMTransformer.renderNode(getElement(), this, writer);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
//		int i = 0;
		if (!isRendered()) {
			return;
		}
//		i = this.getFacets().keySet().size();
		UIComponent deleteButton = getFacet(DELETE_BUTTON_FACET);
		if(deleteButton != null) {
			renderChild(context, deleteButton);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[7];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = element;
		values[3] = submit;
		values[4] = onclick;
		values[5] = selected;
		values[6] = selectedStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		element = (Element) values[2];
		submit = (Boolean) values[3];
		onclick = (String) values[4];
		selected = (Boolean) values[5];
		selectedStyleClass = (String) values[6];
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
