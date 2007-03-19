package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Palette;
import com.idega.formbuilder.presentation.beans.PaletteComponent;
import com.idega.webface.WFUtil;

public class FBPalette extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Palette";
	
	private String itemStyleClass;
	private Integer columns;
	private List items = new LinkedList();
	
	private List<FBPaletteComponent> basic = new LinkedList<FBPaletteComponent>();
	private List<FBPaletteComponent> buttons = new LinkedList<FBPaletteComponent>();
	private List<FBPaletteComponent> plain = new LinkedList<FBPaletteComponent>();
	private List<FBPaletteComponent> autofilled = new LinkedList<FBPaletteComponent>();

	public FBPalette() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		Palette palette = (Palette) WFUtil.getBeanInstance("palette");
		
		Iterator it = palette.getBasic().iterator();
		while(it.hasNext()) {
			PaletteComponent current = (PaletteComponent) it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass);
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag("handleComponentDrag");
			formComponent.setCategory("fbcomp");
			basic.add(formComponent);
		}
		
		it = palette.getButtons().iterator();
		while(it.hasNext()) {
			PaletteComponent current = (PaletteComponent) it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass + "Bt");
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag("handleButtonDrag");
			formComponent.setCategory("fbbutton");
			buttons.add(formComponent);
		}
		it = palette.getPlain().iterator();
		while(it.hasNext()) {
			PaletteComponent current = (PaletteComponent) it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass);
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag("handleComponentDrag");
			formComponent.setCategory("fbcomp");
			plain.add(formComponent);
		}
		
		it = palette.getAutofilled().iterator();
		while(it.hasNext()) {
			PaletteComponent current = (PaletteComponent) it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass);
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag("handleComponentDrag");
			formComponent.setCategory("fbcomp");
			autofilled.add(formComponent);
		}
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", "mainPalette", null);
		writer.startElement("A", null);
		writer.writeText("Basic", null);
		writer.endElement("A");
		writer.startElement("A", null);
		writer.writeText("Buttons", null);
		writer.endElement("A");
		writer.startElement("A", null);
		writer.writeText("Text", null);
		writer.endElement("A");
		writer.startElement("A", null);
		writer.writeText("Custom", null);
		writer.endElement("A");
		writer.endElement("DIV");	
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", "paletteBody", null);
		
		addTab("paletteBody_1", context, basic);
		addTab("paletteBody_2", context, buttons);
		addTab("paletteBody_3", context, plain);
		addTab("paletteBody_4", context, autofilled);
		
		writer.endElement("DIV");
		writer.endElement("DIV");
		writer.write(getEmbededJavascript());
	}
	
	private void addTab(String tab_id, FacesContext context, List<FBPaletteComponent> components) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();
		
		writer.startElement("DIV", null);
		writer.writeAttribute("id", tab_id, null);
		
		writer.startElement("TABLE", null);
		
		int count = 1;
		boolean inRow = false;
		
		Iterator it = components.iterator();
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
		writer.endElement("TABLE");
		writer.endElement("DIV");
	}
	
	private String getEmbededJavascript() {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		result.append("initMenu();\n");
		result.append("</script>\n");
		return result.toString();
		
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
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = itemStyleClass;
		values[2] = columns;
		values[3] = items;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		itemStyleClass = (String) values[1];
		columns = (Integer) values[2];
		items = (List) values[3];
	}

}
