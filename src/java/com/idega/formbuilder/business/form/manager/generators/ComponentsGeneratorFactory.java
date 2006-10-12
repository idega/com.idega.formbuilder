package com.idega.formbuilder.business.form.manager.generators;

import java.net.URI;

import javax.faces.context.FacesContext;

import org.chiba.xml.xslt.TransformerService;
import org.chiba.xml.xslt.impl.CachingTransformerService;
import org.chiba.xml.xslt.impl.FileResourceResolver;
import org.chiba.xml.xslt.impl.ResourceResolver;

import com.idega.block.form.business.BundleResourceResolver;
import com.idega.formbuilder.IWBundleStarter;
import com.idega.formbuilder.sandbox.SandboxUtil;
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

			try {
				
				String temporal_components_xml_stylesheet = SandboxUtil.COMPONENTS_XFORMSHTML_STYLESHEET_CONTEXT_PATH;
				String final_components_xml_stylesheet = SandboxUtil.COMPONENTS_XFORMSXML_STYLESHEET_CONTEXT_PATH;
				
				FormComponentsGenerator gen = FormComponentsGenerator.getInstance();
				String file_str = "file";
				gen.setTemporalXmlStylesheetUri(new URI(file_str, temporal_components_xml_stylesheet, null));
				gen.setFinalXmlStylesheetUri(new URI(file_str, final_components_xml_stylesheet, null));
				
				TransformerService transf_service = new CachingTransformerService(new FileResourceResolver());
				
				gen.setTransformerService(transf_service);
				
				ComponentsGeneratorFactory.gen = gen;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		} else {
			
			IWMainApplication iw_app = IWMainApplication.getIWMainApplication(ctx);
			
			String temporal_components_xml_stylesheet = IWBundleStarter.bundle_path_start+(String)iw_app.getAttribute(IWBundleStarter.TEMPORAL_COMPONENTS_XML_STYLESHEET_PATH);
			String final_components_xml_stylesheet = IWBundleStarter.bundle_path_start+(String)iw_app.getAttribute(IWBundleStarter.FINAL_COMPONENTS_XML_STYLESHEET_PATH);
			
			if(temporal_components_xml_stylesheet != null && final_components_xml_stylesheet != null) {
				
				FormComponentsGenerator gen = FormComponentsGenerator.getInstance();
				gen.setTemporalXmlStylesheetUri(URI.create(temporal_components_xml_stylesheet));
				gen.setFinalXmlStylesheetUri(URI.create(final_components_xml_stylesheet));
				
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