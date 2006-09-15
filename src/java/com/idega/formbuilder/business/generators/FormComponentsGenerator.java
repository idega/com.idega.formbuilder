package com.idega.formbuilder.business.generators;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.locking.LockException;
import org.chiba.tools.xslt.StylesheetLoader;
import org.chiba.tools.xslt.UIGenerator;
import org.chiba.xml.xforms.exception.XFormsException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.idega.formbuilder.business.generators.FBXSLTGenerator;
import com.idega.repository.data.Singleton;
import com.idega.xml.XMLException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Initial components description is kept in xforms document.<br />
 * This class parses all those components to html format components into xml document.
 * 
 */
public class FormComponentsGenerator implements Singleton, IComponentsGenerator  {
	
	private static Log logger = LogFactory.getLog(FormComponentsGenerator.class);
	
	protected static IComponentsGenerator me = null;
	private String absolute_components_xforms_path = null;
	private String absolute_components_xforms_stylesheet_path = null;
	private String absolute_components_stylesheet_path = null;
	
	protected final int params_cnt = 3;
	private Document xforms_doc = null;
	private int locked_cnt = 0;
	
	/**
	 * TODO: checkout how to write this method effective
	 */
	public static synchronized IComponentsGenerator getInstance() {
		
		if (me == null) {
			me = new FormComponentsGenerator();
		}

		return me;
	}
	
	private FormComponentsGenerator() { }

	/* (non-Javadoc)
	 * @see com.idega.formbuilder.generators.IComponentsGenerator#isInitiated()
	 */
	public boolean isInitiated() {
		
		return absolute_components_xforms_stylesheet_path != null && 
		absolute_components_stylesheet_path != null &&
		(absolute_components_xforms_path != null || xforms_doc != null);
	}
	
	/**
	 * 
	 *  @param params -	full pathes with file names:<br />
	 *  [0] - absolute components xforms path - should be not null if xforms document is not set explicitly<br />
	 *  [1] - absolute components xforms stylesheet path - stylesheet (xsl) with conversion from xforms to components html - xml<br />
	 *  [2] - absolute components stylesheet path - stylesheet (xsl) with conversion from html - xml to plain components xml<br />
	 * 
	 * @throws LockException - if someone is using generateBaseComponentsDocument method
	 * @throws IndexOutOfBoundsException - if not enough parameters are passed.
	 * 
	 */
	public synchronized void init(String[] params) throws LockException, IndexOutOfBoundsException {
		
		if(locked_cnt > 0)
			throw new LockException("generateBaseComponentsDocument is running, you cannot lock now", 0, null);
		
		//TODO: is this solution correct?
		locked_cnt = -1;
		initParams(params);
		locked_cnt = 0;
	}
	
	protected void initParams(String[] params) throws IndexOutOfBoundsException {

		if(params.length < params_cnt)
			throw new IndexOutOfBoundsException("Not enough parameters passed. I want to get "+params_cnt);
		
		absolute_components_xforms_path = params[0];
		absolute_components_xforms_stylesheet_path = params[1];
		absolute_components_stylesheet_path = params[2];
	}
	
	public void setXFormsDocument(Document xforms_doc) throws NullPointerException {
		
		if(xforms_doc != null)
			this.xforms_doc = xforms_doc;
		else
			throw new NullPointerException("Use removeXFormsDocument method, if you want to remove it.");
	}
	
	private static final String NO_XFORMS_DOC_DEFINITION_MSG = "Set absolute components xforms path first. Either xforms document or path should not be null";
	public void removeXFormsDocument() throws NullPointerException {
		
		if(absolute_components_xforms_path == null)
			throw new NullPointerException(NO_XFORMS_DOC_DEFINITION_MSG);
			
		xforms_doc = null;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.formbuilder.generators.IComponentsGenerator#generateBaseComponentsDocument()
	 */
	public synchronized Document generateBaseComponentsDocument() throws XMLException, IOException, XFormsException, LockException  {
		
		if(locked_cnt < 0) {
			logger.error("generateBaseComponentsDocument(). Tried to call, while initializing.");
			throw new LockException("Call after initialization.", 0, null);
		}
		
		locked_cnt++;
		if(!isInitiated()) {

			locked_cnt--;
			logger.error("generateBaseComponentsDocument() was called, when not initiated. Should not happen. ");
			throw new NullPointerException(NO_XFORMS_DOC_DEFINITION_MSG);
		}
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        
	        factory.setNamespaceAware(true);
	        factory.setValidating(false);
	        DocumentBuilder document_builder = factory.newDocumentBuilder();
	        
	        synchronized(this) {
	        	
	        	if(xforms_doc == null && absolute_components_xforms_path != null) {
		        	
		        	xforms_doc = document_builder.parse(
		        			new FileInputStream(absolute_components_xforms_path)
		        	);
		        }
	        }

	        /*
	         * generate temporal xml document from components xforms document
	         */
	        UIGenerator gen = new FBXSLTGenerator(new StylesheetLoader(null));
	        
	        ((FBXSLTGenerator)gen).setAbsoluteStylesheetPath(absolute_components_xforms_stylesheet_path);
	        gen.setInputNode(xforms_doc);
	        
	        Document temp_xml_doc = document_builder.newDocument();
	        gen.setOutput(temp_xml_doc);
        	
        	gen.generate();
        	
        	/*
        	 * generate final components xml
        	 */
        	((FBXSLTGenerator)gen).setAbsoluteStylesheetPath(absolute_components_stylesheet_path);
        	gen.setInputNode(temp_xml_doc);
        	
        	temp_xml_doc = document_builder.newDocument();
        	gen.setOutput(temp_xml_doc);
        	
        	gen.generate();
        	
        	return temp_xml_doc;
        	
		} catch (ParserConfigurationException e) {
			
			throw new XMLException(e.getMessage(), e);
		
		} catch (SAXException e) {
			
			throw new XMLException(e.getMessage(), e);
		} finally {
			locked_cnt--;
		}
	}
}
