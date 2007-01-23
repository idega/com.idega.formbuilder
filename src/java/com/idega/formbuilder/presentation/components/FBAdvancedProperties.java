package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlSelectOneRadio;

import com.idega.formbuilder.business.form.beans.IComponentPropertiesSelect;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.webface.WFBlock;
import com.idega.webface.WFTitlebar;
import com.idega.webface.WFUtil;

public class FBAdvancedProperties extends FBComponentBase {

	public static final String COMPONENT_TYPE = "AdvancedProperties";
	
	private static final String WFBLOCK_CONTENT_FACET = "WFBLOCK_CONTENT_FACET";
	private static final String EXTERNAL_SRC_FACET = "EXTERNAL_SRC_FACET";
	private static final String LOCAL_SRC_FACET = "LOCAL_SRC_FACET";
	
	public FBAdvancedProperties() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		
		WFTitlebar bar = new WFTitlebar();
		bar.addTitleText("Selection component properties");
		
		WFBlock pageInfo = new WFBlock();
		pageInfo.setId("fbadvancedroperties");
		pageInfo.setRendered(true);
		pageInfo.setTitlebar(bar);
		
		HtmlOutputLabel emptyLabelLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		emptyLabelLabel.setValue("Empty element label");
		emptyLabelLabel.setFor("propertyEmptyLabel");
		pageInfo.getChildren().add(emptyLabelLabel);
		
		HtmlInputText emptyLabel = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		emptyLabel.setId("propertyEmptyLabel");
		emptyLabel.setValueBinding("value", application.createValueBinding("#{formComponent.emptyLabel}"));
		emptyLabel.setOnblur("");
		pageInfo.getChildren().add(emptyLabel);
		
		
		
		
		HtmlOutputLabel advancedL = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		advancedL.setValue("Select options properties");
		pageInfo.getChildren().add(advancedL);
		
		HtmlSelectOneRadio dataSrcSwitch = (HtmlSelectOneRadio) application.createComponent(HtmlSelectOneRadio.COMPONENT_TYPE);
		dataSrcSwitch.setStyleClass("inlineRadioButton");
		dataSrcSwitch.setId("dataSrcSwitch");
		dataSrcSwitch.setOnchange("switchDataSource()");
		dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{dataSources.selectedDataSource}"));

		UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
		dataSrcSwitch.getChildren().add(dataSrcs);
		
		/*UIAjaxSupport dataSrcsS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		dataSrcsS.setEvent("onchange");
		dataSrcsS.setReRender("workspaceform1:ajaxMenuPanel");
		dataSrcsS.setActionListener(application.createMethodBinding("#{dataSources.switchDataSource}", new Class[]{ActionEvent.class}));
		dataSrcsS.setAjaxSingle(true);
		dataSrcSwitch.getChildren().add(dataSrcsS);*/
		
		pageInfo.getChildren().add(dataSrcSwitch);
		
		FBDivision extSrcDiv = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		extSrcDiv.setId("extSrcDiv");
		
		HtmlOutputLabel externalSrcLabel = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		externalSrcLabel.setValue("External data source");
		externalSrcLabel.setFor("propertyExternal");
		extSrcDiv.getChildren().add(externalSrcLabel);
		
		HtmlInputText external = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		external.setId("propertyExternal");
		external.setValueBinding("value", application.createValueBinding("#{formComponent.externalSrc}"));
		emptyLabel.setOnblur("");
		extSrcDiv.getChildren().add(external);
		
		FBDivision localSrcDiv = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		localSrcDiv.setId("localSrcDiv");
		
		FBSelectValuesList selectValues = (FBSelectValuesList) application.createComponent(FBSelectValuesList.COMPONENT_TYPE);
		selectValues.setValueBinding("itemSet", application.createValueBinding("#{formComponent.items}"));
		selectValues.setId("selectOpts");
		localSrcDiv.getChildren().add(selectValues);
		
		addFacet(WFBLOCK_CONTENT_FACET, pageInfo);
		addFacet(LOCAL_SRC_FACET, localSrcDiv);
		addFacet(EXTERNAL_SRC_FACET, extSrcDiv);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		Integer current;
		if (!isRendered()) {
			return;
		}
		if(!((FormComponent) WFUtil.getBeanInstance("formComponent")).isSimple()) {
			UIComponent body = getFacet(WFBLOCK_CONTENT_FACET);
			if(body != null) {
				renderChild(context, body);
			}
			
			current = ((IComponentPropertiesSelect)((FormComponent) WFUtil.getBeanInstance("formComponent")).getProperties()).getDataSrcUsed();
			if(current == IComponentPropertiesSelect.LOCAL_DATA_SRC) {
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
