package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
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
	
	public void savePageTitle(ActionEvent ae) {
		String title = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pageTitle");
		if(title != null) {
			setTitle(title);
		}
	}
	
	public String getLoadPage() {
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			String id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("loadPageId");
			String temp = id.substring(id.indexOf(":") + 1);
			int k = temp.indexOf("_", temp.indexOf("_") + 1);
			String temp2 = temp.substring(0, k);
			Page page = document.getPage(temp2);
			loadPageInfo(page);
			Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
			if(workspace != null) {
				workspace.setView("design");
				workspace.setSelectedMenu("3");
				workspace.setRenderedMenu(true);
			}
		}
		return "";
	}
	
	public String getDeletePage() {
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			String id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deletePageId");
			String temp = id.substring(id.indexOf(":") + 1);
			int k = temp.indexOf("_", temp.indexOf("_") + 1);
			String temp2 = temp.substring(0, k);
			Page page = document.getPage(temp2);
			if(page != null) {
				List<String> ids = getCommonPagesIdList(document);
				int index = ids.indexOf(temp2);
				String newPageId = "";
				if(index < 1) {
					newPageId = ids.get(1);
				} else {
					newPageId = ids.get(index - 1);
				}
				page.remove();
				page = document.getPage(newPageId);
				loadPageInfo(page);
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
				if(workspace != null) {
					workspace.setView("design");
					workspace.setSelectedMenu("3");
					workspace.setRenderedMenu(true);
				}
				try {
					document.save();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return "";
	}
	
	public List<String> getCommonPagesIdList(Document document) {
		List<String> result = new LinkedList<String>();
		List<String> ids = document.getContainedPagesIdList();
		String confId = "";
		String tksId = "";
		Page temp = document.getConfirmationPage();
		if(temp != null) {
			confId = temp.getId();
		}
		temp = document.getThxPage();
		if(temp != null) {
			tksId = temp.getId();
		}
		Iterator it = ids.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			if(nextId.equals(confId) || nextId.equals(tksId)) {
				continue;
			}
			result.add(nextId);
		}
		return result;
	}
	
	public void clearPageInfo() {
		properties = null;
		page = null;
		id = "";
		title = "";
		titleBean = null;
	}
	
	public void loadPageInfo(Page page) {
		PropertiesPage pp = (PropertiesPage) page.getProperties();
		this.page = page;
		properties = pp;
		id = page.getId();
		titleBean = pp.getLabel();
		title = titleBean.getString(new Locale("en"));
	}
	
	public void createNewPage(ActionEvent ae) {
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
			try {
				document.save();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
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
			try {
				document.save();
			} catch (Exception e) {
				// TODO: handle exception
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
