package com.idega.formbuilder.business;

import java.io.Serializable;
import java.util.List;

import com.idega.formbuilder.business.form.beans.ItemBean;

public class SelectOptions implements Serializable {
	
	private static final long serialVersionUID = -7539955909568793992L;

	private List<ItemBean> items;

	public List<ItemBean> getItems() {
		items.clear();
		items.add(new ItemBean("tiger", "Tiger"));
		items.add(new ItemBean("dolphin", "Dolphin"));
		items.add(new ItemBean("mustang", "Mustang"));
		items.add(new ItemBean("panther", "Panther"));
		items.add(new ItemBean("leopard", "Leopard"));
		return items;
	}

	public void setItems(List<ItemBean> items) {
		items.clear();
		items.add(new ItemBean("tiger", "Tiger"));
		items.add(new ItemBean("dolphin", "Dolphin"));
		items.add(new ItemBean("mustang", "Mustang"));
		items.add(new ItemBean("panther", "Panther"));
		items.add(new ItemBean("leopard", "Leopard"));
		//this.items = items;
	}
}
