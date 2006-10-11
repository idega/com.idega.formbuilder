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
	
	public void init(String document_id) throws NullPointerException {
		
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
			throw new InitializationException("Persistance manager is not initialized");
		
		if(document == null)
			throw new NullPointerException("Document is not provided");
		
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
	
	public Exception[] getSavedExceptions() {
		
		return document_to_webdav_save_exception != null ? new Exception[] {document_to_webdav_save_exception} : null; 
	}
	
	public boolean isInitiated() {
		return inited;
	}
	
	private Exception document_to_webdav_save_exception = null;
	private String[] form_pathes = null;
	private static final String FORMS_REPO_CONTEXT = "/files/formbuilder/forms/";
	
	private String[] getFormPath(String form_id) {
	
		if(form_pathes == null) {
			
			String path_to_file = 
			new StringBuffer(forms_path == null ? FORMS_REPO_CONTEXT : forms_path)
			.append(form_id)
			.append(FormManagerUtil.slash)
			.toString();
			
			form_pathes = new String[] {path_to_file, form_id+".xforms"};			
		}
		
		return form_pathes;
	}
	
	private String forms_path;
	
	public void setFormsPath(String path) {
		forms_path = path;
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
}
