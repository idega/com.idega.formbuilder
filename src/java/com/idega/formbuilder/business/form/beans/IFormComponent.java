package com.idega.formbuilder.business.form.beans;

import com.idega.formbuilder.business.form.PropertiesComponent;
import com.idega.formbuilder.business.form.manager.IXFormsManager;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public interface IFormComponent {

	public abstract void render();

	public abstract void setComponentAfterThis(IFormComponent component);
	
	public abstract IFormComponent getComponentAfterThis();
	
	public abstract void setComponentAfterThisRerender(IFormComponent component);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract void setType(String type);
	
	public abstract String getType();

	public abstract PropertiesComponent getProperties();
	
	public abstract void remove();
	
	public abstract void setLoad(boolean load);
	
	public abstract void setParent(IFormComponentContainer parent);
	
	public abstract IXFormsManager getComponentXFormsManager();
	
	/**
	 * method for doing some additional tasks if component is first in container
	 * shouldn't do rendering, as it's done elsewhere
	 * @param first
	 */
	public abstract void setFirst(boolean first);
}