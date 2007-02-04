package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.PaletteComponent;

public class FBPalette extends FBComponentBase {
	
//	public static final String RENDERER_TYPE = "fb_palette";
//	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "Palette";
	
	private String styleClass;
	private String itemStyleClass;
	private Integer columns;
	private List items = new ArrayList();

	public FBPalette() {
		super();
		this.setRendererType(null);
	}
	
	/*public String getFamily() {
		return FBPalette.COMPONENT_FAMILY;
	}
	
	public String getRendererType() {
		return null;
	}
	
	public boolean getRendersChildren() {
		return true;
	}*/
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		this.getChildren().clear();
		
		ValueBinding vb = this.getValueBinding("items");
		if(vb != null) {
			List items = (List) vb.getValue(context);
			Iterator it = items.iterator();
			while(it.hasNext()) {
				PaletteComponent current = (PaletteComponent) it.next();
				FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
				formComponent.setStyleClass(itemStyleClass);
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				formComponent.setIcon(current.getIconPath());
				add(formComponent);
			}
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", styleClass, "styleClass");
		writer.startElement("TABLE", null);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		int count = 1;
		boolean inRow = false;
		
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			if((count % columns) == 1 || columns == 1) {
				writer.startElement("TR", null);
				inRow = true;
			}
			FBPaletteComponent current = (FBPaletteComponent) it.next();
			if(current != null) {
				writer.startElement("TD", null);
				current.encodeEnd(context);
				writer.endElement("TD");
			}
			if((count % columns) == 0 || columns == 1) {
				writer.endElement("TR");
				inRow = false;
			}
			count++;
		}
		if(inRow) {
			writer.endElement("TR");
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.endElement("TABLE");
		writer.endElement("DIV");
		
		super.encodeEnd(context);
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public String getItemStyleClass() {
		return itemStyleClass;
	}

	public void setItemStyleClass(String itemStyleClass) {
		this.itemStyleClass = itemStyleClass;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = itemStyleClass;
		values[3] = columns;
		values[4] = items;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		itemStyleClass = (String) values[2];
		columns = (Integer) values[3];
		items = (List) values[4];
	}

}
