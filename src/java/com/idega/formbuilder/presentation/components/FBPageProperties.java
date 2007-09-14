package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.TextInput;

public class FBPageProperties extends FBComponentBase {
	
public static final String COMPONENT_TYPE = "PageProperties";
	
	private static final String CONTENT_FACET = "CONTENT_FACET";
	
	public FBPageProperties() {
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
		
//		Text titleLabel = ;
//		HtmlOutputText titleLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
//		titleLabel.setValue("Section title");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(new Text("Section title"));
		
		TextInput title = new TextInput();
//		HtmlInputText title = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		title.setId("pageTitle");
		title.setValueBinding("value", application.createValueBinding("#{formPage.title}"));
		title.setOnBlur("savePageTitle(this.value);");
		title.setOnKeyDown("savePropertyOnEnter(this.value,'pageTitle',event);");
		
		cell = row.createCell();
		cell.add(title);
		
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
