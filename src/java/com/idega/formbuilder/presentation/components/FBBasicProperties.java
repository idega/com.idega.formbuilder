package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;

public class FBBasicProperties extends FBComponentBase {

	public static final String COMPONENT_TYPE = "BasicProperties";
	
	private static final String CONTENT_FACET = "CONTENT_FACET";
	
	public FBBasicProperties() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		Table2 table = new Table2();
		table.setStyleAttribute("width: 300px;");
		table.setCellpadding(0);
		TableRowGroup group = table.createBodyRowGroup();
		TableRow row = null;
		TableCell2 cell = null;
		
		HtmlOutputText requiredLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		requiredLabel.setValue("Required field");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(requiredLabel);
		
		HtmlSelectBooleanCheckbox required = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		required.setId("propertyRequired");
		required.setValueBinding("value", application.createValueBinding("#{formComponent.required}"));
		required.setOnclick("saveRequired(this.value)");
		
		cell = row.createCell();
		cell.add(required);
		
		HtmlOutputText titleLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		titleLabel.setValue("Field name");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("propertyTitle");
		title.setValueBinding("value", application.createValueBinding("#{formComponent.label}"));
		title.setOnblur("saveComponentLabel(this.value)");
		
		cell = row.createCell();
		cell.add(title);
		
		HtmlOutputText errorMsgLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		errorMsgLabel.setValue("Error message");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(errorMsgLabel);
		
		HtmlInputText errorMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		errorMsg.setId("propertyErrorMessage");
		errorMsg.setValueBinding("value", application.createValueBinding("#{formComponent.errorMessage}"));
		errorMsg.setOnblur("saveErrorMessage(this.value)");
		
		cell = row.createCell();
		cell.add(errorMsg);
		
		HtmlOutputText helpMsgLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		helpMsgLabel.setValue("Help text");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(helpMsgLabel);
		
		HtmlInputText helpMsg = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		helpMsg.setId("propertyHelpText");
		helpMsg.setValueBinding("value", application.createValueBinding("#{formComponent.helpMessage}"));
		helpMsg.setOnblur("saveHelpMessage(this.value)");
		
		cell = row.createCell();
		cell.add(helpMsg);
		
		addFacet(CONTENT_FACET, table);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		UIComponent body = getFacet(CONTENT_FACET);
		if(body != null) {
			renderChild(context, body);
		}
	}
	
}
