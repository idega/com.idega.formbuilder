package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;

import com.idega.block.process.variables.Variable;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.presentation.components.FBButtonArea;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
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
	
	public Object[] assignVariableAndMoveComponent(String componentId, String variable, int before) throws Exception {
		Object[] results = new Object[3];
//		Object[] results2 = assignVariable(componentId, variable);
//		results[0] = doc;
		String move = moveComponent(componentId, before);
		results[1] = move;
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(componentId);
			Document comp = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component), true);
			results[2] = comp;
		}
		return results;
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
					result[1] = action == null ? null : processData.getVariableStatus(action).getStatus();
					
					return result;
				}
			}
		}
		
		return null;
	}
	
	public String[] assignTransition(String buttonId, String transition) {
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					String action = properties.getReferAction();
					properties.setReferAction(transition);
					
					String[] result = new String[3];
					result[0] = processData.bindTransition(buttonId, transition).getStatus();
					result[1] = action;
					result[2] = action == null ? null : processData.getTransitionStatus(action).getStatus();
					
					return result;
				}
			}
		}
		
		return null;
	}
	
	public Object[] assignTransitionAndRenderButton(String buttonId, String transition) {
		Object[] result = new Object[2];
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					properties.setReferAction(transition);
					result[0] = processData.bindTransition(buttonId, transition).getStatus();
					result[1] = getButton(button);
				}
			}
		}
		return result;
	}
	
	public List<AdvancedProperty> getAvailableComponentVariables(String type) {
		Set<String> variables = getProcessData().getComponentTypeVariables(type);
		List<AdvancedProperty> result = new ArrayList<AdvancedProperty>();
		for(String var : variables) {
			String name = var.split(":")[1];
			result.add(new AdvancedProperty(var, name));
		}
		
		return result;
	}
	
	public String getDataSrc() {return null;}

	public void setDataSrc(String dataSrc) {}
	
	public Object[] addTaskComponent(String type) throws Exception {
		Object[] result = new Object[3];
		
		String doc = addComponent(type);
		result[0] = doc;
		
		Set<String> datatype = getProcessPalette().getComponentDatatype(type);
		result[1] = datatype;
		
		Set<String> vars = getProcessData().getComponentTypeVariables(type);
		result[2] = vars;
		
		return result;
	}
	
//	public String addButton(String type) {
//		Page page = formPage.getPage();
//		if(page != null) {
//			ButtonArea area = page.getButtonArea();
//			Button button = null;
//			if(area != null) {
//				button = area.addButton(ConstButtonType.getByStringType(type), null);
//			} else {
//				area = page.createButtonArea(null);
//				button = area.addButton(ConstButtonType.getByStringType(type), null);
//			}
//			return button.getId();
////			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
//		}
//		return null;
//	}
	
	public Document getRenderedButton(String type) {
		if(type == null) {
			return null;
		}
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			Button button = null;
			if(area != null) {
				button = area.addButton(ConstButtonType.getByStringType(type), null);
			} else {
				area = page.createButtonArea(null);
				button = area.addButton(ConstButtonType.getByStringType(type), null);
			}
			return getButton(button);
		}
		return null;
	}
	
	private String addComponent(String type) throws Exception {
		if(type == null) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(formPage.isSpecial()) {
			return null;
		}
		if(page != null) {
			String before = null;
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				before = area.getId();
			}
			Component component = page.addComponent(type, before);
			if(component != null) {
				return component.getId();
			}
		}
		return null;
	}
	
	public Object[] moveAndRenderComponent(String type, int before) throws Exception {
		if(type == null) {
			return null;
		}
		
		Object[] result = new Object[2];
		
		Page page = formPage.getPage();
		if(formPage.isSpecial()) {
			return null;
		}
		if(page != null) {
			String beforeId = null;
			if(before == -1) {
				result[0] = "append";
				
				ButtonArea area = page.getButtonArea();
				if(area != null) {
					beforeId = area.getId();
				}
			} else {
				List<String> ids = page.getContainedComponentsIds();
				if(ids.size() > before + 1) {
					beforeId = page.getContainedComponentsIds().get(before + 1);
					
					ButtonArea area = page.getButtonArea();
					if(area != null && beforeId.equals(area.getId())) {
						result[0] = "append";
					}
				}
			}
			Component component = page.addComponent(type, beforeId);
			if(result[0] == null) {
				result[0] = beforeId;
			}
			result[1] = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component), true);
		}
		return result;
	}
	
	public String moveComponent(String id, int before) throws Exception {
		if(before == -1) {
			return "append";
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
				
				Object[] result = new Object[3];
				result[0] = id;
				result[1] = update ? getPropertiesPanel(null) : null;
				result[2] = formPage.hasRegularComponents() ? null : getDesignView();
				
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
					
					button.remove();
					
					Object[] result = new Object[3];
					result[0] = id;
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

//	public String getValidationText() {
//		return getComponent().getProperties().getValidationText().getString(FBUtil.getUILocale());
//	}
//	
//	public void setValidationText(String validationText) {
//		LocalizedStringBean bean = getComponent().getProperties().getValidationText();
//		bean.setString(FBUtil.getUILocale(), validationText);
//		getComponent().getProperties().setValidationText(bean);
//	}
	
	public String getAutofillKey() {
			return getComponent().getProperties().getAutofillKey();
	}

	public void setAutofillKey(String autofillKey) {
		if(autofillKey == null || CoreConstants.EMPTY.equals(autofillKey)) {
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