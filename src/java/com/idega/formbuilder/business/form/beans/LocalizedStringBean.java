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
	
	private Map<String, String> strings;
	
	public LocalizedStringBean() {
		strings = new HashMap<String, String>();
	}
	
	public Set<String> getLanguagesKeySet() {
		return strings.keySet();
	}
	
	public void setString(String lang_code, String text) {
		
		if(lang_code == null || text == null)
			throw new NullPointerException("Lang code or text is not provided");
		
		strings.put(lang_code, text);
	}
	
	public void setString(Locale lang, String text) {
		
		if(lang == null || text == null)
			throw new NullPointerException("Lang code or text is not provided");
		
		strings.put(lang.getLanguage(), text);
	}
	
	public String getString(String lang_code) {
		return strings.get(lang_code);
	}
	
	public String getString(Locale lang) {
		return strings.get(lang.getLanguage());
	}
	
	public void removeString(String lang_code) {
		strings.remove(lang_code);
	}
	
	public void removeString(Locale lang) {
		strings.remove(lang.getLanguage());
	}
	
	public void clear() {
		strings.clear();
	}
}
