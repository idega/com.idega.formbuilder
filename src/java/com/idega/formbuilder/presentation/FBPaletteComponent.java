package com.idega.formbuilder.presentation;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.presentation.IWBaseComponent;

public class FBPaletteComponent extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	public static final String COMPONENT_TYPE = "PaletteComponent";
	
	private static final String INLINE_DIV_STYLE = "display: inline;";
	
	private String id;
	private String styleClass;
	private String name;
	private String type;
	private String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public FBPaletteComponent() {
		super();
		this.setRendererType(null);
	}
	
	public String getFamily() {
		return FBPaletteComponent.COMPONENT_FAMILY;
	}

	public String getRendererType() {
		return null;
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("DIV", this);
		writer.writeAttribute("class", styleClass, "styleClass");
		ValueBinding vb = getValueBinding("type");
		String type = null;
		if(vb != null) {
			type = (String) vb.getValue(context);	
		} else {
			type = getType();
		}
		writer.writeAttribute("id", type, "type");
		writer.startElement("IMG", null);
		writer.writeAttribute("style", INLINE_DIV_STYLE, null);
		vb = getValueBinding("icon");
		String icon = null;
		if(vb != null) {
			icon = (String) vb.getValue(context);
		} else {
			icon = getIcon();
		}
		writer.writeAttribute("src", icon, "icon");
		writer.endElement("IMG");
		
		writer.startElement("P", null);
		writer.writeAttribute("style", INLINE_DIV_STYLE, null);
		vb = getValueBinding("name");
		String name = null;
		if(vb != null) {
			name = (String) vb.getValue(context);
		} else {
			name = getName();
		}
		writer.writeText(name, null);
		writer.endElement("P");
		writer.endElement("DIV");
		writer.write(getEmbededJavascript(type));
	}
	
	public static String getEmbededJavascript(String id) {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		result.append("new Draggable(\"" + id + "\", {tag:\"div\",starteffect:handleComponentDrag,revert:true});\n");
		result.append("</script>\n");
		return result.toString();
		
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[6];
		values[0] = super.saveState(context);
		values[1] = styleClass;
		values[2] = id;
		values[3] = name;
		values[4] = type;
		values[5] = icon;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		styleClass = (String) values[1];
		id = (String) values[2];
		name = (String) values[3];
		type = (String) values[4];
		icon = (String) values[5];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
