package com.idega.formbuilder.presentation.components;

import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.block.process.variables.Variable;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.ProcessData;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBVariableList extends FBComponentBase {
	
	private String type;
	private boolean transition;
	
	public FBVariableList() {}
	
	public FBVariableList(String type, boolean transition) {
		super();
		this.type = type;
		this.transition = transition;
	}
	
	public boolean isTransition() {
		return transition;
	}

	public void setTransition(boolean transition) {
		this.transition = transition;
	}
	
	protected void initializeComponent(FacesContext context) {	
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = new Layer(Layer.DIV);
		body.setId("selectVariableDialog");
		body.setStyleClass("selectVariableDialogStyle");
		body.setStyleAttribute("visibility", "visible");
		
		Layer header = new Layer(Layer.SPAN);
		header.setStyleClass("accordionHeading");
		body.add(header);
		
		Text title = new Text(getLocalizedString(iwc, "fb_select_variable_label", "Select variable"));
		title.setStyleClass("title");
		header.add(title);
		
		Lists variableList = new Lists();
		variableList.setId("variablePopupList");
		variableList.setStyleClass("variablePopupList");
		
		ProcessData processData = (ProcessData) WFUtil.getBeanInstance(ProcessData.BEAN_ID);
		
		if(transition) {
			List<String> trans = processData.getTransitions();
			
			if(trans == null || trans.isEmpty()) {
				
				ListItem item = new ListItem();
				item.add(new Text(getLocalizedString(iwc, "fb_no_transitions", "No available transitions")));
				
				variableList.add(item);
				
			} else {
				for(String tr : trans) {
					
					ListItem item = new ListItem();
					
					Link itemLink = new Link();
					itemLink.setMarkupAttribute("rel", tr);
					itemLink.setText(tr);
					
					item.add(itemLink);
					
					variableList.add(item);
					
				}
				
			}
			
		} else {
			List<Variable> names = processData.getComponentTypeVariables(type);
			
			if(names == null || names.isEmpty()) {
				
				ListItem item = new ListItem();
				item.add(new Text(getLocalizedString(iwc, "fb_no_variables", "No available variables")));
				
				variableList.add(item);
				
			} else {
				
				for(Variable name : names) {
					
					ListItem item = new ListItem();
					
					Link itemLink = new Link();
					itemLink.setMarkupAttribute("rel", name.getDefaultStringRepresentation());
					itemLink.setText(name.getName());
					
					item.add(itemLink);
					
					variableList.add(item);
				}
				
			}
			
		}
		
		body.add(variableList);
		
		Link noVariable = new Link();
		noVariable.setId("noVariableBtn");
		noVariable.setText(getLocalizedString(iwc, "fb_none_label", "None"));
		
		Link cancelVariable = new Link();
		cancelVariable.setId("cancelVariableBtn");
		cancelVariable.setText(getLocalizedString(iwc, "fb_cancel_label", "Cancel"));
		
		body.add(noVariable);
		body.add(cancelVariable);
		
		add(body);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
