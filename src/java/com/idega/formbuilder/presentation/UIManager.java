package com.idega.formbuilder.presentation;

import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;

import org.apache.myfaces.component.html.ext.HtmlInputHidden;
import org.apache.myfaces.component.html.ext.HtmlPanelGrid;
import org.apache.myfaces.custom.div.Div;
import org.apache.myfaces.custom.tabbedpane.HtmlPanelTabbedPane;
import org.apache.myfaces.custom.tabbedpane.TabChangeEvent;
import org.apache.myfaces.custom.tabbedpane.TabChangeListener;

import com.idega.formbuilder.business.ComponentPalette;

public class UIManager implements TabChangeListener {
	
	private ComponentPalette palette;
	private int elementCount;
	
	private HtmlPanelTabbedPane optionsPane = null;
	private HtmlInputHidden selectedFieldType = null;
	private HtmlInputHidden selectedFieldId = null;
	private Div formView = null;
	
	private String selectedFieldTypeValue;
	
	public String getSelectedFieldTypeValue() {
		return selectedFieldTypeValue;
	}

	public void setSelectedFieldTypeValue(String selectedFieldTypeValue) {
		this.selectedFieldTypeValue = selectedFieldTypeValue;
	}

	public HtmlInputHidden getSelectedFieldType() {
		return selectedFieldType;
	}

	public void setSelectedFieldType(HtmlInputHidden selectedFieldType) {
		this.selectedFieldType = selectedFieldType;
	}

	public UIManager() {
		this.palette = new ComponentPalette();
	}

	public ComponentPalette getPalette() {
		return palette;
	}
	
	public void saveForm() {
		
	}
	
	public void newForm() {
		this.saveForm();
		clearFormView();
	}
	
	public void deleteForm() {
		
	}
	
	private void clearFormView() {
		getFormView().getChildren().clear();
	}
	
	public void addFormField() {
		Application application = FacesContext.getCurrentInstance().getApplication();
		try {
			String id = "form_component_" + new Integer(elementCount++).toString();
			List children = getFormView().getChildren();
	        Div field = (Div) application.createComponent(Div.COMPONENT_TYPE);
	        field.setStyleClass("form_component");
	        field.setId(id);
	        field.getAttributes().put("forceId", "true");
	        HtmlPanelGrid fieldInnerStructure = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
	        fieldInnerStructure.setColumns(2);
	        fieldInnerStructure.setOnclick("selectFormField(this)");
	        fieldInnerStructure.setStyleClass("fieldInnerStructure");
	        Div fieldInnerHtml = (Div) application.createComponent(Div.COMPONENT_TYPE);
	        Div fieldButtons = (Div) application.createComponent(Div.COMPONENT_TYPE);
	        fieldInnerStructure.getChildren().add(fieldInnerHtml);
	        fieldInnerStructure.getChildren().add(fieldButtons);
	        field.getChildren().add(fieldInnerStructure);
	        //System.out.println(getSelectedFieldTypeValue());
	        field.setValue("field_text");
	        children.add(field);
		} catch(Exception e) {
			System.out.println("FORM VIEW IS NULL");
		}
	}
	
	public void selectFormField() {
		Application application = FacesContext.getCurrentInstance().getApplication();
		this.getOptionsPane().setSelectedIndex(2);
	}
	
	public void processTabChange(TabChangeEvent e) throws AbortProcessingException {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("--------" + e.getComponent().getAttributes().get("id"));
	}

	public Div getFormView() {
		return formView;
	}

	public void setFormView(Div formView) {
		this.formView = formView;
	}

	public void setPalette(ComponentPalette palette) {
		this.palette = palette;
	}

	public HtmlInputHidden getSelectedFieldId() {
		return selectedFieldId;
	}

	public void setSelectedFieldId(HtmlInputHidden selectedFieldId) {
		this.selectedFieldId = selectedFieldId;
	}

	public HtmlPanelTabbedPane getOptionsPane() {
		return optionsPane;
	}

	public void setOptionsPane(HtmlPanelTabbedPane optionsPane) {
		this.optionsPane = optionsPane;
	}
}
