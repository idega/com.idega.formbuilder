package com.idega.formbuilder.presentation.pages;

import com.idega.formbuilder.presentation.components.FBFormPreview;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;

public class PreviewPage extends Page {
	
	public void main(IWContext iwc) throws Exception {
		getParentPage().setAllMargins(0);
		FBFormPreview previewView = new FBFormPreview();
		previewView.setId("previewView2");
		add(previewView);
	}
	
}
