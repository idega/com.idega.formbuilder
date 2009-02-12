/**
 * 
 */
package com.idega.formbuilder.presentation.beans;

import org.jdom.Document;

/**
 * Bean for transfering Process tasks throuh DWR.
 * 
 * @author donatas
 *
 */
public class ProcessTasksBean {

	private Document document;
	
	private int taskFormCount;
	
	private String taskCount;

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public int getTaskFormCount() {
		return taskFormCount;
	}

	public void setTaskFormCount(int taskFormCount) {
		this.taskFormCount = taskFormCount;
	}

	public String getTaskCount() {
		return taskCount;
	}

	public void setTaskCount(String taskCount) {
		this.taskCount = taskCount;
	}
	
}
