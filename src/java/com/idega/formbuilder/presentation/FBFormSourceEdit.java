package com.idega.formbuilder.presentation;

import java.io.IOException;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.component.html.ext.HtmlCommandButton;
import org.apache.myfaces.component.html.ext.HtmlInputTextarea;

import com.idega.webface.WFContainer;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBFormSourceEdit extends WFContainer {
	
	public static final String COMPONENT_TYPE = "FBFormSourceEdit";
	private static final String COMPONENT_FAMILY = "formbuilder";
	private static final String value_att = "value";
	private static final String FORM_CONTAINER = "FORM_CONTAINER";
	private static final String container_style = "com_idega_formbuilder_presentation_FBFormSourceEdit_container";

	@Override
	protected void initializeComponent(FacesContext ctx) {
		
		Application app = ctx.getApplication();
		
		HtmlInputTextarea textarea = new HtmlInputTextarea();
		textarea.setRendered(true);
		textarea.setValueBinding(value_att, app.createValueBinding("#{formSourceCodeManager.sourceCode}"));
		textarea.setWrap("false");
		
		HtmlCommandButton src_submit = new HtmlCommandButton();
		src_submit.setRendered(true);
		src_submit.setActionListener(app.createMethodBinding("#{formSourceCodeManager.processAction}", new Class[]{ActionEvent.class}));
		src_submit.setValue("change source code");
		
		WFContainer form_container = new WFContainer();
		form_container.setStyleClass(container_style);
		List<UIComponent> container_children = form_container.getChildren();
		container_children.add(textarea);
		container_children.add(src_submit);
		
		getFacets().put(FORM_CONTAINER, form_container);
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
		
	@Override
	public void encodeChildren(FacesContext ctx) throws IOException {
		
		super.encodeChildren(ctx);
		
		UIComponent form_container = getFacet(FORM_CONTAINER);
		
		if(form_container != null) {
			
			form_container.setRendered(true);
			renderChild(ctx, form_container);
		}
	}
	
	@Override
	public String getRendererType() {
		return null;
	}
}