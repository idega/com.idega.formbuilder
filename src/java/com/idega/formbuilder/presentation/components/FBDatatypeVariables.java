package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.TaskFormDocument;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBDatatypeVariables extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "DatatypeVariables";
	
	private static final String fb_proc_palette_no_variables = "fb_proc_palette_no_variables";
	private static final String datatype_group_variables_class = "datatypeGroupVariables";
	private static final String datatype_group_variable_class = "datatypeGroupVariable";
	private static final String datatype_variable_container_id_postfix = "_vContainer";
	
	private String datatype;
	
	public FBDatatypeVariables() {
		super();
		setRendererType(null);
	}
	
	public FBDatatypeVariables(String datatype) {
		super();
		setRendererType(null);
		this.datatype = datatype;
	}
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		getChildren().clear();
		
		Layer variableContainer = new Layer(Layer.DIV);
		variableContainer.setStyleClass(datatype_group_variables_class);
		variableContainer.setId(datatype + datatype_variable_container_id_postfix);
		
		JbpmProcessBusinessBean jbpmBusiness = (JbpmProcessBusinessBean) WFUtil.getBeanInstance(JbpmProcessBusinessBean.BEAN_ID);
		TaskFormDocument taskFormDocument = (TaskFormDocument) WFUtil.getBeanInstance(TaskFormDocument.BEAN_ID);
		List<String> variables = jbpmBusiness.getTaskVariablesByDatatype(new Long(taskFormDocument.getProcessId()).toString(), taskFormDocument.getTaskName(), datatype);
		
		if(variables != null && !variables.isEmpty()) {
			Iterator<String> it = variables.iterator();
			while(it.hasNext()) {
				Text variable = new Text(it.next());
				variable.setStyleClass(datatype_group_variable_class);
				variableContainer.add(variable);
			}
		} else {
			Text variable = new Text(getLocalizedString(iwc, fb_proc_palette_no_variables, "No variables of this type"));
			variable.setStyleClass(datatype_group_variable_class);
			variableContainer.add(variable);
		}
		
		FBAddVariableComponent addVariableComponent = new FBAddVariableComponent(true, datatype);
		variableContainer.add(addVariableComponent);
		
		add(variableContainer);
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

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}
