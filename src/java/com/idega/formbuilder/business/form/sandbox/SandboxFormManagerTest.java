package com.idega.formbuilder.business.form.sandbox;

import java.util.Locale;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.ConstButtonType;
import com.idega.formbuilder.business.form.Container;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.FormManagerFactory;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;

public class SandboxFormManagerTest {

	/**
	 * @param args
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
			
			
			ButtonArea page1_button_area = page.getButtonArea();
			System.out.println("button area: "+page1_button_area);
			System.out.println("pprev button: "+page1_button_area.getButton(new ConstButtonType(ConstButtonType.next_page_button)));
			
//			Page page5 = fm.getCurrentDocument().addPage("fbcomp_4");
			ButtonArea ba = page2.createButtonArea(null);
			ba.addButton(new ConstButtonType(ConstButtonType.previous_page_button), null);
			
			
			System.out.println("source code___________");
			System.out.println(fm.getCurrentDocument().getFormSourceCode());
			
			System.out.println("the end");
			
		} catch (Exception e) {
			System.out.println("exception caught at main");
			e.printStackTrace();
		}
	}
}
