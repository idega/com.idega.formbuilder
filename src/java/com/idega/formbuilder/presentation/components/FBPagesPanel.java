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
	private String componentStyleClass;

	public FBPagesPanel() {
		super();
		this.setRendererType(null);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		Application application = context.getApplication();
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", id, "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		
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
			formPage.setStyleClass(componentStyleClass);
			add(formPage);
		}
		this.getChildren().remove(0);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
		
		Object values[] = new Object[2];
		values[0] = id;
		values[1] = componentStyleClass;
		writer.write(getEmbededJavascript(values));
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	private String getEmbededJavascript(Object values[]) {
		return 	"<script language=\"JavaScript\">\n"
		
				+ "function setupPagesDragAndDrop() {\n"
				+ "Position.includeScrollOffsets = true;\n"
				+ "Sortable.create(\"" + values[0] + "\",{dropOnEmpty:true,tag:\"div\",only:\"" + values[1] + "\",onUpdate:rearrangePages,scroll:\"" + values[0] + "\",constraint:false});\n"
				+ "}\n"
				
				+ "function rearrangePages() {\n"
//				+ "var componentIDs = Sortable.serialize(\"" + values[2] + "\",{tag:\"div\",name:\"id\"});\n"
//				+ "var delimiter = '&id[]=';\n"
//				+ "var idPrefix = 'fbcomp_';\n"
//				+ "dwrmanager.updateComponentList(updateOrder,componentIDs,idPrefix,delimiter);\n"
//				+ "pressedDelete = true;\n"
				+ "}\n"
				
//				+ "function updateOrder() {}\n"
				
				+ "setupPagesDragAndDrop();\n"
				
				+ "</script>\n";
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context); 
		values[1] = id;
		values[2] = styleClass;
		values[3] = pages;
		values[4] = componentStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		pages = (ArrayList<String>) values[3];
		componentStyleClass = (String) values[4];
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

	public String getComponentStyleClass() {
		return componentStyleClass;
	}

	public void setComponentStyleClass(String componentStyleClass) {
		this.componentStyleClass = componentStyleClass;
	}

}
