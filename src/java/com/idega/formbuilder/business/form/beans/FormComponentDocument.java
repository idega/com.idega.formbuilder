package com.idega.formbuilder.business.form.beans;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.manager.CacheManager;
import com.idega.formbuilder.business.form.manager.IXFormsManager;
import com.idega.formbuilder.business.form.manager.XFormsManagerDocument;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentDocument extends FormComponentContainer implements com.idega.formbuilder.business.form.Document {
	
	protected IFormDocument document;
	
	public void setContainerElement(Element container_element) {
		((XFormsManagerDocument)getXFormsManager()).setComponentsContainer(container_element);
	}
	
	@Override
	protected IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerDocument();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
		}
		
		return (XFormsManagerDocument)xforms_manager;
	}
	
	@Override
	public Document getXformsDocument() {
		return document.getXformsDocument();
	}
	@Override
	public void setFormDocumentModified(boolean changed) {
		document.setFormDocumentModified(changed);
	}
	@Override
	public boolean isFormDocumentModified() {
		return document.isFormDocumentModified();
	}
	@Override
	public Document getComponentsXml() {
		return document.getComponentsXml();
	}
	@Override
	public void setComponentsXml(Document xml) {
		document.setComponentsXml(xml);
	}
	@Override
	public String getFormId() {
		return document.getFormId();
	}
	@Override
	public Locale getDefaultLocale() {
		return document.getDefaultLocale();
	}
	@Override
	public Element getWizardElement() {
		return document.getWizardElement();
	}
	@Override
	public void setWizardElement(Element wizard_element) {
		document.setWizardElement(wizard_element);
	}
	public void setFormDocument(IFormDocument document) {
		this.document = document;
	}
	public Page addPage(String page_after_this_id) throws NullPointerException {
		Page page = (Page)super.addComponent(FormComponentFactory.page_type, page_after_this_id);
		componentsOrderChanged();
		return page;
	}
	@Override
	public Component addComponent(String component_type, String component_after_this_id) throws NullPointerException {
		throw new NullPointerException("Use addPage method instead");
	}
	public Page getPage(String page_id) {
		return (Page)getContainedComponent(page_id);
	}
	@Override
	public void tellComponentId(String component_id) {
		document.tellComponentId(component_id);
	}
	public List<String> getContainedPagesIdList() {
		return getContainedComponentsIdList();
	}
	@Override
	public String generateNewComponentId() {
		return document.generateNewComponentId();
	}
	
	public String getFormSourceCode() throws Exception {
		return document.getXFormsDocumentSourceCode();
	}
	
	public void setFormSourceCode(String new_source_code) throws Exception {
		document.setXFormsDocumentSourceCode(new_source_code);
	}
	
	public LocalizedStringBean getFormTitle() {
		return document.getFormTitle();
	}
	
	public void setFormTitle(LocalizedStringBean form_name) throws FBPostponedException, Exception {
		document.setFormTitle(form_name);
	}
	
	public void rearrangeDocument() throws Exception {
		rearrangeComponents();
	}
	@Override
	public void remove() {
		throw new NullPointerException("You shall not remove ME!!!!");
	}
	@Override
	public void componentsOrderChanged() {
//		TODO: look at this and rearrangeComponents methods, they look similar
		Map<String, IFormComponent> contained_components = getContainedComponents();
		int components_amount = getContainedComponents().size();
		int i = 0;
		
		for (String comp_id : getContainedComponentsIdList()) {
			
			IFormComponentPage page = (IFormComponentPage)contained_components.get(comp_id);
			if(page == null)
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
			
			page.setPageSiblings(
					i == 0 ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i - 1)),
					(i+1) == components_amount ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i + 1))
			);
			page.pagesSiblingsChanged();
			page.setFirst(i++ == 0);
		}
	}
	@Override
	public void rearrangeComponents() {
		
		List<String> contained_components_id_list = getContainedComponentsIdList();
		int components_amount = contained_components_id_list.size();
		Map<String, IFormComponent> contained_components = getContainedComponents();
		
		for (int i = components_amount-1; i >= 0; i--) {
			
			String component_id = contained_components_id_list.get(i);
			
			if(contained_components.containsKey(component_id)) {
				
				IFormComponentPage page = (IFormComponentPage)contained_components.get(component_id);
				
				if(i != components_amount-1) {
					
					page.setComponentAfterThisRerender(
						contained_components.get(
								contained_components_id_list.get(i+1)
						)
					);
				} else
					page.setComponentAfterThisRerender(null);
				
				page.setPageSiblings(
						i == 0 ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i - 1)),
						(i+1) == components_amount ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i + 1))
				);
				page.pagesSiblingsChanged();
				page.setFirst(i == 0);
				
			} else
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
		}
	}
	@Override
	public void loadContainerComponents() {
		
		super.loadContainerComponents();
		componentsOrderChanged();
	}
}