package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.TextInput;

public class FBNewFormComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "NewFormComponent";
	
	private static final String CONTENT_DIV_FACET = "CONTENT_DIV_FACET";
	private static final String COMPONENT_ID = "newFormComp";
	private static final String NEW_ICON_ID = "newIcon";
	private static final String NEW_INPUT_ID = "newTxt";
	private static final String NEW_LINK_ID = "newBt";
	private static final String OK_LINK_ID = "okBt";
	private static final String CANCEL_LINK_ID = "cancelBt";
	private static final String NEW_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/images/new_form_icon.gif";
	
	public FBNewFormComponent() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		IWContext iwc = IWContext.getIWContext(context);
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(getStyleClass());
		body.setId(COMPONENT_ID);
		
		Layer innerLayer = new Layer(Layer.DIV);
		
		Image newIcon = new Image();
		newIcon.setSrc(NEW_ICON);
		newIcon.setId(NEW_ICON_ID);
		
		TextInput formName = new TextInput();
		formName.setId(NEW_INPUT_ID);
		
		Link newButton = new Link(getLocalizedString(iwc, "fb_home_new_link", "New form"));
		newButton.setId(NEW_LINK_ID);
		
		Link okButton = new Link(getLocalizedString(iwc, "fb_home_create_link", "Create"));
		okButton.setId(OK_LINK_ID);
		
		Link cancelButton = new Link(getLocalizedString(iwc, "fb_home_cancel_link", "Cancel"));
		cancelButton.setId(CANCEL_LINK_ID);
		
		innerLayer.add(newIcon);
		innerLayer.add(formName);
		innerLayer.add(newButton);
		innerLayer.add(okButton);
		innerLayer.add(cancelButton);
		body.add(innerLayer);
		
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
}
