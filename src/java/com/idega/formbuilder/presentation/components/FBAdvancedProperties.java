package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.presentation.IWBaseComponent;
import com.idega.webface.WFBlock;
import com.idega.webface.WFTitlebar;
import com.idega.webface.WFUtil;

public class FBAdvancedProperties extends IWBaseComponent {

	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "AdvancedProperties";
	
	private static final String WFBLOCK_CONTENT_FACET = "WFBLOCK_CONTENT_FACET";
	
	public FBAdvancedProperties() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBAdvancedProperties.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
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
//		emptyLabel.setOnblur("applyChanges()");
		emptyLabel.setValueBinding("value", application.createValueBinding("#{formComponent.emptyLabel}"));
		pageInfo.getChildren().add(emptyLabel);
		
		
		
		
		/*HtmlOutputLabel advancedL = (HtmlOutputLabel) application.createComponent(HtmlOutputLabel.COMPONENT_TYPE);
		advancedL.setValue("Select options properties");
		pageInfo.getChildren().add(advancedL);
		
		HtmlSelectOneRadio dataSrcSwitch = (HtmlSelectOneRadio) application.createComponent(HtmlSelectOneRadio.COMPONENT_TYPE);
		dataSrcSwitch.setStyleClass("inlineRadioButton");
		dataSrcSwitch.setOnchange("switchDataSrc()");
		dataSrcSwitch.setValueBinding("value", application.createValueBinding("#{dataSources.selectedDataSource}"));
		UISelectItems dataSrcs = (UISelectItems) application.createComponent(UISelectItems.COMPONENT_TYPE);
		dataSrcs.setValueBinding("value", application.createValueBinding("#{dataSources.sources}"));
		dataSrcSwitch.getChildren().add(dataSrcs);
		pageInfo.getChildren().add(dataSrcSwitch);*/
		
		this.getFacets().put(WFBLOCK_CONTENT_FACET, pageInfo);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		if(!((FormComponent) WFUtil.getBeanInstance("formComponent")).isSimple()) {
			UIComponent body = getFacet(WFBLOCK_CONTENT_FACET);
			if(body != null) {
				renderChild(context, body);
			}
		}
	}
	
	/*public Object saveState(FacesContext context) {
		Object values[] = new Object[1];
		values[0] = super.saveState(context);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
	}*/
	
}
