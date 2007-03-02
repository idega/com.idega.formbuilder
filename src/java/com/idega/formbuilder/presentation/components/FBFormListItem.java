package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.text.Text;
import com.idega.webface.WFDivision;

public class FBFormListItem extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormListItem";
	
	public static final String delete_button_postfix = "_delete";
	public static final String entries_button_postfix = "_entries";
	public static final String duplicate_button_postfix = "_duplicate";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private String formId;
	private String formTitle;
	
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public FBFormListItem() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision body = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		body.setStyleClass(getStyleClass());
		
		WFDivision bodyTop = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		bodyTop.setStyleClass("formListItemTop");
		
		WFDivision bodyTopLeft = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		bodyTopLeft.setStyleClass("formListItemTopLeft");
		
		WFDivision bodyTopRight = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		bodyTopRight.setStyleClass("formListItemTopRight");
		
		WFDivision bodyBottom = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		bodyBottom.setStyleClass("formListItemBottom");
		
		Text name = new Text();
		name.setText(formTitle);
		name.setStyleClass("formTitle");
		
		Text created = new Text();
		created.setText(formId);
		created.setStyleClass("createdDate");
		
		FBToolbarButton entriesButton = new FBToolbarButton();
		entriesButton.setStyleClass("bottomButton");
		entriesButton.setId(getId() + entries_button_postfix);
		entriesButton.setDisplayText("Entries");
		entriesButton.setDefaultImageURI("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/application_view_list.png");
		entriesButton.setAction(application.createMethodBinding("#{formDocument.loadFormDocumentEntries}", null));
		FBToolbarButton editButton = new FBToolbarButton();
		editButton.setStyleClass("bottomButton");
		editButton.setDisplayText("Edit");
		editButton.setId(getId() + "_edit");
		editButton.setDefaultImageURI("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/application_form_edit.png");
		editButton.setAction(application.createMethodBinding("#{formDocument.loadFormDocument}", null));
		FBToolbarButton codeButton = new FBToolbarButton();
		codeButton.setStyleClass("bottomButton");
		codeButton.setDisplayText("Code");
		codeButton.setId(getId() + "_code");
		codeButton.setDefaultImageURI("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/page_white_code.png");
		codeButton.setAction(application.createMethodBinding("#{formDocument.loadFormDocumentCode}", null));
		FBToolbarButton duplicateButton = new FBToolbarButton();
		duplicateButton.setStyleClass("bottomButton");
		duplicateButton.setDisplayText("Duplicate");
		duplicateButton.setId(getId() + duplicate_button_postfix);
		duplicateButton.setDefaultImageURI("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/page_copy.png");
		duplicateButton.setAction(application.createMethodBinding("#{formDocument.duplicateFormDocument}", null));
		FBToolbarButton deleteButton = new FBToolbarButton();
		deleteButton.setStyleClass("bottomButton");
		deleteButton.setDisplayText("Delete");
		deleteButton.setId(getId() + delete_button_postfix);
		deleteButton.setDefaultImageURI("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/bin_closed.png");
		deleteButton.setAction(application.createMethodBinding("#{formDocument.deleteFormDocument}", null));
		
		addChild(name, bodyTopLeft);
		addChild(bodyTopLeft, bodyTop);
		addChild(created, bodyTopRight);
		addChild(bodyTopRight, bodyTop);
		addChild(entriesButton, bodyBottom);
		addChild(editButton, bodyBottom);
		addChild(codeButton, bodyBottom);
		addChild(duplicateButton, bodyBottom);
		addChild(deleteButton, bodyBottom);
		addChild(bodyTop, body);
		addChild(bodyBottom, body);
		
		addFacet(CONTENT_DIV_FACET, body);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		UIComponent component = getFacet(CONTENT_DIV_FACET);
		if(component != null) {
			renderChild(context, component);
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = formId;
		values[2] = formTitle;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		formId = (String) values[1];
		formTitle = (String) values[2];
	}

}
