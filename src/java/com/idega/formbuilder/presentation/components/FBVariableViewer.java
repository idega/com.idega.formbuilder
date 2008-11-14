package com.idega.formbuilder.presentation.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

import com.idega.block.process.variables.Variable;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.ProcessData;
import com.idega.jbpm.exe.ProcessDefinitionW;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;


public class FBVariableViewer extends FBComponentBase {

	public static final String COMPONENT_TYPE = "VariableViewer";
	
	private static final String ADD_VAR_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/add-tiny.png";
	
	private static final String FB_MENU_TABBAR = "fbMenuTabBar";
	private static final String VARIABLE_LIST_SECTION = "variableListSection";
	private static final String VAR_ENTRY = "varEntry";
//	private static final String VAR_POSTFIX = "var";
	private static final String ADD_ICON_POSTFIX = "add";
	private static final String ADD_VARIABLE_ICON = "addVariableIcon";
	private static final String TRANS_POSTFIX = "trans";
	private static final String FB_VARIABLES_LEGEND = "fbVariablesLegend";
	private static final String LEGEND_ITEM = "legenItem";
	private static final String UNUSED_LEGEND_CLASS = "unusedLegend";
	private static final String SINGLE_LEGEND_CLASS = "singleLegend";
	private static final String MULTIPLE_LEGEND_CLASS = "multipleLegend";
	
	protected void initializeComponent(FacesContext context) {	
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = new Layer(Layer.DIV);
		body.setId("variableViewer");
		
		ProcessData processData = (ProcessData) WFUtil.getBeanInstance(ProcessData.BEAN_ID);
		
//		Map<String, Set<String>> variables = jbpmBusiness.getProcessVariables(processData.getProcessId());
		
		Map<String, Set<String>> variables = new HashMap<String, Set<String>>();
		
		Map<String, List<String>> vars = processData.getDatatypedVariables();
		for(String datatype : vars.keySet()) {
			Layer header = new Layer(Layer.DIV);
			header.setStyleClass(FB_MENU_TABBAR);
			Text headerText = new Text(datatype);
			header.add(headerText);
			
			Layer layer = new Layer(Layer.DIV);
			layer.setStyleClass(VARIABLE_LIST_SECTION);
			
			Set<String> list = variables.get(datatype);
			
			if(list != null) {
				for(String var : list) {
					StringBuffer buffer = new StringBuffer(datatype)
						.append(CoreConstants.UNDER)
						.append(var);
					
					Text varEntry = new Text(var);
					varEntry.setStyleClass(VAR_ENTRY);
					varEntry.setStyleClass("fbvar");
					varEntry.setId(buffer.toString());
					
					String status = processData.getVariableStatus(buffer.toString()).getStatus();
					varEntry.setStyleClass(status);
					
					layer.add(varEntry);
				}
			}
			
			Image addVarIcon = new Image();
			addVarIcon.setSrc(ADD_VAR_IMG);
			addVarIcon.setId(datatype + CoreConstants.UNDER + ADD_ICON_POSTFIX);
			addVarIcon.setStyleClass(ADD_VARIABLE_ICON);
			
//			layer.add(addVarIcon);
			
			body.add(header);
			body.add(layer);
			
		}
		
		Layer header = new Layer(Layer.DIV);
		header.setStyleClass(FB_MENU_TABBAR);
		Text headerText = new Text(getLocalizedString(iwc, "fb_var_transitions_label", "Transitions"));
		header.add(headerText);
		
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass(VARIABLE_LIST_SECTION);
		
		if(processData.getProcessId() != null) {
			
			try {
				
//				List<String> transitions = jbpmBusiness.getTaskTransitions(processData.getProcessId(), processData.getTaskName());
//				for(String transition : transitions) {
//					StringBuffer buffer = new StringBuffer(transition)
//					.append(CoreConstants.UNDER)
//					.append(TRANS_POSTFIX);
//					
//					Text varEntry = new Text(transition);
//					varEntry.setStyleClass(VAR_ENTRY);
//					varEntry.setStyleClass("fbtrans");
//					varEntry.setId(buffer.toString());
//					
//					String status = processData.getTransitionStatus(transition).getStatus();
//					varEntry.setStyleClass(status);
//					
//					layer.add(varEntry);
//				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		body.add(header);
		body.add(layer);
		
		Layer legend = new Layer(Layer.DIV);
		legend.setStyleClass(FB_VARIABLES_LEGEND);
		legend.setStyleClass(FB_MENU_TABBAR);
		headerText = new Text(getLocalizedString(iwc, "fb_var_legend_label", "Legend"));
		legend.add(headerText);
		
		Lists topList = new Lists();
		ListItem item2 = new ListItem();
		item2.setStyleClass(LEGEND_ITEM);
		Text legendItem = new Text(getLocalizedString(iwc, "fb_var_legend_unused", "Unused"));
		legendItem.setStyleClass(UNUSED_LEGEND_CLASS);
		item2.add(legendItem);
		topList.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass(LEGEND_ITEM);
		legendItem = new Text(getLocalizedString(iwc, "fb_var_legend_single", "Single"));
		legendItem.setStyleClass(SINGLE_LEGEND_CLASS);
		item2.add(legendItem);
		topList.add(item2);
		
		item2 = new ListItem();
		item2.setStyleClass(LEGEND_ITEM);
		legendItem = new Text(getLocalizedString(iwc, "fb_var_legend_multiple", "Multiple"));
		legendItem.setStyleClass(MULTIPLE_LEGEND_CLASS);
		item2.add(legendItem);
		topList.add(item2);
		
		legend.add(topList);
		
		body.add(legend);
		
		add(body);
	}
	
}
