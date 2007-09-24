package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jdom.Document;
import org.w3c.dom.Node;

import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.ComponentPlain;
import com.idega.documentmanager.business.form.ComponentSelect;
import com.idega.documentmanager.business.form.ConstButtonType;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.beans.ItemBean;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.presentation.components.FBComponentPropertiesPanel;
import com.idega.formbuilder.presentation.components.FBFormComponent;
import com.idega.formbuilder.presentation.converters.PaletteComponentInfo;
import com.idega.formbuilder.util.FBConstants;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private Component component;
	private ComponentSelect selectComponent;
	private ComponentPlain plainComponent;
	private String id;
	
	private boolean autofill;
	private boolean button;
	
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
	
	public void initializeBeanInstace(String id) {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
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
	
	public Document saveComponentLabel(String value) {
		setLabel(value);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormComponent(id), true);
	}
	
	public Document saveButtonLabel(String value) {
		setLabel(value);
		return getFormButtonInfo(id);
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
	
	public boolean isSimple() {
		if(selectComponent != null) {
			return false;
		} else {
			return true;
		}
	}

	public void setDataSrc(String dataSrc) {
//		this.dataSrc = dataSrc;
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
//			setItems(items);
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
	
	public void loadButton(String id) {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				component = area.getComponent(id);
				if(component != null) {
					this.id = id;
					button = true;
					selectComponent = null;
					plainComponent = null;
				}
			}
		}
	}
	
	public Document getFormButtonInfo(String id) {
		this.id = id;
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentPropertiesPanel(id, FBConstants.BUTTON_TYPE), true);
	}
	
	public Document getFormComponentInfo(String id) {
		initializeBeanInstace(id);
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBComponentPropertiesPanel(id, FBConstants.COMPONENT_TYPE), true);
	}
	
	public boolean switchDataSource() {
		if(getDataSrc().equals(DataSourceList.externalDataSrc)) {
			setDataSrc(DataSourceList.localDataSrc);
			return true;
		} else {
			setDataSrc(DataSourceList.externalDataSrc);
			getItems().clear();
//			setItems(items);
			return false;
		}
	}
	
	public Node addComponent(PaletteComponentInfo info) throws Exception {
		FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
		Page page = formPage.getPage();
		if(page != null) {
			String before = null;
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				before = area.getId();
			}
			Component component = page.addComponent(info.getType(), before);
			if(component != null) {
				if(info.getAutofill() != null) {
					if(component.getProperties() != null)
						component.getProperties().setAutofillKey(info.getAutofill());
				}
				Node element = component.getHtmlRepresentation(new Locale("en")).cloneNode(true);
				return element;
			}
		}
		return null;
	}
	
	public String moveComponent(String id, int before) throws Exception {
		if(before == -1) {
			return "append";
		} else {
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
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
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			Button button = null;
			if(area != null) {
				button = area.addButton(new ConstButtonType(type), null);
			} else {
				area = page.createButtonArea(null);
				button = area.addButton(new ConstButtonType(type), null);
			}
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "loadButtonInfo", "removeButton"), true);
		}
		return null;
	}
	
	public String removeComponent(String id) {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			Component component = page.getComponent(id);
			if(component != null) {
				component.remove();
			}
		}
		return id;
	}
	
	public String removeButton(String id) {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
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
		if(component != null) {
			component.getProperties().setAutofillKey(autofillKey);
		} else if(selectComponent != null) {
			selectComponent.getProperties().setAutofillKey(autofillKey);
		}
	}

	public boolean isAutofill() {
		return autofill;
	}

	public void setAutofill(boolean autofill) {
		this.autofill = autofill;
	}

	public String getPlainText() {
		if(plainComponent != null) {
			return plainComponent.getProperties().getText();
		}
		return null;
	}

	public void setPlainText(String plainText) {
		if(plainComponent != null) {
			plainComponent.getProperties().setText(plainText);
		}
	}

	public ComponentPlain getPlainComponent() {
		return plainComponent;
	}

	public void setPlainComponent(ComponentPlain plainComponent) {
		this.plainComponent = plainComponent;
	}

	public boolean isButton() {
		return button;
	}

	public void setButton(boolean button) {
		this.button = button;
	}

}