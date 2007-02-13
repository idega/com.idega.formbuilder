package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlInputText;

import com.idega.formbuilder.business.form.beans.ItemBean;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.webface.WFDivision;

public class FBSelectValuesList extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "SelectValuesList";
	
	private static final String INLINE_DIV_STYLE = "display: inline";
	private static final String HIDDEN_DIV_STYLE = "display: none";
	private static final String HAND_CURSOR_STYLE = "cursor: pointer";
	
	private static final String DIV_PREFIX = "rowDiv_";
	private static final String DELETE_BUTTON_PREFIX = "delB_";
	private static final String EXPAND_BUTTON_PREFIX = "expB_";
	private static final String LABEL_FIELD_PREFIX = "labelF_";
	private static final String VALUE_FIELD_PREFIX = "valueF_";
	
	private static final String DELETE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/del_16.gif";
	private static final String ADD_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/new_16.gif";
	private static final String EXPAND_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-redo.png";
	private static final String COLLAPSE_BUTTON_IMG = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-undo.png";

	private static final String ADD_NEW_BUTTON = "ADD_NEW_BUTTON";
	private static final String EXPAND_ALL_BUTTON = "EXPAND_ALL_BUTTON";
	private static final String COLLAPSE_ALL_BUTTON = "COLLAPSE_ALL_BUTTON";
	
	private String id;
	private String styleClass;
	private List<ItemBean> itemSet = new ArrayList<ItemBean>();
	
	public FBSelectValuesList() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		HtmlGraphicImage addButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		addButton.setId("addButton");
		addButton.setValue(ADD_BUTTON_IMG);
		addButton.setOnclick("addNewItem()");
		addFacet(ADD_NEW_BUTTON, addButton);
		
		HtmlGraphicImage expandAllButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		expandAllButton.setId("expandAllButton");
		expandAllButton.setValue(EXPAND_BUTTON_IMG);
//		expandAllButton.setOnclick("addEmptyOption()");
		addFacet(EXPAND_ALL_BUTTON, expandAllButton);
		
		HtmlGraphicImage collapseAllButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		collapseAllButton.setId("collapseAllButton");
		collapseAllButton.setValue(COLLAPSE_BUTTON_IMG);
//		collapseAllButton.setOnclick("addEmptyOption()");
		addFacet(COLLAPSE_ALL_BUTTON, collapseAllButton);
		
		ValueBinding vb = this.getValueBinding("itemSet");
		if(vb != null) {
			itemSet = (List<ItemBean>) vb.getValue(context);
		}
		for(int i = 0; i < itemSet.size(); i++) {
			String label = itemSet.get(i).getLabel();
			String value = itemSet.get(i).getValue();
			add(getNextSelectRow(label, value, i, context));
		}
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
		row.getChildren().add(deleteButton);
		
		HtmlInputText labelF = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		labelF.setValue(field);
		labelF.setId(LABEL_FIELD_PREFIX + index);
		labelF.setStyle(INLINE_DIV_STYLE);
		labelF.setOnblur("saveLabel(this)");
		row.getChildren().add(labelF);
		
		HtmlInputText valueF = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		valueF.setValue(value);
		valueF.setId(VALUE_FIELD_PREFIX + index);
		valueF.setStyle(HIDDEN_DIV_STYLE);
		valueF.setOnblur("saveValue(this)");
		row.getChildren().add(valueF);
		
		HtmlGraphicImage expandButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		expandButton.setValue(EXPAND_BUTTON_IMG);
		expandButton.setId(EXPAND_BUTTON_PREFIX + index);
		expandButton.setStyle(INLINE_DIV_STYLE);
		expandButton.setOnclick("expandOrCollapse(this,true)");
		row.getChildren().add(expandButton);
		
		return row;
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
//		writer.writeAttribute("style", "width: 320px; height: 180px;", null);
		
		UIComponent addOptionButton = getFacet(ADD_NEW_BUTTON);
		if(addOptionButton != null) {
			renderChild(context, addOptionButton);
		}
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", getId() + "Inner", null);
		writer.writeAttribute("class", getStyleClass() + "Inner", null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("DIV");
		writer.endElement("DIV");
		
		writer.startElement("DIV", null);
		writer.writeAttribute("style", "display: none", null);
		Object values[] = new Object[1];
		values[0] = getId();
		writer.write(getEmbededJavascript(getJavascriptParameters(getId())));
		writer.endElement("DIV");
		
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		ValueBinding vb = this.getValueBinding("itemSet");
		if(vb != null) {
			itemSet = (List<ItemBean>) vb.getValue(context);
		}
		for(int i = 0; i < itemSet.size(); i++) {
			String label = itemSet.get(i).getLabel();
			String value = itemSet.get(i).getValue();
			renderChild(context, getNextSelectRow(label, value, i, context));
		}
	}
	
	public static Object[] getJavascriptParameters(String componentId) {
		Object values[] = new Object[13];
		values[0] = componentId;
		values[1] = DELETE_BUTTON_IMG;
		values[2] = EXPAND_BUTTON_IMG;
		values[3] = DELETE_BUTTON_PREFIX;
		values[4] = EXPAND_BUTTON_PREFIX;
		values[5] = INLINE_DIV_STYLE;
		values[6] = LABEL_FIELD_PREFIX;
		values[7] = DIV_PREFIX;
		values[8] = COLLAPSE_BUTTON_IMG;
		values[9] = HAND_CURSOR_STYLE;
		values[10] = HIDDEN_DIV_STYLE;
		values[11] = VALUE_FIELD_PREFIX;
		values[12] = componentId + "Inner";
		return values;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = itemSet;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		itemSet = (List<ItemBean>) values[3];
	}
	
	public static String getEmbededJavascript(Object values[]) {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		
		result.append("function expandOrCollapse(node,expand) {\n");
		result.append("if(expand) {\n");
		result.append("node.previousSibling.setAttribute('style','display: inline;');\n");
		result.append("var value = node.previousSibling.getAttribute('value');\n");
		//result.append("if(value.length == 0) {\n");
		//result.append("node.previousSibling.setAttribute('value',node.previousSibling.previousSibling.getAttribute('value'));\n");
		//result.append("}\n");
		result.append("node.setAttribute('src',\"" + values[8] + "\");\n");
		result.append("node.setAttribute('onclick','expandOrCollapse(this,false)');\n");
		result.append("} else {\n");
		result.append("node.previousSibling.setAttribute('style','display: none;');\n");
		result.append("node.setAttribute('src',\"" + values[2] + "\");\n");
		result.append("node.setAttribute('onclick','expandOrCollapse(this,true)');\n");
		result.append("}\n");
		result.append("}\n");
		
		result.append("function saveLabel(element) {\n");
		result.append("var index = element.id.split('_')[1];\n");
		result.append("var value = element.value;\n");
//		result.append("var value = element.getAttribute('value');\n");
//		result.append("alert(value);\n");
//		result.append("var value = element.getAttribute('value');\n");
		result.append("dwrmanager.saveLabel(removedItem,index,value);\n");
		result.append("}\n");
		
		result.append("function saveValue(element) {\n");
		result.append("var index = element.id.split('_')[1];\n");
		result.append("var value = element.value;\n");
		result.append("dwrmanager.saveValue(removedItem,index,value);\n");
		result.append("}\n");
		
		result.append("function addNewItem() {\n");
		result.append("var newInd = getNextRowIndex();\n");
		result.append("$(\"" + values[0] + "\").lastChild.appendChild(getEmptySelect(newInd));\n");
		result.append("}\n");
		
		result.append("function deleteThisItem(ind) {\n");
		
		result.append("var index = ind.split('_')[1];\n");
//		result.append("alert(index);\n");
		result.append("dwrmanager.removeItem(removedItem,index);\n");
		result.append("var currRow = document.getElementById(ind);\n");
		result.append("$(\"" + values[12] + "\").removeChild(currRow);\n");
		result.append("}\n");
		
		result.append("function removedItem() {\n");
		result.append("$('workspaceform1:refreshView').click();\n");
		result.append("}\n");
		
		result.append("function getEmptySelect(index) {\n");
		result.append("var result = document.createElement('div');\n");
		result.append("result.setAttribute('id',\"" + "workspaceform1:" + values[7] + "\"+index);\n");
		result.append("var remB = document.createElement('img');\n");
		result.append("remB.setAttribute('style',\"" + values[5] + "\");\n");
		result.append("remB.setAttribute('onclick','deleteThisItem(this.parentNode.id)');\n");
		result.append("remB.setAttribute('id',\"" + values[3] + "\"+index);\n");
		result.append("remB.setAttribute('src',\"" + values[1] + "\");\n");
		result.append("var label = document.createElement('input');\n");
		result.append("label.setAttribute('id',\"" + values[6] + "\"+index);\n");
		result.append("label.setAttribute('type','text');\n");
		result.append("label.setAttribute('style',\"" + values[5] + "\");\n");
		result.append("label.setAttribute('value','');\n");
		result.append("label.setAttribute('onblur', 'saveLabel(this)');\n");
		result.append("var value = document.createElement('input');\n");
		result.append("value.setAttribute('id',\"" + values[11] + "\"+index);\n");
		result.append("value.setAttribute('type','text');\n");
		result.append("value.setAttribute('style',\"" + values[10] + "\");\n");
		result.append("value.setAttribute('value','');\n");
		result.append("value.setAttribute('onblur', 'saveValue(this)');\n");
		result.append("var expB = document.createElement('img');\n");
		result.append("expB.setAttribute('style',\"" + values[5] + "\");\n");
		result.append("expB.setAttribute('id',\"" + values[4] + "\"+index);\n");
		result.append("expB.setAttribute('src',\"" + values[2] + "\");\n");
		result.append("expB.setAttribute('onclick','expandOrCollapse(this,true)');\n");
		result.append("result.appendChild(remB);\n");
		result.append("result.appendChild(label);\n");
		result.append("result.appendChild(value);\n");
		result.append("result.appendChild(expB);\n");
		result.append("return result;\n");
		result.append("}\n");
		
		result.append("function getNextRowIndex() {\n");
		result.append("var lastC = $(\"" + values[0] + "\").lastChild.lastChild;\n");
//		result.append("alert(lastC.id);\n");
		result.append("if(lastC) {\n");
		result.append("var lastCId = lastC.id;\n");
		result.append("var ind = lastCId.split('_')[1];\n");
		result.append("ind++;\n");
		result.append("return ind;\n");
		result.append("}\n");
		result.append("}\n");
		
		result.append("</script>\n");
		return result.toString();
		
	}

	public List getItemSet() {
		return itemSet;
	}

	public void setItemSet(List<ItemBean> itemSet) {
		this.itemSet = itemSet;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
