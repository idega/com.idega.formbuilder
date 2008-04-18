package com.idega.formbuilder.presentation.beans;

import java.util.List;

import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.business.component.ComponentPlain;
import com.idega.documentmanager.business.component.ComponentSelect;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.component.beans.ItemBean;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;

public class ComponentPropertyManager {
	
	private GenericComponent component;
	
	private FormPage formPage;

	public FormPage getFormPage() {
		return formPage;
	}

	public void setFormPage(FormPage formPage) {
		this.formPage = formPage;
	}

	public GenericComponent getComponent() {
		return component;
	}

	public void setComponent(GenericComponent component) {
		this.component = component;
	}
	
	public Object[] switchDataSource() {
		if(component.getDataSrc().equals(DataSourceList.externalDataSrc)) {
			component.setDataSrc(DataSourceList.localDataSrc);
			List<ItemBean> itemSet = component.getItems();
			itemSet.clear();
			component.setItems(itemSet);
		} else {
			component.setDataSrc(DataSourceList.externalDataSrc);
			List<ItemBean> itemSet = component.getItems();
			itemSet.clear();
			component.setItems(itemSet);
		}
		Document properties = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(component), true);
		Document comp = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component.getComponent()), true);
		Object[] result = {component.getComponent().getId(), comp, properties};
		return result;
	}
	
	public Document selectComponent(String id, String type) {
		if(type == null || id == null) {
			return null;
		}
		
		Document componentPropertiesDOM = null;
		Page page = formPage.getPage();
		if(page != null) {
			if(type.equals(FormComponent.COMPONENT_TYPE)) {
				Component comp = page.getComponent(id);
				if(comp instanceof ComponentMultiUpload) {
					component = new FormMultiUploadComponent((ComponentMultiUpload) comp);
				} else if(comp instanceof ComponentPlain) {
					component = new FormPlainComponent((ComponentPlain) comp);
				} else if(comp instanceof ComponentSelect) {
					component = new FormSelectComponent((ComponentSelect) comp);
				} else if(comp instanceof Component) {
					component = new FormComponent(comp);
				}
			} else if(type.equals(FormComponent.BUTTON_TYPE)) {
				ButtonArea area = page.getButtonArea();
				if(area != null) {
					Button button = (Button) area.getComponent(id);
					component = new FormButton(button);
				}
			}
			componentPropertiesDOM = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(component), true);
		}
		
		return componentPropertiesDOM;
	}
	
	public Document saveAutofill(boolean autofill) {
		if(autofill) {
			component.setAutofillKey("example");
		} else {
			component.setAutofillKey(CoreConstants.EMPTY);
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(component), true);
	}
	
	public Object[] removeSelectOption(int index) {
		removeItem(index);
		Document doc = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component.getComponent()), true);
		Object[] result = {component.getComponent().getId(), doc};
		return result;
	}
	
	public void removeItem(int index) {
		List<ItemBean> itemSet = component.getItems();
		if(index < itemSet.size()) {
			itemSet.remove(index);
		}
		component.setItems(itemSet);
	}
	
	public Object[] saveSelectOptionLabel(int index, String value) {
		int size = component.getItems().size();
		List<ItemBean> itemSet = component.getItems();
		if(index >= size) {
			ItemBean newItem = new ItemBean();
			newItem.setLabel(value);
			newItem.setValue(value);
			itemSet.add(newItem);
		} else {
			itemSet.get(index).setLabel(value);
			itemSet.get(index).setValue(value);
		}
		component.setItems(itemSet);
		Document doc = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component.getComponent()), true);
		Object[] result = {component.getComponent().getId(), doc};
		return result;
	}
	
	public Object[] saveSelectOptionValue(int index, String value) {
		List<ItemBean> itemSet = component.getItems();
		if(index >= itemSet.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setValue(value);
			itemSet.add(newItem);
		} else {
			itemSet.get(index).setValue(value);
		}
		component.setItems(itemSet);
		Document doc = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component.getComponent()), true);
		Object[] result = {component.getComponent().getId(), doc};
		return result;
	}
	
	public Object[] saveComponentProperty(String componentId, String propertyName, String propertyValue) {
		if(propertyName == null || propertyValue == null | componentId == null) {
			return null;
		}
		
		if(propertyName.equals("buttonLabel")) {
			component.setLabel(propertyValue);
			Object[] result = {componentId, propertyValue};
			return result;
		} else {
			if(propertyName.equals("compLabel")) {
				component.setLabel(propertyValue);
			} else if(propertyName.equals("compError")) {
				component.setErrorMessage(propertyValue);
			} else if(propertyName.equals("compHelp")) {
				component.setHelpMessage(propertyValue);
			} else if(propertyName.equals("compRequired")) {
				component.setRequired(Boolean.parseBoolean(propertyValue));
			} else if(propertyName.equals("plainText")) {
				component.setPlainText(propertyValue);
			} else if(propertyName.equals("plainLabel")) {
				component.setLabel(propertyValue);
			} else if(propertyName.equals("compAddButton")) {
				component.setAddButtonLabel(propertyValue);
			} else if(propertyName.equals("compAuto")) {
				component.setAutofillKey(propertyValue);
			} else if(propertyName.equals("compRemoveButton")) {
				component.setRemoveButtonLabel(propertyValue);
			} else if(propertyName.equals("externalSrc")) {
				if("".equals(propertyValue)) {
					propertyValue = null;
				}
				component.setExternalSrc(propertyValue);
			}
			Document doc = BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(component.getComponent()), true);
			Object[] result = {componentId, doc};
			return result;
		}
	}

}
