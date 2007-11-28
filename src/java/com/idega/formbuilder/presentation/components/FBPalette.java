package com.idega.formbuilder.presentation.components;

import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Palette;
import com.idega.formbuilder.presentation.beans.PaletteComponent;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPalette extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Palette";
	
	private String itemStyleClass;
	
//	private static final String COMPONENT_CATEGORY = "fbc";
//	private static final String BUTTON_CATEGORY = "fbbutton";
//	private static final String PROCESS_CATEGORY = "fbprocess";
	private static final String BODY_ID = "firstList";
	private static final String MAIN_PALETTE_ID = "mainPalette";
	private static final String PALETTE_BODY_ID = "paletteBody";
	private static final String TAB1_ID = "paletteBody_1";
	private static final String TAB2_ID = "paletteBody_2";
	private static final String fb_palette_simple = "fb_palette_simple";
	private static final String fb_palette_advanced = "fb_palette_advanced";
	private static final String palette_row_class = "paletteRow";
	private static final String palette_row_left_class = "left";
	private static final String palette_row_right_class = "right";
	

	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = new Layer(Layer.DIV);
		body.setId(BODY_ID);
		body.setStyleClass(getStyleClass());
		
		Layer mainPalette = new Layer(Layer.DIV);
		mainPalette.setId(MAIN_PALETTE_ID);
		body.add(mainPalette);
		
		Palette palette = (Palette) WFUtil.getBeanInstance(Palette.BEAN_ID);
			
		Link tab = new Link(getLocalizedString(iwc, fb_palette_simple, "Simple"));
		tab.setNoURL();
		mainPalette.add(tab);
		tab = new Link(getLocalizedString(iwc, fb_palette_advanced, "Advanced"));
		tab.setNoURL();
		mainPalette.add(tab);
			
		Layer paletteBody = new Layer(Layer.DIV);
		paletteBody.setId(PALETTE_BODY_ID);
		body.add(paletteBody);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
			
		paletteBody.add(getTab(TAB1_ID, context, palette.getAllComponents(), workspace.isProcessMode()));
		paletteBody.add(getTab(TAB2_ID, context, null, workspace.isProcessMode()));
		
		add(body);
	}
	
	private Layer getTab(String tab_id, FacesContext context, List<PaletteComponent> components, boolean processMode) {
		Layer tab = new Layer(Layer.DIV);
		tab.setId(tab_id);
		
		if(components != null) {
		
			for(Iterator<PaletteComponent> it = components.iterator(); it.hasNext(); ) {
				Layer row = new Layer(Layer.DIV);
				row.setStyleClass(palette_row_class);
				if(it.hasNext()) {
					PaletteComponent current = it.next();
					
					FBPaletteComponent formComponent = new FBPaletteComponent();
					formComponent.setName(current.getName());
					formComponent.setType(current.getType());
					formComponent.setIcon(current.getIconPath());
					formComponent.setCategory(current.getCategory() + (processMode ? "p" : ""));
					formComponent.setStyleClass(itemStyleClass + " " + palette_row_left_class);
					row.add(formComponent);
				}
				if(it.hasNext()) {
					PaletteComponent current = it.next();
					
					FBPaletteComponent formComponent = new FBPaletteComponent();
					formComponent.setName(current.getName());
					formComponent.setType(current.getType());
					formComponent.setIcon(current.getIconPath());
					formComponent.setCategory(current.getCategory() + (processMode ? "p" : ""));
					formComponent.setStyleClass(itemStyleClass + " " + palette_row_right_class);
					row.add(formComponent);
				}
				tab.add(row);
			}
			
		}
		
		return tab;
	}
	
	public String getItemStyleClass() {
		return itemStyleClass;
	}

	public void setItemStyleClass(String itemStyleClass) {
		this.itemStyleClass = itemStyleClass;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[2];
		values[0] = super.saveState(context);
		values[1] = itemStyleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		itemStyleClass = (String) values[1];
	}

}
