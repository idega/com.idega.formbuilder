package com.idega.formbuilder.business.form.beans;

public interface IComponentPropertiesSelectParent extends IComponentPropertiesParent {
	
	public abstract void updateDataSrcUsed();
	
	public abstract void updateItemset();
	
	public abstract void updateEmptyElementLabel();
	
	public abstract void updateExternalDataSrc();
}