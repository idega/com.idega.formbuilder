package com.idega.formbuilder.presentation;

import java.util.Locale;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;

public class FBFormComponent extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_formComponent";
	public static final String COMPONENT_TYPE = "FBFormComponent";
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	private String id;
	private String styleClass;
	private Element element;
	private boolean submit;

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
		Locale current = (Locale) FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		//String currentLocale = ((Workspace) WFUtil.getBeanInstance("workspace")).getCurrentLocale();
		String currentLocale = "asdasd";
		IFormManager formManagerInstance = (IFormManager) context.getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		if(currentLocale != null) {
			//Locale current = new Locale(currentLocale);
			if(submit) {
				Element element = formManagerInstance.getLocalizedSubmitComponent(current);
				element.setAttribute("class", this.getStyleClass());
				Element button = (Element) element.getFirstChild();
				button.setAttribute("disabled", "true");
				this.setElement(element);
			} else {
				Element element = formManagerInstance.getLocalizedFormHtmlComponent(this.getId(), current);
				element.setAttribute("class", this.getStyleClass());
				this.setElement(element);
			}
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

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

}
