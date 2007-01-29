package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBPagesPanel extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PagesPanel";
	
	private String id;
	private String styleClass;
	private List<String> pages = new ArrayList<String>();

	public FBPagesPanel() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		/*FBDivision switcher = (FBDivision) application.createComponent(FBDivision.COMPONENT_TYPE);
		switcher.setId("pagesPanel");
		switcher.setStyleClass("pagesPanel");*/
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		writer.writeAttribute("style", "float: right", null);
		
		ValueBinding vb = this.getValueBinding("pages");
		if(vb != null) {
			pages = (ArrayList<String>) vb.getValue(context);
		}
		Iterator it = pages.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			FBFormPage formPage = (FBFormPage) application.createComponent(FBFormPage.COMPONENT_TYPE);
			formPage.setId(nextId);
			formPage.setLabel(nextId);
			add(formPage);
		}
		
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		super.encodeEnd(context);
		
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = this.getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context); 
		values[1] = id;
		values[2] = styleClass;
		values[3] = pages;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		pages = (ArrayList<String>) values[3];
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

	public List<String> getPages() {
		return pages;
	}

	public void setPages(List<String> pages) {
		this.pages = pages;
	}

}
