package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PropertiesPage;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.converters.FormPageInfo;
import com.idega.webface.WFUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private Page page;
	private PropertiesPage properties;
	private String id;
	private String title;
	
	private boolean special;
	
	private LocalizedStringBean titleBean;
	
	public FormPage() {
		properties = null;
		page = null;
		id = "";
		title = "";
		titleBean = null;
		
		special = false;
	}
	
	public void loadThxPage() {
		Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
		if(document != null) {
			Page page = document.getThxPage();
			if(page != null) {
				this.page = page;
				special = true;
			}
		}
	}
	
	public void updateComponentList(String idSequence, String idPrefix, String delimiter) throws Exception {
		List<String> ids = page.getContainedComponentsIdList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			ids.add(idPrefix + tokenizer.nextToken());
		}
		page.rearrangeComponents();
	}
	
	public void updateButtonList(String idSequence, String idPrefix, String delimiter) throws Exception {
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			List<String> ids = area.getContainedComponentsIdList();
			ids.clear();
			String test = "&" + idSequence;
			StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
			while(tokenizer.hasMoreTokens()) {
				ids.add(idPrefix + tokenizer.nextToken());
			}
			area.rearrangeComponents();
		}
	}
	
	public String removePage(String id) {
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			String temp = id.substring(id.indexOf(":") + 1);
			int k = temp.indexOf("_", temp.indexOf("_") + 1);
			String temp2 = temp.substring(0, k);
			Page page = document.getPage(temp2);
			if(page != null) {
				List<String> ids = formDocument.getCommonPagesIdList();
				int index = ids.indexOf(temp2);
				String newPageId = "";
				if(index < 1) {
					if(ids.size() > 1) {
						newPageId = ids.get(1);
						page.remove();
						page = document.getPage(newPageId);
						loadPageInfo(page);
					}
				} else {
					newPageId = ids.get(index - 1);
					page.remove();
					page = document.getPage(newPageId);
					loadPageInfo(page);
				}
				Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
				if(workspace != null) {
					workspace.setView("design");
					workspace.setRenderedMenu(true);
				}
				return id;
			}
		}
		return null;
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
			special = false;
			Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
			if(workspace != null) {
				workspace.setView("design");
				workspace.setSelectedMenu("3");
				workspace.setRenderedMenu(true);
			}
		}
		return result;
	}
	
	public FormPageInfo getConfirmationPageInfo() {
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getConfirmationPage();
			if(page != null) {
				this.page = page;
				special = true;
				FormPageInfo result = new FormPageInfo();
				result.setPageTitle(page.getProperties().getLabel().getString(new Locale("en")));
				result.setPageId(page.getId());
				return result;
			}
		}
		return null;
	}
	
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
		result.setPageId(id);
		return result;
	}
	
	public FormPageInfo createNewPage() {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
			if(document != null) {
				Page page = null;
				String temp = "";
				if(document.getConfirmationPage() != null) {
					temp = document.getConfirmationPage().getId();
				} else {
					temp = document.getThxPage().getId();
					
				}
				page = document.addPage(temp);
				if(page != null) {
					special = false;
					FormPageInfo result = loadPageInfo(page);
					FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
					formComponent.clearFormComponentInfo();
					workspace.setView("design");
					workspace.setRenderedMenu(true);
					return result;
				}
			}
		}
		return null;
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

	public FormPageInfo setTitle(String title) {
		this.title = title;
		titleBean.setString(new Locale("en"), title);
		if(properties != null) {
			properties.setLabel(titleBean);
		}
		FormPageInfo result = new FormPageInfo();
		result.setPageTitle(title);
		result.setPageId(id);
		return result;
	}
	
	public String saveTitle(String title) {
		setTitle(title);
		return title;
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

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}
	

}
