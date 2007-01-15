package com.idega.formbuilder.presentation;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.ajax.UIAjaxCommandLink;
import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.presentation.IWBaseComponent;

public class FBMenuPanel extends IWBaseComponent {

	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "MenuPanel";
	
	private static final String COMPONENT_EMPTY_BODY_FACET = "COMPONENT_EMPTY_BODY_FACET";
	private static final String COMPONENT_HEADER_FACET = "COMPONENT_HEADER_FACET";
	private static final String COMPONENT_BODY_FACET = "COMPONENT_BODY_FACET";
	
	private String styleClass;
	private boolean expanded;
	private String title;
	private String reRender;
	
	public String getReRender() {
		return reRender;
	}

	public void setReRender(String reRender) {
		this.reRender = reRender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public FBMenuPanel() {
		super();
		this.setRendererType(null);
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getFamily() {
		return FBMenuPanel.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();
		FBDivision emptyPanel = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		
		HtmlOutputText out = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		out.setValue("Empty panel");
		emptyPanel.getChildren().add(out);
		
		FBDivision headerPanel = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		headerPanel.setStyleClass("menuPanelTitle");
		headerPanel.setId(getId() + "HP");
		
		UIAjaxCommandLink header = (UIAjaxCommandLink) application.createComponent(UIAjaxCommandLink.COMPONENT_TYPE);
		header.setId(getId() + "Title");
		MethodBinding mb = application.createMethodBinding("#{switchMenuAction.processAction}", new Class[]{ActionEvent.class});
		header.setActionListener(mb);
		header.setAjaxSingle(true);
		header.setReRender("mainApplication");
		
		header.setValueBinding("value", this.getValueBinding("title"));
		
		headerPanel.getChildren().add(header);
		
		FBDivision bodyPanel = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		
		this.getFacets().put(COMPONENT_HEADER_FACET, headerPanel);
		this.getFacets().put(COMPONENT_EMPTY_BODY_FACET, emptyPanel);
		this.getFacets().put(COMPONENT_BODY_FACET, bodyPanel);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		UIComponent header = (UIComponent) this.getFacets().get(COMPONENT_HEADER_FACET);
		if(header != null) {
			this.renderChild(context, header);
		}
		if(isExpanded()) {
			Iterator it = getChildren().iterator();
			while(it.hasNext()) {
				this.renderChild(context, (UIComponent) it.next());
			}
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		
		if (!isRendered()) {
			return;
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		//values[1] = id;
		values[2] = styleClass;
		values[3] = expanded;
		values[4] = title;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		//id = (String) values[1];
		styleClass = (String) values[2];
		expanded = (Boolean) values[3];
		title = (String) values[4];
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
