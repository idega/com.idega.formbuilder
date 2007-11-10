package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBFormPage;
import com.idega.formbuilder.presentation.converters.FormButtonInfo;
import com.idega.formbuilder.presentation.converters.FormPageInfo;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private static Log logger = LogFactory.getLog(FormDocument.class);
	
	public static final String BEAN_ID = "formPage";
	
	private Page page;
	private String id;
	private boolean special;
	
	private FormDocument formDocument;
	private Workspace workspace;
	
	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public FormDocument getFormDocument() {
		return formDocument;
	}

	public void setFormDocument(FormDocument formDocument) {
		this.formDocument = formDocument;
	}

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
	
	public Page initializeBeanInstance(Page page, boolean special) {
		initializeBeanInstance(page);
		this.special = special;
		
		return page;
	}
	
	public FormPageInfo getFirstPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			if(page != null) {
				return loadPageInfo(page);
			}
		}
		return null;
	}
	
	public org.jdom.Document getThxPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getThxPage();
			if(page != null) {
				special = true;
				initializeBeanInstance(page, true);
			}
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
	}
	
	public void updateComponentList(List<String> idSequence) throws Exception {
		if(page != null) {
			List<String> ids = page.getContainedComponentsIdList();
			ids.clear();
			for(Iterator<String> it = idSequence.iterator(); it.hasNext(); )
				ids.add(it.next());
			page.rearrangeComponents();
		} else {
			throw new Exception("Page component missing");
		}
	}
	
	public void updateButtonList(List<String> idSequence) throws Exception {
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			List<String> ids = area.getContainedComponentsIdList();
			ids.clear();
			for(Iterator<String> it = idSequence.iterator(); it.hasNext(); )
				ids.add(it.next());
			area.rearrangeComponents();
		}
	}
	
	public List<Object> removePage(String id) throws Exception {
		List<Object> properties = new ArrayList<Object>();
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
				properties.add(id);
				properties.add(newPageId);
				properties.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false));
				workspace.setView("design");
			}
		}
		return properties;
	}
	
	public org.jdom.Document getFormPageInfo(String id) {
		if(id == null || id.equals("")) {
			return null;
		}
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
		
		Document document = formDocument.getDocument();
		if(document != null) {
			initializeBeanInstance(document.getPage(realId));
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
	}
	
	public org.jdom.Document getConfirmationPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getConfirmationPage();
			if(page != null) {
				special = true;
				initializeBeanInstance(page, true);
			}
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false);
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
			Iterator<String> it = buttons.iterator();
			while(it.hasNext()) {
				String nextId = it.next();
				Button button = (Button) area.getComponent(nextId);
				if(button != null) {
					result.getButtons().add(new FormButtonInfo(nextId, button.getProperties().getLabel().getString(new Locale("en"))));
				}
			}
		}
		List<String> components = page.getContainedComponentsIdList();
		Iterator<String> it = components.iterator();
		while(it.hasNext()) {
			String nextId = it.next();
			if(areaId.equals(nextId)) {
				continue;
			}
			result.getComponents().add((Element) page.getComponent(nextId).getHtmlRepresentation(new Locale("en")).cloneNode(true));
		}
		return result;
	}
	
	public List<org.jdom.Document> createNewPage() throws Exception {
		Document document = formDocument.getDocument();
		List<org.jdom.Document> doms = new ArrayList<org.jdom.Document>();
		if(document != null) {
			String temp = null;
			if(document.getConfirmationPage() != null) {
				temp = document.getConfirmationPage().getId();
			} else {
				temp = document.getThxPage().getId();
			}
			Page page = document.addPage(temp);
			if(page != null) {
				special = false;
				loadPageInfo(page);
				workspace.setView("design");
				doms.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView("formElement"), false));
				doms.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormPage(id, page.getProperties().getLabel().getString(FBUtil.getUILocale())), true));
			}
		}
		return doms;
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

	public void setTitle(String title) {
		if(page != null) {
			LocalizedStringBean bean = page.getProperties().getLabel();
			bean.setString(FBUtil.getUILocale(), title);
			page.getProperties().setLabel(bean);
		}
	}
	
	public String saveTitle(String title) {
		setTitle(title);
		return title;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}
	

}
