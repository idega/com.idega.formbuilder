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
					javascripturls="/dwr/interface/FormDocument.js,
									/dwr/engine.js,
												
									/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/formshome.js,
									/idegaweb/bundles/com.idega.formbuilder.bundle/resources/javascript/smoothbox.js"
					stylesheeturls="/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/formshome.css,
									/idegaweb/bundles/com.idega.formbuilder.bundle/resources/style/smoothbox.css">
			<h:form id="workspaceform1" onsubmit="return false;">
				<f:verbatim>
                	<script type="text/javascript">
                		window.addEvent('domready', function() {
							var errorHanlder = function() {
								reloadPage();
							}
							DWREngine.setErrorHandler(errorHanlder);
						});
						window.addEvent('domready', registerFormsHomeActions);
                	</script>
                </f:verbatim>
				<fb:homePage />
				<t:div id="newFormDialog" forceId="true" styleClass="newFormDialogStyle" style="display: none;">
					<t:inputText id="newFormDialogInput" onkeydown="this.setProperty('value', this.value);" forceId="true" />
					<t:htmlTag value="a">
						<t:outputText forceId="true" id="createFormBtn" onclick="createNewForm($('newFormDialogInput').getText());" value="#{localizedStrings['com.idega.formbuilder']['fb_create_form']}" />
					</t:htmlTag>
				</t:div>
				<t:div styleClass="fbBottomButtonsContainer">
					<t:commandLink id="newFormButton" styleClass="rightButton" forceId="true" value="#{localizedStrings['com.idega.formbuilder']['toolbar_new']}" />
				</t:div>
			</h:form>
		</ws:page>
	</f:view>
</jsp:root>