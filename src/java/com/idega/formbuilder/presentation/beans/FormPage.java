package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.idega.builder.business.BuilderLogic;
import com.idega.xformsmanager.business.Document;
import com.idega.xformsmanager.business.component.ButtonArea;
import com.idega.xformsmanager.business.component.Component;
import com.idega.xformsmanager.business.component.Page;
import com.idega.xformsmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.presentation.components.FBFormPage;
import com.idega.formbuilder.presentation.components.FBViewPanel;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String BEAN_ID = "formPage";
	
	private static final String FORM_ELEMENT = "formElement formElementHover";
	
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
	
	public boolean hasRegularComponents() {
		if(page == null) {
			return false;
		}
		
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			return page.getContainedComponentsIds().size() > 1;
		} else {
			return !page.getContainedComponentsIds().isEmpty();
		}
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
				initializeBeanInstance(page, true);
			}
		}
		return getDesignView(FORM_ELEMENT);
	}
	
	private org.jdom.Document getDesignView(String elementStyleClass) {
		return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBDesignView(elementStyleClass), false);
	}
	
	public void updateComponentList(List<String> idSequence) throws Exception {
		if(page != null) {
			List<String> ids = page.getContainedComponentsIds();
			ButtonArea area = page.getButtonArea();
			String buttonAreaId = null;
			if(area != null) {
				buttonAreaId = area.getId();
			}
			ids.clear();
			ids.addAll(idSequence);
			if(buttonAreaId != null) {
				ids.add(buttonAreaId);
			}
			page.rearrangeComponents();
		} else {
			throw new Exception("Page component missing");
		}
	}
	
	public void updateButtonList(List<String> idSequence) throws Exception {
		ButtonArea area = page.getButtonArea();
		if(area != null) {
			List<String> ids = area.getContainedComponentsIds();
			ids.clear();
			ids.addAll(idSequence);
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
				String newPageId = CoreConstants.EMPTY;
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
				properties.add(getDesignView(FORM_ELEMENT));
				workspace.setView(FBViewPanel.DESIGN_VIEW);
			}
		}
		return properties;
	}
	
	public org.jdom.Document getFormPageInfo(String id) {
		if(StringUtils.isEmpty(id)) {
			return null;
		}
		
		Document document = formDocument.getDocument();
		if(document != null) {
			initializeBeanInstance(document.getPage(id));
		}
		return getDesignView(FORM_ELEMENT);
	}
	
	public org.jdom.Document getConfirmationPageInfo() throws Exception {
		Document document = formDocument.getDocument();
		if(document != null) {
			Page page = document.getConfirmationPage();
			if(page != null) {
				initializeBeanInstance(page, true);
			}
		}
		return getDesignView(FORM_ELEMENT);
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
				initializeBeanInstance(page, false);
				workspace.setView(FBViewPanel.DESIGN_VIEW);
				doms.add(getDesignView(FORM_ELEMENT));
				doms.add(BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBFormPage(id + "_P", page.getProperties().getLabel().getString(FBUtil.getUILocale())), true));
			}
		}
		return doms;
	}
	
	public List<String> getAssignedVariables(Page page) {
		List<String> list = new ArrayList<String>();
		if(page != null) {
			List<String> components = page.getContainedComponentsIds();
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
		if(page == null) {
			return CoreConstants.EMPTY;
		}
		return FBUtil.getPropertyString(page.getProperties().getLabel().getString(FBUtil.getUILocale()));
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
