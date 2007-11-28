package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.ProcessData;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.webface.WFUtil;


public class FBVariableViewer extends FBComponentBase {

	public static final String COMPONENT_TYPE = "VariableViewer";
	
	private static final String ADD_VAR_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/add-tiny.png";
	
	protected void initializeComponent(FacesContext context) {	
		Layer body = new Layer(Layer.DIV);
		
		ProcessData processData = (ProcessData) WFUtil.getBeanInstance(ProcessData.BEAN_ID);
		JbpmProcessBusinessBean jbpmBusiness = (JbpmProcessBusinessBean) WFUtil.getBeanInstance(JbpmProcessBusinessBean.BEAN_ID);
		Map<String, Set<String>> variables = jbpmBusiness.getProcessVariables(processData.getProcessId());
		
		Map<String, List<String>> vars = processData.getDatatypedVariables();
		for(Iterator<String> it = vars.keySet().iterator(); it.hasNext(); ) {
			String datatype = it.next();
			
			Layer header = new Layer(Layer.DIV);
			header.setStyleClass("fbMenuTabBar");
			Text headerText = new Text(datatype);
			header.add(headerText);
			
			Layer layer = new Layer(Layer.DIV);
			layer.setStyleClass("variableListSection");
			
			Set<String> list = variables.get(datatype);
			
			for(Iterator<String> it2 = list.iterator(); it2.hasNext(); ) {
				String var = it2.next();
				
				Text varEntry = new Text(var);
				varEntry.setStyleClass("varEntry");
				varEntry.setId(var + "_var");
				
				String status = processData.getVariableStatus(datatype + ":" + var).getStatus();
				varEntry.setStyleClass(status);
				
				layer.add(varEntry);
			}
			
			Image addVarIcon = new Image();
			addVarIcon.setSrc(ADD_VAR_IMG);
			addVarIcon.setId(datatype + "_add");
			addVarIcon.setStyleClass("addVariableIcon");
			
			layer.add(addVarIcon);
			
			body.add(header);
			body.add(layer);
			
		}
		
		Layer header = new Layer(Layer.DIV);
		header.setStyleClass("fbMenuTabBar");
		Text headerText = new Text("Transitions");
		header.add(headerText);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("variableListSection");
		
		List<String> transitions = jbpmBusiness.getTaskTransitions(processData.getProcessId(), processData.getTaskName());
		for(Iterator<String> it2 = transitions.iterator(); it2.hasNext(); ) {
			String transition = it2.next();
			
			Text varEntry = new Text(transition);
			varEntry.setStyleClass("varEntry");
			varEntry.setId(transition + "_trans");
			
			String status = processData.getTransitionStatus(transition).getStatus();
			varEntry.setStyleClass(status);
			
			layer.add(varEntry);
		}
		
		body.add(header);
		body.add(layer);
		
		Layer legend = new Layer(Layer.DIV);
		legend.setStyleClass("fbVariablesLegend");
		
		Lists topList = new Lists();
		ListItem item2 = new ListItem();
		item2.setStyleClass("legenItem");
		Text legendItem = new Text("Unused");
		legendItem.setStyleClass("unusedLegend");
		item2.add(legendItem);
		topList.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass("legenItem");
		legendItem = new Text("Single");
		legendItem.setStyleClass("singleLegend");
		item2.add(legendItem);
		topList.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass("legenItem");
		legendItem = new Text("Multiple");
		legendItem.setStyleClass("multipleLegend");
		item2.add(legendItem);
		topList.add(item2);
		
		legend.add(topList);
		
		body.add(legend);
		
		add(body);
	}
	
}
