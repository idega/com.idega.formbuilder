package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;

import com.idega.formbuilder.business.form.PropertiesSelect;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.webface.WFDivision;
import com.idega.webface.WFUtil;

public class FBAdvancedProperties extends FBComponentBase {

	public static final String COMPONENT_TYPE = "AdvancedProperties";
	
	private static final String CONTENT_FACET = "CONTENT_FACET";
	private static final String EXTERNAL_SRC_FACET = "EXTERNAL_SRC_FACET";
	private static final String LOCAL_SRC_FACET = "LOCAL_SRC_FACET";
	
	public FBAdvancedProperties() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		
		Table2 table = new Table2();
		table.setId("advPropsPanel1");
//		table.setStyleClass(getStyleClass());
		table.setStyleAttribute("width: 300px;");
		table.setCellpadding(0);
		TableRowGroup group = table.createBodyRowGroup();
		TableRow row = null;
		TableCell2 cell = null;
		
		HtmlOutputText emptyLabelLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		emptyLabelLabel.setValue("Empty item label");
		
		row = group.createRow();
		cell = row.createCell();
		cell.setWidth("100");
		cell.add(emptyLabelLabel);
		
		HtmlInputText emptyLabel = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		emptyLabel.setId("propertyEmptyLabel");
		emptyLabel.setValueBinding("value", application.createValueBinding("#{formComponent.emptyLabel}"));
		emptyLabel.setOnblur("$('workspaceform1:saveEmptyLabel').click();");
		
		cell = row.createCell();
		cell.add(emptyLabel);
		
		HtmlOutputText advancedL = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		advancedL.setValue("Select source");

		row = group.createRow();
		cell = row.createCell();
		cell.add(advancedL);
		
		HtmlSelectOneRadio dataSrcSwitch = (HtmlSelectOneRadio) application.createComponent(HtmlSelectOneRadio.COMPONENT_TYPE);
		dataSrcSwitch.setStyleClass("inlineRadioButton");
		dataSrcSwitch.setId("dataSrcSwitch");
		dataSrcSwitch.setOnchange("switchDataSource(this);");
		dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{formComponent.dataSrc}"));

		UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
		addChild(dataSrcs, dataSrcSwitch);
		
		cell = row.createCell();
		cell.add(dataSrcSwitch);
		
		Table2 table2 = new Table2();
		table2.setId("advPropsPanel2");
//		table2.setStyleClass(getStyleClass());
		table2.setStyleAttribute("width: 300px;");
		table2.setCellpadding(0);
		TableRowGroup group2 = table2.createBodyRowGroup();
		TableRow row2 = null;
		TableCell2 cell2 = null;
		
		HtmlOutputText externalSrcLabel = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		externalSrcLabel.setValue("External data source");
		
		row2 = group2.createRow();
		cell2 = row2.createCell();
		cell2.setWidth("100");
		cell2.add(externalSrcLabel);
		
		HtmlInputText external = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		external.setId("propertyExternal");
		external.setValueBinding("value", application.createValueBinding("#{formComponent.externalSrc}"));
		external.setOnblur("$('workspaceform1:saveExtSrc').click();");
		
		cell2 = row2.createCell();
		cell2.add(external);
		
		WFDivision localSrcDiv = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		localSrcDiv.setId("localSrcDiv");
//		localSrcDiv.setStyleClass(getStyleClass());
		
		FBSelectValuesList selectValues = (FBSelectValuesList) application.createComponent(FBSelectValuesList.COMPONENT_TYPE);
		selectValues.setValueBinding("itemSet", application.createValueBinding("#{formComponent.items}"));
		selectValues.setId("selectOpts");
		addChild(selectValues, localSrcDiv);
		
		addFacet(CONTENT_FACET, table);
		addFacet(LOCAL_SRC_FACET, localSrcDiv);
		addFacet(EXTERNAL_SRC_FACET, table2);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		FormComponent component = (FormComponent) WFUtil.getBeanInstance("formComponent");
		boolean simple = component.isSimple();
		Integer dataSrc = Integer.parseInt(component.getDataSrc());
		WFDivision local = (WFDivision) getFacet(LOCAL_SRC_FACET);
		Table2 body = (Table2) getFacet(CONTENT_FACET);
		Table2 ext = (Table2) getFacet(EXTERNAL_SRC_FACET);
		if(!simple) {
			if(body != null) {
				body.setStyleAttribute("visibility: visible");
				renderChild(context, body);
			}
			if(dataSrc == PropertiesSelect.LOCAL_DATA_SRC) {
				local.setStyle("visibility: visible");
				renderChild(context, local);
				ext.setStyleAttribute("visibility: hidden");
				renderChild(context, ext);
			} else {
				local.setStyle("visibility: hidden");
				renderChild(context, local);
				ext.setStyleAttribute("visibility: visible");
				renderChild(context, ext);
			}
		} else {
			
			local.setStyle("visibility: hidden");
			
			if(body != null) {
				body.setStyleAttribute("visibility: hidden");
				renderChild(context, body);
			}
			renderChild(context, local);
			if(ext != null) {
				ext.setStyleAttribute("visibility: hidden");
				renderChild(context, ext);
			}
		}
	}
	
}
