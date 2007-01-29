package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.IComponentPropertiesSelect;
import com.idega.formbuilder.business.form.beans.ILocalizedItemset;
import com.idega.formbuilder.business.form.beans.ItemBean;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.view.ActionManager;

public class FormComponent implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private IComponentProperties properties;
	
	private String id;
	
	private Boolean required;
	private String label;
	private String errorMessage;
	
	private String emptyLabel;
	private String externalSrc;
	private List<ItemBean> items = new ArrayList<ItemBean>();
	
	private LocalizedStringBean labelStringBean;
	private LocalizedStringBean errorStringBean;
	
	private LocalizedStringBean emptyLabelBean;
	private ILocalizedItemset itemset;
	
	private String dataSrc;
	
	public String getDataSrc() {
		if(properties instanceof IComponentPropertiesSelect) {
			IComponentPropertiesSelect icps = (IComponentPropertiesSelect) properties;
			if(icps.getDataSrcUsed() != null) {
				this.dataSrc = icps.getDataSrcUsed().toString();
			} else {
				this.setDataSrc(DataSourceList.localDataSrc);
			}
		}
		return dataSrc;
	}

	public void setDataSrc(String dataSrc) {
		this.dataSrc = dataSrc;
		((IComponentPropertiesSelect) properties).setDataSrcUsed(Integer.parseInt(dataSrc));
	
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
		
		this.labelStringBean = null;
		this.errorStringBean = null;
		this.emptyLabelBean = null;
		this.itemset = null;
		
		this.dataSrc = DataSourceList.localDataSrc;
	}
	
	public void clearFormComponentInfo() {
		this.id = "";
		this.label = "";
		this.labelStringBean = null;
		this.errorMessage = "";
		this.errorStringBean = null;
		this.required = false;
		this.emptyLabel = "";
		this.emptyLabelBean = null;
		this.itemset = null;
		
		this.properties = null;
	}
	
	public void loadProperties(String id) {
		this.id = id;
		properties = ActionManager.getFormManagerInstance().getComponentProperties(id);
		
		required = properties.isRequired();
		
		labelStringBean = properties.getLabel();
		label = labelStringBean.getString(new Locale("en"));
		
		errorStringBean = properties.getErrorMsg();
		errorMessage = errorStringBean.getString(new Locale("en"));
		
		if(properties instanceof IComponentPropertiesSelect) {
			IComponentPropertiesSelect selectProperties = (IComponentPropertiesSelect) properties;
			
			emptyLabelBean = selectProperties.getEmptyElementLabel();
			emptyLabel = emptyLabelBean.getString(new Locale("en"));
			
			externalSrc = ((IComponentPropertiesSelect) properties).getExternalDataSrc();
			
			itemset = ((IComponentPropertiesSelect) properties).getItemset();
			items = itemset.getItems(new Locale("en"));
			
			if(items.size() == 0) {
				items.add(new ItemBean("", ""));
				items.add(new ItemBean("", ""));
				items.add(new ItemBean("", ""));
			}
		}
	}
	
	public void saveComponentLabel(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyTitle");
		if(value != null) {
			setLabel(value);
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		}
	}
	
	public void saveComponentRequired(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyRequired");
		if(value != null) {
			setRequired(true);
		} else {
			setRequired(false);
		}
		ActionManager.getFormManagerInstance().updateFormComponent(id);
	}
	
	public void saveComponentErrorMessage(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyErrorMessage");
		if(value != null) {
			setErrorMessage(value);
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		}
	}
	
	public void saveComponentEmptyLabel(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyEmptyLabel");
		if(value != null) {
			setEmptyLabel(value);
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		}
	}
	
	public void saveComponentExternalSource(ActionEvent ae) throws Exception {
		String value = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("propertyExternal");
		if(value != null) {
			this.setExternalSrc(value);
			ActionManager.getFormManagerInstance().updateFormComponent(id);
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
		this.errorStringBean.setString(new Locale("en"), errorMessage);
		this.properties.setErrorMsg(errorStringBean);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		this.labelStringBean.setString(new Locale("en"), label);
		this.properties.setLabel(labelStringBean);
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
		this.properties.setRequired(required);
	}

	public IComponentProperties getProperties() {
		return properties;
	}

	public void setProperties(IComponentProperties properties) {
		this.properties = properties;
	}

	public boolean isSimple() {
		if(properties instanceof IComponentPropertiesSelect) {
			return false;
		}
		return true;
	}

	public String getEmptyLabel() {
		return emptyLabel;
	}

	public void setEmptyLabel(String emptyLabel) {
		this.emptyLabel = emptyLabel;
		this.emptyLabelBean.setString(new Locale("en"), emptyLabel);
		((IComponentPropertiesSelect) this.properties).setEmptyElementLabel(emptyLabelBean);
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
		((IComponentPropertiesSelect) this.properties).setExternalDataSrc(externalSrc);
	}

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
		this.itemset.setItems(new Locale("en"), items);
		try {
			ActionManager.getFormManagerInstance().updateFormComponent(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public ILocalizedItemset getItemset() {
		return itemset;
	}

	public void setItemset(ILocalizedItemset itemset) {
		this.itemset = itemset;
	}

}
