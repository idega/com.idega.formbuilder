package com.idega.formbuilder.presentation;

import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

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
	
	public void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		IFormManager formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		this.getChildren().clear();
		List<String> ids = formManagerInstance.getFormComponentsIdsList();
		Iterator it = ids.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			FBFormComponent formComponent = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
			formComponent.setId(nextId);
			formComponent.setStyleClass(this.getComponentStyleClass());
		    this.getChildren().add(formComponent);
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
				FBFormComponent submitButton = (FBFormComponent) application.createComponent(FBFormComponent.COMPONENT_TYPE);
				submitButton.setStyleClass(this.getComponentStyleClass());
				submitButton.setSubmit(true);
				this.getFacets().put("submit", submitButton);
				//this.getChildren().add(submitButton);
			}
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
