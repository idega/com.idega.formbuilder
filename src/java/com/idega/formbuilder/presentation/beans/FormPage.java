package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.Document;
import com.idega.documentmanager.business.form.Page;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.converters.FormButtonInfo;
import com.idega.formbuilder.presentation.converters.FormPageInfo;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	private Page page;
	private String id;
	private boolean special;
	
	public Page initializeBeanInstance(Page page) {
		if(page != null) {
			this.page = page;
			this.id = page.getId();
			this.special = false;
		} else {
			this.page = null;
			this.id = null;
			this.special = false;
		}
		
		return page;
	}
	
	public FormPageInfo getFirstPageInfo() throws Exception {
		Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
		if(document != null) {
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			if(page != null) {
				return loadPageInfo(page);
			}
		}
		return null;
	}
	
	public FormPageInfo getThxPageInfo() throws Exception {
		Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
		if(document != null) {
			Page page = document.getThxPage();
			if(page != null) {
				special = true;
				return loadPageInfo(page);
			}
		}
		return null;
	}
	
	public void updateComponentList(String idSequence, String idPrefix, String delimiter) throws Exception {
		if(page != null) {
			List<String> ids = page.getContainedComponentsIdList();
			ids.clear();
			String test = "&" + idSequence;
			StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
			while(tokenizer.hasMoreTokens()) {
				ids.add(idPrefix + tokenizer.nextToken());
			}
			page.rearrangeComponents();
		} else {
			throw new Exception("Page component missing");
		}
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
	
	public String removePage(String id) throws Exception {
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
	
	public org.jdom.Document getFormPageInfo(String id) {
		String realId = null;
		try {
			String temp = id.substring(id.indexOf(":") + 1);
			int k = temp.indexOf("_", temp.indexOf("_") + 1);
			realId = temp.substring(0, k);
			this.id = realId;
		} catch(ArrayIndexOutOfBoundsException e) {
			logger.error("Could not parse page ID", e);
			return null;
		}
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			initializeBeanInstance(document.getPage(realId));
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
	}
	
	public FormPageInfo getConfirmationPageInfo() throws Exception {
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getConfirmationPage();
			if(page != null) {
				special = true;
				return loadPageInfo(page);
			}
		}
		return null;
	}
	
	public FormPageInfo loadPageInfo(Page page) throws Exception {
		this.page = page;
		id = page.getId();
		special = false;
		
		FormPageInfo result = new FormPageInfo();
		result.setPageTitle(getTitle());
		result.setPageId(id);
		
		String areaId = "";
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			areaId = area.getId();
			result.setButtonAreaId(areaId);
			List<String> buttons = area.getContainedComponentsIdList();
			Iterator it = buttons.iterator();
			while(it.hasNext()) {
				String nextId = (String) it.next();
				Button button = (Button) area.getComponent(nextId);
				if(button != null) {
					result.getButtons().add(new FormButtonInfo(nextId, button.getProperties().getLabel().getString(new Locale("en"))));
				}
			}
		}
		List<String> components = page.getContainedComponentsIdList();
		Iterator it = components.iterator();
		while(it.hasNext()) {
			String nextId = (String) it.next();
			if(areaId.equals(nextId)) {
				continue;
			}
			result.getComponents().add((Element) page.getComponent(nextId).getHtmlRepresentation(new Locale("en")).cloneNode(true));
		}
		return result;
	}
	
	public FormPageInfo createNewPage() throws Exception {
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
//					FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
//					formComponent.clearFormComponentInfo();
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
		return page.getProperties().getLabel().getString(FBUtil.getUILocale());
	}

	public FormPageInfo setTitle(String title) {
		page.getProperties().getLabel().setString(FBUtil.getUILocale(), title);
//		this.title = title;
//		if(titleBean != null) {
//			titleBean.setString(new Locale("en"), title);
//			if(properties != null) {
//				properties.setLabel(titleBean);
//			}
//		}
		FormPageInfo result = new FormPageInfo();
		result.setPageTitle(title);
		result.setPageId(id);
		return result;
	}
	
	public String saveTitle(String title) {
		setTitle(title);
		return title;
	}

//	public LocalizedStringBean getTitleBean() {
//		return titleBean;
//	}
//
//	public void setTitleBean(LocalizedStringBean titleBean) {
//		this.titleBean = titleBean;
//	}

//	public PropertiesPage getProperties() {
//		return properties;
//	}
//
//	public void setProperties(PropertiesPage properties) {
//		this.properties = properties;
//	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}
	

}
