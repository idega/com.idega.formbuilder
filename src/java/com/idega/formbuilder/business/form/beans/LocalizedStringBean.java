package com.idega.formbuilder.business.form.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 * Just the wrapper around Map object, representing localized string
 */
public class LocalizedStringBean {
	
	private Map<Locale, String> strings;
	
	public LocalizedStringBean() {
		strings = new HashMap<Locale, String>();
	}
	
	public Set<Locale> getLanguagesKeySet() {
		return strings.keySet();
	}
	
	public void setString(Locale locale, String text) {
		
		if(locale == null || text == null)
			throw new NullPointerException("Locale or text is not provided");
		
		strings.put(locale, text);
	}
	
	public String getString(Locale locale) {
		return strings.get(locale);
	}
	
	public void removeString(Locale locale) {
		strings.remove(locale);
	}
	
	public void clear() {
		strings.clear();
	}
}
