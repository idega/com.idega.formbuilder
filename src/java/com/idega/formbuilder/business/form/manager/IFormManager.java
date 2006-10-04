package com.idega.formbuilder.business.form.manager;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.FormPropertiesBean;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;


/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public interface IFormManager {

	/**
	 * creates primary user form document and stores it depending on implementation
	 * 
	 * @param form_props - primary form description. Only id is mandatory.
	 * @throws FBPostponedException - if some kind of exception happened during previous request. FormManager user knows,
	 * that error happened and can (most likely) happen again, so some adequate actions can be taken.
	 * @throws NullPointerException - form_props is null or id not provided
	 * @throws Exception - some kind of other error occured
	 */
	public abstract void createFormDocument(FormPropertiesBean form_properties)
			throws FBPostponedException, NullPointerException, Exception;

	/**
	 * __NOT IMPLEMENTED YET__
	 * @param component_id - form component id to remove
	 * @throws FBPostponedException - see exception description at createFormDocument(..) javadoc 
	 * @throws NullPointerException - form document is not created
	 */
	public abstract void removeFormComponent(String component_id)
			throws FBPostponedException, NullPointerException;

	/**
	 * <p>
	 * Creates new form component by component type provided,
	 * inserts it after specific component OR after all components in form component list.
	 * New xforms document is saved depending on implementation and component id is returned.
	 * </p>
	 * <p>
	 * <i><b>ATTENTION: </b></i>Of course form document should be created or imported before.
	 * </p>
	 * 
	 * @param component_type - type of component from components_types list,
	 * which should be inserted to form document.
	 * @param component_after_new_id - where new component should be places. This id must come from
	 * currently editing form document component. Provide <i>null</i> if component needs to be appended
	 * to other components list.
	 * @return newly created form component id
	 * @throws FBPostponedException - see exception description at createFormDocument(..) javadoc
	 * @throws NullPointerException - form document was not created first, 
	 * component_after_new_id was provided, but such component was not found, other..
	 * @throws Exception - something else is wrong
	 */
	public abstract String createFormComponent(String component_type,
			String component_after_new_id) throws FBPostponedException,
			NullPointerException, Exception;

	/**
	 * 
	 * @return List of available form components types to place on form
	 */
	public abstract List<String> getAvailableFormComponentsList() throws FBPostponedException;

	/**
	 * 
	 * @return List of all form components, placed on current form
	 */
	public abstract List<String> getFormComponentsList();
	
	public abstract Element getLocalizedFormHtmlComponent(String component_id, Locale locale) throws FBPostponedException, NullPointerException;
}