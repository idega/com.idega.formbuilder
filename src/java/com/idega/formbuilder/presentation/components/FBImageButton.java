package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

import com.idega.webface.event.WFToolbarButtonPressedListener;

public class FBImageButton extends UICommand {
	
	private String defaultImageURI;
	private String toolTip;
	private String displayText;
	private String styleClass;
	
	public FBImageButton() {
		
	}
	
	public void setListener(WFToolbarButtonPressedListener listener) {
		this.addFacesListener(listener);
	}
	
	public String getDefaultImageURI() {
		return this.defaultImageURI;
	}

	public void setDefaultImageURI(String defaultImageURI) {
		this.defaultImageURI = defaultImageURI;
	}

	public String getToolTip() {
		return this.toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}
	
	public String getDisplayText() {
		if(this.displayText != null) {
			return this.displayText;
		}
		ValueBinding binding = getValueBinding("displayText");
		return (binding!=null)?(String)binding.getValue(getFacesContext()):null;
	}

	public void setDisplayText(String text) {
		this.displayText = text;
	}
	
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public String getStyleClass() {
		return this.styleClass;
	}
	
	public void encodeBegin(FacesContext context) {
	}
	
	public void encodeChildren(FacesContext context) {
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter out = context.getResponseWriter();

		String buttonId = getClientId(context);
		String imageId = buttonId + "_img";
		
		out.startElement("input", null);
		out.writeAttribute("id", buttonId, null);
		out.writeAttribute("type", "hidden", null);
		out.writeAttribute("name", buttonId, null);
		out.writeAttribute("value", getDisplayText(), null);
		out.writeAttribute("style", "display:none;", null);
		out.endElement("input");

		String formName = determineFormName(this);
		if (getDefaultImageURI() != null) {
			out.startElement("img", null);
			out.writeAttribute("src", getDefaultImageURI(), null);
			out.writeAttribute("id", imageId, null);
			if (formName == null) {
				throw new IOException("Toolbars should be nested in a UIForm !");
			}
			if (this.toolTip != null) {
				out.writeAttribute("alt", this.toolTip, null);
				out.writeAttribute("title", this.toolTip, null);
			}
			String onmouseup = "document.forms['" + formName + "'].elements['" + buttonId + 
			"'].value='true';document.forms['" + formName + "'].submit();";
			out.writeAttribute("onmouseup", onmouseup, null);
			String onmouseout = "document.forms['" + formName + "'].elements['" + buttonId + "'].value='';this.src='" + 
					getDefaultImageURI() + "'";
			out.writeAttribute("onmouseout", onmouseout, null);
			out.endElement("img");
		} else {
			out.startElement("a", null);
			String onmouseup = "document.forms['" + formName + "'].elements['" + buttonId + 
			"'].value='true';document.forms['" + formName + "'].submit();";
			out.writeAttribute("onmouseup", onmouseup, null);
			
			if (this.styleClass != null) {
				out.writeAttribute("class", this.styleClass, null);
			}
			if (this.toolTip != null) {
				out.writeAttribute("alt", this.toolTip, null);
				out.writeAttribute("title", this.toolTip, null);
			}
			
			out.writeAttribute("href","#",null);
			
			String text = getDisplayText();
			if(text != null){
				out.write(text);
			}
			
			out.endElement("a");
		}
	}
		
	public void renderChild(FacesContext context, UIComponent child) throws IOException {
		child.encodeBegin(context);
		child.encodeChildren(context);
		child.encodeEnd(context);
	}
	
	public void decode(FacesContext context) {
		String buttonId = getClientId(context);
		String inputValue =	(String) context.getExternalContext().getRequestParameterMap().get(buttonId);
		if (inputValue != null && inputValue.equals("true")) {
			ActionEvent event = new ActionEvent(this);
			queueEvent(event);
		}
	}
	
	/**
	 * Determines the client id of the form in which a component is enclosed.
	 * Useful for generating submitForm('xyz') javascripts...
	 * 
	 * @return
	 */
	public static String determineFormName(UIComponent component) {
		String ret = null;
		UIComponent current = component.getParent();
		UIComponent form = null;
		FacesContext ctx = FacesContext.getCurrentInstance();
		
		while(current != null) {
			if(current instanceof UIForm) {
				form = current;
				break;
			}
			current = current.getParent();
		}
		if(form==null){
			UIComponent root = ctx.getViewRoot();
			form = findFormDown(root);
		}
		
		if(form != null) {
			ret = form.getClientId(ctx);
		}
		
		return ret;
	}
	
	
	public static UIForm findFormDown(UIComponent component){
		for (Iterator iterator = component.getFacetsAndChildren(); iterator.hasNext();) {
			UIComponent child = (UIComponent) iterator.next();
			if(child instanceof UIForm){
				return (UIForm)child;
			}
			else{
				return findFormDown(child);
			}
		}
		return null;
	}
	

	/**
	 * @see javax.faces.component.UIPanel#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext ctx) {
		Object values[] = new Object[8];
		values[0] = super.saveState(ctx);
		values[1] = this.defaultImageURI;
		values[5] = this.toolTip;
		values[6] = this.displayText;
		values[7] = this.styleClass;
		return values;
	}

	/**
	 * @see javax.faces.component.UIPanel#restoreState(javax.faces.context.FacesContext,
	 *      java.lang.Object)
	 */
	public void restoreState(FacesContext ctx, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(ctx, values[0]);
		this.defaultImageURI = (String) values[1];
		this.toolTip = (String) values[5];
		this.displayText = (String) values[6];
		this.styleClass = (String)values[7];
	}
}
