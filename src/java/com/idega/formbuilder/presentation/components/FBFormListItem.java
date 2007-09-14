package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

public class FBFormListItem extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "FormListItem";
	
	public static final String delete_button_postfix = "_delete";
	public static final String entries_button_postfix = "_entries";
	public static final String duplicate_button_postfix = "_duplicate";
	public static final String edit_button_postfix = "_edit";
	public static final String code_button_postfix = "_code";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	
	private String createdDate;
	private String formTitle;

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
		IWContext iwc = IWContext.getIWContext(context);
		getChildren().clear();
		
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(getStyleClass());
		body.setId("Item" + getId());
		
//		String formName = WFToolbarButton.determineFormName(this);
		
		Layer bodyTop = new Layer(Layer.DIV);
		bodyTop.setStyleClass("formListItemTop");
//		bodyTop.setOnclick("document.forms['" + formName + "'].elements['" + formName + ":" + getId() + edit_button_postfix + "'].value='true';document.forms['" + formName + "'].submit();");
		
		Layer bodyTopLeft = new Layer(Layer.DIV);
		bodyTopLeft.setStyleClass("formListItemTopLeft");
		
		Layer bodyTopRight = new Layer(Layer.DIV);
		bodyTopRight.setStyleClass("formListItemTopRight");
		
		Layer bodyBottom = new Layer(Layer.DIV);
		bodyBottom.setStyleClass("formListItemBottom");
		body.setId("ItemBottom" + getId());
		
		Text name = new Text(formTitle);
		name.setStyleClass("formTitle");
		
		Text created = new Text(createdDate);
		created.setStyleClass("createdDate");
		
		Link entriesButton = new Link(getLocalizedString(iwc, "fb_home_entries_link", "Entries"));
		entriesButton.setStyleClass("bottomButton entriesButton");
		entriesButton.setId(getId() + entries_button_postfix);
		
		Link editButton = new Link(getLocalizedString(iwc, "fb_home_edit_link", "Edit"));
		editButton.setStyleClass("bottomButton editButton");
		editButton.setId(getId() + edit_button_postfix);
		
		Link codeButton = new Link(getLocalizedString(iwc, "fb_home_code_link", "Code"));
		codeButton.setStyleClass("bottomButton codeButton");
		codeButton.setId(getId() + code_button_postfix);
		
		Link duplicateButton = new Link(getLocalizedString(iwc, "fb_home_duplicate_link", "Duplicate"));
		duplicateButton.setStyleClass("bottomButton duplicateButton");
		duplicateButton.setId(getId() + duplicate_button_postfix);
		
		Link deleteButton = new Link(getLocalizedString(iwc, "fb_home_delete_link", "Delete"));
		deleteButton.setStyleClass("bottomButton deleteButton");
		deleteButton.setId(getId() + delete_button_postfix);
		
		bodyTopLeft.add(name);
		bodyTop.add(bodyTopLeft);
		bodyTopRight.add(created);
		bodyTop.add(bodyTopRight);
		bodyBottom.add(entriesButton);
		bodyBottom.add(editButton);
		bodyBottom.add(codeButton);
		bodyBottom.add(duplicateButton);
		bodyBottom.add(deleteButton);
		body.add(bodyTop);
		body.add(bodyBottom);
		
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
		values[1] = createdDate;
		values[2] = formTitle;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		createdDate = (String) values[1];
		formTitle = (String) values[2];
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

}
