package com.idega.formbuilder.view;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.beans.ItemBean;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.beans.FormComponent;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.formbuilder.presentation.beans.FormPage;
import com.idega.formbuilder.presentation.beans.Workspace;
import com.idega.formbuilder.util.FBUtil;
import com.idega.webface.WFUtil;

public class DWRManager implements Serializable {
	
	private static final long serialVersionUID = -753995343458793992L;
	
	public void removeItem(int index) {
		List<ItemBean> items = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		if(index < items.size()) {
			items.remove(index);
			((FormComponent) WFUtil.getBeanInstance("formComponent")).setItems(items);
		}
	}
	
	public void saveLabel(int index, String value) {
		List<ItemBean> items = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		if(index >= items.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setLabel(value);
			newItem.setValue(value);
			items.add(newItem);
		} else {
			items.get(index).setLabel(value);
			items.get(index).setValue(value);
		}
		((FormComponent) WFUtil.getBeanInstance("formComponent")).setItems(items);
	}
	
	public void saveValue(int index, String value) {
		List<ItemBean> items = ((FormComponent) WFUtil.getBeanInstance("formComponent")).getItems();
		if(index >= items.size()) {
			ItemBean newItem = new ItemBean();
			newItem.setValue(value);
			items.add(newItem);
		} else {
			items.get(index).setValue(value);
		}
		((FormComponent) WFUtil.getBeanInstance("formComponent")).setItems(items);
	}
	
	public void updateComponentList(String idSequence, String idPrefix, String delimiter) throws Exception {
		Page page = ((FormPage) WFUtil.getBeanInstance("formPage")).getPage();
		List<String> ids = page.getContainedComponentsIdList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			ids.add(idPrefix + tokenizer.nextToken());
		}
		page.rearrangeComponents();
		Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
		if(document != null) {
			document.save();
		}
	}
	
	public void updatePagesList(String idSequence, String idPrefix, String delimiter) throws Exception {
		Document document = ((FormDocument) WFUtil.getBeanInstance("formDocument")).getDocument();
//		List<String> idsList = ((FormPage) WFUtil.getBeanInstance("formPage")).getCommonPagesIdList(document);
		String confirmId = "";
		String thxId = "";
		Page temp = document.getConfirmationPage();
		if(temp != null) {
			confirmId = temp.getId();
		}
		temp = document.getThxPage();
		if(temp != null) {
			thxId = temp.getId();
		}
		List<String> ids = document.getContainedPagesIdList();
		ids.clear();
		String test = "&" + idSequence;
		StringTokenizer tokenizer = new StringTokenizer(test, delimiter);
		while(tokenizer.hasMoreTokens()) {
			ids.add(idPrefix + tokenizer.nextToken());
		}
		ids.add(confirmId);
		ids.add(thxId);
		document.rearrangeDocument();
		if(document != null) {
			document.save();
		}
	}
	
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
