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
//import com.idega.formbuilder.business.FormComponent;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.presentation.components.FBDesignView;
import com.idega.formbuilder.util.FBUtil;
import com.idega.webface.WFUtil;

public class DWRManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
//	private IFormManager formManagerInstance;
	
	/*public DWRManager() {
		formManagerInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
	}*/
	
	/*public void removeOption(String id) {
		if(id != null && id.contains("_")) {
			int index = Integer.parseInt(id.substring(id.length()-1));
			int size = ((FormComponent) WFUtil.getBeanInstance("component")).getItems().size();
			if(index < size) {
				((FormComponent) WFUtil.getBeanInstance("component")).getItems().remove(index);
			} else {
				((FormComponent) WFUtil.getBeanInstance("component"));
			}
		}
	}*/
	
	private void setSelectedMenu(String selectedMenu) {
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu(selectedMenu);
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
		String elementId = ActionManager.getFormManagerInstance().createFormComponent(type, null);
		Element element = (Element) ActionManager.getFormManagerInstance().getLocalizedFormHtmlComponent(elementId, new Locale("en")).cloneNode(true);
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
		List<String> ids = ActionManager.getFormManagerInstance().getFormComponentsIdsList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			ids.add(idPrefix + tokenizer.nextToken());
		}
		ActionManager.getFormManagerInstance().rearrangeDocument();
	}
	
	public Element createNewForm(String name) throws Exception {
		Locale current = (Locale) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE);
		if(current == null) {
			current = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}
		String id = FBUtil.generateFormId(name);
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(current, name);
		ActionManager.getFormManagerInstance().createFormDocument(id, formName);
		Element element = ActionManager.getFormManagerInstance().getLocalizedSubmitComponent(new Locale("en"));
		Element button = (Element) element.getFirstChild();
		button.setAttribute("disabled", "true");
		((Workspace) WFUtil.getBeanInstance("workspace")).setView("design");
		((Workspace) WFUtil.getBeanInstance("workspace")).setDesignViewStatus("empty");
		((Workspace) WFUtil.getBeanInstance("workspace")).setSelectedMenu("0");
		((Workspace) WFUtil.getBeanInstance("workspace")).setRenderedMenu(true);
		((FormDocument) WFUtil.getBeanInstance("formDocument")).setFormTitle(name);
		((FormDocument) WFUtil.getBeanInstance("formDocument")).setFormId(id);
		return element;
	}
	
	public String removeComponent(String id) {
		try {
			ActionManager.getFormManagerInstance().removeFormComponent(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(ActionManager.getFormManagerInstance().getFormComponentsIdsList().isEmpty()) {
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
