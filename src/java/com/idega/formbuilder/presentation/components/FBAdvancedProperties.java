package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
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
		dataSrcSwitch.setOnchange("$('workspaceform1:srcSwitcher').click();");
		dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{formComponent.dataSrc}"));

		UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
		dataSrcSwitch.getChildren().add(dataSrcs);
		
		cell = row.createCell();
		cell.add(dataSrcSwitch);
		
		/*UIAjaxSupport dataSrcsS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		dataSrcsS.setEvent("onchange");
		dataSrcsS.setReRender("workspaceform1:ajaxMenuPanel");
		dataSrcsS.setActionListener(application.createMethodBinding("#{dataSources.switchDataSource}", new Class[]{ActionEvent.class}));
		dataSrcsS.setAjaxSingle(true);
		dataSrcSwitch.getChildren().add(dataSrcsS);*/
		
		Table2 table2 = new Table2();
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
		
		FBDivision localSrcDiv = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		localSrcDiv.setId("localSrcDiv");
		
		FBSelectValuesList selectValues = (FBSelectValuesList) application.createComponent(FBSelectValuesList.COMPONENT_TYPE);
		selectValues.setValueBinding("itemSet", application.createValueBinding("#{formComponent.items}"));
		selectValues.setId("selectOpts");
		localSrcDiv.getChildren().add(selectValues);
		
		addFacet(CONTENT_FACET, table);
		addFacet(LOCAL_SRC_FACET, localSrcDiv);
		addFacet(EXTERNAL_SRC_FACET, table2);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		Integer current;
		if (!isRendered()) {
			return;
		}
		if(!((FormComponent) WFUtil.getBeanInstance("formComponent")).isSimple()) {
			UIComponent body = getFacet(CONTENT_FACET);
			if(body != null) {
				renderChild(context, body);
			}
			
			current = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getPropertiesSelect().getDataSrcUsed();
			if(current == PropertiesSelect.LOCAL_DATA_SRC) {
				UIComponent local = getFacet(LOCAL_SRC_FACET);
				if(local != null) {
					renderChild(context, local);
				}
			} else {
				UIComponent ext = getFacet(EXTERNAL_SRC_FACET);
				if(ext != null) {
					renderChild(context, ext);
				}
			}
		}
	}
	
}
