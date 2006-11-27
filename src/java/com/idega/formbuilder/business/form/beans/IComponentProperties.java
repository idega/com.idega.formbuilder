package com.idega.formbuilder.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * <i><b>WARNING: </b></i>for changes to take effect, u need to use setter methods for every property change
 */
public interface IComponentProperties {
	
	public abstract LocalizedStringBean getErrorMsg();

	public abstract void setErrorMsg(LocalizedStringBean error_msg);

	public abstract LocalizedStringBean getLabel();

	public abstract void setLabel(LocalizedStringBean label);

	public abstract boolean isRequired();

	public abstract void setRequired(boolean required);
	
	public abstract Integer getPhaseNumber();
	
	public abstract void setPhaseNumber(Integer phase_number);
}