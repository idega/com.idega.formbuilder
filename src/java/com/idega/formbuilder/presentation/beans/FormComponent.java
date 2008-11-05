package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jdom.Document;

import com.idega.block.process.variables.Variable;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.ConstButtonType;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.business.component.properties.PropertiesButton;
import com.idega.xformsmanager.business.component.properties.PropertiesComponent;
import com.idega.xformsmanager.component.beans.ItemBean;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.formbuilder.presentation.components.FBVariableViewer;
import com.idega.formbuilder.util.FBUtil;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;

public class FormComponent extends GenericComponent {
	
	public static final String BEAN_ID = "formComponent";
	
	public static final String BUTTON_TYPE = "button";
	public static final String COMPONENT_TYPE = "component";
	
	private Component component;
	
	private ProcessPalette processPalette;
	private JbpmProcessBusinessBean jbpmProcessBusiness;
	private ProcessData processData;
	private FormPage formPage;
	
	public FormComponent() {}
	
	public FormComponent(Component component) {
		this.component = component;
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
	
	public Document assignVariable(String componentId, String variable) {
		if(componentId == null) {
			componentId = component.getId();
		}
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(componentId);
			PropertiesComponent properties = component.getProperties();
			if(properties != null) {
				Variable oldVar = properties.getVariable();
				if(oldVar != null) {
					processData.unbindVariable(oldVar.getDefaultStringRepresentation(), componentId);
				}
				properties.setVariable(variable);
				processData.bindVariable(componentId, variable).getStatus();
			}
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBVariableViewer(), true);
	}
	
	public Object[] assignVariableAndMoveComponent(String componentId, String variable, int before) throws Exception {
		Object[] results = new Object[3];
		Document doc = assignVariable(componentId, variable);
		results[0] = doc;
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
					result[1] = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
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
	
	public String addComponent(String type) throws Exception {
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
	
	public Document getRenderedButton(String id) {
		if(id == null) {
			return null;
		}
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(id);
				if(button != null) {
					return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(id, "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
				}
			}
		}
		return null;
	}
	
	public Object[] moveAndRenderComponent(String id, int before) throws Exception {
		Object[] result = new Object[2];
		Page page = formPage.getPage();
		if(before == -1) {
			result[0] = "append";
		} else {
			String beforeId = CoreConstants.EMPTY;
			if(page != null) {
				List<String> ids = page.getContainedComponentsIds();
				if(ids.contains(id)) {
					beforeId = ids.get(before);
					ids.remove(id);
					ids.add(before, id);
				}
				page.rearrangeComponents();
			}
			if(beforeId.length() > 0) {
				result[0] = beforeId;
			} else {
				result[0] = "append";
			}
			
		}
		Component component = page.getComponent(id);
		result[1] = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component), true);
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
	
	public String addButton(String type) {
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
			return button.getId();
//			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
		}
		return null;
	}
	
	public String removeComponent(String id) {
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(id);
			if(component != null) {
				component.remove();
			}
		}
		return id;
	}
	
	public String removeButton(String id) {
		if(id == null) {
			return null;
		}
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(id);
				if(button != null) {
					button.remove();
					return id;
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

	public String getValidationText() {
		return getComponent().getProperties().getValidationText().getString(FBUtil.getUILocale());
	}
	
	public void setValidationText(String validationText) {
		LocalizedStringBean bean = getComponent().getProperties().getValidationText();
		bean.setString(FBUtil.getUILocale(), validationText);
		getComponent().getProperties().setValidationText(bean);
	}

	
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

	public JbpmProcessBusinessBean getJbpmProcessBusiness() {
		return jbpmProcessBusiness;
	}

	public void setJbpmProcessBusiness(JbpmProcessBusinessBean jbpmProcessBusiness) {
		this.jbpmProcessBusiness = jbpmProcessBusiness;
	}

	public ProcessData getProcessData() {
		return processData;
	}

	public void setProcessData(ProcessData processData) {
		this.processData = processData;
	}

}