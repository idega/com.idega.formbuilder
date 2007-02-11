package com.idega.formbuilder.business.form.sandbox;

import java.util.Locale;

import org.chiba.xml.dom.DOMUtil;

import com.idega.formbuilder.business.form.Button;
import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.ConstButtonType;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.FormManagerFactory;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.manager.FormManager;

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
			ButtonArea ba = page.getButtonArea();
			Button next = ba.getButton(new ConstButtonType(ConstButtonType.next_page_button));
			System.out.println("next button: "+next);
			System.out.println("button_area components _________________ : "+ba.getContainedComponentsIdList());
			Component c = ba.getComponent(ba.getContainedComponentsIdList().get(0));
			System.out.println("and got: ___ "+c);
			DOMUtil.prettyPrintDOM(c.getHtmlRepresentation(new Locale("en")));
			
			String xx0 = xx.getContainedPagesIdList().get(0);
			String xx1 = xx.getContainedPagesIdList().get(1);
			xx.getContainedPagesIdList().set(1, xx0);
			xx.getContainedPagesIdList().set(0, xx1);
			xx.rearrangeDocument();
			
			System.out.println("preview: "+xx.getConfirmationPage());
			System.out.println("thx: "+xx.getThxPage());
			Page np = xx.addPage(null);
			System.out.println("new page added: "+np);
			ba = np.createButtonArea(null);
			
			np.addComponent(fm.getAvailableFormComponentsTypesList().get(0), null);
			
			System.out.println("opening ____________________");
//			((FormManager)fm).openForm(xx.getXformsDocument());
			
			System.out.println("source code___________");
			System.out.println(fm.getCurrentDocument().getFormSourceCode());
			
			System.out.println("the end");
			
		} catch (Exception e) {
			System.out.println("exception caught at main");
			e.printStackTrace();
		}
	}
}
