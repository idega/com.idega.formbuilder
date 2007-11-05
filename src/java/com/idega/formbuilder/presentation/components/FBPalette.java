package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.Palette;
import com.idega.formbuilder.presentation.beans.PaletteComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPalette extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "Palette";
	
	private String itemStyleClass;
	
	private List<FBPaletteComponent> basic = new LinkedList<FBPaletteComponent>();
	private List<FBPaletteComponent> buttons = new LinkedList<FBPaletteComponent>();
	private List<FBPaletteComponent> plain = new LinkedList<FBPaletteComponent>();
	private List<FBPaletteComponent> autofilled = new LinkedList<FBPaletteComponent>();
	
	private static final String DEFAULT_COMPONENT_DRAG_ACTION = "handleComponentDrag";
	private static final String DEFAULT_BUTTON_DRAG_ACTION = "handleButtonDrag";
	private static final String COMPONENT_CATEGORY = "fbcomp";
	private static final String BUTTON_CATEGORY = "fbbutton";

	public FBPalette() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();
		Application application = context.getApplication();
		Palette palette = (Palette) WFUtil.getBeanInstance("palette");
		
		Iterator<PaletteComponent> it = palette.getBasic().iterator();
		while(it.hasNext()) {
			PaletteComponent current = it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass);
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
			formComponent.setCategory(COMPONENT_CATEGORY);
			basic.add(formComponent);
		}
		it = palette.getButtons().iterator();
		while(it.hasNext()) {
			PaletteComponent current = it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass + "Bt");
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag(DEFAULT_BUTTON_DRAG_ACTION);
			formComponent.setCategory(BUTTON_CATEGORY);
			buttons.add(formComponent);
		}
		it = palette.getPlain().iterator();
		while(it.hasNext()) {
			PaletteComponent current = it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass);
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
			formComponent.setCategory(COMPONENT_CATEGORY);
			plain.add(formComponent);
		}
		
		it = palette.getAutofilled().iterator();
		while(it.hasNext()) {
			PaletteComponent current = it.next();
			FBPaletteComponent formComponent = (FBPaletteComponent) application.createComponent(FBPaletteComponent.COMPONENT_TYPE);
			formComponent.setStyleClass(itemStyleClass);
			formComponent.setName(current.getName());
			formComponent.setType(current.getType());
			formComponent.setIcon(current.getIconPath());
			formComponent.setOnDrag(DEFAULT_COMPONENT_DRAG_ACTION);
			formComponent.setCategory(COMPONENT_CATEGORY);
			formComponent.setAutofillKey(current.getAutofillKey());
			autofilled.add(formComponent);
		}
		
		Layer body = new Layer(Layer.DIV);
		body.setId("firstList");
		body.setStyleClass(getStyleClass());
		
		Layer mainPalette = new Layer(Layer.DIV);
		mainPalette.setId("mainPalette");
		body.add(mainPalette);
		
		Link tab = new Link(getLocalizedString(iwc, "fb_palette_basic", "Basic"));
		tab.setNoURL();
		mainPalette.add(tab);
		tab = new Link(getLocalizedString(iwc, "fb_palette_buttons", "Buttons"));
		tab.setNoURL();
		mainPalette.add(tab);
		tab = new Link(getLocalizedString(iwc, "fb_palette_text", "Text"));
		tab.setNoURL();
		mainPalette.add(tab);
		tab = new Link(getLocalizedString(iwc, "fb_palette_custom", "Custom"));
		tab.setNoURL();
		mainPalette.add(tab);
		
		Layer paletteBody = new Layer(Layer.DIV);
		paletteBody.setId("paletteBody");
		body.add(paletteBody);
		
//		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
//		boolean processMode = workspace.isProcessMode();
		
//		if(processMode) {
//			
//		} else {
			paletteBody.add(getTab("paletteBody_1", context, basic));
//		}
		paletteBody.add(getTab("paletteBody_2", context, buttons));
		paletteBody.add(getTab("paletteBody_3", context, plain));
//		if(!processMode) {
			paletteBody.add(getTab("paletteBody_4", context, autofilled));
//		}
		
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
	
	private Layer addProcessVariablesTab(String tab_id, FacesContext context) {
		Layer tab = new Layer(Layer.DIV);
		tab.setId(tab_id);
		return tab;
	}
	
	private Layer getTab(String tab_id, FacesContext context, List<FBPaletteComponent> components) {
		Layer tab = new Layer(Layer.DIV);
		tab.setId(tab_id);
		for(Iterator<FBPaletteComponent> it = components.iterator(); it.hasNext(); ) {
			Layer row = new Layer(Layer.DIV);
			row.setStyleClass("paletteRow");
			if(it.hasNext()) {
				FBPaletteComponent left = it.next();
				left.setStyleClass(left.getStyleClass() + " left");
				row.add(left);
			}
			if(it.hasNext()) {
				FBPaletteComponent right = it.next();
				right.setStyleClass(right.getStyleClass() + " right");
				row.add(right);
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
