package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;

public class FBFormProperties extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormProperties";
	
	private static final String CONTENT_FACET = "CONTENT_FACET";
	
	public FBFormProperties() {
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
		
		HtmlOutputText titleLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		titleLabel.setValue("Form title");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(titleLabel);
		
		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("formTitle");
		title.setValueBinding("value", application.createValueBinding("#{formDocument.formTitle}"));
		title.setOnblur("$('workspaceform1:saveFormTitle').click();");
		
		cell = row.createCell();
		cell.add(title);
		
		HtmlOutputText submitLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		submitLabel.setValue("Submit button label");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(submitLabel);
		
		HtmlInputText submit = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		submit.setId("submitLabel");
		submit.setOnblur("$('workspaceform1:saveSubmitLabel').click();");
		submit.setValueBinding("value", application.createValueBinding("#{formDocument.submitLabel}"));
		
		cell = row.createCell();
		cell.add(submit);
		
		addFacet(CONTENT_FACET, table);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		UIComponent body = getFacet(CONTENT_FACET);
		if(body != null) {
			renderChild(context, body);
		}
	}
	
}
