package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.block.form.presentation.FormViewer;
import com.idega.documentmanager.business.Document;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.CoreUtil;
import com.idega.webface.WFUtil;

public class FBPreviewPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "PreviewPage";

	private static final String FB_ADMIN_PAGE_ID = "fbPreviewPage";
	private static final String FB_HP_HEADER_BLOCK_OD = "fbHomePageHeaderBlock";
	private static final String FB_HP_LEFT = "fbHPLeft";
	private static final String HEADER_NAME_ID = "headerName";
	private static final String HEADER_SLOGAN_ID = "headerSlogan";

	protected void initializeComponent(FacesContext context) {
		IWContext iwc = CoreUtil.getIWContext();

		Layer body = new Layer(Layer.DIV);
		body.setId(FB_ADMIN_PAGE_ID);

		Layer header = new Layer(Layer.DIV);
		header.setId(FB_HP_HEADER_BLOCK_OD);

		Layer headerPartLeft = new Layer(Layer.DIV);
		headerPartLeft.setId(FB_HP_LEFT);

		Text name = new Text();
		name.setText(getLocalizedString(iwc, "fb_admin_logo", "Formbuilder"));
		name.setId(HEADER_NAME_ID);
		Text slogan = new Text();
		slogan.setText(getLocalizedString(iwc, "fb_admin_slogan", "The easy way to build your forms"));
		slogan.setId(HEADER_SLOGAN_ID);

		headerPartLeft.add(name);
		headerPartLeft.add(slogan);
		header.add(headerPartLeft);

		body.add(header);
		
		FormDocument formDocument = (FormDocument)WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		Document xformsDocument = formDocument.getDocument();
		
		FormViewer formViewer = new FormViewer();
		
		if(xformsDocument != null) {
			
			formViewer.setFormId(formDocument.getFormId());
			formViewer.setXFormsDocument((org.w3c.dom.Document)xformsDocument.getXformsDocument().cloneNode(true));
		}
		
		body.add(formViewer);
			
		add(body);
	}

}
