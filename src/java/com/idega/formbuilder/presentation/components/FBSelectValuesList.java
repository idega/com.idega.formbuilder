package com.idega.formbuilder.presentation.components;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.documentmanager.component.beans.ItemBean;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.TextInput;

public class FBSelectValuesList extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "SelectValuesList";
	
	private static final String INLINE_DIV_STYLE = "display: inline";
	private static final String HIDDEN_DIV_STYLE = "display: none";
	
	private static final String DIV_PREFIX = "rowDiv_";
	private static final String DELETE_BUTTON_PREFIX = "delB_";
	private static final String EXPAND_BUTTON_PREFIX = "expB_";
	private static final String LABEL_FIELD_PREFIX = "labelF_";
	private static final String VALUE_FIELD_PREFIX = "valueF_";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete-tiny.png";
	private static final String ADD_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/add-tiny.png";
	private static final String EXPAND_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png";
	private static final String COLLAPSE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_left.png";

	private FormComponent component;
	
	public FBSelectValuesList() {
		this(null);
	}
	
	public FBSelectValuesList(FormComponent component) {
		this.component = component;
	}
	
	protected void initializeComponent(FacesContext context) {
		Layer container = new Layer(Layer.DIV);
		container.setId("selectOpts");
		container.setStyleClass(getStyleClass());
		
		Image addButton = new Image();
		addButton.setId("addButton");
		addButton.setSrc(ADD_BUTTON_IMG);
		addButton.setOnClick("addNewItem('selectOpts');");
		
		container.add(addButton);
		
		Image expandAllButton = new Image();
		expandAllButton.setId("expandAllButton");
		expandAllButton.setSrc(EXPAND_BUTTON_IMG);
		expandAllButton.setOnClick("expandAllItems();");
		container.add(expandAllButton);
		
		Image collapseAllButton = new Image();
		collapseAllButton.setId("collapseAllButton");
		collapseAllButton.setSrc(COLLAPSE_BUTTON_IMG);
		collapseAllButton.setOnClick("collapseAllItems();");
		container.add(collapseAllButton);
		
		Layer inner = new Layer(Layer.DIV);
		inner.setId("selectOptsInner");
		
		List<ItemBean> itemSet = component.getItems();
		for(int i = 0; i < itemSet.size(); i++) {
			String label = itemSet.get(i).getLabel();
			String value = itemSet.get(i).getValue();
			inner.add(getNextSelectRow(label, value, i, context));
		}
		
		container.add(inner);
		
		add(container);
	}
	
	private UIComponent getNextSelectRow(String field, String value, int index, FacesContext context) {
		Layer row = new Layer(Layer.DIV);
		row.setId(DIV_PREFIX + index);
		row.setStyleClass("selectRow");
		
		Image deleteButton = new Image();
		deleteButton.setSrc(DELETE_BUTTON_IMG);
		deleteButton.setId(DELETE_BUTTON_PREFIX + index);
		deleteButton.setStyleClass(INLINE_DIV_STYLE);
		deleteButton.setOnClick("deleteThisItem(this.getParent().getProperty('id'))");
		row.add(deleteButton);
		
		TextInput labelF = new TextInput();
		labelF.setValue(field);
		labelF.setId(LABEL_FIELD_PREFIX + index);
		labelF.setStyleClass(INLINE_DIV_STYLE);
		labelF.setStyleClass("fbSelectListItem");
		labelF.setOnBlur("saveLabel(this)");
		row.add(labelF);
		
		TextInput valueF = new TextInput();
		valueF.setValue(value);
		valueF.setId(VALUE_FIELD_PREFIX + index);
		valueF.setStyleClass(HIDDEN_DIV_STYLE);
		valueF.setStyleClass("fbSelectListItem");
		valueF.setOnBlur("saveValue(this)");
		row.add(valueF);
		
		Image expandButton = new Image();
		expandButton.setSrc(EXPAND_BUTTON_IMG);
		expandButton.setId(EXPAND_BUTTON_PREFIX + index);
		expandButton.setStyleClass(INLINE_DIV_STYLE);
		expandButton.setOnClick("expandOrCollapse(this,true)");
		row.add(expandButton);
		
		return row;
	}
	
	public FormComponent getComponent() {
		return component;
	}

	public void setComponent(FormComponent component) {
		this.component = component;
	}
}
