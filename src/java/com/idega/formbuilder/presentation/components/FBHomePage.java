package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;

import org.apache.myfaces.component.html.ext.HtmlCommandLink;
import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlOutputText;

import com.idega.block.web2.business.Web2Business;
import com.idega.business.IBOLookup;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormList;
import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObjectUtil;
import com.idega.presentation.Script;
import com.idega.presentation.text.Text;
import com.idega.webface.WFDivision;
import com.idega.webface.WFUtil;

public class FBHomePage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "HomePage";
	
	private static final String HEADER_BLOCK_FACET = "HEADER_BLOCK_FACET";
	private static final String GREETING_BLOCK_FACET = "GREETING_BLOCK_FACET";
	private static final String FORM_LIST_FACET = "FORM_LIST_FACET";
	
	private static final String HOMEPAGE_JS = "/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/homepage.js";
	
	public FBHomePage() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		Application application = context.getApplication();
		getChildren().clear();
		
		Page parentPage = PresentationObjectUtil.getParentPage(this);
		if (parentPage != null) {
			try {
				Web2Business business = (Web2Business) IBOLookup.getServiceInstance(IWContext.getInstance(), Web2Business.class);
				String prototypeURI = business.getBundleURIToPrototypeLib();
				String scriptaculousURI = business.getBundleURIToScriptaculousLib();
				String ricoURI = business.getBundleURIToRico();
	
				Script s = new Script();
				s.addScriptSource(prototypeURI);
				s.addScriptSource(scriptaculousURI);
				s.addScriptSource(ricoURI);
				s.addScriptSource(HOMEPAGE_JS);
				
				// THIS HAS TO BE ADDED TO THE <BODY> in the html, if not it does not work in Safari
				parentPage.setOnLoad("javascript:bodyOnLoad()");
				
				add(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		FormList formList = (FormList) WFUtil.getBeanInstance("formSelector");
		List<SelectItem> formsList = formList.getForms();
		
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
		
		WFDivision headerPartRight = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		headerPartRight.setId("fbHPRight");
		
		FBNewFormComponent newFormComponent = (FBNewFormComponent) application.createComponent(FBNewFormComponent.COMPONENT_TYPE);
		newFormComponent.setStyleClass("newFormComponentIdle");
		
		addChild(newFormComponent, headerPartRight);
		addChild(headerPartLeft, header);
		addChild(headerPartRight, header);
		
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
		greetingText2.setValue(" and welcome. I see you have created ");
		greetingText2.setId("greetingText2");
		
		HtmlOutputText formCount = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		formCount.setValue(formsList.size());
		formCount.setId("formCount");
		
		HtmlOutputText greetingText3 = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		greetingText3.setValue(" forms. You can manage them in the list below or create a new one ");
		greetingText3.setId("greetingText3");
		
		HtmlCommandLink greetingTextL = (HtmlCommandLink) application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		greetingTextL.setValue("here");
		greetingTextL.setOnclick("showInputField();return false");
		greetingTextL.setId("greetingTextL");
		
		addChild(greetingText1, greeting);
		addChild(userName, greeting);
		addChild(greetingText2, greeting);
		addChild(formCount, greeting);
		addChild(greetingText3, greeting);
		addChild(greetingTextL, greeting);
		
		addFacet(GREETING_BLOCK_FACET, greeting);
		
		WFDivision listContainer = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		listContainer.setId("formListContainer");
		
		WFDivision arrowUp = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		arrowUp.setId("arrow_up");
		
		WFDivision forms = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		forms.setId("forms");
		
		WFDivision arrowDown = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		arrowDown.setId("arrow_down");
		
		HtmlGraphicImage arrowUpImg = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		arrowUpImg.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow-up.gif");
		arrowUpImg.setId("arrow_up_image");
		arrowUpImg.setStyleClass("arrowUpImg");
		
		HtmlGraphicImage arrowDownImg = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		arrowDownImg.setValue("/idegaweb/bundles/com.idega.formbuilder.bundle/resources/images/arrow-down.gif");
		arrowDownImg.setId("arrow_down_image");
		arrowDownImg.setStyleClass("arrowDownImg");
		
		WFDivision noname = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		noname.setStyleClass("noname");
		
		addChild(noname, forms);
		addChild(arrowUpImg, arrowUp);
		addChild(arrowDownImg, arrowDown);
		addChild(arrowUp, listContainer);
		addChild(forms, listContainer);
		addChild(arrowDown, listContainer);
		
		WFDivision slideEnd = (WFDivision) application.createComponent(WFDivision.COMPONENT_TYPE);
		slideEnd.setId("slideEnd");
		
		
		Iterator it = formsList.iterator();
		while(it.hasNext()) {
			SelectItem item = (SelectItem) it.next();
			FBFormListItem formListItem = (FBFormListItem) application.createComponent(FBFormListItem.COMPONENT_TYPE);
			formListItem.setId(item.getValue().toString());
			formListItem.setStyleClass("formListItem");
			formListItem.setFormTitle(item.getLabel());
			String createdDate = getCreatedDate(item.getValue().toString());
			formListItem.setFormId("Created " + createdDate);
			addChild(formListItem, noname);
		}
		addChild(slideEnd, noname);
		
		addFacet(FORM_LIST_FACET, listContainer);
	}
	
	public String getEmbededJavascript() {
		StringBuilder result = new StringBuilder();
		result.append("<script language=\"JavaScript\">\n");
		result.append("initGalleryScript();\n");
		result.append("</script>\n");
		return result.toString();
		
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
		writer.write(getEmbededJavascript());
	}
}
