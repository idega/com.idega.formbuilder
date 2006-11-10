package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.FormComponent;
import com.idega.formbuilder.business.Workspace;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;
import com.idega.formbuilder.presentation.FBDesignView;
import com.idega.formbuilder.util.FBUtil;
import com.idega.webface.WFUtil;

public class DWRManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	private IFormManager formManagerInstance;
	
	public DWRManager() {
		formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
	}
	
	public void saveChanges() throws Exception {
		//((FormComponent) WFUtil.getBeanInstance("component")).saveProperties();
	}
	
	public Element getElement(String type) throws Exception {
		Element rootDivImported = null;
		String elementId = formManagerInstance.createFormComponent(type, null);
		Element element = (Element) formManagerInstance.getLocalizedFormHtmlComponent(elementId, new Locale("en")).cloneNode(true);
		String id = element.getAttribute("id");
		element.removeAttribute("id");
		//element.setAttribute("class", "formElement");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
        try {
          DocumentBuilder builder = factory.newDocumentBuilder();
          document = builder.newDocument();
          Element delete = (Element) document.createElement("IMG");
          delete.setAttribute("src", "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-delete.png");
          delete.setAttribute("class", "speedButton");
          delete.setAttribute("onclick", "deleteComponent(this)");
          //Element edit = (Element) document.createElement("IMG");
          //edit.setAttribute("src", "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/edit-find-replace.png");
          //edit.setAttribute("class", "speedButton");
          //edit.setAttribute("onclick", "editProperties(this)");
          Element deleteIcon = (Element) element.getOwnerDocument().importNode(delete, true);
          //Element editIcon = (Element) element.getOwnerDocument().importNode(edit, true);
          Element rootDiv = (Element) document.createElement("DIV");
          rootDiv.setAttribute("id", id);
          rootDiv.setAttribute("class", "formElement");
          rootDiv.setAttribute("onclick", "editProperties(this)");
          rootDivImported = (Element) element.getOwnerDocument().importNode(rootDiv, true);
          rootDivImported.appendChild(element);
          rootDivImported.appendChild(deleteIcon);
          //rootDivImported.appendChild(editIcon);
          ((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_ACTIVE);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
		return rootDivImported;
	}
	
	public void updateComponentList(String idSequence, String idPrefix, String delimiter) throws Exception {
		List<String> ids = formManagerInstance.getFormComponentsIdsList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			ids.add(idPrefix + tokenizer.nextToken());
		}
		formManagerInstance.rearrangeDocument();
	}
	
	public Element createNewForm(String name) throws Exception {
		Locale current = (Locale) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE);
		if(current == null) {
			current = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}
//		String id = FBUtil.generateFormId(name);
		String id = "thisisformid";
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(current, name);
		formManagerInstance.createFormDocument(id, formName);
		//System.out.println("NEW FORM CREATED: " + id + "---------------------");
		Element element = formManagerInstance.getLocalizedSubmitComponent(new Locale("en"));
		//DOMUtil.prettyPrintDOM(element);
		//element.setAttribute("class", "formElement");
		Element button = (Element) element.getFirstChild();
		button.setAttribute("disabled", "true");
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "EMPTY_FORM");
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_FORM_ID, id);
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE, current);
		((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_EMPTY);
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedTab(1);
		((Workspace) WFUtil.getBeanInstance("workspace")).setFormTitle(name);
		return element;
	}
	
	public String removeComponent(String id) {
		try {
			formManagerInstance.removeFormComponent(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(formManagerInstance.getFormComponentsIdsList().isEmpty()) {
			((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_EMPTY);
		}
		return id;
	}
	
	public String editComponentProperties(String id) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setCurrentComponent(id);
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedTab(2);
		return id;
	}
	
}
