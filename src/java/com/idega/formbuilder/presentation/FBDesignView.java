package com.idega.formbuilder.presentation;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.div.Div;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.manager.IFormManager;

public class FBDesignView extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_designview";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "DesignView";
	
	public static final String FACET_VISIBLE_STYLECLASS = "visibleFacet";
	public static final String FACET_INVISIBLE_STYLECLASS = "invisibleFacet";
	
	public static final String DESIGN_VIEW_STATUS_NOFORM = "noform";
	public static final String DESIGN_VIEW_STATUS_EMPTY = "empty";
	public static final String DESIGN_VIEW_STATUS_ACTIVE = "active";
	
	public static final String DESIGN_VIEW_NOFORM_FACET = "noFormNoticeFacet";
	public static final String DESIGN_VIEW_EMPTY_FACET = "emptyFormFacet";
	public static final String DESIGN_VIEW_HEADER_FACET = "formHeaderFacet";
	
	private String styleClass;
	private String componentStyleClass;
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

	public FBDesignView() {
		super();
		this.setRendererType(FBDesignView.RENDERER_TYPE);
	}
	
	public String getFamily() {
		return FBDesignView.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return FBDesignView.RENDERER_TYPE;
	}
	
	private void printChildrenIDs(UIComponent comp) {
		Iterator it = comp.getChildren().iterator();
		while(it.hasNext()) {
			UIComponent c = (UIComponent) it.next();
			String nextId = c.getId();
			System.out.println("PARENT COMPONENT:" + c + " HAS ID: " + nextId);
			System.out.println("CHILDREN IDS: ");
			printChildrenIDs(c);
		}
	}
	
	public void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		this.getChildren().clear();
		System.out.println("----------SIZE: " + this.getChildren().size());
		List<String> ids = formManagerInstance.getFormComponentsIdsList();
		Iterator it = ids.iterator();
		//Div innerDiv = (Div) application.createComponent(Div.COMPONENT_TYPE);
		//innerDiv.setId(this.getId() + "inner");
		
		while(it.hasNext()) {
			
			String nextId = (String) it.next();
			
			FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
			formComponent.setId(nextId);
			//System.out.println("NEXT ID: " + formComponent.getClientId(context));
			formComponent.setStyleClass(this.getComponentStyleClass());
		    this.getChildren().add(formComponent);
			//innerDiv.getChildren().add(formComponent);
		}
		//this.getChildren().add(innerDiv);
		Iterator it3 = this.getChildren().iterator();
		while(it3.hasNext()) {
			System.out.println("---------" + ((UIComponent) it3.next()).getId());
		}
		ValueBinding vb = this.getValueBinding("status");
		String status = null;
		if(vb != null) {
			status = (String) vb.getValue(context);
		} else {
			status = this.getStatus();
		}
		if(status != null) {
			if(FBDesignView.DESIGN_VIEW_STATUS_EMPTY.equals(status) || FBDesignView.DESIGN_VIEW_STATUS_ACTIVE.equals(status)) {
				System.out.println("This designview has " + this.getFacets().size() + " facets");
				Iterator it2 = this.getFacets().keySet().iterator();
				while(it2.hasNext()) {
					System.out.println("FACET: " + (String) it2.next());
				}
				FBFormComponent submitButton = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
				submitButton.setStyleClass(this.getComponentStyleClass());
				submitButton.setSubmit(true);
				this.getFacets().put("submit", submitButton);
				//this.getChildren().add(submitButton);
			}
		}
		
		
		System.out.println("ROOT ID: " + context.getViewRoot().getId() + " INSIDE FBDESIGNVIEW " + this.getRendersChildren());
		printChildrenIDs(context.getViewRoot(), context);
	}
	
	private void printChildrenIDs(UIComponent comp, FacesContext context) {
		Iterator it = comp.getChildren().iterator();
		while(it.hasNext()) {
			UIComponent c = (UIComponent) it.next();
			String nextId = c.getClientId(context);
			System.out.println("COMPONENT WITH ID: " + nextId + " (" + c.getParent().getId() + ")");
			//System.out.println("CHILDREN IDS: ");
			printChildrenIDs(c, context);
		}
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = componentStyleClass;
		values[3] = status;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		componentStyleClass = (String) values[2];
		status = (String) values[3];
	}
}
