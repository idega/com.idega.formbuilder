package com.idega.formbuilder.presentation.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.formbuilder.presentation.FBComponentBase;
import com.idega.formbuilder.presentation.beans.FormDocument;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.TextArea;
import com.idega.util.RenderUtils;
import com.idega.webface.WFUtil;

public class FBSourceView extends FBComponentBase {
	
	public static final String COMPONENT_TYPE = "SourceView";
	
	private String id;
	private String styleClass;
	private String value;
	
	public FBSourceView() {
		super();
		this.setRendererType(null);
	}
	
	protected void initializeComponent(FacesContext context) {
		getChildren().clear();
		
		Layer content = new Layer(Layer.DIV);
		content.setStyleClass(styleClass);
		content.setId(id + "Div");
		
		FormDocument formDocument = (FormDocument) WFUtil.getBeanInstance(FormDocument.BEAN_ID);
		
		TextArea textarea = new TextArea();
		textarea.setRendered(true);
		textarea.setValue(formDocument.getSourceCode());
		textarea.setId("sourceTextarea");
		textarea.setStyleClass("codepress html linenumbers-on");
		
		content.add(textarea);
		
		add(content);
	}
	
	@SuppressWarnings("unchecked")
	public void encodeChildren(FacesContext context) throws IOException {
		for(Iterator it = getChildren().iterator(); it.hasNext(); ) {
			RenderUtils.renderChild(context, (UIComponent) it.next());
		}
	}
	
	public Object saveState(FacesContext context) {
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = id;
		values[2] = styleClass;
		values[3] = value;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		id = (String) values[1];
		styleClass = (String) values[2];
		value = (String) values[3];
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
