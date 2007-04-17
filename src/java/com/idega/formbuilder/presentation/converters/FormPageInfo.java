package com.idega.formbuilder.presentation.converters;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

public class FormPageInfo {
	
	private String pageTitle;
	private String pageId;
	private List<Element> components = new LinkedList<Element>();
	private String buttonAreaId;
	private List<FormButtonInfo> buttons = new LinkedList<FormButtonInfo>();
	
	public FormPageInfo() {
		this.pageTitle = "";
		this.pageId = "";
		this.buttonAreaId = "";
		this.components.clear();
		this.buttons.clear();
	}

	public String getButtonAreaId() {
		return buttonAreaId;
	}

	public void setButtonAreaId(String buttonAreaId) {
		this.buttonAreaId = buttonAreaId;
	}

	public List<FormButtonInfo> getButtons() {
		return buttons;
	}

	public void setButtons(List<FormButtonInfo> buttons) {
		this.buttons = buttons;
	}

	public List<Element> getComponents() {
		return components;
	}

	public void setComponents(List<Element> components) {
		this.components = components;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

}
