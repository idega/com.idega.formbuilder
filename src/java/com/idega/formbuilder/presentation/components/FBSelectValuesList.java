package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.documentmanager.component.beans.ItemBean;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.TextInput;
import com.idega.webface.WFUtil;

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

	private static final String ADD_NEW_BUTTON = "ADD_NEW_BUTTON";
	private static final String EXPAND_ALL_BUTTON = "EXPAND_ALL_BUTTON";
	private static final String COLLAPSE_ALL_BUTTON = "COLLAPSE_ALL_BUTTON";
	
	protected void initializeComponent(FacesContext context) {
		Image addButton = new Image();
		addButton.setId("addButton");
		addButton.setSrc(ADD_BUTTON_IMG);
		addButton.setOnClick("addNewItem('" + getId() + "');");
		addFacet(ADD_NEW_BUTTON, addButton);
		
		Image expandAllButton = new Image();
		expandAllButton.setId("expandAllButton");
		expandAllButton.setSrc(EXPAND_BUTTON_IMG);
		expandAllButton.setOnClick("expandAllItems();");
		addFacet(EXPAND_ALL_BUTTON, expandAllButton);
		
		Image collapseAllButton = new Image();
		collapseAllButton.setId("collapseAllButton");
		collapseAllButton.setSrc(COLLAPSE_BUTTON_IMG);
		collapseAllButton.setOnClick("collapseAllItems();");
		addFacet(COLLAPSE_ALL_BUTTON, collapseAllButton);
	}
	
	private UIComponent getNextSelectRow(String field, String value, int index, FacesContext context) {
		Layer row = new Layer(Layer.DIV);
		row.setId(DIV_PREFIX + index);
		
		Image deleteButton = new Image();
		deleteButton.setSrc(DELETE_BUTTON_IMG);
		deleteButton.setId(DELETE_BUTTON_PREFIX + index);
		deleteButton.setStyleClass(INLINE_DIV_STYLE);
		deleteButton.setOnClick("deleteThisItem(this.parentNode.id)");
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
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		UIComponent addOptionButton = getFacet(ADD_NEW_BUTTON);
		if(addOptionButton != null) {
			renderChild(context, addOptionButton);
		}
		UIComponent expandAllButton = getFacet(EXPAND_ALL_BUTTON);
		if(expandAllButton != null) {
			renderChild(context, expandAllButton);
		}
		UIComponent collapseAllButton = getFacet(COLLAPSE_ALL_BUTTON);
		if(collapseAllButton != null) {
			renderChild(context, collapseAllButton);
		}
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId() + "Inner", null);
		writer.writeAttribute("class", getStyleClass() + "Inner", null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("DIV");
		writer.endElement("DIV");
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		List<ItemBean> itemSet = (List<ItemBean>) ((FormComponent) WFUtil.getBeanInstance(FormComponent.BEAN_ID)).getItems();
		for(int i = 0; i < itemSet.size(); i++) {
			String label = itemSet.get(i).getLabel();
			String value = itemSet.get(i).getValue();
			renderChild(context, getNextSelectRow(label, value, i, context));
		}
	}
}
