package com.idega.formbuilder.presentation.converters;

public class FormDocumentInfo {
	
	private String title;
	private String thankYouTitle;
	private String thankYouText;
	private boolean hasPreview;
	private boolean enableSwitcher;
	
	public boolean isEnableSwitcher() {
		return enableSwitcher;
	}
	public void setEnableSwitcher(boolean enableSwitcher) {
		this.enableSwitcher = enableSwitcher;
	}
	public boolean isHasPreview() {
		return hasPreview;
	}
	public void setHasPreview(boolean hasPreview) {
		this.hasPreview = hasPreview;
	}
	public String getThankYouText() {
		return thankYouText;
	}
	public void setThankYouText(String thankYouText) {
		this.thankYouText = thankYouText;
	}
	public String getThankYouTitle() {
		return thankYouTitle;
	}
	public void setThankYouTitle(String thankYouTitle) {
		this.thankYouTitle = thankYouTitle;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
