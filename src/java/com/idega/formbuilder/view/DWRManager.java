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
		Element root = null;
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
          DocumentBuilder builder = factory.newDocumentBuilder();
          document = builder.newDocument();
          root = (Element) document.createElement("div");
          root.setAttribute("class", "formElement");
          root.setAttribute("id", "fbcomp_1");
          Element label = (Element) document.createElement("label");
          label.setAttribute("class", "label");
          label.setAttribute("id", "fbcomp_2-label");
          label.setAttribute("for", "fbcomp_1-value");
          Text text = (Text) document.createTextNode("Some label");
          label.appendChild(text);
          root.appendChild(label);
          Element input = (Element) document.createElement("input");
          input.setAttribute("class", "value");
          input.setAttribute("id", "fbcomp_2-value");
          input.setAttribute("name", "d_fbcomp_2");
          input.setAttribute("type", "text");
          input.setAttribute("value", "Some text");
          Element para = (Element) document.createElement("p");
          Text text2 = (Text) document.createTextNode("Some rtext ok okoko");
          para.appendChild(text2);
          //input.appendChild(para);
          Element input2 = (Element) document.createElement("input");
          input2.setAttribute("class", "value");
          input2.setAttribute("id", "fbcomp_2-value2");
          input2.setAttribute("name", "d_fbcomp_22");
          input2.setAttribute("type", "text");
          input2.setAttribute("value", "Some text");
          
          root.appendChild(input);
          root.appendChild(para);
          root.appendChild(input2);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
        System.out.println("Hand composed element DOM");
        DOMUtil.prettyPrintDOM(root);
		
		//System.out.println("INSIDE METHOD");
		//IFormManager fbInstance = (IFormManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(FormbuilderViewManager.FORM_MANAGER_INSTANCE);
		System.out.println("Value (type) is: " + type);
		String elementId = formManagerInstance.createFormComponent(type, null);
		Element element = formManagerInstance.getLocalizedFormHtmlComponent(elementId, new Locale("en"));
		System.out.println("Generated element DOM");
		DOMUtil.prettyPrintDOM(element);
		System.out.println(element);
		//root.appendChild(element.getFirstChild().cloneNode(true));
		//element.s
		//System.out.println("END OF METHOD");
		//GenericFieldParser.printNode(element, null, null);
		return element;
	}
	
}
