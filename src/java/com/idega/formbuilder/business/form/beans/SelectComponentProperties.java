package com.idega.formbuilder.business.form.beans;

import java.util.Map;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class SelectComponentProperties extends ComponentProperties {
	
	private boolean required;
	private LocalizedStringBean empty_element_label;
	private String external_data_src;
	private Map<String, String> static_data_src; 
	
	public LocalizedStringBean getEmptyElementLabel() {
		return empty_element_label;
	}
	public void setEmptyElementLabel(LocalizedStringBean empty_element_label) {
		this.empty_element_label = empty_element_label;
	}
	public boolean isRequired() {
		return required;
	}
	/**
	 * <b>WARNING: </b>if required is set to false and value for empty label is not presented 
	 * (which is not recommended), default component value will be used. 
	 */
	public void setRequired(boolean required) {
		
		if(!required && empty_element_label == null)
			
			empty_element_label = new LocalizedStringBean();
			
		this.required = required;
	}
	public String getExternalDataSrc() {
		return external_data_src;
	}
	/**
	 * External data source cannot be used together with static data source.
	 * If both are set, static one is used
	 * 
	 * @param external_data_src - source to xml, containing data source<br />
	 * <b>IMPORTANT:</b> xml data structure should validate against 
	 * select-data-src.xsd (look somewhere around for it, probably in resources/templates).
	 * See also select-data.src.xml for example.
	 * 
	 */
	public void setExternalDataSrc(String external_data_src) {
		this.external_data_src = external_data_src;
	}
	public Map<String, String> getStaticDataSrc() {
		return static_data_src;
	}
	/**
	 * Static data source cannot be used together with external data source.
	 * If both are set, static one is used
	 * 
	 * @param static_data_src - provide key/value pairs for select data source 
	 */
	public void setStaticDataSrc(Map<String, String> static_data_src) {
		this.static_data_src = static_data_src;
	}
}
