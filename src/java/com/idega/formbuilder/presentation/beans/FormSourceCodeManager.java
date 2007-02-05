package com.idega.formbuilder.presentation.beans;

import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.formbuilder.view.ActionManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 */
public class FormSourceCodeManager implements ActionListener {
	
	private static Log logger = LogFactory.getLog(FormSourceCodeManager.class);
	
	private String source_code;
	
	public void processAction(ActionEvent event) {
		
		if(source_code == null)
			return;
		
		try {
			
			/*DocumentManager form_manager =*/ ActionManager.getDocumentManagerInstance();
//			form_manager.setFormSourceCode(source_code);
			
		} catch (Exception e) {
			logger.error("Error when setting form source code", e);
		}
		
	}
	
	public String getSourceCode() {
		
//		try {
//			
//			DocumentManager form_manager = ActionManager.getDocumentManagerInstance();
//			
//			if(form_manager != null) {
//				
//				return form_manager.getFormSourceCode();
//			}
//			
//		} catch (Exception e) {
//			logger.error("Error when getting form source code", e);
//		}
		
		return "";
	}
	
	public void setSourceCode(String source_code) {
		
		this.source_code = source_code;
	}
}
