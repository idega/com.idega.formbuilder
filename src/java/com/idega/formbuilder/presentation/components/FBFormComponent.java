package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.ProcessPalette;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.webface.WFUtil;

public class FBFormComponent extends FBComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormComponent.class);
	
	public static final String COMPONENT_TYPE = "FormComponent";
	
	private static final String DELETE_BUTTON_FACET = "DELETE_BUTTON_FACET";
	private static final String HANDLE_LAYER_FACET = "HANDLE_LAYER_FACET";
	private static final String VARIABLE_NAME_FACET = "VARIABLE_NAME_FACET";
	private static final String DELETE_BUTTON_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/del_16.png";
	private static final String DEFAULT_LOAD_ACTION = "loadComponentInfo(this);";
	private static final String DEFAULT_DELETE_ACTION = "removeComponent(this);";
	private static final String DEFAULT_CLASS = "formElement formElementHover";
	private static final String DEFAULT_SPEED_CLASS = "speedButton";
	private static final String HANDLER_LAYER_CLASS = "fbCompHandler";
	private static final String DELETE_BUTTON_PREFIX = "db";
	private static final String ID_ATTRIBUTE = "id";
	private static final String ONCLICK_ATTRIBUTE = "onclick";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String STYLECLASS_ATTRIBUTE = "styleClass";
	private static final String EDIT_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit_16.png";
	private static final String ASSIGN_VAR_BOX_CLASS = "assignVariableBox";
	private static final String LETTER_A = "A";
	private static final String ASSIGN_LABEL_CLASS = "assignLabel";
	private static final String REL_ATTRIBUTE = "rel";
	
	private Element element;
	private String onLoad;
	private String onDelete;
	private String speedButtonStyleClass;
	private String value;
	private String type;
	private Component component;

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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
	}
	
	public FBFormComponent(Component component) {
		this.component = component;
		setId(component.getId());
		setStyleClass(DEFAULT_CLASS);
		this.speedButtonStyleClass = DEFAULT_SPEED_CLASS;
		this.onDelete = DEFAULT_DELETE_ACTION;
		this.onLoad = DEFAULT_LOAD_ACTION;
	}
	
	public FBFormComponent(String componentId) {
		super();
		setId(componentId);
		setStyleClass(DEFAULT_CLASS);
		this.speedButtonStyleClass = DEFAULT_SPEED_CLASS;
		this.onDelete = DEFAULT_DELETE_ACTION;
		this.onLoad = DEFAULT_LOAD_ACTION;
	}
	
	protected void initializeComponent(FacesContext context) {
		if(component == null) {
			return;
		}
		IWContext iwc = IWContext.getIWContext(context);
		
		Locale current = FBUtil.getUILocale();
		try {
			Element element = (Element) component.getHtmlRepresentation(current).cloneNode(true);
			if(element != null) {
				element.removeAttribute(ID_ATTRIBUTE);
				setElement(element);
			}
		} catch(Exception e) {
			logger.error("Could not get HTML representation of component: " + getId(), e);
		}
						
		Layer handleLayer = new Layer(Layer.DIV);
		handleLayer.setStyleClass(HANDLER_LAYER_CLASS);
						
		PropertiesComponent properties = component.getProperties();
		type = component.getType();
		if(type.startsWith("xf:")) {
			ProcessPalette processPalette = (ProcessPalette) WFUtil.getBeanInstance(ProcessPalette.BEAN_ID);
			type = processPalette.getComponentInternalTypeMappings().get(type);
		}
		if(properties.getVariable() != null) {
			value = properties.getVariable().getName();
		}
						
		Layer assignVariable = new Layer(Layer.DIV);
		assignVariable.setStyleClass(ASSIGN_VAR_BOX_CLASS);
		assignVariable.setMarkupAttribute(REL_ATTRIBUTE, type);
		assignVariable.setId(LETTER_A + getId());
						
		Link assignLabel = new Link();
		assignLabel.setStyleClass(ASSIGN_LABEL_CLASS);
		if(value == null) {
			assignLabel.setText(getLocalizedString(iwc, "fb_no_assign_label", "Not assigned"));
		} else {
			assignLabel.setText(getLocalizedString(iwc, "fb_assigned_to_label", "Assigned to: ") + value);
		}
							
		Image icon = new Image();
		icon.setSrc(EDIT_ICON);
							
		assignVariable.add(icon);
		assignVariable.add(assignLabel);
						
		Image deleteButton = new Image();
		deleteButton.setId(DELETE_BUTTON_PREFIX + getId());
		deleteButton.setSrc(DELETE_BUTTON_ICON);
//		deleteButton.setOnClick(onDelete);
		deleteButton.setStyleClass(speedButtonStyleClass);
						
		addFacet(VARIABLE_NAME_FACET, assignVariable);
		addFacet(DELETE_BUTTON_FACET, deleteButton);
		addFacet(HANDLE_LAYER_FACET, handleLayer);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement(Layer.DIV, this);
		writer.writeAttribute(CLASS_ATTRIBUTE, getStyleClass(), STYLECLASS_ATTRIBUTE);
		writer.writeAttribute(ID_ATTRIBUTE, getId(), ID_ATTRIBUTE);
//		writer.writeAttribute(ONCLICK_ATTRIBUTE, onLoad, ONCLICK_ATTRIBUTE);
		UIComponent handleLayer = getFacet(HANDLE_LAYER_FACET);
		if(handleLayer != null) {
			renderChild(context, handleLayer);
		}
		DOMTransformer.renderNode(element, this, writer);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement(Layer.DIV);
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		
		if(workspace.isProcessMode()) {
			UIComponent facet = getFacet(VARIABLE_NAME_FACET);
			if(facet != null) {
				renderChild(context, facet);
			}
		}
		UIComponent facet = getFacet(DELETE_BUTTON_FACET);
		if(facet != null) {
			renderChild(context, facet);
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
