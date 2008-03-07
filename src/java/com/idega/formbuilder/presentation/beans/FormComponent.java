package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;

import com.idega.builder.bean.AdvancedProperty;
import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.ComponentPlain;
import com.idega.documentmanager.business.component.ComponentSelect;
import com.idega.documentmanager.business.component.ConstButtonType;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesButton;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.beans.ItemBean;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.formbuilder.util.FBConstants;
import com.idega.formbuilder.util.FBUtil;
import com.idega.jbpm.business.JbpmProcessBusinessBean;
import com.idega.jbpm.def.Variable;
import com.idega.util.CoreUtil;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	public static final String BEAN_ID = "formComponent";
	
	public static final String BUTTON_TYPE = "button";
	public static final String COMPONENT_TYPE = "component";
	
	private Button button;
	private Component component;
	private ComponentSelect selectComponent;
	private ComponentPlain plainComponent;
	private String id;
	
	private ProcessPalette processPalette;
	private JbpmProcessBusinessBean jbpmProcessBusiness;
	private ProcessData processData;
	private FormPage formPage;
	
	public FormPage getFormPage() {
		return formPage;
	}

	public void setFormPage(FormPage formPage) {
		this.formPage = formPage;
	}
	
	public String assignVariable(String componentId, String variable, String datatype) {
		if(componentId == null) {
			componentId = id;
		}
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.getComponent(componentId);
			PropertiesComponent properties = component.getProperties();
			if(properties != null) {
				properties.setVariable(Variable.parseDefaultStringRepresentation(datatype + ":" + variable));
				return processData.bindVariable(componentId, datatype + ":" + variable).getStatus();
			}
		}
		return null;
	}
	
	public String assignTransition(String buttonId, String transition) {
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(buttonId);
				PropertiesButton properties = button.getProperties();
				if(properties != null) {
					properties.setReferAction(transition);
					return processData.bindTransition(buttonId, transition).getStatus();
				}
			}
		}
		return null;
	}
	
	public void initializeBeanInstace(Component component) {
		if(component instanceof ComponentPlain) {
			this.plainComponent = (ComponentPlain) component;
			this.selectComponent = null;
			this.component = null;
			this.id = component.getId();
		} else if(component instanceof ComponentSelect) {
			this.selectComponent = (ComponentSelect) component;
			this.component = null;
			this.plainComponent = null;
			this.id = component.getId();
		} else if(component instanceof Component) {
			this.component = component;
			this.selectComponent = null;
			this.plainComponent = null;
			this.id = component.getId();
		} else {
			this.component = null;
			this.selectComponent = null;
			this.plainComponent = null;
			this.id = null;
		}
	}
	
	public void initializeBeanInstace(String id, String type) {
		Page page = formPage.getPage();
		if(page != null) {
			if(BUTTON_TYPE.equals(type)) {
				ButtonArea area = page.getButtonArea();
				if(area != null) {
					Button button = (Button) area.getComponent(id);
					this.button = button;
					this.id = button.getId();
					this.selectComponent = null;
					this.component = null;
					this.plainComponent = null;
				}
			} else if(COMPONENT_TYPE.equals(type)) {
				this.button = null;
				Component component = page.getComponent(id);
				if(component instanceof ComponentPlain) {
					this.plainComponent = (ComponentPlain) component;
					this.selectComponent = null;
					this.component = null;
					this.id = component.getId();
				} else if(component instanceof ComponentSelect) {
					this.selectComponent = (ComponentSelect) component;
					this.component = null;
					this.plainComponent = null;
					this.id = component.getId();
				} else if(component instanceof Component) {
					this.component = component;
					this.selectComponent = null;
					this.plainComponent = null;
					this.id = component.getId();
				} else {
					this.component = null;
					this.selectComponent = null;
					this.plainComponent = null;
					this.id = null;
				}
			}
		}
	}
	
	public Document saveComponentLabel(String value) {
		setLabel(value);
		if(button != null) {
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(id), true);
		} else {
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
		}
	}
	
	public void saveComponentAction(String value) {

		if(button != null)
			button.getProperties().setReferAction(value);
	}
	
	public void saveComponentProcessVariableName(String value) {
		
		Component component = this.component != null ? this.component : this.selectComponent != null ? this.selectComponent : this.plainComponent != null ? this.plainComponent : null;
		
		if(component != null)
			component.getProperties().setVariable(value);
	}
	
	public Document saveComponentExternalSrc(String value) {
		if(value != null && !"".equals(value))
			setExternalSrc(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveComponentRequired(boolean value) {
		setRequired(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveSelectOptionLabel(int index, String label) {
		saveLabel(index, label);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveSelectOptionValue(int index, String value) {
		saveValue(index, value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveComponentErrorMessage(String value) {
		setErrorMessage(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveComponentHelpMessage(String value) {
		setHelpMessage(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveComponentPlainText(String value) {
		setPlainText(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveComponentAutofillKey(String value) {
		setAutofillKey(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public List<AdvancedProperty> getAvailableComponentVariables(String type) {
		Set<String> variables = getProcessData().getComponentTypeVariables(type);
		List<AdvancedProperty> result = new ArrayList<AdvancedProperty>();
		for(Iterator<String> it = variables.iterator(); it.hasNext(); ) {
			String var = it.next();
			result.add(new AdvancedProperty(var, var));
		}
		return result;
	}
	
//	public Document assignComponentToVariable(String variable, String componentId, String type) {
//		FBAssignVariableComponent newAssign = new FBAssignVariableComponent();
//		newAssign.setId(componentId + "-" + type);
//		if(variable != null) {
//			Page page = formPage.getPage();
//			if(page != null) {
//				Component component = page.getComponent(componentId);
//				PropertiesComponent properties = component.getProperties();
//				if(properties != null) {
//					properties.setVariable(Variable.parseDefaultStringRepresentation(variable));
//				}
//			}
//			newAssign.setValue(variable.substring(variable.indexOf(":") + 1));
//		}
//		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), newAssign, true);
//	}
	
	public String getDataSrc() {
		if(selectComponent != null) {
			if(selectComponent.getProperties().getDataSrcUsed() != null) {
				return selectComponent.getProperties().getDataSrcUsed().toString();
			} else {
				return DataSourceList.localDataSrc;
			}
		}
		return null;
	}
	
	public void setDataSrc(String dataSrc) {
		if(selectComponent != null) {
			selectComponent.getProperties().setDataSrcUsed(Integer.parseInt(dataSrc));
		}
	}

	public Document removeSelectOption(int index) {
		removeItem(index);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public void removeItem(int index) {
		if(index < getItems().size()) {
			getItems().remove(index);
		}
	}
	
	public void saveLabel(int index, String value) {
		int size = getItems().size();
		List<ItemBean> itemSet = getItems();
		if(index >= size) {
			ItemBean newItem = new ItemBean();
			newItem.setLabel(value);
			newItem.setValue(value);
			itemSet.add(newItem);
		} else {
			itemSet.get(index).setLabel(value);
			itemSet.get(index).setValue(value);
		}
		setItems(itemSet);
	}
	
	public void saveValue(int index, String value) {
		List<ItemBean> itemSet = getItems();
		if(index >= itemSet.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setValue(value);
			itemSet.add(newItem);
		} else {
			itemSet.get(index).setValue(value);
		}
		setItems(itemSet);
	}
	
	public Document getFormButtonInfo(String id) {
		initializeBeanInstace(id, BUTTON_TYPE);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(id, FBConstants.BUTTON_TYPE), true);
	}
	
	public Document getFormComponentInfo(String id) {
		initializeBeanInstace(id, COMPONENT_TYPE);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(id, FBConstants.COMPONENT_TYPE), true);
	}
	
	public Document switchDataSource() {
		if(getDataSrc().equals(DataSourceList.externalDataSrc)) {
			setDataSrc(DataSourceList.localDataSrc);
		} else {
			setDataSrc(DataSourceList.externalDataSrc);
			getItems().clear();
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(id, FBConstants.COMPONENT_TYPE), true);
	}
	
	public Document addComponent(String type) throws Exception {
		if(type == null) {
			return null;
		}
		
		Page page = formPage.getPage();
		if(page != null) {
			String before = null;
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				before = area.getId();
			}
			Component component = page.addComponent(type, before);
			if(component != null) {
				return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component.getId()), true);
			}
		}
		return null;
	}
	
	public String moveComponent(String id, int before) throws Exception {
		if(before == -1) {
			return "append";
		} else {
			Page page = formPage.getPage();
			String beforeId = "";
			if(page != null) {
				List<String> ids = page.getContainedComponentsIdList();
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
	
	public Document addButton(String type) {
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
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
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

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getErrorMessage() {
		if(component != null) {
			return component.getProperties().getErrorMsg().getString(FBUtil.getUILocale());
		} else if(selectComponent != null) {
			return selectComponent.getProperties().getErrorMsg().getString(FBUtil.getUILocale());
		}
		return null;
	}

	public void setErrorMessage(String errorMessage) {
		LocalizedStringBean bean = null;
		if(component != null) {
			bean = component.getProperties().getErrorMsg();
			bean.setString(FBUtil.getUILocale(), errorMessage);
			component.getProperties().setErrorMsg(bean);
		} else if(selectComponent != null) {
			bean = selectComponent.getProperties().getErrorMsg();
			bean.setString(FBUtil.getUILocale(), errorMessage);
			selectComponent.getProperties().setErrorMsg(bean);
		}
	}

	public String getLabel() {
		if(component != null) {
			return component.getProperties().getLabel().getString(FBUtil.getUILocale());
		} else if(selectComponent != null) {
			return selectComponent.getProperties().getLabel().getString(FBUtil.getUILocale());
		}
		return null;
	}

	public void setLabel(String label) {
		LocalizedStringBean bean = null;
		if(component != null) {
			bean = component.getProperties().getLabel();
			bean.setString(FBUtil.getUILocale(), label);
			component.getProperties().setLabel(bean);
		} else if(selectComponent != null) {
			bean = selectComponent.getProperties().getLabel();
			bean.setString(FBUtil.getUILocale(), label);
			selectComponent.getProperties().setLabel(bean);
		} else if(button != null) {
			bean = button.getProperties().getLabel();
			bean.setString(FBUtil.getUILocale(), label);
			button.getProperties().setLabel(bean);
		} else if(plainComponent != null) {
			bean = plainComponent.getProperties().getLabel();
			
			if(bean == null)
				bean = new LocalizedStringBean();
			
			bean.setString(FBUtil.getUILocale(), label);
			plainComponent.getProperties().setLabel(bean);
		}
	}

	public boolean getRequired() {
		if(component != null) {
			return component.getProperties().isRequired();
		} else if(selectComponent != null) {
			return selectComponent.getProperties().isRequired();
		}
		return false;
	}

	public void setRequired(boolean required) {
		if(component != null) {
			component.getProperties().setRequired(required);
		} else if(selectComponent != null) {
			selectComponent.getProperties().setRequired(required);
		}
	}

	public String getExternalSrc() {
		if(selectComponent != null) {
			return selectComponent.getProperties().getExternalDataSrc();
		}
		return null;
	}

	public void setExternalSrc(String externalSrc) {
		if(selectComponent != null) {
			selectComponent.getProperties().setExternalDataSrc(externalSrc);
		}
	}

	public List<ItemBean> getItems() {
		if(selectComponent != null) {
			return selectComponent.getProperties().getItemset().getItems(FBUtil.getUILocale());
		}
		return new ArrayList<ItemBean>();
	}

	public void setItems(List<ItemBean> items) {
		if(selectComponent != null) {
			selectComponent.getProperties().getItemset().setItems(FBUtil.getUILocale(), items);
		}
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public ComponentSelect getSelectComponent() {
		return selectComponent;
	}

	public void setSelectComponent(ComponentSelect selectComponent) {
		this.selectComponent = selectComponent;
	}

	public String getHelpMessage() {
		if(component != null) {
			return component.getProperties().getHelpText().getString(FBUtil.getUILocale());
		} else if(selectComponent != null) {
			return selectComponent.getProperties().getHelpText().getString(FBUtil.getUILocale());
		}
		return null;
	}
	
	public String getVariableName() {
		if(component != null) {
			return component.getProperties().getVariable() == null ? null : component.getProperties().getVariable().getDefaultStringRepresentation();
		} else if(selectComponent != null) {
			return selectComponent.getProperties().getVariable() == null ? null : selectComponent.getProperties().getVariable().getDefaultStringRepresentation();
		}
		return null;
	}

	public void setHelpMessage(String helpMessage) {
		LocalizedStringBean bean = null;
		if(component != null) {
			bean = component.getProperties().getHelpText();
			bean.setString(FBUtil.getUILocale(), helpMessage);
			component.getProperties().setHelpText(bean);
		} else if(selectComponent != null) {
			bean = selectComponent.getProperties().getHelpText();
			bean.setString(FBUtil.getUILocale(), helpMessage);
			selectComponent.getProperties().setHelpText(bean);
		}
	}

	public String getAutofillKey() {
		if(component != null) {
			return component.getProperties().getAutofillKey();
		} else if(selectComponent != null) {
			return selectComponent.getProperties().getAutofillKey();
		}
		return null;
	}

	public void setAutofillKey(String autofillKey) {
		if(autofillKey == null || "".equals(autofillKey)) {
			return;
		}
		if(component != null) {
			component.getProperties().setAutofillKey(autofillKey);
		} else if(selectComponent != null) {
			selectComponent.getProperties().setAutofillKey(autofillKey);
		}
	}

	public Document saveAutofill(boolean autofill) {
		if(autofill) {
			setAutofillKey("example");
		} else {
			setAutofillKey("");
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(id, FBConstants.COMPONENT_TYPE), true);
	}

	public String getPlainText() {
		if(plainComponent != null) {
			return plainComponent.getProperties().getText() == null ? null : plainComponent.getProperties().getText().getString(FBUtil.getUILocale());
		}
		return null;
	}

	public void setPlainText(String plainText) {
		if(plainComponent != null) {
			
			LocalizedStringBean text = plainComponent.getProperties().getText();
			
			if(text == null)
				text = new LocalizedStringBean();
			
			text.setString(FBUtil.getUILocale(), plainText);
			plainComponent.getProperties().setText(text);
		}
	}

	public ComponentPlain getPlainComponent() {
		return plainComponent;
	}

	public void setPlainComponent(ComponentPlain plainComponent) {
		this.plainComponent = plainComponent;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

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