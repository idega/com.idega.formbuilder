package com.idega.formbuilder.sandbox;

import java.io.File;
import java.io.FileOutputStream;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;

import com.idega.formbuilder.business.form.manager.IPersistenceManager;
import com.idega.formbuilder.business.form.manager.WebdavPersistenceManager;
import com.idega.formbuilder.business.form.manager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class SandboxPersistenceManager extends WebdavPersistenceManager {
	
	public static IPersistenceManager getInstance() {
		
		return new SandboxPersistenceManager();
	}
	
	protected SandboxPersistenceManager() { }
	
	public void persistDocument(final Document document) throws InitializationException, NullPointerException {
		
		if(!isInitiated())
			throw new InitializationException("Persistance manager is not initialized");
		
		if(document == null)
			throw new NullPointerException("Document is not provided");
		
		new Thread() {
			
			public void run() {
				
				try {
					System.out.println("writing document to file");
					
					synchronized (SandboxPersistenceManager.class) {
						
						DOMUtil.prettyPrintDOM(document);
					
						DOMUtil.prettyPrintDOM(document, new FileOutputStream(
							new File(SandboxUtil.GENERATED_DOCUMENT_LOCATION)));
						
					}
						
					System.out.println("done writing document to file");
					
				} catch (Exception e) {
					
					some_crazy_exception = e;
				}
			}
		}.start();
	}
	
	private Exception some_crazy_exception;
	
	public Exception[] getSavedExceptions() {

		return some_crazy_exception != null ? new Exception[] {some_crazy_exception} : null;
	}
}
