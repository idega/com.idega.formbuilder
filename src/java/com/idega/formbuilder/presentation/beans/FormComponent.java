package com.idega.formbuilder.presentation.beans;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;

import com.idega.block.process.variables.Variable;
import com.idega.builder.business.BuilderLogic;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.presentation.components.FBButtonArea;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.formbuilder.presentation.components.FBVariableList;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.ConstButtonType;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.component.beans.ItemBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;

public class FormComponent extends GenericComponent {
	
	public static final String BEAN_ID = "formComponent";
	
	public static final String BUTTON_TYPE = "button";
	public static final String COMPONENT_TYPE = "component";
	
	private static final String APPEND = "append";
	
	protected Component component;
	
	private ProcessPalette processPalette;
	private ProcessData processData;
	private FormPage formPage;
	private ComponentPropertyManager propertyManager;
	
	public FormComponent() {}
	
	public FormComponent(Component component) {
		this.component = component;
	}
	
	public ComponentPropertyManager getPropertyManager() {
		return propertyManager;
	}

	public void setPropertyManager(ComponentPropertyManager propertyManager) {
		this.propertyManager = propertyManager;
	}

	public FormPage getFormPage() {
		return formPage;
	}
	
	public String getId() {
		return component.getId();
	}

	public void setFormPage(FormPage formPage) {
		this.formPage = formPage;
	}
	
	public String[] assignVariable(String componentId, String variable) {
		if(componentId == null) {
			componentId = component.getId();
		}
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(componentId);
			PropertiesComponent properties = component.getProperties();
			if(properties != null) {
				Variable oldVar = properties.getVariable();
				String oldVarName = null;
				if(oldVar != null) {
					oldVarName = oldVar.getDefaultStringRepresentation();
					processData.unbindVariable(oldVarName, componentId);
				}
				properties.setVariable(variable);
				
				String[] result = new String[3];
				result[0] = processData.bindVariable(componentId, variable).getStatus();
				result[1] = oldVarName;
				result[2] = oldVarName == null ? null : processData.getVariableStatus(oldVarName).getStatus();
				
				return result;
			}
		}
		return null;
	}
	
	public String[] removeVariableBinding(String componentId) {
		if(componentId == null) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(componentId);
			PropertiesComponent properties = component.getProperties();
			if(properties != null) {
				Variable oldVar = properties.getVariable();
				String oldVarName = null;
				if(oldVar != null) {
					oldVarName = oldVar.getDefaultStringRepresentation();
					processData.unbindVariable(oldVarName, componentId);
				}
				properties.setVariable(CoreConstants.EMPTY);
				
				String[] result = new String[2];
				result[0] = oldVarName;
				result[1] = oldVarName == null ? null : processData.getVariableStatus(oldVarName).getStatus();
				
				return result;
			}
		}
		
		return null;
	}
	
	public String[] removeTransitionBinding(String buttonId) {
		if(buttonId == null) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					String action = properties.getReferAction();
					properties.setReferAction(null);
					
					processData.unbindTransition(buttonId, action).getStatus();
					
					String[] result = new String[2];
					result[0] = action;
					result[1] = action == null ? null : processData.getTransitionStatus(action).getStatus();
					
					return result;
				}
			}
		}
		
		return null;
	}
	
	public String[] assignTransition(String buttonId, String transition) {
		if(StringUtils.isEmpty(buttonId) || buttonId.equals("-1")) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					String action = properties.getReferAction();
					properties.setReferAction(transition);
					
					String[] result = new String[4];
					result[0] = processData.bindTransition(buttonId, transition).getStatus();
					result[1] = action;
					result[2] = action == null ? null : processData.getTransitionStatus(action).getStatus();
					
					return result;
				}
			}
		}
		
		return null;
	}
	
	public String getDataSrc() {return null;}

	public void setDataSrc(String dataSrc) {}
	
	public Document getAvailableProcessDataList(String type, boolean transition) {
		if(StringUtils.isEmpty(type)) {
			return null;
		}
		
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBVariableList(type, transition), true);
	}
	
	public Object[] addButton(String type, String before, String transition) {
		if(type == null) {
			return null;
		}
		
		Object[] result = new Object[3];
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			
			if(area == null) {
				area = page.createButtonArea(null);
			}
			
			String beforeId = null;
			if(StringUtils.isEmpty(before)) {
				result[0] = APPEND;
			} else {
				List<String> ids = area.getContainedComponentsIds();
				int beforeInt = ids.indexOf(before);
				if(ids.size() > beforeInt + 1) {
					beforeId = ids.get(beforeInt + 1);
				}
			}
			
			Button button = area.addButton(ConstButtonType.getByStringType(type), beforeId);
			Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
			if(workspace.isProcessMode() && !StringUtils.isEmpty(transition)) {
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					properties.setReferAction(transition);
					
					result[2] = processData.bindTransition(button.getId(), transition).getStatus();
				}
			}
			
			if(result[0] == null) {
				result[0] = beforeId;
			}
			result[1] = getButton(button);
		}
		return result;
	}
	
	public Object[] addComponent(String type, String before, String variable) throws Exception {
		if(type == null) {
			return null;
		}
		
		Object[] result = new Object[3];
		
		Page page = formPage.getPage();
		if(page.isSpecialPage()) {
			return null;
		}
		if(page != null) {
			String beforeId = null;
			if(StringUtils.isEmpty(before)) {
				result[0] = APPEND;
				
				ButtonArea area = page.getButtonArea();
				if(area != null) {
					beforeId = area.getId();
				}
			} else {
				List<String> ids = page.getContainedComponentsIds();
				int beforeInt = ids.indexOf(before);
				if(ids.size() > beforeInt + 1) {
					beforeId = page.getContainedComponentsIds().get(beforeInt + 1);
					
					ButtonArea area = page.getButtonArea();
					if(area != null && beforeId.equals(area.getId())) {
						result[0] = APPEND;
					}
				}
			}
			Component component = page.addComponent(type, beforeId);
			Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
			if(workspace.isProcessMode() && !StringUtils.isEmpty(variable)) {
				PropertiesComponent properties = component.getProperties();
				if(properties != null) {
					properties.setVariable(variable);
					
					result[2] = processData.bindVariable(component.getId(), variable).getStatus();
				}
			}
			if(result[0] == null) {
				result[0] = beforeId;
			}
			result[1] = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component), true);
		}
		return result;
	}
	
	public String moveComponent(String id, int before) throws Exception {
		if(before == -1) {
			return APPEND;
		} else {
			Page page = formPage.getPage();
			String beforeId = CoreConstants.EMPTY;
			if(page != null) {
				List<String> ids = page.getContainedComponentsIds();
				if(ids.indexOf(id) != -1) {
					beforeId = ids.get(before);
					ids.remove(id);
					ids.add(before, id);
				}
				page.rearrangeComponents();
			}
			return beforeId;
		}
	}
	
	private Document getButtonArea(String styleClass, String componentStyleClass) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButtonArea(styleClass, componentStyleClass), true);
	}
	
	private Document getButton(Button button) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
	}
	
	private Document getPropertiesPanel(GenericComponent component) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(component),true);
	}
	
	private Document getDesignView() {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
	}
	
	public Object[] removeComponent(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(id);
			if(component != null) {
				boolean update = propertyManager.resetComponent(component);
				
				component.remove();
				
				Object[] result = new Object[5];
				result[0] = id;
				result[1] = update ? getPropertiesPanel(null) : null;
				result[2] = formPage.hasRegularComponents() ? null : getDesignView();
				
				Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
				if(workspace.isProcessMode()) {
					PropertiesComponent properties = component.getProperties();
					if(properties != null) {
						Variable oldVar = properties.getVariable();
						String oldVarName = null;
						if(oldVar != null) {
							oldVarName = oldVar.getDefaultStringRepresentation();
							processData.unbindVariable(oldVarName, id);
						}
						
						result[3] = oldVarName;
						result[4] = oldVarName == null ? null : processData.getVariableStatus(oldVarName).getStatus();
						
					}
				}
				
				return result;
			}
		}
		return null;
	}
	
	public Object[] removeButton(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(id);
				if(button != null) {
					boolean update = propertyManager.resetComponent(button);
					
					Object[] result = new Object[5];
					result[0] = id;
					
					Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
					if(workspace.isProcessMode()) {
						PropertiesButton properties = button.getProperties();
						if(properties != null) {
							String action = properties.getReferAction();
							if(action != null) {
								processData.unbindTransition(id, action).getStatus();
							}
							
							result[3] = action;
							result[4] = action == null ? null : processData.getTransitionStatus(action).getStatus();
						}
					}
					
					button.remove();
					
					result[1] = update ? getPropertiesPanel(null) : null;
					result[2] = area.getContainedComponentsIds().isEmpty() ? getButtonArea("formElement formElementHover", "formButton") : null;
					
					return result;
				}
			}
		}
		return null;
	}

	public String getErrorMessage(ErrorType errorType) {
		return getComponent().getProperties().getErrorMsg(errorType).getString(FBUtil.getUILocale());
	}

	public void setErrorMessage(ErrorType errorType, String errorMessage) {
		LocalizedStringBean bean = getComponent().getProperties().getErrorMsg(errorType);
		bean.setString(FBUtil.getUILocale(), errorMessage);
		getComponent().getProperties().setErrorMsg(errorType, bean);
	}

	public String getLabel() {
		return getComponent().getProperties().getLabel().getString(FBUtil.getUILocale());
	}

	public void setLabel(String label) {
		LocalizedStringBean bean = getComponent().getProperties().getLabel();
		if(bean == null) {
			bean = new LocalizedStringBean();
		}
		bean.setString(FBUtil.getUILocale(), label);
		getComponent().getProperties().setLabel(bean);
	}
	
	public boolean getRequired() {
		return getComponent().getProperties().isRequired();
	}

	public void setRequired(boolean required) {
		getComponent().getProperties().setRequired(required);
	}

	public List<ItemBean> getItems() {return null;}

	public void setItems(List<ItemBean> items) {}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public String getHelpMessage() {
		return getComponent().getProperties().getHelpText().getString(FBUtil.getUILocale());
	}
	
	public void setHelpMessage(String helpMessage) {
		LocalizedStringBean bean = getComponent().getProperties().getHelpText();
		bean.setString(FBUtil.getUILocale(), helpMessage);
		getComponent().getProperties().setHelpText(bean);
	}

	public String getAutofillKey() {
		return getComponent().getProperties().getAutofillKey();
	}

	public void setAutofillKey(String autofillKey) {
		if(StringUtils.isEmpty(autofillKey)) {
			return;
		}
		getComponent().getProperties().setAutofillKey(autofillKey);
	}

	public String getPlainText() {return null;}

	public void setPlainText(String plainText) {}
	
	public void setAddButtonLabel(String value) {}
	
	public void setRemoveButtonLabel(String value) {}
	
	public void setExternalSrc(String externalSrc) {}
	
	public String getExternalSrc() {return null;}
	
	public String getRemoveButtonLabel() {return null;}
	
	public String getAddButtonLabel() {return null;}
	
	public String getUploadDescription() {return null;}
	
	public void setUploadDescription(String value) {}

	public ProcessPalette getProcessPalette() {
		return processPalette;
	}

	public void setProcessPalette(ProcessPalette processPalette) {
		this.processPalette = processPalette;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public void setProcessData(ProcessData processData) {
		this.processData = processData;
	}

}