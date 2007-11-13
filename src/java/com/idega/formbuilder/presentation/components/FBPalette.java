package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Palette;
import com.idega.formbuilder.presentation.beans.PaletteComponent;
import com.idega.formbuilder.presentation.beans.ProcessPalette;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPalette extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Palette";
	
	private String itemStyleClass;
	
//	private static final String DEFAULT_COMPONENT_DRAG_ACTION = "handleComponentDrag";
//	private static final String DEFAULT_BUTTON_DRAG_ACTION = "handleButtonDrag";
	private static final String COMPONENT_CATEGORY = "fbcomp";
	private static final String BUTTON_CATEGORY = "fbbutton";
	private static final String PROCESS_CATEGORY = "fbprocess";
	private static final String BODY_ID = "firstList";
	private static final String MAIN_PALETTE_ID = "mainPalette";
	private static final String PALETTE_BODY_ID = "paletteBody";
	private static final String TAB1_ID = "paletteBody_1";
	private static final String TAB2_ID = "paletteBody_2";
	private static final String TAB3_ID = "paletteBody_3";
	private static final String fb_palette_basic = "fb_palette_basic";
	private static final String fb_palette_buttons = "fb_palette_buttons";
	private static final String fb_palette_text = "fb_palette_text";
	private static final String fb_palette_components = "fb_palette_components";
	private static final String datatype_legend_container_class = "datatypeLegenContainer";
	private static final String datatype_group_class = "datatypeGroup";
	private static final String datatype_group_header_container_class = "datatypeGroupHeaderContainer";
	private static final String datatype_group_header_class = "datatypeGroupHeader";
	private static final String datatype_group_components_class = "datatypeGroupComponents";
	private static final String palette_row_class = "paletteRow";
	private static final String palette_row_left_class = "left";
	private static final String palette_row_right_class = "right";
	

	public FBPalette() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		
		Layer body = new Layer(Layer.DIV);
		body.setId(BODY_ID);
		body.setStyleClass(getStyleClass());
		
		Layer mainPalette = new Layer(Layer.DIV);
		mainPalette.setId(MAIN_PALETTE_ID);
		body.add(mainPalette);
		
		Workspace workspace = (Workspace) WFUtil.getBeanInstance(Workspace.BEAN_ID);
		boolean processMode = workspace.isProcessMode();
		
		if(false) {
			
			ProcessPalette processPalette = (ProcessPalette) WFUtil.getBeanInstance(ProcessPalette.BEAN_ID);
			
			Link tab = new Link(getLocalizedString(iwc, fb_palette_components, "Components"));
			tab.setNoURL();
			mainPalette.add(tab);
			tab = new Link(getLocalizedString(iwc, fb_palette_buttons, "Buttons"));
			tab.setNoURL();
			mainPalette.add(tab);
			tab = new Link(getLocalizedString(iwc, fb_palette_text, "Text"));
			tab.setNoURL();
			mainPalette.add(tab);
			
			Layer paletteBody = new Layer(Layer.DIV);
			paletteBody.setId(PALETTE_BODY_ID);
			body.add(paletteBody);
			
			paletteBody.add(getProcessComponentsTab(TAB1_ID, context, processPalette));
			paletteBody.add(getTab(TAB2_ID, context, processPalette.getButtons(), BUTTON_CATEGORY));
			paletteBody.add(getTab(TAB3_ID, context, processPalette.getPlain(), COMPONENT_CATEGORY));
		} else {
			Palette palette = (Palette) WFUtil.getBeanInstance(Palette.BEAN_ID);
			
			Link tab = new Link(getLocalizedString(iwc, fb_palette_basic, "Basic"));
			tab.setNoURL();
			mainPalette.add(tab);
			tab = new Link(getLocalizedString(iwc, fb_palette_buttons, "Buttons"));
			tab.setNoURL();
			mainPalette.add(tab);
			tab = new Link(getLocalizedString(iwc, fb_palette_text, "Text"));
			tab.setNoURL();
			mainPalette.add(tab);
			
			Layer paletteBody = new Layer(Layer.DIV);
			paletteBody.setId(PALETTE_BODY_ID);
			body.add(paletteBody);
			
			paletteBody.add(getTab(TAB1_ID, context, palette.getBasic(), COMPONENT_CATEGORY));
			paletteBody.add(getTab(TAB2_ID, context, palette.getButtons(), BUTTON_CATEGORY));
			paletteBody.add(getTab(TAB3_ID, context, palette.getPlain(), COMPONENT_CATEGORY));
		}
		
		add(body);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		if (!isRendered()) {
			return;
		}
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			renderChild(context, (UIComponent) it.next());
		}
	}
	
	private Layer getProcessComponentsTab(String tab_id, FacesContext context, ProcessPalette processPalette) {
		Layer tab = new Layer(Layer.DIV);
		tab.setId(tab_id);
		
		Set<String> datatypes = processPalette.getDatatypes();
		for(Iterator<String> it = datatypes.iterator(); it.hasNext(); ) {
			String datatype = it.next();
			tab.add(getComponentDatatypeGroup(datatype, context, processPalette.getComponents(datatype)));
		}
		
		Layer legenContainer = new Layer(Layer.DIV);
		legenContainer.setStyleClass(datatype_legend_container_class);
		
		tab.add(legenContainer);
		
		return tab;
	}
	
	private Layer getComponentDatatypeGroup(String datatype, FacesContext context, List<PaletteComponent> components) {
		Layer tab = new Layer(Layer.DIV);
		tab.setStyleClass(datatype_group_class);
		tab.setId(datatype + "_" + datatype_group_class);
		
		Layer heading = new Layer(Layer.DIV);
		heading.setStyleClass(datatype_group_header_container_class);
		
		Text header = new Text(datatype);
		header.setStyleClass(datatype_group_header_class);
		heading.add(header);
		
		FBDatatypeVariables variableContainer = new FBDatatypeVariables();
		variableContainer.setDatatype(datatype);
		
		Layer componentContainer = new Layer(Layer.DIV);
		componentContainer.setStyleClass(datatype_group_components_class);
		
		Iterator<PaletteComponent> it2 = components.iterator();
		while(it2.hasNext()) {
			Layer row = new Layer(Layer.DIV);
			row.setStyleClass(palette_row_class);
			if(it2.hasNext()) {
				PaletteComponent current = it2.next();
				
				FBPaletteComponent formComponent = new FBPaletteComponent();
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				formComponent.setIcon(current.getIconPath());
//				formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
				formComponent.setCategory(PROCESS_CATEGORY);
				
				formComponent.setStyleClass(itemStyleClass + " " + palette_row_left_class);
				row.add(formComponent);
			}
			if(it2.hasNext()) {
				PaletteComponent current = it2.next();
				
				FBPaletteComponent formComponent = new FBPaletteComponent();
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				formComponent.setIcon(current.getIconPath());
//				formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
				formComponent.setCategory(PROCESS_CATEGORY);
				
				formComponent.setStyleClass(itemStyleClass + " " + palette_row_right_class);
				row.add(formComponent);
			}
			componentContainer.add(row);
		}
		
		tab.add(heading);
		tab.add(variableContainer);
		tab.add(componentContainer);
		
		return tab;
	}
	
	private Layer getTab(String tab_id, FacesContext context, List<PaletteComponent> components, String category) {
		Layer tab = new Layer(Layer.DIV);
		tab.setId(tab_id);
		
		for(Iterator<PaletteComponent> it = components.iterator(); it.hasNext(); ) {
			Layer row = new Layer(Layer.DIV);
			row.setStyleClass(palette_row_class);
			if(it.hasNext()) {
				PaletteComponent current = it.next();
				
				FBPaletteComponent formComponent = new FBPaletteComponent();
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				formComponent.setIcon(current.getIconPath());
				formComponent.setCategory(category);
//				if(category.equals(BUTTON_CATEGORY)) {
//					formComponent.setOnDrag(DEFAULT_BUTTON_DRAG_ACTION);
//				} else {
//					formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
//				}
				
				formComponent.setStyleClass(itemStyleClass + " " + palette_row_left_class);
				row.add(formComponent);
			}
			if(it.hasNext()) {
				PaletteComponent current = it.next();
				
				FBPaletteComponent formComponent = new FBPaletteComponent();
				formComponent.setName(current.getName());
				formComponent.setType(current.getType());
				formComponent.setIcon(current.getIconPath());
				formComponent.setCategory(category);
//				if(category.equals(BUTTON_CATEGORY)) {
//					formComponent.setOnDrag(DEFAULT_BUTTON_DRAG_ACTION);
//				} else {
//					formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
//				}
				
				formComponent.setStyleClass(itemStyleClass + " " + palette_row_right_class);
				row.add(formComponent);
			}
			tab.add(row);
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
