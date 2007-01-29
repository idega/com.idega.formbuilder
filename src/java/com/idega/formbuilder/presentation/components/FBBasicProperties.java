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
		required.setOnclick("$('workspaceform1:saveCompReq').click();");
		/*UIAjaxSupport requiredS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		requiredS.setEvent("onclick");
		requiredS.setReRender("mainApplication");
		requiredS.setActionListener(application.createMethodBinding("#{savePropertiesAction.processAction}", new Class[]{ActionEvent.class}));
		requiredS.setAjaxSingle(true);
		required.getChildren().add(requiredS);*/
		
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
		title.setOnblur("$('workspaceform1:saveCompLabel').click();");
		/*UIAjaxSupport titleS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		titleS.setEvent("onblur");
		titleS.setReRender("mainApplication");
		titleS.setActionListener(application.createMethodBinding("#{formComponent.saveComponentLabel}", new Class[]{ActionEvent.class}));
		titleS.setAjaxSingle(true);
		title.getChildren().add(titleS);*/
		
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
		errorMsg.setOnblur("$('workspaceform1:saveCompErr').click();");
		/*UIAjaxSupport errorMsgS = (UIAjaxSupport) application.createComponent("org.ajax4jsf.ajax.Support");
		errorMsgS.setEvent("onblur");
		errorMsgS.setReRender("mainApplication");
		errorMsgS.setActionListener(application.createMethodBinding("#{savePropertiesAction.processAction}", new Class[]{ActionEvent.class}));
		errorMsgS.setAction(application.createMethodBinding("#{savePropertiesAction.saveProperties}", null));
		errorMsgS.setAjaxSingle(false);
		errorMsgS.setOnsubmit("alert('SH')");
		errorMsgS.setOncomplete("alert('SH error done')");
		errorMsg.getChildren().add(errorMsgS);*/
		
		cell = row.createCell();
		cell.add(errorMsg);
		
		addFacet(CONTENT_FACET, table);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		UIComponent body = getFacet(CONTENT_FACET);
		if(body != null) {
			renderChild(context, body);
		}
	}
	
}
