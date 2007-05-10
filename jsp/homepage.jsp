<?xml version="1.0"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:fb="http://xmlns.idega.com/com.idega.formbuilder"
	xmlns:ws="http://xmlns.idega.com/com.idega.workspace" version="1.2"
	xmlns:t="http://myfaces.apache.org/tomahawk">
	<jsp:directive.page contentType="text/html" />
	<f:view>
		<ws:page id="formbuilder" showFunctionMenu="false">
			<h:form id="workspaceform1">
				<fb:homePage id="fbHomePage" />
				<t:commandButton style="display: none" id="newFormButton" forceId="true" type="button" action="#{formDocument.createNewForm}" value="#{localizedStrings['com.idega.formbuilder']['toolbar_save']}"></t:commandButton>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>