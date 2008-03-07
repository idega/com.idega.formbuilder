package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idega.documentmanager.component.datatypes.ComponentType;
import com.idega.documentmanager.component.datatypes.ConstComponentDatatype;

public class ProcessPalette extends Palette implements Serializable {
	
	private static final long serialVersionUID = -753995857789493992L;
	
	private static Log logger = LogFactory.getLog(ProcessPalette.class);
	
	public static final String BEAN_ID = "processPalette";
	
	private Map<String, List<PaletteComponent>> components = new HashMap<String, List<PaletteComponent>>();
	private Map<String, String> componentInternalTypeMappings = new HashMap<String, String>();
	
	public Map<String, String> getComponentInternalTypeMappings() {
		if(componentInternalTypeMappings.isEmpty()) {
			componentInternalTypeMappings.put("xf:input", "fbc_text");
			componentInternalTypeMappings.put("xf:textarea", "fbc_textarea");
		}
		return componentInternalTypeMappings;
	}

	public void setComponentInternalTypeMappings(
			Map<String, String> componentInternalTypeMappings) {
		this.componentInternalTypeMappings = componentInternalTypeMappings;
	}

	private List<PaletteComponent> populatePaletteComponentList(List<ComponentType> components) {
		List<PaletteComponent> list = new ArrayList<PaletteComponent>();
		Iterator<ComponentType> it = components.iterator();
		while(it.hasNext()) {
			ComponentType nextComp = it.next();
			try {
				list.add(new PaletteComponent(nextComp.getType(), "fbv"));
			} catch(Exception e) {
				e.printStackTrace();
				logger.error("Could not retrieve component: " + nextComp);
			}
		}
		return list;
	}
	
	public Set<String> getDatatypes() {
		if(components == null || components.isEmpty()) {
			populateComponentMap();
		}
		return components.keySet();
	}
	
	public List<PaletteComponent> getComponents(String datatype) {
		if(components == null || components.isEmpty()) {
			populateComponentMap();
		}
		return components.get(datatype);
	}
	
	public String getComponentDatatype(String componentType) {
		Set<String> datatypesSet = getDatatypes();
		for(Iterator<String> it = datatypesSet.iterator(); it.hasNext(); ) {
			String datatype = it.next();
			List<PaletteComponent> comps = getComponents(datatype);
			for(Iterator<PaletteComponent> it2 = comps.iterator(); it2.hasNext(); ) {
				PaletteComponent comp = it2.next();
				if(comp.getType().equals(componentType)) {
					return datatype;
				}
			}
		}
		return null;
	}
	
	private void populateComponentMap() {
		List<ComponentType> compTypes = getInstanceManager().getDocumentManagerInstance().getComponentsByDatatype(new ConstComponentDatatype(ConstComponentDatatype.STRING));
		List<PaletteComponent> paletteComps = populatePaletteComponentList(compTypes);
		components.put(ConstComponentDatatype.STRING, paletteComps);
		compTypes = getInstanceManager().getDocumentManagerInstance().getComponentsByDatatype(new ConstComponentDatatype(ConstComponentDatatype.FILE));
		paletteComps = populatePaletteComponentList(compTypes);
		components.put(ConstComponentDatatype.FILE, paletteComps);
		compTypes = getInstanceManager().getDocumentManagerInstance().getComponentsByDatatype(new ConstComponentDatatype(ConstComponentDatatype.LIST));
		paletteComps = populatePaletteComponentList(compTypes);
		components.put(ConstComponentDatatype.LIST, paletteComps);
	}

	public List<PaletteComponent> getString() {
		if(components == null || components.isEmpty()) {
			populateComponentMap();
		}
		return components.get(ConstComponentDatatype.STRING);
	}

	public void setString(List<PaletteComponent> string) {
		components.put(ConstComponentDatatype.STRING, string);
	}

	public List<PaletteComponent> getFile() {
		if(components == null || components.isEmpty()) {
			populateComponentMap();
		}
		return components.get(ConstComponentDatatype.FILE);
	}

	public void setFile(List<PaletteComponent> file) {
		components.put(ConstComponentDatatype.FILE, file);
	}

	public List<PaletteComponent> getList() {
		if(components == null || components.isEmpty()) {
			populateComponentMap();
		}
		return components.get(ConstComponentDatatype.LIST);
	}

	public void setList(List<PaletteComponent> list) {
		components.put(ConstComponentDatatype.LIST, list);
	}

	public Map<String, List<PaletteComponent>> getComponents() {
		return components;
	}

	public void setComponents(Map<String, List<PaletteComponent>> components) {
		this.components = components;
	}

}
