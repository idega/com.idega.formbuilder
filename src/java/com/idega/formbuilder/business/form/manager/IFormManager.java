package com.idega.formbuilder.business.form.manager;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.beans.ComponentPropertiesSubmitButton;
import com.idega.formbuilder.business.form.beans.IComponentProperties;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.util.FBPostponedException;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public interface IFormManager {

	/**
	 * creates primary user form document and stores it depending on persistance manager implementation
	 * 
	 * @param form_id - cannot be null
	 * @param name - form name. Can be null, then default is used.
	 *  
	 * @throws FBPostponedException - if some kind of exception happened during previous request.
	 * @throws NullPointerException - form_id was not provided
	 * @throws Exception - some kind of other error occured
	 */
	public abstract void createFormDocument(String form_id, LocalizedStringBean name)
			throws FBPostponedException, NullPointerException, Exception;

	/**
	 * Removes component from form document
	 * 
	 * @param component_id - form component id to remove
	 * @throws FBPostponedException - see exception description at createFormDocument(..) javadoc 
	 * @throws NullPointerException
	 */
	public abstract void removeFormComponent(String component_id)
			throws FBPostponedException, NullPointerException, Exception;

	/**
	 * <p>
	 * Creates new form component by component type provided,
	 * inserts it before specific component OR after all components in form component list.
	 * New xforms document is saved depending on implementation and newly created component id is returned.
	 * </p>
	 * <p>
	 * <i><b>ATTENTION: </b></i>Of course, form document should be created or imported before.
	 * </p>
	 * 
	 * @param component_type - type of component, which should be created on form document - types can be got using getFormComponentsList()
	 * @param component_after_this_id - where new component should be placed.
	 * If provided, new component will be inserted <b>before</b> component with component_after_this_id id.
	 * Provide <i>null</i> if component needs to be appended to the end of document.
	 * @return newly created form component id
	 * @throws FBPostponedException - see exception description at createFormDocument(..) javadoc
	 * @throws NullPointerException - form document was not created/imported before, 
	 * component_after_new_id was provided, but such component was not found in document, __NOT SPECIFIED__
	 * @throws Exception - something else is wrong (earthquake or smth)
	 */
	public abstract String createFormComponent(String component_type,
			String component_after_this_id) throws FBPostponedException,
			NullPointerException, Exception;

	/**
	 * 
	 * @return List of available form components types
	 */
	public abstract List<String> getAvailableFormComponentsTypesList() throws FBPostponedException;

	/**
	 * 
	 * @return List of all form components ids, placed on current form
	 */
	public abstract List<String> getFormComponentsIdsList();

	/**
	 * 
	 * Returns localized component html representation
	 * 
	 * @param component_id
	 * @param locale
	 * @return
	 * @throws FBPostponedException - see exception description at createFormDocument(..) javadoc
	 * @throws NullPointerException - component by such an id was not found, locale was not provided, ...
	 */
	public abstract Element getLocalizedFormHtmlComponent(String component_id, Locale locale) throws FBPostponedException, NullPointerException;
	
	/**
	 * use getFormComponentsIdsList(), use this list to change the order of components,
	 * then call this method
	 *
	 */
	public abstract void rearrangeDocument() throws FBPostponedException, Exception;
	
	public abstract void updateFormComponent(String component_id) throws FBPostponedException, NullPointerException, Exception;
	
	/**
	 * 
	 * @param component_id
	 * @return return specific class component's properties. U still need to know about concrete classes,
	 * so u can use their specific methods, if needed.
	 */
	public abstract IComponentProperties getComponentProperties(String component_id);
	
	public ComponentPropertiesSubmitButton getSubmitButtonProperties();
	
	public Element getLocalizedSubmitComponent(Locale locale) throws FBPostponedException, NullPointerException;
}