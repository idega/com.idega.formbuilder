package com.idega.formbuilder.presentation.components;

import javax.faces.context.FacesContext;

import com.idega.block.formadmin.presentation.SDataViewer;
import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Text;

public class FBAdminPage extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "AdminPage";
	
	protected void initializeComponent(FacesContext context) {
		Layer body = new Layer(Layer.DIV);
		body.setId("fbAdminPage");
		
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
		
		body.add(header);
		
		SDataViewer listContainer = new SDataViewer();
		listContainer.setRendered(true);
		
		body.add(listContainer);
		
		add(body);
	}
	
}
