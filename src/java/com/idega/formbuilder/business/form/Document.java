package com.idega.formbuilder.business.form;

import java.util.List;

import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public interface Document extends Container {

	/**
	 * <p>
	 * Creates new form page,
	 * inserts it before specific component OR after all components in form component list.
	 * New xforms document is saved and newly created component id is returned.
	 * </p>
	 * <p>
	 * <i><b>Note: </b></i>Of course, form document should be created or imported before.
	 * </p>
	 * 
	 * @param component_after_this_id - where new component should be placed.
	 * If provided, new component will be inserted <b>before</b> component with component_after_this_id id.
	 * Provide <i>null</i> if component needs to be appended to the end of all the components.
	 * @return newly created form page id
	 * @throws FBPostponedException - see exception description at createFormDocument(..) javadoc
	 * @throws NullPointerException - form document was not created/imported before, 
	 * component_after_new_id was provided, but such component was not found in document
	 * @throws Exception - something else is wrong
	 * @return created page object
	 */
	public abstract Page addPage(String page_after_this_id) throws NullPointerException;
	
	public abstract String getFormSourceCode() throws Exception;
	
	public abstract void setFormSourceCode(String new_source_code) throws Exception;
	
	public abstract LocalizedStringBean getFormTitle();
	
	public abstract void setFormTitle(LocalizedStringBean form_name) throws FBPostponedException, Exception;
	
	/**
	 * using getContainedPagesIdList method get components id list, then use this list to change the order of components,
	 * and then call this method for changes to take an effect
	 *
	 */
	public abstract void rearrangeDocument() throws FBPostponedException, Exception;
	
	public abstract Page getPage(String page_id);
	
	public abstract List<String> getContainedPagesIdList();
}