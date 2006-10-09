package com.idega.formbuilder.business.form.beans;

import java.util.Map;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * <i><b>WARNING: </b></i>for changes to take effect, u need to use setter methods for every property change
 * 
 */
public interface IComponentSelectProperties extends IComponentProperties {
	
	public abstract String getExternalDataSrc();
	
	public abstract void setExternalDataSrc(String external_data_src);
	
	public abstract Map<String, String> getStaticDataSrc();
	
	public abstract void setStaticDataSrc(Map<String, String> static_data_src);
	
	public abstract LocalizedStringBean getEmptyElementLabel();
	
	public abstract void setEmptyElementLabel(LocalizedStringBean empty_element_label);
}