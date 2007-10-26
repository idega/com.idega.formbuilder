package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.idega.block.formadmin.presentation.SDataViewer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;
import com.idega.util.RenderUtils;

public class FBAdminPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "AdminPage";
	
	public FBAdminPage() {
		super();
		setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		
		Layer header = new Layer(Layer.DIV);
		header.setId("fbHomePageHeaderBlock");
		
		Layer headerPartLeft = new Layer(Layer.DIV);
		headerPartLeft.setId("fbHPLeft");
		
		Text name = new Text();
		name.setText("FormBuilder");
		name.setId("headerName");
		Text slogan = new Text();
		slogan.setText("The easy way to build your forms");
		slogan.setId("headerSlogan");
		
		headerPartLeft.add(name);
		headerPartLeft.add(slogan);
		header.add(headerPartLeft);
		
		add(header);
		
		SDataViewer listContainer = new SDataViewer();
		listContainer.setRendered(true);
		
		add(listContainer);
	}
	
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeBegin(context);
		writer.startElement("DIV", this);
		writer.writeAttribute("id", getId(), "id");
		writer.writeAttribute("class", getStyleClass(), "styleClass");
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
	
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("DIV");
		super.encodeEnd(context);
	}

}
