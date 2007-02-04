package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.ajax4jsf.ajax.html.HtmlAjaxCommandLink;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBMenuPanel extends FBComponentBase {

	public static final String COMPONENT_TYPE = "MenuPanel";
	
	private static final String COMPONENT_HEADER_FACET = "COMPONENT_HEADER_FACET";
	
	private String id;
	private String styleClass;
	private boolean expanded;
	private String title;

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
	
	protected void initializeComponent(FacesContext context) {		
		Application application = context.getApplication();

		HtmlAjaxCommandLink header = new HtmlAjaxCommandLink();
		header.setId(getId() + "Title");
		header.setOnclick("changeMenu(this.id)");
		header.setAjaxSingle(true);
		header.setReRender("workspaceform1:ajaxMenuPanel");
		header.setValueBinding("value", this.getValueBinding("title"));
		
//		HtmlOutputText header = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
//		header.setId(id + "Title");
//		header.setValueBinding("value", getValueBinding("title"));
//		header.setStyleClass("menuPanelTitle");
//		
//		HtmlAjaxSupport headerS = new HtmlAjaxSupport();
//		header.setId(id + "TitleS");
//		headerS.setEvent("onclick");
//		headerS.setAjaxSingle(false);
//		headerS.setReRender("mainApplication");
//		headerS.setActionListener(application.createMethodBinding("#{workspace.changeMenu}", new Class[]{ActionEvent.class}));
//		header.getChildren().add(headerS);
//		addChild(headerS, header);
		
		FBDivision headerPanel = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		headerPanel.setStyleClass("menuPanelTitle");
		headerPanel.setId(getId() + "HP");
//		headerPanel.getChildren().add(header);
		addChild(header, headerPanel);
		
		addFacet(COMPONENT_HEADER_FACET, headerPanel);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		UIComponent header = getFacet(COMPONENT_HEADER_FACET);
		if(header != null) {
			renderChild(context, header);
		}
		if(isExpanded()) {
			Iterator it = getChildren().iterator();
			while(it.hasNext()) {
				UIComponent current = (UIComponent) it.next();
				if(current != null) {
					renderChild(context, current);
				}
			}
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = expanded;
		values[4] = title;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
