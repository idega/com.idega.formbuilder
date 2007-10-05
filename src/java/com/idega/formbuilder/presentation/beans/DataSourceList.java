package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;

import com.idega.documentmanager.business.component.properties.PropertiesSelect;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.idegaweb.BundleLocalizationMap;
import com.idega.presentation.ui.SelectOption;
import com.idega.util.config.Config;
import com.idega.util.config.ConfigException;
import com.idega.util.config.ConfigFactory;
import com.idega.webface.WFUtil;

public class DataSourceList implements Serializable {
	
	private static final long serialVersionUID = -1462694112346788168L;
	private static final String select_sources = "select-external_data_sources";
	private static final Logger logger = Logger.getLogger(DataSourceList.class.getName());
	
	public static String localDataSrc = new Integer(PropertiesSelect.LOCAL_DATA_SRC).toString();
	public static String externalDataSrc = new Integer(PropertiesSelect.EXTERNAL_DATA_SRC).toString();
	
	private List<SelectItem> sources = new ArrayList<SelectItem>();
	private List<SelectOption> ext_data_sources;

	public DataSourceList() {
		sources.clear();
		sources.add(new SelectItem(localDataSrc, "List of values"));
		sources.add(new SelectItem(externalDataSrc, "External"));
	}

	public List<SelectItem> getSources() {
		return sources;
	}

	public void setSources(List<SelectItem> sources) {
		this.sources = sources;
	}
	
	public List<SelectOption> getExternalDataSources() {
	
		if(ext_data_sources == null) {
			
			ext_data_sources = new ArrayList<SelectOption>();
			ext_data_sources.add(new SelectOption("", "Choose"));
			
			try {
				Map loc_strings = (BundleLocalizationMap) ((HashMap) WFUtil.getBeanInstance("localizedStrings")).get(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
				
				Config cfg = ConfigFactory.getInstance().getConfig(IWBundleStarter.IW_BUNDLE_IDENTIFIER, IWBundleStarter.FB_CFG_FILE);
				Map<String, String > props = cfg.getProperies(select_sources);
				
				if(props != null) {
					
					for (Iterator<String> iter = props.keySet().iterator(); iter.hasNext();) {
						String src_key = iter.next();
						ext_data_sources.add(new SelectOption(props.get(src_key), (String)loc_strings.get(src_key+".label")));
					}
				}
				
			} catch (ConfigException e) {
				logger.log(Level.SEVERE, "Unable to load select external data sources list. Config key provided: "+select_sources, e);
			}
		}
		
		return ext_data_sources;
	}
}
