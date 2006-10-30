package com.idega.formbuilder.presentation.renderkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.formbuilder.presentation.FBToolbar;
import com.idega.webface.WFMenuItems;
import com.idega.webface.WFTab;
import com.idega.webface.renderkit.ToolbarRenderer;

/**
 * <p>
 * Default Renderer for the FBToolbar component.
 * </p>
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FBToolbarRenderer extends ToolbarRenderer {
	
	/* (non-Javadoc)
	 * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
	 */
	public void encodeBegin(FacesContext context, UIComponent comp)
			throws IOException {
		
		FBToolbar menu = (FBToolbar)comp;
		
		ResponseWriter out = context.getResponseWriter();
		
		UIComponent menuHeader = menu.getMenuHeader();
		if(menuHeader != null)
			this.renderChild(context,menuHeader);
		
		if(menu.isEmpty())
			return;
		
		out.startElement("div",null);
		String mainStyle = menu.getMenuStyleClass();
		if (mainStyle != null) {
			out.writeAttribute("class", mainStyle+"container", null);
		}
		
		out.startElement("div", null);
		if (menu.getStyleClass() != null) {
			out.writeAttribute("class", menu.getStyleClass(), null);
		}
		out.writeAttribute("id", "" + comp.getId(), null);
		//MenuItems:
		
		WFMenuItems menuItems = null;
		List itemList = null;
		if(menu.getChildCount() > 1){
			itemList = menu.getChildren();
		} else {
			Iterator childs = menu.getChildren().iterator();
			while (childs.hasNext()) {
				Object obj = childs.next();
				if(obj instanceof WFMenuItems){
					menuItems = (WFMenuItems)obj;
					break;
				}
			}
			
			if(menuItems != null){
				itemList = (List)menuItems.getValue();
			} else {
				itemList = new ArrayList();
				Map menuFacetMap = menu.getFacets();
				Set keys = menuFacetMap.keySet();
				Iterator iter = keys.iterator();
				while (iter.hasNext()) {
					String key = (String) iter.next();
					if(key.startsWith("menuitem_")) {
						itemList.add(menuFacetMap.get(key));
					}
				}
			}
		}
		
		Iterator iter = itemList.iterator();
		boolean isFirstItem = true;
		boolean hasNext = iter.hasNext();
		
		while (hasNext) {
			UIComponent menuItem =  (UIComponent) iter.next();
			hasNext=iter.hasNext();
			String buttonId = menuItem.getId();
			//String buttonStyleClass = menu.getDeselectedMenuItemStyleClass();
			
			if(menuItem.isRendered()) {
				if(menuItem instanceof WFTab) {
					WFTab tab = (WFTab) menuItem;
					if (buttonId.equals(menu.getSelectedMenuItemId())) {
						tab.setSelected(true);
				//		buttonStyleClass = menu.getSelectedMenuItemStyleClass();
					} else {
						tab.setSelected(false);
					}
				}
//				if (buttonId.equals(menu.getSelectedMenuItemId())) {
//					buttonStyleClass = menu.getSelectedMenuItemStyleClass();
//				} 
				out.startElement("span", null);
				
				if(menu.getButtonsStyleClass() != null)
					out.writeAttribute("class", menu.getButtonsStyleClass(), null);
				
				renderChild(context,menuItem);
				out.endElement("span");
			}
		}
		out.endElement("div");
		out.endElement("div");
	}
}
