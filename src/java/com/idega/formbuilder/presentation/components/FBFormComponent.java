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
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.formbuilder.dom.DOMTransformer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.webface.WFUtil;

public class FBFormComponent extends FBComponentBase {
	
	private static Log logger = LogFactory.getLog(FBFormComponent.class);
	
	public static final String COMPONENT_TYPE = "FormComponent";
	
	private static final String DELETE_BUTTON_FACET = "DELETE_BUTTON_FACET";
	private static final String HANDLE_LAYER_FACET = "HANDLE_LAYER_FACET";
	private static final String VARIABLE_NAME_FACET = "VARIABLE_NAME_FACET";
	private static final String DELETE_BUTTON_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png";
	private static final String DEFAULT_LOAD_ACTION = "loadComponentInfo(this);";
	private static final String DEFAULT_DELETE_ACTION = "removeComponent(this);";
	
	private Element element;
	private String onLoad;
	private String onDelete;
	private String speedButtonStyleClass;

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
		setRendererType(null);
	}
	
	public FBFormComponent(String componentId) {
		super();
		setRendererType(null);
		setId(componentId);
		setStyleClass("formElement");
		this.speedButtonStyleClass = "speedButton";
		this.onDelete = DEFAULT_DELETE_ACTION;
		this.onLoad = DEFAULT_LOAD_ACTION;
	}
	
	protected void initializeComponent(FacesContext context) {
		Page page = ((FormPage) WFUtil.getBeanInstance(FormPage.BEAN_ID)).getPage();
		if(page != null) {
			Component component = page.getComponent(getId());
			if(component != null) {
				try {
					Locale current = FBUtil.getUILocale();
					Element element = (Element) component.getHtmlRepresentation(current).cloneNode(true);
					if(element != null) {
						
						element.removeAttribute("id");
						setElement(element);
						
						Layer handleLayer = new Layer(Layer.DIV);
						handleLayer.setStyleClass("fbCompHandler");
						
						FBAssignVariableComponent assignVariable = new FBAssignVariableComponent();
						PropertiesComponent properties = component.getProperties();
						String type = component.getType();
						type = type.substring(3);
						assignVariable.setId(component.getId() + "-fbcomp_" + type);
						if(properties.getVariableName() != null) {
							assignVariable.setValue(properties.getVariableName().substring(properties.getVariableName().indexOf(":") + 1));
						}						
						
						Image deleteButton = new Image();
						deleteButton.setId("db" + getId());
						deleteButton.setSrc(DELETE_BUTTON_ICON);
						deleteButton.setOnClick(onDelete);
						deleteButton.setStyleClass(speedButtonStyleClass);
						
						addFacet(VARIABLE_NAME_FACET, assignVariable);
						addFacet(DELETE_BUTTON_FACET, deleteButton);
						addFacet(HANDLE_LAYER_FACET, handleLayer);
					}
				} catch(Exception e) {
					logger.error("Could not get HTML representation of component: " + getId(), e);
				}
			}
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement(Layer.DIV, this);
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("onclick", onLoad, "onclick");
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
		if (!isRendered()) {
			return;
		}
//		UIComponent facet = getFacet(VARIABLE_NAME_FACET);
//		if(facet != null) {
//			renderChild(context, facet);
//		}
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
