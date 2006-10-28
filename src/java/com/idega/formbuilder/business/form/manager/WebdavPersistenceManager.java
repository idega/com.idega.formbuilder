package com.idega.formbuilder.business.form.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWSlideService;
import com.idega.slide.business.IWSlideSession;
import com.idega.slide.util.WebdavExtendedResource;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class WebdavPersistenceManager implements IPersistenceManager {
	
	private static Log logger = LogFactory.getLog(WebdavPersistenceManager.class);
	
	private Exception document_to_webdav_save_exception = null;
	private String[] form_pathes = null;
	private boolean inited = false;

	protected static final String FORMS_PATH = "/files/forms";
	protected static final String FORMS_FILE_EXTENSION = ".xhtml";
	
	public static IPersistenceManager getInstance() {
		
		return new WebdavPersistenceManager();
	}
	
	public void init(String document_id) throws NullPointerException {
		
		clear();
		
		if(document_id == null || document_id.equals(""))
			throw new NullPointerException("Document id not provided");
		
		getFormPath(document_id);
		inited = true;
	}
	
	protected WebdavPersistenceManager() { }
	
	/**
	 * see IPersistenceManager javadoc for additional info
	 * 
	 * Implementation specific:
	 * <p>
	 * <b>imporant:</b> method uses thread to upload file.
	 * So, if something bad happens during this process
	 * exceptions thrown are saved.<br />
	 * Those excepptions should be time to time checked.<br />
	 * Exceptions are set to null everytime, when no exception is thrown.<br />
	 * That means, successful request overrides unsuccessful traces.
	 * </p>
	 */
	public void persistDocument(final Document document) throws InitializationException, NullPointerException {
		
		if(!isInitiated())
			throw new InitializationException("Persistence manager is not initialized");
		
		if(document == null)
			throw new NullPointerException("Document is not provided");
		
		if(true)
			persistDocument2(document);
		
		final String path_to_file = form_pathes[0];
		final String file_name = form_pathes[1];
		final IWSlideService service_bean = getServiceBean();
		
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
	
	public void persistDocument2(final Document document) throws InitializationException, NullPointerException {

		
		final WebdavExtendedResource webdav_resource = getWebdavResource();
		
		new Thread() {
			
			public void run() {
				
				try {
					
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					DOMUtil.prettyPrintDOM(document, out);
					InputStream is = new ByteArrayInputStream(out.toByteArray());
					
					webdav_resource.putMethod(is);
					
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
	
	protected String[] getFormPath(String form_id) {
	
		if(form_pathes == null) {			
			form_pathes = new String[] {FORMS_PATH+"/"+form_id+"/", form_id+FORMS_FILE_EXTENSION};			
		}
		
		return form_pathes;
	}
	
	protected void clear() {
		document_to_webdav_save_exception = null;
		form_pathes = null;
		inited = false;
		webdav_resource = null;
	}
	
	private IWSlideService getServiceBean() {
		
		IWSlideService service_bean = null;
		try {
			
			service_bean = (IWSlideService)IBOLookup.getServiceInstance(IWContext.getInstance(), IWSlideService.class);
			
		} catch (IBOLookupException e) {
			
			logger.error("Error during lookup for IWSlideService", e);
		}
		
		return service_bean;
	}
	
	public Document loadDocument() throws InitializationException, Exception {
		
		if(!isInitiated())
			throw new InitializationException("Persistence manager is not initialized");

		InputStream is = getWebdavResource().getMethodData();
		return FormManagerUtil.getDocumentBuilder().parse(is);
	}
	
	protected WebdavExtendedResource webdav_resource;
	
	protected WebdavExtendedResource getWebdavResource() {
		
		if(webdav_resource == null) {
			
			try {
				
				IWSlideSession session = (IWSlideSession) IBOLookup.getSessionInstance(IWContext.getInstance(), IWSlideSession.class);
				
				webdav_resource = session.getWebdavResource(form_pathes[0]+form_pathes[1]);
				webdav_resource.setProperties();
				
			} catch (Exception e) {
				
				logger.error("Error getting WebdavExtendedResource", e);
			}
		}		
		return webdav_resource;
	}
	
}
