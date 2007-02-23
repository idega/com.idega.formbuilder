package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.converters.FormPageInfo;
import com.idega.webface.WFUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private Page page;
	private PropertiesPage properties;
	private String id;
	private String title;
	
	private LocalizedStringBean titleBean;
	
	public FormPage() {
		properties = null;
		page = null;
		id = "";
		title = "";
		titleBean = null;
	}
	
	public void removePage(String id) {
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			String temp = id.substring(id.indexOf(":") + 1);
			int k = temp.indexOf("_", temp.indexOf("_") + 1);
			String temp2 = temp.substring(0, k);
			Page page = document.getPage(temp2);
			if(page != null) {
				FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
				List<String> ids = formDocument.getCommonPagesIdList();
				int index = ids.indexOf(temp2);
				String newPageId = "";
				if(index < 1) {
					if(ids.size() > 1) {
						newPageId = ids.get(1);
						page.remove();
						page = document.getPage(newPageId);
						formPage.loadPageInfo(page);
					}
				} else {
					newPageId = ids.get(index - 1);
					page.remove();
					page = document.getPage(newPageId);
					formPage.loadPageInfo(page);
				}
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
				if(workspace != null) {
					workspace.setView("design");
					workspace.setRenderedMenu(true);
				}
			}
		}
	}
	
	public FormPageInfo getFormPageInfo(String id) {
		FormPageInfo result = null;
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			String temp = id.substring(id.indexOf(":") + 1);
			int k = temp.indexOf("_", temp.indexOf("_") + 1);
			String temp2 = temp.substring(0, k);
			Page page = document.getPage(temp2);
			result = loadPageInfo(page);
			Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
			if(workspace != null) {
				workspace.setView("design");
				workspace.setSelectedMenu("3");
				workspace.setRenderedMenu(true);
			}
		}
		return result;
	}
	
//	public String getDeletePage() {
//		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
//		Document document = formDocument.getDocument();
//		if(document != null) {
//			String id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletePageId");
//			String temp = id.substring(id.indexOf(":") + 1);
//			int k = temp.indexOf("_", temp.indexOf("_") + 1);
//			String temp2 = temp.substring(0, k);
//			Page page = document.getPage(temp2);
//			if(page != null) {
//				List<String> ids = getCommonPagesIdList(document);
//				int index = ids.indexOf(temp2);
//				String newPageId = "";
//				if(index < 1) {
//					newPageId = ids.get(1);
//				} else {
//					newPageId = ids.get(index - 1);
//				}
//				page.remove();
//				page = document.getPage(newPageId);
//				loadPageInfo(page);
//				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
//				if(workspace != null) {
//					workspace.setView("design");
//					workspace.setSelectedMenu("3");
//					workspace.setRenderedMenu(true);
//				}
//				try {
//					document.save();
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		}
//		return "";
//	}
	
	public void clearPageInfo() {
		properties = null;
		page = null;
		id = "";
		title = "";
		titleBean = null;
	}
	
	public FormPageInfo loadPageInfo(Page page) {
		PropertiesPage pp = (PropertiesPage) page.getProperties();
		this.page = page;
		properties = pp;
		id = page.getId();
		titleBean = pp.getLabel();
		title = titleBean.getString(new Locale("en"));
		
		FormPageInfo result = new FormPageInfo();
		result.setPageTitle(title);
		
		return result;
	}
	
//	public FormPageInfo getFormPageInfo() {
//		FormPageInfo info = new FormPageInfo();
//		info.setPageTitle(title);
//		return info;
//	}
	
	public String createNewPage() {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
			if(document != null) {
				Page page = document.addPage(null);
				if(page != null) {
					FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
					formPage.setPage(page);
					FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
					formComponent.clearFormComponentInfo();
					workspace.setView("design");
					workspace.setRenderedMenu(true);
					return page.getId();
				}
			}
		}
		return "";
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
		titleBean.setString(new Locale("en"), title);
		if(properties != null) {
			properties.setLabel(titleBean);
		}
	}

	public LocalizedStringBean getTitleBean() {
		return titleBean;
	}

	public void setTitleBean(LocalizedStringBean titleBean) {
		this.titleBean = titleBean;
	}

	public PropertiesPage getProperties() {
		return properties;
	}

	public void setProperties(PropertiesPage properties) {
		this.properties = properties;
	}
	

}
