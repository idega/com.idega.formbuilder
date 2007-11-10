package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.TextInput;
import com.idega.util.RenderUtils;

public class FBAddVariableComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "AddVariableComponent";
	
	private static final String FULL_PANEL_CLASS = "addVariableBox";
	private static final String ADD_VARIABLE_BUTTON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/add-tiny.png";
	private static final String SHOW_DIALOG_ACTION = "showNewVariableDialog(this.id);";
	private static final String HIDE_DIALOG_ACTION = "hideNewVariableDialog(this.id);";
	private static final String ADD_VARIABLE_ACTION = "addNewVariable(event);";
	private static final String ADD_PREFIX = "add_";
	private static final String BOX_POSTFIX = "_box";
	
	private boolean idle;
	private String datatype;
	
	public FBAddVariableComponent() {
		super(null, FULL_PANEL_CLASS);
		setRendererType(null);
		this.idle = true;
	}
	
	public FBAddVariableComponent(boolean idle, String datatype) {
		super(null, FULL_PANEL_CLASS);
		setRendererType(null);
		this.idle = idle;
		this.datatype = datatype;
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(getStyleClass());
		body.setId(datatype + BOX_POSTFIX);
		
		if(idle) {
			Image addVariableButton = new Image();
			addVariableButton.setSrc(ADD_VARIABLE_BUTTON);
			addVariableButton.setId(ADD_PREFIX + datatype);
			addVariableButton.setOnClick(SHOW_DIALOG_ACTION);
			body.add(addVariableButton);
		} else {
			TextInput name = new TextInput();
			name.setId(ADD_PREFIX + datatype);
			name.setOnKeyDown(ADD_VARIABLE_ACTION);
			name.setOnBlur(HIDE_DIALOG_ACTION);
			body.add(name);
		}
		
		add(body);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}
