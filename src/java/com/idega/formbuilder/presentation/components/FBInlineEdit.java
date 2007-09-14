package com.idega.formbuilder.presentation.components;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import com.idega.formbuilder.presentation.FBComponentBase;

public class FBInlineEdit extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "InlineEdit";
	
	private String value;
	private String onSelect;
	private String onBlur;
	private String onReturn;
	
	public String getOnBlur() {
		return onBlur;
	}

	public void setOnBlur(String onBlur) {
		this.onBlur = onBlur;
	}

	public String getOnReturn() {
		return onReturn;
	}

	public void setOnReturn(String onReturn) {
		this.onReturn = onReturn;
	}

	public FBInlineEdit() {
		super();
		setRendererType(null);
	}
	
	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		ValueBinding vb = getValueBinding("value");
		if(vb != null) {
			value = (String) vb.getValue(context);
		}
		
		
		writer.startElement("span", this);
		writer.writeAttribute("id", getId(), null);
		writer.writeAttribute("class", getStyleClass(), null);
		if(value != null) {
			writer.writeText(value, null);
		}
		writer.endElement("span");
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = value;
		values[2] = onSelect;
		values[3] = onBlur;
		values[4] = onReturn;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		value = (String) values[1];
		onSelect = (String) values[2];
		onBlur = (String) values[3];
		onReturn = (String) values[4];
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
