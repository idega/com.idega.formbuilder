package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.business.SpringBeanLookup;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.ProcessLogic;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SelectionBox;
import com.idega.util.CoreUtil;
import com.idega.util.RenderUtils;

public class FBProcessEditor extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ProcessEditor";
	
	public FBProcessEditor() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("fbTabViewContent");
		
		Layer leftDiv = new Layer(Layer.DIV);
		leftDiv.setStyleClass("taskNodeSelection");
		
		ProcessLogic logic = (ProcessLogic) SpringBeanLookup.getInstance().getSpringBean(CoreUtil.getIWContext(), ProcessLogic.class);
		logic.getDeployedProcesses();
		
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		DropdownMenu processSelector = new DropdownMenu();
		processSelector.setId("processSelector");
		Label label = new Label("Select process", processSelector);
		formItem.add(label);
		formItem.add(processSelector);
		leftDiv.add(formItem);
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		SelectionBox select = new SelectionBox();
		select.setMaximumChecked(1, "Select only one");
		select.setHeight("20");
		select.setWidth("300");
		label = new Label("Select task", select);
		formItem.add(label);
		formItem.add(select);
		leftDiv.add(formItem);
		
		Layer rightDiv = new Layer(Layer.DIV);
		rightDiv.setStyleClass("taskNodeProperties");
		
		FBTaskPropertiesPanel taskProperties = new FBTaskPropertiesPanel();
		rightDiv.add(taskProperties);
		
		body.add(leftDiv);
		body.add(rightDiv);
		
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
