package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.ComponentPlain;
import com.idega.documentmanager.business.form.ComponentSelect;
import com.idega.documentmanager.business.form.ConstButtonType;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PropertiesComponent;
import com.idega.documentmanager.business.form.PropertiesPlain;
import com.idega.documentmanager.business.form.PropertiesSelect;
import com.idega.documentmanager.business.form.beans.ILocalizedItemset;
import com.idega.documentmanager.business.form.beans.ItemBean;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.converters.FormButtonInfo;
import com.idega.formbuilder.presentation.converters.FormComponentInfo;
import com.idega.formbuilder.presentation.converters.PaletteComponentInfo;
import com.idega.webface.WFUtil;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private PropertiesComponent properties;
	private PropertiesSelect propertiesSelect;
	private PropertiesPlain propertiesPlain;
	
	private Component component;
	private ComponentSelect selectComponent;
	private ComponentPlain plainComponent;
	
	private String id;
	
	private boolean required;
	private boolean autofill;
	private boolean button;
	private String label;
	private String errorMessage;
	private String helpMessage;
	private String autofillKey;
	private String plainText;
	
	private String externalSrc;
	private List<ItemBean> items = new ArrayList<ItemBean>();
	
	private LocalizedStringBean labelStringBean;
	private LocalizedStringBean errorStringBean;
	private LocalizedStringBean helpStringBean;
	
	private ILocalizedItemset itemset;
	
	private String dataSrc;
	
	public Element saveComponentLabel(String value) {
		setLabel(value);
		return getComponentGUINode();
	}
	
	public FormButtonInfo saveButtonLabel(String value) {
		setLabel(value);
		return getFormButtonInfo(id);
	}
	
	public Element saveComponentExternalSrc(String value) {
		setExternalSrc(value);
		return getComponentGUINode();
	}
	
	public Element saveComponentRequired(boolean value) {
		setRequired(value);
		return getComponentGUINode();
	}
	
	public Element saveSelectOptionLabel(int index, String label) {
		saveLabel(index, label);
		return getComponentGUINode();
	}
	
	public Element saveSelectOptionValue(int index, String value) {
		saveValue(index, value);
		return getComponentGUINode();
	}
	
	public Element saveComponentErrorMessage(String value) {
		setErrorMessage(value);
		return getComponentGUINode();
	}
	
	public Element saveComponentHelpMessage(String value) {
		setHelpMessage(value);
		return getComponentGUINode();
	}
	
	public Element saveComponentPlainText(String value) {
		setPlainText(value);
		return getComponentGUINode();
	}
	
	public Element saveComponentAutofillKey(String value) {
		setAutofillKey(value);
		return getComponentGUINode();
	}
	
	public String getDataSrc() {
		if(propertiesSelect != null) {
			if(propertiesSelect.getDataSrcUsed() != null) {
				dataSrc = propertiesSelect.getDataSrcUsed().toString();
			} else {
				dataSrc = DataSourceList.localDataSrc;
			}
		}
		return dataSrc;
	}
	
	public boolean isSimple() {
		if(propertiesSelect != null) {
			return false;
		} else {
			return true;
		}
	}

	public void setDataSrc(String dataSrc) {
		this.dataSrc = dataSrc;
		if(propertiesSelect != null) {
			propertiesSelect.setDataSrcUsed(Integer.parseInt(dataSrc));
		}
	}

	public LocalizedStringBean getErrorStringBean() {
		return errorStringBean;
	}

	public void setErrorStringBean(LocalizedStringBean errorStringBean) {
		this.errorStringBean = errorStringBean;
	}

	public LocalizedStringBean getLabelStringBean() {
		return labelStringBean;
	}

	public void setLabelStringBean(LocalizedStringBean labelStringBean) {
		this.labelStringBean = labelStringBean;
	}

	public FormComponent() {
		this.id = "";
		
		this.label = "";
		this.errorMessage = "";
		this.required = false;
		this.helpMessage = "";
		this.autofillKey = "";
		this.plainText = "";
		
		this.labelStringBean = null;
		this.errorStringBean = null;
		this.helpStringBean = null;
		this.itemset = null;
		
		this.dataSrc = DataSourceList.localDataSrc;
		
		this.component = null;
		this.selectComponent = null;
		this.plainComponent = null;
		
		this.properties = null;
		this.propertiesSelect = null;
		this.propertiesPlain = null;
	}
	
	public void clearFormComponentInfo() {
		this.id = "";
		
		this.label = "";
		this.labelStringBean = null;
		
		this.errorMessage = "";
		this.errorStringBean = null;
		
		this.helpMessage = "";
		this.helpStringBean = null;
		
		this.required = false;
		
		this.autofillKey = "";
		
		this.plainText = "";
		
		this.itemset = null;
		
		this.properties = null;
		this.propertiesSelect = null;
		this.propertiesPlain = null;
		
		this.component = null;
		this.selectComponent = null;
		this.plainComponent = null;
	}
	
	public Element removeSelectOption(int index) {
		removeItem(index);
		return getComponentGUINode();
	}
	
	public void removeItem(int index) {
		if(index < items.size()) {
			items.remove(index);
			setItems(items);
		}
	}
	
	public void saveLabel(int index, String value) {
		int size = items.size();
		if(index >= size) {
			ItemBean newItem = new ItemBean();
			newItem.setLabel(value);
			newItem.setValue(value);
			items.add(newItem);
		} else {
			items.get(index).setLabel(value);
			items.get(index).setValue(value);
		}
		setItems(items);
	}
	
	public void saveValue(int index, String value) {
		if(index >= items.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setValue(value);
			items.add(newItem);
		} else {
			items.get(index).setValue(value);
		}
		setItems(items);
	}
	
	public Element getComponentGUINode() {
		Locale current = ((Workspace) WFUtil.getBeanInstance("workspace")).getLocale();
		Element element = null;
		try {
			if(component != null) {
				element = (Element) component.getHtmlRepresentation(current).cloneNode(true);
			} else if(selectComponent != null) {
				element = (Element) selectComponent.getHtmlRepresentation(current).cloneNode(true);
			} else if(plainComponent != null) {
				element = (Element) plainComponent.getHtmlRepresentation(current).cloneNode(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return element;
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
					properties = component.getProperties();
					
					labelStringBean = properties.getLabel();
					label = labelStringBean.getString(new Locale("en"));
					
					selectComponent = null;
					propertiesSelect = null;
					
					plainComponent = null;
					propertiesPlain = null;
				}
			}
		}
	}
	
	public FormButtonInfo getFormButtonInfo(String id) {
		loadButton(id);
		FormButtonInfo info = new FormButtonInfo(id, label);
		return info;
	}
	
	public FormComponentInfo getFormComponentInfo(String id) {
		loadProperties(id);
		FormComponentInfo info = new FormComponentInfo();
		info.setId(id);
		if(propertiesPlain != null) {
			info.setPlainText(plainText);
			info.setPlain(true);
		} else {
			info.setLabel(label);
			info.setRequired(required);
			info.setErrorMessage(errorMessage);
			info.setHelpMessage(helpMessage);
			info.setAutofillKey(autofillKey);
			
			if(propertiesSelect != null) {
				if(dataSrc.equals(DataSourceList.externalDataSrc)) {
					info.setExternalSrc(externalSrc);
					info.setLocal(false);
					items.clear();
					info.setItems(items);
				} else {
					info.setLocal(true);
					info.setItems(items);
					info.setExternalSrc("");
				}
				info.setComplex(true);
			} else {
				info.setComplex(false);
			}
			info.setPlain(false);
		}
		return info;
	}
	
	public boolean switchDataSource() {
		if(dataSrc.equals(DataSourceList.externalDataSrc)) {
			setDataSrc(DataSourceList.localDataSrc);
			return true;
		} else {
			setDataSrc(DataSourceList.externalDataSrc);
			items.clear();
			setItems(items);
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
	
	public FormButtonInfo addButton(String type) {
		FormButtonInfo result = new FormButtonInfo();
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			Button button = null;
			if(area != null) {
				button = area.addButton(new ConstButtonType(type), null);
				result.setType(type);
				result.setId(button.getId());
				result.setLabel(button.getProperties().getLabel().getString(new Locale("en")));
			} else {
				area = page.createButtonArea(null);
				button = area.addButton(new ConstButtonType(type), null);
				result.setType(type);
				result.setId(button.getId());
				result.setLabel(button.getProperties().getLabel().getString(new Locale("en")));
			}
		}
		return result;
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

	public void loadProperties(String id) {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		this.id = id;
		button = false;
		component = page.getComponent(id);
		if(component instanceof ComponentPlain) {
			plainComponent = (ComponentPlain) component;
			propertiesPlain = plainComponent.getProperties();
			
			selectComponent = null;
			propertiesSelect = null;
			
			component = null;
			properties = null;
			
			plainText = propertiesPlain.getText();
		} else if(component instanceof ComponentSelect) {
			selectComponent = (ComponentSelect) component;
			propertiesSelect = selectComponent.getProperties();
			
			component = null;
			properties = null;
			
			plainComponent = null;
			propertiesPlain = null;
			
			plainText = "";
			
			required = propertiesSelect.isRequired();
			labelStringBean = propertiesSelect.getLabel();
			label = labelStringBean.getString(new Locale("en"));
			
			errorStringBean = propertiesSelect.getErrorMsg();
			errorMessage = errorStringBean.getString(new Locale("en"));
			
			autofillKey = propertiesSelect.getAutofillKey();
			
			helpStringBean = propertiesSelect.getHelpText();
			helpMessage = helpStringBean.getString(new Locale("en"));
			
			if(propertiesSelect.getDataSrcUsed() != null) {
				dataSrc = propertiesSelect.getDataSrcUsed().toString();
			} else {
				propertiesSelect.setDataSrcUsed(PropertiesSelect.LOCAL_DATA_SRC);
				dataSrc = DataSourceList.localDataSrc;
			}
			
			externalSrc = propertiesSelect.getExternalDataSrc();
			
			itemset = propertiesSelect.getItemset();
			items = itemset.getItems(new Locale("en"));
			if(items.size() == 0) {
				items.add(new ItemBean("", ""));
			}
		} else {
			selectComponent = null;
			propertiesSelect = null;
			plainComponent = null;
			propertiesPlain = null;
			plainText = "";
			properties = component.getProperties();
			
			required = properties.isRequired();

			labelStringBean = properties.getLabel();
			label = labelStringBean.getString(new Locale("en"));
			
			errorStringBean = properties.getErrorMsg();
			errorMessage = errorStringBean.getString(new Locale("en"));
			
			helpStringBean = properties.getHelpText();
			helpMessage = helpStringBean.getString(new Locale("en"));
			
			autofillKey = properties.getAutofillKey();
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		if(errorStringBean != null) {
			errorStringBean.setString(new Locale("en"), errorMessage);
			if(properties != null) {
				properties.setErrorMsg(errorStringBean);
			} else if(propertiesSelect != null) {
				propertiesSelect.setErrorMsg(errorStringBean);
			}
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		if(labelStringBean != null) {
			labelStringBean.setString(new Locale("en"), label);
			if(properties != null) {
				properties.setLabel(labelStringBean);
			} else if(propertiesSelect != null) {
				propertiesSelect.setLabel(labelStringBean);
			}
		}
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
		if(properties != null) {
			properties.setRequired(required);
		} else if(propertiesSelect != null) {
			propertiesSelect.setRequired(required);
		}
	}

	public PropertiesComponent getProperties() {
		return properties;
	}

	public String getExternalSrc() {
		return externalSrc;
	}

	public void setExternalSrc(String externalSrc) {
		this.externalSrc = externalSrc;
		if(propertiesSelect != null) {
			if(!externalSrc.equals("")) {
				propertiesSelect.setExternalDataSrc(externalSrc);
			}
		}
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
		if(itemset != null) {
			itemset.setItems(new Locale("en"), items);
		}
	}

	public ILocalizedItemset getItemset() {
		return itemset;
	}

	public void setItemset(ILocalizedItemset itemset) {
		this.itemset = itemset;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public void setProperties(PropertiesComponent properties) {
		this.properties = properties;
	}

	public PropertiesSelect getPropertiesSelect() {
		return propertiesSelect;
	}

	public void setPropertiesSelect(PropertiesSelect propertiesSelect) {
		this.propertiesSelect = propertiesSelect;
	}

	public ComponentSelect getSelectComponent() {
		return selectComponent;
	}

	public void setSelectComponent(ComponentSelect selectComponent) {
		this.selectComponent = selectComponent;
	}

	public String getHelpMessage() {
		return helpMessage;
	}

	public void setHelpMessage(String helpMessage) {
		this.helpMessage = helpMessage;
		if(helpStringBean != null) {
			helpStringBean.setString(new Locale("en"), helpMessage);
			if(properties != null) {
				properties.setHelpText(helpStringBean);
			} else if(propertiesSelect != null) {
				propertiesSelect.setHelpText(helpStringBean);
			}
		}
	}

	public LocalizedStringBean getHelpStringBean() {
		return helpStringBean;
	}

	public void setHelpStringBean(LocalizedStringBean helpStringBean) {
		this.helpStringBean = helpStringBean;
	}

	public String getAutofillKey() {
		return autofillKey;
	}

	public void setAutofillKey(String autofillKey) {
		this.autofillKey = autofillKey;
		if(properties != null) {
			properties.setAutofillKey(autofillKey);
		} else if(propertiesSelect != null) {
			propertiesSelect.setAutofillKey(autofillKey);
		}
	}

	public boolean isAutofill() {
		return autofill;
	}

	public void setAutofill(boolean autofill) {
		this.autofill = autofill;
	}

	public String getPlainText() {
		return plainText;
	}

	public void setPlainText(String plainText) {
		this.plainText = plainText;
		if(propertiesPlain != null) {
			propertiesPlain.setText(plainText);
		}
	}

	public ComponentPlain getPlainComponent() {
		return plainComponent;
	}

	public void setPlainComponent(ComponentPlain plainComponent) {
		this.plainComponent = plainComponent;
	}

	public PropertiesPlain getPropertiesPlain() {
		return propertiesPlain;
	}

	public void setPropertiesPlain(PropertiesPlain propertiesPlain) {
		this.propertiesPlain = propertiesPlain;
	}

	public boolean isButton() {
		return button;
	}

	public void setButton(boolean button) {
		this.button = button;
	}

}
