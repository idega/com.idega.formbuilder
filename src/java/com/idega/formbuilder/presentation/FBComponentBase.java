package com.idega.formbuilder.presentation;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.IWBundleStarter;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBComponentBase extends IWBaseComponent {
	
	public static final String COMPONENT_FAMILY = "formbuilder";
	
	protected Logger logger = Logger.getLogger(FBComponentBase.class.getName());
	
	private String id;
	private String styleClass;
	
	public FBComponentBase() {
		this(null, null);
	}
	
	public FBComponentBase(String id, String styleClass) {
		super();
		setRendererType(null);
		this.id = id;
		this.styleClass = styleClass;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public void renderChild(FacesContext context, UIComponent component) throws IOException {
		if(component.isRendered()) {
			component.encodeBegin(context);
			component.encodeChildren(context);
			component.encodeEnd(context);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addFacet(String name, UIComponent component) {
		getFacets().put(name, component);
	}
	
	@SuppressWarnings("unchecked")
	public void addChild(UIComponent child, UIComponent parent) {
		parent.getChildren().add(child);
	}
	
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	public boolean getRendersChildren() {
		return true;
	}
	
	public String getRendererType() {
		return null;
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[3];
		values[0] = super.saveState(context); 
		values[1] = id;
		values[2] = styleClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
	}
	
	protected static String _(String localizationKey) {
		return WFUtil.getLocalizedStringExpr(IWBundleStarter.IW_BUNDLE_IDENTIFIER, localizationKey);
	}
	
	protected static String _(IWContext iwc, String localizationKey, String defaultString) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle bundle = iwma.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
		return bundle.getResourceBundle(iwc.getCurrentLocale()).getLocalizedString(localizationKey, defaultString);
	}
	
	public static String getLocalizedString(IWContext iwc, String localizationKey, String defaultString) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle bundle = iwma.getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER);
		return bundle.getResourceBundle(iwc.getCurrentLocale()).getLocalizedString(localizationKey, defaultString);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}

}
