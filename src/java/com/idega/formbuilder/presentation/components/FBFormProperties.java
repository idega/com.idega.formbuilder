package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlInputTextarea;
import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.component.html.ext.HtmlSelectBooleanCheckbox;

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
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		
		Table2 table = new Table2();
		table.setStyleAttribute("width: 280px;");
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
		title.setOnblur("saveFormTitle(this.value)");
		
		cell = row.createCell();
		cell.add(title);
		
		HtmlOutputText previewLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		previewLabel.setValue("Form contains preview");
		
		row = group.createRow();
		cell = row.createCell();
		cell.add(previewLabel);
		
		HtmlSelectBooleanCheckbox preview = (HtmlSelectBooleanCheckbox) application.createComponent(HtmlSelectBooleanCheckbox.COMPONENT_TYPE);
		preview.setId("previewScreen");
		preview.setValueBinding("value", application.createValueBinding("#{formDocument.hasPreview}"));
		preview.setOnclick("saveHasPreview(this.value)");
		
		cell = row.createCell();
		cell.add(preview);
		
		HtmlOutputText thankYouTitleLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		thankYouTitleLabel.setValue("Thank you title");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(thankYouTitleLabel);
		
		HtmlInputText thankYouTitle = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		thankYouTitle.setId("thankYouTitle");
		thankYouTitle.setValueBinding("value", application.createValueBinding("#{formDocument.thankYouTitle}"));
		thankYouTitle.setOnblur("saveThankYouTitle(this.value)");
		
		cell = row.createCell();
		cell.add(thankYouTitle);
		
		HtmlOutputText thankYouTextLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		thankYouTextLabel.setValue("Thank You Text");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(thankYouTextLabel);
		
		HtmlInputTextarea thankYouText = (HtmlInputTextarea) application.createComponent(HtmlInputTextarea.COMPONENT_TYPE);
		thankYouText.setId("thankYouText");
		thankYouText.setValueBinding("value", application.createValueBinding("#{formDocument.thankYouText}"));
		thankYouText.setOnblur("saveThankYouText(this.value)");
		
		cell = row.createCell();
		cell.add(thankYouText);
		
//		HtmlCommandButton newPageButton = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
//		newPageButton.setValue("New page");
//		newPageButton.setId("newPB_FP");
//		newPageButton.setOnclick("createNewPage();return false");
//		
//		row = group.createRow();
//		cell = row.createCell();
//		cell.add(newPageButton);
		
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
