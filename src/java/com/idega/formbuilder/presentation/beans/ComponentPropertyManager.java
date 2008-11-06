package com.idega.formbuilder.presentation.beans;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.formbuilder.presentation.components.FBComponentProperties;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.xformsmanager.business.component.Button;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.ComponentMultiUpload;
import com.idega.xformsmanager.business.component.ComponentPlain;
import com.idega.xformsmanager.business.component.ComponentSelect;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.component.beans.ItemBean;

public class ComponentPropertyManager {

	private static final String BUTTON_LABEL_PROP = "buttonLabel";
	private static final String COMP_LABEL_PROP = "compLabel";
//	private static final String COMP_ERROR_PROP = "compError";
	private static final String COMP_HELP_PROP = "compHelp";
	private static final String COMP_REQ_PROP = "compRequired";
//	private static final String COMP_VALIDATATION_PROP = "compValidation";
	private static final String PLAIN_LABEL_PROP = "plainLabel";
	private static final String PLAIN_TEXT_PROP = "plainText";
	private static final String COMP_AUTO_PROP = "compAuto";
	private static final String COMP_ADD_BUTTON_PROP = "compAddButton";
	private static final String COMP_REMOVE_BUTTON_PROP = "compRemoveButton";
	private static final String COMP_EXT_SRC_PROP = "externalSrc";
	private static final String COMP_UPL_DESC_PROP = "uploadDesc";

	private GenericComponent component;

	private FormPage formPage;

	public FormPage getFormPage() {
		return formPage;
	}

	public String getSelectedComponentId() {
		if (component != null) {
			return component.getComponent().getId();
		} else {
			return CoreConstants.EMPTY;
		}
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
		if (component.getDataSrc().equals(DataSourceList.externalDataSrc)) {
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
		Object[] result = { component.getComponent().getId(), getFormComponent(component), getPropertiesPanel(component) };
		return result;
	}
	
	public Document selectComponent(String id, String type) {
		if (type == null || id == null) {
			return null;
		}

		Document componentPropertiesDOM = null;
		Page page = formPage.getPage();
		if (page != null) {
			if (type.equals(FormComponent.COMPONENT_TYPE)) {
				Component comp = page.getComponent(id);
				if (comp instanceof ComponentMultiUpload) {
					component = new FormMultiUploadDescriptionComponent((ComponentMultiUpload) comp);
				} else if (comp instanceof ComponentPlain) {
					component = new FormPlainComponent((ComponentPlain) comp);
				} else if (comp instanceof ComponentSelect) {
					component = new FormSelectComponent((ComponentSelect) comp);
				} else {
					component = new FormComponent(comp);
				}
			} else if (type.equals(FormComponent.BUTTON_TYPE)) {
				ButtonArea area = page.getButtonArea();
				if (area != null) {
					Button button = (Button) area.getComponent(id);
					component = new FormButton(button);
				}
			}
			componentPropertiesDOM = getPropertiesPanel(component);
		}

		return componentPropertiesDOM;
	}

	public Document saveAutofill(boolean autofill) {
		if (autofill) {
			component.setAutofillKey("example");
		} else {
			component.setAutofillKey(CoreConstants.EMPTY);
		}
		return getPropertiesPanel(component);
	}

	public Object[] removeSelectOption(int index) {
		removeItem(index);
		Object[] result = { component.getComponent().getId(), getFormComponent(component) };
		return result;
	}

	public void removeItem(int index) {
		List<ItemBean> itemSet = component.getItems();
		if (index < itemSet.size()) {
			itemSet.remove(index);
		}
		component.setItems(itemSet);
	}

	public Object[] saveSelectOptionLabel(int index, String value) {
		int size = component.getItems().size();
		List<ItemBean> itemSet = component.getItems();
		if (index >= size) {
			ItemBean newItem = new ItemBean();
			newItem.setLabel(value);
			newItem.setValue(value);
			itemSet.add(newItem);
		} else {
			itemSet.get(index).setLabel(value);
			itemSet.get(index).setValue(value);
		}
		component.setItems(itemSet);
		Object[] result = { component.getComponent().getId(), getFormComponent(component) };
		return result;
	}

	public Object[] saveSelectOptionValue(int index, String value) {
		List<ItemBean> itemSet = component.getItems();
		if (index >= itemSet.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setValue(value);
			itemSet.add(newItem);
		} else {
			itemSet.get(index).setValue(value);
		}
		component.setItems(itemSet);
		Object[] result = { component.getComponent().getId(), getFormComponent(component) };
		return result;
	}

	public Object[] saveComponentErrorMessage(String errorTypeStr, String errorMessage) {

		ErrorType errType = ErrorType.getByStringRepresentation(errorTypeStr);

		if (errType != null) {
			component.setErrorMessage(errType, errorMessage);
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,"Tried to set error message, but no error type resolved by error type string provided="+ errorTypeStr);
		}

		return getResponse(component, false);
	}

	// TODO: remove componentId param
	public Object[] saveComponentProperty(String componentId, String propertyName, String propertyValue) {
		if (propertyName == null || propertyValue == null) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING,
									"Tried to save component property, but either property name or value not set. PropertyName="
									+ propertyName
									+ ", propertyValue="
									+ propertyValue
									+ ", component id = "
									+ component.getId());
			return null;
		}
		
		boolean reloadProperties = false;

		if (propertyName.equals(BUTTON_LABEL_PROP)) {
			component.setLabel(propertyValue);
			Object[] result = { component.getId(), propertyValue };
			return result;
		} else {
			if (propertyName.equals(COMP_LABEL_PROP)) {
				component.setLabel(propertyValue);
			} else if (propertyName.equals(COMP_HELP_PROP)) {
				component.setHelpMessage(propertyValue);
			} else if (propertyName.equals(COMP_REQ_PROP)) {
				component.setRequired(Boolean.parseBoolean(propertyValue));
				reloadProperties = true;
//			} else if (propertyName.equals(COMP_VALIDATATION_PROP)) {
//				component.setValidationText(propertyValue);
			} else if (propertyName.equals(PLAIN_TEXT_PROP)) {
				component.setPlainText(propertyValue);
			} else if (propertyName.equals(PLAIN_LABEL_PROP)) {
				component.setLabel(propertyValue);
			} else if (propertyName.equals(COMP_ADD_BUTTON_PROP)) {
				component.setAddButtonLabel(propertyValue);
			} else if (propertyName.equals(COMP_AUTO_PROP)) {
				component.setAutofillKey(propertyValue);
			} else if (propertyName.equals(COMP_REMOVE_BUTTON_PROP)) {
				component.setRemoveButtonLabel(propertyValue);
			} else if (propertyName.equals(COMP_EXT_SRC_PROP)) {
				if (CoreConstants.EMPTY.equals(propertyValue)) {
					propertyValue = null;
				}
				component.setExternalSrc(propertyValue);
			} else if (propertyName.equals(COMP_UPL_DESC_PROP)) {
				component.setUploadDescription(propertyValue);
			}
			
			return getResponse(component, reloadProperties);
		}
	}

	private Object[] getResponse(GenericComponent component, boolean reloadProperties) {
		Document propertiesPanel = null;
		if(reloadProperties) {
			propertiesPanel = getPropertiesPanel(component);
		}
		return new Object[] { component.getId(), getFormComponent(component), propertiesPanel };
	}
	
	private Document getPropertiesPanel(GenericComponent component) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentProperties(component),true);
	}
	
	private Document getFormComponent(GenericComponent component) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(),new FBFormComponent(component.getComponent()), true);
	}
}