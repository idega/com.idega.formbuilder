package com.idega.formbuilder.view;

import java.util.Locale;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.util.FBUtil;
import com.idega.webface.WFUtil;

public class DWRManager {
	
	public void createNewFormDocument(String title) {
		Workspace workspace = (Workspace) WFUtil.getBeanInstance("workspace");
		if(workspace != null) {
			Locale locale = workspace.getLocale();
			DocumentManager formManagerInstance = ActionManager.getDocumentManagerInstance();
			Document document = null;
			String id = FBUtil.generateFormId(title);
			LocalizedStringBean formName = new LocalizedStringBean();
			formName.setString(locale, title);
			
			try {
				document = formManagerInstance.createForm(id, formName);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			workspace.setView("design");
			workspace.setDesignViewStatus("empty");
			workspace.setSelectedMenu("0");
			workspace.setRenderedMenu(true);
			
			FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance("formDocument");
			formDocument.clearFormDocumentInfo();
			formDocument.setFormTitle(title);
			formDocument.setFormId(id);
			formDocument.setDocument(document);
			
			Page page = document.getPage(document.getContainedPagesIdList().get(0));
			FormPage formPage = (FormPage) WFUtil.getBeanInstance("formPage");
			if(formPage != null) {
				formPage.setPage(page);
			}
			FormComponent formComponent = (FormComponent) WFUtil.getBeanInstance("formComponent");
			if(formComponent != null) {
				formComponent.clearFormComponentInfo();
			}
		}
	}
	
}
