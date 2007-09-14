<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" showFunctionMenu="false"
					javascripturls="/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/mootools/1.11/mootools-all.js,
									/idegaweb/bundles/com.idega.block.web2.0.bundle/resources/javascript/transcorners/Transcorners.js,
												
									/dwr/interface/FormDocument.js,
									/dwr/engine.js,
												
									/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/homepage.js"
					stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formshome.css">
			<h:form id="workspaceform1">
				<fb:homePage id="fbHomePage" />
				<t:commandButton style="display: none" id="newFormButton" onclick="alert('done');" forceId="true" type="button" action="#{formDocument.createNewForm}" value="#{localizedStrings['com.idega.formbuilder']['toolbar_save']}"></t:commandButton>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>