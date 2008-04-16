package com.idega.formbuilder.presentation.beans;

import java.util.List;

import org.jdom.Document;

import com.idega.builder.business.BuilderLogic;
import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.ConstButtonType;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.component.beans.ItemBean;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.formbuilder.presentation.components.FBButton;
import com.idega.formbuilder.util.FBUtil;
import com.idega.util.CoreUtil;

public class FormButton extends GenericComponent {
	
	public static final String BEAN_ID = "formButton";
	
	private Button button;
	
	public Component getComponent() {
		return button;
	}
	
	public void setLabel(String value) {
		LocalizedStringBean bean = getComponent().getProperties().getLabel();
		bean.setString(FBUtil.getUILocale(), value);
		getComponent().getProperties().setLabel(bean);
	}
	
	public List<ItemBean> getItems() {return null;}
	
	public void setItems(List<ItemBean> list) {}
	
	public void setExternalSrc(String externalSrc) {}
	
	public void setErrorMessage(String value) {}
	
	public String getErrorMessage() {return null;}
	
	public void setHelpMessage(String value) {}
	
	public String getHelpMessage() {return null;}
	
	public void setPlainText(String value) {}
	
	public String getPlainText() {return null;}
	
	public void setAddButtonLabel(String value) {}
	
	public void setRemoveButtonLabel(String value) {}
	
	public void setRequired(boolean value) {}
	
	public boolean getRequired() {return false;}
	
	public String getExternalSrc() {return null;}
	
	public String getRemoveButtonLabel() {return null;}
	
	public String getAddButtonLabel() {return null;}
	
	public String getDataSrc() {return null;}

	public void setDataSrc(String dataSrc) {}
	
	public String getAutofillKey() {return null;}

	public void setAutofillKey(String autofillKey) {}
	
	private FormPage formPage;
	
	public FormPage getFormPage() {
		return formPage;
	}

	public void setFormPage(FormPage formPage) {
		this.formPage = formPage;
	}
	
	public FormButton() {}
	
	public FormButton(Button button) {
		this.button = button;
	}
	
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
	}
	
	public String getLabel() {
		if(button == null)
			return null;
		LocalizedStringBean bean = button.getProperties().getLabel();
		if(bean != null) {
			return bean.getString(FBUtil.getUILocale());
		} else {
			return null;
		}
	}
	
	public Document addButton(String type) {
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			Button button = null;
			if(area != null) {
				button = area.addButton(ConstButtonType.getByStringType(type), null);
			} else {
				area = page.createButtonArea(null);
				button = area.addButton(ConstButtonType.getByStringType(type), null);
			}
			return BuilderLogic.getInstance().getRenderedComponent(CoreUtil.getIWContext(), new FBButton(button.getId(), "formButton", "loadButtonInfo(this);", "removeButton(this);"), true);
		}
		return null;
	}
	
	public String removeButton(String id) {
		if(id == null) {
			return null;
		}
		Page page = formPage.getPage();
		if(page != null) {
			ButtonArea area = page.getButtonArea();
			if(area != null) {
				Button button = (Button) area.getComponent(id);
				if(button != null) {
					button.remove();
					return id;
				}
			}
		}
		return null;
	}

}
