package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.web2.business.Web2Business;
import com.idega.business.SpringBeanLookup;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.webface.WFUtil;

public class FBHomePage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "HomePage";
	
	private static final String HEADER_BLOCK_FACET = "HEADER_BLOCK_FACET";
	private static final String GREETING_BLOCK_FACET = "GREETING_BLOCK_FACET";
	private static final String FORM_LIST_FACET = "FORM_LIST_FACET";
	
	private static final String HOMEPAGE_JS = "//idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/homepage.js";
	private static final String DWR_ENGINE_JS = "//dwr/engine.js";
	private static final String DWR_FORM_DOCUMENT_JS = "//dwr/interface/FormDocument.js";
	private static final String FORMS_HOME_CSS = "//idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formshome.css";
	private static final String ARROW_UP_IMG = "//idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow-up.gif";
	private static final String ARROW_DOWN_IMG = "//idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow-down.gif";
	
	public FBHomePage() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		IWContext iwc = IWContext.getIWContext(context);
		
		Layer header = new Layer(Layer.DIV);
		header.setId("fbHomePageHeaderBlock");
		
		Layer headerPartLeft = new Layer(Layer.DIV);
		headerPartLeft.setId("fbHPLeft");
		
		Text name = new Text(getLocalizedString(iwc, "fb_home_top_title", "Formbuilder"));
		name.setId("headerName");
		Text slogan = new Text(getLocalizedString(iwc, "fb_home_top_slogan", "The easy way to build your forms"));
		slogan.setId("headerSlogan");
		
		headerPartLeft.add(name);
		headerPartLeft.add(slogan);
		
		Layer headerPartRight = new Layer(Layer.DIV);
		headerPartRight.setId("fbHPRight");
		
		FBNewFormComponent newFormComponent = (FBNewFormComponent) application.createComponent(FBNewFormComponent.COMPONENT_TYPE);
		newFormComponent.setStyleClass("newFormComponentIdle");
		
		headerPartRight.add(newFormComponent);
		header.add(headerPartLeft);
		header.add(headerPartRight);
		
		addFacet(HEADER_BLOCK_FACET, header);
	}
	
	private String getCreatedDate(String formId) {
		String interm1 = formId.substring(formId.indexOf("-") + 5);
		String month = interm1.substring(0, 3);
		String interm2 = interm1.substring(interm1.indexOf("_") + 1);
		String day = interm2.substring(0, 2);
		String year = interm2.substring(interm2.length() - 4);
		return month + " " + day + ", " + year;
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		IWContext iwc = IWContext.getIWContext(context);
		Web2Business business = (Web2Business) SpringBeanLookup.getInstance().getSpringBean(iwc, Web2Business.class);
		
		if(business == null) {
			logger.log(Level.SEVERE, "Exception while looking up Web2Business through SrpingBeanLookup");
			return;
		}
		
		String transcornersURI = business.getTranscornersScriptFilePath();
		String mootoolsURI = business.getBundleURIToMootoolsLib(true);
			
		AddResource resourceAdder = AddResourceFactory.getInstance(context);
//		resourceAdder.addStyleSheet(context, AddResource.HEADER_BEGIN, FORMS_HOME_CSS);
//		resourceAdder.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, mootoolsURI);
//		resourceAdder.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, transcornersURI);
//		resourceAdder.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DWR_ENGINE_JS);
//		resourceAdder.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DWR_FORM_DOCUMENT_JS);
//		resourceAdder.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, HOMEPAGE_JS);
		
		Application application = context.getApplication();
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
		
		PersistenceManager persistence_manager = (PersistenceManager) WFUtil.getBeanInstance("xformsPersistenceManager");
		List<SelectItem> formsList = persistence_manager.getForms();
		
		Layer greeting = new Layer(Layer.DIV);
		greeting.setId("fbHomePageWelcomeBlock");
		
		Text greetingText1 = new Text(getLocalizedString(iwc, "fb_home_greeting1", "Good afternoon"));
		greetingText1.setId("greetingText1");
		
		Text userName = new Text();
		User currentUser = iwc.getCurrentUser();
		if(currentUser != null) {
			userName.setText(currentUser.getName() + " ");
			userName.setId("userName");
		}
		
		Text greetingText2 = new Text(getLocalizedString(iwc, "fb_home_greeting2", " and welcome. I see you have created "));
		greetingText2.setId("greetingText2");
		
		Text formCount = new Text();
		formCount.setText(new Integer(formsList.size()).toString());
		formCount.setId("formCount");
		
		String form = "form";
		if(formsList.size() != 1) {
			form += "s";
		}
		
		Text greetingText3 = new Text(" " + form + getLocalizedString(iwc, "fb_home_greeting3", ". You can manage them in the list below or create a new one "));
		greetingText3.setId("greetingText3");
		
		Link greetingTextL = new Link();
		greetingTextL.setText(getLocalizedString(iwc, "fb_home_greeting_link", "here"));
		greetingTextL.setId("greetingTextL");
		
		greeting.add(greetingText1);
		greeting.add(userName);
		greeting.add(greetingText2);
		greeting.add(formCount);
		greeting.add(greetingText3);
		greeting.add(greetingTextL);
		
		addFacet(GREETING_BLOCK_FACET, greeting);
		
		Layer listContainer = new Layer(Layer.DIV);
		listContainer.setId("formListContainer");
		
		Layer arrowUp = new Layer(Layer.DIV);
		arrowUp.setId("arrow_up");
		
		Layer forms = new Layer(Layer.DIV);
		forms.setId("forms");
		
		Layer arrowDown = new Layer(Layer.DIV);
		arrowDown.setId("arrow_down");
		
		Image arrowUpImg = new Image();
		arrowUpImg.setSrc(ARROW_UP_IMG);
		arrowUpImg.setId("arrow_up_image");
		arrowUpImg.setStyleClass("arrowUpImg");
		
		Image arrowDownImg = new Image();
		arrowDownImg.setSrc(ARROW_DOWN_IMG);
		arrowDownImg.setId("arrow_down_image");
		arrowDownImg.setStyleClass("arrowDownImg");
		
		Layer noname = new Layer(Layer.DIV);
		noname.setStyleClass("noname");
		noname.getChildren().clear();
		
		forms.add(noname);
		arrowUp.add(arrowUpImg);
		arrowDown.add(arrowDownImg);
		listContainer.add(arrowUp);
		listContainer.add(forms);
		listContainer.add(arrowDown);
		
		Layer slideEnd = new Layer(Layer.DIV);
		slideEnd.setId("slideEnd");
		
		Iterator it = formsList.iterator();
		while(it.hasNext()) {
			SelectItem item = (SelectItem) it.next();
			FBFormListItem formListItem = (FBFormListItem) application.createComponent(FBFormListItem.COMPONENT_TYPE);
			formListItem.setId(item.getValue().toString());
			formListItem.setStyleClass("formListItem");
			formListItem.setFormTitle(item.getLabel());
			String createdDate = getCreatedDate(item.getValue().toString());
			formListItem.setCreatedDate("Created " + createdDate);
			addChild(formListItem, noname);
		}
		noname.add(slideEnd);
		
		addFacet(FORM_LIST_FACET, listContainer);
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
