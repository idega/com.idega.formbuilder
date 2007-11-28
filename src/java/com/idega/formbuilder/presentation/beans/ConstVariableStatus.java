package com.idega.formbuilder.presentation.beans;

import java.util.ArrayList;
import java.util.List;


public class ConstVariableStatus {
	
	public static final String UNUSED = "unused";
	public static final String SINGLE = "single";
	public static final String MULTIPLE = "multiple";
	
	private static List<String> variable_status = new ArrayList<String>();
	
	private String status;
	
	static {
		variable_status.add(UNUSED);
		variable_status.add(SINGLE);
		variable_status.add(MULTIPLE);
	}
	
	public ConstVariableStatus(String status) {
		
		if(!variable_status.contains(status))
			throw new NullPointerException("Provided status not supported: " + status);
		
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "variable status: " + getStatus();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
