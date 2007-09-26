package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.jbpm.graph.def.ProcessDefinition;

import com.idega.business.IBOLookup;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SelectionBox;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.util.CoreUtil;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBProcessEditor extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "ProcessEditor";
	
	public FBProcessEditor() {
		super();
		setRendererType(null);
	}
	
	private String processId;
	
	protected void initializeComponent(FacesContext context) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("fbTabViewContent");
		
		Layer leftDiv = new Layer(Layer.DIV);
		leftDiv.setStyleClass("taskNodeSelection");
		
		JbpmProcessBusinessBean logic = (JbpmProcessBusinessBean) WFUtil.getBeanInstance("jbpmProcessBusiness");
		List<ProcessDefinition> processes = logic.getProcessList();
		
		Layer formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		DropdownMenu processSelector = new DropdownMenu();
		for(Iterator it = processes.iterator(); it.hasNext(); ) {
			ProcessDefinition pd = (ProcessDefinition) it.next();
			processSelector.addMenuElement(new Long(pd.getId()).toString(), pd.getName());
		}
		processSelector.addMenuElementFirst("", "Select process");
		processSelector.setId("processSelector");
		processSelector.setOnChange("loadProcessTaskList(this.value);");
		Label label = new Label("Select process", processSelector);
		formItem.add(label);
		formItem.add(processSelector);
		leftDiv.add(formItem);
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		SelectionBox select = new SelectionBox();
		select.setId("taskSelector");
//		String processId = "1";
//		if(processId != null) {
//			List<String[]> tasks = logic.getProcessDefinitionTasks(processId);
//			for(Iterator it = tasks.iterator(); it.hasNext(); ) {
//				String[] obj = (String[]) it.next();
//				select.addMenuElement(obj[0], obj[1]);
//			}
//		}
		select.setMaximumChecked(1, "Select only one");
		select.setOnChange("loadTaskProperties(this.value);");
		select.setHeight("20");
		select.setWidth("300");
		label = new Label("Select task", select);
		formItem.add(label);
		formItem.add(select);
		leftDiv.add(formItem);
		
		Layer rightDiv = new Layer(Layer.DIV);
		rightDiv.setStyleClass("taskNodeProperties");
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		Text panelHeader = new Text("Task properties");
		panelHeader.setStyleClass("fbPanelHeader");
		formItem.add(panelHeader);
		rightDiv.add(formItem);
		
		PersistenceManager persistence_manager = (PersistenceManager) WFUtil.getBeanInstance("formbuilderPersistenceManager");
		List<SelectItem> forms = persistence_manager.getForms();
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		DropdownMenu input = new DropdownMenu();
//		TextInput input = new TextInput();
		input.setId("taskForm");
		input.setDisabled(true);
		for(Iterator it = forms.iterator(); it.hasNext(); ) {
			SelectItem item = (SelectItem) it.next();
			input.setMenuElement((String) item.getValue(), (String) item.getLabel());
		}
		label = new Label("Select task form", input);
		formItem.add(label);
		formItem.add(input);
		rightDiv.add(formItem);
		
		Collection groups = null;
		try {
			GroupBusiness groupBusiness = (GroupBusiness) IBOLookup.getServiceInstance(CoreUtil.getIWContext(), GroupBusiness.class);
			groups = groupBusiness.getAllGroups();
		} catch (Exception e) {
			
		}
		
		formItem = new Layer(Layer.DIV);
		formItem.setStyleClass("formItem");
		input = new DropdownMenu();
		input.setId("taskGroup");
		input.setDisabled(true);
		for(Iterator it = groups.iterator(); it.hasNext(); ) {
			Group item = (Group) it.next();
			input.setMenuElement((String) item.getId(), (String) item.getName());
		}
		label = new Label("Select task user group", input);
		formItem.add(label);
		formItem.add(input);
		rightDiv.add(formItem);
		
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
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
//		values[1] = super
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}
	
}
