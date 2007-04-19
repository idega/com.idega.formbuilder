package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBPaletteComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PaletteComponent";
	
	private String name;
	private String autofill_key;
	private String type;
	private String icon;
	private String onDrag;
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOnDrag() {
		return onDrag;
	}

	public void setOnDrag(String onDrag) {
		this.onDrag = onDrag;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public FBPaletteComponent() {
		super();
		setRendererType(null);
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", this);
		writer.writeAttribute("class", getStyleClass() + " " + category, "styleClass");
		ValueBinding vb = getValueBinding("type");
		if(vb != null) {
			type = (String) vb.getValue(context);	
		}
		writer.writeAttribute("id", type, "type");
		writer.startElement("IMG", null);
		vb = getValueBinding("icon");
		if(vb != null) {
			icon = (String) vb.getValue(context);
		}
		writer.writeAttribute("src", icon, "icon");
		writer.endElement("IMG");
		
		writer.startElement("SPAN", null);
		vb = getValueBinding("name");
		if(vb != null) {
			name = (String) vb.getValue(context);
		}
		writer.writeText(name, null);
		writer.endElement("SPAN");
		writer.endElement("DIV");
		writer.write(getEmbededJavascript());
	}
	
	public String getEmbededJavascript() {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
//		result.append("$('" + type + "').draggableTag($$('.dropBox'), null, '" + category + "');\n");
		/*if(autofill_key == null) {
			result.append("dndMgr.registerDraggable( new FBDraggable($('" + type + "'), '" + type + "', '" + category + "', null) );\n");
		} else {
			result.append("dndMgr.registerDraggable( new FBDraggable($('" + type + "'), '" + type + "', '" + category + "', '"+autofill_key+"') );\n");
		}*/
		result.append("</script>\n");
		return result.toString();
		
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[7];
		values[0] = super.saveState(context);
		values[1] = name;
		values[2] = type;
		values[3] = icon;
		values[4] = onDrag;
		values[5] = category;
		values[6] = autofill_key;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		name = (String) values[1];
		type = (String) values[2];
		icon = (String) values[3];
		onDrag = (String) values[4];
		category = (String) values[5];
		autofill_key = (String) values[6];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAutofillKey() {
		return autofill_key;
	}

	public void setAutofillKey(String autofill_key) {
		this.autofill_key = autofill_key;
	}
}