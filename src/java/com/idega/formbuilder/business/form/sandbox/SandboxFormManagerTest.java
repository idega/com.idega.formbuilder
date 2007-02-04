package com.idega.formbuilder.business.form.sandbox;

import java.util.Locale;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.ComponentSelect;
import com.idega.formbuilder.business.form.ConstButtonType;
import com.idega.formbuilder.business.form.Container;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.FormManagerFactory;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.PropertiesSelect;

public class SandboxFormManagerTest {

	/**
	 * @param args 
	 * when launching:
	 * VM argument
	 * -Didegaweb.bundles.resource.dir=<your workspace dir>
	 * should be provided
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			DocumentManager fm = FormManagerFactory.newFormManager(null);
			Document xx = fm.createForm("xx", null);
			xx = fm.getCurrentDocument();
			
			System.out.println("pages id list: "+xx.getContainedPagesIdList());
			Page page = xx.getPage(xx.getContainedPagesIdList().get(0));
			PropertiesPage page_properties = page.getProperties();
			System.out.println("page label: "+page_properties.getLabel());
			
			Component comp = page.getComponent(page.getContainedComponentsIdList().get(0));
			
			if(comp instanceof Container)
				comp = ((Container)comp).getComponent(((Container)comp).getContainedComponentsIdList().get(0));
			
			System.out.println("component: "+comp);
			
			Element localized_component = comp.getHtmlRepresentation(new Locale("en"));
			
			System.out.println("localized: "+localized_component);
			DOMUtil.prettyPrintDOM(localized_component);
			
			System.out.println("available types list: "+fm.getAvailableFormComponentsTypesList());
			
//			Component text_component = page.addComponent(
//					fm.getAvailableFormComponentsTypesList().get(0), null);
			
//			System.out.println("added: "+text_component);
			Page page2 = fm.getCurrentDocument().addPage(null);
//			Page page3 = fm.getCurrentDocument().addPage(page2.getId());
//			Page page4 = fm.getCurrentDocument().addPage(page.getId());
			Component select = page2.addComponent("fbcomp_single_select_minimal", null);
			
			System.out.println("select______: "+select.getClass());
			
			if(select instanceof ComponentSelect) {
				System.out.println("select compo---------------");
				ComponentSelect se = (ComponentSelect)select;
				PropertiesSelect prop_sel = se.getProperties();
				System.out.println("select label: "+prop_sel.getLabel());
				se.getProperties().getLabel().setString(new Locale("en"), "I'm the select component");
				se.getProperties().setLabel(se.getProperties().getLabel());
				System.out.println("select label2: "+prop_sel.getLabel());
			}
			
			ButtonArea page1_button_area = page.getButtonArea();
			System.out.println("button area: "+page1_button_area);
			System.out.println("pprev button: "+page1_button_area.getButton(new ConstButtonType(ConstButtonType.next_page_button)));
			
//			Page page5 = fm.getCurrentDocument().addPage("fbcomp_4");
			ButtonArea ba = page2.createButtonArea(null);
			ba.addButton(new ConstButtonType(ConstButtonType.previous_page_button), null);
			System.out.println("page2 id list: "+page2.getContainedComponentsIdList());
			
			String x0 = page2.getContainedComponentsIdList().get(0);
			String x1 = page2.getContainedComponentsIdList().get(1);
			page2.getContainedComponentsIdList().set(1, x0);
			page2.getContainedComponentsIdList().set(0, x1);
			
			System.out.println("page2 id list: "+page2.getContainedComponentsIdList());
			
			System.out.println("source code___________");
			System.out.println(fm.getCurrentDocument().getFormSourceCode());
			
			System.out.println("the end");
			
		} catch (Exception e) {
			System.out.println("exception caught at main");
			e.printStackTrace();
		}
	}
}
