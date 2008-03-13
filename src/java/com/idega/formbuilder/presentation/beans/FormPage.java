package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.Document;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBFormPage;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String BEAN_ID = "formPage";
	
	private static final String FORM_ELEMENT = "formElement";
	
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
	
	public org.jdom.Document getThxPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getThxPage();
			if(page != null) {
				special = true;
				initializeBeanInstance(page, true);
			}
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(FORM_ELEMENT), false);
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
			Page page = document.getPage(id);
			if(page != null) {
				List<String> ids = formDocument.getCommonPagesIdList();
				int index = ids.indexOf(id);
				String newPageId = "";
				if(index < 1) {
					if(ids.size() > 1) {
						newPageId = ids.get(1);
						page.remove();
						page = document.getPage(newPageId);
						initializeBeanInstance(page);
					}
				} else {
					newPageId = ids.get(index - 1);
					page.remove();
					page = document.getPage(newPageId);
					initializeBeanInstance(page);
				}
				properties.add(id);
				properties.add(newPageId);
				properties.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(FORM_ELEMENT), false));
				workspace.setView("design");
			}
		}
		return properties;
	}
	
	public org.jdom.Document getFormPageInfo(String id) {
		if(id == null || CoreConstants.EMPTY.equals(id)) {
			return null;
		}
		
		Document document = formDocument.getDocument();
		if(document != null) {
			initializeBeanInstance(document.getPage(id));
		}
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(FORM_ELEMENT), false);
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
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(FORM_ELEMENT), false);
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
				initializeBeanInstance(page);
				workspace.setView("design");
				doms.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(FORM_ELEMENT), false));
				doms.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormPage(id, page.getProperties().getLabel().getString(FBUtil.getUILocale())), true));
			}
		}
		return doms;
	}
	
	public List<String> getAssignedVariables(Page page) {
		List<String> list = new ArrayList<String>();
		if(page != null) {
			List<String> components = page.getContainedComponentsIdList();
			for(Iterator<String> it = components.iterator(); it.hasNext(); ) {
				String componentId = it.next();
				Component component = page.getComponent(componentId);
				String variableName = component.getProperties().getVariable().getDefaultStringRepresentation();
				if(variableName != null) {
					list.add(variableName);
				}
			}
		}
		return list;
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
