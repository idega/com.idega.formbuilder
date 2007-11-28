package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;

public class FBPaletteComponent extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PaletteComponent";
	
	private String name;
	private String type;
	private String icon;
	private String category;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	protected void initializeComponent(FacesContext context) {	
		Layer body = new Layer(Layer.DIV);
		body.setStyleClass(getStyleClass());
		body.setStyleClass(category);
		body.setId(type);
		
		Image iconImg = new Image();
		iconImg.setSrc(icon);
		
		Text label = new Text(name);
		
		body.add(iconImg);
		body.add(label);
		
		add(body);
	}

	public Object saveState(FacesContext context) {
		Object values[] = new Object[5];
		values[0] = super.saveState(context);
		values[1] = name;
		values[2] = type;
		values[3] = icon;
		values[4] = category;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		name = (String) values[1];
		type = (String) values[2];
		icon = (String) values[3];
		category = (String) values[4];
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

}