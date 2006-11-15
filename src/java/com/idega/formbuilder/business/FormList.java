package com.idega.formbuilder.business;

import java.util.List;
import javax.faces.model.SelectItem;
import com.idega.block.formadmin.business.beans.AvailableFormsLister;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas ‰ivilis</a>
 * @version 1.0
 * 
 */
public class FormList {
	
	public List<SelectItem> getForms() {
		
		return AvailableFormsLister.getInstance().getAvailableFormsAsSelectItems();
	}
}