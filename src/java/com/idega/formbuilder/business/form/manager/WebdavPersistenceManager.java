package com.idega.formbuilder.business.form.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.webdav.lib.PropertyName;
import org.apache.webdav.lib.WebdavResource;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;

import com.idega.block.form.business.AvailableFormsLister;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;
import com.idega.formbuilder.business.form.manager.util.InitializationException;
import com.idega.presentation.IWContext;
import com.idega.slide.business.IWSlideService;
import com.idega.slide.business.IWSlideSession;
import com.idega.slide.util.WebdavExtendedResource;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas �ivilis</a>
 * @version 1.0
 * 
 */
public class WebdavPersistenceManager implements IPersistenceManager {
	
	private static Log logger = LogFactory.getLog(WebdavPersistenceManager.class);
	
	private Exception document_to_webdav_save_exception = null;
	private String[] form_pathes = null;
	private boolean inited = false;

	public static final String FORMS_PATH = "/files/forms";
	public static final String FORMS_FILE_EXTENSION = ".xhtml";
	
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
	
//	/**
//	 * see IPersistenceManager javadoc for additional info
//	 * 
//	 * Implementation specific:
//	 * <p>
//	 * <b>imporant:</b> method uses thread to upload file.
//	 * So, if something bad happens during this process
//	 * exceptions thrown are saved.<br />
//	 * Those excepptions should be time to time checked.<br />
//	 * Exceptions are set to null everytime, when no exception is thrown.<br />
//	 * That means, successful request overrides unsuccessful traces.
//	 * </p>
//	 */
//	public void persistDocument(Document document) throws InitializationException, NullPointerException, Exception {
//		
//		if(!isInitiated())
//			throw new InitializationException("Persistence manager is not initialized");
//		
//		if(document == null)
//			throw new NullPointerException("Document is not provided");
//		
//		final WebdavExtendedResource webdav_resource = getWebdavResource();
//		final ByteArrayOutputStream out = new ByteArrayOutputStream();
//		DOMUtil.prettyPrintDOM(document, out);
//		
//		System.out.println("we are saving: ");
//		DOMUtil.prettyPrintDOM(document);
//		
//		new Thread() {
//			
//			public void run() {
//				
//				InputStream is = null;
//				
//				try {
//					
//					is = new ByteArrayInputStream(out.toByteArray());
//					
//					System.out.println("saving to webdav: "+is);
//					
//					webdav_resource.putMethod(is);
//					
//					document_to_webdav_save_exception = null;
//					
//					long end = System.currentTimeMillis();
//					System.out.println("saved: "+end);
//					
//				} catch (Exception e) {
//					logger.error("Exception occured while saving document to webdav dir: ", e);
//					
//					document_to_webdav_save_exception = e;
//				}
////				finally {
////					if(is != null) {
////						try {
////							is.close();
////						} catch (Exception e) {
////						}
////					}
////				}
//			}
//		}.start();
//	}
	
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
	public void persistDocument(Document document) throws InitializationException, NullPointerException, Exception {
		
		if(!isInitiated())
			throw new InitializationException("Persistence manager is not initialized");
		
		if(document == null)
			throw new NullPointerException("Document is not provided");
		
		final String path_to_file = form_pathes[0];
		final String file_name = form_pathes[1];
		final IWSlideService service_bean = getServiceBean();
		
//		TODO: find better method for doing this.
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMUtil.prettyPrintDOM(document, out);
		final String form_title = lookupFormTitleFromDocument(document);
		
		final PropertyName property_name = AvailableFormsLister.getInstance().getFormPropertyName();
		
		new Thread() {
			
			public void run() {
				
				try {
					
					InputStream is = new ByteArrayInputStream(out.toByteArray());
					service_bean.uploadFileAndCreateFoldersFromStringAsRoot(
							path_to_file, file_name,
							is, "text/xml", false
					);
					
					WebdavResource webdav_res = service_bean.getWebdavResourceAuthenticatedAsRoot(path_to_file);
					webdav_res.proppatchMethod(property_name, form_title, true);
					
					AvailableFormsLister.getInstance().setAvailableFormsChanged();

					document_to_webdav_save_exception = null;
					
				} catch (Exception e) {
					logger.error("Exception occured while saving document to webdav dir: ", e);
					
					document_to_webdav_save_exception = e;
				}
			}
		}.start();
	}
	
	public Exception[] getSavedExceptions() {
		
		Exception[] saved_exceptions = document_to_webdav_save_exception != null ? new Exception[] {document_to_webdav_save_exception} : null;
		document_to_webdav_save_exception = null;
		
		return saved_exceptions; 
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
	}
	
	public Document loadDocument() throws InitializationException, Exception {
		if (!isInitiated())
			throw new InitializationException("Persistence manager is not initialized");
		
		IWSlideSession session = getIWSlideSession();
		Document document = null;
		try {
			WebdavExtendedResource webdav_resource = session.getWebdavResource(form_pathes[0] + form_pathes[1]);
			if (webdav_resource.exists())
				webdav_resource.setProperties();
			InputStream is = webdav_resource.getMethodData();
			document = FormManagerUtil.getDocumentBuilder().parse(is);
			webdav_resource.close();
		}
		catch (IOException e) {
			logger.error("Error reading document from Webdav", e);
		}
		return document;
	}
	
	protected IWSlideSession getIWSlideSession() {
		IWSlideSession session = null;
		try {
			session = (IWSlideSession) IBOLookup.getSessionInstance(IWContext.getInstance(), IWSlideSession.class);
		}
		catch (Exception e) {
			logger.error("Error getting IWSlideSession", e);
		}
		return session;
	}
	
	IWSlideService service_bean;
	
	protected IWSlideService getServiceBean() {
		
		if(service_bean == null) {
			
			try {
				service_bean = (IWSlideService)IBOLookup.getServiceInstance(IWContext.getInstance(), IWSlideService.class);
				
			} catch (IBOLookupException e) {
				
				logger.error("Error during lookup for IWSlideService", e);
			}
		}
		
		return service_bean;
	}
	
	protected String lookupFormTitleFromDocument(Document form_document) {
		LocalizedStringBean localized_title = FormManagerUtil.getTitleLocalizedStrings(form_document);
		
		if(localized_title == null)
			return "";
		
		Locale default_locale = FormManagerUtil.getDefaultFormLocale(form_document);
		
		String title = localized_title.getString(default_locale);
		
		return title == null ? "" : title;
	}
}