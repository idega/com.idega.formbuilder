package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.process.variables.Variable;
import com.idega.jbpm.exe.BPMFactory;
import com.idega.jbpm.exe.ProcessDefinitionW;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.Document;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;

@Service(ProcessData.BEAN_ID)
@Scope("session")
public class ProcessData implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	
	public static final String BEAN_ID = "processData";
	
	private List<Variable> variables = new ArrayList<Variable>();
	private List<String> transitions = new ArrayList<String>();
	private Map<String, List<Variable>> datatypedVariables = new HashMap<String, List<Variable>>();
	private Map<String, List<String>> variableUsageList = new HashMap<String, List<String>>();
	private Map<String, List<String>> transitionUsageList = new HashMap<String, List<String>>();
	
	private Long processId;
	private String processName;
	private String taskName;
	private Long taskId;
	private Document document;
	
	@Autowired private BPMFactory bpmFactory;

	public BPMFactory getBpmFactory() {
		return bpmFactory;
	}

	public void setBpmFactory(BPMFactory bpmFactory) {
		this.bpmFactory = bpmFactory;
	}

	public Map<String, List<Variable>> getDatatypedVariables() {
		if(datatypedVariables.isEmpty()) {
			for(Variable variable : variables) {
				String varType = variable.getDataType().toString();
				
				if(datatypedVariables.containsKey(varType)) {
					datatypedVariables.get(varType).add(variable);
				} else {
					List<Variable> newList = new ArrayList<Variable>();
					newList.add(variable);
					datatypedVariables.put(varType, newList);
				}
			}
		}
		return datatypedVariables;
	}
	
	public void initializeBeanInstance(Document document, Long processId, String taskName) {
		this.processId = processId;
		this.taskName = taskName;
		this.document = document;
		
		this.variables.clear();
		this.transitions.clear();
		
		this.transitionUsageList.clear();
		
		this.datatypedVariables.clear();
		this.variableUsageList.clear();
		
		ProcessDefinitionW pdw = getBpmFactory().getProcessManager(processId).getProcessDefinition(processId);
		
		variables.addAll(pdw.getTaskVariableList(taskName));
		
		transitions.addAll(pdw.getTaskNodeTransitionsNames(taskName));
	}
	
	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public List<String> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<String> transitions) {
		this.transitions = transitions;
	}
	
	public Map<String, List<String>> getTransitionUsageList() {
		if(transitionUsageList.isEmpty() && document != null) {
			List<String> pages = document.getContainedPagesIdList();
			for(String p : pages) {
				Page page = document.getPage(p);
				
				ButtonArea buttonArea = page.getButtonArea();
				if(buttonArea != null) {
					List<String> buttons = buttonArea.getContainedComponentsIds();
					for(String buttonId : buttons) {
						Button button = (Button) buttonArea.getComponent(buttonId);
						PropertiesButton properties = button.getProperties();
						if(properties != null) {
							String transition = properties.getReferAction();
							if(transition != null) {
								if(transitionUsageList.containsKey(transition)) {
									transitionUsageList.get(transition).add(buttonId);
								} else {
									List<String> comps = new ArrayList<String>();
									comps.add(buttonId);
									transitionUsageList.put(transition, comps);
								}
							}
						}
					}
				}
			}
			for(String transitionName : transitions) {
				if(transitionUsageList.containsKey(transitionName)) {
					continue;
				}
				transitionUsageList.put(transitionName, new ArrayList<String>());
			}
		}
		return transitionUsageList;
	}

	public Map<String, List<String>> getVariableUsageList() {
		if(variableUsageList.isEmpty() && document != null) {
			List<String> pages = document.getContainedPagesIdList();
			for(String p : pages) {
				Page page = document.getPage(p);
				
				List<String> components = page.getContainedComponentsIds();
				for(String c : components) {
					Component component = page.getComponent(c);
					PropertiesComponent properties = component.getProperties();
					if(properties != null) {
						Variable variable = properties.getVariable();
						if(variable != null) {
							String variableProperty = variable.getDefaultStringRepresentation();
							if(variableUsageList.containsKey(variableProperty)) {
								variableUsageList.get(variableProperty).add(component.getId());
							} else {
								List<String> comps = new ArrayList<String>();
								comps.add(component.getId());
								variableUsageList.put(variableProperty, comps);
							}
						}
					}
				}
			}
			
			for(Variable variable : variables) {
				String varName = variable.getDefaultStringRepresentation();
				
				if(variableUsageList.containsKey(varName)) {
					continue;
				}
				variableUsageList.put(varName, new ArrayList<String>());
			}
		}
		return variableUsageList;
	}

	public void setVariableUsageList(Map<String, List<String>> variableUsageList) {
		this.variableUsageList = variableUsageList;
	}

	public void setDatatypedVariables(Map<String, List<Variable>> datatypedVariables) {
		this.datatypedVariables = datatypedVariables;
	}

	public ConstVariableStatus bindVariable(String componentId, String variable) {
		if(getVariableUsageList().containsKey(variable)) {
			getVariableUsageList().get(variable).add(componentId);
		} else {
			List<String> comps = new ArrayList<String>();
			comps.add(componentId);
			getVariableUsageList().put(variable, comps);
		}
		return getVariableStatus(variable);
	}
	
	public void createVariable(String variable, String datatype) {
//		jbpmProcessBusiness.addTaskVariable(processId, taskName, datatype, variable);
	}
	
	public ConstVariableStatus getTransitionStatus(String transition) {
		return getStatus(transition, getTransitionUsageList());
	}
	
	public ConstVariableStatus getVariableStatus(String variable) {
		return getStatus(variable, getVariableUsageList());
	}
	
	private ConstVariableStatus getStatus(String name, Map<String, List<String>> usageMap) {
		List<String> comps = usageMap.get(name);
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
		List<String> usage = getVariableUsageList().get(variable);
		if(usage != null) {
			usage.remove(componentId);
		}
		return getVariableStatus(variable);
	}
	
	public ConstVariableStatus bindTransition(String buttonId, String transition) {
		if(getTransitionUsageList().containsKey(transition)) {
			getTransitionUsageList().get(transition).add(buttonId);
		} else {
			List<String> comps = new ArrayList<String>();
			comps.add(buttonId);
			getTransitionUsageList().put(transition, comps);
		}
		return getTransitionStatus(transition);
	}
	
	public ConstVariableStatus unbindTransition(String transition, String buttonId) {
		List<String> usage = getTransitionUsageList().get(transition);
		if(usage != null) {
			usage.remove(buttonId);
		}
		return getTransitionStatus(transition);
	}
	
	public List<String> getAvailableTransitions(String buttonType) {
		if(buttonType == null || !buttonType.equals("fbc_button_submit"))
			return null;
		
//		return jbpmProcessBusiness.getTaskTransitions(getProcessId(), getTaskName());
		return null;
	}
	
	public List<Variable> getComponentTypeVariables(String componentType) {
		ProcessPalette processPalette = (ProcessPalette) WFUtil.getBeanInstance(ProcessPalette.BEAN_ID);
		Set<String> datatypes = processPalette.getComponentDatatype(componentType);
		
		List<Variable> vars = new ArrayList<Variable>();
		for(String dataType : datatypes) {
			
			List<Variable> dvars = getDatatypedVariables().get(dataType);
			
			if(dvars != null && !dvars.isEmpty()) {
				vars.addAll(dvars);
			}
			
		}
		
		return vars;
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

	public void setTransitionUsageList(Map<String, List<String>> transitionUsageList) {
		this.transitionUsageList = transitionUsageList;
	}
}
