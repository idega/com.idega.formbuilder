package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
	
	private int optionsCount;
	private boolean addEmptyOption;
	
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
		
		this.optionsCount = 3;
		this.addEmptyOption= false;
	}
	
	public void loadProperties(String id, IFormManager formManagerInstance) {
		this.formManagerInstance = formManagerInstance;
		this.id = id;
		this.properties = formManagerInstance.getComponentProperties(id);
		
		this.required = properties.isRequired();
		this.labelStringBean = properties.getLabel();
		this.errorStringBean = properties.getErrorMsg();
		
		if(properties instanceof IComponentPropertiesSelect) {
			System.out.println("LOADING PROPERTIES OF SELECT COMPONENT");
			this.externalSrc = ((IComponentPropertiesSelect) properties).getExternalDataSrc();
			this.emptyLabelBean = ((IComponentPropertiesSelect) properties).getEmptyElementLabel();
			this.itemset = ((IComponentPropertiesSelect) properties).getItemset();
		}
	}
	
	public void removeOption(ActionEvent ae) {
		System.out.println("REMOVE OPTION: " + ae.getComponent().getId());
	}
	
	public void addEmptyOption(ActionEvent ae) {
		System.out.println("ADDING NEW OPTION");
		this.addEmptyOption = true;
	}
	
	public void saveProperties(ActionEvent ae) throws Exception {
		System.out.println("SAVING COMPONENT PROPERTIES");
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
			System.out.println("Setting error message");
			this.setErrorMsg(terror);
		}
		if(properties != null) {
			properties.setRequired(required);
			if(errorStringBean == null) {
				System.out.println("ERROR STRING BEAN is null");
			} else {
				System.out.println("ERROR STRING BEAN: " + this.errorStringBean.getString(new Locale("en")));
				properties.setErrorMsg(errorStringBean);
			}
			
			properties.setLabel(labelStringBean);
			if(properties instanceof IComponentPropertiesSelect) {
				
				String temptylabel = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyEmptyLabel");
				if(temptylabel != null) {
					this.setEmptyLabel(temptylabel);
				}
				IComponentPropertiesSelect propertiesSelect = (IComponentPropertiesSelect) properties;
				
				if(propertiesSelect != null) {
					System.out.println("SAVING SELECT COMPONENT PROPERTIES");
					propertiesSelect.setEmptyElementLabel(emptyLabelBean);
					//this.setItems(this.decodeSelectItems());
					String dataSrc = ((DataSourceList) WFUtil.getBeanInstance("dataSources")).getSelectedDataSource();
					System.out.println("SELECTED DATA SOURCE: " + dataSrc);
					if(dataSrc.equals(new Integer(IComponentPropertiesSelect.EXTERNAL_DATA_SRC).toString())) {
						String texternal = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("workspaceform1:propertyExternal");
						if(texternal != null) {
							System.out.println("DATA SOURCE: " + texternal);
							this.setExternalSrc(texternal);
						}
						
						propertiesSelect.setExternalDataSrc(texternal);
					} else {
						
						this.setItems(this.decodeSelectItems());
						System.out.println("SAVED ITEMSET SIZE: " + ((IComponentPropertiesSelect) formManagerInstance.getComponentProperties(id)).getItemset().getItems(new Locale("en")).size());
					}
					
					/*
					if(propertiesSelect.getDataSrcUsed() != null) {
						
					} else {
						System.out.println("DATA SOURCE UNKNOWN");
					}
					*/
					
				}
			}
		}
		
		formManagerInstance.updateFormComponent(id);
	}
	
	private List<ItemBean> decodeSelectItems() {
		printParameterMap();
		List<ItemBean> result = new ArrayList<ItemBean>();
		Set keys = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().keySet();
		Map parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		Iterator it = keys.iterator();
		while(it.hasNext()) {
			String currentParam = (String) it.next();
			//System.out.println(currentParam);
			if(currentParam.contains("labelF_")) {
				if(currentParam.contains("workspaceform1:")) {
					String label = (String) parameters.get(currentParam);
					if(!label.equals("")) {
						ItemBean item = new ItemBean();
						String index = currentParam.substring(currentParam.length()-1);
						item.setLabel(label);
						String value = (String) parameters.get("workspaceform1:valueF_" + index);
						if(!value.equals("")) {
							item.setValue(value);
						} else {
							item.setValue(label);
						}
						System.out.println(index);
						result.add(item);
					}
				}
			}
		}
		System.out.println("TOTAL SELECT OPTIONS FOUND: " + result.size());
		printItemSet(result);
		return result;
	}
	
	private void printParameterMap() {
		System.out.println("PRINTING PARAMETER MAP");
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
		System.out.println("INSIDE SETTER FOR ERROR MESSAGE");
		this.errorStringBean.setString(new Locale("en"), errorMsg);
		System.out.println("ENDING SETTER FOR ERROR MESSAGE: " + this.errorStringBean.getString(new Locale("en")));
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
		items = ((IComponentPropertiesSelect) properties).getItemset().getItems(new Locale("en"));
		System.out.println("GETTING ITEMS: " + items.size());
		System.out.println("optionsCount: " + optionsCount);
		int k = optionsCount - items.size();
		System.out.println("K: " + k);
		for(int i = 0; i < k; i++) {
			System.out.println("ADDING NEW ITEMBEAN");
			items.add(new ItemBean());
		}
		System.out.println("addEmptyOption: " + addEmptyOption);
		if(addEmptyOption) {
			items.add(new ItemBean());
			this.addEmptyOption = false;
			optionsCount++;
		}
		System.out.println("GETTING ITEMS: " + items.size());
		printItemSet(items);
		return items;
	}

	public void setItems(List<ItemBean> items) {
		System.out.println("SETTING ITEMS: " + items.size());
		printItemSet(items);
		this.items = items;
		((IComponentPropertiesSelect) properties).getItemset().setItems(new Locale("en"), items);
		System.out.println("AFTER SETTING ITEMS: " + ((IComponentPropertiesSelect) properties).getItemset().getItems(new Locale("en")).size());
		printItemSet(((IComponentPropertiesSelect) properties).getItemset().getItems(new Locale("en")));
	}
	
	private void printItemSet(List<ItemBean> items) {
		Iterator it = items.iterator();
		while(it.hasNext()) {
			ItemBean current = (ItemBean) it.next();
			System.out.println("ROW: " + current.getLabel() + " : " + current.getValue());
		}
	}

}
