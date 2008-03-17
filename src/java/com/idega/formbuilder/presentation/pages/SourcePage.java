package com.idega.formbuilder.presentation.pages;

import java.util.ArrayList;
import java.util.List;

import com.idega.block.web2.business.Web2Business;
import com.idega.formbuilder.presentation.components.FBSourceView;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Script;
import com.idega.util.PresentationUtil;
import com.idega.webface.WFUtil;

public class SourcePage extends Page {
	
	private static final String SOURCE_VIEW_ID = "sourceView";
	
	public void main(IWContext iwc) throws Exception {
		getParentPage().setAllMargins(0);
		Web2Business web2 = (Web2Business) WFUtil.getBeanInstance("web2bean");
		
		List<String> scriptFiles = new ArrayList<String>();
		scriptFiles.add(web2.getCodePressScriptFilePath());
		
//		getParentPage().add(PresentationUtil.getJavaScriptSourceLines(scriptFiles));
		Script script = new Script();
		script.addScriptLine("alert('CodePress.run()');");
		script.addScriptLine("CodePress.run();");
//		getParentPage().add(script);
		FBSourceView sourceVoew = new FBSourceView();
		sourceVoew.setId(SOURCE_VIEW_ID);
		add(sourceVoew);
	}

}
