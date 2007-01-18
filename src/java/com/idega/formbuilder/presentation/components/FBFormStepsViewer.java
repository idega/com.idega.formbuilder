package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxCommandButton;

import com.idega.presentation.IWBaseComponent;

public class FBFormStepsViewer extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "FormStepsViewer";
	
//	private static final String NEW_STEP_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/window-new.png";
//	private static final String REMOVE_STEP_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/del_16.gif";
	private static final String STEP_ICON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/format-justify-fill.png";
	
	private static final String NEW_STEP_BUTTON_FACET = "newStepButton";
	private static final String REMOVE_STEP_BUTTON_FACET = "removeStepButton";
	
	private String id;
	private String styleClass;
	private String itemStyleClass;
	private int steps;
	
	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public FBFormStepsViewer() {
		super();
		this.setRendererType(null);
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	protected void initializeComponent(FacesContext context) {
		System.out.println("INITIALIZING STEPS LIST");
		Application application = context.getApplication();
		this.getChildren().clear();
		
		/*HtmlGraphicImage newStepButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		newStepButton.setValue(NEW_STEP_IMG);
		newStepButton.setOnclick("alert('PRESSING...')");
		
		UIAjaxSupport newStepButtonS = (UIAjaxSupport) application.createComponent(UIAjaxSupport.COMPONENT_TYPE);
		newStepButtonS.setEvent("onclick");
		newStepButtonS.setReRender("mainApplication");
		newStepButtonS.setActionListener(application.createMethodBinding("#{controlMenuAction.processAction}", new Class[]{ActionEvent.class}));
		newStepButtonS.setAjaxSingle(true);
		newStepButton.getChildren().add(newStepButtonS);
		
		this.getFacets().put(NEW_STEP_BUTTON_FACET, newStepButton);*/
		
		UIAjaxCommandButton addNewPhase = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		addNewPhase.setId("addNewPhase");
		addNewPhase.setActionListener(application.createMethodBinding("#{controlMenuAction.processAction}", new Class[]{ActionEvent.class}));
		addNewPhase.setAjaxSingle(true);
		addNewPhase.setOncomplete("alert('WOW')");
		addNewPhase.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['add_step']}"));
		addNewPhase.setReRender("mainApplication");
		
		this.getFacets().put(NEW_STEP_BUTTON_FACET, addNewPhase);
		
		UIAjaxCommandButton removePhase = (UIAjaxCommandButton) application.createComponent(UIAjaxCommandButton.COMPONENT_TYPE);
		removePhase.setId("removePhase");
		removePhase.setActionListener(application.createMethodBinding("#{controlMenuAction.processAction}", new Class[]{ActionEvent.class}));
		removePhase.setAjaxSingle(true);
		removePhase.setOncomplete("alert('WOW')");
		removePhase.setValueBinding("value", application.createValueBinding("#{localizedStrings['com.idega.formbuilder']['remove_step']}"));
		removePhase.setReRender("mainApplication");
		
		this.getFacets().put(REMOVE_STEP_BUTTON_FACET, removePhase);
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = steps;
		values[4] = itemStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		steps = (Integer) values[3];
		itemStyleClass = (String) values[4];
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", null);
		writer.writeAttribute("class", styleClass, null);
		
		UIComponent add = (UIComponent) getFacet(NEW_STEP_BUTTON_FACET);
		if(add != null) {
			renderChild(context, add);
		}
		UIComponent remove = (UIComponent) getFacet(REMOVE_STEP_BUTTON_FACET);
		if(remove != null) {
			renderChild(context, remove);
		}
		
		writer.startElement("DIV", null);
//		writer.writeAttribute("style", "width: 200px; height: auto; padding: 5px; border: 1px;", null);
//		System.out.println("NUMBER OF STEPS: " + getSteps());
		for(int i = 0; i < steps; i++) {
			writer.startElement("IMG", null);
			writer.writeAttribute("src", STEP_ICON_IMG, null);
			writer.writeAttribute("id", "phase" + i, null);
			writer.writeAttribute("class", itemStyleClass, null);
			writer.endElement("IMG");
		}
		writer.endElement("DIV");
		writer.endElement("DIV");
	}
	
	/*public void encodeChildren(FacesContext context) throws IOException {
		if (!this.isRendered()) {
			return;
		}
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		writer.endElement("DIV");
	}*/

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

	public String getItemStyleClass() {
		return itemStyleClass;
	}

	public void setItemStyleClass(String itemStyleClass) {
		this.itemStyleClass = itemStyleClass;
	}

}
