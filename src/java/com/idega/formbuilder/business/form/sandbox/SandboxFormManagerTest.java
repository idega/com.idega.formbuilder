package com.idega.formbuilder.business.form.sandbox;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;

import org.chiba.xml.dom.DOMUtil;

import com.idega.formbuilder.business.form.Button;
import com.idega.formbuilder.business.form.ButtonArea;
import com.idega.formbuilder.business.form.Component;
import com.idega.formbuilder.business.form.ConstButtonType;
import com.idega.formbuilder.business.form.ConstComponentCategory;
import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.DocumentManager;
import com.idega.formbuilder.business.form.DocumentManagerFactory;
import com.idega.formbuilder.business.form.Page;
import com.idega.formbuilder.business.form.PropertiesPage;
import com.idega.formbuilder.business.form.beans.LocalizedStringBean;
import com.idega.formbuilder.business.form.manager.FormManager;
import com.idega.formbuilder.business.form.manager.util.FormManagerUtil;

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
			
			DocumentManager fm = DocumentManagerFactory.newFormManager(null);
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
			
			np.addComponent(fm.getAvailableFormComponentsTypesList(null).get(0), null);
//			Page confirmation_page_added = xx.addConfirmationPage(null);
			
			System.out.println("thx txt: "+xx.getThxPage().getProperties().getText());
			LocalizedStringBean zz = xx.getThxPage().getProperties().getText();
			zz.setString(new Locale("en"), "and here it goes");
			xx.getThxPage().getProperties().setText(zz);
			
//			System.out.println("added conf page: "+confirmation_page_added);
			
			c = np.addComponent(fm.getAvailableFormComponentsTypesList(null).get(0), null);
			String xxx = np.getId();
			String xxx1 = c.getId();
			c.getProperties().setAutofillKey("raktukas");
			
			System.out.println("av: "+fm.getAvailableFormComponentsTypesList(new ConstComponentCategory(ConstComponentCategory.BASIC)));
			
			xx0 = np.getContainedComponentsIdList().get(0);
			xx1 = np.getContainedComponentsIdList().get(2);
			np.getContainedComponentsIdList().set(2, xx0);
			np.getContainedComponentsIdList().set(0, xx1);
			np.rearrangeComponents();
			
			System.out.println("opening ____________________");
			Document opened = ((FormManager)fm).openForm(xx.getXformsDocument());
			c = opened.getPage(xxx).getComponent(xxx1);
			System.out.println("autofill key: "+c.getProperties().getAutofillKey());
			
			InputStream stream = new FileInputStream(new File("/Users/civilis/tmp/badform.xml"));
	        org.w3c.dom.Document doc = FormManagerUtil.getDocumentBuilder().parse(stream);
	        stream.close();
	        opened = ((FormManager)fm).openForm(doc);
			
			System.out.println("source code___________");
			System.out.println(fm.getCurrentDocument().getFormSourceCode());
			
			System.out.println("the end");
			
		} catch (Exception e) {
			System.out.println("exception caught at main");
			e.printStackTrace();
		}
	}
}
