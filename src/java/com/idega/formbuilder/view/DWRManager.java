package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.beans.ItemBean;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.beans.DataSourceList;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.util.FBUtil;
import com.idega.webface.WFUtil;

public class DWRManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	public void removeItem(int index) {
		List<ItemBean> items = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		if(index < items.size()) {
			items.remove(index);
			((FormComponent) WFUtil.getBeanInstance("formComponent")).setItems(items);
		}
	}
	
	public void saveLabel(int index, String value) {
		List<ItemBean> items = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		if(index >= items.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setLabel(value);
			newItem.setValue(value);
			items.add(newItem);
		} else {
			items.get(index).setLabel(value);
			items.get(index).setValue(value);
		}
		((FormComponent) WFUtil.getBeanInstance("formComponent")).setItems(items);
	}
	
	public void saveValue(int index, String value) {
		List<ItemBean> items = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		if(index >= items.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setValue(value);
			items.add(newItem);
		} else {
			items.get(index).setValue(value);
		}
		((FormComponent) WFUtil.getBeanInstance("formComponent")).setItems(items);
	}
	
	private void setSelectedMenu(String selectedMenu) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu(selectedMenu);
	}
	
	public void switchDataSource() {
		FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
		String current = formComponent.getDataSrc();
		if(current.equals(DataSourceList.externalDataSrc)) {
			formComponent.setDataSrc(DataSourceList.localDataSrc);
		} else {
			formComponent.setDataSrc(DataSourceList.externalDataSrc);
		}
	}
	
	public void changeMenu(String id) {
		if(id.equals("workspaceform1:tab1Title")) {
			setSelectedMenu("0");
		} else if(id.equals("workspaceform1:tab2Title")) {
			setSelectedMenu("1");
		} else if(id.equals("workspaceform1:tab3Title")) {
			setSelectedMenu("2");
		} else if(id.equals("workspaceform1:tab4Title")) {
			setSelectedMenu("3");
		}
	}
	
	public Element createComponent(String type) throws Exception {
		Element rootDivImported = null;
		FormPage page = (FormPage) WFUtil.getBeanInstance("formPage");
		Component component = page.getPage().addComponent(type, null);
//		String elementId = component.getId();
//		String elementId = ActionManager.getDocumentManagerInstance().getCurrentDocument().a.createFormComponent(type, null);
		Element element = (Element) component.getHtmlRepresentation(new Locale("en")).cloneNode(true);
		String id = element.getAttribute("id");
		element.removeAttribute("id");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
        try {
          DocumentBuilder builder = factory.newDocumentBuilder();
          document = builder.newDocument();
          Element delete = document.createElement("IMG");
          delete.setAttribute("src", "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
          delete.setAttribute("class", "speedButton");
          delete.setAttribute("onclick", "deleteComponentJS(this)");
          Element deleteIcon = (Element) element.getOwnerDocument().importNode(delete, true);
          Element rootDiv = document.createElement("DIV");
          rootDiv.setAttribute("id", id);
          rootDiv.setAttribute("class", "formElement");
          rootDiv.setAttribute("onclick", "editProperties(this.id)");
          rootDivImported = (Element) element.getOwnerDocument().importNode(rootDiv, true);
          rootDivImported.appendChild(element);
          rootDivImported.appendChild(deleteIcon);
          ((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_ACTIVE);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
		return rootDivImported;
	}
	
	public void updateComponentList(String idSequence, String idPrefix, String delimiter) throws Exception {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		List<String> ids = page.getContainedComponentsIdList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			ids.add(idPrefix + tokenizer.nextToken());
		}
		((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument().rearrangeDocument();
	}
	
	public Element createNewForm(String name) throws Exception {
		/*Locale current = (Locale) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE);
		if(current == null) {
			current = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}*/
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Locale locale = workspace.getLocale();
			DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
			
			String id = FBUtil.generateFormId(name);
			LocalizedStringBean formName = new LocalizedStringBean();
			formName.setString(locale, name);
			formManagerInstance.createForm(id, formName);
			
			workspace.setView("design");
			workspace.setDesignViewStatus("empty");
			workspace.setSelectedMenu("0");
			workspace.setRenderedMenu(true);
			
			FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
			if(formDocument != null) {
				formDocument.clearFormDocumentInfo();
				formDocument.setFormTitle(name);
				formDocument.setFormId(id);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
			
			/*Element element = formManagerInstance.getLocalizedSubmitComponent(locale);
			if(element != null) {
				Element button = (Element) element.getFirstChild();
				if(button != null) {
					button.setAttribute("disabled", "true");
					
					
				}
			}*/
			return null;
		}
		return null;
	}
	
	public String removeComponent(String id) {
		FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
		formComponent.getComponent().remove();
//		try {
//			ActionManager.getDocumentManagerInstance().removeFormComponent(id);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		if(page.getContainedComponentsIdList().isEmpty()) {
			((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_EMPTY);
		}
		return id;
	}
	
	public String getComponentProperties(String id) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("1");
		((com.idega.formbuilder.presentation.beans.FormComponent) WFUtil.getBeanInstance("formComponent")).loadProperties(id);
		return id;
	}
	
}
