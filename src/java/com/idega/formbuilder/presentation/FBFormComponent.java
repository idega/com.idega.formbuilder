package com.idega.formbuilder.presentation;

import java.util.Iterator;
import java.util.Locale;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.Workspace;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;
import com.idega.webface.WFUtil;

public class FBFormComponent extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_formComponent";
	public static final String COMPONENT_TYPE = "FBFormComponent";
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private String styleClass;
	private Element element;
	private boolean submit;
	private String onclick;

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
	
	public void printChildrenIDs(UIComponent comp, FacesContext context) {
		Iterator it = comp.getChildren().iterator();
		while(it.hasNext()) {
			UIComponent c = (UIComponent) it.next();
			String nextId = c.getClientId(context);
			System.out.println("COMPONENT WITH ID: " + nextId + " (" + c.getParent().getId() + ")");
			printChildrenIDs(c, context);
		}
	}

	public FBFormComponent() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public void initializeComponent(FacesContext context) throws FBPostponedException {
		//Locale current = (Locale) FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		Application application = context.getApplication();
		this.getChildren().clear();
		String currentLocale = ((Workspace) WFUtil.getBeanInstance("workspace")).getCurrentLocale();
		IFormManager formManagerInstance = (IFormManager) context.getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		currentLocale = "en";
		if(currentLocale != null) {
			Locale current = new Locale(currentLocale);
			if(current != null) {
				if(submit) {
					Element element = formManagerInstance.getLocalizedSubmitComponent(current);
					Element button = (Element) element.getFirstChild();
					button.setAttribute("disabled", "true");
					this.setElement(element);
				} else {
					System.out.println(this.getId());
					Element element = formManagerInstance.getLocalizedFormHtmlComponent(this.getId(), current);
					element.setAttribute("id", this.getId() + "_i");
					this.setElement(element);
					this.setOnclick("editProperties(this)");
					HtmlGraphicImage infoButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
					infoButton.setId("ib" + this.getId());
					infoButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-find-replace.png");
					infoButton.setStyleClass("speedButton");
					//infoButton.setOnclick("editProperties(this)");
					HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
					deleteButton.setId("db" + this.getId());
					deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
					deleteButton.setOnclick("deleteComponent(this)");
					deleteButton.setStyleClass("speedButton");
					this.getChildren().add(deleteButton);
					this.getChildren().add(infoButton);
				}
			}
		}
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
