package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.util.FBConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SelectDropdown;
import com.idega.presentation.ui.TextInput;
import com.idega.util.RenderUtils;

public class FBNewFormComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "NewFormComponent";
	
	private static final String NEW_ICON_ID = "newIcon";
	private static final String NEW_INPUT_ID = "newTxt";
	private static final String NEW_LINK_ID = "newBt";
	private static final String OK_LINK_ID = "okBt";
	private static final String CANCEL_LINK_ID = "cancelBt";
	private static final String NEW_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/images/new_form_icon.gif";
	private static final String FULL_PANEL_CLASS = "fbFullPanel";
	
	private boolean compact;
	
	public FBNewFormComponent() {
		super(null, FULL_PANEL_CLASS);
		setRendererType(null);
		this.compact = false;
	}
	
	private Layer createPropertyContainer(String styleClass) {
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(styleClass);
		body.setStyleClass("fbProperty");
		
		return body;
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		IWContext iwc = IWContext.getIWContext(context);
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(getStyleClass());
		body.setId(getId() + "Box");
		
		if(compact) {
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
		} else {
			Layer externalline = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
			
			SelectDropdown select = new SelectDropdown();
			select.setId("propertyTask");
			select.setOnChange("");
			
			externalline.add(new Label(getLocalizedString(iwc, "fb_home_process_task", "Process task"), select));
			externalline.add(select);
			body.add(externalline);
			
			Layer line = createPropertyContainer(FBConstants.SINGLE_LINE_PROPERTY);
			TextInput labelValue = new TextInput("propertyName", "");
			line.add(new Label(getLocalizedString(iwc, "fb_home_form_name", "Form name"), labelValue));
			line.add(labelValue);
			body.add(line);
		}
		add(body);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}

	public boolean isCompact() {
		return compact;
	}

	public void setCompact(boolean compact) {
		this.compact = compact;
	}
}
