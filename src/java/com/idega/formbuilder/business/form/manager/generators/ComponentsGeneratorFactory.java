package com.idega.formbuilder.business.form.manager.generators;

import java.net.URI;

import javax.faces.context.FacesContext;

import org.chiba.xml.xslt.TransformerService;
import org.chiba.xml.xslt.impl.CachingTransformerService;
import org.chiba.xml.xslt.impl.ResourceResolver;

import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.business.form.manager.util.BundleResourceResolver;
import com.idega.idegaweb.IWMainApplication;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 *
 */
public class ComponentsGeneratorFactory {
	
	protected ComponentsGeneratorFactory() { }
	
	public static void init(FacesContext ctx) {
		
		if(ctx == null) {
			
		} else {
			
			IWMainApplication iw_app = IWMainApplication.getIWMainApplication(ctx);
			
			String components_stylesheet = (String)iw_app.getAttribute(IWBundleStarter.COMPONENTS_STYLESHEET_PATH);
			String components_xforms_stylesheet = (String)iw_app.getAttribute(IWBundleStarter.COMPONENTS_XFORMS_STYLESHEET_PATH);
			
			if(components_stylesheet != null && components_xforms_stylesheet != null) {
				
				FormComponentsGenerator gen = FormComponentsGenerator.getInstance();
				gen.setComponentsStylesheetUri(URI.create(components_stylesheet));
				gen.setComponentsXformsStylesheetUri(URI.create(components_xforms_stylesheet));
				
				ResourceResolver resolver = new BundleResourceResolver(iw_app);
				TransformerService transf_service = new CachingTransformerService(resolver);
				
				gen.setTransformerService(transf_service);
				ComponentsGeneratorFactory.gen = gen;
			}
		}
	}
	
	private static IComponentsGenerator gen;

	public static IComponentsGenerator createComponentsGenerator() {
		
		return gen;
	}
}