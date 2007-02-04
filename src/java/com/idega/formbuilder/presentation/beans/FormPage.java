package com.idega.formbuilder.presentation.beans;

import java.io.Serializable;

import com.idega.formbuilder.business.form.Document;
import com.idega.formbuilder.business.form.Page;

public class FormPage implements Serializable {
	
	private static final long serialVersionUID = -1462694198346788168L;
	
	private Document document;
	private Page page;
	private String id;
	
	public FormPage() {
		document = null;
		page = null;
		id = "";
	}
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	

}
