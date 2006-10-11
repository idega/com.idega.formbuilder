package com.idega.formbuilder.business.form.manager.generators;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.chiba.adapter.ui.UIGenerator;
import org.chiba.adapter.ui.XSLTGenerator;
import org.chiba.xml.xforms.exception.XFormsException;
import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.repository.data.Singleton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Initial components description is kept in xforms document.<br />
 * This class parses all those components to html format components into xml document.
 * 
 */
public class FormComponentsGenerator implements Singleton, IComponentsGenerator  {
	
	protected static FormComponentsGenerator me = null;
	
	private URI components_xforms_stylesheet_uri;
	private URI components_stylesheet_uri;
	
	private TransformerService transf_service;
	private UIGenerator components_generator;
	private UIGenerator components_xforms_generator;
	
	private Document xforms_doc = null;
	
	public static FormComponentsGenerator getInstance() {
		
		if (me == null) {
			
			synchronized (FormComponentsGenerator.class) {
				if (me == null) {
					me = new FormComponentsGenerator();
				}
			}
		}

		return me;
	}
	
	protected FormComponentsGenerator() { }

	public boolean isInitiated() {
		
		return components_xforms_stylesheet_uri != null && 
		components_stylesheet_uri != null &&
		xforms_doc != null && transf_service != null;
	}
	
	public void setDocument(Document xforms_doc) {
		
		this.xforms_doc = xforms_doc;
	}
	
	public Document generateBaseComponentsDocument() throws NullPointerException, ParserConfigurationException, XFormsException {
		
		if(!isInitiated()) {
			
			String err_msg = new StringBuffer("Either is not provided:")
			.append("\ncomponents xforms stylesheet uri: ")
			.append(components_xforms_stylesheet_uri)
			.append("\ncomponents stylesheet uri: ")
			.append(components_stylesheet_uri)
			.append("\nxforms doc: ")
			.append(xforms_doc)
			.append("\ntransformer service: ")
			.append(transf_service)
			.toString();
			
			throw new NullPointerException(err_msg);
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        
        /*
         * generate temporal xml document from components xforms document
         */
        UIGenerator gen = getComponentsXFormsGenerator();
        
        gen.setInput(xforms_doc);
        
//        	TODO: there could be only one stylesheet used for all this, do it if u master it enough for components.xsl
    	copyLocalizationKeysToElements(xforms_doc);
    	
    	DocumentBuilder document_builder = factory.newDocumentBuilder();
    	
        Document temp_xml_doc = document_builder.newDocument();
        gen.setOutput(temp_xml_doc);
    	
    	gen.generate();
    	
    	/*
    	 * generate final components xml
    	 */
    	gen = getComponentsGenerator();
    	gen.setInput(temp_xml_doc);
    	
    	temp_xml_doc = document_builder.newDocument();
    	gen.setOutput(temp_xml_doc);
    	
    	gen.generate();
    	
    	return temp_xml_doc;
	}
	
	private static void copyLocalizationKeysToElements(Document managed_doc) {
		
		Element components_container = (Element)managed_doc.getElementsByTagName("xf:group").item(0);
		
		NodeList child_elements = components_container.getElementsByTagName("*");
		
		for (int i = 0; i < child_elements.getLength(); i++) {
			Element child = (Element)child_elements.item(i);
			
			if(child.hasAttribute("ref")) {
				
				String ref = child.getAttribute("ref");
				
				if(!FormManagerUtil.isRefFormCorrect(ref))
					continue;
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				Node key_node = managed_doc.createTextNode(key);
				child.appendChild(key_node);
			}
		}
	}
	
	public void setComponentsXformsStylesheetUri(URI uri) {
		components_xforms_stylesheet_uri = uri;
	}

	public void setComponentsStylesheetUri(URI uri) {
		components_stylesheet_uri = uri;
	}
	
	public void setTransformerService(TransformerService transf_service) {
		this.transf_service = transf_service;
	}
	
	protected UIGenerator getComponentsGenerator() {
		
		if(components_generator == null) {
			
			synchronized (this) {
				
				if(components_generator == null) {
					XSLTGenerator gen = new XSLTGenerator();
					gen.setTransformerService(transf_service);
					gen.setStylesheetURI(components_stylesheet_uri);
					
					components_generator = gen;
				}
			}
		}
		return components_generator;
	}
	
	protected UIGenerator getComponentsXFormsGenerator() {
		
		if(components_xforms_generator == null) {
			
			synchronized (this) {
				
				if(components_xforms_generator == null) {
					XSLTGenerator gen = new XSLTGenerator();
					gen.setTransformerService(transf_service);
					gen.setStylesheetURI(components_xforms_stylesheet_uri);
					
					components_xforms_generator = gen;
				}
			}
		}
		return components_xforms_generator;
	}
}
