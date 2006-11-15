package com.idega.formbuilder.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.custom.div.Div;

import com.idega.formbuilder.business.form.beans.ItemBean;

public class FBSelectValuesList extends UIComponentBase {
	
	public static final String RENDERER_TYPE = "fb_selectvalueslist";
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "SelectValuesList";

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
		Application application = context.getApplication();
		this.getChildren().clear();
		
		HtmlGraphicImage addButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		addButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/new_16.gif");
		//button.setStyleClass("speedButton");
		addButton.setOnclick("alert('Not implemented')");
		this.getChildren().add(addButton);
		
		int listSize = itemSet.size();
		
		if(listSize < 1) {
			
			for(int i = 0; i < 3; i++) {
				Div row = (Div) application.createComponent(Div.COMPONENT_TYPE);
				
				HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
				deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
				//button.setStyleClass("speedButton");
				deleteButton.setOnclick("alert('Not implemented')");
				row.getChildren().add(deleteButton);
				
				HtmlInputText label = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
				label.setValue("");
				//label.setStyleClass("speedButton");
				row.getChildren().add(label);
				
				HtmlGraphicImage expandButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
				expandButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
				//expandButton.setStyleClass("speedButton");
				expandButton.setOnclick("alert('Not implemented')");
				row.getChildren().add(expandButton);
				
				this.getChildren().add(row);
			}
			
		} else {
			
			for(int i = 0; i <= listSize; i++) {
				Div row = (Div) application.createComponent(Div.COMPONENT_TYPE);
				
				HtmlGraphicImage deleteButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
				deleteButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
				//button.setStyleClass("speedButton");
				deleteButton.setOnclick("alert('Not implemented')");
				row.getChildren().add(deleteButton);
				
				HtmlInputText label = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
				label.setValue("");
				//label.setStyleClass("speedButton");
				row.getChildren().add(label);
				
				HtmlGraphicImage expandButton = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
				expandButton.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
				//expandButton.setStyleClass("speedButton");
				expandButton.setOnclick("alert('Not implemented')");
				row.getChildren().add(expandButton);
				
				this.getChildren().add(row);
			}
			
		}
		
		/*UIData selectOptions = (UIData) application.createComponent(UIData.COMPONENT_TYPE);
		selectOptions.setValueBinding("value", application.createValueBinding("#{component.items}"));
		selectOptions.setVar("item");
		
		UIColumn labels = (UIColumn) application.createComponent(UIColumn.COMPONENT_TYPE);
		
		
		
		labels.getFacets().put("header", addButton);
		
		HtmlInputText label = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		label.setValueBinding("value", application.createValueBinding("#{item.label}"));
		
		UIColumn buttons = (UIColumn) application.createComponent(UIColumn.COMPONENT_TYPE);
		
		HtmlGraphicImage button = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		button.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
		//button.setStyleClass("speedButton");
		button.setOnclick("alert('Not implemented')");
		
		buttons.getChildren().add(button);
		labels.getChildren().add(label);
		selectOptions.getChildren().add(labels);
		selectOptions.getChildren().add(buttons);
		this.getChildren().add(selectOptions);*/
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
		itemSet = (List) values[2];
	}
	
	public String getEmbededJavascript(Object values[]) {
		return "";
	}

	public List getItemSet() {
		return itemSet;
	}

	public void setItemSet(List itemSet) {
		this.itemSet = itemSet;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
