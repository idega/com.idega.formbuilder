package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.JbpmContext;
import org.jbpm.JbpmException;
import org.jbpm.context.def.VariableAccess;
import org.jbpm.taskmgmt.def.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.block.process.variables.Variable;
import com.idega.block.process.variables.VariableDataType;
import com.idega.builder.business.BuilderLogic;
import com.idega.jbpm.BPMContext;
import com.idega.jbpm.JbpmCallback;
import com.idega.jbpm.data.ViewTaskBind;
import com.idega.jbpm.exe.BPMFactory;
import com.idega.jbpm.exe.ProcessDefinitionW;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableBodyRowGroup;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.text.Heading3;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.util.CoreConstants;
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
	
	public static final String DEFAULT_ACCESS = "read,write";
	public static final String DEFAULT_REQUIRED_ACCESS = "read,write,required";
	
	private static final String[] AVAILABLE_ACCESES  = {"read", "write", "required"};
	private static final VariableDataType[] AVAILABE_TYPES = { VariableDataType.STRING, VariableDataType.DATE, VariableDataType.LIST, VariableDataType.FILE, VariableDataType.FILES, VariableDataType.OBJLIST };
	
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

	private BPMContext idegaJbpmContext;
	
	public BPMFactory getBpmFactory() {
		return bpmFactory;
	}

	public void setBpmFactory(BPMFactory bpmFactory) {
		this.bpmFactory = bpmFactory;
	}

	public Map<String, List<Variable>> getDatatypedVariables() {
		if (datatypedVariables.isEmpty()) {
			for (VariableDataType type : AVAILABE_TYPES) {
				datatypedVariables.put(type.toString(), new ArrayList<Variable>());
			}
			for (Variable variable : variables) {
				String varType = variable.getDataType().toString();

				if (datatypedVariables.containsKey(varType)) {
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
		initializeVariablesAndTransitions();
		this.transitionUsageList.clear();
		this.datatypedVariables.clear();
		this.variableUsageList.clear();
		
		
	}
	
	private void initializeVariablesAndTransitions() {
		this.variables.clear();
		this.transitions.clear();
		this.datatypedVariables.clear();
		ProcessDefinitionW pdw = getBpmFactory().getProcessManager(processId).getProcessDefinition(processId);
		
		variables.addAll(pdw.getTaskVariableWithAccessesList(taskName));
		
		Collection<String> transitionNames = pdw.getTaskNodeTransitionsNames(taskName);
		
		if(transitionNames != null) {
			transitions.addAll(transitionNames);
		}
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
	
	public void createVariable(final String variable, final String datatype, final boolean required) {
		if (variable != null && datatype != null) {
		getIdegaJbpmContext().execute(new JbpmCallback() {

			public Object doInJbpm(JbpmContext context) throws JbpmException {
				Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
				Long parentFormId = workspace.getParentFormId();
				ViewTaskBind vtb = getBpmFactory().getBPMDAO().getViewTaskBindByView(parentFormId.toString(), "xforms");
				Task task = getBpmFactory().getBPMDAO().getTaskFromViewTaskBind(vtb);
			    task = getIdegaJbpmContext().mergeProcessEntity(task);
				List<VariableAccess> variableAccesses = (List<VariableAccess>)task.getTaskController().getVariableAccesses();
				String newName = datatype + CoreConstants.UNDER + variable;
				for (VariableAccess variableAccess : variableAccesses) {
					if (variableAccess.getVariableName().equals(newName)) {
						//exists already
						return null;
					}
				}
				VariableAccess variableAccess = new VariableAccess(newName, required ? DEFAULT_REQUIRED_ACCESS : DEFAULT_ACCESS, null);
				getIdegaJbpmContext().saveProcessEntity(variableAccess);
				variableAccesses.add(variableAccess);
				getIdegaJbpmContext().mergeProcessEntity(task);
				return null;
			}
		});
		initializeVariablesAndTransitions();
		}
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
	
	public void unbindTransitions(List<String> buttonIds) {
		if(buttonIds == null) {
			return;
		}
		
		for(String buttonId : buttonIds) {
			for(String transition : getTransitionUsageList().keySet()) {
				List<String> list = getTransitionUsageList().get(transition);
				if(list != null) {
					list.remove(buttonId);
				}
			}
		}
	}
	
	public void unbindVariables(List<String> componentIds) {
		if(componentIds == null) {
			return;
		}
		
		for(String componentId : componentIds) {
			for(String variable : getVariableUsageList().keySet()) {
				List<String> list = getVariableUsageList().get(variable);
				if(list != null) {
					list.remove(componentId);
				}
			}
		}
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
	
	public org.jdom.Document getVariableAccessesBox(String variableName) {
		
		if (variableName == null) {
			return null;
		}
		
		Variable targetVariable = null;
		
		List<Variable> variables = getVariables();
		for (Variable variable : variables) {
			if (variable.getName().equals(variableName)) {
				targetVariable = variable;
				break;
			}
		}
		
		if (targetVariable == null) {
			return null;
		}
		
		Layer container = new Layer();
		Heading3 heading3 = new Heading3("Accesses");
		heading3.setStyleAttribute("align", "center");
		container.add(heading3);
		Table2 table = new Table2();
		TableBodyRowGroup rowGroup = table.createBodyRowGroup();
		for (String access : AVAILABLE_ACCESES) {
			TableRow accessRow = rowGroup.createRow();
			TableCell2 nameCell = accessRow.createCell();
			nameCell.add(new Text(access));
			TableCell2 checkboxCell = accessRow.createCell();
			CheckBox checkBox = new CheckBox(access, access);
			checkBox.setStyleClass(access);
			checkBox.setChecked(targetVariable.hasAccess(access));
			checkboxCell.add(checkBox);
		}
		TableRow buttonRow = rowGroup.createRow();
		TableCell2 saveCell = buttonRow.createCell();
		GenericButton saveButton = new GenericButton("save");
		saveButton.setOnClick("saveVariableAccessRights(this, '" + targetVariable.getDataType().toString() + "_" + variableName + "');closeVariableAccessRightsBox(this);");
		saveCell.add(saveButton);
		TableCell2 cancelCell = buttonRow.createCell();
		GenericButton cancelButton = new GenericButton("cancel");
		cancelButton.setOnClick("closeVariableAccessRightsBox(this);");
		cancelCell.add(cancelButton);
		container.add(table);
		return BuilderLogic.getInstance().getRenderedComponent(IWContext.getCurrentInstance(), container, true);
	}
	
	public boolean saveVariableAccesses(final String variableName, final String access) {
		if (variableName != null && access != null) {
			final StringBuilder accessString = new StringBuilder();
			for (String availableAccess : AVAILABLE_ACCESES) {
				if (access.indexOf(availableAccess) > -1) {
					accessString.append(accessString.length() > 0 ? "," : "").append(availableAccess);
				}
			}
			getIdegaJbpmContext().execute(new JbpmCallback() {

				public Object doInJbpm(JbpmContext context) throws JbpmException {
					Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
					Long parentFormId = workspace.getParentFormId();
					ViewTaskBind vtb = getBpmFactory().getBPMDAO().getViewTaskBindByView(parentFormId.toString(), "xforms");
					Task task = getBpmFactory().getBPMDAO().getTaskFromViewTaskBind(vtb);
				    task = getIdegaJbpmContext().mergeProcessEntity(task);
					List<VariableAccess> variableAccesses = task.getTaskController().getVariableAccesses();
					for (VariableAccess variableAccess : variableAccesses)
					{
						if (variableAccess.getVariableName().equals(variableName)) {
							VariableAccess newVariableAccess = new VariableAccess(variableName, accessString.toString(), variableAccess.getMappedName());
							getIdegaJbpmContext().saveProcessEntity(newVariableAccess);
							variableAccesses.add(newVariableAccess);
							variableAccesses.remove(variableAccess);
							getIdegaJbpmContext().mergeProcessEntity(task);
							return null;
						}
					}
					return null;	
				}
			});
			initializeVariablesAndTransitions();
		}
		return true;
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
	
	public BPMContext getIdegaJbpmContext() {
		return idegaJbpmContext;
	}

	@Autowired
	public void setIdegaJbpmContext(BPMContext idegaJbpmContext) {
		this.idegaJbpmContext = idegaJbpmContext;
	}
}
