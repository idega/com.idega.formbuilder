package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.event.ActionEvent;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.webface.WFUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private Page page;
	private String id;
	private String title;
	
	private LocalizedStringBean titleBean;
	
	public FormPage() {
		page = null;
		id = "";
		title = "";
		
		titleBean = null;
	}
	
	public void savePageTitle(ActionEvent ae) {
		
	}
	
	public String getDeletePage() {
		return "asdasd";
	}
	
	public void clearPageInfo() {
		page = null;
		id = "";
		title = "";
		
		titleBean = null;
	}
	
	public void loadPageInfo(String id) {
		PropertiesPage pp = page.getProperties();
		
		titleBean = pp.getLabel();
		title = titleBean.getString(new Locale("en"));
	}
	
	public String getNewPage() {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
			if(document != null) {
				Page page = document.addPage(null);
				FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
				if(formPage != null) {
					formPage.setPage(page);
				}
				FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
				if(formComponent != null) {
					formComponent.clearFormComponentInfo();
				}
				workspace.setView("design");
				workspace.setSelectedMenu("3");
				workspace.setRenderedMenu(true);
			}
			document.save();
		}
		return id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Page getPage() {
		return page;
	}
	
	public void setPage(Page page) {
		this.page = page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalizedStringBean getTitleBean() {
		return titleBean;
	}

	public void setTitleBean(LocalizedStringBean titleBean) {
		this.titleBean = titleBean;
	}
	

}
