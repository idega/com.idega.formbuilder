package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.Date;
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
		System.out.println(new Date().toString());
		System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
		Locale current = (Locale) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE);
		if(current == null) {
			current = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}
		//String generatedId = new Long(System.currentTimeMillis()).toString();
		String id = generateId(name);
		LocalizedStringBean formName = new LocalizedStringBean();
		formName.setString(current, name);
		formManagerInstance.createFormDocument(id, formName);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_DESIGNVIEW_STATUS, "EMPTY_FORM");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_FORM_ID, id);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(FormbuilderViewManager.FORMBUILDER_CURRENT_LOCALE, current);
	}
	
	private String generateId(String name) {
		String result = "";
		result = name.replace(' ', '_');
		result += "-[" + new Date() + "]";
		return result;
	}
	
	public String removeComponent(String id) throws Exception {
		formManagerInstance.removeFormComponent(id);
		return id;
	}
	
}
