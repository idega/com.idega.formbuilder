package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
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
	
	public Element getElement(String type) throws Exception {
		String elementId = formManagerInstance.createFormComponent(type, null);
		Element element = formManagerInstance.getLocalizedFormHtmlComponent(elementId, new Locale("en"));
		element.setAttribute("class", "formElement");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document document = null;
        try {
          DocumentBuilder builder = factory.newDocumentBuilder();
          document = builder.newDocument();
          Element delete = (Element) document.createElement("DIV");
          delete.setAttribute("class", "removeComponentButton");
          delete.setAttribute("onclick", "removeComponent(this)");
          Element edit = (Element) document.createElement("DIV");
          edit.setAttribute("class", "editComponentButton");
          //edit.setAttribute("onclick", "removeComponent(this)");
          Element deleteIcon = (Element) element.getOwnerDocument().importNode(delete, true);
          Element editIcon = (Element) element.getOwnerDocument().importNode(edit, true);
          element.appendChild(editIcon);
          element.appendChild(deleteIcon);
          ((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_ACTIVE);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
		return element;
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
		String id = FBUtil.generateFormId(name);
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(current, name);
		formManagerInstance.createFormDocument(id, formName);
		Element element = formManagerInstance.getLocalizedSubmitComponent(current);
		element.setAttribute("class", "formElement");
		Element button = (Element) element.getFirstChild();
		button.setAttribute("disabled", "true");
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "EMPTY_FORM");
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_FORM_ID, id);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE, current);
		((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus(FBDesignView.DESIGN_VIEW_STATUS_EMPTY);
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedTab(1);
		return element;
	}
	
	public String removeComponent(String id) {
		try {
			formManagerInstance.removeFormComponent(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
}
