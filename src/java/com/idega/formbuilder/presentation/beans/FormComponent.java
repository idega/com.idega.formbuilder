package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.ComponentSelect;
import com.idega.documentmanager.business.form.ConstButtonType;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PropertiesComponent;
import com.idega.documentmanager.business.form.PropertiesSelect;
import com.idega.documentmanager.business.form.beans.ILocalizedItemset;
import com.idega.documentmanager.business.form.beans.ItemBean;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.converters.FormButtonInfo;
import com.idega.formbuilder.presentation.converters.FormComponentInfo;
import com.idega.webface.WFUtil;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private PropertiesComponent properties;
	private PropertiesSelect propertiesSelect;
	
	private Component component;
	private ComponentSelect selectComponent;
	
	private String id;
	
	private boolean required;
	private boolean autofill;
	private String label;
	private String errorMessage;
	private String helpMessage;
	private String autofillKey;
	
	private String emptyLabel;
	private String externalSrc;
	private List<ItemBean> items = new ArrayList<ItemBean>();
	
	private LocalizedStringBean labelStringBean;
	private LocalizedStringBean errorStringBean;
	private LocalizedStringBean helpStringBean;
	
	private LocalizedStringBean emptyLabelBean;
	private ILocalizedItemset itemset;
	
	private String dataSrc;
	
	public String getDataSrc() {
		if(propertiesSelect != null) {
			if(propertiesSelect.getDataSrcUsed() != null) {
				this.dataSrc = propertiesSelect.getDataSrcUsed().toString();
			} else {
				this.dataSrc = DataSourceList.localDataSrc;
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
		propertiesSelect.setDataSrcUsed(Integer.parseInt(dataSrc));
	
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
		this.emptyLabel = "";
		this.helpMessage = "";
		this.autofillKey = "";
		
		this.labelStringBean = null;
		this.errorStringBean = null;
		this.helpStringBean = null;
		this.emptyLabelBean = null;
		this.itemset = null;
		
		this.dataSrc = DataSourceList.localDataSrc;
		
		this.component = null;
		this.selectComponent = null;
		this.properties = null;
		this.propertiesSelect = null;
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
		
		this.emptyLabel = "";
		this.emptyLabelBean = null;
		
		this.itemset = null;
		
		this.properties = null;
		this.propertiesSelect = null;
		
		this.component = null;
		this.selectComponent = null;
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
	
	public void loadButton(String id) {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				component = area.getComponent(id);
				if(component != null) {
					this.id = id;
					properties = component.getProperties();
					
					labelStringBean = properties.getLabel();
					label = labelStringBean.getString(new Locale("en"));
					
					selectComponent = null;
					propertiesSelect = null;
				}
			}
		}
	}
	
	public FormButtonInfo getFormButtonInfo(String id) {
		loadButton(id);
		FormButtonInfo info = new FormButtonInfo();
		info.setId(id);
		info.setLabel(label);
		return info;
	}
	
	public FormComponentInfo getFormComponentInfo(String id) {
		//boolean temp = required;
		loadProperties(id);
		FormComponentInfo info = new FormComponentInfo();
		//temp = required;
		info.setLabel(label);
		info.setRequired(required);
		info.setErrorMessage(errorMessage);
		info.setHelpMessage(helpMessage);
		info.setAutofillKey(autofillKey);
		
		if(propertiesSelect != null) {
			info.setEmptyLabel(emptyLabel);
			if(dataSrc.equals(DataSourceList.externalDataSrc)) {
				info.setExternalSrc(externalSrc);
				info.setLocal(false);
			} else {
				info.setLocal(true);
				info.setItems(items);
			}
			info.setComplex(true);
		} else {
			info.setComplex(false);
		}
		return info;
	}
	
	public boolean switchDataSource() {
		if(dataSrc.equals(DataSourceList.externalDataSrc)) {
			setDataSrc(DataSourceList.localDataSrc);
			setExternalSrc("");
			return true;
		} else {
			setDataSrc(DataSourceList.externalDataSrc);
			clearItems();
			setItems(items);
			return false;
		}
	}
	
	public void clearItems() {
		items.clear();
	}
	
	public Element createComponent(String type) throws Exception {
		Element rootDivImported = null;
		FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
		Page page = formPage.getPage();
		if(page != null) {
			Component component = page.addComponent(type, null);
			if(component != null) {
				Element element = (Element) component.getHtmlRepresentation(new Locale("en")).cloneNode(true);
				String id = element.getAttribute("id");
				element.removeAttribute("id");
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				org.w3c.dom.Document domDocument = null;
		        try {
		          DocumentBuilder builder = factory.newDocumentBuilder();
		          domDocument = builder.newDocument();
		          Element delete = domDocument.createElement("IMG");
		          delete.setAttribute("src", "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
		          delete.setAttribute("class", "speedButton");
		          delete.setAttribute("onclick", "removeComponent(this)");
		          Element deleteIcon = (Element) element.getOwnerDocument().importNode(delete, true);
		          Element rootDiv = domDocument.createElement("DIV");
		          rootDiv.setAttribute("id", id);
		          rootDiv.setAttribute("class", "formElement");
		          rootDiv.setAttribute("onclick", "loadComponentInfo(this.id)");
		          rootDivImported = (Element) element.getOwnerDocument().importNode(rootDiv, true);
		          rootDivImported.appendChild(element);
		          rootDivImported.appendChild(deleteIcon);
		          ((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_ACTIVE);
		        } catch (ParserConfigurationException pce) {
		            pce.printStackTrace();
		        }
			}
		}
		return rootDivImported;
	}
	
	public FormButtonInfo addButton(String type) {
		FormButtonInfo result = new FormButtonInfo();
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = null;
				if(area != null) {
					button = area.addButton(new ConstButtonType(type), null);
				} else {
					area = page.createButtonArea(null);
					button = area.addButton(new ConstButtonType(type), null);
				}
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

	public void loadProperties(String id) {
		//boolean temp = required;
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		this.id = id;
		component = page.getComponent(id);
		if(component instanceof ComponentSelect) {
			selectComponent = (ComponentSelect) component;
			propertiesSelect = selectComponent.getProperties();
			
			component = null;
			properties = null;
			
			required = propertiesSelect.isRequired();
			//temp = required;
			
			labelStringBean = propertiesSelect.getLabel();
			label = labelStringBean.getString(new Locale("en"));
			
			errorStringBean = propertiesSelect.getErrorMsg();
			errorMessage = errorStringBean.getString(new Locale("en"));
			
			autofillKey = propertiesSelect.getAutofillKey();
			
//			helpStringBean = propertiesSelect.get
			
			emptyLabelBean = propertiesSelect.getEmptyElementLabel();
			emptyLabel = emptyLabelBean.getString(new Locale("en"));
			
			externalSrc = propertiesSelect.getExternalDataSrc();
			
			itemset = propertiesSelect.getItemset();
			items = itemset.getItems(new Locale("en"));
			
			if(items.size() == 0) {
				items.add(new ItemBean("", ""));
				items.add(new ItemBean("", ""));
				items.add(new ItemBean("", ""));
			}
		} else {
			selectComponent = null;
			propertiesSelect = null;
			properties = component.getProperties();
			
			required = properties.isRequired();
			//temp = required;
			
			labelStringBean = properties.getLabel();
			label = labelStringBean.getString(new Locale("en"));
			
			errorStringBean = properties.getErrorMsg();
			errorMessage = errorStringBean.getString(new Locale("en"));
			
			autofillKey = properties.getAutofillKey();
		}
		
		
	}
	
	public void saveComponentLabel(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyTitle");
		if(value != null) {
			setLabel(value);
		}
	}
	
	public void saveComponentRequired(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyRequired");
		if(value != null) {
			setRequired(true);
		} else {
			setRequired(false);
		}
	}
	
	public void saveComponentErrorMessage(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyErrorMessage");
		if(value != null) {
			setErrorMessage(value);
		}
	}
	
	public void saveComponentEmptyLabel(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyEmptyLabel");
		if(value != null) {
			setEmptyLabel(value);
		}
	}
	
	public void saveComponentExternalSource(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyExternal");
		if(value != null) {
			setExternalSrc(value);
		}
	}
	
	public void saveComponentDataSource(ActionEvent ae) throws Exception {
		if(dataSrc.equals(DataSourceList.externalDataSrc)) {
			setDataSrc(DataSourceList.localDataSrc);
		} else {
			setDataSrc(DataSourceList.externalDataSrc);
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
		errorStringBean.setString(new Locale("en"), errorMessage);
		if(properties != null) {
			properties.setErrorMsg(errorStringBean);
		} else if(propertiesSelect != null) {
			propertiesSelect.setErrorMsg(errorStringBean);
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		labelStringBean.setString(new Locale("en"), label);
		if(properties != null) {
			properties.setLabel(labelStringBean);
		} else if(propertiesSelect != null) {
			propertiesSelect.setLabel(labelStringBean);
		}
	}

	public boolean getRequired() {
		//boolean temp = required;
		return required;
	}

	public void setRequired(boolean required) {
		//boolean temp = required;
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

	public String getEmptyLabel() {
		return emptyLabel;
	}

	public void setEmptyLabel(String emptyLabel) {
		this.emptyLabel = emptyLabel;
		emptyLabelBean.setString(new Locale("en"), emptyLabel);
		if(propertiesSelect != null) {
			propertiesSelect.setEmptyElementLabel(emptyLabelBean);
		}
	}

	public LocalizedStringBean getEmptyLabelBean() {
		return emptyLabelBean;
	}

	public void setEmptyLabelBean(LocalizedStringBean emptyLabelBean) {
		this.emptyLabelBean = emptyLabelBean;
	}

	public String getExternalSrc() {
		return externalSrc;
	}

	public void setExternalSrc(String externalSrc) {
		this.externalSrc = externalSrc;
		if(propertiesSelect != null) {
			propertiesSelect.setExternalDataSrc(externalSrc);
		}
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
		itemset.setItems(new Locale("en"), items);
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

}
