package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.idega.formbuilder.FormbuilderViewManager;
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
		//System.out.println("Generated element DOM");
		//DOMUtil.prettyPrintDOM(element);
		//System.out.println(element);
		//root.appendChild(element.getFirstChild().cloneNode(true));
		//element.s
		//System.out.println("END OF METHOD");
		//GenericFieldParser.printNode(element, null, null);
		return element;
	}
	
}
