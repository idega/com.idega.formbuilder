package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.TextInput;
import com.idega.util.RenderUtils;

public class FBInlineEdit extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "InlineEdit";
	
	private static final String EDIT_ICON = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit_16.png";
	
	private boolean idle;
	private String text;
	private String action;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public FBInlineEdit() {
		this(true, null);
	}
	
	public FBInlineEdit(boolean idle, String text) {
		super();
		setRendererType(null);
		this.idle = idle;
		this.text = text;
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass("inlineEditBox");
		
		if(idle) {
			body.setId("inline-" + getId());
			Label assignLabel = new Label();
			assignLabel.setLabel(text);
			assignLabel.setOnClick("reloadInlineEdit(false, event);return false;");
			
			Image icon = new Image();
			icon.setSrc(EDIT_ICON);
			icon.setOnClick("reloadInlineEdit(true, event);return false;");
			
			body.add(assignLabel);
			body.add(icon);
		} else {
			body.setId("inline-" + getId());
			
			TextInput input = new TextInput();
			input.setValue(text);
				
			body.add(input);
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

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}

}
