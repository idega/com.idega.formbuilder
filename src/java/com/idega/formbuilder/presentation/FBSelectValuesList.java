package com.idega.formbuilder.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;

import com.idega.formbuilder.business.form.beans.ItemBean;

public class FBSelectValuesList extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_selectvalueslist";
	public static final String COMPONENT_FAMILY = "formbuilder";
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

	private String styleClass;
	private List<ItemBean> itemSet = new ArrayList<ItemBean>();
	
	public FBSelectValuesList() {
		super();
		this.setRendererType(FBSelectValuesList.RENDERER_TYPE);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBSelectValuesList.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return FBSelectValuesList.RENDERER_TYPE;
	}
	
	public void initializeComponent(FacesContext context) {
		System.out.println("INITIALIZING VALUE LIST");
		Application application = context.getApplication();
		this.getChildren().clear();
		
		HtmlGraphicImage addButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		addButton.setValue(ADD_BUTTON_IMG);
		//addButton.setOnclick("addNewEmptySelect()");
		addButton.setOnclick("addEmptyOption()");
		this.getChildren().add(addButton);
		
		ValueBinding vb = this.getValueBinding("itemSet");
		List<ItemBean> items = new ArrayList<ItemBean>();
		int listSize;
		if(vb != null) {
			items = (List<ItemBean>) vb.getValue(context);
		}
		listSize = items.size();
		
		if(listSize < 1) {
			System.out.println("3 FIELDS");
			for(int i = 0; i < 3; i++) {
				this.getChildren().add(getNextSelectRow("", "", i, context));
			}
			
		} else {
			System.out.println(listSize + " FIELDS");
			for(int i = 0; i < listSize; i++) {
				String label = items.get(i).getLabel();
				String value = items.get(i).getValue();
				this.getChildren().add(getNextSelectRow(label, value, i, context));
			}
		}
	}
	
	protected UIComponent getNextSelectRow(String field, String value, int index, FacesContext context) {
		Application application = context.getApplication();
		
		FBDivision row = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		row.setId(DIV_PREFIX + index);
		
		HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		deleteButton.setValue(DELETE_BUTTON_IMG);
		deleteButton.setId(DELETE_BUTTON_PREFIX + index);
		deleteButton.setStyle(INLINE_DIV_STYLE);
		deleteButton.setOnclick("deleteThisRow(this.parentNode.id)");
		row.getChildren().add(deleteButton);
		
		HtmlInputText labelF = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		labelF.setValue(field);
		labelF.setId(LABEL_FIELD_PREFIX + index);
		labelF.setStyle(INLINE_DIV_STYLE);
		row.getChildren().add(labelF);
		
		HtmlInputText valueF = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		valueF.setValue(value);
		valueF.setId(VALUE_FIELD_PREFIX + index);
		valueF.setStyle(HIDDEN_DIV_STYLE);
		row.getChildren().add(valueF);
		
		HtmlGraphicImage expandButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		expandButton.setValue(EXPAND_BUTTON_IMG);
		expandButton.setId(EXPAND_BUTTON_PREFIX + index);
		expandButton.setStyle(INLINE_DIV_STYLE);
		expandButton.setOnclick("expandOrCollapse(this,true)");
		row.getChildren().add(expandButton);
		
		return row;
	}
	
	public static Object[] getJavascriptParameters(String componentId) {
		Object values[] = new Object[12];
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
		return values;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = itemSet;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		itemSet = (List<ItemBean>) values[2];
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
		
		result.append("function addNewEmptySelect() {\n");
		result.append("var newInd = getNextRowIndex();\n");
		result.append("$(\"" + values[0] + "\").appendChild(getEmptySelect(newInd));\n");
		result.append("}\n");
		
		result.append("function deleteThisRow(ind) {\n");
		result.append("var currRow = document.getElementById(ind);\n");
		result.append("$(\"" + values[0] + "\").removeChild(currRow);\n");
		result.append("}\n");
		
		result.append("function getEmptySelect(index) {\n");
		result.append("var result = document.createElement('div');\n");
		result.append("result.setAttribute('id',\"" + "workspaceform1:" + values[7] + "\"+index);\n");
		result.append("var remB = document.createElement('img');\n");
		result.append("remB.setAttribute('style',\"" + values[5] + "\");\n");
		result.append("remB.setAttribute('onclick','deleteThisRow(this.parentNode.id)');\n");
		result.append("remB.setAttribute('id',\"" + values[3] + "\"+index);\n");
		result.append("remB.setAttribute('src',\"" + values[1] + "\");\n");
		result.append("var label = document.createElement('input');\n");
		result.append("label.setAttribute('id',\"" + values[6] + "\"+index);\n");
		result.append("label.setAttribute('type','text');\n");
		result.append("label.setAttribute('style',\"" + values[5] + "\");\n");
		result.append("label.setAttribute('value','');\n");
		result.append("var value = document.createElement('input');\n");
		result.append("value.setAttribute('id',\"" + values[11] + "\"+index);\n");
		result.append("value.setAttribute('type','text');\n");
		result.append("value.setAttribute('style',\"" + values[10] + "\");\n");
		result.append("value.setAttribute('value','');\n");
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
		result.append("var lastC = $(\"" + values[0] + "\").lastChild;\n");
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

}
