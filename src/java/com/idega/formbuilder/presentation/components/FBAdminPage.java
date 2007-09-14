package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.component.html.ext.HtmlOutputText;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.formadmin.presentation.SDataViewer;
import com.idega.formbuilder.FormbuilderViewManager;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.webface.WFDivision;

public class FBAdminPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "AdminPage";
	
	private static final String HEADER_BLOCK_FACET = "HEADER_BLOCK_FACET";
	private static final String GREETING_BLOCK_FACET = "GREETING_BLOCK_FACET";
	private static final String FORM_LIST_FACET = "FORM_LIST_FACET";
	
	public FBAdminPage() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		WFDivision header = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		header.setId("fbHomePageHeaderBlock");
		
		WFDivision headerPartLeft = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		headerPartLeft.setId("fbHPLeft");
		
		Text name = new Text();
		name.setText("FormBuilder");
		name.setId("headerName");
		Text slogan = new Text();
		slogan.setText("The easy way to build your forms");
		slogan.setId("headerSlogan");
		
		addChild(name, headerPartLeft);
		addChild(slogan, headerPartLeft);
		
		addChild(headerPartLeft, header);
		
		addFacet(HEADER_BLOCK_FACET, header);
		
		
		WFDivision greeting = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		greeting.setId("fbHomePageWelcomeBlock");
		
		HtmlOutputText greetingText1 = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		greetingText1.setValue("Good afternoon ");
		greetingText1.setId("greetingText1");
		
		HtmlOutputText userName = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		userName.setValue(IWContext.getInstance().getCurrentUser().getName());
		userName.setId("userName");
		
		HtmlOutputText greetingText2 = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		greetingText2.setValue(" and welcome. Here below you can select a form on the left by clicking its name and then look at each entry by clicking it in the list that will appear on the right ");
		greetingText2.setId("greetingText2");
		
		addChild(greetingText1, greeting);
		addChild(userName, greeting);
		addChild(greetingText2, greeting);
		
		addFacet(GREETING_BLOCK_FACET, greeting);

//		SDataViewer listContainer = (SDataViewer) application.createComponent("SDataViewer");
		SDataViewer listContainer = new SDataViewer();
		listContainer.setRendered(true);
		
		addFacet(FORM_LIST_FACET, listContainer);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		
		AddResource resourceAdder = AddResourceFactory.getInstance(context);
		resourceAdder.addStyleSheet(context, AddResource.HEADER_BEGIN, FormbuilderViewManager.FORMBUILDER_CSS);
		
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		if(!isRendered()) {
			return;
		}
		Iterator it = getChildren().iterator();
		while(it.hasNext()) {
			renderChild(context, (UIComponent) it.next());
		}
		UIComponent header = getFacet(HEADER_BLOCK_FACET);
		if(header != null) {
			renderChild(context, header);
		}
		UIComponent greeting = getFacet(GREETING_BLOCK_FACET);
		if(greeting != null) {
			renderChild(context, greeting);
		}
		UIComponent list = getFacet(FORM_LIST_FACET);
		if(list != null) {
			renderChild(context, list);
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}

}
