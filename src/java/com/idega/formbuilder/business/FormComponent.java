package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IComponentPropertiesSelect;
import com.idega.formbuilder.business.form.beans.ILocalizedItemset;
import com.idega.formbuilder.business.form.beans.ItemBean;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.webface.WFUtil;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -7539955909568793992L;
	
	private IFormManager formManagerInstance;
	
	private String id;
	
	private Boolean required;
	private String label;
	private String errorMsg;
	
	private String externalSrc;
	private String emptyLabel;
	private List<ItemBean> items = new ArrayList<ItemBean>();
	
	private LocalizedStringBean labelStringBean;
	private LocalizedStringBean errorStringBean;
	private LocalizedStringBean emptyLabelBean;
	private ILocalizedItemset itemset;
	
	private IComponentProperties properties;
	
	public FormComponent() {
		
		this.required = new Boolean(false);
		this.label = "";
		this.errorMsg = "";
		
		this.externalSrc = "";
		this.emptyLabel = "";
		
		this.labelStringBean = null;
		this.errorStringBean = null;
		this.emptyLabelBean = null;
		this.itemset = null;
	}
	
	public void loadProperties(String id, IFormManager formManagerInstance) {
		this.formManagerInstance = formManagerInstance;
		this.id = id;
		this.properties = formManagerInstance.getComponentProperties(id);
		
		this.required = properties.isRequired();
		this.labelStringBean = properties.getLabel();
		this.errorStringBean = properties.getErrorMsg();
		
		if(properties instanceof IComponentPropertiesSelect) {
			this.externalSrc = ((IComponentPropertiesSelect) properties).getExternalDataSrc();
			this.emptyLabelBean = ((IComponentPropertiesSelect) properties).getEmptyElementLabel();
			this.itemset = ((IComponentPropertiesSelect) properties).getItemset();
		}
		
		if(items == null) {
			System.out.println("ITEMS IS NULL MOTERFUKER");
		} else {
			items.clear();
			items.add(new ItemBean("tiger", "Tiger"));
			items.add(new ItemBean("dolphin", "Dolphin"));
			items.add(new ItemBean("mustang", "Mustang"));
			items.add(new ItemBean("panther", "Panther"));
			items.add(new ItemBean("leopard", "Leopard"));
		}
	}
	
	public void saveProperties(ActionEvent ae) throws Exception {
		System.out.println("SAVING COMPONENT PROPERTIES");
		//this.printParameterMap();
		String trequired = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyRequired");
		if(trequired != null && !trequired.equals("")) {
			this.setRequired(new Boolean(trequired));
		}
		String tlabel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyTitle");
		if(tlabel != null) {
			this.setLabel(tlabel);
		}
		String terror = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyErrorMessage");
		if(terror != null) {
			this.setErrorMsg(terror);
		}
		if(properties != null) {
			properties.setRequired(required);
			properties.setErrorMsg(errorStringBean);
			properties.setLabel(labelStringBean);
			if(properties instanceof IComponentPropertiesSelect) {
				String texternal = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyExternal");
				if(texternal != null) {
					System.out.println("DATA SOURCE: " + texternal);
					this.setExternalSrc(texternal);
				}
				String temptylabel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyEmptyLabel");
				if(temptylabel != null) {
					this.setEmptyLabel(temptylabel);
				}
				IComponentPropertiesSelect propertiesSelect = (IComponentPropertiesSelect) properties;
				
				if(propertiesSelect != null) {
					propertiesSelect.setEmptyElementLabel(emptyLabelBean);
					if(propertiesSelect.getDataSrcUsed() != null) {
						if(((DataSourceList) WFUtil.getBeanInstance("dataSources")).getSelectedDataSource() == new Integer(IComponentPropertiesSelect.EXTERNAL_DATA_SRC).toString()) {
							propertiesSelect.setExternalDataSrc(texternal);
						} else {
							
						}
					} else {
						System.out.println("DATA SOURCE UNKNOWN");
					}
					this.setItems(this.decodeSelectItems());
				}
			}
		}
		formManagerInstance.updateFormComponent(id);
	}
	
	private List<ItemBean> decodeSelectItems() {
		List<ItemBean> result = new ArrayList<ItemBean>();
		Set keys = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			String currentParam = (String) it.next();
			System.out.println(currentParam);
			if(currentParam.contains("labelF_")) {
				String index = currentParam.substring(currentParam.length()-1);
				ItemBean item = new ItemBean();
				item.setLabel((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(currentParam));
				item.setValue((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspace1:valueF_" + index));
				System.out.println(index);
				result.add(item);
			}
		}
		return result;
	}
	
	private void printParameterMap() {
		Set keys = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().keySet();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			System.out.println((String) it.next());
		}
		
	}
	
	public IComponentProperties getProperties() {
		return properties;
	}

	public void setProperties(IComponentProperties properties) {
		this.properties = properties;
	}

	public Boolean getRequired() {
		return new Boolean(properties.isRequired());
	}

	public String getErrorMsg() {
		this.errorMsg = errorStringBean.getString(new Locale("en"));
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		this.errorStringBean.setString(new Locale("en"), errorMsg);
	}
	
	public Boolean isRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
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

	public String getLabel() {
		label = labelStringBean.getString(new Locale("en"));
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		labelStringBean.setString(new Locale("en"), this.label);
	}

	public String getExternalSrc() {
		return externalSrc;
	}

	public void setExternalSrc(String externalSrc) {
		this.externalSrc = externalSrc;
	}

	public String getEmptyLabel() {
		emptyLabel = emptyLabelBean.getString(new Locale("en"));
		return emptyLabel;
	}

	public void setEmptyLabel(String emptyLabel) {
		this.emptyLabel = emptyLabel;
		emptyLabelBean.setString(new Locale("en"), emptyLabel);
	}

	public LocalizedStringBean getEmptyLabelBean() {
		return emptyLabelBean;
	}

	public void setEmptyLabelBean(LocalizedStringBean emptyLabelBean) {
		this.emptyLabelBean = emptyLabelBean;
	}

	public ILocalizedItemset getItemset() {
		return itemset;
	}

	public void setItemset(ILocalizedItemset itemset) {
		this.itemset = itemset;
	}

	public List<ItemBean> getItems() {
		//items = itemset.getItems(new Locale("en"));
		/*System.out.println("GETTING ITEMS");
		items.clear();
		items.add(new ItemBean("tiger", "Tiger"));
		items.add(new ItemBean("dolphin", "Dolphin"));
		items.add(new ItemBean("mustang", "Mustang"));
		items.add(new ItemBean("panther", "Panther"));
		items.add(new ItemBean("leopard", "Leopard"));*/
		items = itemset.getItems(new Locale("en"));
		return items;
	}

	public void setItems(List<ItemBean> items) {
		System.out.println("SETTING ITEMS");
		/*items.clear();
		items.add(new ItemBean("tiger", "Tiger"));
		items.add(new ItemBean("dolphin", "Dolphin"));
		items.add(new ItemBean("mustang", "Mustang"));
		items.add(new ItemBean("panther", "Panther"));
		items.add(new ItemBean("leopard", "Leopard"));*/
		this.items = items;
		itemset.setItems(new Locale("en"), items);
	}

}
