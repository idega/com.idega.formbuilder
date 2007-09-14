package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextInput;
import com.idega.util.RenderUtils;

public class FBTaskPropertiesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "TaskPropertiesPanel";
	
	public FBTaskPropertiesPanel() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("fbTaskProperties");
		
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Text panelHeader = new Text("Task properties");
		panelHeader.setStyleClass("fbPanelHeader");
		formItem.add(panelHeader);
		body.add(formItem);
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		TextInput input = new TextInput();
		Label label = new Label("Select task form", input);
		formItem.add(label);
		formItem.add(input);
		body.add(formItem);
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		input = new TextInput();
		label = new Label("Select task user group", input);
		formItem.add(label);
		formItem.add(input);
		body.add(formItem);
		
		add(body);
	}

	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}

}
