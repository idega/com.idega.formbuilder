package com.idega.formbuilder.business.generators;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.chiba.tools.xslt.StylesheetLoader;
import org.chiba.tools.xslt.UIGenerator;
import org.chiba.tools.xslt.XSLTGenerator;
import org.chiba.xml.xforms.exception.XFormsException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Vytautas ‰ivilis
 * @version 1.0
 * 
 * Can use absolute path to stylesheet instead of using parameters from chiba config file.
 * 
 */
public class FBXSLTGenerator extends XSLTGenerator implements UIGenerator {
	
	private HashMap userParameters = null;
    private Node inputNode = null;
    private StylesheetLoader sl = null;
    private String absolute_stylesheet_path = null;

	public FBXSLTGenerator(StylesheetLoader aLoader) throws XFormsException {
		super(aLoader);
		sl = aLoader;
	}
	
	/**
	 * method changes: can use absolute_path variable, to determine path to stylesheet
	 * stylesheet path can be set using setAbsolutePath method
	 */
	public void generate(Document output) throws XFormsException {
		
		if(absolute_stylesheet_path == null)
			super.generate(output);
		
        try {
        	Transformer transform = sl.getTemplates(absolute_stylesheet_path).newTransformer();
        	
            setTransformerParameters(transform);

            DOMSource inputDoc = new DOMSource(inputNode);
            transform.transform(inputDoc, new DOMResult(output));
        } catch (TransformerException e) {
        	
            throw new XFormsException(e.getMessageAndLocation());
        }
    }
	
	public void setInputNode(Node input) {
        this.inputNode = input;
    }
	
	/**
	 * for method changes see generate(Document output) javadoc
	 */
	public void generate(Writer responseWriter) throws XFormsException {
		
		if(absolute_stylesheet_path == null)
			super.generate(responseWriter);
		
        try {
        	
        	Transformer transform = sl.getTemplates(absolute_stylesheet_path).newTransformer();
            setTransformerParameters(transform);
            DOMSource inputDoc = new DOMSource(inputNode);
            StreamResult sr = new StreamResult(responseWriter);
            transform.transform(inputDoc, sr);
        } catch (TransformerException e) {
            throw new XFormsException(e);
        }
    }
	
	private void setTransformerParameters(Transformer transformer) {
        if (userParameters != null) {
            Iterator it = userParameters.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
                transformer.setParameter(key, userParameters.get(key));
            }
        }
    }

	public String getAbsoluteStylesheetPath() {
		return absolute_stylesheet_path;
	}

	public void setAbsoluteStylesheetPath(String absolute_path) {
		this.absolute_stylesheet_path = absolute_path;
	}
}