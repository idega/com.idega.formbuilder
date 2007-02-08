package com.idega.formbuilder.business.form.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class XFormsComponentDataBean implements Cloneable {
	
	private Element element;
	private Element bind;
	private Element nodeset;
	private Element preview_element;
	
	public Element getPreviewElement() {
		return preview_element;
	}
	public void setPreviewElement(Element preview_element) {
		this.preview_element = preview_element;
	}
	public Element getBind() {
		return bind;
	}
	public void setBind(Element bind) {
		this.bind = bind;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public Element getNodeset() {
		return nodeset;
	}
	public void setNodeset(Element nodeset) {
		this.nodeset = nodeset;
	}
	
	public Object clone() {
		
		XFormsComponentDataBean clone = getDataBeanInstance();
		
		if(element != null)
			clone.setElement((Element)element.cloneNode(true));
		
		if(bind != null)
			clone.setBind((Element)bind.cloneNode(true));
		
		if(nodeset != null)
			clone.setNodeset((Element)nodeset.cloneNode(true));
		
		return clone;
	}
	
	protected XFormsComponentDataBean getDataBeanInstance() {
		
		return new XFormsComponentDataBean();
	}
}
