package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.ProcessPalette;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Label;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBAssignVariableComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "AssignVariableComponent";
	
	private static final String EDIT_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit_16.png";
	
	private String status;
	private String value;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FBAssignVariableComponent() {
		this("idle", null, null);
	}
	
	public FBAssignVariableComponent(String status) {
		this(status, null, null);
	}
	
	public FBAssignVariableComponent(String status, String type, String value) {
		super();
		setRendererType(null);
		this.status = status;
		this.value = value;
		this.type = type;
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer body = new Layer(Layer.DIV);
		
		if(status.equals("idle")) {
			body.setId("assignV-" + getId());
			Link assignLabel = new Link();
			if(value == null) {
				assignLabel.setText(getLocalizedString(iwc, "fb_no_assign_label", "Not assigned"));
			} else {
				assignLabel.setText("Assigned to: " + value);
			}
			assignLabel.setOnClick("reloadAssignVariable(event);return false;");
			
			Image icon = new Image();
			icon.setSrc(EDIT_ICON);
			
			body.add(icon);
			body.add(assignLabel);
		} else if(status.equals("assign")) {
			body.setId("assignV-" + getId());
			
			DropdownMenu taskChooser = new DropdownMenu();
			taskChooser.setId("assignV-" + getId() + "-chooser");
			taskChooser.addMenuElementFirst("", "---------");
			
			if(type != null) {
				
				ProcessPalette processPalette = (ProcessPalette) WFUtil.getBeanInstance(ProcessPalette.BEAN_ID);
				String datatype = processPalette.getComponentDatatype(type);
				JbpmProcessBusinessBean jbpmProcessBean = (JbpmProcessBusinessBean) WFUtil.getBeanInstance(JbpmProcessBusinessBean.BEAN_ID);
				FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
				List<String> variables = jbpmProcessBean.getTaskVariablesByDatatype(new Long(formDocument.getProcessId()).toString(), formDocument.getTaskName(), datatype);
				
				for(Iterator<String> it = variables.iterator(); it.hasNext(); ) {
					String variable = it.next();
					taskChooser.addMenuElement(datatype + ":" + variable, variable);
				}
				
			}
			
//			taskChooser.setOnChange("reloadAddTaskForm2(event);return false;");
//			taskChooser.setOnBlur("resetAddTaskForm(event);return false;");
			body.add(new Label("Assign to:", taskChooser));
			body.add(taskChooser);
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
