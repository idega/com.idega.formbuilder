package com.idega.formbuilder.business.form.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.slide.business.IWSlideServiceBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class WebdavPersistenceManager implements IPersistenceManager {
	
	private static Log logger = LogFactory.getLog(WebdavPersistenceManager.class);
	
	public static IPersistenceManager getInstance() {
		
		return new WebdavPersistenceManager();
	}
	
	private boolean inited = false;
	
	public void init(String document_id) throws InstantiationException {
		
		if(document_id == null || document_id.equals(""))
			throw new InstantiationException("Document id not provided");
		
		getFormPath(document_id);
		inited = true;
	}
	
	private WebdavPersistenceManager() { }

	public void persistDocument(final Document document) throws TransformerException, InstantiationException, NullPointerException {
		
		if(!inited)
			throw new InstantiationException("Persistance manager is not initialized");
		
		if(document == null)
			throw new NullPointerException("Document is not provided");
		
		if(true)
			return;
		
		final String path_to_file = form_pathes[0];
		final String file_name = form_pathes[1];
		final IWSlideServiceBean service_bean = getServiceBean();
		
		new Thread() {
			
			public void run() {
				
				try {
					
//					TODO: find better method for doing this.
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					DOMUtil.prettyPrintDOM(document, out);
					InputStream is = new ByteArrayInputStream(out.toByteArray());
//					--
					
					service_bean.uploadFileAndCreateFoldersFromStringAsRoot(
							path_to_file, file_name,
							is, "text/xml", false
					);
					
					document_to_webdav_save_exception = null;
					
				} catch (Exception e) {
					logger.error("Exception occured while saving document to webdav dir: ", e);
					document_to_webdav_save_exception = e;
				}
			}
		}.start();
	}
	
	public Exception[] getSavedExceptions() {
		
		return document_to_webdav_save_exception != null ? new Exception[] {document_to_webdav_save_exception} : null; 
	}
	
	public boolean isInitiated() {
		return inited;
	}
	
	private IWSlideServiceBean service_bean = null;
	private Exception document_to_webdav_save_exception = null;
	private String[] form_pathes = null;
	private static final String FORMS_REPO_CONTEXT = "/files/formbuilder/forms/";
	
	private String[] getFormPath(String form_id) {
	
		if(form_pathes == null) {
			
			String path_to_file = 
			new StringBuffer(FORMS_REPO_CONTEXT)
			.append(form_id)
			.append(FormManagerUtil.slash)
			.toString();
			
			form_pathes = new String[] {path_to_file, form_id+".xforms"};			
		}
		
		return form_pathes;
	}
	
	private IWSlideServiceBean getServiceBean() {
		
		if(service_bean == null)
			service_bean = new IWSlideServiceBean();
		
		return service_bean;
	}
}
