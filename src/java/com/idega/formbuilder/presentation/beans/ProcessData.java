package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.idega.block.process.variables.Variable;
import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesButton;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.util.CoreConstants;
import com.idega.webface.WFUtil;

//TODO: remake this, use standard way of resolving variables
public class ProcessData implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	public static final String BEAN_ID = "processData";
	
	private JbpmProcessBusinessBean jbpmProcessBusiness;
	
	private Map<String, List<String>> variables = new HashMap<String, List<String>>();
	private Map<String, List<String>> transitions = new HashMap<String, List<String>>();
	private Long processId;
	private String processName;
	private String taskName;
	private Long taskId;
	
	public Map<String, List<String>> getDatatypedVariables() {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		if(variables != null) {
			for(Iterator<String> it = variables.keySet().iterator(); it.hasNext(); ) {
				String variableFullname = it.next();
				StringTokenizer stk = new StringTokenizer(variableFullname, CoreConstants.UNDER);
				String type = stk.nextToken();
				String name = null;
				if(stk.hasMoreTokens()) {
					name = stk.nextToken();
				}
				if(result.containsKey(type)) {
					result.get(type).add(name);
				} else {
					List<String> newList = new ArrayList<String>();
					newList.add(name);
					result.put(type, newList);
				}
			}
		}
		return result;
	}
	
	public void initializeBeanInstance(Document document, Long processId, String taskName) {
		this.processId = processId;
		this.taskName = taskName;
		this.variables.clear();
		this.transitions.clear();
		Set<String> vars = jbpmProcessBusiness.getProcessVariables(processId, true);
		List<String> trans = jbpmProcessBusiness.getTaskTransitions(processId, taskName);
		List<String> pages = document.getContainedPagesIdList();
		for(Iterator<String> it = pages.iterator(); it.hasNext(); ) {
			Page page = document.getPage(it.next());
			List<String> components = page.getContainedComponentsIds();
			for(Iterator<String> it2 = components.iterator(); it2.hasNext(); ) {
				Component component = page.getComponent(it2.next());
				PropertiesComponent properties = component.getProperties();
				if(properties != null) {
					Variable variable = properties.getVariable();
					if(variable != null) {
						String variableProperty = variable.getDefaultStringRepresentation();
						if(variables.containsKey(variableProperty)) {
							variables.get(variableProperty).add(component.getId());
						} else {
							List<String> comps = new ArrayList<String>();
							comps.add(component.getId());
							variables.put(variableProperty, comps);
						}
					}
				}
			}
			ButtonArea buttonArea = page.getButtonArea();
			if(buttonArea != null) {
				List<String> buttons = buttonArea.getContainedComponentsIds();
				for(Iterator<String> it4 = buttons.iterator(); it4.hasNext(); ) {
					String buttonId = it4.next();
					Button button = (Button) buttonArea.getComponent(buttonId);
					PropertiesButton properties = button.getProperties();
					if(properties != null) {
						String transition = properties.getReferAction();
						if(transition != null) {
							if(transitions.containsKey(transition)) {
								transitions.get(transition).add(buttonId);
							} else {
								List<String> comps = new ArrayList<String>();
								comps.add(buttonId);
								transitions.put(transition, comps);
							}
						}
					}
				}
			}
		}
		for(Iterator<String> it3 = vars.iterator(); it3.hasNext(); ) {
			String variableName = it3.next();
			if(variables.containsKey(variableName)) {
				continue;
			}
			variables.put(variableName, new ArrayList<String>());
		}
		for(Iterator<String> it5 = trans.iterator(); it5.hasNext(); ) {
			String transitionName = it5.next();
			if(transitions.containsKey(transitionName)) {
				continue;
			}
			transitions.put(transitionName, new ArrayList<String>());
		}
	}
	
	public ConstVariableStatus bindVariable(String componentId, String variable) {
		if(variables.containsKey(variable)) {
			variables.get(variable).add(componentId);
		} else {
			List<String> comps = new ArrayList<String>();
			comps.add(componentId);
			variables.put(variable, comps);
		}
		return getVariableStatus(variable);
	}
	
	public void createVariable(String variable, String datatype) {
		jbpmProcessBusiness.addTaskVariable(processId, taskName, datatype, variable);
	}
	
	public ConstVariableStatus getVariableStatus(String variableName) {
		List<String> comps = variables.get(variableName);
		if(comps != null) {
			if(comps.size() == 0) {
				return new ConstVariableStatus(ConstVariableStatus.UNUSED);
			} else if(comps.size() == 1) {
				return new ConstVariableStatus(ConstVariableStatus.SINGLE);
			} else {
				return new ConstVariableStatus(ConstVariableStatus.MULTIPLE);
			}
		} else {
			return new ConstVariableStatus(ConstVariableStatus.UNUSED);
		}
	}
	
	public ConstVariableStatus unbindVariable(String variable, String componentId) {
		variables.get(variable).remove(componentId);
		//TODO add actual nulling of the component property
		return getVariableStatus(variable);
	}
	
	public ConstVariableStatus bindTransition(String buttonId, String transition) {
		if(transitions.containsKey(transition)) {
			transitions.get(transition).add(buttonId);
		} else {
			List<String> comps = new ArrayList<String>();
			comps.add(buttonId);
			transitions.put(transition, comps);
		}
		return getTransitionStatus(transition);
	}
	
	public ConstVariableStatus unbindTransition(String transition, String buttonId) {
		transitions.get(transition).remove(buttonId);
		//TODO add actual nulling of the component property
		return getTransitionStatus(transition);
	}
	
	public List<String> getAvailableTransitions(String buttonType) {
		if(buttonType == null || !buttonType.equals("fbc_button_submit"))
			return null;
		
		return jbpmProcessBusiness.getTaskTransitions(getProcessId(), getTaskName());
	}
	
	public Set<String> getComponentTypeVariables(String componentType) {
		ProcessPalette processPalette = (ProcessPalette) WFUtil.getBeanInstance(ProcessPalette.BEAN_ID);
		Set<String> datatypes = processPalette.getComponentDatatype(componentType);
		return jbpmProcessBusiness.getProcessVariablesByDatatypes(getProcessId(), datatypes);
	}
	
	public ConstVariableStatus getTransitionStatus(String transition) {
		List<String> comps = transitions.get(transition);
		if(comps != null) {
			if(comps.size() == 0) {
				return new ConstVariableStatus(ConstVariableStatus.UNUSED);
			} else if(comps.size() == 1) {
				return new ConstVariableStatus(ConstVariableStatus.SINGLE);
			} else {
				return new ConstVariableStatus(ConstVariableStatus.MULTIPLE);
			}
		} else {
			return new ConstVariableStatus(ConstVariableStatus.UNUSED);
		}
	}
	
	public Long getProcessId() {
		return processId;
	}
	public void setProcessId(Long processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Map<String, List<String>> getVariables() {
		return variables;
	}
	public void setVariables(Map<String, List<String>> variables) {
		this.variables = variables;
	}
	public Map<String, List<String>> getTransitions() {
		return transitions;
	}
	public void setTransitions(Map<String, List<String>> transitions) {
		this.transitions = transitions;
	}

	public JbpmProcessBusinessBean getJbpmProcessBusiness() {
		return jbpmProcessBusiness;
	}

	public void setJbpmProcessBusiness(JbpmProcessBusinessBean jbpmProcessBusiness) {
		this.jbpmProcessBusiness = jbpmProcessBusiness;
	}

}
