package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.IFormManager;

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
		Iterator it = ids.iterator();
		while(it.hasNext()) {
			System.out.println((String) it.next());
		}
		formManagerInstance.rearrangeDocument();
	}
	
	public void createNewForm(String name) throws Exception {
		System.out.println("NEW FORM BEING CREATED");
		String generatedId = new Long(System.currentTimeMillis()).toString();
		String id = generatedId.substring(generatedId.length() - 8);
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(new Locale("en"), name);
		formManagerInstance.createFormDocument(id, formName);
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "EMPTY_FORM");
		//FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_FORM_ID, id);
	}
	
}
