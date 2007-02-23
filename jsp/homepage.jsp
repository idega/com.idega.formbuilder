<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:wf="http://xmlns.idega.com/com.idega.webface"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:a4j="https://ajax4jsf.dev.java.net/ajax">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder"
				 showFunctionMenu="false"
				 javascripturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/modal-message.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/ajax-dynamic-content.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formbuilder.js,
								/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/forms-scroller.js">
			<h:form id="workspaceform1">
				<fb:homePage id="fbHomePage" />
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>