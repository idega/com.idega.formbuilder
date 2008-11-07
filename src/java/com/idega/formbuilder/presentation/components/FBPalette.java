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
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPalette extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Palette";
	
	private String itemStyleClass;
	
	private static final String BODY_ID = "firstList";
	private static final String MAIN_PALETTE_ID = "mainPalette";
	private static final String TAB1_ID = "paletteBody_1";
	private static final String TAB2_ID = "paletteBody_2";
	private static final String fb_palette_simple = "fb_palette_simple";
	private static final String fb_palette_advanced = "fb_palette_advanced";
	private static final String palette_row_class = "paletteRow";
	private static final String palette_row_left_class = "left";
	private static final String palette_row_right_class = "right";
	private static final String MOOTABS_TITLE_CLASS = "mootabs_title";
	private static final String TITLE_ATTRIBUTE = "title";
	private static final String PROCESS_TAB_TITLE = "processes";
	private static final String STANDALONE_TAB_TITLE = "standalone";
	private static final String MOOTABS_PANEL_CLASS = "mootabs_panel";
	private static final String LETTER_P = "p";
	

	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = new Layer(Layer.DIV);
		body.setId(BODY_ID);
		body.setStyleClass(getStyleClass());
		
		Lists tabsList = new Lists();
		tabsList.setStyleClass(MOOTABS_TITLE_CLASS);
		tabsList.setStyleClass(MAIN_PALETTE_ID);
		
		ListItem tab1 = new ListItem();
		tab1.setMarkupAttribute(TITLE_ATTRIBUTE, PROCESS_TAB_TITLE);
		tab1.add(new Text(getLocalizedString(iwc, fb_palette_simple, "Simple")));
		tab1.setStyleClass("stateFullTab");
		
		ListItem tab2 = new ListItem();
		tab2.add(new Text(getLocalizedString(iwc, fb_palette_advanced, "Advanced")));
		tab2.setMarkupAttribute(TITLE_ATTRIBUTE, STANDALONE_TAB_TITLE);
		tab2.setStyleClass("stateFullTab");
		
		tabsList.add(tab1);
		tabsList.add(tab2);
		
		body.add(tabsList);
		
		Layer tab1Forms = new Layer(Layer.DIV);
		tab1Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab1Forms.setId(PROCESS_TAB_TITLE);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		Palette palette = (Palette) WFUtil.getBeanInstance(Palette.BEAN_ID);
		
		tab1Forms.add(getTab(TAB1_ID, context, palette.getBasicComponents(), workspace.isProcessMode()));
		
		body.add(tab1Forms);
		
		Layer tab2Forms = new Layer(Layer.DIV);
		tab2Forms.setStyleClass(MOOTABS_PANEL_CLASS);
		tab2Forms.setId(STANDALONE_TAB_TITLE);
		
		tab2Forms.add(getTab(TAB2_ID, context, palette.getAdvancedComponents(), workspace.isProcessMode()));
		
		body.add(tab2Forms);
		
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
					row.add(getFBPaletteComponent(it.next(), processMode, false));
				}
				if(it.hasNext()) {
					row.add(getFBPaletteComponent(it.next(), processMode, true));
				}
				tab.add(row);
			}
			
		}
		
		return tab;
	}
	
	private FBPaletteComponent getFBPaletteComponent(PaletteComponent component, boolean processMode, boolean right) {
		FBPaletteComponent formComponent = new FBPaletteComponent();
		formComponent.setName(component.getName());
		formComponent.setType(component.getType());
		formComponent.setIcon(component.getIconPath());
		formComponent.setCategory(component.getCategory() + (processMode ? LETTER_P : CoreConstants.EMPTY));
		formComponent.setStyleClass(itemStyleClass + CoreConstants.SPACE + (right ? palette_row_right_class : palette_row_left_class));
		
		return formComponent;
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
