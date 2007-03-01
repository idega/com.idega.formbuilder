package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlInputText;

import com.idega.documentmanager.business.form.beans.ItemBean;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.webface.WFDivision;
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
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/delete.png";
	private static final String ADD_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/add.png";
	private static final String EXPAND_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_right.png";
	private static final String COLLAPSE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow_left.png";

	private static final String ADD_NEW_BUTTON = "ADD_NEW_BUTTON";
	private static final String EXPAND_ALL_BUTTON = "EXPAND_ALL_BUTTON";
	private static final String COLLAPSE_ALL_BUTTON = "COLLAPSE_ALL_BUTTON";
	
	public FBSelectValuesList() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		HtmlGraphicImage addButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		addButton.setId("addButton");
		addButton.setValue(ADD_BUTTON_IMG);
		addButton.setOnclick("addNewItem('" + getId() + "');");
		addFacet(ADD_NEW_BUTTON, addButton);
		
		HtmlGraphicImage expandAllButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		expandAllButton.setId("expandAllButton");
		expandAllButton.setValue(EXPAND_BUTTON_IMG);
		expandAllButton.setOnclick("expandAllItems();");
		addFacet(EXPAND_ALL_BUTTON, expandAllButton);
		
		HtmlGraphicImage collapseAllButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		collapseAllButton.setId("collapseAllButton");
		collapseAllButton.setValue(COLLAPSE_BUTTON_IMG);
		collapseAllButton.setOnclick("collapseAllItems();");
		addFacet(COLLAPSE_ALL_BUTTON, collapseAllButton);
	}
	
	private UIComponent getNextSelectRow(String field, String value, int index, FacesContext context) {
		Application application = context.getApplication();
		
		WFDivision row = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		row.setId(DIV_PREFIX + index);
		
		HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		deleteButton.setValue(DELETE_BUTTON_IMG);
		deleteButton.setId(DELETE_BUTTON_PREFIX + index);
		deleteButton.setStyle(INLINE_DIV_STYLE);
		deleteButton.setOnclick("deleteThisItem(this.parentNode.id)");
		addChild(deleteButton, row);
		
		HtmlInputText labelF = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		labelF.setValue(field);
		labelF.setId(LABEL_FIELD_PREFIX + index);
		labelF.setStyle(INLINE_DIV_STYLE);
		labelF.setStyleClass("fbSelectListItem");
		labelF.setOnblur("saveLabel(this)");
		addChild(labelF, row);
		
		HtmlInputText valueF = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		valueF.setValue(value);
		valueF.setId(VALUE_FIELD_PREFIX + index);
		valueF.setStyle(HIDDEN_DIV_STYLE);
		valueF.setStyleClass("fbSelectListItem");
		valueF.setOnblur("saveValue(this)");
		addChild(valueF, row);
		
		HtmlGraphicImage expandButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		expandButton.setValue(EXPAND_BUTTON_IMG);
		expandButton.setId(EXPAND_BUTTON_PREFIX + index);
		expandButton.setStyle(INLINE_DIV_STYLE);
		expandButton.setOnclick("expandOrCollapse(this,true)");
		addChild(expandButton, row);
		
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
		List<ItemBean> itemSet = (List<ItemBean>) ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		for(int i = 0; i < itemSet.size(); i++) {
			String label = itemSet.get(i).getLabel();
			String value = itemSet.get(i).getValue();
			renderChild(context, getNextSelectRow(label, value, i, context));
		}
	}
}
