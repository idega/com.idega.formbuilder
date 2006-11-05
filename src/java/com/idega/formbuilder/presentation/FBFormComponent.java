package com.idega.formbuilder.presentation;

import java.util.Locale;

import javax.faces.application.Application;
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
	
	private String id;
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
		String currentLocale = ((Workspace) WFUtil.getBeanInstance("workspace")).getCurrentLocale();
		System.out.println("CURRENT LOCALE: " + currentLocale);
		//String currentLocale = "asdasd";
		IFormManager formManagerInstance = (IFormManager) context.getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		currentLocale = "en";
		if(currentLocale != null) {
			
			Locale current = new Locale(currentLocale);
			if(current != null) {
				System.out.println("CURRENT LOCALE: " + current.toString());
				if(submit) {
					Element element = formManagerInstance.getLocalizedSubmitComponent(current);
					//element.setAttribute("class", this.getStyleClass());
					Element button = (Element) element.getFirstChild();
					button.setAttribute("disabled", "true");
					this.setElement(element);
				} else {
					Element element = formManagerInstance.getLocalizedFormHtmlComponent(this.getId(), current);
					String id = element.getAttribute("id");
					this.setId(id);
					element.removeAttribute("id");
					//element.setAttribute("class", this.getStyleClass());
					this.setElement(element);
					this.setOnclick("editProperties(this)");
					System.out.println("INIT START CHILD COUNT: " + this.getChildCount());
					//Div infoButtonDiv = (Div) application.createComponent(Div.COMPONENT_TYPE);
					HtmlGraphicImage infoButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
					infoButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-find-replace.png");
					infoButton.setStyleClass("speedButton");
					//infoButton.setOnclick("editProperties(this)");
					HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
					deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
					deleteButton.setOnclick("deleteComponent(this)");
					deleteButton.setStyleClass("speedButton");
					//infoButtonDiv.getChildren().add(infoButton);
					this.getChildren().add(deleteButton);
					this.getChildren().add(infoButton);
				}
			}
		}
		
		System.out.println("INIT END CHILD COUNT: " + this.getChildCount());
	}
	
	public boolean getRendersChildren() {
		return true;
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

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
